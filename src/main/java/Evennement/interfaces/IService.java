package Evennement.interfaces;

import java.util.Date;
import java.util.List;

public interface IService <T> {
    public void add(T t);

    public void update(T entity,int id);

    public void delete(int id);

    public List<T> getAllData();
}

