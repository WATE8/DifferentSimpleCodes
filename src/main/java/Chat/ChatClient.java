package Chat;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345);
             Scanner scanner = new Scanner(System.in);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            System.out.println(in.readLine());
            String name = scanner.nextLine();
            out.println(name);

            new Thread(() -> {
                try {
                    String msg;
                    while ((msg = in.readLine()) != null) {
                        System.out.println(msg);
                    }
                } catch (IOException ignored) {}
            }).start();

            while (true) {
                String msg = scanner.nextLine();
                if (msg.equalsIgnoreCase("/exit")) {
                    out.println(msg);
                    break;
                }
                out.println(msg);
            }

        } catch (IOException e) {
            System.out.println("Ошибка подключения: " + e.getMessage());
        }
    }
}
