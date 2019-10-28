package filesio;

import data.Order;
import logic.collections.IOrder;
import logic.collections.Orders;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

public abstract class AManageOrder implements IOrder {
    public final File path;
    public final Orders orders;
    public String EXT;

    public AManageOrder(Orders orders, File directory) {
        path = directory;
        this.orders = orders;
    }

    public File createPathByUUID(UUID id, String extension) throws IOException {
        return new File(path.getCanonicalPath() + "\\" + id.toString() + "." + extension);
    }

    public abstract void saveOrderInFile(Order order, File file) throws IOException;

    public abstract Order readOrderFromFile(File file) throws IOException, ClassNotFoundException;

    @Override
    public void saveById(UUID id) throws IOException {
        saveOrderInFile(orders.orderById(id), createPathByUUID(id, EXT));
    }

    @Override
    public void saveAll() throws IOException {
        Collection<Order> snapshot = orders.snapshot();
        for(Order x : snapshot)
            saveOrderInFile(x, createPathByUUID(x.getId(), EXT));
    }

    @Override
    public Order readById(UUID id) throws IOException, ClassNotFoundException {
        return readOrderFromFile(createPathByUUID(id, EXT));
    }

    @Override
    public Orders readAll() throws IOException, ClassNotFoundException {
        Orders result = new Orders();

        File[] files = path.listFiles(x -> x.getName().endsWith(EXT));
        if(files == null) return result;

        for(File f : files)
            result.add(readOrderFromFile(f));

        return result;
    }
}
