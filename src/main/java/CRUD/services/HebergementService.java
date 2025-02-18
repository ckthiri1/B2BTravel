package CRUD.services;

import CRUD.entities.Hebergement;
import CRUD.interfaces.IService;
import CRUD.tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HebergementService implements IService<Hebergement> {
    @Override
    public void addEntity(Hebergement hebergement) {
        try{
            String requette ="INSERT INTO hebergement(nom, adresse, type, description) " +
                    "VALUES ('"+hebergement.getNom()+"','"+ hebergement.getAdresse()+"','"+hebergement.getType()+"','"+hebergement.getdescription()+"')";
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            st.executeUpdate(requette);
            System.out.println("Hebergement ajoutée");
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addEntity2(Hebergement hebergement) {
        try{
            String requette ="INSERT INTO hebergement( nom, adresse, type, description) " +
                    "VALUES (? , ? , ? , ?)";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requette);

            pst.setString(1, hebergement.getNom());
            pst.setString(2, hebergement.getAdresse());
            pst.setString(3, hebergement.getType());
            pst.setString(4, hebergement.getdescription());

            pst.executeUpdate();
            System.out.println("success!");
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateEntity(int id, Hebergement hebergement) {
        if (hebergement == null) {
            System.out.println("Erreur: L'objet hébergement est null.");
            return;
        }

        try {
            String requete = "UPDATE hebergement SET nom = ?, adresse = ?, type = ?, description = ? WHERE id_hebergement = ?";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);

            pst.setString(1, hebergement.getNom());
            pst.setString(2, hebergement.getAdresse());
            pst.setString(3, hebergement.getType());
            pst.setString(4, hebergement.getdescription());
            pst.setInt(5, id);

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Hébergement mis à jour avec succès! (ID: " + id + ")");
            } else {
                System.out.println("Erreur: Aucun hébergement trouvé avec l'ID: " + id);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour: " + e.getMessage());
        }
    }


    @Override
    public boolean deleteEntity(Hebergement hebergement) {
        if (hebergement == null) {
            System.out.println("Erreur: L'objet hébergement est null.");
            return false;
        }

        try {
            // Step 1: Retrieve the ID from the database based on the provided object
            String getIdQuery = "SELECT id_hebergement FROM hebergement WHERE nom = ? AND adresse = ? AND type = ? AND description = ?";
            PreparedStatement getIdPst = MyConnection.getInstance().getCnx().prepareStatement(getIdQuery);
            getIdPst.setString(1, hebergement.getNom());
            getIdPst.setString(2, hebergement.getAdresse());
            getIdPst.setString(3, hebergement.getType());
            getIdPst.setString(4, hebergement.getdescription());

            ResultSet rs = getIdPst.executeQuery();
            if (!rs.next()) {
                System.out.println("Aucun hébergement correspondant trouvé.");
                return false;
            }

            int id_hebergement = rs.getInt("id_hebergement");

            // Step 2: Delete the Hebergement using the retrieved ID
            String deleteQuery = "DELETE FROM hebergement WHERE id_hebergement = ?";
            PreparedStatement deletePst = MyConnection.getInstance().getCnx().prepareStatement(deleteQuery);
            deletePst.setInt(1, id_hebergement);

            int rowsDeleted = deletePst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Hébergement supprimé avec succès! (ID: " + id_hebergement + ")");
            } else {
                System.out.println("Erreur: L'hébergement n'a pas pu être supprimé.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression: " + e.getMessage());
        }
        return false;
    }


    @Override
    public List<Hebergement> getAllData() {
        List<Hebergement> results = new ArrayList<>();
        String requette = "SELECT * FROM hebergement";
        try {
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(requette);
            while (rs.next()) {
                Hebergement p = new Hebergement();
                p.setId_hebergement(rs.getInt("id_hebergement"));
                p.setNom(rs.getString("nom"));
                p.setAdresse(rs.getString("adresse"));
                p.setType(rs.getString("type"));
                p.setdescription(rs.getString("description"));
                results.add(p);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return results;
    }

    public Hebergement getById(int id) {
        Hebergement hebergement = null;
        String query = "SELECT * FROM hebergement WHERE id_hebergement = ?";
        try {
            PreparedStatement preparedStatement = MyConnection.getInstance().getCnx().prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                hebergement = new Hebergement();
                hebergement.setId_hebergement(rs.getInt("id_hebergement"));
                hebergement.setNom(rs.getString("nom"));
                hebergement.setAdresse(rs.getString("adresse"));
                hebergement.setType(rs.getString("type"));
                hebergement.setdescription(rs.getString("description"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return hebergement;
    }


}
