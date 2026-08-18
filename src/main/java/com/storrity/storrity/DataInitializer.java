/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity;

import com.storrity.storrity.cashaccounts.dto.CashAccountCreationDto;
import com.storrity.storrity.cashaccounts.entity.CashAccount;
import com.storrity.storrity.cashaccounts.entity.CashAccountQueryParams;
import com.storrity.storrity.cashaccounts.entity.CashAccountStatus;
import com.storrity.storrity.cashaccounts.entity.CashAccountType;
import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.cashaccounts.service.CashAccountService;
import com.storrity.storrity.license.dto.LicenseDto;
import com.storrity.storrity.license.service.LicenseService;
import com.storrity.storrity.product.entity.Product;
import com.storrity.storrity.product.entity.ProductPackage;
import com.storrity.storrity.supply.entity.SupplyStatus;
import com.storrity.storrity.product.repository.ProductPackageRepository;
import com.storrity.storrity.product.repository.ProductRepository;
import com.storrity.storrity.sales.dto.SaleCreationDto;
import com.storrity.storrity.sales.dto.SalesCreationDto;
import com.storrity.storrity.sales.entity.PckQtyWithSellinPrice;
import com.storrity.storrity.stockmovement.entity.PckQty;
import com.storrity.storrity.store.entity.Store;
import com.storrity.storrity.store.entity.StoreStatus;
import com.storrity.storrity.store.repository.StoreRepository;
import com.storrity.storrity.supply.dto.SupplyCreationDtoStale;
import com.storrity.storrity.supply.dto.SupplyItemCreationDtoStale;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.storrity.storrity.sales.service.SalesService;
import com.storrity.storrity.security.dto.UserCreationDto;
import com.storrity.storrity.security.entity.UserPermission;
import com.storrity.storrity.security.entity.UserRole;
import com.storrity.storrity.security.repository.UserRepository;
import com.storrity.storrity.security.repository.UserRoleRepository;
import com.storrity.storrity.security.service.AppUserService;
import com.storrity.storrity.security.service.AuthenticatedUser;
import com.storrity.storrity.supply.dto.DeliveryDto;
import com.storrity.storrity.supply.dto.DeliveryItemDto;
import com.storrity.storrity.supply.dto.PurchaseOrderItemCreationDto;
import com.storrity.storrity.supply.dto.PurchaseOrderCreationDto;
import com.storrity.storrity.supply.dto.SupplyDto;
import com.storrity.storrity.supply.service.SupplyService;
import org.springframework.context.annotation.Profile;
import java.util.UUID;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Seun Owa
 */
@Component
@Profile("dev")
public class DataInitializer implements CommandLineRunner{
    
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final ProductPackageRepository productPackageRepository;
    private final SupplyService supplyService;
    private final SalesService SaleService;
    private final CashAccountService cashAccountService;    
    private final UserRepository userRepo;
    private final UserRoleRepository roleRepo;
    private final AppUserService appUserService;
    private final LicenseService licenseService;

    @Autowired
    public DataInitializer(StoreRepository storeRepository, ProductRepository productRepository
            , ProductPackageRepository productPackageRepository, SupplyService supplyService
            , SalesService SaleService, CashAccountService cashAccountService
            , UserRepository userRepo, UserRoleRepository roleRepo, AppUserService appUserService
            , LicenseService licenseService) {
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
        this.productPackageRepository = productPackageRepository;
        this.supplyService = supplyService;
        this.SaleService = SaleService;
        this.cashAccountService = cashAccountService;
        this.userRepo = userRepo;
        this.appUserService = appUserService;
        this.roleRepo = roleRepo;
        this.licenseService = licenseService;
    }
    
    @Override
    public void run(String... args) throws Exception {
//        seedAdmin();        
        installDevLicense();
        List<Store> stores = storeRepository.findAll();
        if(stores.isEmpty()){
            runAsDevUser(this::init);
        }
    }
    
    private void runAsDevUser(Runnable action) {

        AuthenticatedUser devUser = new AuthenticatedUser(
                "dev-user",
                "dev-client",
                "Development Client"
        );

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        devUser,
                        null,
                        null
                );

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        try {
            action.run();
        } finally {
            SecurityContextHolder.clearContext();
        }
    }
    
