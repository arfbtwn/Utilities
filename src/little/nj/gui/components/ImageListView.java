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
package little.nj.gui.components;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.ItemSelectable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import little.nj.gui.events.EventSupportImpl;
import little.nj.util.ImageUtil;

/**
 * A panel to display a list of images, and allow addition, removal and
 * re-ordering of the underlying list
 *
 * @author Nicholas Little
 *
 */
@SuppressWarnings("serial")
public class ImageListView extends JPanel implements ItemSelectable {

    /**
     * A panel to contain a single image
     *
     * @author Nicholas Little
     *
     */
    private class ImagePanel extends JPanel {

        JCheckBox check;

        JButton down, up;

        BufferedImage image;

        JLabel label;

        JLabel text, usertext;

        private ActionListener transpose = new ActionListener() {

            /*
             * (non-Javadoc)
             *
             * @see
             * java.awt.event.ActionListener#actionPerformed(java.awt.event.
             * ActionEvent)
             */
            @Override
            public void actionPerformed(ActionEvent arg0) {
                BufferedImage i = getImage();
                int index1 = images.indexOf(i);
                int index2 = index1;

                if (arg0.getSource() == up)
                    index2 = switchUp(content, ImagePanel.this);
                else
                    index2 = switchDown(content, ImagePanel.this);

                if (index2 != index1) {
                    images.set(index1, images.get(index2));
                    images.set(index2, i);

                    up.setEnabled(index2 > 0);
                    down.setEnabled(index2 < images.size() - 1);

                    ImagePanel moved = panel_map.get(images.get(index1));

                    moved.up.setEnabled(index1 > 0);
                    moved.down.setEnabled(index1 < images.size() - 1);
                }
            }
        };

        private ImagePanel(BufferedImage i) {
            image = i;
            check = new JCheckBox();
            label = new JLabel();
            text = new JLabel();
            usertext = new JLabel();
            up = new JButton(ImageUtil.getImageIcon("images/Up24.gif"));
            up.addActionListener(transpose);
            down = new JButton(ImageUtil.getImageIcon("images/Down24.gif"));
            down.addActionListener(transpose);
            init();
        }

        public BufferedImage getImage() {
            return image;
        }

        private void init() {
            removeAll();
            setMaximumSize(dimension);
            label.setIcon(new ImageIcon(ImageUtil.resizeImage(image,
                    (int) (dimension.getWidth() / 2.0D),
                    (int) dimension.getHeight())));
            text.setBorder(ImageListView.BORDER);
            up.setEnabled(images.indexOf(image) > 0);
            down.setEnabled(images.indexOf(image) < images.size() - 1);
            GroupLayout group = new GroupLayout(this);
            setLayout(group);
            group.setHorizontalGroup(group
                    .createSequentialGroup()
                    .addGroup(
                            group.createParallelGroup().addComponent(up)
                                    .addComponent(down))
                    .addComponent(check)
                    .addComponent(label)
                    .addGroup(
                            group.createParallelGroup(
                                    GroupLayout.Alignment.CENTER)
                                    .addComponent(text).addComponent(usertext)));
            group.setVerticalGroup(group.createSequentialGroup().addGroup(
                    group.createParallelGroup(GroupLayout.Alignment.CENTER)
                            .addGroup(
                                    group.createSequentialGroup()
                                            .addComponent(up)
                                            .addComponent(down))
                            .addComponent(check)
                            .addComponent(label)
                            .addGroup(
                                    group.createSequentialGroup()
                                            .addComponent(text)
                                            .addComponent(usertext))));
            text.setText(image.getWidth() + "x" + image.getHeight());
            setBorder(ImageListView.BORDER);
        }

        public void setText(String s) {
            usertext.setText(s);
        }
    }

    /**
     * The selection mode of the ImageListView
     *
     * @author Nicholas Little
     *
     */
    public static enum Mode {
        MULTI, RADIO;
    }

    public static final int HEIGHT = 300;

    public static final int MARGINS = 10;

    public static final int WIDTH = 400;

    public static final Border BORDER = BorderFactory.createEmptyBorder(
            MARGINS, MARGINS, MARGINS, MARGINS);

    public static final Dimension DIMENSION = new Dimension(WIDTH, HEIGHT);

    static int switchDown(JComponent i, JComponent j) {
        return transpose(i, j, 1);
    }

    static int switchUp(JComponent i, JComponent j) {
        return transpose(i, j, -1);
    }

    static int transpose(JComponent i, JComponent j, int k) {
        List<Component> comps = Arrays.asList(i.getComponents());
        int index = comps.indexOf(j) + k;

        if (index >= comps.size())
            index = comps.size() - 1;
        else if (index < 0)
            index = 0;

        i.add(j, index);
        i.validate();

        return index;
    }

    private JButton add = new JButton(ImageUtil.getImageIcon("images/Add24.gif"));

    private JButton rem = new JButton(ImageUtil.getImageIcon("images/Delete24.gif"));

