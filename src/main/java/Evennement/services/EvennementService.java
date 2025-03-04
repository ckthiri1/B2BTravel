package Evennement.services;

import Evennement.entities.Evennement;
import Evennement.entities.Organisateur;
import Evennement.entities.EventType;  // Ensure EventType is imported
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

            // Ensure Organisateur is not null before inserting
            if (e.getOrganisateur() != null && e.getOrganisateur().getIDOr() != 0) {
                pst.setInt(5, e.getOrganisateur().getIDOr());
            } else {
                throw new IllegalArgumentException("Organisateur cannot be null or have ID 0 for an Evennement.");
            }

            // Insert event type as a string (check if eventType is null before)
            if (e.getEventType() != null) {
                pst.setString(6, e.getEventType().name());
            } else {
                pst.setString(6, null);
            }

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
        String requete = "UPDATE Evennement SET NomE = ?, Local = ?, DateE = ?, DesE = ?, IDOr = ?, event_type = ? WHERE IDE = ?";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete)) {
            pst.setString(1, e.getNomE());
            pst.setString(2, e.getLocal());
            pst.setTimestamp(3, new Timestamp(e.getDateE().getTime()));
            pst.setString(4, e.getDesE());
            pst.setInt(5, e.getOrganisateur().getIDOr());

            // Debugging: Verify the eventType value before updating
            System.out.println("Updating EventType: " + e.getEventType());  // Debugging print statement

            if (e.getEventType() != null) {
                pst.setString(6, e.getEventType().name());
            } else {
                pst.setString(6, null);  // Ensure null is set if eventType is null
            }

            pst.setInt(7, IDE);

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
    public List<Evennement> getAllData() {
        List<Evennement> events = new ArrayList<>();
        String query = "SELECT e.IDE, e.NomE, e.Local, e.DateE, e.DesE, e.event_type, o.NomOr FROM Evennement e JOIN Organisateur o ON e.IDOr = o.IDOr";

        try (Statement stmt = MyConnection.getInstance().getCnx().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int IDE = rs.getInt("IDE");
                String nomE = rs.getString("NomE");
                String local = rs.getString("Local");
                String desE = rs.getString("DesE");
                Date dateE = new Date(rs.getTimestamp("DateE").getTime());
                String eventTypeString = rs.getString("event_type");  // Retrieve event_type as string
                EventType eventType = eventTypeString != null ? EventType.valueOf(eventTypeString) : null;

                String NomOr = rs.getString("NomOr");

                // Retrieve the Organisateur details
                int IDOr = rs.getInt("IDOr");
                String nomOr = rs.getString("NomOr");
                String Contact = rs.getString("Contact");

                Organisateur organisateur = new Organisateur(IDOr, nomOr, Contact);

                Evennement evennement = new Evennement(nomE, local, desE, dateE, organisateur, eventType);  // Set eventType
                evennement.setIDE(IDE);
                events.add(evennement);
            }
        } catch (SQLException e) {
            System.out.println("❌ SQL Error while fetching events: " + e.getMessage());
        }

        return events;
    }























    @Override
    public void delete(int IDE) {
        String requete = "DELETE FROM Evennement WHERE IDE = ?";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete)) {
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
}
