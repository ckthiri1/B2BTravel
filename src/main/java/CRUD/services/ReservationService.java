package CRUD.services;

import CRUD.entities.Hebergement;
import CRUD.entities.Reservation;
import CRUD.interfaces.IService;
import CRUD.tools.MyConnection;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class ReservationService  implements IService<Reservation> {


    public void addEntity(Reservation reservation) {
        try{
            String requette ="INSERT INTO reservation_heberge( date, prix, status, idH) " +
                    "VALUES ('"+reservation.getDate_reservation()+"','"+ reservation.getPrix()+"','"+reservation.getStatus()+"','"+reservation.getHebergement_id()+"')";
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            st.executeUpdate(requette);
            System.out.println("Reservation ajoutée");
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addEntity2(Reservation reservation) {
        try{
            String requette ="INSERT INTO reservation_heberge( date, prix, status, idH) " +
                    "VALUES (? , ? , ? , ?)";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requette);
            pst.setTimestamp(1, Timestamp.valueOf(reservation.getDate_reservation()));
            pst.setInt(2, reservation.getPrix());
            pst.setString(3, reservation.getStatus());
            pst.setInt(4, reservation.getHebergement_id());

            pst.executeUpdate();
            System.out.println("success!");
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateEntity(int id, Reservation reservation) {
        try {
            String requete = "UPDATE reservation_heberge SET date = ?, prix = ?, status = ?, idH = ? WHERE id_resH = ?";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);

            // Convert LocalDateTime to Timestamp
            pst.setTimestamp(1, Timestamp.valueOf(reservation.getDate_reservation()));
            pst.setDouble(2, reservation.getPrix());
            pst.setString(3, reservation.getStatus());
            pst.setInt(4, reservation.getHebergement_id());
            pst.setInt(5, id);

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Reservation mise à jour avec succès!");
            } else {
                System.out.println("Aucune réservation trouvée avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour: " + e.getMessage());
        }
    }


    @Override
    public boolean deleteEntity(Reservation reservation) {
        if (reservation == null) {
            System.out.println("Erreur: L'objet réservation est null.");
            return false;
        }

        try {
            // Step 1: Delete the reservation using its ID
            String deleteQuery = "DELETE FROM reservation_heberge WHERE id_resH = ?";
            PreparedStatement deletePst = MyConnection.getInstance().getCnx().prepareStatement(deleteQuery);
            deletePst.setInt(1, reservation.getId_reservation());

            int rowsDeleted = deletePst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Réservation supprimée avec succès! (ID: " + reservation.getId_reservation() + ")");
                return true; // Return true if deletion is successful
            } else {
                System.out.println("Erreur: La réservation n'a pas pu être supprimée.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression: " + e.getMessage());
        }
        return false;
    }




    @Override
    public List <Reservation> getAllData() {
        List<Reservation> results = new ArrayList<>();
        String requette = "SELECT * FROM reservation_heberge";
        try {
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(requette);
            while (rs.next()) {
                Reservation p = new Reservation();
                p.setId_reservation(rs.getInt("id_resH"));
                p.setDate_reservation(rs.getTimestamp("date").toLocalDateTime());
                p.setPrix(rs.getInt("prix"));
                p.setStatus(rs.getString("status"));
                p.setHebergement_id(rs.getInt("idH"));
                results.add(p);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return results;
    }
    public List<String> getHebergementNames() {
        List<String> hebergementNames = new ArrayList<>();
        String requete = "SELECT nom FROM hebergement"; // Modifier selon ta structure
        try {
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()) {
                hebergementNames.add(rs.getString("nom"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return hebergementNames;
    }

    public List<Hebergement> getAllHebergements() {
        List<Hebergement> hebergements = new ArrayList<>();
        String query = "SELECT id_hebergement, nom FROM hebergement"; // Ensure correct column names

        try (Statement st = MyConnection.getInstance().getCnx().createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id_hebergement"); // Ensure correct column name
                String nom = rs.getString("nom");
                hebergements.add(new Hebergement(id, nom));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des hébergements : " + e.getMessage());
        }

        return hebergements;
    }



}
