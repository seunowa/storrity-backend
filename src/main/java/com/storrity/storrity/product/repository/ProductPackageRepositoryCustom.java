/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.storrity.storrity.product.repository;

import java.util.List;
import java.util.UUID;

/**
 *
 * @author Seun Owa
 */
public interface ProductPackageRepositoryCustom {
     public List<String> findPackages(String query, Integer limit, UUID storeId);
}
