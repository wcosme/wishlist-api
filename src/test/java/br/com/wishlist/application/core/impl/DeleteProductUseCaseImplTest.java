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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.junit.jupiter.api.Assertions.*;

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
    void shouldRemoveProductFromWishlist() throws Exception {
        // Given
        wishlist.addProduct(new Product("product1", "Product 1"));
        given(repository.findByClientId("client1")).willReturn(Optional.of(entity));
        given(mapper.toDomain(entity)).willReturn(wishlist);
        given(mapper.toEntity(wishlist)).willReturn(entity);

        // When
        useCase.execute("client1", "product1");

        // Then
        assertTrue(wishlist.getProducts().isEmpty());
        then(repository).should().save(entity);
    }

    @Test
    void shouldThrowExceptionWhenWishlistNotFound() {
        // Given
        given(repository.findByClientId("client1")).willReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            useCase.execute("client1", "product1");
        });

        assertEquals("Wishlist not found for client: client1", exception.getMessage());
        then(repository).should(never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenProductNotFoundInWishlist() {
        // Given
        given(repository.findByClientId("client1")).willReturn(Optional.of(entity));
        given(mapper.toDomain(entity)).willReturn(wishlist);

        // When & Then
        Exception exception = assertThrows(Exception.class, () -> {
            useCase.execute("client1", "product1");
        });

        assertEquals("Product not found in the wishlist: product1", exception.getMessage());
        then(repository).should(never()).save(any());
    }

    @Test
    void shouldCatchAndRethrowUnexpectedException() {
        // Given
        given(repository.findByClientId("client1")).willThrow(new RuntimeException("Database error"));

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () -> {
            useCase.execute("client1", "product1");
        });

        assertEquals("An unexpected error occurred while deleting the product from the wishlist.", exception.getMessage());
        then(repository).should().findByClientId("client1");
    }
}