package AP2014.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

public class WebProxy {

    ServerSocket server;
    int port;

    public WebProxy(int port) {
        this.port = port;
        System.out.println("Starting proxy...");
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Proxy started !");

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (server.isBound()) {
                    System.out.println("Accepting connection ..");
                    try {
                        final Socket s = server.accept();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    handleConnection(s);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    public static void main(String[] args) {
        new WebProxy(2030);


    }

    private void handleConnection(Socket s) throws Exception {

        System.out.println("New connection from " + s.getRemoteSocketAddress().toString());

        //Read request
        BufferedReader r = new BufferedReader
                (new InputStreamReader(s.getInputStream()));
        String request = r.readLine();

        //Do it
        String response = "";
        long start = System.currentTimeMillis();
        try {
            URL url = null;
            String[] sp = request.split(" ");
            url = new URL(sp[1]);
            System.out.println("Downloading :" + url);
            response = Utils.readAllPage(url);
        } catch (Exception e) {
            response = "<h1>Error!</h1><br>" + e.toString();
        }

        long now = System.currentTimeMillis();
        response += "<div style='background-color:yellow;'>Powered by MyProxy / page served in <b>" + (now - start) + "</b> Milliseconds<div>";

        //Send response
        PrintWriter w = new PrintWriter(s.getOutputStream());
        w.write("HTTP/1.0 200 OK\r\n");
        w.write("\r\n");
        w.write(response);
        w.flush();

        //Close the connection
        s.close();
    }


}
