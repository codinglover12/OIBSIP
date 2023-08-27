package main.java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class LibraryManagementSystem {

    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "Tech123";

    public static void main(String[] args) {
        System.out.println("****** Library Management System ******");

        Scanner sc = new Scanner(System.in);
        System.out.print("Username: ");
        String username = sc.nextLine();

        System.out.print("Password: ");
        String password = sc.nextLine();

        if (isValidLogin(username, password)) {
            Library library = new Library();
            library.run();
        } else {
            System.out.println("Invalid login credentials.");
        }
    }

    private static boolean isValidLogin(String username, String password) {
        return username.equals(DEFAULT_USERNAME) && password.equals(DEFAULT_PASSWORD);
    }
}

class Library {
    static String Name, date, b;
    static int a, c;

    static Connection getConnection() throws SQLException {
        String jdbcUrl = "jdbc:mysql://localhost:3306/librarymanagement";
        String user = "root";
        String password = "Techmaster@123";
        return DriverManager.getConnection(jdbcUrl, user, password);
    }

    void run() {
        char r;
        do {
            System.out.println("******* Library Management System ********");
            System.out.println("Press 1 to add a book");
            System.out.println("Press 2 to issue a book");
            System.out.println("Press 3 to return a book");
            System.out.println("Press 4 to print complete issue details ");

            try (Scanner obj1 = new Scanner(System.in)) {
                System.out.println("Enter any Number");
                int choice = obj1.nextInt();
                processChoice(choice);
                System.out.println("Do you want to select the next option? (Y/N)");
                r = obj1.next().charAt(0);
            }
        } while (r == 'y' || r == 'Y');

        if (r == 'n' || r == 'N') {
            exit();
        }
    }

    private void processChoice(int choice) {
        switch (choice) {
            case 1:
                add();
                break;
            case 2:
                issue();
                break;
            case 3:
                ret();
                break;
            case 4:
                detail();
                break;
            default:
                System.out.println("Invalid number");
        }
    }

    void add() {
        try (Connection connection = getConnection()) {
            System.out.println("Enter Book Name, Price and Book_No");
            try (Scanner obj2 = new Scanner(System.in)) {
                String name = obj2.nextLine();
                float price = obj2.nextFloat();
                int bookNo = obj2.nextInt();

                String query = "INSERT INTO books (name, price, book_no, available) VALUES (?, ?, ?, 1)";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, name);
                    statement.setFloat(2, price);
                    statement.setInt(3, bookNo);
                    statement.executeUpdate();
                    System.out.println("Book added successfully!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void issue() {
        try (Connection connection = getConnection()) {
            System.out.println("Enter Book Number and User Name");
            try (Scanner obj2 = new Scanner(System.in)) {
                int bookNo = obj2.nextInt();
                obj2.nextLine(); // Consume the newline character
                String userName = obj2.nextLine();

                String availabilityQuery = "SELECT * FROM books WHERE book_no = ? AND available = 1";
                try (PreparedStatement availabilityStatement = connection.prepareStatement(availabilityQuery)) {
                    availabilityStatement.setInt(1, bookNo);
                    try (ResultSet resultSet = availabilityStatement.executeQuery()) {
                        if (resultSet.next()) {
                            String issueQuery = "INSERT INTO issued_books (book_no, user_name) VALUES (?, ?)";
                            try (PreparedStatement issueStatement = connection.prepareStatement(issueQuery)) {
                                issueStatement.setInt(1, bookNo);
                                issueStatement.setString(2, userName);
                                issueStatement.executeUpdate();
                                System.out.println("Book issued successfully!");

                                String markUnavailableQuery = "UPDATE books SET available = 0 WHERE book_no = ?";
                                try (PreparedStatement markUnavailableStatement = connection.prepareStatement(markUnavailableQuery)) {
                                    markUnavailableStatement.setInt(1, bookNo);
                                    markUnavailableStatement.executeUpdate();
                                }
                            }
                        } else {
                            System.out.println("Book not available for issuing.");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void ret() {
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false); // Disable auto-commit

            System.out.println("Enter Book Number");
            try (Scanner obj2 = new Scanner(System.in)) {
                int bookNo = obj2.nextInt();

                String availabilityQuery = "SELECT * FROM books WHERE book_no = ?";
                try (PreparedStatement availabilityStatement = connection.prepareStatement(availabilityQuery)) {
                    availabilityStatement.setInt(1, bookNo);
                    try (ResultSet resultSet = availabilityStatement.executeQuery()) {
                        if (resultSet.next()) {
                            int available = resultSet.getInt("available");
                            if (available == 1) {
                                System.out.println("The book is not issued and cannot be returned.");
                            } else {
                                String returnQuery = "DELETE FROM issued_books WHERE book_no = ?";
                                try (PreparedStatement returnStatement = connection.prepareStatement(returnQuery)) {
                                    returnStatement.setInt(1, bookNo);
                                    int rowsDeleted = returnStatement.executeUpdate();
                                    if (rowsDeleted > 0) {
                                        String markAvailableQuery = "UPDATE books SET available = 1 WHERE book_no = ?";
                                        try (PreparedStatement markAvailableStatement = connection.prepareStatement(markAvailableQuery)) {
                                            markAvailableStatement.setInt(1, bookNo);
                                            markAvailableStatement.executeUpdate();
                                        }
                                        System.out.println("Book returned successfully!");
                                        connection.commit(); // Commit the transaction
                                    } else {
                                        System.out.println("Book not found in issued books.");
                                    }
                                }
                            }
                        } else {
                            System.out.println("Book not found.");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void detail() {
        try (Connection connection = getConnection()) {
            String query = "SELECT * FROM issued_books";
            try (PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {
                System.out.println("Issued Books:");
                while (resultSet.next()) {
                    int bookNo = resultSet.getInt("book_no");
                    String userName = resultSet.getString("user_name");
                    System.out.println("Book Number: " + bookNo + ", User Name: " + userName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void exit() {
        System.exit(0);
    }
}
