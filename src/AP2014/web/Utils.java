package AP2014.web;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class Utils {

    public static String readAllPage(URL url) {

        BufferedInputStream in = null;
        ByteOutputStream out = null;

        System.out.println(System.getProperty("http.proxyHost"));

        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            in = new BufferedInputStream(conn.getInputStream());
            out = new ByteOutputStream();
            byte[] data = new byte[1024];
            int count;
            while ((count = in.read(data, 0, 1024)) != -1)
                out.write(data, 0, count);
        } catch (Exception e) {
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (out != null)
                out.close();
        }

        if (out != null)
            return new String(out.getBytes());
        else
            return null;

    }

    public static URL resolveRelativeURL(URL last, String r) {

        String base = urlUp(last.toString());

        for (String s : r.split("/"))
            if (s.equals(".."))
                base = urlUp(base);
            else if (!s.equals("."))
                base = urlJoin(base, s);


        try {
            return new URL(base);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public static String urlUp(String s) {
        int l = s.lastIndexOf('/');
        if (l != -1)
            return s.substring(0, l);
        else
            return s;
    }

    public static String urlJoin(String a, String b) {
        return a + (a.endsWith("/") ? "" : "/") + b;
    }


}
