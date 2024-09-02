package br.com.wishlist.adapters.out.repository;

import br.com.wishlist.application.core.domain.Wishlist;
import br.com.wishlist.application.core.domain.WishlistRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MongoWishlistRepository extends WishlistRepository, MongoRepository<Wishlist, String> {

    @Override
    Optional<Wishlist> findByClientId(String clientId);

    @Override
    boolean deleteProductFromWishlist(String clientId, String productId);

    @Override
    boolean existsProductInWishlist(String clientId, String productId);
}
