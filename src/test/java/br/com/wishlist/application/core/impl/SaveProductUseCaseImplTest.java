package br.com.wishlist.application.core.impl;

import br.com.wishlist.adapters.out.mapper.WishlistMapper;
import br.com.wishlist.adapters.out.repository.MongoWishlistRepository;
import br.com.wishlist.adapters.out.repository.entity.WishlistEntity;
import br.com.wishlist.application.core.domain.Product;
import br.com.wishlist.application.core.domain.Wishlist;
import br.com.wishlist.exception.CustomException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaveProductUseCaseImplTest {

    @Mock
    private MongoWishlistRepository repository;

    @Mock
    private WishlistMapper mapper;

    @InjectMocks
    private SaveProductUseCaseImpl useCase;



    @Test
    void execute_ShouldAddProductToExistingWishlist() {
        String clientId = "client1";
        Product product = new Product("product1", "Product 1");
        WishlistEntity wishlistEntity = new WishlistEntity(clientId, new ArrayList<>());
        Wishlist wishlist = new Wishlist(clientId, new ArrayList<>());

        when(repository.findByClientId(clientId)).thenReturn(Optional.of(wishlistEntity));
        when(mapper.toDomain(wishlistEntity)).thenReturn(wishlist);
        when(mapper.toEntity(any(Wishlist.class))).thenReturn(wishlistEntity);

        useCase.execute(clientId, product);

        verify(repository).save(any(WishlistEntity.class));
        assertEquals(1, wishlist.getProducts().size());
        assertEquals("product1", wishlist.getProducts().get(0).getProductId());
    }

    @Test
    void execute_ShouldCreateNewWishlistIfNotFound(){
        String clientId = "client2";
        Product product = new Product("product2", "Product 2");
        Wishlist newWishlist = new Wishlist(clientId);
        WishlistEntity newWishlistEntity = new WishlistEntity(clientId, new ArrayList<>());

        when(repository.findByClientId(clientId)).thenReturn(Optional.empty());
        when(mapper.toEntity(any(Wishlist.class))).thenReturn(newWishlistEntity);

        useCase.execute(clientId, product);

        verify(repository).save(any(WishlistEntity.class));
        verify(mapper).toEntity(newWishlist);
    }

    @Test
    void execute_ShouldThrowExceptionWhenExceedingProductLimit() {
        String clientId = "client3";
        Product product = new Product("product21", "Product 21");

        List<Product> products = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            products.add(new Product("product" + i, "Product " + i));
        }

        Wishlist wishlist = new Wishlist(clientId, products);
        WishlistEntity wishlistEntity = new WishlistEntity(clientId, new ArrayList<>());

        when(repository.findByClientId(clientId)).thenReturn(Optional.of(wishlistEntity));
        when(mapper.toDomain(wishlistEntity)).thenReturn(wishlist);

        Exception exception = assertThrows(CustomException.class, () -> {
            useCase.execute(clientId, product);
        });

        assertEquals("Cannot add more than 20 products to the wishlist.", exception.getMessage());
        verify(repository, never()).save(any(WishlistEntity.class));
    }

}