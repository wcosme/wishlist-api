package br.com.wishlist.application.core.impl;

import br.com.wishlist.adapters.out.mapper.WishlistMapper;
import br.com.wishlist.adapters.out.repository.MongoWishlistRepository;
import br.com.wishlist.adapters.out.repository.entity.WishlistEntity;
import br.com.wishlist.application.core.usecase.DeleteProductUseCase;
import br.com.wishlist.application.core.domain.Wishlist;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteProductUseCaseImpl implements DeleteProductUseCase {

    private final MongoWishlistRepository repository;
    private final WishlistMapper mapper;

    @Override
    public void execute(String clientId, String productId) throws Exception {

        WishlistEntity entity = repository.findByClientId(clientId)
                .orElseThrow(() -> new Exception("Wishlist not found for client: " + clientId));

        Wishlist wishlist = mapper.toDomain(entity);

        boolean removed = wishlist.removeProductById(productId);
        if (!removed) {
            throw new Exception("Product not found in the wishlist: " + productId);
        }

        repository.save(mapper.toEntity(wishlist));
    }

}
