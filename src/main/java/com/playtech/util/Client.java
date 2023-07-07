package com.playtech.util;

import com.playtech.controller.ClientOneChatWindowFormController;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Client {
    static Socket socket;
    static Reader reader;
    static String message;
    static String reply;

    public Client() throws IOException {
        System.out.println("initialize");
        socket = new Socket("localhost",4005);
    }
    public static String msgGetFromServer(String ans) {
        Thread t2= new Thread(()->{
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
        return reply==null? "":reply;
    }
    public static void msgSendToServer(String msg) {
        Server.type="msg";
        message=msg;
        System.out.println("Cli: "+message);
        try {
            Writer writer = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);
            PrintWriter printWriter = new PrintWriter(writer);
            printWriter.println(message);
            printWriter.flush();
            System.out.println("flush " + message);
            message="";
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendImgToServer(File selectedFile,String type) throws IOException {
        Server.type="img";
        OutputStream outputStream = socket.getOutputStream();
        BufferedImage image = ImageIO.read(selectedFile);
        ImageIO.write(image, type, outputStream);
        outputStream.flush();
        System.out.println("Image transferred successfully.");
    }
    public File getImgFromServer() throws IOException {
        File outputFile = new File("image.jpg");
        Thread t2= new Thread(()->{
            try {
                BufferedImage image = ImageIO.read(socket.getInputStream());
                String[] writerFileSuffixes=ImageIO.getWriterFileSuffixes();
                String desiredImageType="";
                for (String suffix : writerFileSuffixes) {
                    desiredImageType=suffix;
                }
                if (image != null) {
                    try {
                        ImageIO.write(image, desiredImageType, outputFile);
                        System.out.println("Image saved successfully.");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Image get.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t2.start();
        return outputFile;
    }
}
