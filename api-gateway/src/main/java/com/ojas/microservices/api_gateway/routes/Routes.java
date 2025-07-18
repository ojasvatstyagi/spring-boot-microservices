package com.ojas.microservices.api_gateway.routes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions.circuitBreaker;
import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.setPath;

@Configuration(proxyBeanMethods = false)
public class Routes {

    private static final URI FALLBACK_URI = URI.create("forward:/fallbackRoute");

    // ---------- Microservice Routes ----------

    @Bean
    public RouterFunction<ServerResponse> productServiceRoute() {
        return route("product_service")
                .route(RequestPredicates.path("/api/product/**"), http("http://localhost:8081"))
                .filter(circuitBreaker("productServiceCircuitBreaker", FALLBACK_URI))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> orderServiceRoute() {
        return route("order_service")
                .route(RequestPredicates.path("/api/order/**"), http("http://localhost:8082"))
                .filter(circuitBreaker("orderServiceCircuitBreaker", FALLBACK_URI))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> inventoryServiceRoute() {
        return route("inventory_service")
                .route(RequestPredicates.path("/api/inventory/**"), http("http://localhost:8083"))
                .filter(circuitBreaker("inventoryServiceCircuitBreaker", FALLBACK_URI))
                .build();
    }

    // ---------- Swagger Aggregation Routes ----------

    @Bean
    public RouterFunction<ServerResponse> productServiceSwaggerRoute() {
        return route("product_service_swagger")
                .route(RequestPredicates.path("/aggregate/product-service/v3/api-docs"), http("http://localhost:8081"))
                .filter(circuitBreaker("productServiceSwaggerCircuitBreaker", FALLBACK_URI))
                .filter(setPath("/v3/api-docs"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> orderServiceSwaggerRoute() {
        return route("order_service_swagger")
                .route(RequestPredicates.path("/aggregate/order-service/v3/api-docs"), http("http://localhost:8082"))
                .filter(circuitBreaker("orderServiceSwaggerCircuitBreaker", FALLBACK_URI))
                .filter(setPath("/v3/api-docs"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> inventoryServiceSwaggerRoute() {
        return route("inventory_service_swagger")
                .route(RequestPredicates.path("/aggregate/inventory-service/v3/api-docs"), http("http://localhost:8083"))
                .filter(circuitBreaker("inventoryServiceSwaggerCircuitBreaker", FALLBACK_URI))
                .filter(setPath("/v3/api-docs"))
                .build();
    }

    // ---------- Fallback Handler ----------

    @Bean
    public RouterFunction<ServerResponse> fallbackRoute() {
        return route("fallbackRoute")
                .GET("/fallbackRoute", request ->
                        ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
                                .body("Service Unavailable, please try again later"))
                .build();
    }
}