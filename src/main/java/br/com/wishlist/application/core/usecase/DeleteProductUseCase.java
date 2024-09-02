package br.com.wishlist.application.core.usecase;

public interface DeleteProductUseCase {
    void execute(String clientId, String productId) throws Exception;
}
