package com.ojas.microservices.product.config;

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
    public OpenAPI productServiceAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Product Service API")
                        .description("REST API documentation for managing products in the microservices architecture.")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Ojas Tyagi")
                                .email("ojas.vats.tyagi@gmail.com")
                                .url("https://github.com/ojasvatstyagi"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server().url("http://localhost:8081").description("Local server"),
                        new Server().url("http://localhost:9000").description("API Gateway")
                ))
                .externalDocs(new ExternalDocumentation()
                        .description("Product Service Wiki")
                        .url("http://localhost:8081/api-docs"));
    }
}
