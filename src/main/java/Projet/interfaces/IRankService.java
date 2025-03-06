package Projet.interfaces;

import Projet.entities.Rank;

import java.util.List;

public interface IRankService<T> {
    void addRank(T t);
    List<Rank> getAllRank();
    void updateRank(T t);
    void deleteRank(int IDRang);

}
