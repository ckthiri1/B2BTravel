package Reclamation.Services;

import Reclamation.entities.Reponse;
import Reclamation.tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReponseServices {

    public int addEntity(Reponse reponse) {
        String requete = "INSERT INTO Reponse (DescriptionRep, DateRep, IDR) VALUES (?, ?, ?)";
        int generatedId = -1;

        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, reponse.getDescriptionRep());
            pst.setDate(2, java.sql.Date.valueOf(reponse.getDateRep()));
            pst.setInt(3, reponse.getIDR()); // Utilisation de IDR pour faire le lien avec la réclamation

            int rowsInserted = pst.executeUpdate();
            if (rowsInserted > 0) {
                ResultSet rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                    System.out.println("Réponse ajoutée avec succès ! ID : " + generatedId);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la réponse : " + e.getMessage());
        }
        return generatedId;
    }

    public void deleteEntity(Reponse reponse) {
        String requete = "DELETE FROM Reponse WHERE IDRep = ?";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete)) {
            pst.setInt(1, reponse.getIDRep());
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Réponse supprimée avec succès !");
            } else {
                System.out.println("Aucune réponse correspondante trouvée.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la réponse : " + e.getMessage());
        }
    }

    public void updateEntity(int id, Reponse reponse) {
        String requete = "UPDATE Reponse SET DescriptionRep = ?, DateRep = ?, IDR = ? WHERE IDRep = ?";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete)) {
            pst.setString(1, reponse.getDescriptionRep());
            pst.setDate(2, java.sql.Date.valueOf(reponse.getDateRep()));
            pst.setInt(3, reponse.getIDR());
            pst.setInt(4, id);

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Réponse mise à jour avec succès !");
            } else {
                System.out.println("Aucune réponse correspondante trouvée.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la réponse : " + e.getMessage());
        }
    }

    public List<Reponse> getAllData() {
        List<Reponse> results = new ArrayList<>();
        String requete = "SELECT * FROM Reponse";

        try (Statement st = MyConnection.getInstance().getCnx().createStatement();
             ResultSet rs = st.executeQuery(requete)) {

            while (rs.next()) {
                Reponse r = new Reponse();
                r.setIDRep(rs.getInt("IDRep"));
                r.setDescriptionRep(rs.getString("DescriptionRep"));
                r.setDateRep(rs.getDate("DateRep").toLocalDate());
                r.setIDR(rs.getInt("IDR")); // Lier la réponse à la réclamation via IDR

                results.add(r);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des données : " + e.getMessage());
        }
        return results;
    }

    // Méthode pour obtenir les réponses par réclamation
    public List<Reponse> getReponsesByReclamation(int idReclamation) {
        List<Reponse> results = new ArrayList<>();
        String requete = "SELECT * FROM Reponse WHERE IDR = ?";

        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete)) {
            pst.setInt(1, idReclamation);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Reponse r = new Reponse();
                r.setIDRep(rs.getInt("IDRep"));
                r.setDescriptionRep(rs.getString("DescriptionRep"));
                r.setDateRep(rs.getDate("DateRep").toLocalDate());
                r.setIDR(rs.getInt("IDR"));

                results.add(r);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des réponses : " + e.getMessage());
        }
        return results;
    }
}
