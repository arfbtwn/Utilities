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

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.xml.bind.DatatypeConverter;

import little.nj.adts.ByteField;
import little.nj.gui.components.ByteFieldPanel.FieldChangeListener;

@SuppressWarnings("serial")
public class FieldPanelEditor extends AbstractCellEditor
                              implements TableCellEditor, TableCellRenderer {

    private static final ByteFieldPanel panel = new ByteFieldPanel();
    private transient ByteField value;

    @Override
    public Object getCellEditorValue() { return value; }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        
        this.value = (ByteField)value;
        
        return FieldPanelFactory.create((ByteField)value);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        ByteFieldPanel cell = panel;
        
        cell.setField((ByteField)value);
        cell.setRenderer(new ByteFieldPanel.FieldRenderer() {
            
            JLabel label = new JLabel();
            
            @Override
            public void render(ByteField field) {
                String s = DatatypeConverter.printHexBinary(field.getBytes());
                label.setText(s.length() > 10 ? s.substring(0, 7) + "..." : s);
            }
            
            @Override
            public JComponent register(ByteField field, FieldChangeListener listener) {
                label.setHorizontalAlignment(JLabel.TRAILING);
                return label;
            }
        });
        
        if (hasFocus) {
            cell.setBorder(BorderFactory.createDashedBorder(null, 2f, 5f, 1f, true));
        } else {
            cell.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        }
            
        if (isSelected) {
            cell.setForeground(table.getSelectionForeground());
            cell.setBackground(table.getSelectionBackground());
        } else {
            cell.setForeground(table.getForeground());
            cell.setBackground(table.getBackground());
        }
        
        return cell;
    }
    
    

}
