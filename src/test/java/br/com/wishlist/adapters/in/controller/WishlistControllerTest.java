package br.com.wishlist.adapters.in.controller;

import br.com.wishlist.adapters.in.controller.request.ProductRequest;
import br.com.wishlist.adapters.in.mapper.WishlistControllerMapper;
import br.com.wishlist.application.core.domain.Product;
import br.com.wishlist.application.core.domain.Wishlist;
import br.com.wishlist.application.core.usecase.*;
import br.com.wishlist.adapters.in.controller.response.WishlistResponse;
import br.com.wishlist.exception.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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

    @MockBean
    private WishlistControllerMapper mapper;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldAddProductToWishlist() throws Exception {
        ProductRequest request = new ProductRequest("product1", "Product 1");

        mockMvc.perform(post("/wishlist/client1/products")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldRemoveProductFromWishlist() throws Exception {
        mockMvc.perform(delete("/wishlist/client1/products")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new WishlistResponse("client1", List.of()))))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnWishlistForClient() throws Exception {
        // Arrange
        String clientId = "client1";
        Product product = new Product("product1", "Product 1");
        Wishlist wishlist = new Wishlist(clientId, List.of(product));
        WishlistResponse response = new WishlistResponse(clientId, List.of(product));

        when(getProductUseCase.execute(clientId)).thenReturn(wishlist);
        when(mapper.toWishlistResponse(wishlist)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/wishlist/{clientId}", clientId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientId").value(clientId))
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

    @Test
    void shouldReturnNotFoundWhenWishlistNotFound() throws Exception {
        when(getProductUseCase.execute(anyString())).thenThrow(new CustomException("Wishlist not found"));

        mockMvc.perform(get("/wishlist/client1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNotFoundWhenProductNotInWishlist() throws Exception {
        when(checkProductUseCase.execute(anyString(), anyString())).thenThrow(new CustomException("Product not found"));

        mockMvc.perform(get("/wishlist/client1/products/product1"))
                .andExpect(status().isNotFound());
    }
}