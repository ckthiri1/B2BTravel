package Evennement.services;

import Evennement.entities.Evennement;
import Evennement.entities.Organisateur;
import Evennement.interfaces.IService;
import Evennement.tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EvennementService implements IService<Evennement> {
    @Override
    public void add(Evennement e) {
        String requete = "INSERT INTO Evennement (NomE, Local, DateE, DesE, IDOr, event_type) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete)) {
            pst.setString(1, e.getNomE());
            pst.setString(2, e.getLocal());
            pst.setTimestamp(3, (e.getDateE() != null) ? new Timestamp(e.getDateE().getTime()) : null); // Handle null DateE
            pst.setString(4, e.getDesE());

            // ✅ Ensure Organisateur is not null before inserting
            if (e.getOrganisateur() != null && e.getOrganisateur().getIDOr() != 0) {
                pst.setInt(5, e.getOrganisateur().getIDOr());
            } else {
                throw new IllegalArgumentException("Organisateur cannot be null or have ID 0 for an Evennement.");
            }

            // ✅ Insert event type as a string
            pst.setString(6, (e.getEventType() != null) ? e.getEventType().name() : null);

            pst.executeUpdate();
            System.out.println("✅ Evennement ajouté avec succès!");
        } catch (SQLException exception) {
            System.err.println("❌ SQL Error: " + exception.getMessage());
            exception.printStackTrace();
            throw new RuntimeException("Error adding event", exception);
        }
    }
    @Override
    public void update(Evennement e, int IDE) {
        String requete = "UPDATE Evennement SET NomE = ?, Local = ?, DateE = ?, DesE = ?, IDOr = ? WHERE IDE = ?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, e.getNomE());
            pst.setString(2, e.getLocal());
            pst.setTimestamp(3, new Timestamp(e.getDateE().getTime()));
            pst.setString(4, e.getDesE());
            pst.setInt(5, e.getOrganisateur().getIDOr()); // Update the foreign key
            pst.setInt(6, IDE);

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Evennement avec ID " + IDE + " mis à jour !");
            } else {
                System.out.println("⚠️ Aucun événement trouvé avec ID " + IDE);
            }
        } catch (SQLException exception) {
            System.out.println("❌ SQL Error: " + exception.getMessage());
        }
    }
    @Override
    public void delete(int IDE) {
        String requete = "DELETE FROM Evennement WHERE IDE = ?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setInt(1, IDE);
            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Evennement avec ID " + IDE + " supprimé avec succès!");
            } else {
                System.out.println("⚠️ Aucun événement trouvé avec ID " + IDE);
            }
        } catch (SQLException exception) {
            System.out.println("❌ SQL Error: " + exception.getMessage());
        }
    }

    @Override
    public List<Evennement> getAllData() {
        List<Evennement> events = new ArrayList<>();
        String query = "SELECT e.IDE, e.NomE, e.Local, e.DateE, e.DesE, o.NomOr FROM Evennement e JOIN Organisateur o ON e.IDOr = o.IDOr";

        try {
            Statement stmt = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int IDE = rs.getInt("IDE");
                String nomE = rs.getString("NomE");
                String local = rs.getString("Local");
                String desE = rs.getString("DesE");
                Date dateE = new Date(rs.getTimestamp("DateE").getTime());
                String NomOr = rs.getString("NomOr");

                // Retrieve the Organisateur details
                int IDOr = rs.getInt("IDOr"); // ✅ This should now work correctly
                String nomOr = rs.getString("NomOr");
                int Contact = rs.getInt("Contact");

                Organisateur organisateur = new Organisateur(IDOr, nomOr, Contact);

                Evennement evennement = new Evennement(nomE, local, desE, dateE, organisateur);
                evennement.setIDE(IDE);
                events.add(evennement);
            }
        } catch (SQLException e) {
            System.out.println("❌ SQL Error while fetching events: " + e.getMessage());
        }

        return events;
    }
}
