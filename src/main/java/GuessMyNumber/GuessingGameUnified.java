package GuessMyNumber;

import javax.swing.*;

public class GuessingGameUnified {

    public static void main(String[] args) {
        boolean useDetailedStyle = true;

        playGame(useDetailedStyle);
    }

    public static void playGame(boolean useDetailedStyle) {
        int secretNumber = (int) (Math.random() * 100 + 1);
        int userGuess = 0;
        int attempt = 1;

        System.out.println("Загаданное число (для проверки): " + secretNumber);

        while (userGuess != secretNumber) {
            String input = JOptionPane.showInputDialog(null,
                    "Введите число от 1 до 100", "Игра: Угадай число", JOptionPane.INFORMATION_MESSAGE);

            if (input == null) {
                JOptionPane.showMessageDialog(null, "Игра прервана.");
                return;
            }

            try {
                userGuess = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Пожалуйста, введите целое число.");
                continue;
            }

            String message = useDetailedStyle
                    ? generateMessageCase(userGuess, secretNumber, attempt)
                    : generateMessageSimple(userGuess, secretNumber, attempt);

            JOptionPane.showMessageDialog(null, message);
            attempt++;
        }

        int choice = JOptionPane.showConfirmDialog(null,
                "Вы хотите сыграть ещё раз?", "Победа!", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            playGame(useDetailedStyle);
        } else {
            JOptionPane.showMessageDialog(null, "Спасибо за игру!");
        }
    }

    public static String generateMessageCase(int guess, int secret, int attempts) {
        String word = (attempts == 1) ? " попытка." : " попыток.";

        if (guess <= 0 || guess > 100) {
            return "Некорректный ввод! Число должно быть от 1 до 100.";
        } else if (guess == secret) {
            return "🎉 Поздравляем! Вы угадали число!\nВсего: " + attempts + word;
        } else if (guess > secret) {
            return "Слишком большое число. Попробуйте снова.\nИспользовано: " + attempts + word;
        } else {
            return "Слишком маленькое число. Попробуйте снова.\nИспользовано: " + attempts + word;
        }
    }

    public static String generateMessageSimple(int guess, int secret, int attempt) {
        if (guess <= 0 || guess > 100) {
            return "Ошибка: введите число от 1 до 100.";
        } else if (guess == secret) {
            return "Верно! 🎯 Вы угадали число за " + attempt + " попыток.";
        } else if (guess > secret) {
            return "Слишком много! Попробуйте меньше.\nПопытка №" + attempt;
        } else {
            return "Слишком мало! Попробуйте больше.\nПопытка №" + attempt;
        }
    }
}
