package br.com.wishlist.adapters.out.repository;

import br.com.wishlist.adapters.out.repository.entity.WishlistEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MongoWishlistRepository extends MongoRepository<WishlistEntity, String> {
    Optional<WishlistEntity> findByClientId(String clientId);
}
