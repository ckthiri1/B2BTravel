package Projet.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {
    private final String Url = "jdbc:mysql://127.0.0.1:3308/integration";
    private final String Login = "root";
    private final String Pwd = "";

    private static MyConnection instance;
    private Connection cnx;

    // Private constructor
    private MyConnection() {
        establishConnection();
    }

    // Method to (re)establish the connection
    private void establishConnection() {
        try {
            // Load the MySQL JDBC driver explicitly (optional but useful for debugging)
            Class.forName("com.mysql.cj.jdbc.Driver");
            cnx = DriverManager.getConnection(Url, Login, Pwd);
            System.out.println("Connection established");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
        }
    }

    public static MyConnection getInstance() {
        if (instance == null) {
            instance = new MyConnection();
        }
        return instance;
    }

    // Updated getCnx method to check if connection is closed or null
    public Connection getCnx() {
        try {
            if (cnx == null || cnx.isClosed()) {
                System.out.println("Re-establishing connection...");
                establishConnection();
            }
        } catch (SQLException e) {
            System.err.println("Error checking connection status: " + e.getMessage());
        }
        return cnx;
    }
}
