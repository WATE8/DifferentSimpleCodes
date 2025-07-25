package AccountingForPharmacies;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class PharmacyManager extends JFrame {
    private JTextField nameField, quantityField, priceField, searchField;
    private JTable table;
    private DefaultTableModel tableModel;

    public PharmacyManager() {
        setTitle("Учет лекарств в аптеке");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Верхняя панель: ввод данных
        JPanel inputPanel = new JPanel(new GridLayout(2, 4, 5, 5));
        nameField = new JTextField();
        quantityField = new JTextField();
        priceField = new JTextField();
        JButton addButton = new JButton("Добавить");

        inputPanel.add(new JLabel("Название лекарства:"));
        inputPanel.add(new JLabel("Количество:"));
        inputPanel.add(new JLabel("Цена за единицу:"));
        inputPanel.add(new JLabel(""));
        inputPanel.add(nameField);
        inputPanel.add(quantityField);
        inputPanel.add(priceField);
        inputPanel.add(addButton);

        add(inputPanel, BorderLayout.NORTH);

        // Таблица
        String[] columns = {"Название", "Количество", "Цена за ед.", "Сумма"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Нижняя панель: поиск и итог
        JPanel bottomPanel = new JPanel(new BorderLayout());
        searchField = new JTextField();
        JButton searchButton = new JButton("Поиск");
        JButton totalButton = new JButton("Общая сумма");

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(new JLabel("Поиск по названию: "), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        bottomPanel.add(searchPanel, BorderLayout.NORTH);
        bottomPanel.add(totalButton, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        // События
        addButton.addActionListener(e -> addMedicine());
        searchButton.addActionListener(e -> searchMedicine());
        totalButton.addActionListener(e -> showTotal());

        setVisible(true);
    }

    private void addMedicine() {
        String name = nameField.getText().trim();
        String qtyText = quantityField.getText().trim();
        String priceText = priceField.getText().trim();

        if (name.isEmpty() || qtyText.isEmpty() || priceText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Пожалуйста, заполните все поля.");
            return;
        }

        try {
            int quantity = Integer.parseInt(qtyText);
            double price = Double.parseDouble(priceText);
            double total = quantity * price;

            tableModel.addRow(new Object[]{name, quantity, price, total});
            nameField.setText("");
            quantityField.setText("");
            priceField.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Введите корректные числовые значения.");
        }
    }

    private void searchMedicine() {
        String keyword = searchField.getText().trim().toLowerCase();
        if (keyword.isEmpty()) return;

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String medicine = tableModel.getValueAt(i, 0).toString().toLowerCase();
            if (medicine.contains(keyword)) {
                table.setRowSelectionInterval(i, i);
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Лекарство не найдено.");
    }

    private void showTotal() {
        int totalQty = 0;
        double totalSum = 0;

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int qty = Integer.parseInt(tableModel.getValueAt(i, 1).toString());
            double sum = Double.parseDouble(tableModel.getValueAt(i, 3).toString());

            totalQty += qty;
            totalSum += sum;
        }

        JOptionPane.showMessageDialog(this, "Общее количество: " + totalQty +
                "\nОбщая стоимость: " + String.format("%.2f", totalSum));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PharmacyManager::new);
    }
}
