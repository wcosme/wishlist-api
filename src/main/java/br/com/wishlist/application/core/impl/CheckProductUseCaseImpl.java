package br.com.wishlist.application.core.impl;

import br.com.wishlist.adapters.out.mapper.WishlistMapper;
import br.com.wishlist.adapters.out.repository.MongoWishlistRepository;
import br.com.wishlist.adapters.out.repository.entity.WishlistEntity;
import br.com.wishlist.application.core.usecase.CheckProductUseCase;
import br.com.wishlist.application.core.domain.Wishlist;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckProductUseCaseImpl implements CheckProductUseCase {

    private final MongoWishlistRepository repository;
    private final WishlistMapper mapper;

    @Override
    public boolean execute(String clientId, String productId) throws Exception {
        WishlistEntity entity = repository.findByClientId(clientId)
                .orElseThrow(() -> new Exception("Wishlist not found for client: " + clientId));

        Wishlist wishlist = mapper.toDomain(entity);

        // Verifica se o produto existe na Wishlist (domínio)
        return wishlist.hasProduct(productId);
    }
}
