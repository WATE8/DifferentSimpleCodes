package Chat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;

public class ChatClientGUI extends JFrame {

    private JTextArea chatArea = new JTextArea();
    private JTextField inputField = new JTextField();
    private JButton sendButton = new JButton("Отправить");

    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;

    private String name;

    public ChatClientGUI(String host, int port) {
        setTitle("Чат");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        try {
            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String requestNameMsg = in.readLine();
            name = JOptionPane.showInputDialog(this, requestNameMsg, "Введите имя", JOptionPane.PLAIN_MESSAGE);
            if (name == null || name.trim().isEmpty()) {
                name = "Guest" + socket.getLocalPort();
            }
            out.println(name);

            new Thread(() -> {
                try {
                    String msg;
                    while ((msg = in.readLine()) != null) {
                        chatArea.append(msg + "\n");
                        chatArea.setCaretPosition(chatArea.getDocument().getLength());
                    }
                } catch (IOException e) {
                    showError("Соединение потеряно.");
                }
            }).start();

        } catch (IOException e) {
            showError("Не удалось подключиться к серверу: " + e.getMessage());
            System.exit(1);
        }

        sendButton.addActionListener(e -> sendMessage());

        inputField.addActionListener(e -> sendMessage());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    out.println("/exit");
                    socket.close();
                } catch (IOException ignored) {}
            }
        });
    }

    private void sendMessage() {
        String msg = inputField.getText().trim();
        if (!msg.isEmpty()) {
            out.println(msg);
            inputField.setText("");
            if (msg.equalsIgnoreCase("/exit")) {
                try {
                    socket.close();
                } catch (IOException ignored) {}
                System.exit(0);
            }
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChatClientGUI client = new ChatClientGUI("localhost", 12345);
            client.setVisible(true);
        });
    }
}
