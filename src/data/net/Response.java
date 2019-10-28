package data.net;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import filesio.InstantDeserializer;
import filesio.InstantSerializer;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class Response implements Serializable {
    private UUID orderID;

    @JsonSerialize(using = InstantSerializer.class)
    @JsonDeserialize(using = InstantDeserializer.class)
    private Instant createdTime;

    @JsonSerialize(using = InstantSerializer.class)
    @JsonDeserialize(using = InstantDeserializer.class)
    private Instant expectedTime;

    public Response(UUID orderID, Instant createdTime, Instant expectedTime) {
        this.orderID = orderID;
        this.createdTime = createdTime;
        this.expectedTime = expectedTime;
    }

    private Response(){}

    public Instant getCreatedTime() {
        return createdTime;
    }

    public Instant getExpectedTime() {
        return expectedTime;
    }

    public UUID getOrderID() {
        return orderID;
    }

}