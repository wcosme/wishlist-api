package br.com.wishlist.application.usecase;

import br.com.wishlist.domain.Wishlist;

public interface GetProductUseCase {
    Wishlist execute(String clientId) throws Exception;
}
