package fullstack.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ValidationErrorIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void createProduct_withEmptyPayload_returnsFieldValidationErrors() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        assertThat(response)
                .contains("productId")
                .contains("title")
                .contains("artist")
                .contains("label")
                .contains("formatName")
                .contains("formatType")
                .contains("imageUrl")
                .contains("price");
    }

    @Test
    @WithMockUser
    void createOrder_withInvalidNumbers_returnsBadRequest() throws Exception {
        Map<String, Object> payload = new HashMap<>();
        payload.put("customerId", 1);
        payload.put("productId", "abbey-road-vinilo");
        payload.put("quantity", 0);
        payload.put("status", "");

        MvcResult result = mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .contains("quantity")
                .contains("status");
    }

    @Test
    @WithMockUser
    void createProduct_withDuplicateId_returnsBusinessValidationMessage() throws Exception {
        Map<String, Object> product = new HashMap<>();
        product.put("productId", "abbey-road-vinilo");
        product.put("title", "Abbey Road");
        product.put("artist", Map.of("artistId", 1));
        product.put("label", Map.of("labelId", 1));
        product.put("formatName", "Vinilo");
        product.put("formatType", "VINYL");
        product.put("imageUrl", "https://example.com/image.jpg");
        product.put("price", 20000);
        product.put("stockQuantity", 5);
        product.put("avgRating", 4.5);
        product.put("ratingCount", 100);
        product.put("isAvailable", true);

        MvcResult result = mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .contains("Product already exists with ID: abbey-road-vinilo");
    }
}
