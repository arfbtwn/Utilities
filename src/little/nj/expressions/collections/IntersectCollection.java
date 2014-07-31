package little.nj.expressions.collections;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class IntersectCollection<T> implements Iterable<T>
{
    final Iterable<T> base, intersect;

    public IntersectCollection(Iterable<T> base, Iterable<T> intersect) {
        this.base = base;
        this.intersect = intersect;
    }

    @Override
    public Iterator<T> iterator()
    {
        return new IntersectIterator<T>(base.iterator(), intersect.iterator());
    }

    private static class IntersectIterator<T> implements Iterator<T> {

        final Iterator<T> base, intersect;
        final Set<T> sect = new HashSet<T>();

        transient T next;

        IntersectIterator (Iterator<T> base, Iterator<T> intersect) {
            this.base = base;
            this.intersect = intersect;
        }

        @Override
        public void remove()
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasNext()
        {
            while (intersect.hasNext()) {
                sect.add(intersect.next());
            }

            while(next == null && base.hasNext()) {
                T that = base.next();

                if (!sect.add(that)) {
                    next = that;
                    break;
                }
            }

            return null != next;
        }

        @Override
        public T next()
        {
            if (!hasNext())
                throw new NoSuchElementException();

            T that = next;
            next = null;
            return that;
        }
    }
}
