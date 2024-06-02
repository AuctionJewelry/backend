package com.se.jewelryauction.components.configurations;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class OpenApiConfig {
        @Value("${app.openapi.dev-url}")
        private String devUrl;


        @Bean
        public OpenAPI myOpenAPI() {
                io.swagger.v3.oas.models.servers.Server devServer = new io.swagger.v3.oas.models.servers.Server();
                devServer.setUrl(devUrl);
                devServer.setDescription("Server URL in Development environment");

                io.swagger.v3.oas.models.info.Info info = new Info()
                        .title("API JewelryAuction")
                        .version("1.0.0")
                        .description("This API exposes endpoints to manage demo.");

                return new OpenAPI().info(info).servers(List.of(devServer))
                        .addSecurityItem(new SecurityRequirement().
                                addList("Bearer Authentication"))
                        .components(new Components().addSecuritySchemes
                                ("Bearer Authentication", createAPIKeyScheme()));
        }

        private io.swagger.v3.oas.models.security.SecurityScheme createAPIKeyScheme() {
                return new io.swagger.v3.oas.models.security.SecurityScheme().type(SecurityScheme.Type.HTTP)
                        .bearerFormat("JWT")
                        .scheme("bearer");
        }
}