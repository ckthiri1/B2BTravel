package Projet.services;

import Projet.entities.Voyage;
import Projet.interfaces.IVoyageService;
import Projet.tools.MyConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VoyageService implements IVoyageService<Voyage> {
    @Override
    public void addVoyage(Voyage v) {
        String sql = "INSERT INTO voyage (Depart, Destination, Description) VALUES (?, ?, ?)";

        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(sql)) {
            pst.setString(1, v.getDepart());
            pst.setString(2, v.getDestination());
            pst.setString(3, v.getDescription());

            int rowsInserted = pst.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("✅ Voyage Added Successfully!");
            }
        } catch (SQLException e) {
            System.err.println("⚠ Error Adding Voyage: " + e.getMessage());
        }
    }

    @Override
    public List<Voyage> getAllVoyages() {
        List<Voyage> voyages = new ArrayList<>();
        String sql = "SELECT * FROM voyage";

        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Voyage v = new Voyage();
                v.setVID(rs.getInt("VID"));
                v.setDepart(rs.getString("Depart"));
                v.setDestination(rs.getString("Destination"));
                v.setDescription(rs.getString("Description"));

                voyages.add(v);
            }

        } catch (SQLException e) {
            System.err.println("⚠️ Error Fetching Voyages: " + e.getMessage());
        }
        return voyages;
    }

    @Override
    public void updateVoyage(Voyage v) {
        String sql = "UPDATE voyage SET depart = ?, destination = ?, description = ? WHERE VID = ?";

        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(sql)) {
            pst.setString(1, v.getDepart());
            pst.setString(2, v.getDestination());
            pst.setString(3, v.getDescription());
            pst.setInt(4, v.getVID());

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Voyage Updated Successfully!");
            }
        } catch (SQLException e) {
            System.err.println("⚠️ Error Updating Voyage: " + e.getMessage());
        }
    }

    @Override
    public void deleteVoyage(int vid) {

            String sql = "DELETE FROM voyage WHERE vid = ?";

            try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(sql)) {
                pst.setInt(1, vid);

                int rowsDeleted = pst.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("✅ Voyage Deleted Successfully!");
                }
            } catch (SQLException e) {
                System.err.println("⚠️ Error Deleting Voyage: " + e.getMessage());
            }
        }

    public boolean checkExisistance(String depart, String destination, int excludeId) {
        String query = "SELECT COUNT(*) FROM Voyage v WHERE v.depart = ? AND v.destination = ? AND v.VID != ?";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query)){

            pst.setString(1, depart);
            pst.setString(2, destination);
            pst.setInt(3, excludeId);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    }
