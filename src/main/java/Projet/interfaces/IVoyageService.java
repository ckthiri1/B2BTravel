package Projet.interfaces;

import Projet.entities.Voyage;

import java.util.List;

public interface IVoyageService<T> {
    void addVoyage(T t);
    List<Voyage> getAllVoyages();
    void updateVoyage(T t);
    void deleteVoyage(int vid);
}
