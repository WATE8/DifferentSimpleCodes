package AtmSoftware;

import java.util.Scanner;

public class ATM {
    private BankAccount account;
    private Scanner scanner;

    public ATM(BankAccount account) {
        this.account = account;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.print("Введите PIN-код: ");
        String inputPin = scanner.nextLine();

        if (!account.checkPin(inputPin)) {
            System.out.println("Неверный PIN-код. Доступ запрещён.");
            return;
        }

        System.out.println("Добро пожаловать, " + account.getOwner() + "!");
        while (true) {
            System.out.println("\n=== МЕНЮ БАНКОМАТА ===");
            System.out.println("1. Посмотреть баланс");
            System.out.println("2. Пополнить счёт");
            System.out.println("3. Снять деньги");
            System.out.println("4. История операций");
            System.out.println("5. Выйти");

            System.out.print("Выберите действие: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.println("Текущий баланс: " + account.getBalance() + "₸");
                    break;
                case "2":
                    System.out.print("Введите сумму пополнения: ");
                    double dep = Double.parseDouble(scanner.nextLine());
                    account.deposit(dep);
                    System.out.println("Счёт пополнен.");
                    break;
                case "3":
                    System.out.print("Введите сумму для снятия: ");
                    double with = Double.parseDouble(scanner.nextLine());
                    if (account.withdraw(with)) {
                        System.out.println("Деньги сняты.");
                    } else {
                        System.out.println("Недостаточно средств.");
                    }
                    break;
                case "4":
                    System.out.println("История операций:");
                    account.printTransactions();
                    break;
                case "5":
                    System.out.println("Спасибо за использование банкомата. До свидания!");
                    return;
                default:
                    System.out.println("Неверный выбор.");
            }
        }
    }
}
