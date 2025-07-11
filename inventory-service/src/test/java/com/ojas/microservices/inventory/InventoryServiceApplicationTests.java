package com.ojas.microservices.inventory;

import com.ojas.microservices.inventory.model.Inventory;
import com.ojas.microservices.inventory.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.MySQLContainer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
@ContextConfiguration
public class InventoryServiceApplicationTests {

	@ServiceConnection
	static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.3.0");

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private InventoryRepository inventoryRepository;

	@BeforeEach
	void setup() {
		inventoryRepository.deleteAll();
		inventoryRepository.save(new Inventory(null, "iphone_15", 10));
		inventoryRepository.save(new Inventory(null, "macbook_air", 0));
	}

	@Test
	void shouldReturnInStockTrueForSufficientQuantity() throws Exception {
		mockMvc.perform(get("/api/inventory")
						.param("skuCode", "iphone_15")
						.param("quantity", "5"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.skuCode").value("iphone_15"))
				.andExpect(jsonPath("$.inStock").value(true));
	}

	@Test
	void shouldReturnInStockFalseForInsufficientQuantity() throws Exception {
		mockMvc.perform(get("/api/inventory")
						.param("skuCode", "macbook_air")
						.param("quantity", "1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.skuCode").value("macbook_air"))
				.andExpect(jsonPath("$.inStock").value(false));
	}
}
