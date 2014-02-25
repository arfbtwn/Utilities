/**
 * Copyright (C) 2014 
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
package little.nj.gui.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class EditableTableModel<T> extends AbstractTableModel
                                   implements List<T> {
    
    private final List<T> data;
    
    public EditableTableModel(List<T> data) {
        this.data = data;
    }
    
    public EditableTableModel() {
        this(new ArrayList<T>());
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return get(rowIndex * getColumnCount() + columnIndex);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        set(rowIndex * getColumnCount() + columnIndex, (T)aValue);
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return get(columnIndex).getClass();
    }

    @Override
    public int size() { return data.size(); }

    @Override
    public boolean isEmpty() { return data.isEmpty(); }

    @Override
    public boolean contains(Object o) { return data.contains(o); }

    @Override
    public Iterator<T> iterator() { return data.iterator(); }

    @Override
    public Object[] toArray() { return data.toArray(); }

    @Override
    public <E> E[] toArray(E[] a) { return data.toArray(a); }

    @Override
    public boolean add(T e) {
        if (data.add(e)) {
            int row = data.size() / getColumnCount(),
                col = data.size() % getColumnCount();
            
            fireTableCellUpdated(row, col);
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        if (data.remove(o)) {
            int row = (data.size() + 1) / getColumnCount(),
                col = (data.size() + 1) % getColumnCount();
            
            fireTableCellUpdated(row, col);
            return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) { 
        return data.containsAll(c); 
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return addAll(0, c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        if (data.addAll(index, c)) {
            fireTableDataChanged();
            return true;
        }
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (data.removeAll(c)) {
            fireTableDataChanged();
            return true;
        }
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (data.retainAll(c)) {
            fireTableDataChanged();
            return true;
        }
        return false;
    }

    @Override
    public void clear() {
        if (data.size() > 0) {
            data.clear();
            fireTableDataChanged();
        }
    }

    @Override
    public T get(int index) { return data.get(index); }

    @Override
    public T set(int index, T element) {
        T old = data.set(index, element);
        
        int row = index / getColumnCount(),
            col = index % getColumnCount();
        
        fireTableCellUpdated(row, col);
        
        return old;
    }

    @Override
    public void add(int index, T element) {
        data.add(index, element);
        
        int firstRow = index / getColumnCount(),
            lastRow  = (int) Math.ceil(data.size() / (double)getColumnCount());
        
        fireTableRowsUpdated(firstRow, lastRow);
    }

    @Override
    public T remove(int index) {
        T old = data.remove(index);
        
        int firstRow = index / getColumnCount(),
            lastRow = (int) Math.ceil(data.size() / (double)getColumnCount());
        
        fireTableRowsUpdated(firstRow, lastRow);
        
        return old;
    }

    @Override
    public int indexOf(Object o) { return data.indexOf(o); }

    @Override
    public int lastIndexOf(Object o) { return data.lastIndexOf(o); }

    @Override
    public ListIterator<T> listIterator() { return data.listIterator(); }

    @Override
    public ListIterator<T> listIterator(int index) {
        return data.listIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return data.subList(fromIndex, toIndex);
    }
}