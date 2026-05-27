package atmin.controller;


import atmin.entity.Product;
import atmin.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // STT 6: GET all – HTTP 200 -> JSON array
    @Test
    void getAll_ShouldReturnJsonArrayAndStatus200() throws Exception {
        when(productService.findAll()).thenReturn(Arrays.asList(
                new Product(1L, "Keyboard", 150.0, 10)
        ));

        mockMvc.perform(get("/api/products").accept(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Keyboard"));
    }

    // STT 7: GET by id – 404 -> Status 404
    @Test
    void getById_WhenNotFound_ShouldReturnStatus404() throws Exception {
        when(productService.findById(99L)).thenThrow(new RuntimeException("Not found"));

        mockMvc.perform(get("/api/products/99"))
                .andExpect(status().isNotFound());
    }

    // STT 8: POST – 201 -> Body chứa id
    @Test
    void postProduct_ShouldReturnStatus201AndBodyContainingId() throws Exception {
        Product input = new Product(null, "Mouse", 50.0, 5);
        Product saved = new Product(2L, "Mouse", 50.0, 5);

        when(productService.save(any(Product.class))).thenReturn(saved);

        mockMvc.perform(post("/api/products")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Mouse"));
    }

    // STT 9: DELETE – 204 -> No content
    @Test
    void deleteProduct_ShouldReturnStatus204NoContent() throws Exception {
        when(productService.delete(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());
    }
}