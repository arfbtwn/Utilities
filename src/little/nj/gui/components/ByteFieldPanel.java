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
package little.nj.gui.components;

import java.util.EventListener;

import javax.swing.GroupLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.xml.bind.DatatypeConverter;

import little.nj.adts.ByteField;


@SuppressWarnings("serial")
public class ByteFieldPanel extends JPanel {
    
    private ByteField field;

    private JLabel name, bytes;
    
    private JPanel dummy;
    
    private FieldRenderer renderer;
    
    public ByteFieldPanel(ByteField field) {
        this.field = field;
        
        name = new JLabel();
        bytes = new JLabel();
        dummy = new JPanel();
        dummy.setOpaque(false);
        
        init();
    }
    
    protected void init() {
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(name)
                                .addGap(10)
                                .addComponent(dummy))
                        .addGap(10)
                        .addComponent(bytes)));
        
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                        .addComponent(name)
                        .addGap(10)
                        .addComponent(dummy))
                .addGap(10)
                .addComponent(bytes));
        
        name.setText(field.getName());
        refresh();
    }
    
    protected void refresh() {
        bytes.setText(DatatypeConverter.printHexBinary(field.getBytes()));
        
        if (null != renderer) {
            renderer.render(field);
        }
    }
    
    public void setRenderer(FieldRenderer renderer) {
        this.renderer = renderer;
        
        dummy.removeAll();
        dummy.add(renderer.register(field, listener));
        
        renderer.render(field);
    }
    
    private FieldChangeListener listener = new FieldChangeListener() { 
        @Override
        public void fieldChange(byte[] data) {
            field.setBytes(data);
            bytes.setText(DatatypeConverter.printHexBinary(field.getBytes()));
        }
    };
    
    public static interface FieldRenderer {
        
        JComponent register(ByteField field, FieldChangeListener listener);
        
        void render(ByteField field);
        
    }
    
    public static interface FieldChangeListener extends EventListener {
        void fieldChange(byte[] data);
    }
}
