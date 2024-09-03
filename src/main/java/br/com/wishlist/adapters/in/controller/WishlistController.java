package br.com.wishlist.adapters.in.controller;

import br.com.wishlist.adapters.in.controller.request.AddProductRequest;
import br.com.wishlist.adapters.in.controller.request.RemoveProductRequest;
import br.com.wishlist.adapters.in.controller.response.WishlistResponse;
import br.com.wishlist.adapters.in.mapper.WishlistControllerMapper;
import br.com.wishlist.application.core.domain.Product;
import br.com.wishlist.application.core.domain.Wishlist;
import br.com.wishlist.application.core.usecase.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wishlist")
@RequiredArgsConstructor
@Tag(name = "Wishlist", description = "Endpoints para gerenciar a lista de desejos")
public class WishlistController {

    private final SaveProductUseCase saveProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final GetProductUseCase getProductUseCase;
    private final CheckProductUseCase checkProductUseCase;
    private final WishlistControllerMapper mapper;

    @Operation(summary = "Adicionar produto à Wishlist", description = "Adiciona um novo produto à Wishlist")
    @PostMapping("/{clientId}/products")
    public ResponseEntity<WishlistResponse> addProduct(
            @PathVariable String clientId,
            @RequestBody AddProductRequest request) {

        Product product = mapper.toProduct(request);
        saveProductUseCase.execute(clientId, product);

        Wishlist wishlist = getProductUseCase.execute(clientId);
        WishlistResponse response = mapper.toWishlistResponse(wishlist);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Remover produto da Wishlist", description = "Remove um produto da Wishlist")
    @DeleteMapping("/{clientId}/products")
    public ResponseEntity<Void> removeProduct(
            @PathVariable String clientId,
            @RequestBody RemoveProductRequest request) {

        deleteProductUseCase.execute(clientId, request.productId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Obter Wishlist do Cliente", description = "Retorna a Wishlist")
    @GetMapping("/{clientId}")
    public ResponseEntity<WishlistResponse> getWishlist(@PathVariable String clientId) {

        Wishlist wishlist = getProductUseCase.execute(clientId);
        WishlistResponse response = mapper.toWishlistResponse(wishlist);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Verificar Produto na Wishlist", description = "Verifica se um produto está na Wishlist")
    @GetMapping("/{clientId}/products/{productId}")
    public ResponseEntity<Boolean> checkProductInWishlist(
            @PathVariable String clientId,
            @PathVariable String productId) {

        boolean exists = checkProductUseCase.execute(clientId, productId);
        return ResponseEntity.ok(exists);
    }
}