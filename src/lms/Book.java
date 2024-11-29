package lms;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Book {
    private int id;
    private String title;
    private String author;
    private String genre;
    private String publisher;
    private int yearPublished;
    private int quantity;
    private int available;
    private int borrowCount;

    // Constructor
    public Book(String title, String author, String genre, String publisher, int yearPublished, int quantity) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.publisher = publisher;
        this.yearPublished = yearPublished;
        this.quantity = quantity;
        this.available = quantity; // Initially, available books are equal to quantity
        this.borrowCount = 0; // Initially, no books have been borrowed
    }

    // Save the book to the database
    public void save() {
        try (Connection conn = DatabaseConnector.connect()) {
            if (conn != null) {
                String sql = "INSERT INTO books (title, author, genre, publisher, year_published, quantity, available, borrow_count) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, title);
                    pstmt.setString(2, author);
                    pstmt.setString(3, genre);
                    pstmt.setString(4, publisher);
                    pstmt.setInt(5, yearPublished);
                    pstmt.setInt(6, quantity);
                    pstmt.setInt(7, available);
                    pstmt.setInt(8, borrowCount);
                    pstmt.executeUpdate();
                }
            } else {
                System.out.println("Database connection is null.");
            }
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Retrieve all books from the database
    public static List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        try (Connection conn = DatabaseConnector.connect()) {
            if (conn != null) {
                String sql = "SELECT * FROM books";
                try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                    while (rs.next()) {
                        Book book = new Book(
                                rs.getString("title"),
                                rs.getString("author"),
                                rs.getString("genre"),
                                rs.getString("publisher"),
                                rs.getInt("year_published"),
                                rs.getInt("quantity")
                        );
                        book.id = rs.getInt("book_id"); // Retrieve ID
                        book.available = rs.getInt("available");
                        book.borrowCount = rs.getInt("borrow_count");
                        books.add(book);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL error while retrieving books: " + e.getMessage());
            e.printStackTrace();
        }
        return books;
    }

    // Update the book's availability (borrow or return the book)
    public void borrowBook() {
        if (available > 0) {
            available--;
            borrowCount++;
            updateBookInDatabase();
        }
    }

    public void returnBook() {
        available++;
        updateBookInDatabase();
    }

    // Update book details in the database
    private void updateBookInDatabase() {
        try (Connection conn = DatabaseConnector.connect()) {
            if (conn != null) {
                String sql = "UPDATE books SET available = ?, borrow_count = ? WHERE book_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, available);
                    pstmt.setInt(2, borrowCount);
                    pstmt.setInt(3, id);
                    pstmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL error while updating book: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public String getPublisher() {
        return publisher;
    }

    public int getYearPublished() {
        return yearPublished;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getAvailable() {
        return available;
    }

    public int getBorrowCount() {
        return borrowCount;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.available = quantity; // Update available count when quantity changes
    }
}
