package com.example.store.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

  @Autowired MockMvc mvc;
  @Autowired ObjectMapper mapper;

  @Test
  void adminCanCreateProduct() throws Exception {
    var req = new ProductDtos.CreateProductRequest(
        "SKU-001",
        "Coffee",
        "500g",
        new java.math.BigDecimal("19.99"),
        "RON",
        10
    );

    mvc.perform(post("/api/products")
            .with(httpBasic("admin", "adminpass"))
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(req)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", notNullValue()))
        .andExpect(jsonPath("$.sku", is("SKU-001")))
        .andExpect(jsonPath("$.price", is(19.99)));
  }

  @Test
  void userCannotCreateProduct() throws Exception {
    var req = new ProductDtos.CreateProductRequest(
        "SKU-002",
        "Tea",
        null,
        new java.math.BigDecimal("9.50"),
        "EUR",
        5
    );

    mvc.perform(post("/api/products")
            .with(httpBasic("user", "userpass"))
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(req)))
        .andExpect(status().isForbidden());
  }

  @Test
  void userCanListProducts() throws Exception {
    // create one product first
    var req = new ProductDtos.CreateProductRequest(
        "SKU-003",
        "Milk",
        null,
        new java.math.BigDecimal("7.25"),
        "RON",
        3
    );

    mvc.perform(post("/api/products")
            .with(httpBasic("admin", "adminpass"))
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(req)))
        .andExpect(status().isCreated());

    mvc.perform(get("/api/products")
            .with(httpBasic("user", "userpass")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", not(empty())))
        .andExpect(jsonPath("$.content[0].sku", notNullValue()));
  }
}
