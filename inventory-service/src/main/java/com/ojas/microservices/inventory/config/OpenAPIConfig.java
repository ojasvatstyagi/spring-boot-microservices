package com.ojas.microservices.inventory.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.ExternalDocumentation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI inventoryServiceAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Inventory Service API")
                        .description("REST API for inventory availability and stock checks")
                        .version("v1.0")
                        .contact(new Contact().name("Ojas Tyagi").email("ojas.vats.tyagi@gmail.com"))
                        .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server().url("http://localhost:8083").description("Inventory Service Local"),
                        new Server().url("http://localhost:9000").description("API Gateway")
                ))
                .externalDocs(new ExternalDocumentation()
                        .description("Inventory Service Docs")
                        .url("http://localhost:8083/api-docs"));
    }
}
