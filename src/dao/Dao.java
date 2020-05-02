package dao;

import java.util.ArrayList;
import java.util.Optional;

public interface Dao<T> {
	
	//String path = "C:\\Users\\Gabriel Boscoli\\Documents\\INF1013\\db.json";
	
	Optional<T> get(long id);
    
    ArrayList<T> getAll();
     
    void save(T t);
     
//    void update(T t, String[] params);
    void update();
     
    void delete(T t);
}
