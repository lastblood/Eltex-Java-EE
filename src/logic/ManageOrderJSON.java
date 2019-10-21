package logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import data.Order;

import java.io.File;
import java.io.IOException;

public class ManageOrderJSON extends AManageOrder {

    public ManageOrderJSON(Orders orders, File directory) {
        super(orders, directory);
        EXT = "json";
    }

    @Override
    public void saveOrderInFile(Order order, File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(file, order);
    }

    @Override
    public Order readOrderFromFile(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(file, Order.class);
    }
}
