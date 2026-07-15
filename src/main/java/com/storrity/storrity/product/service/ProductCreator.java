/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.product.service;

import com.storrity.storrity.product.dto.ProductCreationDto;
import com.storrity.storrity.product.dto.ProductDto;
import com.storrity.storrity.product.entity.Product;
import com.storrity.storrity.product.entity.ProductPackage;
import com.storrity.storrity.product.event.ProductCreatedEvent;
import com.storrity.storrity.product.repository.ProductPackageRepository;
import com.storrity.storrity.product.repository.ProductRepository;
import com.storrity.storrity.store.entity.Store;
import com.storrity.storrity.store.service.StoreService;
import com.storrity.storrity.util.exception.InputValidationAppException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 *
 * @author Seun Owa
 *
 * Holds the actual "create one product" logic that used to live inline in
 * ProductServiceImpl.create(ProductCreationDto). It was pulled out into its
 * own bean so both ProductServiceImpl (the public single-create API) and
 * ProductRowImportService (per-row CSV import, run in its own REQUIRES_NEW
 * transaction) can call it directly — without ProductRowImportService having
 * to depend on ProductService/ProductServiceImpl, which is what caused the
 * circular dependency (ProductServiceImpl -> ProductRowImportService ->
 * ProductService -> ProductServiceImpl).
 *
 * ProductCreator has no dependency on ProductService or ProductServiceImpl,
 * so it can safely sit underneath both.
 */
@Service
public class ProductCreator {

    private final StoreService storeService;
    private final ProductRepository productRepository;
    private final ProductPackageRepository productPackageRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public ProductCreator(StoreService storeService, ProductRepository productRepository,
            ProductPackageRepository productPackageRepository, ApplicationEventPublisher eventPublisher) {
        this.storeService = storeService;
        this.productRepository = productRepository;
        this.productPackageRepository = productPackageRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public ProductDto create(ProductCreationDto dto) {
// @Todo consider how to ensure the stock keeping unit in the product is available as a package
        dto.getPackages()
                .stream()
                .filter(p -> p.getName().equalsIgnoreCase(dto.getStockKeepingUnit()))
                .findFirst()
                .orElseThrow(() -> new InputValidationAppException(
                        "No matching package found for SKU: " + dto.getStockKeepingUnit()));

        Store store = storeService.fetch(dto.getStoreId());

        Product newProd = Product.builder()
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

        Product savedProduct = productRepository.save(newProd);

        List<ProductPackage> prodPackages = dto.getPackages()
                .stream()
                .map(pkg -> ProductPackage.builder()
                        .name(pkg.getName())
                        .multiplier(pkg.getMultiplier())
                        .productId(savedProduct.getId())
                        .sellingPrice(pkg.getSellingPrice())
                        .build())
                .collect(Collectors.toList());

        List<ProductPackage> savedProdPackages = productPackageRepository.saveAll(prodPackages);

//      @Todo  Consider how best to add savedProdPackages to product which will be returned
        savedProduct.setPackages(savedProdPackages);
        eventPublisher.publishEvent(new ProductCreatedEvent(savedProduct));
        return ProductDto.from(savedProduct);
    }
}
