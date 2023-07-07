package com.playtech.util;

import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Server {
    private static ServerSocket serverSocket;
    static String message;
    static Reader reader;
    static Socket socket;
    static String type = "";

    private Server() {
    }

    public static ServerSocket getServerSocket() throws IOException {
        return serverSocket == null ? serverSocket = new ServerSocket(4005) : serverSocket;
    }

    public static void runServer() throws InterruptedException, IOException {
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
                                if (type.equals("msg")) {
                                    System.out.println("type msg");
                                    flushMessage(message);
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                t2.start();
                Thread t3 = new Thread(() -> {
                    while (true) {
                        try {
                            BufferedImage image = ImageIO.read(socket.getInputStream());
                            String[] writerFileSuffixes = ImageIO.getWriterFileSuffixes();
                            String desiredImageType = "";
                            for (String suffix : writerFileSuffixes) {
                                desiredImageType = suffix;
                            }
                            if (type.equals("img")) {
                                System.out.println("type img");
                                flushImage(image,desiredImageType);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                t3.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t1.start();
    }

    private static void flushImage(BufferedImage image, String desiredImageType) throws IOException {
        for (Socket sock : SocketContainer.sockets) {
            if (image != null) {
                OutputStream outputStream = sock.getOutputStream();
                ImageIO.write(image, desiredImageType, outputStream);
                outputStream.flush();
                System.out.println("Image sent.");
            }
        }
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
