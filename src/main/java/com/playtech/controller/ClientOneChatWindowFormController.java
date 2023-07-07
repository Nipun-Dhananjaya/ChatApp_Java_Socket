package com.playtech.controller;

import com.jfoenix.controls.JFXButton;
import com.playtech.util.Client;
import com.playtech.util.Server;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientOneChatWindowFormController implements Initializable{
    public AnchorPane chatPane;
    public TextField msgTxt;
    public JFXButton imgBtn;
    public JFXButton sndBtn;
    public JFXButton addBtn;
    public TextArea txtAr;
    Client client=new Client();
    static String ans="";
    private String previousMessage;

    public ClientOneChatWindowFormController() throws IOException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //OutputStreams.remoteSockets.add(sock);
        txtAr.setDisable(true);
        sndBtn.setDisable(true);
        Thread t2= new Thread(()->{
            getMessage(ans);
            try {
                getImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t2.start();
    }

    private void getImage() throws IOException {
        File receivedFile = client.getImgFromServer();
        BufferedImage bufferedImage = ImageIO.read(receivedFile);

        Image image = SwingFXUtils.toFXImage(bufferedImage, null);

        ImageView img= new ImageView(image);
        img.setFitWidth(200);
        img.setPreserveRatio(true);

        Group group = new Group();

        group.getChildren().add(img);
        group.getChildren().add(txtAr);
        System.out.println("grouped");
    }

    private void getMessage(String answer) {
        while (true){
            ans = client.msgGetFromServer(answer);
            if (!ans.equals("")) {
                if (!ans.equals(previousMessage)) {
                    Platform.runLater(() -> {
                        txtAr.appendText("\n" + ans);
                    });
                    System.out.println("Contr  " + ans);
                    previousMessage = ans;
                }
            }
        }
    }

    public void imgBtnOnAction(ActionEvent actionEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        FileChooser.ExtensionFilter image = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif");
        fileChooser.getExtensionFilters().addAll(image);
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            sendTextMsg(((Stage) chatPane.getScene().getWindow()).getTitle()+": ");
            String type= FilenameUtils.getExtension(selectedFile.getName());
            System.out.println(type);
            client.sendImgToServer(selectedFile,type);
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
        }
    }

    public void sendBtnOnAction(ActionEvent actionEvent) throws InterruptedException, FileNotFoundException, UnsupportedEncodingException {
        sendTextMsg(((Stage) chatPane.getScene().getWindow()).getTitle()+": "+msgTxt.getText());
    }

    public void msgTxtOnAction(ActionEvent actionEvent) {
    }

    public void addBtnOnAction(ActionEvent actionEvent) throws IOException {
        setUI("Login", "/view/LoginForm.fxml");
    }
    private void setUI(String title, String path) throws IOException {
        Parent parent  =FXMLLoader.load(getClass().getResource(path));
        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.setTitle(title);
        stage.show();
    }

    public void txtMsgKeyPressedAction(KeyEvent keyEvent) {
        if (msgTxt.getText().equals("")){
            sndBtn.setDisable(true);
        }else{
            sndBtn.setDisable(false);
        }
    }

    public void sendTextMsg(String msg) {
        ans="";
        client.msgSendToServer(msg);
        msgTxt.clear();
        sndBtn.setDisable(true);
    }
}
