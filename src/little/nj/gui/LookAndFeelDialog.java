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

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

@SuppressWarnings("serial")
public class LookAndFeelDialog extends JDialog {

    public static final Map<String, LookAndFeelInfo> LOOK_AND_FEELS;
    
    static {
        LOOK_AND_FEELS = new HashMap<String, LookAndFeelInfo>();
        for (LookAndFeelInfo i : UIManager.getInstalledLookAndFeels())
            LOOK_AND_FEELS.put(i.getName(), i);
    }

    /**
     * A blocking method to produce the Look and Feel dialog and allow the user
     * to change it before the application starts for real
     */
    public static void showDialog() {
        LookAndFeelDialog d = new LookAndFeelDialog();
        
        d.setVisible(true);
        while (d.isShowing())
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }
    
    /**
     * Changes the UIManager's style, swallows all exceptions
     * 
     * @param key
     */
    public static void setStyle(String key) {
     
        if (!LOOK_AND_FEELS.containsKey(key))
            throw new IllegalArgumentException(
                    String.format("Invalid Key: '%s'\nPossibles: '%s'",
                            key, LOOK_AND_FEELS.keySet().toArray())
                            );        
        
        try {
            UIManager.setLookAndFeel(
                    LOOK_AND_FEELS.get(key).getClassName()
                    );
            
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } catch (InstantiationException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        } catch (UnsupportedLookAndFeelException e1) {
            e1.printStackTrace();
        }
        
    }

    private JButton           close;

    private JLabel            label;

    private JComboBox 		  list;

    private JPanel            panel;

    private LookAndFeelDialog() {
        super();
        init();
    }
    
    public LookAndFeelDialog(Window owner, ModalityType modality) {
        super(owner, modality);
        init();
    }

    protected void init() {
        panel = new JPanel();
        label = new JLabel("Select Look 'n' Feel");
        list = new JComboBox(LOOK_AND_FEELS.keySet().toArray(new String[0]));
        close = new JButton("Close");
        
        setContentPane(panel);
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        setTitle(label.getText());
        layout.setHorizontalGroup(layout
                .createSequentialGroup()
                .addContainerGap()
                .addGroup(
                        layout.createParallelGroup()
                                .addGroup(
                                        layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(label).addGap(10)
                                                .addComponent(list)
                                                .addContainerGap())
                                .addComponent(close)).addContainerGap());
        layout.setVerticalGroup(layout
                .createSequentialGroup()
                .addContainerGap()
                .addGroup(
                        layout.createParallelGroup().addComponent(label)
                                .addGap(10).addComponent(list))
                .addComponent(close).addContainerGap());
        list.setSelectedItem(UIManager.getLookAndFeel().getName());
        
        list.addItemListener(DEFAULT_ITEM_LISTENER);
        close.addActionListener(DEFAULT_CLOSE_LISTENER);
        
        pack();
    }
    
    public void addStyleChangeListener(ItemListener aListener) {
        list.addItemListener(aListener);
    }
    
    public void removeStyleChangeListener(ItemListener aListener) {
        list.removeItemListener(aListener);
    }
    
    protected final ActionListener DEFAULT_CLOSE_LISTENER = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            LookAndFeelDialog.this.dispatchEvent(
                    new WindowEvent(
                            LookAndFeelDialog.this, WindowEvent.WINDOW_CLOSING
                            )
                    );
        }
        
    };
    
    protected final ItemListener DEFAULT_ITEM_LISTENER = new ItemListener() {

        @Override
        public void itemStateChanged(ItemEvent e) {
            
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String key = (String) e.getItem();
                
                setStyle(key);

                SwingUtilities.updateComponentTreeUI(LookAndFeelDialog.this);
            }
        }
    };
}
