package logic.net;

import data.CoffeeProduct;
import data.Credentials;
import data.Product;
import data.TeaProduct;
import data.net.Request;
import data.net.Response;
import filesio.StreamJSON;
import logic.collections.ShoppingCart;
import logic.utils.UtilityRandom;

import java.io.*;
import java.net.*;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.UUID;


public class Client {
    //TODO: откуда клиент знает список продуктов? Сам создает, хранит в себе или принимает от сервера?
    public static void client() throws IOException {
        UtilityRandom random = new UtilityRandom();
        ArrayList<Product> products = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Product p = random.nextBoolean() ? new CoffeeProduct() : new TeaProduct();
            p.create();
            products.add(p);
        }
        Credentials credentials = Credentials.getRandomInstance();
        System.out.println("Successful initialization");


        try(DatagramSocket udpSocket = new DatagramSocket(27091)) {
            udpSocket.setSoTimeout(10000); //todo: ловить исключение и выдавать сообщение об отсутствии сервера

            while(true) {
                //Получаем пакет с портом и адресом
                InetAddress ip;
                int port;

                while (true) {
                    try {
                        DatagramPacket packet = waitForUDPPacket(udpSocket);
                        byte[] data = packet.getData();

                        if (data[0] == 1) {
                            ip = packet.getAddress();
                            port = UDPData.decodePort(data);
                            break;
                        }
                    } catch (SocketTimeoutException ste) {
                        System.out.println(ste.getClass());
                    }
                }

                System.out.println(ip + ":" + port);

                UUID orderID = null;
                ShoppingCart<Product> cart;

                try (Socket tcpSocket = new Socket(ip, port);
                     DataInputStream input = new DataInputStream(tcpSocket.getInputStream());
                     DataOutputStream output = new DataOutputStream(tcpSocket.getOutputStream())) {

                    cart = new ShoppingCart<>();
                    cart.addAll(random.nextElementsFrom(products, random.nextInt(5, 7)));

                    Request request = new Request(credentials, cart);
                    output.writeUTF(StreamJSON.write(request));

                    Response r = StreamJSON.read(input.readUTF(), Response.class);
                    orderID = r.getOrderID();

                    System.out.println("Order #" + r.getOrderID() +
                            " was created at " + r.getCreatedTime().atZone(ZoneId.systemDefault()) +
                            " and expected at " + r.getExpectedTime().atZone(ZoneId.systemDefault()));
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }

                System.out.println("Waiting for completion");
                while(true) {
                    DatagramPacket packet = waitForUDPPacket(udpSocket);
                    byte[] data = packet.getData();

                    if(data[0] == 0) {
                        if(UDPData.decodeUUID(data).equals(orderID)) {
                            System.out.println("Order " + orderID + " completed successfully\n");
                            break;
                        }
                    }
                }

                //Клиент "одноразовый"?
                //break;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Starting client");
        client();
    }


    public static DatagramPacket waitForUDPPacket(DatagramSocket socket) throws IOException {
        DatagramPacket packet = new DatagramPacket(new byte[64], 64);
        socket.receive(packet);
        return packet;
    }
}
