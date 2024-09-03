package br.com.wishlist.adapters.in.controller;

import br.com.wishlist.adapters.in.controller.request.AddProductRequest;
import br.com.wishlist.adapters.in.controller.request.RemoveProductRequest;
import br.com.wishlist.adapters.in.controller.response.WishlistResponse;
import br.com.wishlist.adapters.in.mapper.WishlistControllerMapper;
import br.com.wishlist.application.core.domain.Product;
import br.com.wishlist.application.core.domain.Wishlist;
import br.com.wishlist.application.core.usecase.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Add a product to the wishlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product successfully added to the wishlist"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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

    @Operation(summary = "Remove a product from the wishlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product successfully removed from the wishlist"),
            @ApiResponse(responseCode = "404", description = "Product or wishlist not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{clientId}/products")
    public ResponseEntity<Void> removeProduct(
            @PathVariable String clientId,
            @RequestBody RemoveProductRequest request) {

        deleteProductUseCase.execute(clientId, request.productId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Get the wishlist for a client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the wishlist"),
            @ApiResponse(responseCode = "404", description = "Wishlist not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{clientId}")
    public ResponseEntity<WishlistResponse> getWishlist(@PathVariable String clientId) {

        Wishlist wishlist = getProductUseCase.execute(clientId);
        WishlistResponse response = mapper.toWishlistResponse(wishlist);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Check if a product is in the wishlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found in the wishlist"),
            @ApiResponse(responseCode = "404", description = "Product or wishlist not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{clientId}/products/{productId}")
    public ResponseEntity<Boolean> checkProductInWishlist(
            @PathVariable String clientId,
            @PathVariable String productId) {

        boolean exists = checkProductUseCase.execute(clientId, productId);
        return ResponseEntity.ok(exists);
    }
}