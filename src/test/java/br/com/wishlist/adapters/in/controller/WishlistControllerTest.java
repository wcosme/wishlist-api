package br.com.wishlist.adapters.in.controller;

import br.com.wishlist.adapters.in.controller.request.AddProductRequest;
import br.com.wishlist.application.core.domain.Product;
import br.com.wishlist.application.core.usecase.*;
import br.com.wishlist.adapters.in.controller.response.WishlistResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = WishlistController.class)
class WishlistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SaveProductUseCase saveProductUseCase;

    @MockBean
    private DeleteProductUseCase deleteProductUseCase;

    @MockBean
    private GetProductUseCase getProductUseCase;

    @MockBean
    private CheckProductUseCase checkProductUseCase;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldAddProductToWishlist() throws Exception {
        AddProductRequest request = new AddProductRequest("product1", "Product 1");

        mockMvc.perform(post("/wishlist/client1/products")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldRemoveProductFromWishlist() throws Exception {
        mockMvc.perform(delete("/wishlist/client1/products")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(new WishlistResponse("client1", List.of()))))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnWishlistForClient() throws Exception {
        // Arrange
        Product product = new Product("product1", "Product 1");
        WishlistResponse response = new WishlistResponse("client1", List.of(product));

        when(getProductUseCase.execute(anyString())).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/wishlist/client1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientId").value("client1"))
                .andExpect(jsonPath("$.products[0].productId").value("product1"))
                .andExpect(jsonPath("$.products[0].name").value("Product 1"));
    }

    @Test
    void shouldCheckProductInWishlist() throws Exception {
        when(checkProductUseCase.execute(anyString(), anyString())).thenReturn(true);

        mockMvc.perform(get("/wishlist/client1/products/product1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}