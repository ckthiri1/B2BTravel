package Evennement.services;

import Evennement.entities.Evennement;
import Evennement.entities.Organisateur;
import Evennement.interfaces.IService;
import Evennement.tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrganisateurService implements IService <Organisateur>{
    @Override
    public void add(Organisateur organisateur) {
        String requete = "INSERT INTO organisateur (NomOr, contact) VALUES (?, ?)";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, organisateur.getNomOr());
            pst.setInt(2, organisateur.getContact());


            pst.executeUpdate();
            System.out.println("Organisateur ajouté");

        } catch (SQLException exception) {
            System.out.println("Error: " + exception.getMessage());
        }
    }

    @Override
    public void update(Organisateur organisateur, int IDOr) {
        String requete = "UPDATE organisateur SET NomOr = ?, Contact = ? WHERE IDOr = ?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, organisateur.getNomOr());
            pst.setInt(2, organisateur.getContact());
            pst.setInt(3, IDOr);

            pst.executeUpdate();
            System.out.println("✅ Organisateur with ID " + IDOr + " updated!");
        } catch (SQLException exception) {
            System.out.println("❌ SQL Error: " + exception.getMessage());
        }
    }
    @Override
    public void delete(int Idor) {
        try {
            String requete = "DELETE FROM organisateur WHERE IDOr = ?";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setInt(1, Idor);
            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Organisateur with ID " +Idor+ " deleted successfully!");
            } else {
                System.out.println("⚠️ No Organisateur found with ID " + Idor);
            }
        } catch (SQLException exception) {
            System.out.println("❌ SQL Error: " + exception.getMessage());
        }
    }

    @Override
    public List<Organisateur> getAllData() {
        List<Organisateur> organisateurs = new ArrayList<>();
        try {

            String query = "SELECT IDOr, NomOr, Contact FROM organisateur";
            Statement stmt = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // Process the result set and create Evennement objects
            while (rs.next()) {
                int IDOr = rs.getInt("IDOr");
                String NomOr = rs.getString("NomOr");
              int Contact = rs.getInt("Contact");

               Organisateur organisateur = new Organisateur(NomOr, Contact);
               organisateur.setIDOr(IDOr); // Set the ID if it's not set in the constructor
                organisateurs.add(organisateur);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error fetching data: " + e.getMessage());
        }

        // Return the list of events
        return organisateurs;
    }
}
