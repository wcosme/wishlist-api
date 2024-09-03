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
    public Wishlist execute(String clientId) throws Exception {
        return repository.findByClientId(clientId)
                .map(mapper::toDomain)
                .orElseThrow(() -> new CustomException("Wishlist not found for client: " + clientId, 404));
    }
}
