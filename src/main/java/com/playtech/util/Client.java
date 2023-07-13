package com.playtech.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Client {
    static Socket socket;
    static Reader reader;
    static String message;
    static String reply;
    static OutputStream outputStream;
    static InputStream inputStream;
    private static final int CHUNK_SIZE = 1024;

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
        Server.type = "msg";
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

    public void sendImgToServer(File selectedFile, String type) throws IOException {
        Server.type = "img";
        outputStream = socket.getOutputStream();
        BufferedImage image = ImageIO.read(selectedFile);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (image != null) {
            ImageIO.write(image, type, byteArrayOutputStream);
            byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();
            outputStream.write(size);
            outputStream.write(byteArrayOutputStream.toByteArray());
            outputStream.flush();
            System.out.println("Image transferred successfully. ");
        }
    }

    public File getImgFromServer() throws IOException {
        System.out.println("get img method");/*
        File outputFile = new File("image.jpg");
        inputStream = socket.getInputStream();
        byte[] sizeAr = new byte[4];
        inputStream.read(sizeAr);
        int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();
        byte[] imageAr = new byte[size];
        int bytesRead = 0;
        while (bytesRead < size) {
            int chunkSize = Math.min(CHUNK_SIZE, size - bytesRead);
            bytesRead += inputStream.read(imageAr, bytesRead, chunkSize);
        }
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));
        if (image != null) {
            System.out.println("Hay");
            String[] writerFileSuffixes = ImageIO.getWriterFileSuffixes();
            String desiredImageType = "";
            for (String suffix : writerFileSuffixes) {
                desiredImageType = suffix;
            }
            ImageIO.write(image, desiredImageType, outputFile);
            System.out.println("Image saved successfully.");
        }
        return outputFile;*/
        File outputFile = new File("image.jpg");
        inputStream = socket.getInputStream();
        byte[] sizeAr = new byte[4];
        int totalBytesRead = 0;
        while (totalBytesRead < 4) {
            int bytesRead = inputStream.read(sizeAr, totalBytesRead, 4 - totalBytesRead);
            if (bytesRead == -1) {
                // Handle end of stream or other error condition
                break;
            }
            totalBytesRead += bytesRead;
        }

        if (totalBytesRead == 4) {
            int size = ByteBuffer.wrap(sizeAr).getInt();
            if (size >= 0) {
                byte[] imageAr = new byte[size];
                int bytesRead = 0;
                while (bytesRead < size) {
                    int chunkSize = Math.min(CHUNK_SIZE, size - bytesRead);
                    int currentBytesRead = inputStream.read(imageAr, bytesRead, chunkSize);
                    if (currentBytesRead == -1) {
                        // Handle end of stream or other error condition
                        break;
                    }
                    bytesRead += currentBytesRead;
                }

                if (bytesRead == size) {
                    BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));
                    if (image != null) {
                        String[] writerFileSuffixes = ImageIO.getWriterFileSuffixes();
                        String desiredImageType = "";
                        for (String suffix : writerFileSuffixes) {
                            desiredImageType = suffix;
                        }
                        ImageIO.write(image, desiredImageType, outputFile);
                        System.out.println("Image saved successfully.");
                    }
                } else {
                    // Handle incomplete data read or other error condition
                }
            } else {
                // Handle invalid size value
            }
        } else {
            // Handle incomplete data read or other error condition
        }
        return outputFile;
    }
}
