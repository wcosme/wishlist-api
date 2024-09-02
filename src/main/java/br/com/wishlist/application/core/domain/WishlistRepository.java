package br.com.wishlist.application.core.domain;

import br.com.wishlist.adapters.out.repository.entity.WishlistEntity;

import java.util.Optional;

public interface WishlistRepository {
    Optional<WishlistEntity> findByClientId(String clientId);
}
