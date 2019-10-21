package logic;

import data.Credentials;
import data.Order;
import data.Product;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Threads {
    public final ACheck awaitingCheck;
    public final ACheck processedCheck;
    public final ACheck creator;


    public Threads(Orders orders, List<Product> products, int awaitingCheckPeriod, int processedCheckPeriod, int creatorPeriod) {
        if(awaitingCheckPeriod == -1) awaitingCheckPeriod = 12;
        if(processedCheckPeriod == -1) processedCheckPeriod = 18;
        if(creatorPeriod == -1) creatorPeriod = 10;

        UtilityRandom random = new UtilityRandom();

        awaitingCheck = new ACheck(orders, awaitingCheckPeriod) { //перевод из awaiting в processed
            @Override
            void check(Orders o) {
                System.out.println("AWAITING CHECK");
                o.changeWithLambda((Consumer<Order>) f -> f.setStatus(Order.STATUSES.PROCESSED),
                        (Predicate<Order>) g -> g.getExpectedTime().compareTo(LocalDateTime.now()) < 1);
            }
        };

        processedCheck = new ACheck(orders, processedCheckPeriod) {
            @Override
            void check(Orders o) {
                System.out.println("PROCESSED CHECK");
                o.deleteByFilter((Predicate<Order>) f -> f.getStatus() == Order.STATUSES.PROCESSED);
            }
        };

        creator = new ACheck(orders, creatorPeriod) {
            @Override
            void check(Orders o) {
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
        };
    }
}
