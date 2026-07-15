/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.product.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Seun Owa
 */
/**
 * Result of a CSV product import. Import is processed row-by-row (each row its own
 * transaction) so one malformed row does not roll back the whole file — this DTO
 * reports exactly which rows failed and why, so the caller can fix and re-upload
 * just those rows.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductImportResultDto {
    

    private int totalRows;
    private int successCount;
    private int failureCount;
    private List<RowError> errors = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RowError {
        /** 1-based line number in the CSV file, including the header row, so it maps
         *  directly to what the user sees if they open the file in Excel/a text editor. */
        private int lineNumber;
        private String message;
    }
}
