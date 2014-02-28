package AP2014.indexer.v2;

import java.util.Vector;;

public class SortedVector<T extends Comparable<T>> extends Vector<T> implements Sortable<T> {

	private static final long serialVersionUID = 1L;

	public void insertData(T element) {
		int i;
		for(i=0;i<size();i++) {
			if(element.compareTo(get(i))<0) {
				break;
			}
		}
		add(i,element);
	}

	@Override
	public synchronized String toString() {
		String t=new String();
		for(int i=0;i<size();i++) {
			t+=get(i)+"\n";
		}
		return t;
	}

}
