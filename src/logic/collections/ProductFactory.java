package logic.collections;

import data.CoffeeProduct;
import data.Product;
import data.TeaProduct;

public class ProductFactory {
    private final String type;

    public ProductFactory(String type) {
        type = type.toLowerCase();
        if(type.equals("tea") || type.equals("coffee"))
            this.type = type;
        else
            throw new IllegalArgumentException("Wrong type: " + type);
    }

    public Product create() {
        switch(type) {
            case "tea":
                return new TeaProduct();
            case "coffee":
                return new CoffeeProduct();
            default: throw new IllegalArgumentException("Wrong type: " + type);
        }
    }

    public static Product createRandomProduct() {
        Product p = new ProductFactory(Math.random() > 0.5 ? "coffee" : "tea").create();
        p.create();
        return p;
    }
}
