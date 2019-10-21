package logic;

import data.Order;
import data.Product;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int productCount = 10;
        ArrayList<Product> products = new ArrayList<>();

        for (int i = 0; i < productCount; i++) {
            Product p = new ProductFactory(new String[]{"tea", "coffee"}[(int) Math.round(Math.random())]).create();
            p.create();
            products.add(p);
        }

        Orders<Order> orders = new Orders<>();
        Threads threads = new Threads(orders, products, 12, 16, 8);
        Scanner scan = new Scanner(System.in);

        while(true) {
            System.out.println(LocalDateTime.now() + "\n" + orders.toString());

            String s = scan.next();
            switch (s.toLowerCase()) {
                case "status":
                case "info":
                    break;
                case "awaiting":
                case "check":
                    threads.awaitingCheck.startCheck();
                    break;
                case "create":
                case "random":
                    threads.creator.startCheck();
                    break;
                case "delete":
                case "processed":
                    threads.processedCheck.startCheck();
                    break;
                case "exit":
                case "quit":
                    System.exit(0);
                default:
                    System.out.println("Wrong command");
            }
        }
    }
}
