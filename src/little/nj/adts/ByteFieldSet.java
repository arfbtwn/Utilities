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
package little.nj.adts;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.TreeSet;

public class ByteFieldSet implements Cloneable,Iterable<ByteField> {

    private TreeSet<ByteField>         backing;

    private int                        size_actual;

    private int                        size_possible;

    public ByteFieldSet() {
        backing = new TreeSet<ByteField>();
    }

    /**
     * Adds a ByteField to the set, set's its offset
     * 
     * @param i
     */
    public void add(ByteField i) {
        i.setOffset(size_possible);
        size_possible += i.getLength();

        _add(i);
    }
    
    private void _add(ByteField i) {
        backing.add(i);
    }
    
    @Override
    public ByteFieldSet clone() {
        try {
            ByteFieldSet that = (ByteFieldSet) super.clone();
            
            that.backing = new TreeSet<>();
            
            for(ByteField x : backing)
                that._add(x.clone());
            
            return that;
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
            // FIXME: Obviously this is bad form, but we should not get here
            throw new RuntimeException(ex);
        }
    }

    public ByteField get(int offset) {
        ByteField bf = new ByteField(offset);
        return backing.ceiling(bf);
    }

    public ByteBuffer getBuffer() {
        ByteBuffer rtn = ByteBuffer.allocate(length());
        for (ByteField i : backing) {
            if (i.getLength() > rtn.remaining())
                break;
            rtn.put(i.getBytes());
        }
        rtn.rewind();
        return rtn;
    }

    public byte[] getBytes() {
        return getBuffer().array();
    }

    public int length() {
        return size_actual == 0 ? size_possible : size_actual;
    }

    public void parseAll(ByteBuffer in) {
        for (ByteField i : backing) {
            if (!in.hasRemaining())
                break;
            i.parse(in);
            size_actual += i.getLength();
        }
    }

    public void parseBetween(ByteBuffer in, int start, int end) {
        size_actual = end > size_actual ? end : size_actual;
        for (ByteField i : backing)
            if (i.getOffset() >= start && i.getOffset() < end)
                i.parse(in);
            else if (in.position() > end)
                break;
        if (in.position() < end) {
            ByteField ubf = new ByteField(end - in.position(),
                    "Unknown Block");
            add(ubf);
            ubf.parse(in);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<ByteField> it = backing.iterator();
        while (it.hasNext())
            sb.append(it.next() + "\n");
        return sb.toString();
    }

    public int write(ByteBuffer out) {
        for (ByteField i : backing) {
            if (size_actual > 0 && i.getOffset() > size_actual)
                break;
            out.put(i.getBuffer());
        }
        return out.position();
    }

    /* (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<ByteField> iterator() {
        return backing.iterator();
    }
}
