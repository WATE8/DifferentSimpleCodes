package WordCountingProgram;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

import org.apache.poi.xwpf.usermodel.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.text.*;
import org.jsoup.*;

public class WordCounter extends JFrame {
    private JTextArea textArea;
    private JLabel wordCountLabel;
    private JLabel charWithSpacesLabel;
    private JLabel charWithoutSpacesLabel;
    private JButton loadButton, countButton;

    public WordCounter() {
        setTitle("Программа подсчета слов и символов");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridLayout(3, 1));
        wordCountLabel = new JLabel("Слов: 0");
        charWithSpacesLabel = new JLabel("Символов (с пробелами): 0");
        charWithoutSpacesLabel = new JLabel("Символов (без пробелов): 0");

        bottomPanel.add(wordCountLabel);
        bottomPanel.add(charWithSpacesLabel);
        bottomPanel.add(charWithoutSpacesLabel);
        add(bottomPanel, BorderLayout.SOUTH);

        JPanel topPanel = new JPanel();
        loadButton = new JButton("Загрузить файл");
        countButton = new JButton("Подсчитать");

        topPanel.add(loadButton);
        topPanel.add(countButton);
        add(topPanel, BorderLayout.NORTH);

        loadButton.addActionListener(e -> loadFile());
        countButton.addActionListener(e -> countWords());

        setVisible(true);
    }

    private void loadFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = chooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            String path = file.getAbsolutePath().toLowerCase();

            try {
                if (path.endsWith(".txt") || path.endsWith(".md")) {
                    loadTxt(file);
                } else if (path.endsWith(".docx")) {
                    loadDocx(file);
                } else if (path.endsWith(".pdf")) {
                    loadPdf(file);
                } else if (path.endsWith(".html") || path.endsWith(".htm")) {
                    loadHtml(file);
                } else {
                    JOptionPane.showMessageDialog(this, "Неподдерживаемый формат файла.");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Ошибка при чтении файла: " + e.getMessage());
            }
        }
    }

    private void loadTxt(File file) throws IOException {
        textArea.setText("");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            textArea.append(line + "\n");
        }
        reader.close();
    }

    private void loadDocx(File file) throws Exception {
        textArea.setText("");
        FileInputStream fis = new FileInputStream(file);
        XWPFDocument doc = new XWPFDocument(fis);
        for (XWPFParagraph p : doc.getParagraphs()) {
            textArea.append(p.getText() + "\n");
        }
        doc.close();
        fis.close();
    }

    private void loadPdf(File file) throws IOException {
        textArea.setText("");
        PDDocument document = PDDocument.load(file);
        PDFTextStripper stripper = new PDFTextStripper();
        textArea.setText(stripper.getText(document));
        document.close();
    }

    private void loadHtml(File file) throws IOException {
        textArea.setText("");
        org.jsoup.nodes.Document doc = Jsoup.parse(file, "UTF-8");
        String text = doc.body().text();
        textArea.setText(text);
    }

    private void countWords() {
        String text = textArea.getText().trim();

        String[] words = text.split("\\s+");
        int wordCount = text.isEmpty() ? 0 : words.length;

        int charsWithSpaces = text.length();
        int charsWithoutSpaces = text.replace(" ", "").replace("\n", "").replace("\t", "").length();

        wordCountLabel.setText("Слов: " + wordCount);
        charWithSpacesLabel.setText("Символов (с пробелами): " + charsWithSpaces);
        charWithoutSpacesLabel.setText("Символов (без пробелов): " + charsWithoutSpaces);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(WordCounter::new);
    }
}
