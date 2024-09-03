package br.com.wishlist.adapters.in.mapper;

import br.com.wishlist.adapters.in.controller.request.ProductRequest;
import br.com.wishlist.adapters.in.controller.response.WishlistResponse;
import br.com.wishlist.application.core.domain.Product;
import br.com.wishlist.application.core.domain.Wishlist;
import org.springframework.stereotype.Component;

@Component
public class WishlistControllerMapper {

    public Product toProduct(ProductRequest request) {
        if (request == null) {
            return null;
        }
        return new Product(request.productId(), request.name());
    }

    // Converte Wishlist (domínio) para WishlistResponse
    public WishlistResponse toWishlistResponse(Wishlist wishlist) {
        if (wishlist == null) {
            return null;
        }
        return new WishlistResponse(wishlist.getClientId(), wishlist.getProducts());
    }
}
