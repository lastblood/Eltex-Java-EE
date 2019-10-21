package logic;

import data.*;
import logic.utils.UtilityRandom;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.*;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    static void server() throws SocketException {
        Orders<Order> orders = new Orders<>();
        ArrayList<Product> products = new ArrayList<>();

        List<ServerSocket> freeSockets = Collections.synchronizedList(new ArrayList<>());
        List<ServerSocket> usedSockets = Collections.synchronizedList(new ArrayList<>());

        ExecutorService executorService = Executors.newCachedThreadPool();

        try(DatagramSocket udpSocket = new DatagramSocket()) {

            while (true) {
                if(freeSockets.size() < 2) {
                    ServerSocket ss = new ServerSocket();
                    freeSockets.add(ss);
                }

                try {
                    byte[] data = new byte[1024];
                    data[0] = 1;
                    udpSocket.send(new DatagramPacket(data, 1024));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.interrupted();
                    e.printStackTrace();
                    continue;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    //TODO: откуда клиент знает список продуктов? Сам создает, хранит в себе или принимает от сервера?
    static void client() throws IOException {
        UtilityRandom random = new UtilityRandom();
        ArrayList<Product> products = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Product p = random.nextBoolean() ? new CoffeeProduct() : new TeaProduct();
            p.create();
            products.add(p);
        }
        Credentials credentials = Credentials.getRandomInstance();
        System.out.println("Successful initialization");


        try(DatagramSocket udpSocket = new DatagramSocket()) {

            while(true) {
                //Получаем пакет с портом и адресом
                InetAddress ip;
                int port;

                while(true) {
                    DatagramPacket packet = waitForUDPPacket(udpSocket);

                    byte[] data = packet.getData();
                    if(data[0] == 1) {
                        ip = packet.getAddress();
                        port = new BigInteger(data).intValueExact();
                        break;
                    }
                }

                System.out.println("Ip address and port received");

                Order order;

                //Пытаемся подключиться к нему, создаем Order и пытаемся его сразу же отправить
                try (Socket tcpSocket = new Socket(ip, port);
                     ObjectOutputStream oos = new ObjectOutputStream(tcpSocket.getOutputStream())) {
                    ShoppingCart<Product> cart = new ShoppingCart<>();
                    cart.addAll(random.nextElementsFrom(products, random.nextInt(5, 20)));

                    order = new Order(Duration.ofSeconds(random.nextInt(10, 30)), credentials, cart);
                    oos.writeObject(order);

                    System.out.println("Order was send");
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }

                waitForCorrectUUID(udpSocket, order.getId());
                System.out.println("Order completed successfully");

                //todo: Клиент "одноразовый"?
                break;
            }

        }
    }

    static DatagramPacket waitForUDPPacket(DatagramSocket socket) throws IOException {
        DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
        socket.receive(packet);
        return packet;
    }


    static boolean waitForCorrectUUID(DatagramSocket socket, UUID correctId) throws IOException {
        String idString = correctId.toString();

        while(true) {
            DatagramPacket packet = waitForUDPPacket(socket);
            byte[] data = packet.getData();
            if(data[0] == 1) {
                boolean result = idString.equals(new String(data, 1, 36));
                if(result) return result;
            }
        }
    }
}

