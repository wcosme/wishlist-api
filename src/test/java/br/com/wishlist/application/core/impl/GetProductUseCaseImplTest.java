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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    void execute_ShouldReturnWishlistWhenFound() {
        // Arrange
        when(repository.findByClientId("client1")).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(wishlist);

        // Act
        Wishlist result = useCase.execute("client1");

        // Assert
        assertNotNull(result);
        assertEquals("client1", result.getClientId());
        verify(repository).findByClientId("client1");
        verify(mapper).toDomain(entity);
    }

    @Test
    void execute_ShouldThrowCustomExceptionWhenWishlistNotFound() {
        // Arrange
        when(repository.findByClientId("client1")).thenReturn(Optional.empty());

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> useCase.execute("client1"));

        assertEquals("Wishlist not found for client: client1", exception.getMessage());
        verify(repository).findByClientId("client1");
        verify(mapper, never()).toDomain(any());
    }
}