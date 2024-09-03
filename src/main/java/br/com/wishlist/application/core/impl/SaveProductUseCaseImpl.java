package br.com.wishlist.application.core.impl;

import br.com.wishlist.adapters.out.repository.MongoWishlistRepository;
import br.com.wishlist.adapters.out.mapper.WishlistMapper;
import br.com.wishlist.application.core.usecase.SaveProductUseCase;
import br.com.wishlist.application.core.domain.Product;
import br.com.wishlist.application.core.domain.Wishlist;
import br.com.wishlist.adapters.out.repository.entity.WishlistEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SaveProductUseCaseImpl implements SaveProductUseCase {

    private final MongoWishlistRepository repository;
    private final WishlistMapper mapper;

    @Override
    public void execute(String clientId, Product product) throws Exception {

        Optional<WishlistEntity> optional = repository.findByClientId(clientId);

        Wishlist wishlist;

        if (optional.isPresent()) {
            wishlist = mapper.toDomain(optional.get());
        } else {
            wishlist = new Wishlist(clientId);
        }
        wishlist.addProduct(product);

        repository.save(mapper.toEntity(wishlist));
    }
}