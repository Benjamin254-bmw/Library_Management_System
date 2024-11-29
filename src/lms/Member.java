package lms;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Member {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;

    // Constructor
    public Member(String firstName, String lastName, String email, String phone, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    // Save the member to the database
    public void save() {
        try (Connection conn = DatabaseConnector.connect()) {
            if (conn != null) {
                String sql = "INSERT INTO members (first_name, last_name, email, phone, address) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, firstName);
                    pstmt.setString(2, lastName);
                    pstmt.setString(3, email);
                    pstmt.setString(4, phone);
                    pstmt.setString(5, address);
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

    // Retrieve all members from the database
    public static List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        try (Connection conn = DatabaseConnector.connect()) {
            if (conn != null) {
                String sql = "SELECT * FROM members";
                try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
                    while (rs.next()) {
                        Member member = new Member(
                                rs.getString("first_name"),
                                rs.getString("last_name"),
                                rs.getString("email"),
                                rs.getString("phone"),
                                rs.getString("address")
                        );
                        member.id = rs.getInt("id"); // Retrieve ID
                        members.add(member);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL error while retrieving members: " + e.getMessage());
            e.printStackTrace();
        }
        return members;
    }
}
