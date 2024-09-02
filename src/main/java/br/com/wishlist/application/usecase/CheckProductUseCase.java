package br.com.wishlist.application.usecase;

public interface CheckProductUseCase {
    boolean execute(String clientId, String productId) throws Exception;
}
