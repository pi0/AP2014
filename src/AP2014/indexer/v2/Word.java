package AP2014.indexer.v2;

public class Word {

	String word;

	public Word(String word) {
		this.word = word;
	}

	public boolean isValid() {
		return word.length() > 0;
	}


	public Word format() {
		Word n = (Word) this.clone();
		n.word = formatWord(n.word);

		return n;
	}
	
	private String formatWord(String word) {
		
		if(word.length()<1)return "";
		
		if (word.indexOf('\'') == 0)
			return formatWord(word.substring(1));
		if (word.lastIndexOf('\'') == word.length() - 1)
			return formatWord(word.substring(0, word.length() - 1));

		return word.trim().toLowerCase();
	}
	
	@Override
	protected Object clone() {
		return new Word(word);
	}

	@Override
	public boolean equals(Object o) {
		if (o.getClass() == Word.class)
			return this.word.equals(((Word) o).word);
		else
			return false;
	}

	@Override
	public String toString() {
		return this.word;
	}
}
