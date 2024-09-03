package br.com.wishlist.adapters.in.mapper;

import br.com.wishlist.adapters.in.controller.request.AddProductRequest;
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
    void toProduct_ShouldMapAddProductRequestToProduct() {
        // Arrange
        AddProductRequest request = new AddProductRequest("product1", "Product 1");

        // Act
        Product product = mapper.toProduct(request);

        // Assert
        assertNotNull(product);
        assertEquals("product1", product.getProductId());
        assertEquals("Product 1", product.getName());
    }

    @Test
    void toProduct_ShouldReturnNullWhenRequestIsNull() {
        // Act
        Product product = mapper.toProduct(null);

        // Assert
        assertNull(product);
    }

    @Test
    void toWishlistResponse_ShouldMapWishlistToWishlistResponse() {
        // Arrange
        Product product = new Product("product1", "Product 1");
        Wishlist wishlist = new Wishlist("client1", List.of(product));

        // Act
        WishlistResponse response = mapper.toWishlistResponse(wishlist);

        // Assert
        assertNotNull(response);
        assertEquals("client1", response.clientId());
        assertNotNull(response.products());
        assertEquals(1, response.products().size());
        assertEquals("product1", response.products().get(0).getProductId());
        assertEquals("Product 1", response.products().get(0).getName());
    }

    @Test
    void toWishlistResponse_ShouldReturnNullWhenWishlistIsNull() {
        // Act
        WishlistResponse response = mapper.toWishlistResponse(null);

        // Assert
        assertNull(response);
    }

    @Test
    void toWishlistResponse_ShouldHandleEmptyProductList() {
        // Arrange
        Wishlist wishlist = new Wishlist("client1", Collections.emptyList());

        // Act
        WishlistResponse response = mapper.toWishlistResponse(wishlist);

        // Assert
        assertNotNull(response);
        assertEquals("client1", response.clientId());
        assertNotNull(response.products());
        assertTrue(response.products().isEmpty());
    }
}