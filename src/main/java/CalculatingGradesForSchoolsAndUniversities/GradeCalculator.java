package CalculatingGradesForSchoolsAndUniversities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class GradeCalculator extends JFrame {
    private JTextField nameField;
    private JTextField[] subjectFields;
    private JButton calculateButton;

    private java.util.List<Student> allStudents = new ArrayList<>();

    public GradeCalculator() {
        setTitle("Подсчет среднего балла");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new GridLayout(0, 2));

        add(new JLabel("Имя ученика:"));
        nameField = new JTextField();
        add(nameField);

        subjectFields = new JTextField[5]; // 5 предметов

        for (int i = 0; i < subjectFields.length; i++) {
            add(new JLabel("Оценка по предмету " + (i + 1) + ":"));
            subjectFields[i] = new JTextField();
            add(subjectFields[i]);
        }

        calculateButton = new JButton("Рассчитать");
        add(calculateButton);

        // Пустой элемент для выравнивания
        add(new JLabel(""));

        calculateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calculateAverage();
            }
        });

        setVisible(true);
    }

    private void calculateAverage() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Введите имя ученика.");
            return;
        }

        double sum = 0;
        int count = 0;
        try {
            for (JTextField field : subjectFields) {
                double grade = Double.parseDouble(field.getText().trim());
                if (grade < 0 || grade > 100) throw new Exception();
                sum += grade;
                count++;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Введите корректные оценки (0–100)!");
            return;
        }

        double average = sum / count;

        Student currentStudent = new Student(name, average);
        allStudents.add(currentStudent);

        // Сортировка по убыванию среднего балла
        allStudents.sort((s1, s2) -> Double.compare(s2.average, s1.average));

        int rank = allStudents.indexOf(currentStudent) + 1;

        JOptionPane.showMessageDialog(this,
                "Средний балл: " + String.format("%.2f", average) +
                        "\nМесто в рейтинге: " + rank + " из " + allStudents.size());
    }

    private static class Student {
        String name;
        double average;

        public Student(String name, double average) {
            this.name = name;
            this.average = average;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GradeCalculator());
    }
}
