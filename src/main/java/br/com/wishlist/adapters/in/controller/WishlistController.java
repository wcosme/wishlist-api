package br.com.wishlist.adapters.in.controller;

import br.com.wishlist.adapters.in.controller.mapper.WishlistMapper;
import br.com.wishlist.adapters.in.controller.request.AddProductRequest;
import br.com.wishlist.adapters.in.controller.request.RemoveProductRequest;
import br.com.wishlist.adapters.in.controller.response.WishlistResponse;
import br.com.wishlist.application.core.domain.Product;
import br.com.wishlist.application.core.domain.Wishlist;
import br.com.wishlist.application.core.usecase.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final SaveProductUseCase saveProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final GetProductUseCase getProductUseCase;
    private final CheckProductUseCase checkProductUseCase;
    private final WishlistMapper mapper;

    @PostMapping("/{clientId}/products")
    public ResponseEntity<WishlistResponse> addProduct(
            @PathVariable String clientId,
            @RequestBody AddProductRequest request) throws Exception {

        Product product = mapper.toProduct(request);
        saveProductUseCase.execute(clientId, product);

        // Recuperar a Wishlist atualizada para resposta
        Wishlist wishlist = getProductUseCase.execute(clientId);
        WishlistResponse response = mapper.toWishlistResponse(wishlist);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{clientId}/products")
    public ResponseEntity<Void> removeProduct(
            @PathVariable String clientId,
            @RequestBody RemoveProductRequest request) throws Exception {

        deleteProductUseCase.execute(clientId, request.productId());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<WishlistResponse> getWishlist(@PathVariable String clientId) {

        try {
            Wishlist wishlist = getProductUseCase.execute(clientId);
            WishlistResponse response = mapper.toWishlistResponse(wishlist);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{clientId}/products/{productId}")
    public ResponseEntity<Boolean> checkProductInWishlist(
            @PathVariable String clientId,
            @PathVariable String productId) {

        try {
            boolean exists = checkProductUseCase.execute(clientId, productId);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}