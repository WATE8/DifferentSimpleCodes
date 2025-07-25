package CorporateEmailAddressGenerator;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Генератор корпоративной почты ===");

        System.out.print("Введите имя: ");
        String firstName = scanner.nextLine();

        System.out.print("Введите фамилию: ");
        String lastName = scanner.nextLine();

        System.out.print("Введите отдел (например, it, hr, finance): ");
        String department = scanner.nextLine();

        String email = EmailGenerator.generateEmail(firstName, lastName, department);
        String password = EmailGenerator.generatePassword();

        Employee emp = new Employee(firstName, lastName, department, email, password);

        System.out.println("\nСотрудник создан:");
        System.out.println("ФИО: " + emp.getFullName());
        System.out.println("Отдел: " + emp.getDepartment());
        System.out.println("Email: " + emp.getEmail());
        System.out.println("Пароль: " + emp.getPassword());
    }
}
