/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.country.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.storrity.storrity.country.dto.SelectedCountryDto;
import com.storrity.storrity.country.entity.Country;
import com.storrity.storrity.country.entity.CountryQueryParams;
import com.storrity.storrity.country.entity.SelectedCountry;
import com.storrity.storrity.country.repository.SelectedCountryRepository;
import com.storrity.storrity.util.exception.ResourceNotFoundAppException;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Seun Owa
 */
@Service
public class CountryServiceImpl implements CountryService{

    private static final Long SELECTED_COUNTRY_ID = 1L;

    private final SelectedCountryRepository repository;

    private final ObjectMapper objectMapper;

    private List<Country> countries = new ArrayList<>();

    @Autowired
    public CountryServiceImpl(SelectedCountryRepository repository, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.repository = repository;
    }

    @PostConstruct
    private void loadCountries() throws IOException {

        try (InputStream in = new ClassPathResource("countries.json").getInputStream()) {

            countries = objectMapper.readValue(
                    in,
                    new TypeReference<List<Country>>() {
                    });

        }
    }

    @Override
    public List<Country> list(CountryQueryParams params) {

        return countries.stream()
                .filter(c -> contains(c.getCountryName(), params.getCountryName()))
                .filter(c -> contains(c.getCountryCode(), params.getCountryCode()))
                .filter(c -> contains(c.getCurrencyName(), params.getCurrencyName()))
                .filter(c -> contains(c.getCurrencyCode(), params.getCurrencyCode()))
                .limit(params.getSize() == null ? Long.MAX_VALUE : params.getSize())
                .collect(Collectors.toList());

    }

    @Override
    public Country get(String countryName) {

        return countries.stream()
                .filter(c -> c.getCountryName().equalsIgnoreCase(countryName))
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundAppException("Country not found"));

    }

    private boolean contains(String value, String search) {

        if (search == null || search.isBlank()) {
            return true;
        }

        return value.toLowerCase(Locale.ENGLISH)
                .contains(search.toLowerCase(Locale.ENGLISH));

    }

    @Override
    @Transactional
    public Country updateSelectedCountry(SelectedCountryDto dto) {
        Country country = get(dto.getCountryName()); // validates country exists

        SelectedCountry selected = repository
                .findById(SELECTED_COUNTRY_ID)
                .orElse(SelectedCountry.builder()
                        .id(SELECTED_COUNTRY_ID)
                        .build());

        selected.setCountryName(dto.getCountryName());

        repository.save(selected);
        
        return country;
    }

    @Override
    public SelectedCountryDto getSelectedCountry() {
        SelectedCountry selectedCountry = repository.findById(SELECTED_COUNTRY_ID)
                .orElseThrow(() ->
                        new ResourceNotFoundAppException("Selected country has not been configured"));
        
        return SelectedCountryDto.builder().countryName(selectedCountry.getCountryName()).build();
    }

    @Override
    public Country getSelectedCountryDetails() {
        SelectedCountry selectedCountry = repository.findById(SELECTED_COUNTRY_ID)
                .orElseThrow(() ->
                        new ResourceNotFoundAppException("Selected country has not been configured"));
        
        return get(selectedCountry.getCountryName());

    }
}
