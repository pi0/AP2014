package AP2014.indexer.v2;

public class Index {	
	
	private SortedVector<Tople> toples;
	
	public Index() {
		toples=new SortedVector<Tople>();
	}
	
	public Index(String[] lines) {
		this();
		for(int i=0;i<lines.length;i++)
			addLine(lines[i],i+1);
	}
	
	public int size() {
		return toples.size();
	}
	
	public Tople get(int i){
		return toples.get(i);
	}
	
	public Tople get(String word){
		for(int i=0;i<size();i++){
			if(get(i).getWord().equals(word))
				return get(i);
		}
		Tople t=new Tople(word);
		toples.insertData(t);
		return t;
	}
	
	public void addLine(String text,int lineNum){
		Sentence s=new Sentence(text);
		for(int i=0;i<s.size();i++)
			get(s.get(i).toString()).add(lineNum);
	}

	@Override
	public String toString() {
		String s="";
		for(int i=0;i<size();i++)
			s+=get(i)+"\n";
		return s;
	}


}
