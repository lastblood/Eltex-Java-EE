package logic;

import data.Order;

import java.io.*;

public class ManagerOrderFile extends AManageOrder {
    public ManagerOrderFile(Orders orders, File directory) {
        super(orders, directory);
        EXT = "bin";
    }

    @Override
    public void saveOrderInFile(Order order, File file) throws IOException {
        try(ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file))) {
            output.writeObject(order);
        }
    }

    @Override
    public Order readOrderFromFile(File file) throws IOException, ClassNotFoundException {
        try(ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))) {
            return (Order) input.readObject();
        }
    }
}
