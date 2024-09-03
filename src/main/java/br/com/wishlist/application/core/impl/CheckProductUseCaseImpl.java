package br.com.wishlist.application.core.impl;

import br.com.wishlist.adapters.out.mapper.WishlistMapper;
import br.com.wishlist.adapters.out.repository.MongoWishlistRepository;
import br.com.wishlist.adapters.out.repository.entity.WishlistEntity;
import br.com.wishlist.application.core.usecase.CheckProductUseCase;
import br.com.wishlist.application.core.domain.Wishlist;
import br.com.wishlist.exception.CustomException;
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
                .orElseThrow(() -> new CustomException("Wishlist not found for client: " + clientId, 404));

        Wishlist wishlist = mapper.toDomain(entity);

        // Verifica se o produto está na wishlist
        boolean productExists = wishlist.getProducts().stream()
                .anyMatch(product -> product.getProductId().equals(productId));

        if (!productExists) {
            throw new CustomException("Product not found in the wishlist: " + productId, 404);
        }
        return true;
    }
}
