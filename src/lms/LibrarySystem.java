package lms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LibrarySystem extends JFrame {
    
    // Constructor to initialize the GUI
    public LibrarySystem() {
        setTitle("Library Management System");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Main panel with buttons
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 10, 10));

        // Buttons for different actions
        JButton registerMemberButton = new JButton("Register Member");
        JButton addBookButton = new JButton("Add Book");
        JButton borrowBookButton = new JButton("Borrow Book");
        JButton returnBookButton = new JButton("Return Book");
        JButton exitButton = new JButton("Exit");

        // Adding buttons to the panel
        panel.add(registerMemberButton);
        panel.add(addBookButton);
        panel.add(borrowBookButton);
        panel.add(returnBookButton);
        panel.add(exitButton);

        // Adding action listeners to the buttons
        registerMemberButton.addActionListener(e -> openRegisterMemberForm());
        addBookButton.addActionListener(e -> openAddBookForm());
        borrowBookButton.addActionListener(e -> openBorrowBookForm());
        returnBookButton.addActionListener(e -> openReturnBookForm());
        exitButton.addActionListener(e -> System.exit(0));

        // Adding the panel to the frame
        add(panel, BorderLayout.CENTER);
    }

    // Method to open the "Register Member" form
    private void openRegisterMemberForm() {
        JFrame frame = new JFrame("Register Member");
        frame.setSize(300, 300);
        frame.setLayout(new GridLayout(6, 2, 5, 5));

        JTextField firstNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField addressField = new JTextField();

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String address = addressField.getText();

            // Validate that the input fields for names, email, and address are not numbers
            if (isNumber(firstName) || isNumber(lastName) || isNumber(email) || isNumber(address)) {
                JOptionPane.showMessageDialog(frame, "Please enter valid data types only!");
                return;
            }

            // Save member data to database
            Member member = new Member(firstName, lastName, email, phone, address);
            member.save();
            JOptionPane.showMessageDialog(frame, "Member registered successfully!");
            frame.dispose();
        });

        // Adding components to the frame
        frame.add(new JLabel("First Name:"));
        frame.add(firstNameField);
        frame.add(new JLabel("Last Name:"));
        frame.add(lastNameField);
        frame.add(new JLabel("Email:"));
        frame.add(emailField);
        frame.add(new JLabel("Phone:"));
        frame.add(phoneField);
        frame.add(new JLabel("Address:"));
        frame.add(addressField);
        frame.add(saveButton);

        frame.setVisible(true);
    }

    // Method to open the "Add Book" form
    private void openAddBookForm() {
        JFrame frame = new JFrame("Add Book");
        frame.setSize(300, 250);
        frame.setLayout(new GridLayout(6, 2, 5, 5));

        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField genreField = new JTextField();
        JTextField publisherField = new JTextField();
        JTextField yearPublishedField = new JTextField();
        JTextField quantityField = new JTextField();

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String title = titleField.getText();
            String author = authorField.getText();
            String genre = genreField.getText();
            String publisher = publisherField.getText();
            String yearPublishedText = yearPublishedField.getText();
            String quantityText = quantityField.getText();

            // Validate that all fields are filled
            if (title.isEmpty() || author.isEmpty() || genre.isEmpty() || publisher.isEmpty() || yearPublishedText.isEmpty() || quantityText.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill all fields.");
                return;
            }

            // Validate that the title and author are not numbers
            if (isNumber(title) || isNumber(author)) {
                JOptionPane.showMessageDialog(frame, "Title and Author should not contain numbers.");
                return;
            }

            // Validate that year published and quantity are numbers
            int yearPublished = 0;
            int quantity = 0;
            try {
                yearPublished = Integer.parseInt(yearPublishedText);
                quantity = Integer.parseInt(quantityText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter valid numbers for Year Published and Quantity.");
                return;
            }

            // Save book data to database
            Book book = new Book(title, author, genre, publisher, yearPublished, quantity);
            book.save();
            JOptionPane.showMessageDialog(frame, "Book added successfully!");
            frame.dispose();
        });

        // Adding components to the frame
        frame.add(new JLabel("Title:"));
        frame.add(titleField);
        frame.add(new JLabel("Author:"));
        frame.add(authorField);
        frame.add(new JLabel("Genre:"));
        frame.add(genreField);
        frame.add(new JLabel("Publisher:"));
        frame.add(publisherField);
        frame.add(new JLabel("Year Published:"));
        frame.add(yearPublishedField);
        frame.add(new JLabel("Quantity:"));
        frame.add(quantityField);
        frame.add(saveButton);

        frame.setVisible(true);
    }


    // Method to open the "Borrow Book" form
    private void openBorrowBookForm() {
        JFrame frame = new JFrame("Borrow Book");
        frame.setSize(300, 200);
        frame.setLayout(new GridLayout(3, 2, 5, 5));

        JTextField bookIdField = new JTextField();
        JTextField memberIdField = new JTextField();

        JButton borrowButton = new JButton("Borrow");
        borrowButton.addActionListener(e -> {
            final int bookId;
            try {
                bookId = Integer.parseInt(bookIdField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid book ID.");
                return;
            }

            String memberId = memberIdField.getText();
            Book book = Book.getAllBooks().stream().filter(b -> b.getId() == bookId).findFirst().orElse(null);
            if (book != null && book.getAvailable() > 0) {
                book.borrowBook();
                Transaction transaction = new Transaction(String.valueOf(bookId), memberId, "borrow", "2024-11-15");
                transaction.save();
                JOptionPane.showMessageDialog(frame, "Book borrowed successfully!");
            } else {
                JOptionPane.showMessageDialog(frame, "Book not available.");
            }
            frame.dispose();
        });

        // Adding components to the frame
        frame.add(new JLabel("Book ID:"));
        frame.add(bookIdField);
        frame.add(new JLabel("Member ID:"));
        frame.add(memberIdField);
        frame.add(borrowButton);

        frame.setVisible(true);
    }

    // Method to open the "Return Book" form
    private void openReturnBookForm() {
        JFrame frame = new JFrame("Return Book");
        frame.setSize(300, 200);
        frame.setLayout(new GridLayout(3, 2, 5, 5));

        JTextField bookIdField = new JTextField();
        JTextField memberIdField = new JTextField();

        JButton returnButton = new JButton("Return");
        returnButton.addActionListener(e -> {
            final int bookId;
            try {
                bookId = Integer.parseInt(bookIdField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid book ID.");
                return;
            }

            String memberId = memberIdField.getText();
            Book book = Book.getAllBooks().stream().filter(b -> b.getId() == bookId).findFirst().orElse(null);
            if (book != null) {
                book.returnBook();
                Transaction transaction = new Transaction(String.valueOf(bookId), memberId, "return", "2024-11-15");
                transaction.save();
                JOptionPane.showMessageDialog(frame, "Book returned successfully!");
            } else {
                JOptionPane.showMessageDialog(frame, "Book not found.");
            }
            frame.dispose();
        });

        // Adding components to the frame
        frame.add(new JLabel("Book ID:"));
        frame.add(bookIdField);
        frame.add(new JLabel("Member ID:"));
        frame.add(memberIdField);
        frame.add(returnButton);

        frame.setVisible(true);
    }

    // Helper method to check if a string is a number
    private boolean isNumber(String input) {
        try {
            Integer.parseInt(input);  // Try to parse the input as an integer
            return true;
        } catch (NumberFormatException e) {
            return false;  // Return false if it's not a valid integer
        }
    }

    public static void main(String[] args) {
        // Start the GUI application
        SwingUtilities.invokeLater(() -> {
            LibrarySystem gui = new LibrarySystem();
            gui.setVisible(true);
        });
    }
}
