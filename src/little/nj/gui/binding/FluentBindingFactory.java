/**
 * Copyright (C) 2013 Nicholas J. Little <arealityfarbetween@googlemail.com>
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package little.nj.gui.binding;


public interface FluentBindingFactory {

    /**
     * Create a reconfigurable binding
     *  
     * @param btm Source object
     * @param btm_sample Class of source field
     * @param btm_method Name of source field
     * @param top Target object
     * @param top_sample Class of target field 
     * @param top_method Name of target field
     * @return 
     */
    <X, Y> FluentBinding<X, Y> bind(
            Object btm, Class<X> btm_sample, String btm_method, 
            Object top, Class<Y> top_sample, String top_method
            );

}