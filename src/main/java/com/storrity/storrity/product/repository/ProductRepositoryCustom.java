/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.product.repository;

import com.storrity.storrity.product.entity.Product;
import com.storrity.storrity.product.entity.ProductQueryParams;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Seun Owa
 */
public interface ProductRepositoryCustom {    
    public List<Product> list(ProductQueryParams params);
    public Long countRecords (ProductQueryParams params);
    
    public List<String> listProductNames(String query, Integer limit, UUID storeId);

    public List<String> listCategories(String query, Integer limit, UUID storeId);

    public List<String> listSubcategories(String query, Integer limit, UUID storeId);

    public List<String> listBrands(String query, Integer limit, UUID storeId);

    public List<String> listStockKeepingUnits(String query, Integer limit, UUID storeId);
}
