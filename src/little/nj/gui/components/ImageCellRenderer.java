package little.nj.gui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;

import little.nj.util.ImageUtil;

public class ImageCellRenderer implements ListCellRenderer {
	
	public static final int DEFAULT_BOX = 200;
	
	public static final Border DEFAULT_BORDER = 
			BorderFactory.createEmptyBorder(10, 10, 10, 10);
	
	private final int		box;
	private final Border	border;

	private Map<Object, ImageCell> stamps = new HashMap<Object, ImageCell>();

	public ImageCellRenderer(int box, Border border) {
		this.box = box;
		this.border = border;
	}
	
	public ImageCellRenderer() {
		this(DEFAULT_BOX, DEFAULT_BORDER);
	}
	
	@Override
	public Component getListCellRendererComponent(
			JList list, 
			Object value, 
			int index,
			boolean isSelected, 
			boolean cellHasFocus) {
		
		ImageCell stamp = stamps.get(value);
		
		if (null == stamp) {
			stamp = new ImageCell((Image) value);

			stamps.put(value, stamp);
		}
		
		if (isSelected) {
			stamp.setForeground(list.getSelectionForeground());
			stamp.setBackground(list.getSelectionBackground());
		} else {
			stamp.setForeground(list.getForeground());
			stamp.setBackground(list.getBackground());
		}
		
		if (cellHasFocus) {
			stamp.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		} else {
			stamp.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		}
		
		return stamp;
	}
	
	@SuppressWarnings("serial")
	private class ImageCell extends JPanel {
		
		JLabel cell = new JLabel();
		
		ImageCell(Image input) {
			cell.setIcon(new ImageIcon(ImageUtil.resizeImage(input, box, box)));

			add(cell);
			cell.setOpaque(true);
			cell.setBorder(border);
			setOpaque(true);
		}
		
		@SuppressWarnings("unused")
		void setText(String text) { cell.setText(text); }
	}
}