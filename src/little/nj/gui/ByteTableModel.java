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
package little.nj.gui;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

/**
 * A simple table model to use with {@link JTable}s for viewing binary data and
 * easily seeing strings embedded within
 * 
 * @author Nicholas Little
 * 
 */
@SuppressWarnings("serial")
public class ByteTableModel extends AbstractTableModel {

    /**
     * Number of bytes to display per row of information
     */
    public static final int    BYTES_PER_ROW = 0x10;

    /**
     * Column headings, to save processing in {@link #getColumnName(int)}
     */
    public static final char[] COLUMNS       = "0123456789ABCDEF".toCharArray();

    /**
     * The backing data model
     */
    private final byte[]       data;

    /**
     * Construct a table model, from the given byte[]
     * 
     * @param data
     *            Construct the view with this data
     */
    public ByteTableModel(byte[] data) {
        this.data = data;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    @Override
    public int getColumnCount() {
        /*
         * 2 lots of data columns, one offset column
         */
        return BYTES_PER_ROW * 2 + 1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    @Override
    public String getColumnName(int column) {
        if (column == 0)
            return "Offset";
        else if (column > BYTES_PER_ROW)
            column -= BYTES_PER_ROW;
        return Character.toString(COLUMNS[column - 1]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public int getRowCount() {
        return data.length / BYTES_PER_ROW + (data.length % BYTES_PER_ROW == 0 ? 0 : 1);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0)
            return String.format("%X", rowIndex * BYTES_PER_ROW);
        boolean asString = false;
        if (columnIndex > BYTES_PER_ROW) {
            columnIndex -= BYTES_PER_ROW;
            asString = true;
        }
        int idx = rowIndex * BYTES_PER_ROW + columnIndex - 1;
        byte cell = data[idx];
        
        if (asString) {
            if (cell > ' ' && cell < '~')
                return Character.toString((char) cell);
            return ".";
        }
        return String.format("%2X", cell);
    }

    /**
     * The default model is not editable
     * 
     * @see {@link AbstractTableModel#isCellEditable(int, int)}
     * @return false
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}