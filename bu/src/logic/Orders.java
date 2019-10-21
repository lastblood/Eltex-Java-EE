package logic;

import data.Credentials;
import data.Order;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Orders<T extends Order> {
    private final LinkedList<T> ordersCollection;
    private final HashMap<LocalDateTime, T> timeOrderMap;

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


    public synchronized void checkout(T t, ShoppingCart cart, Credentials credentials, Duration duration) {
        t.setStatus(Order.STATUSES.AWAITING);
        t.setCreateTime(LocalDateTime.now());
        t.setExpectedTime(LocalDateTime.now().plusSeconds(duration.toSeconds()));
        t.setCart(cart);
        t.setUserInfo(credentials);
    }

    public synchronized Collection<T> snapshot() {
        return List.copyOf(ordersCollection);
    }

    public synchronized void add(T obj) {
        ordersCollection.add(obj);
    }

    public synchronized void delete(T obj) {
        ordersCollection.remove(obj);
    }

    public synchronized void changeWithLambda(Consumer<T> func) {
        ordersCollection.forEach(func);
    }

    public synchronized void changeWithLambda(Consumer<T> func, Predicate<Order> filter) {
        ordersCollection.stream().filter(filter).forEach(func);
    }

    public synchronized T orderById(UUID id) {
        return ordersCollection.stream().filter(x -> x.getId().equals(id)).findAny().orElseThrow();
    }

    synchronized void deleteByFilter(Predicate<Order> filter) {
        ordersCollection.removeIf(filter);
    }

    @Override
    public synchronized String toString() {
        return "Orders[" +  ordersCollection.size() + "] {"
                + ordersCollection.stream().map(Object::toString).collect(Collectors.joining("\n\t", "\n\t", "\n"))
                + "}";
    }
}