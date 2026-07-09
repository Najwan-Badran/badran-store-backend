package com.badran.store.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures the OpenAPI document for the Badran Store API and declares JWT bearer authentication.
 */
@Configuration
public class OpenApiConfig {

    private static final String BEARER_AUTH = "bearerAuth";

    /**
     * Builds the OpenAPI metadata, security scheme, and global bearer-token requirement.
     */
    @Bean
    public OpenAPI badranStoreOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Badran Car Wash Supplies & Car Accessories Store API")
                        .version("1.0.0")
                        .description("Modular monolith API for authentication, products, cart, wishlist, reviews, and orders.")
                        .license(new License().name("University Project")))
                .components(new Components()
                        .addSecuritySchemes(BEARER_AUTH, new SecurityScheme()
                                .name(BEARER_AUTH)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH));
    }
}
