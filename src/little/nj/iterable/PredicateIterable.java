/**
 * Copyright (C) 2013
 * Nicholas J. Little <arealityfarbetween@googlemail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package little.nj.iterable;

import java.util.Iterator;
import java.util.NoSuchElementException;

import little.nj.expressions.iterators.SingleReel;
import little.nj.expressions.predicates.Predicate;


public class PredicateIterable<T> implements Iterable<T> {

    private final Iterable<T> backing;
    private final Predicate<? super T> predicate;

    public PredicateIterable(Iterable<T> backing, Predicate<? super T> predicate) {
        this.backing = backing;
        this.predicate = predicate;
    }

    /* (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<T> iterator() {
        return new PredicateIterator(backing.iterator());
    }

    private class PredicateIterator extends SingleReel<T, T> {

        private transient T next;

        public PredicateIterator(Iterator<T> iterator) {
            super(iterator);
        }

        /* (non-Javadoc)
         * @see java.util.Iterator#hasNext()
         */
        @Override
        public boolean hasNext() {
            if (next == null) {
                T poss;
                Iterator<T> it = getIterator();

                while(it.hasNext()) {
                    poss = it.next();

                    if (predicate.evaluate(poss)) {
                        next = poss;
                        break;
                    }
                }
            }

            return next != null;
        }

        /* (non-Javadoc)
         * @see java.util.Iterator#next()
         */
        @Override
        public T next() {
            if (!hasNext())
                throw new NoSuchElementException();

            T that = next;

            next = null;

            return that;
        }
    }

}
