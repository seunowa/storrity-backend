/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.product.service;

import com.storrity.storrity.product.dto.ProductCreationDto;
import com.storrity.storrity.product.dto.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Seun Owa
 *
 * Exists purely to give each imported row its own transaction (REQUIRES_NEW).
 *
 * Spring's @Transactional only works through a proxy, so calling create
 * in a loop from within the same bean would NOT get a fresh transaction per
 * call (self-invocation bypasses the proxy). Routing each row through this
 * separate bean sidesteps that, and means one malformed/duplicate row rolls
 * back only itself, not the whole file.
 *
 * Depends on ProductCreator (not ProductService/ProductServiceImpl) so that
 * ProductServiceImpl -> ProductRowImportService -> ProductCreator forms a
 * one-directional dependency graph, with no cycle back to ProductServiceImpl.
 */
@Service
public class ProductRowImportService {

    private final ProductCreator productCreator;

    @Autowired
    public ProductRowImportService(ProductCreator productCreator) {
        this.productCreator = productCreator;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ProductDto importRow(ProductCreationDto dto) {
        return productCreator.create(dto);
    }
}
