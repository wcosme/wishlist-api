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

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SaveProductUseCaseImplTest {

    @Mock
    private MongoWishlistRepository repository;

    @Mock
    private WishlistMapper mapper;

    @InjectMocks
    private SaveProductUseCaseImpl useCase;

    private WishlistEntity entity;
    private Wishlist wishlist;
    private Product product;

    @BeforeEach
    void setUp() {
        entity = new WishlistEntity("client1", null);
        wishlist = new Wishlist("client1");
        product = new Product("product1", "Product 1");
    }

    @Test
    void shouldCreateNewWishlistWhenItDoesNotExist() {
        // Given
        given(repository.findByClientId("client1")).willReturn(Optional.empty());
        given(mapper.toEntity(any(Wishlist.class))).willReturn(entity);

        // When
        useCase.execute("client1", product);

        // Then
        then(repository).should().save(any(WishlistEntity.class));
        then(mapper).should(never()).toDomain(any(WishlistEntity.class));
    }

    @Test
    void shouldAddProductToExistingWishlist() {
        // Given
        given(repository.findByClientId("client1")).willReturn(Optional.of(entity));
        given(mapper.toDomain(entity)).willReturn(wishlist);
        given(mapper.toEntity(wishlist)).willReturn(entity);

        // When
        useCase.execute("client1", product);

        // Then
        assertEquals(1, wishlist.getProducts().size());
        assertEquals("product1", wishlist.getProducts().get(0).getProductId());
        then(repository).should().save(any(WishlistEntity.class));
    }

    @Test
    void shouldThrowCustomExceptionWhenExceedingProductLimit() {
        // Given
        given(repository.findByClientId("client1")).willReturn(Optional.of(entity));
        given(mapper.toDomain(entity)).willReturn(wishlist);

        // Adiciona 20 produtos para atingir o limite
        for (int i = 0; i < 20; i++) {
            wishlist.addProduct(new Product("product" + i, "Product " + i));
        }

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () -> {
            useCase.execute("client1", product);
        });

        assertEquals("Cannot add more than 20 products to the wishlist.", exception.getMessage());
        then(repository).should(never()).save(any(WishlistEntity.class));
    }

    @Test
    void shouldCatchAndRethrowUnexpectedException() {
        // Given
        given(repository.findByClientId("client1")).willThrow(new RuntimeException("Database error"));

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () -> {
            useCase.execute("client1", product);
        });

        assertEquals("An unexpected error occurred while saving the product to the wishlist.", exception.getMessage());
        then(repository).should(never()).save(any(WishlistEntity.class));
    }
}