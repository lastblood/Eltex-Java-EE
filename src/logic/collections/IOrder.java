package logic.collections;

import data.Order;

import java.io.IOException;
import java.util.UUID;

public interface IOrder {
    void saveById(UUID id) throws IOException;

    Order readById(UUID id) throws IOException, ClassNotFoundException;

    void saveAll() throws IOException;

    Orders readAll() throws IOException, ClassNotFoundException;
}
