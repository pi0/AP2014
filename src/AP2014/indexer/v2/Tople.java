package AP2014.indexer.v2;

import java.util.Vector;

public class Tople  implements Comparable<Tople>{
	
	private String word;
	private Vector<Node> nodes;
	
	public Tople(String word) {
		this.word=word;
		nodes=new Vector<Node>();
	}
	
	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	
	public int size() {
		return nodes.size();
	}

	private Node get(int lineNum) {
		for(int i=0;i<nodes.size();i++)
			if(nodes.get(i).getLineNumber()==lineNum)
				return nodes.get(i);
		return null;
	}
	
	public void add(int i) {
		Node n=get(i);
		if(n==null) 
			nodes.add(new Node(i,1));
		else
			n.addRepeat();
	}

	@Override
	public String toString() {
		String tmp=String.format("%-20s", getWord());
		
		for(int i=0;i<nodes.size();i++)
			tmp+=nodes.get(i).toString()+",";
		return tmp.substring(0,tmp.length()-1);
	}
	
	
	public int compareTo(Tople o) {
		return word.compareTo(o.word);
	}

	@Override
	public boolean equals(Object arg0) {
		return word.equals(((Word)arg0).word);
	}
	
	

}
