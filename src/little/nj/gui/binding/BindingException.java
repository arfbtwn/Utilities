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
package little.nj.gui.binding;


/**
 * @author Nicholas Little
 *
 */
@SuppressWarnings("serial")
public class BindingException extends RuntimeException {

    public BindingException() { }

    public BindingException(String message) {
        super(message);
    }

    public BindingException(Throwable cause) {
        super(cause);
    }

    public BindingException(String message, Throwable cause) {
        super(message, cause);
    }

}
