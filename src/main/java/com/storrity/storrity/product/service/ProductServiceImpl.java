/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.product.service;

import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.product.dto.BatchProductCreationDto;
import com.storrity.storrity.product.dto.ProductCreationDto;
import com.storrity.storrity.product.dto.ProductDto;
import com.storrity.storrity.product.dto.ProductImportResultDto;
import com.storrity.storrity.product.dto.ProductPackageDto;
import com.storrity.storrity.product.dto.ProductUpdateDto;
import com.storrity.storrity.product.entity.Product;
import com.storrity.storrity.product.entity.ProductPackage;
import com.storrity.storrity.product.entity.ProductQueryParams;
import com.storrity.storrity.product.event.ProductCreatedEvent;
import com.storrity.storrity.product.event.ProductDeletedEvent;
import com.storrity.storrity.product.event.ProductUpdatedEvent;
import com.storrity.storrity.product.repository.ProductPackageRepository;
import com.storrity.storrity.product.repository.ProductRepository;
import com.storrity.storrity.store.entity.Store;
import com.storrity.storrity.store.service.StoreService;
import com.storrity.storrity.util.csv.CsvContext;
import com.storrity.storrity.util.csv.CsvUtils;
import com.storrity.storrity.util.dto.CountDto;
import com.storrity.storrity.util.exception.DataExportAppException;
import com.storrity.storrity.util.exception.DataImportAppException;
import com.storrity.storrity.util.exception.InputValidationAppException;
import com.storrity.storrity.util.exception.ResourceNotFoundAppException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Seun Owa
 */
@Service
public class ProductServiceImpl implements ProductService{
    
    private final StoreService storeService;
    private final ProductRepository productRepository;
    private final ProductPackageRepository productPackageRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ProductCreator productCreator;
    private final ProductRowImportService productRowImportService;

    @Autowired
    public ProductServiceImpl(StoreService storeService, ProductRepository productRepository
            , ProductPackageRepository productPackageRepository, ApplicationEventPublisher eventPublisher
            , ProductCreator productCreator, ProductRowImportService productRowImportService) {
        this.storeService = storeService;
        this.productRepository = productRepository;
        this.productPackageRepository = productPackageRepository;
        this.eventPublisher = eventPublisher;
        this.productCreator = productCreator;
        this.productRowImportService = productRowImportService;
    }

    @Transactional
    @Override
    public ProductDto create(ProductCreationDto dto) {        
        return productCreator.create(dto);
    }


