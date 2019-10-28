package logic.net;

import java.io.IOException;
import java.net.*;
import java.util.UUID;

public class UdpNotifier {
    private int tcpPort;
    private int udpPort;

    private int exceptionsPerRow = 0;
    public final Thread thread;
    private DatagramSocket udpSocket = null;

    private boolean needToBeClosed = false;
    public static InetAddress broadcastAdress;

    static {
        try {
            broadcastAdress = InetAddress.getByName("192.168.0.255");
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    //При создании класса сразу же создается новый поток, в нем открывается и обрабатывается сокет
    public UdpNotifier(int tcpPort, int udpPort) {
        if(tcpPort < 0 || tcpPort > 65535) throw new IllegalArgumentException();
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;

        thread = new Thread(this::run);
        thread.start();
    }

    private void run() {
        int portBU = -1;
        DatagramPacket packet = null;

        try(DatagramSocket socket = new DatagramSocket(0)) {
            socket.setBroadcast(true);
            udpSocket = socket;

            while (true) {
                if(needToBeClosed) break;

                if (packet == null || tcpPort != portBU) {
                    byte[] data = UDPData.encodePort(tcpPort);
                    portBU = tcpPort;
                    packet = new DatagramPacket(data, 0, data.length, broadcastAdress, 27091);
                }

                try {
                    socket.send(packet);
                    exceptionsPerRow = 0;
                } catch (IOException e) {
                    if (exceptionsPerRow > 10) {
                        socket.close();
                        System.exit(-1);
                    }

                    if(exceptionsPerRow % 10 == 0)
                        e.printStackTrace();
                    else
                        System.err.println(e.getClass());

                    exceptionsPerRow++;
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.interrupted();
                }

                exceptionsPerRow = 0;
            }
        } catch (SocketException e) {
            e.printStackTrace();
            exceptionsPerRow++;

            if(exceptionsPerRow > 3)
                System.exit(-1);
            else
                run(); //todo: Убрать рекурсию во избежание переполнения стека
        }
    }

    //Передает id ордера через тот же сокет. В случае, если он закрыт, возвращается ошибка
    public void sendOrderId(UUID id) {
        if(udpSocket == null || udpSocket.isClosed())
            throw new IllegalArgumentException("UDP socket isn't opened yet or already closed");

        DatagramPacket packet = null;
        byte[] data = UDPData.encodeUUID(id);
        packet = new DatagramPacket(data, 0, data.length, broadcastAdress, 27091);
        try {
            udpSocket.send(packet);
        } catch (IOException e) {
            e.printStackTrace(); //todo: в каких случаях возникает? повтор?
        }
    }

    public void setTcpPort(int tcpPort) {
        this.tcpPort = tcpPort;
    }

    //Передает сообщение о необходимости закрыть сокет
    public void close() {
        needToBeClosed = true;
    }
}
