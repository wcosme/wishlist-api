package br.com.wishlist.application.core.impl;

import br.com.wishlist.adapters.out.mapper.WishlistMapper;
import br.com.wishlist.adapters.out.repository.MongoWishlistRepository;
import br.com.wishlist.adapters.out.repository.entity.ProductEntity;
import br.com.wishlist.adapters.out.repository.entity.WishlistEntity;
import br.com.wishlist.application.core.domain.Product;
import br.com.wishlist.application.core.domain.Wishlist;
import br.com.wishlist.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckProductUseCaseImplTest {

    @Mock
    private MongoWishlistRepository repository;

    @Mock
    private WishlistMapper mapper;

    @InjectMocks
    private CheckProductUseCaseImpl useCase;

    private WishlistEntity wishlistEntity;
    private Wishlist wishlist;
    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product("product1", "Product 1");
        wishlistEntity = new WishlistEntity("client1", Collections.singletonList(new ProductEntity("product1", "Product 1")));
        wishlist = new Wishlist("client1", Collections.singletonList(product));
    }

    @Test
    void execute_ShouldReturnTrueWhenProductExistsInWishlist() {
        // Arrange
        when(repository.findByClientId("client1")).thenReturn(Optional.of(wishlistEntity));
        when(mapper.toDomain(wishlistEntity)).thenReturn(wishlist);

        // Act
        boolean result = useCase.execute("client1", "product1");

        // Assert
        assertTrue(result);
        verify(repository).findByClientId("client1");
        verify(mapper).toDomain(wishlistEntity);
    }

    @Test
    void execute_ShouldThrowCustomExceptionWhenWishlistNotFound() {
        // Arrange
        when(repository.findByClientId("client1")).thenReturn(Optional.empty());

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> {
            useCase.execute("client1", "product1");
        });

        assertEquals("Wishlist not found for client: client1", exception.getMessage());
        verify(repository).findByClientId("client1");
        verify(mapper, never()).toDomain(any());
    }

    @Test
    void execute_ShouldThrowCustomExceptionWhenProductNotFoundInWishlist() {
        // Arrange
        wishlist = new Wishlist("client1", Collections.emptyList());  // Wishlist sem produtos
        when(repository.findByClientId("client1")).thenReturn(Optional.of(wishlistEntity));
        when(mapper.toDomain(wishlistEntity)).thenReturn(wishlist);

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> {
            useCase.execute("client1", "product1");
        });

        assertEquals("Product not found in the wishlist: product1", exception.getMessage());
        verify(repository).findByClientId("client1");
        verify(mapper).toDomain(wishlistEntity);
    }

    @Test
    void execute_ShouldCatchAndRethrowUnexpectedException() {
        // Arrange
        when(repository.findByClientId("client1")).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> {
            useCase.execute("client1", "product1");
        });

        assertEquals("An unexpected error occurred while checking the product in the wishlist.", exception.getMessage());
        verify(repository).findByClientId("client1");
    }
}