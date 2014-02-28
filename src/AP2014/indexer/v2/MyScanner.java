package AP2014.indexer.v2;

import java.io.InputStream;

public class MyScanner {
	
	private final String standardEmptyChars="\n\r\t !@#$%^&*()-=_+[]{}\";:'\\,<.>?~`";
	private InputStream stream;
	

	public MyScanner(InputStream stream) {
		this.stream=stream;
	}

	public MyScanner() {
		this(System.in);
	}
	
	public String readString(String emptyChars ) {
		String str = "";
		int read;

		while(true){
			read = ReadChar();
			if(!isEmptyChar(read,emptyChars))
				break;
		}

		while(true) {
			str += (char) read;
			read = ReadChar();
			if(isEmptyChar(read,emptyChars))
				break;
		}

		if (str != null && str.length() > 0)
			return str;
		else
			return ReadString();// Skip it
	}
	
	public String ReadString() {
		return readString(standardEmptyChars);
	}
	
	private boolean isEmptyChar(int c,String emptyChars) {
		 return (emptyChars.indexOf(c) != -1);
	}
	
	@SuppressWarnings("unused")
	private boolean isEmptyChar(int c) {
		 return isEmptyChar(c,standardEmptyChars);
	}
	
	
	private int ReadChar() {
		int read = -1;
		try {
			read = stream.read();
		} catch (Exception e) {
		}
		return read;
	}
}
