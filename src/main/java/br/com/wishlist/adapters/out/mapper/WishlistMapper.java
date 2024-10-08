package br.com.wishlist.adapters.out.mapper;

import br.com.wishlist.adapters.out.repository.entity.ProductEntity;
import br.com.wishlist.adapters.out.repository.entity.WishlistEntity;
import br.com.wishlist.application.core.domain.Product;
import br.com.wishlist.application.core.domain.Wishlist;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class WishlistMapper {

    public Wishlist toDomain(WishlistEntity entity) {
        List<Product> products = entity.getProductsEntityList().stream()
                .map(this::toDomain)  // Converte ProductEntity para Product
                .collect(Collectors.toList());
        return new Wishlist(entity.getClientId(), products);
    }

    public WishlistEntity toEntity(Wishlist wishlist) {
        List<ProductEntity> productsEntityList = wishlist.getProducts().stream()
                .map(this::toEntity)  // Converte Product para ProductEntity
                .collect(Collectors.toList());
        return new WishlistEntity(wishlist.getClientId(), productsEntityList);
    }

    private Product toDomain(ProductEntity entity) {
        return new Product(entity.getProductId(), entity.getName());
    }

    private ProductEntity toEntity(Product product) {
        return new ProductEntity(product.getProductId(), product.getName());
    }
}