    @Override
    public ProductDto fetch(UUID id) {
        Product p = productRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundAppException("Product not found with id: " + id)); 
        return ProductDto.from(p);
    }

    @Override
    public List<ProductDto> list(ProductQueryParams params) {
        List<Product> products = productRepository.list(params);
        
        return products.stream()
                .map((p)-> ProductDto.from(p) )
                .collect(Collectors.toList());        
    }

    @Override
    public CountDto count(ProductQueryParams params) {
        return CountDto
                .builder()
                .count(productRepository.countRecords(params))
                .build();        
    }

    @Override
    @Transactional
    public ProductDto update(UUID id, ProductUpdateDto dto) {
        
        
        
        
        Product p = productRepository.findByIdForUpdate(id)
                .orElseThrow(()->new ResourceNotFoundAppException("Product not found with id: " + id));       
        
        if(dto.getCategory()!= null){
            p.setCategory(dto.getCategory());
        }
        
        if(dto.getCode()!= null){
            p.setCode(dto.getCode());
        }
        
        if(dto.getName()!= null){
            p.setName(dto.getName());
        }
        
        if(dto.getStockKeepingUnit()!= null){
            dto.getPackages()
            .stream()
            .filter(prod -> prod.getName().equalsIgnoreCase(dto.getStockKeepingUnit()))
            .findFirst()
            .orElseThrow(() -> new InputValidationAppException("No matching package found for SKU: " + dto.getStockKeepingUnit()));
            
            p.setStockKeepingUnit(dto.getStockKeepingUnit());
        }
        
        if(dto.getSubcategory()!= null){
            p.setSubcategory(dto.getSubcategory());
        }
        
        if(dto.getUnitPrice()!= null){
            p.setUnitPrice(dto.getUnitPrice());
            Double qtyInStock = p.getQtyInStock() == null ? 0d : p.getQtyInStock();
            Money inventoryValue = p.getUnitPrice().multiply(qtyInStock);
            p.setInventoryValue(inventoryValue);
        }
        
        if(dto.getBrand()!= null){
            p.setBrand(dto.getBrand());
        }
        
        if(dto.getDescription()!= null){
            p.setDescription(dto.getDescription());
        }
        
        if(dto.getBarCode()!= null){
            p.setBarCode(dto.getBarCode());
        }
        
        if(dto.getLocation()!= null){
            p.setLocation(dto.getLocation());
        }
        
        if(dto.getMinimumStockLevel()!= null){
            p.setMinimumStockLevel(dto.getMinimumStockLevel());
        }
        
        if(dto.getReorderLevel()!= null){
            p.setReorderLevel(dto.getReorderLevel());
        }
        
        if(dto.getReorderQuantity()!= null){
            p.setReorderQuantity(dto.getReorderQuantity());
        }
        
        if(dto.getMaximumStockLevel()!= null){
            p.setMaximumStockLevel(dto.getMaximumStockLevel());
        }
        
        Product savedProduct = productRepository.save(p);
        
        if(dto.getPackages()!= null){
            List<UUID> prevPksIds = p.getPackages().stream()
                    .map((pk)->pk.getId()).collect(Collectors.toList());
            productPackageRepository.deleteAllById(prevPksIds);
            
            List<ProductPackage> prodPackages = dto.getPackages()
                .stream()
                .map((pkg)-> ProductPackage.builder()
                    .name(pkg.getName())
                    .multiplier(pkg.getMultiplier())
                    .productId(savedProduct.getId())
                    .build())
                .collect(Collectors.toList());
            
            List<ProductPackage> savedProdPackages = productPackageRepository.saveAll(prodPackages);
            savedProduct.setPackages(savedProdPackages);
        }
        
        if(dto.getProductType() != null){
            p.setProductType(dto.getProductType());
        }
        
        eventPublisher.publishEvent(new ProductUpdatedEvent(savedProduct, p));
        return ProductDto.from(p);
    }

    @Override
    @Transactional
    public ProductDto delete(UUID id) {        
        Product p = productRepository.findByIdForUpdate(id)
                .orElseThrow(()->new ResourceNotFoundAppException("Product not found with id: " + id));
        productRepository.delete(p);
        eventPublisher.publishEvent(new ProductDeletedEvent(p));
        return ProductDto.from(p);
    }

    
    @Override
    public List<String> listProductNames(String query, Integer limit, UUID storeId) {
        return productRepository.listProductNames(query, limit, storeId);
    }
    
    @Override
    public List<String> listCategories(String query, Integer limit, UUID storeId) {
        return productRepository.listCategories(query, limit, storeId);
    }

    @Override
    public List<String> listSubcategories(String query, Integer limit, UUID storeId) {
        return productRepository.listSubcategories(query, limit, storeId);
    }

    @Override
    public List<String> listBrands(String query, Integer limit, UUID storeId) {
        return productRepository.listBrands(query, limit, storeId);
    }

    @Override
    public List<String> listStockKeepingUnits(String query, Integer limit, UUID storeId) {
        return productRepository.listStockKeepingUnits(query, limit, storeId);
    }

    @Override
    public List<String> listPackages(String query, Integer limit, UUID storeId) {
        return productPackageRepository.findPackages(query, limit, storeId);
    }

    @Transactional
    @Override
    public List<ProductDto> create(BatchProductCreationDto dto) {

        List<ProductCreationDto> productDtos = dto.getProducts();

        // 1. Validate all up front — fail fast before touching the DB at all.
        productDtos.forEach(this::validatePackageMatchesSku);

        // 2. All products in a batch share one store — resolve it once.
        UUID storeId = productDtos.get(0).getStoreId();
        boolean singleStore = productDtos.stream()
                .allMatch(p -> Objects.equals(p.getStoreId(), storeId));
        if (!singleStore) {
            throw new InputValidationAppException("All products in a batch must belong to the same store");
        }
        Store store = storeService.fetch(storeId);

        // 3. Build and bulk-insert products.
        List<Product> newProducts = productDtos.stream()
                .map(p -> buildProduct(p, store))
                .collect(Collectors.toList());

        List<Product> savedProducts = productRepository.saveAll(newProducts);

        // 4. Build all packages for all products, then bulk-insert in one call.
        List<ProductPackage> allPackages = new ArrayList<>();
        for (int i = 0; i < savedProducts.size(); i++) {
            allPackages.addAll(buildPackages(productDtos.get(i).getPackages(), savedProducts.get(i).getId()));
        }

        List<ProductPackage> savedPackages = productPackageRepository.saveAll(allPackages);

        // 5. Re-associate saved packages to their owning product via productId —
        //    don't rely on saveAll preserving input order.
        Map<UUID, List<ProductPackage>> savedPackagesByProductId = savedPackages.stream()
                .collect(Collectors.groupingBy(ProductPackage::getProductId));

        List<ProductDto> result = new ArrayList<>(savedProducts.size());
        for (Product savedProduct : savedProducts) {
            savedProduct.setPackages(savedPackagesByProductId.getOrDefault(savedProduct.getId(), List.of()));
            eventPublisher.publishEvent(new ProductCreatedEvent(savedProduct));
            result.add(ProductDto.from(savedProduct));
        }

        return result;
    }

    private void validatePackageMatchesSku(ProductCreationDto dto) {
        dto.getPackages().stream()
                .filter(p -> p.getName().equalsIgnoreCase(dto.getStockKeepingUnit()))
                .findFirst()
                .orElseThrow(() -> new InputValidationAppException(
                        "No matching package found for SKU: " + dto.getStockKeepingUnit()));
    }

    private Product buildProduct(ProductCreationDto dto, Store store) {
        return Product.builder()
                .category(dto.getCategory())
                .code(dto.getCode())
                .name(dto.getName())
                .stockKeepingUnit(dto.getStockKeepingUnit())
                .qtyInStock(0d)
                .store(store)
                .subcategory(dto.getSubcategory())
                .unitPrice(dto.getUnitPrice())
                .brand(dto.getBrand())
                .description(dto.getDescription())
                .barCode(dto.getBarCode())
                .location(dto.getLocation())
                .reorderLevel(dto.getReorderLevel())
                .reorderQuantity(dto.getReorderQuantity())
                .build();
    }

    private List<ProductPackage> buildPackages(List<ProductPackageDto> packageDtos, UUID productId) {
        return packageDtos.stream()
                .map(pkg -> ProductPackage.builder()
                        .name(pkg.getName())
                        .multiplier(pkg.getMultiplier())
                        .productId(productId)
                        .sellingPrice(pkg.getSellingPrice())
                        .build())
                .collect(Collectors.toList());
    }
    
    @Override
    public ProductImportResultDto importProducts(InputStream csvInputStream, UUID storeId) {
        List<String[]> rows;
        try {
            rows = CsvUtils.readAll(csvInputStream);
        } catch (IOException e) {
            throw new DataImportAppException("Could not read CSV file: " + e.getMessage(), e);
        }
 
        if (rows.isEmpty()) {
            throw new DataImportAppException("CSV file is empty");
        }
 
        ProductCsvRowMapper rowMapper = new ProductCsvRowMapper();
        Map<String, Integer> headerIndex;
        try {
            headerIndex = rowMapper.indexHeader(rows.get(0));
        } catch (RuntimeException e) {
            throw new DataImportAppException(e.getMessage(), e);
        }
 
        ProductImportResultDto result = new ProductImportResultDto();
        result.setTotalRows(rows.size() - 1);
 
        CsvContext csvContext = CsvContext.builder().storeId(storeId).build();
        int success = 0;
        for (int i = 1; i < rows.size(); i++) {
            int lineNumber = i + 1; // 1-based, includes header, matches what a user sees in Excel
            try {
                ProductCreationDto dto = rowMapper.fromCsvRow(headerIndex, rows.get(i), csvContext);
                productRowImportService.importRow(dto);
                success++;
            } catch (Exception e) {
                result.getErrors().add(
                        new ProductImportResultDto.RowError(lineNumber, e.getMessage()));
            }
        }
 
        result.setSuccessCount(success);
        result.setFailureCount(result.getErrors().size());
        return result;
    }
    
    @Override
    public byte[] exportProducts(ProductQueryParams params) {
        List<Product> products = productRepository.list(params);
 
        ProductCsvRowMapper rowMapper = new ProductCsvRowMapper();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (CsvUtils.RowWriter writer = new CsvUtils.RowWriter(buffer)) {
            writer.writeRow(rowMapper.headers());
            for (Product p : products) {
                writer.writeRow(rowMapper.toCsvRow(p));
            }
        } catch (IOException e) {
            throw new DataExportAppException("Could not generate product export: " + e.getMessage(), e);
        }
 
        return buffer.toByteArray();
    }
}
