package esprit.tn.interfaces;
import java.util.List;

public interface iServices <T> {
    //CRUD
    void add(T t);
    void edit(T t);
    void remove(T t);
    List<T> getAll() ;
    List<T> getOne();
}
