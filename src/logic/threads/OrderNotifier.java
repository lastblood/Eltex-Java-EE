package logic.threads;

import data.Order;
import logic.collections.Orders;
import logic.net.UdpNotifier;

import java.util.Collection;

public class OrderNotifier extends ACheck {
    UdpNotifier notifier;

    public OrderNotifier(Orders orders, int periodSeconds, UdpNotifier notifier) {
        super(orders, periodSeconds);
        this.notifier = notifier;
    }

    @Override
    public void check(Orders orders) {
        Collection<Order> snapshot = orders.snapshot();
        snapshot.stream().filter(x -> x.getStatus() == Order.STATUSES.PROCESSED).forEach(x -> {
            System.out.println(x.getId() + " completed");
            notifier.sendOrderId(x.getId());
            System.out.println("Notified");
            x.setStatus(Order.STATUSES.COMPLETED);
        });

    }
}
