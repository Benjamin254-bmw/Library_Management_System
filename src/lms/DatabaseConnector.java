package lms;

import java.sql.*;

public class DatabaseConnector {
    private static final String URL = "jdbc:mysql://localhost:3306/library_systemdb"; // URL to MySQL database
    private static final String USER = "root"; // MySQL username
    private static final String PASSWORD = ""; // MySQL password (use your actual password)

    // Establish a connection to the database
    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
            return null;
        }
    }

    // Method to create tables if they don't exist
    public static void createTables() {
        try (Connection conn = connect()) {
            if (conn != null) {
                Statement stmt = conn.createStatement();

                // Create members table (with INT AUTO_INCREMENT for id)
                stmt.execute("CREATE TABLE IF NOT EXISTS members ("
                        + "id INT AUTO_INCREMENT PRIMARY KEY, "
                        + "firstName VARCHAR(100), "
                        + "lastName VARCHAR(100), "
                        + "email VARCHAR(100), "
                        + "phone VARCHAR(20), "
                        + "address VARCHAR(255))");

                // Create books table
                stmt.execute("CREATE TABLE IF NOT EXISTS books ("
                        + "id INT AUTO_INCREMENT PRIMARY KEY, "
                        + "title VARCHAR(255), "
                        + "author VARCHAR(255), "
                        + "isbn VARCHAR(50) UNIQUE, "
                        + "available INT)");

                // Create transactions table (with INT for memberId and bookId)
                stmt.execute("CREATE TABLE IF NOT EXISTS transactions ("
                        + "id INT AUTO_INCREMENT PRIMARY KEY, "
                        + "memberId INT, "
                        + "bookId INT, "
                        + "borrowDate DATE, "
                        + "returnDate DATE, "
                        + "FOREIGN KEY (memberId) REFERENCES members(id), "
                        + "FOREIGN KEY (bookId) REFERENCES books(id))");
            }
        } catch (SQLException e) {
            System.out.println("Table creation failed: " + e.getMessage());
        }
    }
}
