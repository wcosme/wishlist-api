package br.com.wishlist.application.usecase.impl;

import br.com.wishlist.application.usecase.DeleteProductUseCase;
import br.com.wishlist.domain.Wishlist;
import br.com.wishlist.infrastructure.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteProductUseCaseImpl implements DeleteProductUseCase {

    private final WishlistRepository wishlistRepository;

    @Override
    public void execute(String clientId, String productId) throws Exception {

        Wishlist wishlist = wishlistRepository.findByClientId(clientId)
                .orElseThrow(() -> new Exception("Wishlist not found for client: " + clientId));

        boolean removed = wishlist.removeProductById(productId);
        if (!removed) {
            throw new Exception("Product not found in the wishlist: " + productId);
        }

        wishlistRepository.save(wishlist);
    }

}
