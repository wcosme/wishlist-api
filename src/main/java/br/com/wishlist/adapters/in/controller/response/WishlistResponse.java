package br.com.wishlist.adapters.in.controller.response;

import br.com.wishlist.application.core.domain.Product;

import java.util.List;

public record WishlistResponse(

        String clientId,
        List<Product> products

) {
}
