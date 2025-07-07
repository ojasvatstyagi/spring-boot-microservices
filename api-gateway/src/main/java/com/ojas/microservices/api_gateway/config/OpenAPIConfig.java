package com.ojas.microservices.api_gateway.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.ExternalDocumentation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI apiGatewayDoc() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Gateway")
                        .description("Gateway routing for all services")
                        .version("v1.0")
                        .contact(new Contact().name("Ojas Tyagi").email("ojas.vats.tyagi@gmail.com")))
                .externalDocs(new ExternalDocumentation()
                        .description("API Gateway Docs")
                        .url("http://localhost:9000/api-docs"));
    }
}