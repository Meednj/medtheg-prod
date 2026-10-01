package com.medthegprod.backend.infrastructure.web;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

        @Bean
        public OpenAPI medTheGStoreOpenAPI() {
                return new OpenAPI()
                                .info(new Info()
                                                .title("Med TheG Store API")
                                                .description("REST API for the Med TheG digital music marketplace.")
                                                .version("v1"))
                                .components(new Components()
                                                .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                                                .type(SecurityScheme.Type.HTTP)
                                                                .scheme("bearer")
                                                                .bearerFormat("JWT")
                                                                .description("Enter the JWT only. Swagger UI adds the Bearer prefix.")));
        }
}