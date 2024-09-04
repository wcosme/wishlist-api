package br.com.wishlist.application.core.impl;

import br.com.wishlist.adapters.out.mapper.WishlistMapper;
import br.com.wishlist.adapters.out.repository.MongoWishlistRepository;
import br.com.wishlist.adapters.out.repository.entity.WishlistEntity;
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
class GetProductUseCaseImplTest {

    @Mock
    private MongoWishlistRepository repository;

    @Mock
    private WishlistMapper mapper;

    @InjectMocks
    private GetProductUseCaseImpl useCase;

    private WishlistEntity entity;
    private Wishlist wishlist;

    @BeforeEach
    void setUp() {
        entity = new WishlistEntity("client1", null);
        wishlist = new Wishlist("client1");
    }

    @Test
    void shouldReturnWishlistWhenFound() {
        // Given
        given(repository.findByClientId("client1")).willReturn(Optional.of(entity));
        given(mapper.toDomain(entity)).willReturn(wishlist);

        // When
        Wishlist result = useCase.execute("client1");

        // Then
        assertNotNull(result);
        assertEquals("client1", result.getClientId());
        then(repository).should().findByClientId("client1");
        then(mapper).should().toDomain(entity);
    }

    @Test
    void shouldThrowCustomExceptionWhenWishlistNotFound() {
        // Given
        given(repository.findByClientId("client1")).willReturn(Optional.empty());

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () -> useCase.execute("client1"));

        assertEquals("Wishlist not found for client: client1", exception.getMessage());
        then(repository).should().findByClientId("client1");
        then(mapper).should(never()).toDomain(any());
    }
}