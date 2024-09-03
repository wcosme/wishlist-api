package br.com.wishlist.application.core.usecase;

import br.com.wishlist.application.core.domain.Product;

public interface SaveProductUseCase {
    void execute(String clientId, Product product);
}
