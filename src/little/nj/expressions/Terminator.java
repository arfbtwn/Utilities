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
package little.nj.expressions;


public class Terminator<T> extends ExpressionEngineImpl<T> {

    public Terminator(Iterable<T> backing) {
        super(backing);
    }
    
    /**
     * An optimisation alternative to creating a new engine for 
     * each invocation returning the same type of engine
     */
    @Override
    protected ExpressionEngine<T> getEngine(Iterable<T> iterable) {
        backing = iterable;
        return this;
    }
}
