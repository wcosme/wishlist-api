package br.com.wishlist.application.core.domain;

import java.util.Optional;

public interface WishlistRepository {
    Optional<Wishlist> findByClientId(String clientId);
    Wishlist save(Wishlist wishlist);
    boolean deleteProductFromWishlist(String clientId, String productId);
    boolean existsProductInWishlist(String clientId, String productId);
}
