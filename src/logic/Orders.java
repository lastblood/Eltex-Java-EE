package logic;

import data.Credentials;
import data.Order;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Orders<T extends Order> {
    private final LinkedList<Order> ordersCollection;
    private final HashMap<LocalDateTime, Order> timeOrderMap;

    public Orders(LinkedList<T> collection) {
        ordersCollection = new LinkedList<>();
        ordersCollection.addAll(collection);

        timeOrderMap = new HashMap<>();
        ordersCollection.forEach(x -> timeOrderMap.put(x.getCreateTime(), x) );
    }

    public Orders() {
        ordersCollection = new LinkedList<>();
        timeOrderMap = new HashMap<>();
    }


    synchronized void add(T obj) {
        ordersCollection.add(obj);
    }

    synchronized void delete(T obj) {
        ordersCollection.remove(obj);
    }


    synchronized void checkout(Duration duration, Credentials userInfo, ShoppingCart cart) {
        ordersCollection.add(new Order(duration, userInfo, cart));
    }

    synchronized void check() {
        ordersCollection.removeIf(o ->
                o.getStatus().equals(Order.STATUSES.PROCESSED) ||
                o.getExpectedTime().compareTo(LocalDateTime.now()) < 0
        );
    }


    synchronized void changeWithLambda(Consumer<Order> func) {
        ordersCollection.forEach(func);
    }

    synchronized void deleteByFilter(Predicate<Order> filter) {
        ordersCollection.removeIf(filter);
    }

    @Override
    public String toString() {
        return "Orders {"
                + ordersCollection.stream().map(Object::toString).collect(Collectors.joining("\n\t", "\n\t", "\n"))
            + "}";
    }
}