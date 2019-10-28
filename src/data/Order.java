package data;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import filesio.InstantDeserializer;
import filesio.InstantSerializer;
import logic.collections.ShoppingCart;
import data.net.Request;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public class Order implements Serializable {

    public enum STATUSES {
        AWAITING,
        PROCESSED,
        COMPLETED
    }

    private final UUID id;

    private ShoppingCart cart;
    private STATUSES status;

    private Credentials userInfo;

    @JsonSerialize(using = InstantSerializer.class)
    @JsonDeserialize(using = InstantDeserializer.class)
    private Instant createTime;

    @JsonSerialize(using = InstantSerializer.class)
    @JsonDeserialize(using = InstantDeserializer.class)
    private Instant expectedTime;


    public Order(Duration duration, Credentials userInfo, ShoppingCart cart) {
        id = UUID.randomUUID();
        status = STATUSES.AWAITING;
        this.userInfo = userInfo;
        this.createTime = Instant.now();
        this.cart = cart;
        this.expectedTime = createTime.plusSeconds(duration.getSeconds());
    }

    public Order(Request request, Duration duration) {
        id = UUID.randomUUID();
        status = STATUSES.AWAITING;
        this.createTime = Instant.now();
        this.expectedTime = createTime.plusSeconds(duration.toSeconds());
        this.cart = request.getCart();
        this.userInfo = request.getCredentials();
    }

    //for deserialization only
    private Order() {
        id = UUID.randomUUID();
    }

    public STATUSES getStatus() {
        return status;
    }

    public void setStatus(STATUSES status) {
        this.status = status;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Instant createTime) {
        this.createTime = createTime;
    }

    public Instant getExpectedTime() {
        return expectedTime;
    }

    public void setExpectedTime(Instant expectedTime) {
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
        return "Order { #" + id  + " " + status + " from " + createTime + " to " + expectedTime + "; Cart {" + cart.showAll() + " } }";
    }
}


/*
package data;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import logic.collections.ShoppingCart;
import data.net.Request;

import java.io.IOException;
import java.io.Serializable;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Order implements Serializable {

    public enum STATUSES {
        AWAITING,
        PROCESSED
    }

    private final UUID id;

    private ShoppingCart cart;
    private STATUSES status;

    private Credentials userInfo;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime expectedTime;


    public Order(Duration duration, Credentials userInfo, ShoppingCart cart) {
        id = UUID.randomUUID();
        status = STATUSES.AWAITING;
        this.userInfo = userInfo;
        this.createTime = LocalDateTime.now(ZoneId.of("UTC"));
        this.cart = cart;
        this.expectedTime = createTime.plusSeconds(duration.getSeconds());
    }

    public Order(Request request, Duration duration) {
        id = UUID.randomUUID();
        status = STATUSES.AWAITING;
        this.createTime = LocalDateTime.now();
        this.expectedTime = createTime.plusSeconds(duration.toSeconds());
        this.cart = request.getCart();
        this.userInfo = request.getCredentials();
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


class LocalDateTimeSerializer extends StdSerializer<LocalDateTime> {

    protected LocalDateTimeSerializer() {
        super(LocalDateTime.class);
    }

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        String svalue = value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME); //todo: какой форматтер использовать?
        gen.writeString(svalue);
    }
}


class LocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {

    protected LocalDateTimeDeserializer() {
        super(LocalDateTime.class);
    }

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String value = p.getText();
        return LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME); //todo: какой форматтер использовать?
    }
}*/
