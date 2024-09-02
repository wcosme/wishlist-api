package br.com.wishlist.application.core.usecase;

public interface CheckProductUseCase {
    boolean execute(String clientId, String productId) throws Exception;
}
