package com.ojas.microservices.order;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
class OrderServiceApplicationTests {

	@ServiceConnection
	static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.3.0");

	@LocalServerPort
	private Integer port;

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("inventory.url", () -> wireMockServer.baseUrl());
	}

	static WireMockServer wireMockServer;

	@BeforeAll
	static void setupWireMock() {
		wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
		wireMockServer.start();
	}

	@AfterAll
	static void stopWireMock() {
		if (wireMockServer != null) {
			wireMockServer.stop();
		}
	}

	@BeforeEach
	void setupRestAssured() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	@Test
	void shouldSubmitOrder() {
		String submitOrderJson = """
            {
                "skuCode": "iphone_15",
                "price": 1000,
                "quantity": 1
            }
            """;

		wireMockServer.stubFor(get(urlPathEqualTo("/api/inventory"))
				.withQueryParam("skuCode", equalTo("iphone_15"))
				.withQueryParam("quantity", equalTo("1"))
				.willReturn(aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/json")
						.withBody("""
            {
                "skuCode": "iphone_15",
                "inStock": true
            }
        """)));

		RestAssured.given()
				.contentType("application/json")
				.body(submitOrderJson)
				.when()
				.post("/api/order")
				.then()
				.log().all()
				.statusCode(201)
				.body("id", Matchers.notNullValue())
				.body("orderNumber", Matchers.notNullValue())
				.body("skuCode", Matchers.equalTo("iphone_15"))
				.body("price", Matchers.equalTo(1000))
				.body("quantity", Matchers.equalTo(1));
	}
}