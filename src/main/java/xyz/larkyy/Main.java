package xyz.larkyy;

public class Main {


    public static void main(String[] args) throws Exception {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 25566;
        }
        new TCPServer(port).run();
    }
}