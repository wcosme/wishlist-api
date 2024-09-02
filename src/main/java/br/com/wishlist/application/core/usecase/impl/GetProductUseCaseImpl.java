package br.com.wishlist.application.core.usecase.impl;

import br.com.wishlist.adapters.in.controller.mapper.WishlistMapper;
import br.com.wishlist.adapters.out.repository.MongoWishlistRepository;
import br.com.wishlist.application.core.usecase.GetProductUseCase;
import br.com.wishlist.application.core.domain.Wishlist;
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
                .orElseThrow(() -> new Exception("Wishlist not found for client: " + clientId));
    }
}
