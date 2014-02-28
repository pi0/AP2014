package AP2014.indexer.v2;

public class Node {
	
	private int lineNumber;
	private int repeats;
	
	public Node(int lineNumber,int repeats) {
		this.lineNumber=lineNumber;
		this.repeats=repeats;
	}
	
	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public int getRepeats() {
		return repeats;
	}

	public void setRepeats(int repeats) {
		this.repeats = repeats;
	}
	
	public void addRepeat() {
		setRepeats(getRepeats()+1);
	}
	
	
	@Override
	public String toString() {
		if(getRepeats()>0) {
		String r=""+getLineNumber();
		if(getRepeats()>1)
			r+= "(" + getRepeats()+")";
		return r;
		}else return "";
	}

}
