/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.country.entity;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Seun Owa
 */
@Getter
@Setter
public class CountryQueryParams {

    private String countryName;
    private String countryCode;
    private String currencyName;
    private String currencyCode;

    /**
     * Maximum number of elements to return.
     * Defaults to all.
     */
    private Integer size;

}
