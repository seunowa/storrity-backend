/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.product.service;

import com.storrity.storrity.product.dto.ProductCreationDto;
import com.storrity.storrity.product.dto.ProductPackageDto;
import com.storrity.storrity.product.entity.Product;
import com.storrity.storrity.product.entity.ProductPackage;
import com.storrity.storrity.util.csv.AbstractCsvRowMapper;
import com.storrity.storrity.util.csv.CsvContext;
import com.storrity.storrity.util.exception.InputValidationAppException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Seun Owa
 **
 * Converts between the flat CSV representation of a product and the domain
 * DTOs/entities. Kept separate from ProductServiceImpl so the (fiddly) string
 * parsing/formatting logic doesn't clutter the service.
 *
 * CSV shape (header row required):
 *
 *   name,code,category,subcategory,stockKeepingUnit,unitPrice,storeId,brand,
 *   description,barCode,location,reorderLevel,reorderQuantity,packages
 *
 * The "packages" column flattens the (possibly multiple) product packages into
 * one cell so the file stays a single flat table that's easy to open/edit in
 * Excel or a plain text editor:
 *
 *   PKG_SEP (";")   separates individual packages
 *   FIELD_SEP (":") separates name / multiplier / sellingPrice within a package
 *
 *   e.g.  CARTON:1:3500.00;PACK:12:320.00;UNIT:144:30.00
 *
 * Money values in the "unitPrice" and package "sellingPrice" fields are plain
 * naira decimals (e.g. "3500.00"), converted via Money.fromNaira/Money.toNaira —
 * see com.storrity.storrity.cashaccounts.entity.Money.
 */
public class ProductCsvRowMapper extends AbstractCsvRowMapper<ProductCreationDto, Product>{
    
    private static final String PKG_SEP = ";";
    private static final String FIELD_SEP = ":";

    // ---------------------------------------------------------------
    // Import: CSV row -> ProductCreationDto
    // ---------------------------------------------------------------

    /**
     * Builds the header-name -> column-index lookup once per file so rows don't
     * have to be in exactly the order declared in HEADER (only the header names
     * have to match, case-insensitively).
     */

    @Override
    protected String[] headers() {
        String[] headers = {
            "name", "code", "qtyInStock", "category", "subcategory", "stockKeepingUnit", "unitPrice",
            "store", "brand", "description", "barCode", "location",
            "reorderLevel", "reorderQuantity", "packages"
        };
        return headers;
    }

    @Override
    public ProductCreationDto fromCsvRow(Map<String, Integer> headerIndex, String[] row, CsvContext context) {
        ProductCreationDto dto = new ProductCreationDto();
        dto.setName(field(row, headerIndex, "name"));
        dto.setCode(field(row, headerIndex, "code"));        
        dto.setCategory(blankToNull(field(row, headerIndex, "category")));
        dto.setSubcategory(blankToNull(field(row, headerIndex, "subcategory")));
        dto.setStockKeepingUnit(field(row, headerIndex, "stockKeepingUnit"));
        dto.setUnitPrice(csvToMoney(field(row, headerIndex, "unitPrice")));
//        dto.setStoreId(parseUuid(field(row, headerIndex, "storeId")));
        dto.setStoreId(context.getStoreId());
        dto.setBrand(blankToNull(field(row, headerIndex, "brand")));
        dto.setDescription(blankToNull(field(row, headerIndex, "description")));
        dto.setBarCode(blankToNull(field(row, headerIndex, "barCode")));
        dto.setLocation(blankToNull(field(row, headerIndex, "location")));
        dto.setReorderLevel(parseDouble(field(row, headerIndex, "reorderLevel")));
        dto.setReorderQuantity(parseDouble(field(row, headerIndex, "reorderQuantity")));
        dto.setPackages(parsePackages(field(row, headerIndex, "packages")));

        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new InputValidationAppException("name is required");
        }
        if (dto.getCode() == null || dto.getCode().isBlank()) {
            throw new InputValidationAppException("code is required");
        }
        if (dto.getStockKeepingUnit() == null || dto.getStockKeepingUnit().isBlank()) {
            throw new InputValidationAppException("stockKeepingUnit is required");
        }
        if (dto.getPackages() == null || dto.getPackages().isEmpty()) {
            throw new InputValidationAppException("at least one package is required in the 'packages' column");
        }

        return dto;
    }
    
    private List<ProductPackageDto> parsePackages(String raw) {
        List<ProductPackageDto> packages = new ArrayList<>();
        if (raw == null || raw.isBlank()) {
            return packages;
        }
        String[] entries = raw.split(PKG_SEP);
        for (String entry : entries) {
            if (entry.isBlank()) {
                continue;
            }
            String[] parts = entry.split(FIELD_SEP, -1);
            if (parts.length != 3) {
                throw new InputValidationAppException(
                        "Malformed package entry '" + entry + "' — expected name" + FIELD_SEP
                                + "multiplier" + FIELD_SEP + "sellingPrice");
            }
            ProductPackageDto pkg = new ProductPackageDto();
            pkg.setName(parts[0].trim());
            pkg.setMultiplier(parseDouble(parts[1].trim()));
            pkg.setSellingPrice(csvToMoney(parts[2].trim()));
            packages.add(pkg);
        }
        return packages;
    }

    // ---------------------------------------------------------------
    // Export: Product -> CSV row
    // ---------------------------------------------------------------

    @Override
    public String[] toCsvRow(Product entity) {
        return new String[] {
            nullToBlank(entity.getName()),
            nullToBlank(entity.getCode()),
            entity.getQtyInStock()!= null ? String.valueOf(entity.getQtyInStock()) : "",
            nullToBlank(entity.getCategory()),
            nullToBlank(entity.getSubcategory()),
            nullToBlank(entity.getStockKeepingUnit()),
            moneyToCsv(entity.getUnitPrice()),
            entity.getStore() != null && entity.getStore().getName()!= null ? entity.getStore().getName() : "",
            nullToBlank(entity.getBrand()),
            nullToBlank(entity.getDescription()),
            nullToBlank(entity.getBarCode()),
            nullToBlank(entity.getLocation()),
            entity.getReorderLevel() != null ? String.valueOf(entity.getReorderLevel()) : "",
            entity.getReorderQuantity() != null ? String.valueOf(entity.getReorderQuantity()) : "",
            packagesToCsv(entity.getPackages())
        };
    }

    private String packagesToCsv(java.util.Collection<ProductPackage> packages) {
        if (packages == null || packages.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (ProductPackage pkg : packages) {
            if (!first) {
                sb.append(PKG_SEP);
            }
            first = false;
            sb.append(pkg.getName()).append(FIELD_SEP)
                    .append(formatDouble(pkg.getMultiplier())).append(FIELD_SEP)
                    .append(moneyToCsv(pkg.getSellingPrice()));
        }
        return sb.toString();
    }
}
