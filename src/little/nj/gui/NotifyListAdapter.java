package little.nj.gui;

import java.util.Collection;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import little.nj.gui.events.EventSupportImpl;

public class NotifyListAdapter<T> implements List<T> {
	
    @SuppressWarnings("serial")
	public static class ListEvent extends EventObject {

        public static enum Action {
            ADD, CLEAR, REMOVE, SET;
        }

        Object item = null;

        Action   action;

        public ListEvent(Object src, Object item, Action action) {
        	super(src);
            this.item = item;
            this.action = action;
        }

        public Object getItem() {
            return this.item;
        }

        public Action getType() {
            return this.action;
        }
    }

    public static interface ListListener extends EventListener {
        void listChanged(ListEvent paramListEvent);
    }

	private EventSupportImpl<ListListener, ListEvent> support = 
			new EventSupportImpl<ListListener, ListEvent>();
	private List<T> wrap;
	
	public NotifyListAdapter(List<T> list) {
		wrap = list;
	}

	/*
	 * Event Generating functions
	 */
	
	@Override
	public boolean add(T e) {
		if (wrap.add(e)) {
			support.fireEvent(new ListEvent(this, e, ListEvent.Action.ADD));
			return true;
		}
		return false;
	}

	@Override
	public boolean remove(Object o) {
		if (wrap.remove(o)) {
			support.fireEvent(new ListEvent(this, o, ListEvent.Action.REMOVE));
			return true;
		}
		return false;
	}
	
	@Override
	public boolean addAll(Collection<? extends T> c) {
		if (wrap.addAll(c)) {
			support.fireEvent(new ListEvent(this, c, ListEvent.Action.ADD));
			return true;
		}
		return false;
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		if (wrap.addAll(index, c)) {
			support.fireEvent(new ListEvent(this, c, ListEvent.Action.ADD));
			return true;
		}
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		if (wrap.removeAll(c)) {
			support.fireEvent(new ListEvent(this, c, ListEvent.Action.REMOVE));
			return true;
		}
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		if (wrap.retainAll(c)) {
			support.fireEvent(new ListEvent(this, c, ListEvent.Action.ADD));
			return true;
		}
		return false;
	}

	@Override
	public void clear() { 
		wrap.clear(); 
		support.fireEvent(new ListEvent(this, wrap, ListEvent.Action.CLEAR)); 
	}

	@Override
	public T set(int index, T element) {
		T old = wrap.set(index, element);
		support.fireEvent(new ListEvent(this, element, ListEvent.Action.SET));
		return old;
	}

	@Override
	public void add(int index, T element) {
		wrap.add(index, element);
		support.fireEvent(new ListEvent(this, element, ListEvent.Action.ADD));
	}

	@Override
	public T remove(int index) {
		T old = wrap.remove(index);
		support.fireEvent(new ListEvent(this, old, ListEvent.Action.REMOVE));
		return old;
	}
	
	/*
	 * Forwarding functions
	 */
	
	@Override
	public int size() { return wrap.size(); }

	@Override
	public boolean isEmpty() { return wrap.isEmpty(); }

	@Override
	public T get(int index) { return wrap.get(index); }

	@Override
	public boolean contains(Object o) { return wrap.contains(o); }

	@Override
	public boolean containsAll(Collection<?> c) { return wrap.containsAll(c); }

	@Override
	public Iterator<T> iterator() { return wrap.iterator();	}

	@Override
	public Object[] toArray() { return wrap.toArray(); }

	@Override
	public <T2> T2[] toArray(T2[] a) { return wrap.toArray(a); }

	@Override
	public int indexOf(Object o) { return wrap.indexOf(o); }

	@Override
	public int lastIndexOf(Object o) { return wrap.lastIndexOf(o); }

	@Override
	public ListIterator<T> listIterator() { return wrap.listIterator(); }

	@Override
	public ListIterator<T> listIterator(int index) { return wrap.listIterator(index); }

	@Override
	public List<T> subList(int fromIndex, int toIndex) { return wrap.subList(fromIndex, toIndex); }
	
}
