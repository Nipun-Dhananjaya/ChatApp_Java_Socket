package com.playtech.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.nio.ByteBuffer;

public class Server {
    private static ServerSocket serverSocket;
    static String message;
    static Reader reader;
    static Socket socket;
    static InputStream inputStream;
    static String type = "";
    static int targetWidth = 200;
    static int targetHeight = 200;
    private static final int CHUNK_SIZE = 1024;

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
                                if (type.equals("msg")) {
                                    System.out.println("type msg "+message);
                                    flushMessage(message);
                                }
                            }
                            while (inputStream.available() > 0) {
                                inputStream.read();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }finally {

                        }
                    }
                });
                t2.start();
                Thread t3 = new Thread(() -> {
                    while (true) {
                        try {
                            /*inputStream = socket.getInputStream();
                            byte[] sizeAr = new byte[4];
                            inputStream.read(sizeAr);
                            int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();
                            byte[] imageAr = new byte[size];
                            inputStream.read(imageAr);
                            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));
                            if (type.equals("img")) {
                                System.out.println("type img");
                                if (image != null) {
                                    BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, image.getType());
                                    String[] writerFileSuffixes = ImageIO.getWriterFileSuffixes();
                                    String desiredImageType = "";
                                    for (String suffix : writerFileSuffixes) {
                                        desiredImageType = suffix;
                                    }

                                    Graphics2D graphics2D = resizedImage.createGraphics();
                                    graphics2D.drawImage(image, 0, 0, targetWidth, targetHeight, null);
                                    graphics2D.dispose();
                                    System.out.println("img pass to the method");
                                    flushImage(resizedImage, desiredImageType);
                                }
                                while (inputStream.available() > 0) {
                                    inputStream.read();
                                }
                            }*/
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
                                byte[] imageAr = new byte[size];
                                totalBytesRead = 0;
                                while (totalBytesRead < size) {
                                    int bytesRead = inputStream.read(imageAr, totalBytesRead, size - totalBytesRead);
                                    if (bytesRead == -1) {
                                        break;
                                    }
                                    totalBytesRead += bytesRead;
                                }

                                if (totalBytesRead == size) {
                                    BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));
                                    if (type.equals("img") && image != null) {
                                        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, image.getType());
                                        String[] writerFileSuffixes = ImageIO.getWriterFileSuffixes();
                                        String desiredImageType = "";
                                        for (String suffix : writerFileSuffixes) {
                                            desiredImageType = suffix;
                                        }

                                        Graphics2D graphics2D = resizedImage.createGraphics();
                                        graphics2D.drawImage(image, 0, 0, targetWidth, targetHeight, null);
                                        graphics2D.dispose();
                                        System.out.println("img pass to the method");
                                        flushImage(resizedImage, desiredImageType);
                                    }
                                } else {
                                    continue;
                                }
                            } else {
                                continue;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }finally {

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
            System.out.println("flush image method");
            if (image != null) {
                System.out.println("image not null");
                OutputStream outputStream = sock.getOutputStream();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ImageIO.write(image, desiredImageType, byteArrayOutputStream);
                byte[] imageBytes = byteArrayOutputStream.toByteArray();
                /*int chunkSize = 1024;
                byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();
                outputStream.write(size);
                for (int i = 0; i < imageBytes.length; i += chunkSize) {
                    int length = Math.min(chunkSize, imageBytes.length - i);
                    outputStream.write(imageBytes, i, length);
                }
                outputStream.flush();*/
                int offset = 0;
                while (offset < imageBytes.length) {
                    int length = Math.min(CHUNK_SIZE, imageBytes.length - offset);
                    outputStream.write(imageBytes, offset, length);
                    offset += length;
                }
                outputStream.write(imageBytes, 0, imageBytes.length);
                outputStream.flush();
                System.out.println("Image sent.");
                /*outputStream.write(size);
                outputStream.write(imageBytes);
                outputStream.flush();
                System.out.println("Image sent.");*/
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
