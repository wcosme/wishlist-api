package br.com.wishlist.application.core.usecase;

import br.com.wishlist.application.core.domain.Wishlist;

public interface GetProductUseCase {
    Wishlist execute(String clientId);
}
