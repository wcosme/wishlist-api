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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaveProductUseCaseImplTest {

    @Mock
    private MongoWishlistRepository repository;

    @Mock
    private WishlistMapper mapper;

    @InjectMocks
    private SaveProductUseCaseImpl useCase;

    private WishlistEntity wishlistEntity;
    private Wishlist wishlist;
    private Product product;

    @BeforeEach
    void setUp() {
        wishlistEntity = new WishlistEntity("client1", null);
        wishlist = new Wishlist("client1");
        product = new Product("product1", "Product 1");
    }

    @Test
    void execute_ShouldCreateNewWishlistIfNotFound() {
        // Arrange
        when(repository.findByClientId("client1")).thenReturn(Optional.empty());
        when(mapper.toEntity(any(Wishlist.class))).thenReturn(wishlistEntity);

        // Act
        useCase.execute("client1", product);

        // Assert
        verify(repository).save(any(WishlistEntity.class));
        verify(mapper, never()).toDomain(any(WishlistEntity.class));
    }

    @Test
    void execute_ShouldAddProductToExistingWishlist() {
        // Arrange
        when(repository.findByClientId("client1")).thenReturn(Optional.of(wishlistEntity));
        when(mapper.toDomain(wishlistEntity)).thenReturn(wishlist);
        when(mapper.toEntity(wishlist)).thenReturn(wishlistEntity);

        // Act
        useCase.execute("client1", product);

        // Assert
        assertEquals(1, wishlist.getProducts().size());
        assertEquals("product1", wishlist.getProducts().get(0).getProductId());
        verify(repository).save(any(WishlistEntity.class));
    }

    @Test
    void execute_ShouldThrowCustomExceptionWhenExceedingProductLimit() {
        // Arrange
        when(repository.findByClientId("client1")).thenReturn(Optional.of(wishlistEntity));
        when(mapper.toDomain(wishlistEntity)).thenReturn(wishlist);

        // Adicionar 20 produtos à wishlist para alcançar o limite
        for (int i = 0; i < 20; i++) {
            wishlist.addProduct(new Product("product" + i, "Product " + i));
        }

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> {
            useCase.execute("client1", product);
        });

        assertEquals("Cannot add more than 20 products to the wishlist.", exception.getMessage());
        verify(repository, never()).save(any(WishlistEntity.class));
    }

    @Test
    void execute_ShouldCatchAndRethrowUnexpectedException() {
        // Arrange
        when(repository.findByClientId("client1")).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> {
            useCase.execute("client1", product);
        });

        assertEquals("An unexpected error occurred while saving the product to the wishlist.", exception.getMessage());
        verify(repository, never()).save(any(WishlistEntity.class));
    }
}