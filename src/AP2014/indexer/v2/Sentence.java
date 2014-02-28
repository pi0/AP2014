package AP2014.indexer.v2;

import java.util.Vector;

public class Sentence {
	
	private Vector<Word> words;
	
	public Sentence() {
		words=new Vector<Word>();
	}
	
	public Sentence(String sentence) {
		this();
		append(sentence);
	}
	
	public int size() {
		return words.size();
	}
	
	public Word get(int i) {
		return words.get(i);
	}
	
	public void append(String sentence) {

		//Split input 
		String[] split=sentence.split("[^a-zA-Z\']+");
		
		//Add valid items to words
		for(String s : split) {
			Word w=new Word(s).format();
			
			if(w.isValid())
				words.add(w);
		}
		
	}

	@Override
	public String toString() {
		String s="";
		for(int i=0;i<size();i++)
			s+=get(i).toString()+" ";
		return s.substring(0,s.length()-1);
	}
}