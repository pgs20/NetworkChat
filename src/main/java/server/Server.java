package server;

import java.io.*;
import java.net.*;
import java.util.Date;

public class Server {
    private ServerSocket serverSocket;
    private int port;

    public Server() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("settings.txt"));
            port = Integer.parseInt(br.readLine());
            br.close();
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket);

                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                String username = in.readLine();
                String message;

                while ((message = in.readLine()) != null) {
                    System.out.println("Message from '" + username + "': " + message);

                    // Save message to log file
                    BufferedWriter logWriter = new BufferedWriter(new FileWriter("log.txt", true));
                    logWriter.write(username + " " + new Date() + ": " + message + "\n");
                    logWriter.close();

                    // Echo message back to client
                    out.println("Message received: " + message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}
