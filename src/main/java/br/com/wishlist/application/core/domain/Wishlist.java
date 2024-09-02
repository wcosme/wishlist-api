package br.com.wishlist.application.core.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Wishlist {

    private String clientId;
    private List<Product> products;

    // Construtor padrão
    public Wishlist() {
        this.products = new ArrayList<>();
    }

    // Construtor com argumentos
    public Wishlist(String clientId) {
        this.clientId = clientId;
        this.products = new ArrayList<>();
    }

    // Construtor com todos os argumentos
    public Wishlist(String clientId, List<Product> products) {
        this.clientId = clientId;
        this.products = products != null ? new ArrayList<>(products) : new ArrayList<>();
    }

    // Getters e Setters
    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public List<Product> getProducts() {
        return new ArrayList<>(products); // Retorna uma cópia para proteger a imutabilidade interna
    }

    public void setProducts(List<Product> products) {
        this.products = products != null ? new ArrayList<>(products) : new ArrayList<>();
    }

    // Métodos de domínio
    public boolean addProduct(Product product) {
        if (products.size() >= 20) {
            throw new IllegalStateException("Cannot add more than 20 products to the wishlist.");
        }
        return products.add(product);
    }

    public boolean removeProductById(String productId) {
        return products.removeIf(product -> product.getProductId().equals(productId));
    }

    public boolean hasProduct(String productId) {
        return products.stream().anyMatch(product -> product.getProductId().equals(productId));
    }

    // equals e hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wishlist wishlist = (Wishlist) o;
        return Objects.equals(clientId, wishlist.clientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId);
    }

    // toString
    @Override
    public String toString() {
        return "Wishlist{" +
                "clientId='" + clientId + '\'' +
                ", products=" + products +
                '}';
    }
}