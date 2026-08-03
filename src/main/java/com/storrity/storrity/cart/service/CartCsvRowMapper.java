/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.cart.service;

import com.storrity.storrity.cart.dto.CartCreationDto;
import com.storrity.storrity.cart.entity.Cart;
import com.storrity.storrity.cart.entity.CartItem;
import com.storrity.storrity.product.entity.Product;
import com.storrity.storrity.sales.entity.PckQtyWithSellinPrice;
import com.storrity.storrity.util.csv.AbstractCsvRowMapper;
import com.storrity.storrity.util.csv.CsvContext;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Seun Owa
 */
public class CartCsvRowMapper extends AbstractCsvRowMapper<CartCreationDto, Cart>{
    
    private static final String ITEM_SEP = "; ";
    private static final String PACKAGE_SEP = ", ";
    
    private static final String[] HEADERS = {
        "tag",
        "transactionRef",
        "store",
        "customerId",
        "createdBy",
        "cartStatus",
        "createdAt",
        "updatedAt",
        "items"
    };

    @Override
    protected String[] headers() {
        return HEADERS;
    }

    @Override
    public CartCreationDto fromCsvRow(Map<String, Integer> headerIndex, String[] row, CsvContext context) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String[] toCsvRow(Cart entity) {
        return new String[]{
            nullToBlank(entity.getTag()),
            nullToBlank(entity.getTransactionRef()),
            entity.getStore() != null
                    ? nullToBlank(entity.getStore().getName())
                    : "",
            nullToBlank(entity.getCustomerId().toString()),
            nullToBlank(entity.getCreatedBy()),
            entity.getCartStatus() != null
                    ? entity.getCartStatus().name()
                    : "",
            entity.getCreatedAt() != null
                    ? entity.getCreatedAt().toString()
                    : "",
            entity.getUpdatedAt() != null
                    ? entity.getUpdatedAt().toString()
                    : "",
            itemsToCsv(entity.getItems())
        };
    }
    
    private String itemsToCsv(Collection<CartItem> items) {

        if (items == null || items.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        boolean first = true;

        for (CartItem item : items) {

            if (!first) {
                sb.append(ITEM_SEP);
            }
            first = false;

            Product product = item.getProduct();

            sb.append(product != null
                    ? nullToBlank(product.getName())
                    : "Unknown Product");

            if (product != null && product.getCode() != null) {
                sb.append(" (")
                        .append(product.getCode())
                        .append(")");
            }

            sb.append(" x ")
                    .append(formatDouble(item.getQuantity()));

            if (item.getSku() != null && !item.getSku().isBlank()) {
                sb.append(" [")
                        .append(item.getSku())
                        .append("]");
            }

            if (item.getPckQty() != null && !item.getPckQty().isEmpty()) {
                sb.append(" {")
                        .append(packageQtyToCsv(item.getPckQty()))
                        .append("}");
            }
        }

        return sb.toString();
    }

    private String packageQtyToCsv(List<PckQtyWithSellinPrice> packages) {

        StringBuilder sb = new StringBuilder();
        boolean first = true;

        for (PckQtyWithSellinPrice pkg : packages) {

            if (!first) {
                sb.append(PACKAGE_SEP);
            }
            first = false;

            sb.append(pkg.getPackageName())
                    .append("=")
                    .append(formatDouble(pkg.getQuantity()));

            if (pkg.getSellingPrice() != null) {
                sb.append(" @ ₦")
                        .append(moneyToCsv(pkg.getSellingPrice()));
            }
        }

        return sb.toString();
    }

}
