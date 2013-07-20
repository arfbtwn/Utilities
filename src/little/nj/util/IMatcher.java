package little.nj.util;

public interface IMatcher<T> {
    boolean matches(T obj);
    T find(T[] arrObj);
}