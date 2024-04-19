import client.Client;
import client.Client2;
import server.Server;

public class MainServer {
    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}
