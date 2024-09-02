package br.com.wishlist.application.usecase;

public interface DeleteProductUseCase {
    void execute(String clientId, String productId) throws Exception;
}
