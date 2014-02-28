package AP2014.indexer.v1;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Vector;

public class IndexerMain {

	public static void main(String[] args) {

		// Uncomment to use sample data
		new IndexerTestApp().runSampleData();

		// Uncomment to use stdin
		// new IndexerTestApp().runStdin();

	}
}

class IndexerTestApp {

	private static String input = "This is A Test input can't \n"
			+ "it is this this this only for test!\n" + "''can't' !!.\n"
			+ "finished.";

	public void runStdin() {
		run(System.in);
	}

	public void runSampleData() {
		// Create an InputStream from sample data
		System.out.println("Input:\n==========\n" + input + "\n\n");
		InputStream s = new InputStream() {
			int counter = 0;

			@Override
			public int read() throws IOException {
				return IndexerTestApp.input.charAt(counter++);
			}
		};
		run(s);
	}

	private void run(InputStream s) {
		Indexer i = new Indexer(s);
		i.Tokenize();

		System.out.println("Result:\n==========\n" + i.toString());
	}
}

class Indexer {

	private static String EOF = "finished";
	private Vector<IndexerItem> items;
	private InputStream stream;
	private int lineCounter;
	private int lastReadChar;

	public Indexer(InputStream stream) {
		this.stream = stream;
		items = new Vector<IndexerItem>();
		lineCounter = 0;
	}

	private IndexerItem getItem(String text) {
		text = text.toLowerCase();

		for (int i = 0; i < items.size(); i++)
			if (items.elementAt(i).getText().equals(text))
				return items.elementAt(i);

		IndexerItem n = new IndexerItem(text);
		items.add(n);
		return n;
	}

	public void Tokenize() {
		String str;
		while (true) {
			str = safeReadString();
			if (str.equals(EOF) && lastReadChar == '.')
				break;
			getItem(str).addRepeat(lineCounter);
		}

		// Sort list
		Collections.sort(items);
	}

	private String safeReadString() {
		String str = "";
		int read;

		do
			read = safeReadKey();
		while (isEmptyChar(read));

		do {
			str += (char) read;
			read = safeReadKey();
		} while (!isEmptyChar(read));

		str = filterToken(str);
		if (str != null && str.length() > 0)
			return str;
		else
			return safeReadString();// Skip it
	}

	private String filterToken(String text) {

		// Check for \' char in different positions
		if (text.indexOf('\'') == 0)
			return filterToken(text.substring(1));
		if (text.lastIndexOf('\'') == text.length() - 1)
			return filterToken(text.substring(0, text.length() - 1));

		return text;
	}

	private boolean isEmptyChar(int c) {
		return !((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c == '\''));
		// String emptyChars = "\n\r\t !@#$%^&*()-=_+[]{}\";:'\\,<.>?~`";
		// return (emptyChars.indexOf(c) != -1);
	}

	private int safeReadKey() {
		int read = -1;
		try {
			read = stream.read();
		} catch (Exception e) {
		}

		if (read == '\n') {
			lineCounter++;
		}

		lastReadChar = read;
		return read;
	}

	@Override
	public String toString() {
		String str = "";

		for (int i = 0; i < items.size(); i++)
			str += items.elementAt(i).toString() + "\n";

		return str;
	}
}

class IndexerItem implements Comparable<IndexerItem> {

	private String text;
	private Vector<IndexerItemRepeat> repeats;

	public IndexerItem(String text) {
		this.text = text.toLowerCase();
		repeats = new Vector<IndexerItemRepeat>();
	}

	public IndexerItemRepeat getLine(int lineNumber) {

		for (int i = 0; i < repeats.size(); i++)
			if (repeats.elementAt(i).lineNumber == lineNumber)
				return repeats.elementAt(i);

		IndexerItemRepeat n = new IndexerItemRepeat();
		n.lineNumber = lineNumber;
		repeats.add(n);
		return n;
	}

	public void addRepeat(int lineNumer) {
		getLine(lineNumer).count++;
	}

	public String getText() {
		return this.text;
	}

	public int compareTo(IndexerItem to) {
		return (getText().compareTo(to.getText()));
	}

	@Override
	public String toString() {
		String str = String.format("%-20s", getText());
		if (str.length() > 20) {
			str = str.substring(0, 20);
		}

		for (int i = 0; i < repeats.size(); i++)
			str += repeats.elementAt(i).toString() + ",";

		return str.substring(0, str.length() - 1);
	}

	
}

class IndexerItemRepeat {
	public int lineNumber;
	public int count;

	@Override
	public String toString() {
		if (count > 1)
			return (lineNumber + 1) + "(" + count + ")";
		else if (count == 1)
			return (lineNumber + 1) + "";

		return null;
	}
}
