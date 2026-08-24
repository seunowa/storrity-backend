package com.storrity.storrity.stocktransfer.service;

import com.storrity.storrity.product.entity.Product;
import com.storrity.storrity.product.entity.ProductPackage;
import com.storrity.storrity.product.repository.ProductRepository;
import com.storrity.storrity.security.service.AuthenticatedUser;
import com.storrity.storrity.stockmovement.dto.StockMovementInstruction;
import com.storrity.storrity.stockmovement.dto.StockMovementInstructionItem;
import com.storrity.storrity.stockmovement.entity.PckQty;
import com.storrity.storrity.stockmovement.entity.StockMoevmentDirection;
import com.storrity.storrity.stockmovement.entity.StockMovementType;
import com.storrity.storrity.stockmovement.service.StockMovementService;
import com.storrity.storrity.store.entity.Store;
import com.storrity.storrity.store.service.StoreService;
import com.storrity.storrity.stocktransfer.dto.*;
import com.storrity.storrity.stocktransfer.entity.*;
import com.storrity.storrity.stocktransfer.repository.*;
import com.storrity.storrity.util.dto.CountDto;
import com.storrity.storrity.util.exception.BadRequestAppException;
import com.storrity.storrity.util.exception.ResourceNotFoundAppException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockTransferServiceImpl implements StockTransferService {

    private final StoreService storeService;
    private final ProductRepository productRepository;
    private final StockMovementService stockMovementService;
    private final StockTransferRepository repository;
    private final StockTransferItemRepository itemRepository;
    private final StockTransferReceivedItemRepository receivedItemRepository;
    private final StockTransferProcessSettingsService processSettingsService;

    public StockTransferServiceImpl(
            StoreService storeService,
            ProductRepository productRepository,
            StockMovementService stockMovementService,
            StockTransferRepository repository,
            StockTransferItemRepository itemRepository,
            StockTransferReceivedItemRepository receivedItemRepository,
            StockTransferProcessSettingsService processSettingsService) {
        this.storeService = storeService;
        this.productRepository = productRepository;
        this.stockMovementService = stockMovementService;
        this.repository = repository;
        this.itemRepository = itemRepository;
        this.receivedItemRepository = receivedItemRepository;
        this.processSettingsService = processSettingsService;
    }

    @Override
    public StockTransferDto fetch(UUID id) {
        return StockTransferDto.from(get(id));
    }

    @Override
    public List<StockTransferDto> list(StockTransferQueryParams params) {
        return repository.findAll(
                StockTransferSpecifications.from(params),
                Sort.by(Sort.Direction.DESC, "createdAt"))
            .stream()
            .map(StockTransferDto::from)
            .collect(Collectors.toList());
    }

    @Override
    public CountDto count(StockTransferQueryParams params) {
        return CountDto.builder()
            .count(repository.count(StockTransferSpecifications.from(params)))
            .build();
    }

    @Override
    @Transactional
    public StockTransferDto createDraft(StockTransferCreationDto dto) {
        validateStores(dto.getSourceStoreId(), dto.getDestinationStoreId());

        String username = username();
        Store source = storeService.fetch(dto.getSourceStoreId());
        Store destination = storeService.fetch(dto.getDestinationStoreId());

        StockTransferProcess process = Optional
            .ofNullable(processSettingsService.getStockTransferProcessSettings())
            .orElseGet(() -> StockTransferProcess.builder()
                .actions(new StockTransferProcessTemplate().getSimple())
                .build());

        StockTransfer transfer = StockTransfer.builder()
            .transactionRef(dto.getTransactionRef())
            .sourceStoreId(source.getId())
            .sourceStoreName(source.getName())
            .destinationStoreId(destination.getId())
            .destinationStoreName(destination.getName())
            .expectedTransferDate(dto.getExpectedTransferDate())
            .notes(dto.getNotes())
            .metadata(dto.getMetadata())
            .transferStatus(StockTransferStatus.DRAFT)
            .mostRecentTransferAction(StockTransferAction.DRAFT)
            .stockTransferProcess(process)
            .stockTransferTimeline(new StockTransferTimeline())
            .createdBy(username)
            .build();

        transfer.getStockTransferTimeline().appendEntry(
            timeline(StockTransferAction.DRAFT, username));

        StockTransfer saved = repository.save(transfer);
        replaceDraftItems(saved, dto.getItems());

        return StockTransferDto.from(repository.save(saved));
    }

    @Override
    @Transactional
    public StockTransferDto updateDraft(UUID id, StockTransferCreationDto dto) {
        StockTransfer transfer = lock(id);
        confirmActionIsAllowed(transfer, StockTransferAction.DRAFT);

        validateStores(transfer.getSourceStoreId(), dto.getDestinationStoreId());

        /*
         * A draft cannot change its source/destination after creation.
         * This keeps all already-created product references coherent.
         */
        if (!transfer.getSourceStoreId().equals(dto.getSourceStoreId())
                || !transfer.getDestinationStoreId().equals(dto.getDestinationStoreId())) {
            throw new BadRequestAppException(
                "Source and destination stores cannot be changed after draft creation.");
        }

        transfer.setTransactionRef(dto.getTransactionRef());
        transfer.setExpectedTransferDate(dto.getExpectedTransferDate());
        transfer.setNotes(dto.getNotes());
        transfer.setMetadata(dto.getMetadata());

        itemRepository.deleteByStockTransferId(id);
        replaceDraftItems(transfer, dto.getItems());

        transfer.getStockTransferTimeline().appendEntry(
            timeline(StockTransferAction.DRAFT, username()));

        return StockTransferDto.from(repository.save(transfer));
    }

    @Override
    @Transactional
    public StockTransferDto submitDraft(UUID id) {
        return advance(id, StockTransferAction.SUBMIT_DRAFT,
            StockTransferStatus.AWAITING_DRAFT_APPROVAL);
    }

    @Override
    @Transactional
    public StockTransferDto approveDraft(UUID id) {
        return advance(id, StockTransferAction.APPROVE_DRAFT,
            StockTransferStatus.DRAFT_APPROVED);
    }

    @Override
    @Transactional
    public StockTransferDto send(UUID id) {
        StockTransfer transfer = lock(id);
        confirmActionIsAllowed(transfer, StockTransferAction.SEND);

        List<StockTransferItem> items = itemRepository.findByStockTransferId(id);
        if (items.isEmpty()) {
            throw new BadRequestAppException("A stock transfer must contain at least one item.");
        }

        String username = username();
        LocalDateTime now = LocalDateTime.now();

        stockMovementService.create(
            buildMovement(
                items.stream()
                    .filter(x -> x.getQuantitySent() != null && x.getQuantitySent() > 0)
                    .map(this::outflowItem)
                    .collect(Collectors.toList()),
                "Stock transfer sent",
                transfer.getTransactionRef(),
                username));

        transfer.setSentAt(now);
        transfer.setSentBy(username);
        transfer.setMostRecentTransferAction(StockTransferAction.SEND);
        transfer.setTransferStatus(StockTransferStatus.SENT);
        transfer.getStockTransferTimeline().appendEntry(
            timeline(StockTransferAction.SEND, username));

        return StockTransferDto.from(repository.save(transfer));
    }

    @Override
    @Transactional
    public StockTransferDto submitReceipt(UUID id) {
        return advance(id, StockTransferAction.SUBMIT_RECEIPT,
            StockTransferStatus.AWAITING_RECEIPT_APPROVAL);
    }

    @Override
    @Transactional
    public StockTransferDto approveReceipt(UUID id) {
        return advance(id, StockTransferAction.APPROVE_RECEIPT,
            StockTransferStatus.RECEIPT_APPROVED);
    }

    @Override
    @Transactional
    public StockTransferDto receive(UUID id, StockTransferReceiveDto dto) {
        StockTransfer transfer = lock(id);
        confirmActionIsAllowed(transfer, StockTransferAction.RECEIVE);

        List<StockTransferItem> sentItems = itemRepository.findByStockTransferId(id);
        if (sentItems.isEmpty()) {
            throw new BadRequestAppException("A stock transfer must contain at least one item.");
        }

        List<StockTransferReceivedItem> received =
            buildReceivedItems(transfer, sentItems, dto);

        receivedItemRepository.deleteByStockTransferId(id);
        receivedItemRepository.saveAll(received);

        String username = username();

        /*
         * If the configured process contains SEND, inventory already left
         * the source store. RECEIVE only creates the destination inflow.
         *
         * If the configured process skips SEND (e.g. DRAFT -> RECEIVE),
         * create both movements here so inventory remains balanced.
         */
        boolean sentAlready =
            transfer.getMostRecentTransferAction() == StockTransferAction.SEND
            || transfer.getMostRecentTransferAction() == StockTransferAction.SUBMIT_RECEIPT
            || transfer.getMostRecentTransferAction() == StockTransferAction.APPROVE_RECEIPT;

        if (!sentAlready) {
            stockMovementService.create(
                buildMovement(
                    sentItems.stream()
                        .filter(x -> x.getQuantitySent() != null && x.getQuantitySent() > 0)
                        .map(this::outflowItem)
                        .collect(Collectors.toList()),
                    "Stock transfer sent as part of receipt",
                    transfer.getTransactionRef() + ":SEND",
                    username));
        }

        stockMovementService.create(
            buildMovement(
                received.stream()
                    .filter(x -> x.getQuantityReceived() != null && x.getQuantityReceived() > 0)
                    .map(this::inflowItem)
                    .collect(Collectors.toList()),
                "Stock transfer received",
                transfer.getTransactionRef() + ":RECEIVE",
                username));

        LocalDateTime now = LocalDateTime.now();
        transfer.setReceivedAt(now);
        transfer.setReceivedBy(username);
        transfer.setMostRecentTransferAction(StockTransferAction.RECEIVE);
        transfer.setTransferStatus(StockTransferStatus.RECEIVED);
        transfer.getStockTransferTimeline().appendEntry(
            timeline(StockTransferAction.RECEIVE, username));

        return StockTransferDto.from(repository.save(transfer));
    }

    @Override
    @Transactional
    public StockTransferDto cancel(UUID id) {
        StockTransfer transfer = lock(id);

        if (transfer.getMostRecentTransferAction() == StockTransferAction.RECEIVE
                || transfer.getTransferStatus() == StockTransferStatus.RECEIVED) {
            throw new BadRequestAppException(
                "A received stock transfer cannot be canceled.");
        }

        if (transfer.getMostRecentTransferAction() == StockTransferAction.CANCEL
                || transfer.getTransferStatus() == StockTransferStatus.CANCELED) {
            throw new BadRequestAppException("Stock transfer is already canceled.");
        }

        String username = username();

        /*
         * If inventory has already left the source, cancellation must put
         * it back. Otherwise canceling a sent transfer would permanently
         * reduce source inventory without a corresponding destination inflow.
         */
        if (hasSendOccurred(transfer)) {
            List<StockTransferItem> items =
                itemRepository.findByStockTransferId(id);

            stockMovementService.create(
                buildMovement(
                    items.stream()
                        .filter(x -> x.getQuantitySent() != null && x.getQuantitySent() > 0)
                        .map(this::cancelInflowItem)
                        .collect(Collectors.toList()),
                    "Stock transfer canceled - reverse source movement",
                    transfer.getTransactionRef() + ":CANCEL",
                    username));
        }

        transfer.setCanceledAt(LocalDateTime.now());
        transfer.setCanceledBy(username);
        transfer.setMostRecentTransferAction(StockTransferAction.CANCEL);
        transfer.setTransferStatus(StockTransferStatus.CANCELED);
        transfer.getStockTransferTimeline().appendEntry(
            timeline(StockTransferAction.CANCEL, username));

        return StockTransferDto.from(repository.save(transfer));
    }

    @Override
    @Transactional
    public StockTransferDto delete(UUID id) {
        StockTransfer transfer = lock(id);

        if (transfer.getTransferStatus() != StockTransferStatus.DRAFT) {
            throw new BadRequestAppException(
                "Delete cannot be performed while stock transfer status is "
                + transfer.getTransferStatus());
        }

        repository.delete(transfer);
        return StockTransferDto.from(transfer);
    }

    private StockTransferDto advance(
            UUID id,
            StockTransferAction action,
            StockTransferStatus status) {

        StockTransfer transfer = lock(id);
        confirmActionIsAllowed(transfer, action);

        String username = username();
        LocalDateTime now = LocalDateTime.now();

        switch (action) {
            case SUBMIT_DRAFT -> {
                transfer.setDraftSubmittedAt(now);
                transfer.setDraftSubmittedBy(username);
            }
            case APPROVE_DRAFT -> {
                transfer.setDraftApprovedAt(now);
                transfer.setDraftApprovedBy(username);
            }
            case SUBMIT_RECEIPT -> {
                transfer.setReceiptSubmittedAt(now);
                transfer.setReceiptSubmittedBy(username);
            }
            case APPROVE_RECEIPT -> {
                transfer.setReceiptApprovedAt(now);
                transfer.setReceiptApprovedBy(username);
            }
            default -> { }
        }

        transfer.setMostRecentTransferAction(action);
        transfer.setTransferStatus(status);
        transfer.getStockTransferTimeline().appendEntry(
            timeline(action, username));

        return StockTransferDto.from(repository.save(transfer));
    }

    private void replaceDraftItems(
            StockTransfer transfer,
            List<StockTransferItemCreationDto> requestedItems) {

        if (requestedItems == null || requestedItems.isEmpty()) {
            throw new BadRequestAppException(
                "A stock transfer draft must contain at least one item.");
        }

        Map<String, Product> products = new HashMap<>();

        List<StockTransferItem> items = requestedItems.stream().map(dto -> {
            if (dto.getProductCode() == null || dto.getProductCode().isBlank()) {
                throw new BadRequestAppException("Product code is required.");
            }

            Product product = findProductByCodeAndStore(
                dto.getProductCode(), transfer.getSourceStoreId());

            if (products.putIfAbsent(dto.getProductCode(), product) != null) {
                throw new BadRequestAppException(
                    "Product code appears more than once in the transfer: "
                    + dto.getProductCode());
            }

            double quantity = computeQtyInBaseUnit(
                dto.getPckQty(), product);

            return StockTransferItem.builder()
                .stockTransferId(transfer.getId())
                .productId(product.getId())
                .productName(product.getName())
                .productCode(product.getCode())
                .productCategory(product.getCategory())
                .productSubCategory(product.getSubcategory())
                .storeId(product.getStore().getId())
                .storeName(product.getStore().getName())
                .batchNumber(dto.getBatchNumber())
                .expiryDate(dto.getExpiryDate())
                .quantitySent(quantity)
                .baseUnit(product.getStockKeepingUnit())
                .pckQty(dto.getPckQty())
                .metadata(dto.getMetadata())
                .build();
        }).collect(Collectors.toList());

        itemRepository.saveAll(items);
    }

    private List<StockTransferReceivedItem> buildReceivedItems(
            StockTransfer transfer,
            List<StockTransferItem> sentItems,
            StockTransferReceiveDto dto) {

        if (dto == null || dto.getItems() == null) {
            throw new BadRequestAppException("Receipt items are required.");
        }

        Map<UUID, StockTransferItem> sentById = sentItems.stream()
            .collect(Collectors.toMap(StockTransferItem::getId, Function.identity()));

        Map<UUID, StockTransferReceivedItemDto> requestByItemId =
            dto.getItems().stream().collect(Collectors.toMap(
                StockTransferReceivedItemDto::getStockTransferItemId,
                Function.identity(),
                (a, b) -> {
                    throw new BadRequestAppException(
                        "Duplicate received item for stock transfer item.");
                }));

        if (!requestByItemId.keySet().containsAll(sentById.keySet())) {
            throw new BadRequestAppException(
                "Receipt must contain an entry for every transferred item.");
        }

        return sentItems.stream().map(sent -> {
            StockTransferReceivedItemDto request =
                requestByItemId.get(sent.getId());

            String productCode =
                request.getProductCode() == null
                    ? sent.getProductCode()
                    : request.getProductCode();

            if (!sent.getProductCode().equals(productCode)) {
                throw new BadRequestAppException(
                    "Product code cannot be changed during receipt for item "
                    + sent.getId());
            }

            Product destinationProduct =
                findProductByCodeAndStore(
                    productCode, transfer.getDestinationStoreId());

            Double receivedQuantity = request.getQuantityReceived();

            if (request.getPckQty() != null && !request.getPckQty().isEmpty()) {
                receivedQuantity =
                    computeQtyInBaseUnit(request.getPckQty(), destinationProduct);
            }

            if (receivedQuantity == null) {
                receivedQuantity = 0d;
            }

            if (receivedQuantity < 0) {
                throw new BadRequestAppException(
                    "Quantity received cannot be negative.");
            }

            return StockTransferReceivedItem.builder()
                .stockTransferId(transfer.getId())
                .stockTransferItemId(sent.getId())
                .productId(destinationProduct.getId())
                .productName(destinationProduct.getName())
                .productCode(destinationProduct.getCode())
                .productCategory(destinationProduct.getCategory())
                .productSubCategory(destinationProduct.getSubcategory())
                .storeId(destinationProduct.getStore().getId())
                .storeName(destinationProduct.getStore().getName())
                .batchNumber(request.getBatchNumber() == null
                    ? sent.getBatchNumber() : request.getBatchNumber())
                .expiryDate(request.getExpiryDate() == null
                    ? sent.getExpiryDate() : request.getExpiryDate())
                .quantityReceived(receivedQuantity)
                .quantityVariance(receivedQuantity - sent.getQuantitySent())
                .baseUnit(destinationProduct.getStockKeepingUnit())
                .pckQty(request.getPckQty())
                .costPrice(sent.getCostPrice())
                .stockStatus(request.getStockStatus())
                .metadata(request.getMetadata())
                .build();
        }).collect(Collectors.toList());
    }

    private Product findProductByCodeAndStore(String code, UUID storeId) {
        return productRepository.findByCodeAndStoreId(code, storeId)
            .orElseThrow(() -> new ResourceNotFoundAppException(
                "Product with code '" + code
                + "' was not found in store " + storeId));
    }

    private double computeQtyInBaseUnit(
            List<PckQty> pckQty,
            Product product) {

        if (pckQty == null || pckQty.isEmpty()) {
            throw new BadRequestAppException(
                "Package quantities are required for product "
                + product.getCode());
        }

        if (product.getPackages() == null || product.getPackages().isEmpty()) {
            throw new BadRequestAppException(
                "Product " + product.getCode()
                + " has no package definitions.");
        }

        Map<String, ProductPackage> packageMap =
            product.getPackages().stream().collect(Collectors.toMap(
                ProductPackage::getName,
                Function.identity(),
                (a, b) -> a));

        return pckQty.stream().mapToDouble(x -> {
            ProductPackage p = packageMap.get(x.getPackageName());

            if (p == null) {
                throw new ResourceNotFoundAppException(
                    "Package with name '" + x.getPackageName()
                    + "' not found for product " + product.getCode());
            }

            if (x.getQuantity() == null || x.getQuantity() < 0) {
                throw new BadRequestAppException(
                    "Invalid package quantity for product " + product.getCode());
            }

            return p.getMultiplier() * x.getQuantity();
        }).sum();
    }

    private StockMovementInstruction buildMovement(
            List<StockMovementInstructionItem> items,
            String description,
            String transactionRef,
            String username) {

        return StockMovementInstruction.builder()
            .description(description)
            .instructionItems(items)
            .performedBy(username)
            .transactionRef(transactionRef)
            .movementType(StockMovementType.STOCK_TRANSFER)
            .build();
    }

    private StockMovementInstructionItem outflowItem(
            StockTransferItem item) {
        return StockMovementInstructionItem.builder()
            .flow(StockMoevmentDirection.OUTFLOW)
            .quantity(item.getQuantitySent())
            .pckQty(item.getPckQty() == null ? null : new ArrayList<>(item.getPckQty()))
            .productId(item.getProductId())
            .batchNumber(item.getBatchNumber())
            .expiryDate(item.getExpiryDate())
            .build();
    }

    private StockMovementInstructionItem cancelInflowItem(
            StockTransferItem item) {
        return StockMovementInstructionItem.builder()
            .flow(StockMoevmentDirection.INFLOW)
            .quantity(item.getQuantitySent())
            .pckQty(item.getPckQty() == null ? null : new ArrayList<>(item.getPckQty()))
            .productId(item.getProductId())
            .batchNumber(item.getBatchNumber())
            .expiryDate(item.getExpiryDate())
            .build();
    }

    private StockMovementInstructionItem inflowItem(
            StockTransferReceivedItem item) {
        return StockMovementInstructionItem.builder()
            .flow(StockMoevmentDirection.INFLOW)
            .quantity(item.getQuantityReceived())
            .pckQty(item.getPckQty() == null ? null : new ArrayList<>(item.getPckQty()))
            .productId(item.getProductId())
            .batchNumber(item.getBatchNumber())
            .expiryDate(item.getExpiryDate())
            .build();
    }

    private boolean hasSendOccurred(StockTransfer transfer) {
        return transfer.getSentAt() != null
            || transfer.getMostRecentTransferAction() == StockTransferAction.SEND
            || transfer.getMostRecentTransferAction() == StockTransferAction.SUBMIT_RECEIPT
            || transfer.getMostRecentTransferAction() == StockTransferAction.APPROVE_RECEIPT;
    }

    private void confirmActionIsAllowed(
            StockTransfer transfer,
            StockTransferAction action) {

        StockTransferAction current =
            transfer.getMostRecentTransferAction();

        if (action == StockTransferAction.CANCEL) {
            if (current == StockTransferAction.RECEIVE) {
                throw new BadRequestAppException(
                    "A received stock transfer cannot be canceled.");
            }
            return;
        }

        if (current == null) {
            if (action != StockTransferAction.DRAFT) {
                throw new BadRequestAppException(
                    "The first action must be DRAFT.");
            }
            return;
        }

        if (current == StockTransferAction.RECEIVE
                || current == StockTransferAction.CANCEL) {
            throw new BadRequestAppException(
                "No further action is allowed while transfer is "
                + transfer.getTransferStatus());
        }

        if (current == StockTransferAction.DRAFT
                && action == StockTransferAction.DRAFT) {
            return;
        }

        List<StockTransferAction> sequence =
            transfer.getStockTransferProcess().getActions();

        int index = sequence.indexOf(current);

        if (index < 0) {
            throw new BadRequestAppException(
                "Action " + current
                + " is not part of the configured stock transfer process.");
        }

        if (index + 1 >= sequence.size()) {
            throw new BadRequestAppException(
                "No further action is available for this transfer.");
        }

        StockTransferAction expected = sequence.get(index + 1);

        if (action != expected) {
            throw new BadRequestAppException(
                "Action " + action + " cannot be performed while transfer is "
                + transfer.getTransferStatus()
                + ". Expected action: " + expected);
        }
    }

    private StockTransfer lock(UUID id) {
        return repository.findByIdForUpdate(id)
            .orElseThrow(() ->
                new ResourceNotFoundAppException(
                    "Stock transfer not found with id: " + id));
    }

    private StockTransfer get(UUID id) {
        return repository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundAppException(
                    "Stock transfer not found with id: " + id));
    }

    private String username() {
        AuthenticatedUser principal =
            (AuthenticatedUser) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return principal.getUsername();
    }

    private StockTransferTimelineEntry timeline(
            StockTransferAction action,
            String username) {
        return StockTransferTimelineEntry.builder()
            .action(action)
            .performedAt(LocalDateTime.now())
            .performedBy(username)
            .build();
    }

    private void validateStores(UUID source, UUID destination) {
        if (source == null || destination == null) {
            throw new BadRequestAppException(
                "Source and destination stores are required.");
        }

        if (source.equals(destination)) {
            throw new BadRequestAppException(
                "Source and destination stores must be different.");
        }
    }
}
