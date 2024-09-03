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

import java.util.ArrayList;
import java.util.List;
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

    private WishlistEntity entity;
    private Wishlist wishlist;

    @BeforeEach
    void setUp() {
        entity = new WishlistEntity("client1", new ArrayList<>());
        wishlist = new Wishlist("client1", new ArrayList<>());
    }

    @Test
    void execute_ShouldReturnTrueWhenProductExistsInWishlist() {
        // Arrange
        ProductEntity productEntity = new ProductEntity("product1", "Product 1");
        entity = new WishlistEntity("client1", List.of(productEntity));

        when(repository.findByClientId("client1")).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(new Wishlist("client1", List.of(new Product("product1", "Product 1"))));

        // Act
        boolean result = useCase.execute("client1", "product1");

        // Assert
        assertTrue(result, "Product should exist in the wishlist");
        verify(repository).findByClientId("client1");
        verify(mapper).toDomain(entity);
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
        when(repository.findByClientId("client1")).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(wishlist);

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> {
            useCase.execute("client1", "product1");
        });

        assertEquals("Product not found in the wishlist: product1", exception.getMessage());
        verify(repository).findByClientId("client1");
        verify(mapper).toDomain(entity);
    }
}