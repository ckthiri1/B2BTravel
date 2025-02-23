package Projet.services;

import Projet.entities.User;
import Projet.interfaces.IUserService;
import Projet.tools.MyConnection;
import Projet.tools.PassSecurity;
import Projet.tools.UserSession;

import java.security.SecureRandom;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class UserService implements IUserService {
    PassSecurity ps =new PassSecurity();

    // Generate a unique token
    public String generateToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    // Store the token in the database
    public void storeResetToken(String email, String token) {
        String query = "UPDATE user SET reset_token = ?, token_expiry = ? WHERE email = ?";
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(30); // Token valid for 30 minutes

        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query)) {
            pst.setString(1, token);
            pst.setTimestamp(2, Timestamp.valueOf(expiryTime));
            pst.setString(3, email);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Check if the token is valid
    public boolean isValidToken(String token) {
        String query = "SELECT token_expiry FROM user WHERE reset_token = ?";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query)) {
            pst.setString(1, token);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                LocalDateTime expiry = rs.getTimestamp("token_expiry").toLocalDateTime();
                return LocalDateTime.now(ZoneId.systemDefault()).isBefore(expiry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isEmailExist(String email) {
        String query = "SELECT COUNT(*) FROM user WHERE email = ?";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query)) {
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void updatePassword(String token, String newPassword) {
        // Check if the token is valid first
        if (!isValidToken(token)) {
            System.out.println("Invalid or expired token.");
            return;
        }

        // Hash the new password using PassSecurity
        byte[] salt = ps.generateSalt();
        String hashedPassword = ps.hashPassword(newPassword, salt);

        // Update the password in the database:
        // Store the plaintext password in the 'pwd' column and the hashed version in 'hash'
        String query = "UPDATE user SET pwd = ?, hash = ?, salt = ?, reset_token = NULL, token_expiry = NULL WHERE reset_token = ?";

        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query)) {
            pst.setString(1, newPassword);      // store plaintext password in 'pwd'
            pst.setString(2, hashedPassword);     // store hashed password in 'hash'
            pst.setBytes(3, salt);                // store salt
            pst.setString(4, token);              // match the token
            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Password updated successfully.");
            } else {
                System.out.println("No user found with the provided token.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating password: " + e.getMessage());
        }
    }

    public void updatePasswordd(int userId, String newPassword) {
        // Generate a new salt and hash for the new password
        byte[] newSalt = ps.generateSalt();
        String newHashedPassword = ps.hashPassword(newPassword, newSalt);

        // Update the password (both plaintext and hashed)
        String updateQuery = "UPDATE user SET pwd = ?, hash = ?, salt = ? WHERE user_id = ?";
        try (PreparedStatement updatePst = MyConnection.getInstance().getCnx().prepareStatement(updateQuery)) {
            updatePst.setString(1, newPassword);  // Store plaintext password
            updatePst.setString(2, newHashedPassword);  // Store hashed password
            updatePst.setBytes(3, newSalt);  // Store salt
            updatePst.setInt(4, userId);  // User ID for WHERE clause
            int rowsAffected = updatePst.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Password updated successfully!");
            } else {
                System.out.println("Password update failed.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating password: " + e.getMessage());
        }
    }




    public User getCurrentUser() {
        // Get the ID from UserSession
        User sessionUser = UserSession.getInstance().getUser();
        if (sessionUser == null) {
            throw new RuntimeException("No user logged in");
        }

        int IdUser = sessionUser.getUser_id();

        String query = "SELECT * FROM user WHERE user_id = ?";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query)) {

            pst.setInt(1, IdUser);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    User utilisateur = new User();
                    utilisateur.setUser_id(rs.getInt("user_id"));
                    utilisateur.setNom(rs.getString("nom"));
                    utilisateur.setPrenom(rs.getString("prenom"));
                    utilisateur.setEmail(rs.getString("email"));
                    utilisateur.setPwd(rs.getString("pwd"));
                    utilisateur.setNbrVoyage(rs.getInt("nbrVoyage"));

                    return utilisateur;
                } else {
                    throw new RuntimeException("User not found with ID: " + IdUser);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving user from database", e);
        }
    }



    //////////////////////////////////////////////////////////////////////////:




    @Override
    public void addEntity(User user) {
        try {
            String requete="INSERT INTO user(nom, prenom, email, pwd, nbrVoyage, role) " +
                    "VALUES ('"+user.getNom()+"','"+user.getPrenom()+"','"+user.getEmail()+"','"+user.getPwd()+"','"+user.getNbrVoyage()+"','"+user.getRole()+"')";

            Statement st = MyConnection.getInstance().getCnx().createStatement();

            st.executeUpdate(requete);
            System.out.println("User ajoutée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addEntity2(User user)throws SQLException {
        byte[] salt = ps.generateSalt();
        //hash the password withe salt
        if(emailExists(user.getEmail())){
            System.out.println("User with email"+user.getEmail()+"already exists.");
            return ;// Exit the method if email exists
        }
        try {
            String requete = "INSERT INTO user(nom, prenom, email, pwd, nbrVoyage, role, hash,salt,image_url)" +
                    "VALUES (? , ? , ? , ? , ? , ? , ?, ?, ?)";


            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            String hashedPassword = ps.hashPassword(user.getPwd(), salt);
            pst.setString(1, user.getNom());
            pst.setString(2, user.getPrenom());
            pst.setString(3, user.getEmail());
            pst.setString(4, user.getPwd());
            pst.setInt(5, user.getNbrVoyage());
            pst.setString(6, user.getRole());
            pst.setString(7, hashedPassword);
            pst.setBytes(8, salt);
            pst.setString(9, user.getImage_url());
            pst.executeUpdate();
            System.out.println("sucess!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public User getUserByIdd(int id) throws SQLException {
        String query = "SELECT id, nom, prenom, email, pwd, role, nbrVoyage, image_url FROM User WHERE id = ?";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUser_id(rs.getInt("id"));
                user.setNom(rs.getString("nom"));
                user.setPrenom(rs.getString("prenom"));
                user.setEmail(rs.getString("email"));
                user.setPwd(rs.getString("pwd"));
                user.setRole(rs.getString("role"));
                user.setNbrVoyage(rs.getInt("nbrVoyage"));
                user.setImage_url(rs.getString("image_url"));
                return user;
            }
        }
        return null;
    }

    @Override
    public void updateEntity(int id, User entity) {
        try {
            String requete = "UPDATE user SET nom=?, prenom=?, email=? WHERE user_id=?";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, entity.getNom());
            pst.setString(2, entity.getPrenom());
            pst.setString(3, entity.getEmail());
            pst.setInt(4, id);
            pst.executeUpdate();
            System.out.println("User updated successfully!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteEntity(int userId) {
        try {
            String query = "DELETE FROM user WHERE user_id = ?";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
            pst.setInt(1, userId); // Set the user_id for the WHERE clause
            pst.executeUpdate();
            System.out.println("User deleted successfully!");
        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
    }

    @Override
    public List<User> getAllData() {
        List<User> results = new ArrayList<>();
        String requete = "SELECT * FROM user";
        try {
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()) {
                User u = new User();
                u.setUser_id(rs.getInt("user_id"));
                u.setNom(rs.getString("nom"));
                u.setPrenom(rs.getString("prenom"));
                u.setEmail(rs.getString("email"));
                u.setPwd(rs.getString("pwd"));
                u.setNbrVoyage(rs.getInt("nbrVoyage"));
                u.setRole(rs.getString("role"));
                results.add(u);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return results;
    }

    public static boolean emailExists(String email){
        String query = "SELECT  COUNT(*) FROM user WHERE email = ?";
        try{
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0; //if count>0 email exists
            }
        }catch(SQLException e){
            System.err.println("error checking email existence "+e.getMessage());
        }
        return false;
    }

    public User login(String email, String password) throws SQLException {

        User user = null;

        if (!emailExists(email)) {
            System.out.println("Email doesn't exist");
            return null;
        }

        String query = "SELECT * FROM user WHERE email = ?";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query)) {
            pst.setString(1, email);

            ResultSet resultSet = pst.executeQuery();
            if (resultSet.next()) {
                byte[] salt = resultSet.getBytes("salt");
                String hashedPassword = resultSet.getString("hash");

                if (ps.validatePassword(password, hashedPassword, salt)) {
                    user = new User();
                    user.setUser_id(resultSet.getInt("user_id"));
                    user.setEmail(resultSet.getString("email"));
                    user.setNom(resultSet.getString("nom"));
                    user.setPrenom(resultSet.getString("prenom"));
                    user.setRole(resultSet.getString("role"));

                    user.setImage_url(resultSet.getString("image_url"));
                    return user;
                }
            }
        }
        return null;
    }

}
