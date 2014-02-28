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

import java.awt.BorderLayout;
import java.util.EventListener;

import javax.swing.GroupLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.xml.bind.DatatypeConverter;

import little.nj.adts.ByteField;


@SuppressWarnings("serial")
public class ByteFieldPanel extends JPanel {
    
    private ByteField field;

    private JLabel name, bytes;
    
    private JPanel dummy;
    
    private FieldRenderer renderer;
    
    public ByteFieldPanel() {
        name = new JLabel();
        bytes = new JLabel();
        dummy = new JPanel();
        
        init();
    }
    
    public ByteFieldPanel(ByteField field) {
        this();
        setField(field);
        setRenderer(read_only);
    }
    
    protected void init() {
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        
        dummy.setOpaque(false);
        dummy.setLayout(new BorderLayout());
        
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);
        
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                        .addComponent(name)
                        .addComponent(bytes))
                .addComponent(dummy));
        
        layout.setVerticalGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                        .addComponent(name)
                        .addComponent(bytes))
                .addComponent(dummy));
    }
    
    public void setField(ByteField field) { 
        this.field = field;
        
        register();
        refresh();
    }
    
    public void setRenderer(FieldRenderer renderer) {
        this.renderer = renderer;
        
        register();
        refresh();
    }
    
    protected void register() {
        if (null == renderer || null == field)
            return;
        
        dummy.removeAll();
        dummy.add(renderer.register(field, listener));
    }
    
    protected void refresh() {
        if (null == field)
            return;
        
        name.setText(field.getName());
        setText(field.getBytes());
        
        if (null == renderer)
            return;
        
        renderer.render(field);
    }
    
    private void setText(byte[] data) {
        String bs = DatatypeConverter.printHexBinary(data);
        
        bytes.setText(bs.length() > 10 ? bs.substring(0, 7) + "..." : bs);
    }
    
    private FieldRenderer read_only = new FieldRenderer() {

        final JLabel label = new JLabel();
        
        @Override
        public JComponent register(ByteField field, FieldChangeListener listener) {
            label.setHorizontalAlignment(SwingConstants.TRAILING);
            return label;
        }

        @Override
        public void render(ByteField field) {
            label.setText(DatatypeConverter.printHexBinary(field.getBytes()));
        }
        
    };
    
    private FieldChangeListener listener = new FieldChangeListener() { 
        @Override
        public void fieldChange(byte[] data) {
            field.setBytes(data);
            setText(field.getBytes());
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
