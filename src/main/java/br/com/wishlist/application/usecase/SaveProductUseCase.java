package br.com.wishlist.application.usecase;

import br.com.wishlist.domain.Product;

public interface SaveProductUseCase {
    void execute(String clientId, Product product) throws Exception;
}
