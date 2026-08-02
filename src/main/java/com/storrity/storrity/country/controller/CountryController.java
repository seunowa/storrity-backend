/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.storrity.storrity.country.controller;

import com.storrity.storrity.country.dto.SelectedCountryDto;
import com.storrity.storrity.country.entity.Country;
import com.storrity.storrity.country.entity.CountryQueryParams;
import com.storrity.storrity.country.service.CountryService;
import com.storrity.storrity.util.exception.ApiError;
import com.storrity.storrity.util.exception.ServerError;
import com.storrity.storrity.util.exception.ValidationError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Seun Owa
 */

@CrossOrigin
@RestController
@RequestMapping("/api/v1/countries")
@Tag(name = "Countries", description = "Country and currency lookup")
public class CountryController {
    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @Operation(
            operationId = "listCountries",
            summary = "List countries",
            description = "Returns countries matching supplied filters.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Countries retrieved successfully",
            content = @Content(
                array = @ArraySchema(schema = @Schema(implementation = Country.class))
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Validation Error",
            content = @Content(schema = @Schema(implementation = ValidationError.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @GetMapping
    public List<Country> listCountries(
            @ModelAttribute @Valid @ParameterObject CountryQueryParams params) {

        return countryService.list(params);

    }

    @Operation(
            operationId = "getCountry",
            summary = "Get country",
            description = "Retrieve a country by country name.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Country retrieved successfully",
            content = @Content(schema = @Schema(implementation = Country.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Country not found",
            content = @Content(schema = @Schema(implementation = ApiError.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected error",
            content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @GetMapping("/{countryName}")
    public Country getCountry(
            @PathVariable String countryName) {

        return countryService.get(countryName);

    }
    
    @Operation(
        operationId = "updateSelectedCountry",
        summary = "Update selected country",
        description = "Stores the application's selected country.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Selected country updated successfully",
                content = @Content(schema = @Schema(implementation = Country.class))
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Validation Error",
                content = @Content(schema = @Schema(implementation = ValidationError.class))
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Country not found",
                content = @Content(schema = @Schema(implementation = ApiError.class))
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Unexpected error",
                content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @PutMapping("/selected_country")
    public Country updateSelectedCountry(
            @RequestBody @Valid SelectedCountryDto dto) {
        return countryService.updateSelectedCountry(dto);
    }

    @Operation(
            operationId = "getSelectedCountry",
            summary = "Get selected country",
            description = "Returns the country name currently stored in the database.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Selected country retrieved successfully",
                content = @Content(schema = @Schema(implementation = SelectedCountryDto.class))
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Selected country not configured",
                content = @Content(schema = @Schema(implementation = ApiError.class))
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Unexpected error",
                content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @GetMapping("/selected_country")
    public SelectedCountryDto getSelectedCountry() {

        return countryService.getSelectedCountry();

    }

    @Operation(
            operationId = "getSelectedCountryDetails",
            summary = "Get selected country details",
            description = "Returns the full country information for the selected country.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Country retrieved successfully",
                content = @Content(schema = @Schema(implementation = Country.class))
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Selected country not configured or country not found",
                content = @Content(schema = @Schema(implementation = ApiError.class))
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Unexpected error",
                content = @Content(schema = @Schema(implementation = ServerError.class))
        )
    })
    @GetMapping("/selected_country/details")
    public Country getSelectedCountryDetails() {

        return countryService.getSelectedCountryDetails();

    }

}
