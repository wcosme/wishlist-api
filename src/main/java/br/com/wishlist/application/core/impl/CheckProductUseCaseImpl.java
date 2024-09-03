package br.com.wishlist.application.core.impl;

import br.com.wishlist.adapters.out.mapper.WishlistMapper;
import br.com.wishlist.adapters.out.repository.MongoWishlistRepository;
import br.com.wishlist.adapters.out.repository.entity.WishlistEntity;
import br.com.wishlist.application.core.usecase.CheckProductUseCase;
import br.com.wishlist.application.core.domain.Wishlist;
import br.com.wishlist.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckProductUseCaseImpl implements CheckProductUseCase {

    private final MongoWishlistRepository repository;
    private final WishlistMapper mapper;

    public boolean execute(String clientId, String productId) {
        try {
            WishlistEntity entity = repository.findByClientId(clientId)
                    .orElseThrow(() -> new CustomException("Wishlist not found for client: " + clientId));

            Wishlist wishlist = mapper.toDomain(entity);

            boolean productExists = wishlist.getProducts()
                    .stream()
                    .anyMatch(product -> product.getProductId().equals(productId));

            if (!productExists) {
                throw new CustomException("Product not found in the wishlist: " + productId);
            }
            return true;
        } catch (CustomException e) {
            // Relançar a CustomException a ser tratada pelo GlobalExceptionHandler
            throw e;
        } catch (Exception e) {
            throw new CustomException("An unexpected error occurred while checking the product in the wishlist.");
        }
    }
}