package logic;

import data.*;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Main {

    /*public static void main(String[] args) {
        switch (args[0]) {
            case "server":
                break;
            case "client":
                break;
        }




        while(true) {
            //А нужен ли этот общий буфер?
            System.out.println(LocalDateTime.now() + "\n" + orders.toString());

            String s = scan.next();
            switch (s.toLowerCase()) {
                case "status":
                case "info":
                    break;
                case "awaiting":
                case "check":
                    awaitingCheck.startCheck();
                    break;
                case "create":
                case "random":
                    createCheck.startCheck();
                    break;
                case "delete":
                case "processed":
                    processedCheck.startCheck();
                    break;
                case "exit":
                case "quit":
                    System.exit(0);
                default:
                    System.out.println("Wrong command");
            }
        }
    }*/

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


    class TcpSocketRunnable implements Runnable {
        ServerSocket serverSocket;
        List<ServerSocket> free;
        List<ServerSocket> used;

        public TcpSocketRunnable(List<ServerSocket> free, List<ServerSocket> used) {
            this.serverSocket = serverSocket;
            this.free = free;
            this.used = used;
        }

        @Override
        public void run() {

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


        /*Scanner scan = new Scanner(System.in);

        System.out.print("Enter count of products: ");
        int count = scan.nextInt();

        for (int i = 0; i < count; i++) {
            try {
                boolean random = false;
                System.out.print("Enter product #" + (i+1) + " type: ");
                String s = scan.next();
                if(s.equalsIgnoreCase("random") || s.equalsIgnoreCase("r")) {
                    s = new String[]{"tea", "coffee"}[(int) Math.round(Math.random())];
                    random = true;
                }

                Product p = new ProductFactory(s).create();
                if(random)
                    p.create();
                else
                    p.update(); //todo: неверное разделение обязанностей, потанцевальный конфликт сканеров

                p.read();

                products.add(p);
            } catch (Exception e) {
                System.err.println("Input exception");
            }
        }

        ShoppingCart<Product> cart = new ShoppingCart<>();
        for (Product temp : products) {
            System.out.println("How many " + temp.getName() + " need to be added in order?");
            int countOfProduct = scan.nextInt();
            for (int j = 0; j < countOfProduct; j++)
                cart.add(temp);
        }*/

//        Order order = new Order(Duration.ofSeconds(20), credentials, cart);


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

