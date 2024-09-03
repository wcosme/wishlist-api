package br.com.wishlist.adapters.out.mapper;

import br.com.wishlist.adapters.out.repository.entity.ProductEntity;
import br.com.wishlist.adapters.out.repository.entity.WishlistEntity;
import br.com.wishlist.application.core.domain.Product;
import br.com.wishlist.application.core.domain.Wishlist;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class WishlistMapperTest {

    private final WishlistMapper mapper = new WishlistMapper();

    @Test
    void shouldMapWishlistEntityToWishlist() {
        ProductEntity productEntity = new ProductEntity("product1", "Product 1");
        WishlistEntity wishlistEntity = new WishlistEntity("client1", List.of(productEntity));

        Wishlist wishlist = mapper.toDomain(wishlistEntity);

        assertNotNull(wishlist);
        assertEquals(1, wishlist.getProducts().size());
        assertEquals("product1", wishlist.getProducts().get(0).getProductId());
    }

    @Test
    void shouldMapWishlistToWishlistEntity() {
        Product product = new Product("product1", "Product 1");
        Wishlist wishlist = new Wishlist("client1", List.of(product));

        WishlistEntity wishlistEntity = mapper.toEntity(wishlist);

        assertNotNull(wishlistEntity);
        assertEquals(1, wishlistEntity.getProductsEntityList().size());
        assertEquals("product1", wishlistEntity.getProductsEntityList().get(0).getProductId());
    }
}
