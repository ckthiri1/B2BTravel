package CRUD.interfaces;

import java.util.List;

public interface IService <T> {
    public void addEntity(T t);
    public void addEntity2(T t);
    public void updateEntity(int id,T t);
    public boolean deleteEntity(T t);
    public List<T> getAllData();
}
