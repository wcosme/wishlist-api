package br.com.wishlist.application.core.impl;

import br.com.wishlist.adapters.out.mapper.WishlistMapper;
import br.com.wishlist.adapters.out.repository.MongoWishlistRepository;
import br.com.wishlist.adapters.out.repository.entity.WishlistEntity;
import br.com.wishlist.application.core.domain.Product;
import br.com.wishlist.application.core.domain.Wishlist;
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
    private DeleteProductUseCaseImpl deleteProductUseCase;

    private Wishlist wishlist;
    private WishlistEntity wishlistEntity;

    @BeforeEach
    void setUp() {
        wishlistEntity = new WishlistEntity("client1", new ArrayList<>());
        wishlist = new Wishlist("client1", new ArrayList<>());
    }

    @Test
    void execute_ShouldRemoveProductFromWishlist() throws Exception {
        // Arrange
        wishlist.addProduct(new Product("product1", "Product 1"));
        when(repository.findByClientId("client1")).thenReturn(Optional.of(wishlistEntity));
        when(mapper.toDomain(wishlistEntity)).thenReturn(wishlist);
        when(mapper.toEntity(wishlist)).thenReturn(wishlistEntity);

        // Act
        deleteProductUseCase.execute("client1", "product1");

        // Assert
        assertTrue(wishlist.getProducts().isEmpty());
        verify(repository).save(wishlistEntity);
    }

    @Test
    void execute_ShouldThrowExceptionWhenWishlistNotFound() {
        // Arrange
        when(repository.findByClientId("client1")).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            deleteProductUseCase.execute("client1", "product1");
        });

        assertEquals("Wishlist not found for client: client1", exception.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void execute_ShouldThrowExceptionWhenProductNotFoundInWishlist() {
        // Arrange
        when(repository.findByClientId("client1")).thenReturn(Optional.of(wishlistEntity));
        when(mapper.toDomain(wishlistEntity)).thenReturn(wishlist);

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> {
            deleteProductUseCase.execute("client1", "product1");
        });

        assertEquals("Product not found in the wishlist: product1", exception.getMessage());
        verify(repository, never()).save(any());
    }
}