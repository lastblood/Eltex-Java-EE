package data;

import logic.ShoppingCart;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public class Order {
    public enum STATUSES {
        AWAITING,
        PROCESSED
    }

    private ShoppingCart cart;
    private STATUSES status;
    private LocalDateTime createTime;
    private LocalDateTime expectedTime;
    private Credentials userInfo;
    private UUID id;

    public Order(Duration duration, Credentials userInfo, ShoppingCart cart) {
        id = UUID.randomUUID();
        status = STATUSES.AWAITING;
        this.userInfo = userInfo;
        this.createTime = LocalDateTime.now();
        this.cart = cart;
        this.expectedTime = createTime.plusSeconds(duration.getSeconds());
    }


    public STATUSES getStatus() {
        return status;
    }

    public void setStatus(STATUSES status) {
        this.status = status;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getExpectedTime() {
        return expectedTime;
    }

    public void setExpectedTime(LocalDateTime expectedTime) {
        this.expectedTime = expectedTime;
    }

    public Credentials getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(Credentials userInfo) {
        this.userInfo = userInfo;
    }

    public ShoppingCart getCart() {
        return cart;
    }

    public void setCart(ShoppingCart cart) {
        this.cart = cart;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Order { " + status + " from " + createTime + " to " + expectedTime + "; Cart {" + cart.showAll() + " } }";
    }
}
