package GameSnake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private final int TILE_SIZE = 25;
    private final int WIDTH = 20;
    private final int HEIGHT = 20;
    private int delay = 150;

    private final LinkedList<Point> snake = new LinkedList<>();
    private Point food;
    private char direction = 'R';
    private boolean gameOver = false;
    private boolean ghostMode = false;
    private int score = 0;
    private int level = 1;
    private int highScore = 0;

    private Timer timer;
    private long ghostEndTime = 0;
    private long speedBoostEndTime = 0;
    private boolean isSpeedBoost = false;
    private Random random = new Random();

    public SnakeGame() {
        setPreferredSize(new Dimension(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        loadHighScore();
        initGame();
    }

    private void initGame() {
        snake.clear();
        snake.add(new Point(5, 5));
        spawnFood();
        direction = 'R';
        gameOver = false;
        score = 0;
        level = 1;
        delay = 150;
        timer = new Timer(delay, this);
        timer.start();
    }

    private void spawnFood() {
        food = new Point(random.nextInt(WIDTH), random.nextInt(HEIGHT));
        while (snake.contains(food)) {
            food = new Point(random.nextInt(WIDTH), random.nextInt(HEIGHT));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Сетка
        g.setColor(Color.DARK_GRAY);
        for (int i = 0; i < WIDTH; i++)
            for (int j = 0; j < HEIGHT; j++)
                g.drawRect(i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE, TILE_SIZE);

        // Еда
        g.setColor(isSpeedBoost ? Color.ORANGE : ghostMode ? Color.CYAN : Color.RED);
        g.fillOval(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

        // Змейка
        g.setColor(Color.GREEN);
        for (Point p : snake) {
            g.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        // HUD
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.drawString("Счёт: " + score + " | Уровень: " + level + " | Рекорд: " + highScore, 10, 20);
        if (ghostMode) g.drawString("Призрак!", 10, 40);
        if (isSpeedBoost) g.drawString("Ускорение!", 10, 60);

        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("Game Over", 100, HEIGHT * TILE_SIZE / 2);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) return;

        if (ghostMode && System.currentTimeMillis() > ghostEndTime) ghostMode = false;
        if (isSpeedBoost && System.currentTimeMillis() > speedBoostEndTime) {
            isSpeedBoost = false;
            timer.setDelay(delay);
        }

        Point head = new Point(snake.getFirst());
        switch (direction) {
            case 'U' -> head.y--;
            case 'D' -> head.y++;
            case 'L' -> head.x--;
            case 'R' -> head.x++;
        }

        boolean hitWall = head.x < 0 || head.y < 0 || head.x >= WIDTH || head.y >= HEIGHT;
        boolean hitSelf = snake.contains(head);

        if ((hitWall || hitSelf) && !ghostMode) {
            gameOver = true;
            timer.stop();
            saveHighScore();
            repaint();
            return;
        }

        if (ghostMode) {
            if (head.x < 0) head.x = WIDTH - 1;
            if (head.x >= WIDTH) head.x = 0;
            if (head.y < 0) head.y = HEIGHT - 1;
            if (head.y >= HEIGHT) head.y = 0;
        }

        snake.addFirst(head);

        if (head.equals(food)) {
            score += 10;
            level = score / 50 + 1;

            int chance = random.nextInt(100);
            if (chance < 10) {
                isSpeedBoost = true;
                speedBoostEndTime = System.currentTimeMillis() + 5000;
                timer.setDelay(60);
            } else if (chance < 20) {
                ghostMode = true;
                ghostEndTime = System.currentTimeMillis() + 10000;
            }

            spawnFood();
        } else {
            snake.removeLast();
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        char newDir = switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> 'U';
            case KeyEvent.VK_DOWN -> 'D';
            case KeyEvent.VK_LEFT -> 'L';
            case KeyEvent.VK_RIGHT -> 'R';
            default -> direction;
        };

        if ((direction == 'U' && newDir != 'D') ||
                (direction == 'D' && newDir != 'U') ||
                (direction == 'L' && newDir != 'R') ||
                (direction == 'R' && newDir != 'L')) {
            direction = newDir;
        }

        if (e.getKeyCode() == KeyEvent.VK_R && gameOver) {
            initGame();
        }
    }

    private void saveHighScore() {
        if (score > highScore) {
            try (FileWriter writer = new FileWriter("highscore.txt")) {
                writer.write(String.valueOf(score));
            } catch (IOException ignored) {}
        }
    }

    private void loadHighScore() {
        try (BufferedReader reader = new BufferedReader(new FileReader("highscore.txt"))) {
            String line = reader.readLine();
            if (line != null) highScore = Integer.parseInt(line);
        } catch (IOException ignored) {}
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Змейка");
        SnakeGame game = new SnakeGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
}
