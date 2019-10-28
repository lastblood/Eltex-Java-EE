package logic.net;

public class NetMain {
    public static void main(String[] args) throws Exception {
        if(args.length >= 1 && args[0].toLowerCase().contains("server")) {
            System.out.println("Starting server...");
            Server.server();
        } else {
            System.out.println("Starting client...");
            Client.client();
        }
    }
}
