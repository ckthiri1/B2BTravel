package Projet.services;

import Projet.entities.Rank;
import Projet.interfaces.IRankService;
import Projet.tools.MyConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class RankService implements IRankService<Rank> {

    @Override
    public void addRank(Rank r) {

        String sql = "INSERT INTO rank (NomRank , points) VALUES (? , ?)";

        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(sql)) {
            pst.setString(1, r.getNomRank());
            pst.setInt(2, r.getPoints());

            int rowsInserted = pst.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("✅ Voyage Added Successfully!");
            }
        } catch (SQLException e) {
            System.err.println("⚠ Error Adding Voyage: " + e.getMessage());
        }

    }

    @Override
    public void updateRank(Rank r) {

        String sql = "UPDATE rank SET NomRank = ?, points = ? WHERE IDRang = ?";

            try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(sql)) {
                pst.setString(1, r.getNomRank());
                pst.setInt(2, r.getPoints());
                pst.setInt(3, r.getIDRang());

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Voyage Updated Successfully!");
            }
        } catch (SQLException e) {
            System.err.println("⚠️ Error Updating Rank: " + e.getMessage());
        }
    }

    @Override
    public List<Rank> getAllRank() {
        List<Rank> ranks = new ArrayList<>();
        String sql = "SELECT * FROM rank";

        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Rank v = new Rank();
                v.setIDRang(rs.getInt("IDRang"));
                v.setNomRank(rs.getString("NomRank"));
                v.setPoints(rs.getInt("points"));

                ranks.add(v);
            }

        } catch (SQLException e) {
            System.err.println("⚠️ Error Fetching Voyages: " + e.getMessage());
        }
        return ranks;
    }

    @Override
    public void deleteRank(int IDRang) {
        String sql = "DELETE FROM rank WHERE IDRang = ?";

        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(sql)) {
            pst.setInt(1, IDRang);

            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("✅ Voyage Deleted Successfully!");
            }
        } catch (SQLException e) {
            System.err.println("⚠️ Error Deleting Voyage: " + e.getMessage());
        }
    }

    public Rank getRankById(int rankId) {

        String req = "SELECT * FROM rank WHERE IDRang = ?";

        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(req)) {
            pst.setInt(1, rankId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {

                return new Rank(
                        rs.getInt("IDRang"),
                        rs.getString("NomRank"),
                        rs.getInt("points")
                );
            } else {

                return getDefaultRank();
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving rank by ID: " + e.getMessage());
            return getDefaultRank();
        }
    }

    private Rank getDefaultRank() {
        return new Rank(1, "SILVER",0);
    }

    public boolean existsByRankName(String rankName, int excludeId) {
        String query = "SELECT COUNT(*) FROM Rank r WHERE r.nomRank = ? AND r.IDRang != ?";

        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query)){

            pst.setString(1, rankName);
            pst.setLong(2, excludeId);

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
