package Evennement.tools;


import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class MyConnection {
    private String url="jdbc:mysql://localhost:3306/b2btravel";
    private String login="root";
    private String pwd="";
    private static MyConnection instance;
    Connection cnx;

    public Connection getCnx() {
        return cnx;
    }
    public static MyConnection getInstance() {
        if (instance == null) {
            instance = new MyConnection();
        }
        return instance;

    }

    public MyConnection() {
        try{
            cnx = DriverManager.getConnection(url, login, pwd);
            System.out.println("Connection established");
        } catch (SQLException e) {
            System.out.println("Error connecting to database"+e.getMessage());
        }
    }
    public void closeConnection() {
        try {
            if (cnx != null && !cnx.isClosed()) {
                cnx.close();
                System.out.println("Connection closed");
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }


}
