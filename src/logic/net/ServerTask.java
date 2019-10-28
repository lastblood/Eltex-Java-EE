package logic.net;

import data.Order;
import data.net.Request;
import data.net.Response;
import filesio.StreamJSON;
import logic.utils.UtilityRandom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;

//Принимает открытый сокет, получает Request от клиента, создает по нему Order, формирует и возвращает Response
public class ServerTask implements Callable<Order> {
    private Socket socket;

    public ServerTask(Socket socket) {
        if(socket.isClosed() || !socket.isConnected())
            throw new IllegalArgumentException();

        this.socket = socket;
    }

    @Override
    public Order call() throws Exception {
        Order order;

        //Соединение будет ждать ответа от клиента не более 5 минут (с учетом автоматизации клиента излишне)
        socket.setSoTimeout(300 * 1000);
        System.out.println("Connection created");

        try (DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream oos = new DataOutputStream(socket.getOutputStream())) {

            Request request = StreamJSON.read(input.readUTF(), Request.class);
            order = new Order(request, UtilityRandom.instance.nextDuration(5, 8));

            Response response = new Response(order.getId(), order.getCreateTime(), order.getExpectedTime());
            oos.writeUTF(StreamJSON.write(response));

            System.out.println("Order " + order.getId() + " has been created and sent");
        } finally {
            socket.close();
        }

        return order;
    }

}