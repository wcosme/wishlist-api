package br.com.wishlist.adapters.in.controller.mapper;

import br.com.wishlist.adapters.in.controller.request.AddProductRequest;
import br.com.wishlist.adapters.in.controller.response.WishlistResponse;
import br.com.wishlist.adapters.out.repository.entity.ProductEntity;
import br.com.wishlist.adapters.out.repository.entity.WishlistEntity;
import br.com.wishlist.application.core.domain.Product;
import br.com.wishlist.application.core.domain.Wishlist;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class WishlistMapper {

    public Product toProduct(AddProductRequest request) {
        if (request == null) {
            return null;
        }
        return new Product(request.productId(), request.name());
    }

    public Wishlist toDomain(WishlistEntity entity) {
        if (entity == null) {
            return null;
        }
        // Converte a lista de ProductEntity para Product
        List<Product> products = entity.getProductsEntityList().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
        return new Wishlist(entity.getClientId(), products);
    }

    public WishlistEntity toEntity(Wishlist wishlist) {
        if (wishlist == null) {
            return null;
        }
        // Converte a lista de Product para ProductEntity
        List<ProductEntity> productsEntityList = wishlist.getProducts().stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
        return new WishlistEntity(wishlist.getClientId(), productsEntityList);
    }

    private Product toDomain(ProductEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Product(entity.getProductId(), entity.getName());
    }

    private ProductEntity toEntity(Product product) {
        if (product == null) {
            return null;
        }
        return new ProductEntity(product.getProductId(), product.getName());
    }

    // Converte Wishlist (domínio) para WishlistResponse
    public WishlistResponse toWishlistResponse(Wishlist wishlist) {
        if (wishlist == null) {
            return null;
        }

        // Converte diretamente usando o record WishlistResponse
        return new WishlistResponse(wishlist.getClientId(), wishlist.getProducts());
    }
}