    private ActionListener button_listener = new ActionListener() {

        /*
         * (non-Javadoc)
         *
         * @see
         * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
         * )
         */
        @Override
        public void actionPerformed(ActionEvent arg0) {
            if (arg0.getSource() == add) {
                JFileChooser jfc = new JFileChooser(
                        System.getProperty("user.dir"));
                jfc.setMultiSelectionEnabled(true);
                if (jfc.showOpenDialog(ImageListView.this) == JFileChooser.APPROVE_OPTION)
                    for (File i : jfc.getSelectedFiles())
                        try {
                            BufferedImage tmp = ImageIO.read(i);
                            images.add(tmp);
                            ImageListView.this.addPanel(tmp);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
            } else if (arg0.getSource() == rem) {
                for (BufferedImage i : selected) {
                    images.remove(i);
                    content.remove(panel_map.remove(i));
                }
                selected.clear();
            }
            content.validate();
        }
    };

    private JPanel content = new JPanel();

    private Dimension dimension = DIMENSION;

    private JPanel footer = new JPanel();

    private JLabel footer_label = new JLabel();

    private JPanel header = new JPanel();

    private List<BufferedImage> images = new LinkedList<BufferedImage>();

    private ItemListener item_listener = new ItemListener() {

        @Override
        public void itemStateChanged(ItemEvent arg0) {
            ImagePanel panel = (ImageListView.ImagePanel) ((Component) arg0
                    .getSource()).getParent();

            int state = arg0.getStateChange();

            if (state == ItemEvent.SELECTED) {
                if (mode == ImageListView.Mode.RADIO && selected.size() > 0)
                    panel_map.get(selected.get(0)).check.setSelected(false);
                selected.add(panel.getImage());
            } else {
                selected.remove(panel.getImage());
            }

            ImageListView.this.fireListeners(new ItemEvent(ImageListView.this,
                    ItemEvent.ITEM_STATE_CHANGED, panel.image, state));
        }
    };

    private JScrollPane list = new JScrollPane(content);

    private EventSupportImpl<ItemListener, ItemEvent> support = 
    		new EventSupportImpl<ItemListener, ItemEvent>();

    private Mode mode = Mode.RADIO;

    private boolean modifiable = false;

    private Map<BufferedImage, ImagePanel> panel_map = new HashMap<BufferedImage, ImagePanel>();

    private List<BufferedImage> selected = new LinkedList<BufferedImage>();

    public ImageListView() {
        init();
    }

    public ImageListView(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
        init();
    }

    private void addPanel(BufferedImage i) {
        if (i != null) {
            ImagePanel tmp = new ImagePanel(i);
            tmp.check.addItemListener(item_listener);
            panel_map.put(i, tmp);
            content.add(tmp);
        }
    }

    /**
     * Allows users of the class to get a reference to the internal list header
     * panel
     *
     * @return Header Panel
     */
    public JPanel getHeader() {
        return header;
    }

    /**
     * Gets an array of selected images
     *
     * @return BufferedImage[]
     */
    public BufferedImage[] getSelectedItems() {
        return selected.toArray(new BufferedImage[0]);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.awt.ItemSelectable#getSelectedObjects()
     */
    @Override
    public Object[] getSelectedObjects() {
        return getSelectedItems();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.awt.ItemSelectable#addItemListener(java.awt.event.ItemListener)
     */
    @Override
    public void addItemListener(ItemListener aListener) {
        support.addListener(aListener);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * java.awt.ItemSelectable#removeItemListener(java.awt.event.ItemListener)
     */
    @Override
    public void removeItemListener(ItemListener aListener) {
        support.removeListener(aListener);
    }

    private void fireListeners(ItemEvent event) {
        support.fireEvent(event);
    }

    private void init() {
        removeAll();
        setLayout(new BorderLayout());

        panel_map.clear();
        add(header, "First");
        add(list, "Center");

        list.setViewportView(content);
        content.removeAll();

        content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));
        for (BufferedImage i : images)
            addPanel(i);
        list.setPreferredSize(dimension);
        list.setMinimumSize(dimension);

        setMode(mode);
        setModifiable(modifiable);

        footer.removeAll();
        footer_label = new JLabel();

        footer.add(add);
        footer.add(rem);

        add.removeActionListener(button_listener);
        rem.removeActionListener(button_listener);

        add.addActionListener(button_listener);
        rem.addActionListener(button_listener);

        footer.add(footer_label);
        add(footer, "Last");

        setMinimumSize(dimension);
        validate();
    }

    /**
     * Allows users to set a list of buffered images
     *
     * @param in
     */
    public void setList(List<BufferedImage> in) {
        images = in;
        selected.clear();
        init();
    }

    /**
     * Set the selection mode.
     *
     * @param m
     *            {@link Mode}
     */
    public void setMode(Mode m) {
        mode = m;
        footer_label.setText("Selection Mode: " + mode);
    }

    /**
     * Whether the list is modifyable.
     *
     * @param m
     */
    public void setModifiable(boolean m) {
        modifiable = m;
        add.setVisible(modifiable);
        rem.setVisible(modifiable);
    }

    /**
     * A convenience method to initialise the selected items. This method will
     * addListener the items if they are not already present.
     *
     * @param sel
     */
    public void setSelectedItems(BufferedImage[] sel) {
        for (BufferedImage i : selected)
            panel_map.get(i).check.setSelected(false);
        selected.clear();
        for (BufferedImage i : sel) {
            if (i != null) {
                if (!images.contains(i)) {
                    images.add(i);
                    addPanel(i);
                }
                panel_map.get(i).check.setSelected(true);
            }
        }
    }

    /**
     * Sets the associated text on a given buffered image
     *
     * @param x
     * @param y
     */
    public void setText(BufferedImage x, String y) {
        ImagePanel pan = panel_map.get(x);
        if (pan != null)
            pan.setText(y);
    }
}
