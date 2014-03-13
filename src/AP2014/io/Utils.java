package AP2014.io;

import java.io.*;
import java.nio.file.Files;

public class Utils {

    public static String readAllFile(File file) throws IOException {
        FileInputStream i=new FileInputStream(file);
        byte[] r=new byte[(int)file.length()];
        i.read(r);
        return new String(r);
    }

    public static void writeAllFile(File file,String data) throws IOException {
        FileOutputStream o=new FileOutputStream(file);
        o.write(data.getBytes("UTF-8"));
        o.close();
    }

    public static String filenameWithoutExtension(String name){
        int index=name.lastIndexOf('.');
        if(index<0)
            index=name.length();
        return name.substring(0,index);
    }

    public static String joinPath(String a,String b) {
        return  a+File.separator+b;
    }
}
