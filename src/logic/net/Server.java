package logic.net;

import data.Order;
import logic.collections.Orders;
import logic.threads.AwaitingCheck;
import logic.threads.CompletedCheck;
import logic.threads.OrderNotifier;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Server {
    public static void server() throws IOException {
        ServerSocket ss = new ServerSocket(0);
        System.out.println("ServerSocket's port : " + ss.getLocalPort());
        UdpNotifier notifier = new UdpNotifier(ss.getLocalPort(), 27092);

        Orders orders = new Orders();
        ExecutorService executors = Executors.newCachedThreadPool();
        List<Future<Order>> resultsList = Collections.synchronizedList(new ArrayList<>());
        ClientDataController controller = new ClientDataController(resultsList, orders);

        AwaitingCheck awaitingCheck = new AwaitingCheck(orders, 3);
        CompletedCheck completedCheck = new CompletedCheck(orders, 3);
        OrderNotifier orderNotifier = new OrderNotifier(orders, 3, notifier);

        //В этом потоке только обрабатываются запросы к ServerSocket'у
        while(true) {
            try {
                Socket socket = ss.accept();
                resultsList.add(executors.submit(new ServerTask(socket)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Starting server");
        server();
    }
}