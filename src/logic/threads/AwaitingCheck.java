package logic.threads;

import data.Order;
import logic.collections.Orders;

import java.time.Instant;

public class AwaitingCheck extends ACheck {
    public AwaitingCheck(Orders orders, int periodSeconds) {
        super(orders, periodSeconds);
    }

    @Override
    public void check(Orders o) {
//        System.out.println("AWAITING CHECK");
        o.changeWith(f -> f.setStatus(Order.STATUSES.PROCESSED),
                g -> g.getExpectedTime().compareTo(Instant.now()) < 1);
    }
}
