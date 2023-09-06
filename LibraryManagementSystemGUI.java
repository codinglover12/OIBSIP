package main.java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LibraryManagementSystemGUI {

    private static final String DEFAULT_USERNAME = "Atul Kumar";
    private static final String DEFAULT_PASSWORD = "Tech123";
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/librarymanagement";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Techmaster@123";

    private Connection connection;
    private String enteredUsername;
    private String enteredPassword;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LibraryManagementSystemGUI().showLoginDialog();
        });
    }

    private LibraryManagementSystemGUI() {
        try {
            connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showLoginDialog() {
        JFrame loginFrame = new JFrame("Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(400, 200); // Increased width and height
        loginFrame.setResizable(false);

        JPanel loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10); // Increased padding between components

        JLabel usernameLabel = new JLabel("Username:");
        constraints.gridx = 0;
        constraints.gridy = 0;
        loginPanel.add(usernameLabel, constraints);

        JTextField usernameField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 0;
        loginPanel.add(usernameField, constraints);

        JLabel passwordLabel = new JLabel("Password:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        loginPanel.add(passwordLabel, constraints);

        JPasswordField passwordField = new JPasswordField(20);
        constraints.gridx = 1;
        constraints.gridy = 1;
        loginPanel.add(passwordField, constraints);

        // Add icons to the labels (Replace with your icon URLs)
        try {
            URL userIconUrl = new URL("https://icon-library.com/images/status-icon-png/status-icon-png-24.jpg");
            URL passwordIconUrl = new URL("https://cdn-icons-png.flaticon.com/512/564/564633.png");

            ImageIcon userIcon = new ImageIcon(userIconUrl);
            ImageIcon passwordIcon = new ImageIcon(passwordIconUrl);

            // Scale down the icons for visibility
            Image userImage = userIcon.getImage().getScaledInstance(30, 32, Image.SCALE_SMOOTH);
            Image passwordImage = passwordIcon.getImage().getScaledInstance(30, 32, Image.SCALE_SMOOTH);

            usernameLabel.setIcon(new ImageIcon(userImage));
            passwordLabel.setIcon(new ImageIcon(passwordImage));
        } catch (Exception e) {
            e.printStackTrace();
        }

        JButton loginButton = new JButton("Login");
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2; // Span two columns
        constraints.anchor = GridBagConstraints.CENTER; // Center-align the button
        loginPanel.add(loginButton, constraints);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                enteredUsername = usernameField.getText();
                enteredPassword = new String(passwordField.getPassword());

                if (isValidLogin(enteredUsername, enteredPassword)) {
                    loginFrame.dispose(); // Close the login dialog
                    createAndShowGUI(); // Show the main GUI
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid login credentials.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        loginFrame.getContentPane().add(loginPanel);
        loginFrame.setVisible(true);
        loginFrame.setLocationRelativeTo(null); // Center the login dialog on the screen
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    private boolean isValidLogin(String username, String password) {
        // You can customize this method to check credentials from your database.
        // For now, it compares with the default credentials.
        return username.equals(DEFAULT_USERNAME) && password.equals(DEFAULT_PASSWORD);
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Library Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel(new GridLayout(5, 1));

        Font buttonFont = new Font("Arial", Font.LAYOUT_NO_LIMIT_CONTEXT, 40);

        JButton addBookButton = new JButton("Add Book");
        addBookButton.setFont(buttonFont);
        addBookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addBook();
            }
        });
        panel.add(addBookButton);

        JButton issueBookButton = new JButton("Issue Book");
        issueBookButton.setFont(buttonFont);
        issueBookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                issueBook();
            }
        });
        panel.add(issueBookButton);

        JButton returnBookButton = new JButton("Return Book");
        returnBookButton.setFont(buttonFont);
        returnBookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                returnBook();
            }
        });
        panel.add(returnBookButton);

        JButton printDetailsButton = new JButton("Print Details");
        printDetailsButton.setFont(buttonFont);
        printDetailsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                printDetails();
            }
        });
        panel.add(printDetailsButton);

        JButton exitButton = new JButton("Exit");
        exitButton.setFont(buttonFont);
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exit();
            }
        });
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