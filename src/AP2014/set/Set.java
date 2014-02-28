package AP2014.set;

import java.util.Vector;

public class Set<T> extends Vector<T>{

	private static final long serialVersionUID = 1L;

	public Set(Set<T> data) {
        this();
        cloneFrom(data);
    }

    public Set() {
        super();
    }

    public synchronized void cloneFrom(Set<T> set) {
        clear();
        for (T item : set)
            add(item);
    }

    public synchronized void unionWith(Set<T> set) {
        for (T item : this)
            add(item);
    }

    @Override
    public synchronized void addElement(T item) {
        add(item);
    }

    @Override
    public synchronized Set<T> clone() {
        return new Set<T>(this);
    }

    @Override
    public synchronized boolean add(T item) {
        if (!containsMember(item))
            return super.add(item);
        else return false;
    }

    public synchronized boolean containsMember(T item) {
        return super.contains(item);
    }

    @Override
    public synchronized void add(int index, T item) {
        add(item);
    }

    @Override
    public synchronized String toString() {
        String r = "";
        for (T item : this)
            r += item + ",";
        return "{ " + r.substring(0, r.length() - 1) + " }";
    }

    public synchronized void intersectWith(Set<T> set) {
        Set<T> n = new Set<T>();
        for (T item : set)
            if (containsMember(item))
                n.add(item);
        this.cloneFrom(n);
    }

    public synchronized void setMinus(Set<T> set) {
        for (T item : set)
            remove(item);
    }

    public synchronized boolean isSubsetOf(Set<T> set) {
        for (T item : this)
            if (!set.containsMember(item))
                return false;
        return true;
    }
}


