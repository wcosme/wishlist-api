package br.com.wishlist.application.core.usecase.impl;

import br.com.wishlist.application.core.usecase.CheckProductUseCase;
import br.com.wishlist.application.core.domain.Wishlist;
import br.com.wishlist.infrastructure.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckProductUseCaseImpl implements CheckProductUseCase {

    private final WishlistRepository wishlistRepository;

    @Override
    public boolean execute(String clientId, String productId) throws Exception {
        Wishlist wishlist = wishlistRepository.findByClientId(clientId)
                .orElseThrow(() -> new Exception("Wishlist not found for client: " + clientId));

        return wishlist.getProducts().stream()
                .anyMatch(product -> product.getProductId().equals(productId));
    }
}
