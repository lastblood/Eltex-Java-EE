package logic;

import com.sun.jdi.ObjectReference;
import data.Credentials;
import data.Order;
import data.Product;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    private static ACheck awaitingCheck;
    private static ACheck processedCheck;
    private static Timer createCheck;
    private static UtilityRandom random = new UtilityRandom();

    private static Credentials credentials;
    static {
        credentials.setFirstName("FirstName");
        credentials.setLastName("LastName");
        credentials.setPatronymic("Patronymic");
        credentials.setEmail("email@email.com");
    }

    public static void main(String[] args) throws InterruptedException {
        Orders<Order> orders = new Orders<>();
        Scanner scan = new Scanner(System.in);



        int awaitingCheckPeriod = 10000;
        awaitingCheck = new ACheck(orders, awaitingCheckPeriod) { //перевод из awaiting в processed
            @Override
            void check(Orders o) {
                o.changeWithLambda(f -> ((Order) f).setStatus(Order.STATUSES.PROCESSED));
            }
        };

        int processedCheckPeriod = 20000;

        processedCheck = new ACheck(orders, processedCheckPeriod) {
            @Override
            void check(Orders o) {
                o.deleteByFilter(f -> ((Order) f).getStatus() == Order.STATUSES.PROCESSED);
            }
        };

        int createCheckPeriod = 3000;
        createCheck = new Timer(true);
        createCheck.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                orders.add(new Order(Duration.ofSeconds(random.nextInt(3,30)), credentials, ));
            }
        }, createCheckPeriod, createCheckPeriod);

        System.out.print("Enter count of products: ");
        int count = scan.nextInt();

        ArrayList<Product> products = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            try {
                boolean random = false;
                System.out.print("Enter product #" + (i+1) + " type: ");
                String s = scan.next();
                if(s.equalsIgnoreCase("random")) {
                    s = new String[]{"tea", "coffee"}[(int) Math.round(Math.random())];
                    random = true;
                }

                Product p = new ProductFactory(s).create();
                if(random)
                    p.create();
                else
                    p.update();

                p.read();

                products.add(p);
            } catch (Exception e) {
                System.err.println("Input exception");
            }
        }

        ShoppingCart<Product> cart = new ShoppingCart<>();
        for (Product temp : products) {
            System.out.println("How many " + temp.getName() + " need to be added in order?");
            int countOfProduct = scan.nextInt();
            for (int j = 0; j < countOfProduct; j++)
                cart.add(temp);
        }

        System.out.println("Input time for access order (in seconds): ");
        orders.checkout(Duration.ofSeconds(scan.nextInt()), credentials, cart);

        while(true) {
            String s = scan.next();
            switch (s.toLowerCase()) {
                case "print":
                case "info":
                    System.out.println(orders.toString());
                    break;
                case "check":
                    orders.check();
                    System.out.println(orders.toString());
                    break;
            }
        }
    }
}
