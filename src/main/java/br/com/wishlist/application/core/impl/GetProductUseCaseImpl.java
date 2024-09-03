package br.com.wishlist.application.core.impl;

import br.com.wishlist.adapters.out.mapper.WishlistMapper;
import br.com.wishlist.adapters.out.repository.MongoWishlistRepository;
import br.com.wishlist.application.core.usecase.GetProductUseCase;
import br.com.wishlist.application.core.domain.Wishlist;
import br.com.wishlist.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetProductUseCaseImpl implements GetProductUseCase {

    private final MongoWishlistRepository repository;
    private final WishlistMapper mapper;

    @Override
    public Wishlist execute(String clientId) {
        try {
            return repository.findByClientId(clientId)
                    .map(mapper::toDomain)
                    .orElseThrow(() -> new CustomException("Wishlist not found for client: " + clientId));
        } catch (Exception e) {
            // Captura qualquer outra exceção inesperada e lança uma CustomException
            throw new CustomException("An unexpected error occurred while retrieving the wishlist.");
        }
    }
}