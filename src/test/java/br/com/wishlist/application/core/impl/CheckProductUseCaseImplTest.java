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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.junit.jupiter.api.Assertions.*;

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
    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product("product1", "Product 1");
        entity = new WishlistEntity("client1", Collections.singletonList(new ProductEntity("product1", "Product 1")));
        wishlist = new Wishlist("client1", Collections.singletonList(product));
    }

    @Test
    void shouldReturnTrueWhenProductExistsInWishlist() {
        // Given
        given(repository.findByClientId("client1")).willReturn(Optional.of(entity));
        given(mapper.toDomain(entity)).willReturn(wishlist);

        // When
        boolean result = useCase.execute("client1", "product1");

        // Then
        assertTrue(result);
        then(repository).should().findByClientId("client1");
        then(mapper).should().toDomain(entity);
    }

    @Test
    void shouldThrowCustomExceptionWhenWishlistNotFound() {
        // Given
        given(repository.findByClientId("client1")).willReturn(Optional.empty());

        // When
        CustomException exception = assertThrows(CustomException.class, () -> {
            useCase.execute("client1", "product1");
        });

        // Then
        assertEquals("Wishlist not found for client: client1", exception.getMessage());
        then(repository).should().findByClientId("client1");
        then(mapper).should(never()).toDomain(any());
    }

    @Test
    void shouldThrowCustomExceptionWhenProductNotFoundInWishlist() {
        // Given
        wishlist = new Wishlist("client1", Collections.emptyList());  // Wishlist sem produtos
        given(repository.findByClientId("client1")).willReturn(Optional.of(entity));
        given(mapper.toDomain(entity)).willReturn(wishlist);

        // When
        CustomException exception = assertThrows(CustomException.class, () -> {
            useCase.execute("client1", "product1");
        });

        // Then
        assertEquals("Product not found in the wishlist: product1", exception.getMessage());
        then(repository).should().findByClientId("client1");
        then(mapper).should().toDomain(entity);
    }

    @Test
    void shouldCatchAndRethrowUnexpectedException() {
        // Given
        given(repository.findByClientId("client1")).willThrow(new RuntimeException("Database error"));

        // When
        CustomException exception = assertThrows(CustomException.class, () -> {
            useCase.execute("client1", "product1");
        });

        // Then
        assertEquals("An unexpected error occurred while checking the product in the wishlist.", exception.getMessage());
        then(repository).should().findByClientId("client1");
    }
}
