/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.storrity.storrity;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 *
 * @author Seun Owa
 */

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
//        return new OpenAPI()
//            .info(new Info().title("API Docs").version("1.0"))
//            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
//            .components(new io.swagger.v3.oas.models.Components()
//                .addSecuritySchemes("bearerAuth",
//                    new SecurityScheme()
//                        .name("Authorization")
//                        .type(SecurityScheme.Type.HTTP)
//                        .scheme("bearer")
//                        .bearerFormat("JWT")
//                )
//            );

            return new OpenAPI()
                    .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")));
    }
}
