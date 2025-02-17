package Reclamation.Services;

import Reclamation.entities.Reclamation;
import Reclamation.interfaces.Service;
import Reclamation.tools.MyConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReclamationServices implements Service<Reclamation> {

    @Override
    public int addEntity(Reclamation reclamation) {
        String requete = "INSERT INTO Reclamation (Titre, Description, DateR, Status) VALUES (?, ?, ?, ?)";
        int generatedId = -1;

        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, reclamation.getTitre());
            pst.setString(2, reclamation.getDescription());
            pst.setDate(3, java.sql.Date.valueOf(reclamation.getDateR()));
            pst.setString(4, reclamation.getStatus());

            int rowsInserted = pst.executeUpdate();

            if (rowsInserted > 0) {
                ResultSet rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                    System.out.println("Réclamation ajoutée avec succès ! ID : " + generatedId);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la réclamation : " + e.getMessage());
        }

        return generatedId;
    }

    @Override
    public boolean deleteEntity(Reclamation reclamation) {
        String requete = "DELETE FROM Reclamation WHERE IDR = ?";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete)) {
            pst.setInt(1, reclamation.getIDR());
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Réclamation supprimée avec succès !");
                return true; // Réclamation supprimée avec succès
            } else {
                System.out.println("Aucune réclamation correspondante trouvée.");
                return false; // Aucune réclamation trouvée
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la réclamation : " + e.getMessage());
        }
        return false;
    }

    @Override
    public void updateEntity(int id, Reclamation reclamation) {
        String requete = "UPDATE Reclamation SET Titre = ?, Description = ?, DateR = ?, Status = ? WHERE IDR = ?";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete)) {

            pst.setString(1, reclamation.getTitre());
            pst.setString(2, reclamation.getDescription());
            pst.setDate(3, Date.valueOf(reclamation.getDateR()));
            pst.setString(4, reclamation.getStatus());
            pst.setInt(5, id);

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Réclamation mise à jour avec succès !");
            } else {
                System.out.println("Aucune réclamation correspondante trouvée.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la réclamation : " + e.getMessage());
        }
    }

    @Override
    public List<Reclamation> getAllData() {
        List<Reclamation> reclamations = new ArrayList<>();
        try {
            // SQL query to fetch all reclamations from the Reclamation table
            String query = "SELECT IDR, Titre, Description, DateR, Status FROM Reclamation";

            // Create a statement
            Statement stmt = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // Process the result set and create Reclamation objects
            while (rs.next()) {
                int IDR = rs.getInt("IDR"); // Corrected IDR to match your column name
                String titre = rs.getString("Titre"); // Corrected Titre to match your column name
                String description = rs.getString("Description"); // Corrected Description to match your column name
                String status = rs.getString("Status"); // Corrected Status to match your column name

                // Get the date as java.sql.Date and convert to LocalDate
                java.sql.Date sqlDate = rs.getDate("DateR");
                LocalDate dateR = (sqlDate != null) ? sqlDate.toLocalDate() : null; // Convert to LocalDate if not null

                // Create a Reclamation object and add it to the list
                Reclamation reclamation = new Reclamation(IDR, titre, description, dateR, status); // Using the constructor with IDR
                reclamations.add(reclamation);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error fetching data: " + e.getMessage());
        }

        // Return the list of reclamations
        return reclamations;
    }

    public boolean updateEntity(Reclamation reclamationAModifier) {
        return true;
    }
}

