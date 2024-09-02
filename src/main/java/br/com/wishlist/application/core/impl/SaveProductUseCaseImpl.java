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
    private final WishlistMapper wishlistMapper;

    @Override
    public void execute(String clientId, Product product) throws Exception {

        // Recupere a WishlistEntity do banco de dados
        Optional<WishlistEntity> wishlistEntityOpt = repository.findByClientId(clientId);

        Wishlist wishlist;

        // Converta a entidade para o domínio, se existir
        if (wishlistEntityOpt.isPresent()) {
            wishlist = wishlistMapper.toDomain(wishlistEntityOpt.get());
        } else {
            wishlist = new Wishlist(clientId); // Crie uma nova Wishlist se não existir
        }

        // Adiciona o produto à Wishlist
        wishlist.addProduct(product);

        // Converta o domínio de volta para a entidade e salve
        repository.save(wishlistMapper.toEntity(wishlist));
    }
}