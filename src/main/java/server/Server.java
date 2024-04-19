package server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Server {
    private ServerSocket serverSocket;
    private int port;
    private List<ClientHandler> clients = new ArrayList<>();

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
                clients.add(clientHandler);
                Thread thread = new Thread(clientHandler);
                thread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int getPort() {
        return port;
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

                    BufferedWriter logWriter = new BufferedWriter(new FileWriter("log_server.txt", true));
                    logWriter.write(username + " " + new Date() + ": " + message + "\n");
                    logWriter.close();

                    for (ClientHandler client : clients) {
                        if (client != this) {
                            client.sendMessage(username, message);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void sendMessage(String username, String message) {
            out.println(username + ": " + message);
        }
    }
}

