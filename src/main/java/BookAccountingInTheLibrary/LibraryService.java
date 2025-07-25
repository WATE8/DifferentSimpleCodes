package BookAccountingInTheLibrary;

import java.sql.*;
import java.util.Scanner;

public class LibraryService {
    private static final String URL = "jdbc:mysql://localhost:3306/library_db";
    private static final String USER = "root";
    private static final String PASSWORD = "4444";

    public static void initDatabase() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS users (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    username VARCHAR(50) NOT NULL UNIQUE,
                    password VARCHAR(50) NOT NULL
                )
            """);

            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users WHERE username = 'admin'");
            if (rs.next() && rs.getInt(1) == 0) {
                stmt.executeUpdate("INSERT INTO users (username, password) VALUES ('admin', '1234')");
                System.out.println("✅ Пользователь admin создан (пароль: 1234).");
            }

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS authors (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(100) NOT NULL,
                    birth_year INT
                )
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS books (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    title VARCHAR(200) NOT NULL,
                    author_id INT,
                    year INT,
                    genre VARCHAR(100),
                    rating DOUBLE,
                    FOREIGN KEY (author_id) REFERENCES authors(id) ON DELETE SET NULL
                )
            """);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean login(String username, String password) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void showBooks() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT books.id, title, name AS author, year, genre, rating " +
                             "FROM books LEFT JOIN authors ON books.author_id = authors.id")) {

            System.out.println("\n📚 Список книг:");
            while (rs.next()) {
                System.out.printf("ID: %d | %s (%d) | Автор: %s | Жанр: %s | Рейтинг: %.1f%n",
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getInt("year"),
                        rs.getString("author") != null ? rs.getString("author") : "неизвестен",
                        rs.getString("genre"),
                        rs.getDouble("rating"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addBook(Scanner scanner) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.print("Название книги: ");
            String title = scanner.nextLine();
            System.out.print("ID автора: ");
            int authorId = Integer.parseInt(scanner.nextLine());

            PreparedStatement checkAuthor = conn.prepareStatement("SELECT * FROM authors WHERE id = ?");
            checkAuthor.setInt(1, authorId);
            ResultSet authorExists = checkAuthor.executeQuery();
            if (!authorExists.next()) {
                System.out.println("❌ Автор с ID " + authorId + " не существует.");
                return;
            }

            System.out.print("Год издания: ");
            int year = Integer.parseInt(scanner.nextLine());
            System.out.print("Жанр: ");
            String genre = scanner.nextLine();
            System.out.print("Рейтинг (0.0 - 5.0): ");
            double rating = Double.parseDouble(scanner.nextLine());

            String sql = "INSERT INTO books (title, author_id, year, genre, rating) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, title);
            stmt.setInt(2, authorId);
            stmt.setInt(3, year);
            stmt.setString(4, genre);
            stmt.setDouble(5, rating);
            stmt.executeUpdate();

            System.out.println("✅ Книга добавлена.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void showAuthors() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM authors")) {

            System.out.println("\n👤 Авторы:");
            while (rs.next()) {
                System.out.printf("ID: %d | %s (%d)%n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("birth_year"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addAuthor(Scanner scanner) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.print("Имя автора: ");
            String name = scanner.nextLine();
            System.out.print("Год рождения: ");
            int birthYear = Integer.parseInt(scanner.nextLine());

            String sql = "INSERT INTO authors (name, birth_year) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setInt(2, birthYear);
            stmt.executeUpdate();

            System.out.println("✅ Автор добавлен.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean register(String username, String password) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String checkSql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                return false;
            }

            String insertSql = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);
            insertStmt.setString(1, username);
            insertStmt.setString(2, password);
            insertStmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
