package Projet.interfaces;

import Projet.entities.User;

import java.sql.SQLException;
import java.util.List;

public interface IUserService {
    void addEntity(User user);

    User getUserByIdd(int id) throws SQLException;

    void updateEntity(int id, User entity);

    void deleteEntity(int userId);

    List<User> getAllData();
}