//    public void seedAdmin() {
//        if (userRepo.findByUsername("admin").isPresent()) return;
//
//        // Create admin role with all permissions
//        Set<UserPermission> allPerms = EnumSet.allOf(UserPermission.class);
//        UserRole adminRole = new UserRole();
//        adminRole.setId("ADMIN");
//        adminRole.setPermissions(allPerms);
//        roleRepo.save(adminRole);
//
//        String password = "password123";
//        // Create admin user
////        AppUser admin = new AppUser();
////        admin.setUsername("admin");
////        admin.setPassword(encoder.encode(password));
////        admin.setRole(adminRole);
////        userRepo.save(admin);
//        UserCreationDto dto = new UserCreationDto();
//        dto.setUsername("admin@admin.com");
//        dto.setPassword(password);
//        dto.setRole(adminRole.getId());
//        appUserService.create(dto);
//
//        System.out.println("✅ Seeded admin user with username=admin and password=" + password);
//    }
    
    public void init() {
        Store s = Store.builder()
                .city("Ibadan")
                .email("store@store.com")
                .managerAddress("Manager Address")
                .managerEmail("manager@email.com")
                .managerName("Manager Name")
                .managerPhone("09067893930")
                .name("Sample Store")
                .phone("080466585885")
                .state("Oyo")
                .street("Dugbe")
                .status(StoreStatus.OPEN)
                .build();
        Store savedStore = storeRepository.save(s);
        
        Store s2 = Store.builder()
                .city("Lagos")
                .email("store2@store.com")
                .managerAddress("Manager Address 2")
                .managerEmail("manager2@email.com")
                .managerName("Manager Name 2")
                .managerPhone("08067893930")
                .name("Sample Store 2")
                .phone("070466585885")
                .state("Lagos")
                .street("Ikeja")
                .status(StoreStatus.OPEN)
                .build();
        Store savedStore2 = storeRepository.save(s2);
        
        Product p = Product.builder()
                .category("Category X")
                .code("12345")
                .name("Sample Product")
                .stockKeepingUnit("Small")
                .qtyInStock(50.0)
                .store(savedStore)
                .subcategory("subcategory Y")
                .unitPrice(new Money(1000000))
                .build();
        
        Product savedProduct = productRepository.save(p);
        
        Product p2 = Product.builder()
                .category("Category B")
                .code("12345")
                .name("Sound System")
                .stockKeepingUnit("Piece")
                .qtyInStock(50.0)
                .store(savedStore)
                .subcategory("subcategory A")
                .unitPrice(new Money(2000000))
                .build();
        
        Product savedProduct2 = productRepository.save(p2);

        // Add 3 sample packages
        ProductPackage smallPack = ProductPackage.builder()
                .name("Small")
                .multiplier(1.0)
                .productId(savedProduct.getId())
                .sellingPrice(new Money(1000000))
                .build();

        ProductPackage mediumPack = ProductPackage.builder()
                .name("Medium")
                .multiplier(2.0)
                .productId(savedProduct.getId())
                .sellingPrice(new Money(2000000))
                .build();

        ProductPackage largePack = ProductPackage.builder()
                .name("Large")
                .multiplier(5.0)
                .productId(savedProduct.getId())
                .sellingPrice(new Money(5000000))
                .build();

        // Save packages (assuming you have a ProductPackageRepository)
        productPackageRepository.saveAll(List.of(smallPack, mediumPack, largePack));
        
        

        // Add 3 sample packages
        ProductPackage piecePk = ProductPackage.builder()
                .name("Piece")
                .multiplier(1.0)
                .productId(savedProduct2.getId())
                .sellingPrice(new Money(2000000))
                .build();

        ProductPackage doublePck = ProductPackage.builder()
                .name("Double Comb")
                .multiplier(2.0)
                .productId(savedProduct2.getId())
                .sellingPrice(new Money(4000000))
                .build();

        // Save packages (assuming you have a ProductPackageRepository)
        productPackageRepository.saveAll(List.of(piecePk, doublePck));
        
//        SupplyCreationDtoStale scDto = SupplyCreationDtoStale.builder()
//                .amountPaid(new Money(50))
//                .approvedByUserId("approved by")
//                .contactPerson("contact person")
//                .deliveryFee(new Money (2))
//                .deliveryNoteNumber("delivery note number")
//                .enteredByUserId("entered by")
//                .invoiceNumber("invoice number")
//                .items(List.of(SupplyItemCreationDtoStale.builder()
//                        .batchNumber("batch123")
//                        
//                        .expiryDate(LocalDate.now())
//                        .pckQty(List.of(PckQty.builder()
//                                .packageName("Small")
//                                .quantity(5d)
//                                .build()))
//                        .productId(savedProduct.getId())
//                        .costPrice(new Money(500))
//                        .build()))
//                .notes("notes")
//                .paymentMethod("Card")
//                .storeId(savedStore.getId())
//                .supplierEmail("supplier@email.com")
//                .supplierId("supplier id")
//                .supplierName("supplier Name")
//                .supplierPhone("suppier phone")
//                .expectedSupplyDate(LocalDate.now())
//                .transactionRef("supplyref101")
//                .build();
//        
//        SupplyDtoStale supplyDto = supplyService.create(scDto);
//        supplyService.updateStatus(supplyDto.getId(), SupplyStatusUpdateDtoStale.builder().supplyStatus(SupplyStatus.RECEIVED).build());


        // 1. CREATE DRAFT
        PurchaseOrderCreationDto supplyCreationDto = PurchaseOrderCreationDto.builder()
                .transactionRef("supplyref101")
                .storeId(savedStore.getId())
                .expectedSupplyDate(LocalDate.now())
                .deliveryNoteNumber("delivery-note-101")
                .invoiceNumber("invoice-101")
                .supplierId("supplier-id-001")
                .supplierName("Sample Supplier")
                .contactPerson("Contact Person")
                .supplierPhone("08012345678")
                .supplierEmail("supplier@email.com")
                .notes("Sample development supply")
                .purchaseOrderItems(List.of(PurchaseOrderItemCreationDto.builder()
                                .productId(savedProduct.getId())
                                .pckQty(List.of(
                                        PckQty.builder()
                                                .packageName("Small")
                                                .quantity(5d)
                                                .build()
                                ))
                                .costPrice(new Money(500))
                                .build()
                ))
                .build();

        SupplyDto supplyDto = supplyService.createDraft(supplyCreationDto);


        // 2. DELIVER
        UUID orderItemId = supplyDto.getOrderItems()
                .iterator()
                .next()
                .getId();

        DeliveryItemDto deliveryItem = new DeliveryItemDto();
        deliveryItem.setOrderItemId(orderItemId);
        deliveryItem.setProductId(savedProduct.getId());
        deliveryItem.setQuantityReceived(5d);
        deliveryItem.setBatchNumber("batch123");
        deliveryItem.setExpiryDate(LocalDate.now().plusMonths(6));
        deliveryItem.setPckQty(List.of(
                PckQty.builder()
                        .packageName("Small")
                        .quantity(5d)
                        .build()
        ));
        deliveryItem.setCostPrice(new Money(500));

        DeliveryDto deliveryDto = new DeliveryDto();
        deliveryDto.setDeliveryNoteNumber("delivery-note-101");
        deliveryDto.setInvoiceNumber("invoice-101");
        deliveryDto.setItems(List.of(deliveryItem));

        supplyService.deliver(supplyDto.getId(), deliveryDto);


        // 3. RECEIVE
        supplyService.receive(supplyDto.getId());
        
        //@Todo refactor implementation such that cost price is not required to create sales
        //rather retrieve cost price from product package
        SaleCreationDto saleCreationDto1 = SaleCreationDto.builder()
                .discountRate(0.1d)
                .pckQty(List.of(PckQtyWithSellinPrice.builder()
                        .packageName("Small")
                        .quantity(10d)
                        .sellingPrice(new Money(1000000)).build()))
                .productId(p.getId())
//                .performedBy("tester")
                .quantity(10d)
                .taxRate(0.075d)
                .transactionRef("sampletransref")
                .unitPrice(new Money(5000000))
                .build();
        
        SaleCreationDto saleCreationDto2 = SaleCreationDto.builder()
                .discountRate(0.1d)
                .pckQty(List.of(PckQtyWithSellinPrice.builder()
                        .packageName("Small")
                        .quantity(5d)
                        .sellingPrice(new Money(500000)).build()))
                .productId(p.getId())
//                .performedBy("tester")
                .quantity(5d)
                .taxRate(0.075d)
                .transactionRef("sampletransref")
                .unitPrice(new Money(5000000))
                .build();
        
        SalesCreationDto salesCreationDto = SalesCreationDto.builder()
                .items(List.of(saleCreationDto1, saleCreationDto2))
                .performedBy("tester")
//                .customerId("")
                .clientSystemId("cs001")
                .clientSystemName("Client system 1")
                .transactionRef("sampletransref")
                .build();
        SaleService.create(salesCreationDto);
        
        CashAccountCreationDto parentCashAccount = CashAccountCreationDto.builder()                
                .cashAccountType(CashAccountType.MAIN)
                .email("parent@test.com")
                .phone("0809809876")
                .enforceMinimumBalance(true)
                .cashAccountId("2222222")
                .minimumBalance(new Money(0))
                .name("Parent Account")
                .status(CashAccountStatus.ACTIVE)
                .build();
        cashAccountService.create(parentCashAccount);
        CashAccountCreationDto cashAccount = CashAccountCreationDto.builder()
                .cashAccountType(CashAccountType.MAIN)
                .email("test@test.com")
                .phone("0809809876")
                .enforceMinimumBalance(true)
                .cashAccountId("123456")
                .minimumBalance(new Money(0))
                .name("Demo Account")
                .parentAccountId("2222222")
                .status(CashAccountStatus.ACTIVE)
                .build();
        cashAccountService.create(cashAccount);
        
        List<CashAccount> accounts = cashAccountService.list(CashAccountQueryParams.builder().build());
    }
    
    private void installDevLicense(){
        String lic = licenseService.generateToken("Dev");
        LicenseDto dto = licenseService.importLicense(lic);
        System.out.println("Installed License /n" + dto);
        
    }
}
