package com.playtech.util;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Server {
    private static ServerSocket serverSocket;
    static String message;
    static Reader reader;
    static Socket socket;
    static InputStream inputStream;

    private Server() {
    }

    public static ServerSocket getServerSocket() throws IOException {
        return serverSocket == null ? serverSocket = new ServerSocket(4005) : serverSocket;
    }

    public static void runServer() {
        Thread t1 = new Thread(() -> {
            try {
                serverSocket = Server.getServerSocket();
                socket = serverSocket.accept();
                SocketContainer.sockets.add(socket);
                System.out.println("server");
                Thread t2 = new Thread(() -> {
                    while (true) {
                        try {
                            System.out.println("inside");
                            reader = new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8);
                            BufferedReader bufferedReader = new BufferedReader(reader);

                            String receivedMessage;
                            while ((receivedMessage = bufferedReader.readLine()) != null) {
                                message = receivedMessage;
                                System.out.println("type msg "+message);
                                flushMessage(message);
                            }
                            while (inputStream.available() > 0) {
                                inputStream.read();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                t2.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t1.start();
    }
    private static void flushMessage(String message) throws IOException {
        System.out.println("mess: " + message);

        for (Socket socket : SocketContainer.sockets) {
            Writer writer = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);
            PrintWriter printWriter = new PrintWriter(writer);
            printWriter.println(message);
            printWriter.flush();
            System.out.println("Flushed: " + message);
        }
    }

}
