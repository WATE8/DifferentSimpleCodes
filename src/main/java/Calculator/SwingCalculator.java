package Calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import net.objecthunter.exp4j.operator.Operator;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.function.Function;

public class SwingCalculator extends JFrame {
    private JTextField display;
    private StringBuilder currentExpression = new StringBuilder();
    private DefaultListModel<String> historyModel = new DefaultListModel<>();
    private JList<String> historyList = new JList<>(historyModel);
    private List<String> history = new ArrayList<>();

    public SwingCalculator() {
        setTitle("Калькулятор");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(450, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        display = new JTextField();
        display.setFont(new Font("Arial", Font.BOLD, 24));
        display.setEditable(false);
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        add(display, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(6, 4, 5, 5));
        String[] buttons = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", ".", "=", "+",
                "C", "(", ")", "sqrt",
                "!", "^", "←", "?"
        };

        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.BOLD, 14));
            button.addActionListener(new ButtonClickListener());
            panel.add(button);
        }

        add(panel, BorderLayout.CENTER);

        JScrollPane historyPane = new JScrollPane(historyList);
        historyPane.setPreferredSize(new Dimension(150, 0));
        add(historyPane, BorderLayout.EAST);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                saveHistoryToFile();
            }
        });

        loadHistoryFromFile();
    }

    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();

            switch (cmd) {
                case "=":
                    calculateResult();
                    break;
                case "C":
                    currentExpression.setLength(0);
                    display.setText("");
                    break;
                case "sqrt":
                    currentExpression.insert(0, "sqrt(").append(")");
                    display.setText(currentExpression.toString());
                    break;
                case "!":
                    currentExpression.append("!");
                    display.setText(currentExpression.toString());
                    break;
                case "←":
                    if (currentExpression.length() > 0) {
                        currentExpression.deleteCharAt(currentExpression.length() - 1);
                        display.setText(currentExpression.toString());
                    }
                    break;
                case "?":
                    showHelp();
                    break;
                default:
                    currentExpression.append(cmd);
                    display.setText(currentExpression.toString());
            }
        }
    }

    private void calculateResult() {
        try {
            String expr = currentExpression.toString();

            Expression expression = new ExpressionBuilder(expr)
                    .operator(new Operator("!", 1, true, Operator.PRECEDENCE_POWER + 1) {
                        @Override
                        public double apply(double... args) {
                            int n = (int) args[0];
                            if (n < 0) throw new IllegalArgumentException("Факториал отрицательного числа");
                            double result = 1;
                            for (int i = 1; i <= n; i++) result *= i;
                            return result;
                        }
                    })
                    .functions(new Function("sqrt", 1) {
                        @Override
                        public double apply(double... args) {
                            return Math.sqrt(args[0]);
                        }
                    })
                    .build();

            double result = expression.evaluate();
            String resultStr = String.valueOf(result);

            String full = expr + " = " + resultStr;
            display.setText(resultStr);
            currentExpression.setLength(0);
            currentExpression.append(resultStr);

            historyModel.addElement(full);
            history.add(full);
        } catch (Exception e) {
            display.setText("Ошибка");
            currentExpression.setLength(0);
        }
    }

    private void showHelp() {
        String help = """
                📘 Поддерживаемые функции:

                • +, -, *, / — стандартные операции
                • ^ — возведение в степень (пример: 2^3 = 8)
                • sqrt(x) — квадратный корень (пример: sqrt(9) = 3)
                • ! — факториал (пример: 5! = 120)
                • Скобки ( и ) поддерживаются
                • ← — удалить один символ
                • C — очистить всё
                • ? — справка (вы видите её сейчас)

                История сохраняется в файл "history.txt"
                """;
        JOptionPane.showMessageDialog(this, help, "Справка", JOptionPane.INFORMATION_MESSAGE);
    }

    private void saveHistoryToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("history.txt"))) {
            for (String line : history) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении истории: " + e.getMessage());
        }
    }

    private void loadHistoryFromFile() {
        File file = new File("history.txt");
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                historyModel.addElement(line);
                history.add(line);
            }
        } catch (IOException e) {
            System.out.println("Ошибка при загрузке истории: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SwingCalculator().setVisible(true));
    }
}
