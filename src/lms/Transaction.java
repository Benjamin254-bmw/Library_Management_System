package lms;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Transaction {
    private String bookId;
    private String memberId;
    private String action; // "borrow" or "return"
    private String date;

    // Constructor
    public Transaction(String bookId, String memberId, String action, String date) {
        this.bookId = bookId;
        this.memberId = memberId;
        this.action = action;
        this.date = date;
    }

    // Save transaction to the database
    public void save() {
        try (Connection conn = DatabaseConnector.connect()) {
            if (conn != null) {
                String sql = "INSERT INTO transactions (bookId, memberId, action, date) VALUES (?, ?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, bookId);
                    pstmt.setString(2, memberId);
                    pstmt.setString(3, action);
                    pstmt.setString(4, date);
                    pstmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get all transactions from the database
    public static List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        try (Connection conn = DatabaseConnector.connect()) {
            if (conn != null) {
                String sql = "SELECT * FROM transactions";
                try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                    while (rs.next()) {
                        Transaction transaction = new Transaction(
                                rs.getString("bookId"),
                                rs.getString("memberId"),
                                rs.getString("action"),
                                rs.getString("date")
                        );
                        transactions.add(transaction);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }
}
