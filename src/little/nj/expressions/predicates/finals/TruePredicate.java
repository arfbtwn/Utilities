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
package little.nj.expressions.predicates.finals;

import little.nj.expressions.predicates.FluentPredicate;


public final class TruePredicate extends FluentPredicate<Object> {

    /* (non-Javadoc)
     * @see little.nj.expressions.IExpression#evaluate(java.lang.Object)
     */
    @Override
    public boolean evaluate(Object obj) {
        return true;
    }

}
