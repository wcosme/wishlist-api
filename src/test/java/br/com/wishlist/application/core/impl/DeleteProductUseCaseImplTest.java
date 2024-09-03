package br.com.wishlist.application.core.impl;

import br.com.wishlist.adapters.out.mapper.WishlistMapper;
import br.com.wishlist.adapters.out.repository.MongoWishlistRepository;
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

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteProductUseCaseImplTest {

    @Mock
    private MongoWishlistRepository repository;

    @Mock
    private WishlistMapper mapper;

    @InjectMocks
    private DeleteProductUseCaseImpl useCase;

    private Wishlist wishlist;
    private WishlistEntity entity;

    @BeforeEach
    void setUp() {
        entity = new WishlistEntity("client1", new ArrayList<>());
        wishlist = new Wishlist("client1", new ArrayList<>());
    }

    @Test
    void execute_ShouldRemoveProductFromWishlist() throws Exception {
        // Arrange
        wishlist.addProduct(new Product("product1", "Product 1"));
        when(repository.findByClientId("client1")).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(wishlist);
        when(mapper.toEntity(wishlist)).thenReturn(entity);

        // Act
        useCase.execute("client1", "product1");

        // Assert
        assertTrue(wishlist.getProducts().isEmpty());
        verify(repository).save(entity);
    }

    @Test
    void execute_ShouldThrowExceptionWhenWishlistNotFound() {
        // Arrange
        when(repository.findByClientId("client1")).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            useCase.execute("client1", "product1");
        });

        assertEquals("Wishlist not found for client: client1", exception.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void execute_ShouldThrowExceptionWhenProductNotFoundInWishlist() {
        // Arrange
        when(repository.findByClientId("client1")).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(wishlist);

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            useCase.execute("client1", "product1");
        });

        assertEquals("Product not found in the wishlist: product1", exception.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void execute_ShouldCatchAndRethrowUnexpectedException() {
        // Arrange
        when(repository.findByClientId("client1")).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> {
            useCase.execute("client1", "product1");
        });

        assertEquals("An unexpected error occurred while deleting the product from the wishlist.", exception.getMessage());
        verify(repository).findByClientId("client1");
    }
}