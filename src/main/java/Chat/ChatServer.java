package Chat;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    static Set<ClientHandler> clients = Collections.synchronizedSet(new HashSet<>());

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(12345);
        System.out.println("Сервер запущен...");

        while (true) {
            Socket socket = serverSocket.accept();
            ClientHandler client = new ClientHandler(socket);
            clients.add(client);
            client.start();
        }
    }

    static void broadcast(String message, ClientHandler sender) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                if (client != sender) {
                    client.sendMessage(message);
                }
            }
        }
    }

    static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private String name;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void sendMessage(String message) {
            out.println(message);
        }

        @Override
        public void run() {
            try (
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ) {
                out = new PrintWriter(socket.getOutputStream(), true);

                out.println("Введите ваше имя:");
                name = in.readLine();
                if (name == null || name.isEmpty()) {
                    name = "Guest" + socket.getPort();
                }
                System.out.println(name + " подключился.");
                broadcast(name + " присоединился к чату.", this);

                String msg;
                while ((msg = in.readLine()) != null) {
                    if (msg.equalsIgnoreCase("/exit")) break;
                    System.out.println(name + ": " + msg);
                    broadcast(name + ": " + msg, this);
                }
            } catch (IOException e) {
                System.out.println(name + " отключился.");
            } finally {
                try {
                    socket.close();
                } catch (IOException ignored) {}
                clients.remove(this);
                broadcast(name + " покинул чат.", this);
            }
        }
    }
}
