package filesio;

import data.Credentials;
import data.Order;
import logic.collections.Orders;
import logic.collections.ProductFactory;
import logic.collections.ShoppingCart;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SerializationTest {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ShoppingCart cart = new ShoppingCart();
        cart.addAll(IntStream.range(0, 3).mapToObj(x -> ProductFactory.createRandomProduct()).collect(Collectors.toList()));

        Order o = new Order(Duration.ofSeconds(20), Credentials.getRandomInstance(), cart);
        System.out.println("CREATED " + o);

        Orders orders = new Orders();
        orders.add(o);

        File dir = new File(".");

        ManagerOrderFile mof = new ManagerOrderFile(orders, dir);
        ManageOrderJSON moj = new ManageOrderJSON(orders, dir);

        mof.saveAll();
        System.out.println("SERIALIZED IN BIN");

        moj.saveAll();
        System.out.println("SERIALIZED IN JSON");

        Order o1 = mof.readById(o.getId());
        System.out.println("DESERIALIZED FROM BIN: " + o1);

        Order o2 = moj.readById(o.getId());
        System.out.println("DESERIALIZED FROM JSON: " + o2);

    }
}
