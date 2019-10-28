package logic.threads;

import data.Credentials;
import data.Order;
import data.Product;
import logic.collections.Orders;
import logic.collections.ShoppingCart;
import logic.utils.UtilityRandom;

import java.time.Duration;
import java.util.List;

public class RandomOrderCreator extends ACheck {
    private UtilityRandom random;
    List<Product> products;

    public RandomOrderCreator(Orders orders, int periodSeconds, List<? extends Product> products) {
        super(orders, periodSeconds);
        random = new UtilityRandom();
    }

    @Override
    public void check(Orders o) {
        System.out.println("CREATE CHECK");
        int count = 0;
        do {
            count++;
            ShoppingCart<Product> sc = new ShoppingCart<>();
            sc.addAll(random.nextElementsFrom(products, products.size()));
            o.add(new Order(Duration.ofSeconds(random.nextInt(3, 30)), Credentials.getRandomInstance(), sc));
        } while (random.nextBoolean());
        System.out.println("CREATED " + count + " RANDOM ORDERS");
    }
}
