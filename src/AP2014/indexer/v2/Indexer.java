package AP2014.indexer.v2;

import java.io.IOException;
import java.io.InputStream;

public class Indexer {

	private final String lineEndings="\r\n";
	private final String EOF="finished.";
	
	public static void main(String[] args){
		new Indexer().Run();
	}
	
	private void Run() {
		
		Index index=new Index();
		InputStream stream;

		//stream=System.in;
		
		stream=new InputStream() {
			private final String m=
					  "This is A Test input can't \n"
					+ "it is this this this only for test!\n" + "''can't' !!.\n"
					+ "finished.\n";
			private int c;
			@Override
			public int read() throws IOException {
				return m.charAt(c++);
			}
		};

		MyScanner scanner=new MyScanner(stream);
		
		int lineNum=1;
		while(true){
			String line=scanner.readString(lineEndings);
			if(!line.endsWith(EOF)){
				index.addLine(line, lineNum);
			}else {
				index.addLine(line.substring(0,line.length()-EOF.length()), lineNum);
				break;
			}
			lineNum++;
		}
		
		System.out.println(index);
		
	}
}
