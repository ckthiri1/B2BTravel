package Projet.services;

import Projet.entities.Fidelite;
import Projet.entities.Rank;
import Projet.entities.User;
import Projet.tools.MyConnection;

import java.sql.*;

public class FideliteService {

    private RankService rankService = new RankService();
    private UserService userService = new UserService();


    public void addFidelite(User user) {
        String req = "INSERT INTO fidelite (points, remise, IdRank, idUser) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(req)) {

            Fidelite fidelite = calculateFidelite(user);

            pst.setInt(1, fidelite.getPoints());
            pst.setDouble(2, fidelite.getRemise());
            pst.setInt(3, fidelite.getRang().getIDRang());
            pst.setInt(4, user.getUser_id());

            pst.executeUpdate();
            System.out.println("Fidelite created successfully!");
        } catch (SQLException e) {
            System.err.println("Error in addFidelite: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void updateFidelite(User user) {
        String req = "UPDATE fidelite SET points = ?, remise = ?, IdRank = ? WHERE idUser = ?";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(req)) {
            Fidelite fidelite = calculateFidelite(user);

            pst.setInt(1, fidelite.getPoints());
            pst.setDouble(2, fidelite.getRemise());
            pst.setInt(3, fidelite.getRang().getIDRang());
            pst.setInt(4, user.getUser_id());

            pst.executeUpdate();
            System.out.println("Fidelite updated successfully!");
        } catch (SQLException e) {
            System.err.println("Error in updateFidelite: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Fidelite getFideliteByUser(int userId) {
        String req = "SELECT f.*, r.* FROM fidelite f " +
                "JOIN rank r ON f.IdRank = r.IDRang " +
                "WHERE f.idUser = ?";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(req)) {
            pst.setInt(1, userId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Rank rank = new Rank(
                            rs.getInt("IDRang"),
                            rs.getString("NomRank"),
                            rs.getInt("points")
                    );

                    User user = userService.getCurrentUser();

                    return new Fidelite(
                            rs.getInt("IDF"),
                            rs.getInt("points"),
                            rs.getDouble("remise"),
                            rank,
                            user
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getFideliteByUser: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public Fidelite calculateFidelite(User user) {
        int points = user.getNbrVoyage() / 2;
        double remise = (points / 3) * 0.3;

        int rankId;
        if (points >= 20) {
            rankId = 4;
        } else if (points >= 15) {
            rankId = 3;
        } else if (points >= 10) {
            rankId = 2;
        } else {
            rankId = 1;
        }


        Rank rank = rankService.getRankById(rankId);

        return new Fidelite(
                0,
                points,
                remise,
                rank,
                user
        );
    }



}
