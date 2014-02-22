package little.nj.gui.components;


import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import little.nj.util.ImageUtil;
import little.nj.util.StringUtil;

@SuppressWarnings("serial")
public class ListPanel extends JPanel {
    
    private final DefaultListModel model;
    private final JList	list;
    private final JScrollPane scroller;
    
    private AddAction	 addAction;	
    private RemoveAction removeAction;
    private UpAction 	 upAction;
    private DownAction	 downAction;
    
    private ControlPanel controls;
    
    public ListPanel(DefaultListModel model) {
    	super(new BorderLayout());
    	
    	this.model = model;
    	this.list = new JList(model);
    	this.scroller = new JScrollPane(list);
    	
    	addAction = new AddAction();
    	removeAction = new RemoveAction();
    	upAction = new UpAction();
    	downAction = new DownAction();
    	
    	controls = new ControlPanel();
    	
    	init();
    }
    
    public ListPanel() {
    	this(new DefaultListModel());
    }
    
    private void init() {
    	list.addListSelectionListener(listListener);

    	add(scroller, BorderLayout.CENTER);
    	add(controls, BorderLayout.PAGE_END);
    }
    
    public JList asJList() { return list; }
    
    private class ControlPanel extends JPanel {
		JButton add = new JButton(addAction),
				remove = new JButton(removeAction),
				up = new JButton(upAction),
				down = new JButton(downAction);
		
		ControlPanel() {
			super();
			
			add.setText(StringUtil.EMPTY_STRING);
			remove.setText(StringUtil.EMPTY_STRING);
			up.setText(StringUtil.EMPTY_STRING);
			down.setText(StringUtil.EMPTY_STRING);
			
			add(add);
			add(remove);
			add(up);
			add(down);
		}
	}

    private ListSelectionListener listListener = new ListSelectionListener() {
		
		@Override
		public void valueChanged(ListSelectionEvent e) {			
			if (e.getValueIsAdjusting())
				return;
			
			System.out.println("listListener.valueChanged");
			
			boolean add = addAction.testEnabled(),
					rem = removeAction.testEnabled(),
					up  = upAction.testEnabled(),
					dn  = downAction.testEnabled();
			
			addAction.setEnabled(add);
			removeAction.setEnabled(rem);
			upAction.setEnabled(up);
			downAction.setEnabled(dn);
		}
	};
	
	private class AddAction extends AbstractAction {
		AddAction() {
			super("+", ImageUtil.getImageIcon("images/Add24.gif"));
		}
		
		public boolean testEnabled() { return true; }
		
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("AddAction.actionPerformed");
			/*
			 * Delegate to a user provided object?
			 */
			if (true)
				return;
			
			// TODO
			@SuppressWarnings("unused")
			Object value = null; // User Function
			model.addElement(value);			
		}
	}
    
	private class RemoveAction extends AbstractAction {

		RemoveAction() {
			super("-", ImageUtil.getImageIcon("images/Delete24.gif"));
			setEnabled(testEnabled());
		}
		
		public boolean testEnabled() {
			return !list.isSelectionEmpty();
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("RemoveAction.actionPerformed");
			for(Object i : list.getSelectedValues())
				model.removeElement(i);
		}
		
	}
	
	private class UpAction extends AbstractAction {
		UpAction() {
			super("/\\", ImageUtil.getImageIcon("images/Up24.gif"));
			setEnabled(testEnabled());
		}
		
		public boolean testEnabled() {
			return list.getSelectedIndices().length == 1 &&
					list.getSelectedIndex() > 0;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("UpAction.actionPerformed");
			int idx = list.getSelectedIndex();
			Object safe = model.get(idx - 1);
			model.set(idx - 1, list.getSelectedValue());
			model.set(idx, safe);
		}
	}
	
	private class DownAction extends AbstractAction {
		DownAction() {
			super("\\/", ImageUtil.getImageIcon("images/Down24.gif"));
			setEnabled(testEnabled());
		}
		
		public boolean testEnabled() {
			return list.getSelectedIndices().length == 1 &&
					list.getSelectedIndex() < model.size() - 1;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("DownAction.actionPerformed");
			int idx = list.getSelectedIndex();
			Object safe = model.get(idx + 1);
			model.set(idx + 1, list.getSelectedValue());
			model.set(idx, safe);
		}
	}
}
