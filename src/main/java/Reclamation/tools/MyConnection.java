package Reclamation.tools;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class MyConnection { private String url = "jdbc:mysql://localhost:3306/crudr"; // URL de la base de données
    private String login = "root"; // Nom d'utilisateur
    private String pwd = ""; // Mot de passe
    private static MyConnection instance; // Instance unique de la classe
    private Connection Cnx; // Objet de connexion

    // Constructeur privé pour empêcher l'instanciation directe
    public MyConnection() {
        try {
            // Établir la connexion à la base de données
            Cnx = DriverManager.getConnection(url, login, pwd);
            System.out.println("Connection established.");
        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
    }

    // Méthode pour obtenir l'instance unique de la classe
    public static MyConnection getInstance() {
        if (instance == null) {
            instance = new MyConnection();
        }
        return instance;
    }

    // Méthode pour obtenir l'objet de connexion
    public Connection getCnx() {
        return Cnx;
    }
}

