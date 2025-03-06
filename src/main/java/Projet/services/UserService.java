package Projet.services;

import Projet.entities.Utilisateur;
import Projet.tools.MyConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {


    public Utilisateur getUserById(int IdUser) {
        String query = "SELECT * FROM utilisateur WHERE IdUser = ?";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query)) {

            pst.setInt(1, IdUser); // Set the ID parameter
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Utilisateur utilisateur = new Utilisateur();
                    utilisateur.setIdUser(rs.getInt("IdUser"));
                    utilisateur.setNom(rs.getString("nom"));
                    utilisateur.setPrenom(rs.getString("prenom"));
                    utilisateur.setEmail(rs.getString("email"));
                    utilisateur.setPassword(rs.getString("password"));
                    utilisateur.setNbrVoyage(rs.getInt("nbrVoyage"));
                    utilisateur.setRole(Utilisateur.Roles.valueOf(rs.getString("role")));

                    return utilisateur;
                } else {
                    throw new RuntimeException("User not found with ID: " + IdUser);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving user from database", e);
        }
    }

}
