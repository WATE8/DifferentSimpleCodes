package BookAccountingInTheLibrary;

import java.util.Scanner;

public class LibraryManager {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        LibraryService.initDatabase();

        System.out.println("=== Добро пожаловать в систему ===");
        System.out.println("1. Войти");
        System.out.println("2. Зарегистрироваться");
        System.out.print("Выбор: ");
        String choice = scanner.nextLine();

        String username, password;
        switch (choice) {
            case "1" -> {
                System.out.print("Введите логин: ");
                username = scanner.nextLine();
                System.out.print("Введите пароль: ");
                password = scanner.nextLine();

                if (!LibraryService.login(username, password)) {
                    System.out.println("❌ Неверный логин или пароль.");
                    return;
                }
                System.out.println("✅ Добро пожаловать, " + username + "!");
            }
            case "2" -> {
                System.out.print("Придумайте логин: ");
                username = scanner.nextLine();
                System.out.print("Придумайте пароль: ");
                password = scanner.nextLine();

                if (LibraryService.register(username, password)) {
                    System.out.println("✅ Пользователь " + username + " успешно зарегистрирован.");
                } else {
                    System.out.println("❌ Логин уже занят.");
                    return;
                }
            }
            default -> {
                System.out.println("Неверный выбор.");
                return;
            }
        }

        while (true) {
            System.out.println("\n=== Главное меню ===");
            System.out.println("1. Показать книги");
            System.out.println("2. Добавить книгу");
            System.out.println("3. Показать авторов");
            System.out.println("4. Добавить автора");
            System.out.println("5. Выйти");
            System.out.print("Выбор: ");
            int menuChoice = Integer.parseInt(scanner.nextLine());

            switch (menuChoice) {
                case 1 -> LibraryService.showBooks();
                case 2 -> LibraryService.addBook(scanner);
                case 3 -> LibraryService.showAuthors();
                case 4 -> LibraryService.addAuthor(scanner);
                case 5 -> {
                    System.out.println("Выход...");
                    return;
                }
                default -> System.out.println("Неверный выбор.");
            }
        }
    }
}
