package TaskManager;


import java.util.ArrayList;
import java.util.Scanner;

public class TaskManager {
    static class Task {
        String title;
        boolean completed;

        Task(String title) {
            this.title = title;
            this.completed = false;
        }

        void markCompleted() {
            completed = true;
        }

        public String toString() {
            return (completed ? "[✔] " : "[ ] ") + title;
        }
    }

    static ArrayList<Task> tasks = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== Task Manager ===");

        while (true) {
            showMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> addTask();
                case "2" -> listTasks();
                case "3" -> completeTask();
                case "4" -> removeTask();
                case "5" -> {
                    System.out.println("Выход...");
                    return;
                }
                default -> System.out.println("Неверный выбор. Введите число от 1 до 5.");
            }
        }
    }

    static void showMenu() {
        System.out.println("\nМеню:");
        System.out.println("1 - Добавить задачу");
        System.out.println("2 - Показать задачи");
        System.out.println("3 - Отметить задачу выполненной");
        System.out.println("4 - Удалить задачу");
        System.out.println("5 - Выйти");
        System.out.print("Ваш выбор: ");
    }

    static void addTask() {
        System.out.print("Введите название задачи: ");
        String title = scanner.nextLine();
        tasks.add(new Task(title));
        System.out.println("Задача добавлена!");
    }

    static void listTasks() {
        if (tasks.isEmpty()) {
            System.out.println("Нет задач.");
            return;
        }

        System.out.println("Ваши задачи:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, tasks.get(i));
        }
    }

    static void completeTask() {
        listTasks();
        if (tasks.isEmpty()) return;

        System.out.print("Введите номер задачи, которую хотите завершить: ");
        int index = readIndex();
        if (isValidIndex(index)) {
            tasks.get(index).markCompleted();
            System.out.println("Задача завершена!");
        }
    }

    static void removeTask() {
        listTasks();
        if (tasks.isEmpty()) return;

        System.out.print("Введите номер задачи для удаления: ");
        int index = readIndex();
        if (isValidIndex(index)) {
            tasks.remove(index);
            System.out.println("Задача удалена!");
        }
    }

    static int readIndex() {
        try {
            return Integer.parseInt(scanner.nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: Введите число.");
            return -1;
        }
    }

    static boolean isValidIndex(int index) {
        if (index < 0 || index >= tasks.size()) {
            System.out.println("Неверный номер.");
            return false;
        }
        return true;
    }
}
