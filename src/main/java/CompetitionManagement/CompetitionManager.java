package CompetitionManagement;

import java.util.*;

public class CompetitionManager {
    private Scanner scanner = new Scanner(System.in);
    private Map<String, User> users = new HashMap<>();
    private List<Event> events = new ArrayList<>();
    private User currentUser;

    public void start() {
        while (true) {
            System.out.println("\n==== УПРАВЛЕНИЕ СОРЕВНОВАНИЯМИ ====");
            System.out.println("1. Вход");
            System.out.println("2. Регистрация");
            System.out.println("3. Выход");
            System.out.print("Выберите: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> login();
                case "2" -> register();
                case "3" -> {
                    System.out.println("До свидания!");
                    return;
                }
                default -> System.out.println("Неверный выбор.");
            }
        }
    }

    private void register() {
        System.out.print("Имя пользователя: ");
        String username = scanner.nextLine();
        if (users.containsKey(username)) {
            System.out.println("Пользователь уже существует.");
            return;
        }

        System.out.print("Пароль: ");
        String password = scanner.nextLine();
        System.out.print("Роль (admin/user): ");
        String roleInput = scanner.nextLine().toLowerCase();
        Role role = roleInput.equals("admin") ? Role.ADMIN : Role.USER;

        users.put(username, new User(username, password, role));
        System.out.println("Регистрация прошла успешно!");
    }

    private void login() {
        System.out.print("Имя пользователя: ");
        String username = scanner.nextLine();
        System.out.print("Пароль: ");
        String password = scanner.nextLine();

        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            System.out.println("Вход выполнен как " + currentUser.getUsername() + " (" + currentUser.getRole() + ")");
            userMenu();
        } else {
            System.out.println("Неверное имя или пароль.");
        }
    }

    private void userMenu() {
        while (true) {
            System.out.println("\n==== МЕНЮ ====");
            if (currentUser.getRole() == Role.ADMIN) {
                System.out.println("1. Создать событие");
                System.out.println("2. Показать события");
                System.out.println("3. Посмотреть участников события");
                System.out.println("4. Добавить призовые места");
                System.out.println("5. Выйти");
                System.out.print("Выберите: ");
                String choice = scanner.nextLine();

                switch (choice) {
                    case "1" -> createEvent();
                    case "2" -> showEvents();
                    case "3" -> viewEventParticipants();
                    case "4" -> addPrizes();
                    case "5" -> { currentUser = null; return; }
                    default -> System.out.println("Неверный выбор.");
                }
            } else {
                System.out.println("1. Показать события");
                System.out.println("2. Зарегистрировать команду");
                System.out.println("3. Посмотреть участников события");
                System.out.println("4. Подписаться на событие");
                System.out.println("5. Посмотреть призовые места");
                System.out.println("6. Выйти");
                System.out.print("Выберите: ");
                String choice = scanner.nextLine();

                switch (choice) {
                    case "1" -> showEvents();
                    case "2" -> registerTeam();
                    case "3" -> viewEventParticipants();
                    case "4" -> subscribeToEvent();
                    case "5" -> viewPrizes();
                    case "6" -> { currentUser = null; return; }
                    default -> System.out.println("Неверный выбор.");
                }
            }
        }
    }

    private void createEvent() {
        System.out.print("Название события: ");
        String title = scanner.nextLine();
        System.out.print("Дата (например, 2025-08-10): ");
        String date = scanner.nextLine();
        System.out.print("Место проведения: ");
        String location = scanner.nextLine();

        Event event = new Event(title, date, location);
        events.add(event);
        System.out.println("Событие создано.");
    }

    private void showEvents() {
        if (events.isEmpty()) {
            System.out.println("Событий пока нет.");
            return;
        }
        System.out.println("\nСписок событий:");
        for (int i = 0; i < events.size(); i++) {
            Event e = events.get(i);
            System.out.printf("%d. %s | %s | %s%n", i + 1, e.getTitle(), e.getDate(), e.getLocation());
        }
    }

    private void registerTeam() {
        if (events.isEmpty()) {
            System.out.println("Нет доступных событий.");
            return;
        }

        showEvents();
        int eventNum = readInt("Выберите номер события: ") - 1;

        if (eventNum < 0 || eventNum >= events.size()) {
            System.out.println("Неверный номер события.");
            return;
        }

        Event event = events.get(eventNum);

        System.out.print("Название команды: ");
        String teamName = scanner.nextLine();
        Team team = new Team(teamName);

        int count = readInt("Сколько участников? ");

        for (int i = 0; i < count; i++) {
            System.out.print("Имя участника " + (i + 1) + ": ");
            String participant = scanner.nextLine();
            team.addParticipant(participant);
        }

        event.addTeam(team);
        System.out.println("Команда успешно зарегистрирована на событие!");
    }

    private void viewEventParticipants() {
        if (events.isEmpty()) {
            System.out.println("Нет событий.");
            return;
        }

        showEvents();
        int eventNum = readInt("Выберите номер события: ") - 1;

        if (eventNum < 0 || eventNum >= events.size()) {
            System.out.println("Неверный номер.");
            return;
        }

        Event event = events.get(eventNum);

        if (event.getTeams().isEmpty()) {
            System.out.println("Участников пока нет.");
            return;
        }

        System.out.println("Участники события:");
        for (Team team : event.getTeams()) {
            System.out.println("Команда: " + team.getName());
            for (String p : team.getParticipants()) {
                System.out.println("  - " + p);
            }
        }
    }

    private void addPrizes() {
        showEvents();
        int eventNum = readInt("Выберите событие: ") - 1;
        if (eventNum < 0 || eventNum >= events.size()) return;

        Event event = events.get(eventNum);
        List<String> prizes = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            System.out.print(i + " место: ");
            prizes.add(scanner.nextLine());
        }
        event.setPrizes(prizes);
        System.out.println("Призы сохранены!");

        // уведомить подписчиков
        for (String subscriber : event.getSubscribers()) {
            System.out.println("Уведомление: Пользователь " + subscriber + " подписан на \"" + event.getTitle() + "\" — добавлены призовые места!");
        }
    }

    private void subscribeToEvent() {
        showEvents();
        int eventNum = readInt("Выберите событие для подписки: ") - 1;
        if (eventNum < 0 || eventNum >= events.size()) return;

        Event event = events.get(eventNum);
        if (event.getSubscribers().contains(currentUser.getUsername())) {
            System.out.println("Вы уже подписаны.");
        } else {
            event.getSubscribers().add(currentUser.getUsername());
            System.out.println("Подписка оформлена!");
        }
    }

    private void viewPrizes() {
        showEvents();
        int eventNum = readInt("Выберите событие: ") - 1;
        if (eventNum < 0 || eventNum >= events.size()) return;

        Event event = events.get(eventNum);
        List<String> prizes = event.getPrizes();
        if (prizes.isEmpty()) {
            System.out.println("Призовые места ещё не указаны.");
        } else {
            System.out.println("Призовые места:");
            for (int i = 0; i < prizes.size(); i++) {
                System.out.println((i + 1) + " место: " + prizes.get(i));
            }
        }
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Введите число.");
            }
        }
    }
}
