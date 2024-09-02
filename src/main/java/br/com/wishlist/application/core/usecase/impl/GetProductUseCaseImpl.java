package br.com.wishlist.application.core.usecase.impl;

import br.com.wishlist.application.core.usecase.GetProductUseCase;
import br.com.wishlist.application.core.domain.Wishlist;
import br.com.wishlist.infrastructure.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetProductUseCaseImpl implements GetProductUseCase {

    private final WishlistRepository wishlistRepository;


    @Override
    public Wishlist execute(String clientId) throws Exception {
        return wishlistRepository.findByClientId(clientId)
                .orElseThrow(() -> new Exception("Wishlist not found for client: " + clientId));
    }
}
