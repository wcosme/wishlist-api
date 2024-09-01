package br.com.wishlist.application.usecase.impl;

import br.com.wishlist.application.usecase.SaveProductUseCase;
import br.com.wishlist.domain.Product;
import br.com.wishlist.domain.Wishlist;
import br.com.wishlist.infrastructure.WishlistRepository;
import org.springframework.stereotype.Service;

@Service
public class SaveProductUseCaseImpl implements SaveProductUseCase {

    private final WishlistRepository wishlistRepository;

    public SaveProductUseCaseImpl(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }

    @Override
    public void execute(String clientId, Product product) throws Exception {

        Wishlist wishlist = wishlistRepository.findByClientId(clientId).orElse(new Wishlist(clientId));

        wishlist.addProduct(product);
        wishlistRepository.save(wishlist);
    }
}
