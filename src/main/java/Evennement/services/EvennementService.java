package Evennement.services;
import Evennement.entities.Evennement;
import Evennement.interfaces.IService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.PreparedStatement;
import Evennement.tools.MyConnection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;


public class EvennementService implements IService <Evennement> {

    @Override
    public void add(Evennement e) {
        String requete = "INSERT INTO Evennement (NomE, Local, DateE, DesE) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, e.getNomE());
            pst.setString(2, e.getLocal());
            pst.setTimestamp(3, new Timestamp(e.getDateE().getTime())); // Convert Date to Timestamp
            pst.setString(4, e.getDesE());

            pst.executeUpdate();
            System.out.println("Evennement ajouté");

        } catch (SQLException exception) {
            System.out.println("Error: " + exception.getMessage());
        }
    }

    @Override
    public void update(Evennement e, int IDE) {
        String requete = "UPDATE Evennement SET NomE = ?, Local = ?, DateE = ?, DesE = ? WHERE IDE = ?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, e.getNomE());
            pst.setString(2, e.getLocal());
            pst.setTimestamp(3, new Timestamp(e.getDateE().getTime())); // Convert Date to Timestamp
            pst.setString(4, e.getDesE());
            pst.setInt(5, IDE); // Use provided IDE parameter instead of e.getIDE()

            pst.executeUpdate();
            System.out.println("✅ Evennement with ID " + IDE + " updated!");
        } catch (SQLException exception) {
            System.out.println("❌ SQL Error: " + exception.getMessage());
        }
    }




    @Override
    public void delete(int IDE) {
        try {
            String requete = "DELETE FROM Evennement WHERE IDE = ?";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setInt(1, IDE); // Bind the event ID directly
            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Event with ID " + IDE + " deleted successfully!");
            } else {
                System.out.println("⚠️ No event found with ID " + IDE);
            }
        } catch (SQLException exception) {
            System.out.println("❌ SQL Error: " + exception.getMessage());
        }
    }


    @Override
    public List<Evennement> getAllData() {
        List<Evennement> events = new ArrayList<>();
        try {
            // SQL query to fetch all events from the Evennement table
            String query = "SELECT IDE, NomE, Local, DateE, DesE FROM Evennement";

            // Create a statement
            Statement stmt = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // Process the result set and create Evennement objects
            while (rs.next()) {
                int IDE = rs.getInt("IDE");
                String nomE = rs.getString("NomE");
                String local = rs.getString("Local");
                String desE = rs.getString("DesE");

                // Get the date as java.sql.Date
                java.sql.Date sqlDate = rs.getDate("DateE");

                // Convert java.sql.Date to java.util.Date
                java.util.Date dateE = new java.util.Date(sqlDate.getTime());

                // Create an Evennement object and add it to the list
                Evennement evennement = new Evennement(nomE, local, desE, dateE);
                evennement.setIDE(IDE); // Set the ID if it's not set in the constructor
                events.add(evennement);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error fetching data: " + e.getMessage());
        }

        // Return the list of events
        return events;
    }
}
