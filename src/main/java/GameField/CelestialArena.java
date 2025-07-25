package GameField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class CelestialArena {

    private static final int EMPTY = 0;
    private static final int MARK_OF_FATE = 1;
    private static final int CIRCLE_OF_ETERNITY = 2;

    private static final int[] board = new int[9];
    private static boolean vsAI = false;

    private static final BufferedReader oracle = new BufferedReader(new InputStreamReader(System.in));
    private static final Random chaos = new Random();

    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31;1m";
    private static final String BLUE = "\u001B[34;1m";

    public static void main(String[] stars) {
        System.out.println("🌌 Добро пожаловать в CelestialArena: Дуэль Разумов");
        System.out.println("Выберите режим: \n1 — Игрок против Игрока\n2 — Игрок против Искусственного Интеллекта");

        try {
            vsAI = oracle.readLine().equals("2");
        } catch (IOException ignored) {}

        boolean fateTurn = false;

        while (true) {
            fateTurn = !fateTurn;
            renderBoard();

            System.out.println("\n🔮 Ход: " + (fateTurn
                    ? RED + "❌ Знак Судьбы (X)" + RESET
                    : (vsAI
                    ? BLUE + "🤖 Искусственный Разум (O)" + RESET
                    : BLUE + "◯ Круг Вечности (O)" + RESET)));

            int move;
            if (fateTurn || !vsAI) {
                move = askHumanMove();
            } else {
                move = invokeArtificialMind();
                System.out.println("🤖 Искусственный Разум совершил ход в ячейку: " + move);
            }

            board[move] = fateTurn ? MARK_OF_FATE : CIRCLE_OF_ETERNITY;

            if (checkVictory()) {
                renderBoard();
                System.out.println("\n🏁 Победа: " + (fateTurn
                        ? RED + "❌ Знак Судьбы (X)"
                        : BLUE + "◯ Круг Вечности (O)") + RESET + "!");
                break;
            }

            if (isDraw()) {
                renderBoard();
                System.out.println("\n🤝 Ничья. Космос в равновесии.");
                break;
            }
        }

        System.out.println("\n⚜ Игра окончена. Пусть впредь тобой движет разум.");
    }

    private static int askHumanMove() {
        while (true) {
            try {
                System.out.print("Введите номер свободной ячейки (0–8): ");
                int cell = Integer.parseInt(oracle.readLine());
                if (cell >= 0 && cell < 9 && board[cell] == EMPTY)
                    return cell;
                System.out.println("⚠ Неверный ход. Попробуйте ещё раз.");
            } catch (Exception ignored) {
                System.out.println("⚠ Ошибка ввода.");
            }
        }
    }

    private static int invokeArtificialMind() {
        for (int i = 0; i < 9; i++) {
            if (board[i] == EMPTY) {
                board[i] = CIRCLE_OF_ETERNITY;
                if (checkVictory()) {
                    board[i] = EMPTY;
                    return i;
                }
                board[i] = EMPTY;
            }
        }

        for (int i = 0; i < 9; i++) {
            if (board[i] == EMPTY) {
                board[i] = MARK_OF_FATE;
                if (checkVictory()) {
                    board[i] = EMPTY;
                    return i;
                }
                board[i] = EMPTY;
            }
        }

        int move;
        do {
            move = chaos.nextInt(9);
        } while (board[move] != EMPTY);

        return move;
    }

    private static boolean checkVictory() {
        return checkTriplet(0, 1, 2) ||
                checkTriplet(3, 4, 5) ||
                checkTriplet(6, 7, 8) ||
                checkTriplet(0, 3, 6) ||
                checkTriplet(1, 4, 7) ||
                checkTriplet(2, 5, 8) ||
                checkTriplet(0, 4, 8) ||
                checkTriplet(2, 4, 6);
    }

    private static boolean checkTriplet(int a, int b, int c) {
        return board[a] != EMPTY && board[a] == board[b] && board[b] == board[c];
    }

    private static boolean isDraw() {
        for (int cell : board) {
            if (cell == EMPTY) return false;
        }
        return true;
    }

    private static void renderBoard() {
        System.out.println();
        System.out.println("     |     |     ");
        for (int i = 0; i < board.length; i++) {
            if (i != 0) {
                if (i % 3 == 0) {
                    System.out.println();
                    System.out.println("_____|_____|_____");
                    System.out.println("     |     |     ");
                } else {
                    System.out.print("|");
                }
            }

            System.out.print("  ");
            switch (board[i]) {
                case EMPTY:
                    System.out.print(i + "  ");
                    break;
                case MARK_OF_FATE:
                    System.out.print(RED + "❌ " + RESET);
                    break;
                case CIRCLE_OF_ETERNITY:
                    System.out.print(BLUE + "◯ " + RESET);
                    break;
            }
        }
        System.out.println();
        System.out.println("     |     |     ");
    }
}
