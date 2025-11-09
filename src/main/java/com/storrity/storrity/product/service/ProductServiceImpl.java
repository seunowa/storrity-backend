/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity.product.service;

import com.storrity.storrity.product.dto.ProductCreationDto;
import com.storrity.storrity.product.dto.ProductDto;
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
import com.storrity.storrity.util.dto.CountDto;
import com.storrity.storrity.util.exception.InputValidationAppException;
import com.storrity.storrity.util.exception.ResourceNotFoundAppException;
import java.util.List;
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

    @Autowired
    public ProductServiceImpl(StoreService storeService, ProductRepository productRepository
            , ProductPackageRepository productPackageRepository, ApplicationEventPublisher eventPublisher) {
        this.storeService = storeService;
        this.productRepository = productRepository;
        this.productPackageRepository = productPackageRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    @Override
    public ProductDto create(ProductCreationDto dto) {
        
// @Todo consider how to ensure the stock keeping unit in the product is available as a package
        dto.getPackages()
            .stream()
            .filter(p -> p.getName().equalsIgnoreCase(dto.getStockKeepingUnit()))
            .findFirst()
            .orElseThrow(() -> new InputValidationAppException("No matching package found for SKU: " + dto.getStockKeepingUnit()));
        
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
                .map((pkg)-> ProductPackage.builder()
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
        
        if(dto.getReorderLevel()!= null){
            p.setReorderLevel(dto.getReorderLevel());
        }
        
        if(dto.getReorderQuantity()!= null){
            p.setReorderQuantity(dto.getReorderQuantity());
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
    
}
