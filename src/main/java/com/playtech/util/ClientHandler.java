package com.playtech.util;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;

    public ClientHandler(Socket client) throws FileNotFoundException, UnsupportedEncodingException {
        this.client=client;
        in=new BufferedReader(new InputStreamReader(new FileInputStream("input.txt"), "UTF-8"));
        out=new PrintWriter("output.txt", "UTF-8");
    }
    @Override
    public void run() {
            try {
                while (true) {
                    String line;
                    while ((line = in.readLine()) != null) {
                        out.write(line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
