package Projet.services;

import Projet.entities.Vol;
import Projet.tools.MyConnection;
import Projet.tools.UserSession;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VolService {
    private Connection connection;

    public VolService() {
        connection = MyConnection.getInstance().getCnx();
    }

    public void addVol(Vol vol) {
        String query = "INSERT INTO vol (dateDepart, dateArrival, airLine, flightNumber, dureeVol, prixVol, typeVol, idVoyage, user_id, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setTimestamp(1, new Timestamp(vol.getDateDepart().getTime()));
            statement.setTimestamp(2, new Timestamp(vol.getDateArrival().getTime()));
            statement.setString(3, vol.getAirLine());
            statement.setInt(4, vol.getFlightNumber());
            statement.setString(5, vol.getDureeVol());
            statement.setInt(6, vol.getPrixVol());
            statement.setString(7, vol.getTypeVol().name());
            statement.setInt(8, vol.getVoyage().getVID());
            statement.setInt(9, vol.getUser().getUser_id());
            statement.setString(10, Vol.StatusVol.NON_RESERVER.name());

            statement.executeUpdate();
            System.out.println("Flight added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean deleteVol(int id) {
        String query = "DELETE FROM vol WHERE volID = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Vol> getVolsByUserId() {
        List<Vol> vols = new ArrayList<>();
        int userId = UserSession.getInstance().getUserId();

        if (userId == -1) {
            System.out.println("No user logged in!");
            return vols;
        }

        String query = "SELECT * FROM vol WHERE user_id = ? AND status = 'NON_RESERVER'";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Vol vol = new Vol(
                        resultSet.getInt("volID"),
                        resultSet.getTimestamp("dateDepart"),
                        resultSet.getTimestamp("dateArrival"),
                        resultSet.getString("airLine"),
                        resultSet.getInt("flightNumber"),
                        resultSet.getString("dureeVol"),
                        resultSet.getInt("prixVol"),
                        Vol.TypeVol.valueOf(resultSet.getString("typeVol")),
                        null,
                        null,
                        Vol.StatusVol.NON_RESERVER);
                vol.setStatus(Vol.StatusVol.valueOf(resultSet.getString("status"))); // ✅ Get status from DB
                vols.add(vol);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vols;
    }

    public List<Vol> getVolsReserverByUserId() {
        List<Vol> vols = new ArrayList<>();
        int userId = UserSession.getInstance().getUserId();

        if (userId == -1) {
            System.out.println("No user logged in!");
            return vols;
        }

        String query = "SELECT * FROM vol WHERE user_id = ? AND status = 'RESERVER'";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Vol vol = new Vol(
                        resultSet.getInt("volID"),
                        resultSet.getTimestamp("dateDepart"),
                        resultSet.getTimestamp("dateArrival"),
                        resultSet.getString("airLine"),
                        resultSet.getInt("flightNumber"),
                        resultSet.getString("dureeVol"),
                        resultSet.getInt("prixVol"),
                        Vol.TypeVol.valueOf(resultSet.getString("typeVol")),
                        null,
                        null,
                        Vol.StatusVol.RESERVER);
                vol.setStatus(Vol.StatusVol.valueOf(resultSet.getString("status")));
                vols.add(vol);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vols;
    }



}
