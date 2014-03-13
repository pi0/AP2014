package AP2014.imageencode;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class imageEncode {
    public static void main(String[] args) {

        //Some sample data ....

        Img i = Img.fromData(new InputStream() {
            int r = 0;
            String data = "8 " +
                    "0 0 0 0    1 1 1 1 " +
                    "0 0 0 0    1 1 1 1 " +
                    "0 0 1 1    1 1 1 1 " +
                    "0 0 1 1    1 1 1 1 " +

                    "1 1 1 1    1 1 1 1 " +
                    "1 1 1 1    1 1 1 1 " +
                    "1 1 1 1    1 1 1 1 " +
                    "1 1 1 1    1 1 1 1 ";

            @Override
            public int read() throws IOException {
                return data.charAt(r++);
            }

        });

       // Img i=Img.fromStdin();
        // Img i=Img.fromFile(....);
        System.out.println(i.beautyEncode());

   //     Img boo=Img.fromEncodedData("21010",4);
   //     System.out.println(boo);

    }
}


class Img {

    boolean[][] data;
    int expanded_size;

    public Img(int size) {
        //Expand size to power of 2
        int n_size=2;
        while (n_size<size)
            n_size*=2;

        expanded_size=n_size-size;
        data = new boolean[n_size][n_size];
    }

    public static Img fromData(InputStream s) {
        Img r = new Img(MyReader.safeReadInt(s));

        for (int j = 0; j < r.size() - r.expanded_size; j++)
            for (int i = 0; i < r.size()- r.expanded_size; i++)
                if (MyReader.safeReadInt(s) == 1)
                    r.data[i][j] = true;

        return r;
    }

    public static Img fromStdin() {
        return fromData(System.in);
    }

    public static Img fromFile(String path) throws Exception {
        return fromData(new FileInputStream(path));
    }


    public int size() {
        return data[0].length;
    }

    private int loadFromEncoded(String data,int dataIndex,int size,int startX,int startY) {
        int x = startX, y = startY;
        switch (data.charAt(dataIndex)) {
            case '1':
            case '0':

                boolean value=(data.charAt(dataIndex)=='1');

                for(;x<size;x++)
                    for(;y<size;y++)
                        this.data[x][y]=value;

                return  1;
            case '2' :
                size/=2;
                int[][] direction = {{0, 0}, {+size, 0}, {0, size}, {-1 * size, 0}};
                dataIndex++;
                for (int j = 0; j < 4; j++) {
                    x+=direction[j][0];
                    y+=direction[j][1];
                    dataIndex+=loadFromEncoded(data,dataIndex,size,x,y);
                }
                return 5;
        }

        return 0;
    }

    public static Img fromEncodedData(String data,int size) {
        Img r=new Img(size);

        r.loadFromEncoded(data, 0, size, 0, 0);

        return r;
    }


    private int getSectionEncodingVal(int startX, int startY, int size) {

        boolean true_found = false, false_found = false;

        for (int i = startX; i < startX + size; i++)
            for (int j = startY; j < startY + size; j++) {
                if (data[i][j] == true)
                    true_found = true;
                else
                    false_found = true;

                if (true_found && false_found)
                    return 2;
            }

        return true_found ? 1 : 0;
    }


    @Override
    public String toString() {
        String ans = "";
        for (int r = 0; r < size(); r++) {
            for (int c = 0; c < size(); c++)
                ans += data[c][r] ? "1 " : "0 ";
            ans += "\n";
        }
        return ans;
    }


    private String encode(int startX, int startY, int size) {

        int sectionVal = getSectionEncodingVal(startX, startY, size);
        String encodedData = "" + sectionVal;

        if (sectionVal == 2) {
            size /= 2;
            int[][] direction = {{0, 0}, {+size, 0}, {0, size}, {-1 * size, 0}};
            int x = startX, y = startY;
            for (int i = 0; i < 4; i++) {
                x += direction[i][0];
                y += direction[i][1];
                encodedData += encode(x, y, size);
            }
        }

        return encodedData;
    }

    @SuppressWarnings("unused")
	private String beautyEncode(String str,int startIndex) {
        String ans="";
        if(str.charAt(startIndex)=='2') {
            ans+="{ ";
        }


        return ans;
    }

    public String beautyEncode() {
        String e = encode();



        return e;
    }

    public String encode() {
        return encode(0, 0, size());
    }



}


class MyReader {

    public static String getStringRaw(int maxLen, InputStream inputStream) throws Exception {

        byte[] r = new byte[maxLen];
        int i;
        int read;

        //Skip to first readable char
        do
            read = inputStream.read();
        while (isEmptyChar(read));

        for (i = 0; i < maxLen; i++) {
            if (!isEmptyChar(read)) {
                r[i] = (byte) read;
            } else break;
            read = inputStream.read();
        }
        //and last empty char will skipped too ...

        String s = "";
        for (int j = 0; j < i; j++)
            s += (char) r[j];
        return s;
    }

    public static String getStringRaw(int maxLen) throws Exception {
        return getStringRaw(maxLen, System.in);
    }

    public static String GetStringRaw() throws Exception {
        return getStringRaw(256);
    }

    static boolean isEmptyChar(int c) {
        return (c == ' ' || c == '\n' || c == 10 || c == 13);
        //windows console sends 13,10 but *unix terminal sends only 10 !
    }


    public static char safeRead(InputStream s) {
        try {
            return (char) s.read();
        } catch (Exception e) {
            return (char) -1;
        }
    }

    public static String safeReadString(InputStream s, int max) {
        try {
            return getStringRaw(max, s).toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static int safeReadInt(InputStream s) {
        return Integer.parseInt(safeReadString(s, 10));
    }

}

