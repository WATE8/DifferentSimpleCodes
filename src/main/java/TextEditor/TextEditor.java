package TextEditor;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.*;
import java.awt.*;
import java.io.*;
public class TextEditor extends JFrame {
    private JTextPane textPane;
    private JFileChooser fileChooser;

    public TextEditor() {
        setTitle("Текстовый редактор");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        textPane = new JTextPane();
        JScrollPane scrollPane = new JScrollPane(textPane);
        add(scrollPane, BorderLayout.CENTER);

        createMenuBar();

        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("Файл");
        JMenuItem openItem = new JMenuItem("Открыть");
        JMenuItem saveItem = new JMenuItem("Сохранить");
        JMenuItem exportHtmlItem = new JMenuItem("Экспорт в HTML");

        openItem.addActionListener(e -> openFile());
        saveItem.addActionListener(e -> saveFile());
        exportHtmlItem.addActionListener(e -> exportToHtml());

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(exportHtmlItem);

        JMenu formatMenu = new JMenu("Формат");
        JMenuItem boldItem = new JMenuItem("Жирный");
        JMenuItem italicItem = new JMenuItem("Курсив");

        boldItem.addActionListener(e -> applyStyle(StyleConstants.Bold));
        italicItem.addActionListener(e -> applyStyle(StyleConstants.Italic));

        formatMenu.add(boldItem);
        formatMenu.add(italicItem);

        menuBar.add(fileMenu);
        menuBar.add(formatMenu);

        setJMenuBar(menuBar);
    }

    private void applyStyle(Object styleAttribute) {
        int start = textPane.getSelectionStart();
        int end = textPane.getSelectionEnd();
        if (start == end) return;

        StyledDocument doc = textPane.getStyledDocument();
        AttributeSet currentAttributes = textPane.getCharacterAttributes();
        SimpleAttributeSet newAttributes = new SimpleAttributeSet(currentAttributes);

        if (styleAttribute == StyleConstants.Bold) {
            boolean isBold = StyleConstants.isBold(currentAttributes);
            StyleConstants.setBold(newAttributes, !isBold);
        } else if (styleAttribute == StyleConstants.Italic) {
            boolean isItalic = StyleConstants.isItalic(currentAttributes);
            StyleConstants.setItalic(newAttributes, !isItalic);
        }

        doc.setCharacterAttributes(start, end - start, newAttributes, false);
    }

    private void openFile() {
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileChooser.getSelectedFile()))) {
                textPane.setText("");
                String line;
                while ((line = reader.readLine()) != null) {
                    textPane.getDocument().insertString(textPane.getDocument().getLength(), line + "\n", null);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ошибка при открытии файла", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveFile() {
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (PrintWriter writer = new PrintWriter(fileChooser.getSelectedFile())) {
                writer.print(textPane.getText());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ошибка при сохранении", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exportToHtml() {
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (PrintWriter writer = new PrintWriter(file)) {
                writer.println("<html><body>");
                StyledDocument doc = textPane.getStyledDocument();
                for (int i = 0; i < doc.getLength(); ) {
                    Element elem = doc.getCharacterElement(i);
                    AttributeSet as = elem.getAttributes();
                    String text = doc.getText(i, elem.getEndOffset() - i);

                    if (StyleConstants.isBold(as)) {
                        text = "<b>" + text + "</b>";
                    }
                    if (StyleConstants.isItalic(as)) {
                        text = "<i>" + text + "</i>";
                    }
                    writer.print(text.replace("\n", "<br>"));
                    i = elem.getEndOffset();
                }
                writer.println("</body></html>");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ошибка при экспорте", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TextEditor().setVisible(true));
    }
}
