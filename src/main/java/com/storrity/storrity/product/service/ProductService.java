/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.product.service;


import com.storrity.storrity.product.dto.ProductCreationDto;
import com.storrity.storrity.product.dto.ProductDto;
import com.storrity.storrity.product.dto.ProductUpdateDto;
import com.storrity.storrity.product.entity.ProductQueryParams;
import com.storrity.storrity.util.dto.CountDto;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Seun Owa
 */
public interface ProductService {
    public ProductDto create(ProductCreationDto dto);
    public ProductDto fetch(UUID id);
    public List<ProductDto> list(ProductQueryParams params);
    public CountDto count(ProductQueryParams params);
    public ProductDto update(UUID id, ProductUpdateDto dto);
    public ProductDto delete(UUID id);
    
    public List<String> listProductNames(String query, Integer limit, UUID storeId);

    public List<String> listCategories(String query, Integer limit, UUID storeId);

    public List<String> listSubcategories(String query, Integer limit, UUID storeId);

    public List<String> listBrands(String query, Integer limit, UUID storeId);

    public List<String> listStockKeepingUnits(String query, Integer limit, UUID storeId);

    public List<String> listPackages(String query, Integer limit, UUID storeId);
}
