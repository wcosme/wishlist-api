package br.com.wishlist.adapters.in.mapper;

import br.com.wishlist.adapters.in.controller.request.ProductRequest;
import br.com.wishlist.adapters.in.controller.response.WishlistResponse;
import br.com.wishlist.application.core.domain.Product;
import br.com.wishlist.application.core.domain.Wishlist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WishlistControllerMapperTest {

    private WishlistControllerMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new WishlistControllerMapper();
    }

    @Test
    void shouldMapProductRequestToProduct() {
        // Given
        ProductRequest request = new ProductRequest("product1", "Product 1");

        // When
        Product product = mapper.toProduct(request);

        // Then
        assertNotNull(product);
        assertEquals("product1", product.getProductId());
        assertEquals("Product 1", product.getName());
    }

    @Test
    void shouldReturnNullWhenProductRequestIsNull() {
        // Given
        ProductRequest request = null;

        // When
        Product product = mapper.toProduct(request);

        // Then
        assertNull(product);
    }

    @Test
    void shouldMapWishlistToWishlistResponse() {
        // Given
        Product product = new Product("product1", "Product 1");
        Wishlist wishlist = new Wishlist("client1", List.of(product));

        // When
        WishlistResponse response = mapper.toWishlistResponse(wishlist);

        // Then
        assertNotNull(response);
        assertEquals("client1", response.clientId());
        assertNotNull(response.products());
        assertEquals(1, response.products().size());
        assertEquals("product1", response.products().get(0).getProductId());
        assertEquals("Product 1", response.products().get(0).getName());
    }

    @Test
    void shouldReturnNullWhenWishlistIsNull() {
        // Given
        Wishlist wishlist = null;

        // When
        WishlistResponse response = mapper.toWishlistResponse(wishlist);

        // Then
        assertNull(response);
    }

    @Test
    void shouldHandleEmptyProductListInWishlist() {
        // Given
        Wishlist wishlist = new Wishlist("client1", Collections.emptyList());

        // When
        WishlistResponse response = mapper.toWishlistResponse(wishlist);

        // Then
        assertNotNull(response);
        assertEquals("client1", response.clientId());
        assertNotNull(response.products());
        assertTrue(response.products().isEmpty());
    }
}