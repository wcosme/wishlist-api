package br.com.wishlist.adapters.out.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "wishlists")
public class WishlistEntity {

    @Id
    private String clientId;
    private List<ProductEntity> productsEntityList;
}
