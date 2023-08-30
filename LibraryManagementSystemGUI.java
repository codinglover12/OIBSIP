package main.java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LibraryManagementSystemGUI {

    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "Tech123";
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/librarymanagement";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Techmaster@123";

    private Connection connection;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LibraryManagementSystemGUI().createAndShowGUI();
        });
    }

    private LibraryManagementSystemGUI() {
        try {
            connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Library Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel(new GridLayout(5, 1));
        JButton addBookButton = new JButton("Add Book");
        JButton issueBookButton = new JButton("Issue Book");
        JButton returnBookButton = new JButton("Return Book");
        JButton printDetailsButton = new JButton("Print Details");
        JButton exitButton = new JButton("Exit");

        addBookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addBook();
            }
        });

        issueBookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                issueBook();
            }
        });

        returnBookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                returnBook();
            }
        });

        printDetailsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                printDetails();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exit();
            }
        });

        panel.add(addBookButton);
        panel.add(issueBookButton);
        panel.add(returnBookButton);
        panel.add(printDetailsButton);
        panel.add(exitButton);

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

    private void addBook() {
        String name = JOptionPane.showInputDialog("Enter Book Name");
        float price = Float.parseFloat(JOptionPane.showInputDialog("Enter Price"));
        int bookNo = Integer.parseInt(JOptionPane.showInputDialog("Enter Book Number"));

        try (Connection conn = getConnection()) {
            String query = "INSERT INTO books (name, price, book_no, available) VALUES (?, ?, ?, 1)";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, name);
                statement.setFloat(2, price);
                statement.setInt(3, bookNo);
                statement.executeUpdate();
                JOptionPane.showMessageDialog(null, "Book added successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void issueBook() {
        int bookNo = Integer.parseInt(JOptionPane.showInputDialog("Enter Book Number"));
        String userName = JOptionPane.showInputDialog("Enter User Name");

        try (Connection conn = getConnection()) {
            String availabilityQuery = "SELECT * FROM books WHERE book_no = ? AND available = 1";
            try (PreparedStatement availabilityStatement = conn.prepareStatement(availabilityQuery)) {
                availabilityStatement.setInt(1, bookNo);
                try (ResultSet resultSet = availabilityStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String issueQuery = "INSERT INTO issued_books (book_no, user_name) VALUES (?, ?)";
                        try (PreparedStatement issueStatement = conn.prepareStatement(issueQuery)) {
                            issueStatement.setInt(1, bookNo);
                            issueStatement.setString(2, userName);
                            issueStatement.executeUpdate();

                            String markUnavailableQuery = "UPDATE books SET available = 0 WHERE book_no = ?";
                            try (PreparedStatement markUnavailableStatement = conn.prepareStatement(markUnavailableQuery)) {
                                markUnavailableStatement.setInt(1, bookNo);
                                markUnavailableStatement.executeUpdate();
                            }
                            JOptionPane.showMessageDialog(null, "Book issued successfully!");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Book not available for issuing.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void returnBook() {
        int bookNo = Integer.parseInt(JOptionPane.showInputDialog("Enter Book Number"));

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);
            String availabilityQuery = "SELECT * FROM books WHERE book_no = ?";
            try (PreparedStatement availabilityStatement = conn.prepareStatement(availabilityQuery)) {
                availabilityStatement.setInt(1, bookNo);
                try (ResultSet resultSet = availabilityStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int available = resultSet.getInt("available");
                        if (available == 1) {
                            JOptionPane.showMessageDialog(null, "The book is not issued and cannot be returned.");
                        } else {
                            String returnQuery = "DELETE FROM issued_books WHERE book_no = ?";
                            try (PreparedStatement returnStatement = conn.prepareStatement(returnQuery)) {
                                returnStatement.setInt(1, bookNo);
                                int rowsDeleted = returnStatement.executeUpdate();
                                if (rowsDeleted > 0) {
                                    String markAvailableQuery = "UPDATE books SET available = 1 WHERE book_no = ?";
                                    try (PreparedStatement markAvailableStatement = conn.prepareStatement(markAvailableQuery)) {
                                        markAvailableStatement.setInt(1, bookNo);
                                        markAvailableStatement.executeUpdate();
                                    }
                                    conn.commit();
                                    JOptionPane.showMessageDialog(null, "Book returned successfully!");
                                } else {
                                    JOptionPane.showMessageDialog(null, "Book not found in issued books.");
                                }
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Book not found.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void printDetails() {
        try (Connection conn = getConnection()) {
            String query = "SELECT * FROM issued_books";
            try (PreparedStatement statement = conn.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {
                StringBuilder details = new StringBuilder("Issued Books:\n");
                while (resultSet.next()) {
                    int bookNo = resultSet.getInt("book_no");
                    String userName = resultSet.getString("user_name");
                    details.append("Book Number: ").append(bookNo).append(", User Name: ").append(userName).append("\n");
                }
                JTextArea textArea = new JTextArea(details.toString());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                JOptionPane.showMessageDialog(null, scrollPane, "Issued Books", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void exit() {
        System.exit(0);
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
    }
}
