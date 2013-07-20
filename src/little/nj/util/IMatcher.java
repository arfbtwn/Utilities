package little.nj.util;

public interface IMatcher<T> {
    
    /**
     * Does this T match
     * 
     * @param obj
     * @return
     */
    boolean matches(T obj);
    
    /**
     * Find the first matching T in this array of type T
     * 
     * @param arrObj
     * @return instance of T, or null
     */
    T findFirst(T[] arrObj);
    
    /**
     * Find all matching T's in this array of type T
     * @param arrObj
     * @return array of T
     */
    T[] findAll(T[] arrObj);
}