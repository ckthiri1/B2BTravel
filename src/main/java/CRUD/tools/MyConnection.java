package CRUD.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {
    private String url="jdbc:mysql://localhost:3306/CRUD";
    private String login="root";
    private String pwd="";
    private static MyConnection instance;
    Connection cnx;
    private MyConnection() {
        try{
            cnx= DriverManager.getConnection(url,login,pwd);
            System.out.println("Connection established!");
        }
        catch(SQLException e){
            System.out.println("Error Connetion/"+e.getMessage());
        }
    }

    public Connection getCnx() {
        return cnx;
    }

    public static MyConnection getInstance(){
        if (instance==null){
            instance=new MyConnection();
        }
        return instance;
    }
}
