package br.com.wishlist.application.core.usecase.impl;

import br.com.wishlist.application.core.usecase.SaveProductUseCase;
import br.com.wishlist.application.core.domain.Product;
import br.com.wishlist.application.core.domain.Wishlist;
import br.com.wishlist.infrastructure.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaveProductUseCaseImpl implements SaveProductUseCase {

    private final WishlistRepository wishlistRepository;


    @Override
    public void execute(String clientId, Product product) throws Exception {

        Wishlist wishlist = wishlistRepository.findByClientId(clientId).orElse(new Wishlist(clientId));

        wishlist.addProduct(product);
        wishlistRepository.save(wishlist);
    }
}
