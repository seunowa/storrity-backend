/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.country.service;

import com.storrity.storrity.country.dto.SelectedCountryDto;
import com.storrity.storrity.country.entity.Country;
import com.storrity.storrity.country.entity.CountryQueryParams;
import java.util.List;

/**
 *
 * @author Seun Owa
 */
public interface CountryService{
    public List<Country> list(CountryQueryParams params);
    public Country get(String countryName);
    public Country updateSelectedCountry(SelectedCountryDto dto);
    public SelectedCountryDto getSelectedCountry();
    public Country getSelectedCountryDetails();
}
