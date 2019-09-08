package com.neuedu.pojo;

import java.util.Objects;

public class CartDetail {

    private String cartId;
    private String username;
    private Integer quantity;
    private Product product;

    public String getCartId() {
        return cartId;
    }

    public String getUsername() {
        return username;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartDetail that = (CartDetail) o;
        return Objects.equals(cartId, that.cartId) &&
                Objects.equals(username, that.username) &&
                Objects.equals(quantity, that.quantity) &&
                Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartId, username, quantity, product);
    }
}
