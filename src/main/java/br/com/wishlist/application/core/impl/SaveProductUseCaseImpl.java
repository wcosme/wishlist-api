package br.com.wishlist.application.core.impl;

import br.com.wishlist.adapters.out.repository.MongoWishlistRepository;
import br.com.wishlist.adapters.out.mapper.WishlistMapper;
import br.com.wishlist.application.core.usecase.SaveProductUseCase;
import br.com.wishlist.application.core.domain.Product;
import br.com.wishlist.application.core.domain.Wishlist;
import br.com.wishlist.adapters.out.repository.entity.WishlistEntity;
import br.com.wishlist.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SaveProductUseCaseImpl implements SaveProductUseCase {

    private final MongoWishlistRepository repository;
    private final WishlistMapper mapper;

    @Override
    public void execute(String clientId, Product product) {
        try {
            Optional<WishlistEntity> optional = repository.findByClientId(clientId);

            Wishlist wishlist;
            if (optional.isPresent()) {
                wishlist = mapper.toDomain(optional.get());
            } else {
                wishlist = new Wishlist(clientId);
            }

            if (wishlist.getProducts().size() >= 20) {
                throw new CustomException("Cannot add more than 20 products to the wishlist.");
            }

            wishlist.addProduct(product);

            repository.save(mapper.toEntity(wishlist));
        } catch (CustomException e) {
            // Rethrow the CustomException to be handled by the GlobalExceptionHandler
            throw e;
        } catch (Exception e) {
            // Wrap other exceptions in a CustomException
            throw new CustomException("An unexpected error occurred while saving the product to the wishlist.");
        }
    }
}