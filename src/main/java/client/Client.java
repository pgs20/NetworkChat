package client;

import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public Client() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("settings.txt"));
            int port = Integer.parseInt(br.readLine());
            String serverAddress = "localhost";
            br.close();

            socket = new Socket(serverAddress, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your username: ");
            String username = scanner.nextLine();
            out.println(username);

            String message;
            while (true) {
                message = scanner.nextLine();
                out.println(message);

                BufferedWriter logWriter = new BufferedWriter(new FileWriter("log_client.txt", true));
                logWriter.write(username + " " + new Date() + ": " + message + "\n");
                logWriter.close();

                if ("/exit".equals(message)) {
                    break;
                }
            }

            socket.close();
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }
}
