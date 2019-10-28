package logic.threads;

import data.Order;
import logic.collections.Orders;

public class CompletedCheck extends ACheck {
    public CompletedCheck(Orders orders, int periodSeconds) {
        super(orders, periodSeconds);
    }

    @Override
    public void check(Orders orders) {
//        System.out.println("COMPLETED CHECK");
        orders.deleteByFilter(f -> f.getStatus() == Order.STATUSES.COMPLETED);
    }
}
