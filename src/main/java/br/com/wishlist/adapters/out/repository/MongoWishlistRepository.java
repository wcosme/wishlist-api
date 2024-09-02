package br.com.wishlist.adapters.out.repository;

import br.com.wishlist.adapters.out.repository.entity.WishlistEntity;
import br.com.wishlist.application.core.domain.WishlistRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MongoWishlistRepository extends WishlistRepository, MongoRepository<WishlistEntity, String> {

    @Override
    Optional<WishlistEntity> findByClientId(String clientId);
}
