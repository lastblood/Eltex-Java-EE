package logic.collections;

import data.Order;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Orders {
    private final LinkedList<Order> ordersCollection;

    public Orders(LinkedList<Order> collection) {
        ordersCollection = new LinkedList<>();
        ordersCollection.addAll(collection);
    }

    public Orders() {
        ordersCollection = new LinkedList<>();
    }

    public synchronized Collection<Order> snapshot() {
        return List.copyOf(ordersCollection);
    }

    public synchronized void add(Order obj) {
        ordersCollection.add(obj);
    }

    public synchronized void delete(Order obj) {
        ordersCollection.remove(obj);
    }

    public synchronized void changeWith(Consumer<Order> func) {
        ordersCollection.forEach(func);
    }

    public synchronized void changeWith(Consumer<Order> func, Predicate<Order> filter) {
        ordersCollection.stream().filter(filter).forEach(func);
    }

    public synchronized Order orderById(UUID id) {
        return ordersCollection.stream().filter(x -> x.getId().equals(id)).findAny().orElseThrow();
    }

    public synchronized void deleteByFilter(Predicate<Order> filter) {
        ordersCollection.removeIf(filter);
    }

    @Override
    public synchronized String toString() {
        return "Orders[" +  ordersCollection.size() + "] {"
                + ordersCollection.stream().map(Object::toString).collect(Collectors.joining("\n\t", "\n\t", "\n"))
            + "}";
    }
}