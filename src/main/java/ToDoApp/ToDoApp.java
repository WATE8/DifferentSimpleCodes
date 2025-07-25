package ToDoApp;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
public class ToDoApp extends JFrame {
    DefaultListModel<String> listModel = new DefaultListModel<>();
    JList<String> taskList = new JList<>(listModel);
    ArrayList<Boolean> completed = new ArrayList<>();
    
    public ToDoApp() {
        setTitle("To-Do App");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BorderLayout());

        JTextField taskField = new JTextField();
        JButton addButton = new JButton("Добавить");
        JButton completeButton = new JButton("Завершить");
        JButton removeButton = new JButton("Удалить");

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(taskField, BorderLayout.CENTER);
        topPanel.add(addButton, BorderLayout.EAST);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(completeButton);
        bottomPanel.add(removeButton);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(taskList), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> {
            String task = taskField.getText().trim();
            if (!task.isEmpty()) {
                listModel.addElement("[ ] " + task);
                completed.add(false);
                taskField.setText("");
            }
        });

        completeButton.addActionListener(e -> {
            int index = taskList.getSelectedIndex();
            if (index != -1 && !completed.get(index)) {
                String item = listModel.get(index).substring(4);
                listModel.set(index, "[✔] " + item);
                completed.set(index, true);
            }
        });

        removeButton.addActionListener(e -> {
            int index = taskList.getSelectedIndex();
            if (index != -1) {
                listModel.remove(index);
                completed.remove(index);
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ToDoApp::new);
    }
}
