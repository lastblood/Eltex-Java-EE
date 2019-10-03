package logic;

import data.Product;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ShoppingCart<T extends Product> {
    private ArrayList<T> productsCollection;

    public ShoppingCart() {
        productsCollection = new ArrayList<>();
    }

    public ShoppingCart(ArrayList<T> productsCollection) {
        this.productsCollection = productsCollection;
    }


    public void add(T product) {
        productsCollection.add(product);
    }

    public void delete(T product) {
        productsCollection.remove(product);
    }


    public String showAll() {
        return productsCollection.stream().map(Object::toString).collect(Collectors.joining(", "));
    }
}
