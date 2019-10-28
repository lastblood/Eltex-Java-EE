package data.net;

import data.Credentials;
import logic.collections.ShoppingCart;

import java.io.Serializable;

public class Request implements Serializable {
    private Credentials credentials;
    private ShoppingCart cart;

    public Request(Credentials credentials, ShoppingCart cart) {
        this.credentials = credentials;
        this.cart = cart;
    }

    private Request() {}

    public Credentials getCredentials() {
        return credentials;
    }

    public ShoppingCart getCart() {
        return cart;
    }
}
