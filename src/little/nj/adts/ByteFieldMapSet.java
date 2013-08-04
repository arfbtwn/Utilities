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
package little.nj.adts;

import java.util.HashMap;


public class ByteFieldMapSet extends ByteFieldSet {

    private final HashMap<String, ByteField> hash_backing;

    public ByteFieldMapSet()
    {
        hash_backing = new HashMap<String, ByteField>();
    }
    
    /* (non-Javadoc)
     * @see little.nj.adts.ByteFieldSet#add(little.nj.adts.ByteField)
     */
    @Override
    public void add(ByteField i) {
        super.add(i);

        if (!hash_backing.containsKey(i.getName()))
            hash_backing.put(i.getName(), i);
    }
    
    public ByteField get(String name) {
        return hash_backing.get(name);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends ByteField> T getAs(String name) {
        T rtn = null;
        
        try {
            rtn = (T) get(name);
        } catch (ClassCastException ex) {
            ex.printStackTrace();
        }
        
        return rtn;
    }
}
