package com.playtech.util;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client {
    static Socket socket;
    static Reader reader;
    static String message;
    static String reply;

    public Client() throws IOException {
        System.out.println("initialize");
        socket = new Socket("localhost", 4005);
    }

    public static String msgGetFromServer(String ans) {
        Thread t2 = new Thread(() -> {
            try {
                reader = new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(reader);

                if (bufferedReader.ready()) {
                    System.out.println("  ran");
                    reply = bufferedReader.readLine();
                    System.out.println("rep: " + reply);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t2.start();
        return reply == null ? "" : reply;
    }

    public static void msgSendToServer(String msg) {
        message = msg;
        System.out.println("Cli: " + message);
        try {
            Writer writer = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);
            PrintWriter printWriter = new PrintWriter(writer);
            printWriter.println(message);
            printWriter.flush();
            System.out.println("flush " + message);
            message = "";
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
