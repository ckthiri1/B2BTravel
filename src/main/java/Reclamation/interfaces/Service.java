package Reclamation.interfaces;

import Reclamation.entities.Reclamation;

import java.util.List;

public interface Service<T> {
    int addEntity(T t);
    boolean deleteEntity(T t);



    boolean updateEntity(int id, T t);

    default List<Reclamation> getAllData() {
        return null;
    }
}

