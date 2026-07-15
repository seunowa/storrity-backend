/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.product.service;

import com.storrity.storrity.cashaccounts.entity.Money;
import com.storrity.storrity.product.dto.ProductCreationDto;
import com.storrity.storrity.product.dto.ProductPackageDto;
import com.storrity.storrity.product.entity.Product;
import com.storrity.storrity.product.entity.ProductPackage;
import com.storrity.storrity.util.exception.InputValidationAppException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
public class ProductCsvMapper {
    public static final String[] HEADER = {
        "name", "code", "qtyInStock", "category", "subcategory", "stockKeepingUnit", "unitPrice",
        "store", "brand", "description", "barCode", "location",
        "reorderLevel", "reorderQuantity", "packages"
    };

    private static final String PKG_SEP = ";";
    private static final String FIELD_SEP = ":";

    private ProductCsvMapper() {
    }

    // ---------------------------------------------------------------
    // Import: CSV row -> ProductCreationDto
    // ---------------------------------------------------------------

    /**
     * Builds the header-name -> column-index lookup once per file so rows don't
     * have to be in exactly the order declared in HEADER (only the header names
     * have to match, case-insensitively).
     */
    public static Map<String, Integer> indexHeader(String[] header) {
        Map<String, Integer> index = new LinkedHashMap<>();
        for (int i = 0; i < header.length; i++) {
            index.put(header[i].trim().toLowerCase(), i);
        }
        for (String required : HEADER) {
            if (!index.containsKey(required.toLowerCase())) {
                throw new InputValidationAppException("CSV is missing required column: " + required);
            }
        }
        return index;
    }

    public static ProductCreationDto fromCsvRow(Map<String, Integer> headerIndex, String[] row, UUID storeId) {
        ProductCreationDto dto = new ProductCreationDto();
        dto.setName(field(row, headerIndex, "name"));
        dto.setCode(field(row, headerIndex, "code"));        
        dto.setCategory(blankToNull(field(row, headerIndex, "category")));
        dto.setSubcategory(blankToNull(field(row, headerIndex, "subcategory")));
        dto.setStockKeepingUnit(field(row, headerIndex, "stockKeepingUnit"));
        dto.setUnitPrice(csvToMoney(field(row, headerIndex, "unitPrice")));
//        dto.setStoreId(parseUuid(field(row, headerIndex, "storeId")));
        dto.setStoreId(storeId);
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

    private static List<ProductPackageDto> parsePackages(String raw) {
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

    public static String[] toCsvRow(Product p) {
        return new String[] {
            nullToBlank(p.getName()),
            nullToBlank(p.getCode()),
            p.getQtyInStock()!= null ? String.valueOf(p.getQtyInStock()) : "",
            nullToBlank(p.getCategory()),
            nullToBlank(p.getSubcategory()),
            nullToBlank(p.getStockKeepingUnit()),
            moneyToCsv(p.getUnitPrice()),
            p.getStore() != null && p.getStore().getName()!= null ? p.getStore().getName().toString() : "",
            nullToBlank(p.getBrand()),
            nullToBlank(p.getDescription()),
            nullToBlank(p.getBarCode()),
            nullToBlank(p.getLocation()),
            p.getReorderLevel() != null ? String.valueOf(p.getReorderLevel()) : "",
            p.getReorderQuantity() != null ? String.valueOf(p.getReorderQuantity()) : "",
            packagesToCsv(p.getPackages())
        };
    }

    private static String packagesToCsv(java.util.Collection<ProductPackage> packages) {
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

    // ---------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------

    private static String field(String[] row, Map<String, Integer> headerIndex, String name) {
        Integer idx = headerIndex.get(name.toLowerCase());
        if (idx == null || idx >= row.length) {
            return "";
        }
        String v = row[idx];
        return v == null ? "" : v.trim();
    }

    private static String blankToNull(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }

    private static String nullToBlank(String s) {
        return s == null ? "" : s;
    }

    private static Double parseDouble(String s) {
        if (s == null || s.isBlank()) {
            return null;
        }
        try {
            return Double.valueOf(s.trim());
        } catch (NumberFormatException e) {
            throw new InputValidationAppException("Invalid number: '" + s + "'");
        }
    }

    private static String formatDouble(Double d) {
        if (d == null) {
            return "";
        }
        // avoid "12.0" noise for whole numbers, e.g. multipliers
        if (d == Math.floor(d) && !d.isInfinite()) {
            return String.valueOf(d.longValue());
        }
        return String.valueOf(d);
    }

    private static UUID parseUuid(String s) {
        if (s == null || s.isBlank()) {
            throw new InputValidationAppException("storeId is required");
        }
        try {
            return UUID.fromString(s.trim());
        } catch (IllegalArgumentException e) {
            throw new InputValidationAppException("Invalid storeId UUID: '" + s + "'");
        }
    }

    private static Money csvToMoney(String s) {
        if (s == null || s.isBlank()) {
            return null;
        }
        try {
            return Money.fromNaira(new BigDecimal(s.trim()));
        } catch (ArithmeticException | NumberFormatException e) {
            throw new InputValidationAppException("Invalid money value: '" + s + "'");
        }
    }

    private static String moneyToCsv(Money money) {
        if (money == null) {
            return "";
        }
        return money.toNaira().stripTrailingZeros().toPlainString();
    }
}
