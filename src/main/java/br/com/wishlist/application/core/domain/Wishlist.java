package br.com.wishlist.application.core.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Document
public class Wishlist {

    @Id
    private String clientId;
    private Set<Product> products = new HashSet<>();

    public Wishlist(String clientId) {
        this.clientId = clientId;
    }

    public void addProduct(Product product) throws Exception {
        if (products.size() >= 20) {
            throw new Exception("Wishlist cannot contain more than 20 products.");
        }
        products.add(product);
    }

    public boolean removeProductById(String productId) {
        return products.removeIf(product -> product.getProductId().equals(productId));
    }

}