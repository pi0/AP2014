package AP2014.io;


import java.io.BufferedReader;
import java.io.InputStream;

public class MyScanner {

    public final static String standardEmptyChars=" \n\r\t";
    public final static String standardEOL="\n\r";

    private InputStream stream;

    public MyScanner() {
        this(System.in);
    }

    public MyScanner(InputStream stream) {
        this.stream=stream;
    }

    public String readString(String emptyChars) {
        String str = "";
        int read;

        while(true){
            read = readChar();
            if(!isEmptyChar(read,emptyChars))
                break;
        }

        while(true) {
            str += (char) read;
            read = readChar();
            if(isEmptyChar(read,emptyChars))
                break;
        }

        if (str != null && str.length() > 0)
            return str;
        else
            return readString(emptyChars);// Skip it . TODO : avoid infinity recursive!

    }

    public int readInt () {
        return Integer.parseInt(readString());
    }

    public double readDouble () {
        return Double.parseDouble(readString());
    }

    public String readLine() {
        return readString(standardEOL);
    }


    public String readString() {
        return readString(standardEmptyChars);
    }

    private boolean isEmptyChar(int c) {
        return isEmptyChar(c,standardEmptyChars);
    }

    private boolean isEmptyChar(int c,String emptyChars) {
        return (emptyChars.indexOf(c) != -1);
    }

    private int readChar() {
        int read = -1;
        try {
            read = stream.read();
        } catch (Exception e) {
        }
        return read;
    }
}
