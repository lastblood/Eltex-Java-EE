package logic.net;

import data.Order;
import logic.collections.Orders;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


//Обрабатывает приходящие Order из коллекции Future, добавляет их в Orders
public class ClientDataController implements Runnable {
    private List<Future<Order>> futureList; //TODO: заменить на set?
    private Orders orders;
    private Thread thread;

    public ClientDataController(List<Future<Order>> futureList, Orders orders) {
        this.futureList = futureList;
        this.orders = orders;

        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while(true) {
            List<Future<Order>> removeList = new ArrayList<>();

            for(int i = 0; i < futureList.size(); i++) {
                Future<Order> future = futureList.get(i);

                if(future.isDone()) { //TODO: сделать проверку на длительность исполнения?
                    try { //TODO: обрабатывать исключения более корректно? Попросить клиента о повторе?
                        Order order = future.get(10, TimeUnit.MILLISECONDS);
                        orders.add(order);
                    } catch (InterruptedException | TimeoutException e) { //как?
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        System.out.println("Execution exception");
                        e.printStackTrace();
                    }
                    removeList.add(future);
                } else if(future.isCancelled()) {
                    System.err.println("Future was cancelled " + future);
                    removeList.add(future);
                }

                futureList.removeAll(removeList);
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.interrupted();
            }
        }
    }

    public Thread getThread() {
        return thread;
    }
}