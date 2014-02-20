package little.nj.algorithms;

/**
 * A class to create a set of permutations recursively based on an
 * arbitrary array of symbols
 *
 * @param <T>
 */
public class PermutationGenerator<T>
{
    /**
     * An interface for users of the generator to influence
     * its operation
     *
     * @param <T>
     */
    interface Callback<T>
    {
        /**
         * Return true when the algorithm finds the permutation you're
         * interested in
         *
         * @param permutation
         * @return
         */
        boolean found(T[] permutation);

        /**
         * Return true to allow the algorithm to skip a branch of the
         * search
         *
         * @param branch
         * @return
         */
        boolean prune(T[] branch);
    }

    final Callback<T> cb;
    final T[] sym;
    boolean stop;

    PermutationGenerator(Callback<T> callback, T... symbols)
    {
        if (symbols.length == 0)
            throw new IllegalArgumentException("No Symbols");

        cb = callback;
        sym = symbols;
    }

    /**
     * Begins the algorithm with an array of the specified size
     *
     * @param size
     */
    void start(T[] size)
    {
        stop = false;
        recurse(size, size.length - 1);
    }

    private void recurse(T[] word, int at)
    {
        if (at < 0)
        {
            stop = cb.found(word);
            return;
        }

        for(T i : sym)
        {
            if (stop)
                return;

            word[at] = i;

            if (!cb.prune(word))
            {
                recurse(word.clone(), at - 1);
            }
        }
    }
}
