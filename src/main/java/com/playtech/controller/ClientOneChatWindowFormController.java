package com.playtech.controller;

import com.jfoenix.controls.JFXButton;
import com.playtech.util.Client;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ClientOneChatWindowFormController implements Initializable{
    public AnchorPane chatPane;
    public TextField msgTxt;
    public JFXButton imgBtn;
    public JFXButton sndBtn;
    public JFXButton addBtn;
    public ScrollPane scrlPn;
    public VBox vBox;
    Client client=new Client();
    static String ans="";
    private String previousMessage;
    static String msgType = "";

    public ClientOneChatWindowFormController() throws IOException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        scrlPn.setDisable(true);
        sndBtn.setDisable(true);
        Thread t2= new Thread(()->{
            getMessage(ans);
        });
        t2.start();
    }

    private void getImage() throws IOException {
        while (true){
            System.out.println("img get method");
            File receivedFile = client.getImgFromServer();
            if (receivedFile != null && receivedFile.exists()) {
                BufferedImage bufferedImage = ImageIO.read(receivedFile);
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);

                Platform.runLater(() -> {
                    HBox hb = new HBox();
                    hb.setAlignment(Pos.CENTER_LEFT);

                    ImageView img = new ImageView(image);
                    img.setFitHeight(200);
                    img.setFitWidth(200);
                    img.setPreserveRatio(true);

                    hb.getChildren().add(img);
                    vBox.getChildren().add(hb);
                });
            }
        }
    }

    private void getMessage(String answer) {
        while (true){
            ans = client.msgGetFromServer(answer);
            if(msgType.equals("msg")) {
                if (!ans.equals("")) {
                    if (!ans.equals(previousMessage)) {
                        Platform.runLater(() -> {
                            Label l1 = new Label("\n" + ans);
                            l1.setWrapText(true);
                            l1.setMaxWidth(300);
                            l1.setMinHeight(20);
                            l1.setFont(new Font("Arial", 20));
                            l1.setTextFill(Color.web("#00090d"));
                            HBox hb = new HBox();
                            hb.setAlignment(Pos.CENTER_LEFT);
                            hb.getChildren().add(l1);
                            vBox.getChildren().add(hb);
                        });
                        System.out.println("Contr  " + ans);
                        previousMessage = ans;
                    }
                }
            }else if (msgType.equals("img")) {
                try {
                    getImage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void imgBtnOnAction(ActionEvent actionEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        FileChooser.ExtensionFilter image = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif");
        fileChooser.getExtensionFilters().add(image);
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            int preCount=vBox.getChildren().size();
            sendTextMsg(((Stage) chatPane.getScene().getWindow()).getTitle()+": ");
            do{
                String type = FilenameUtils.getExtension(selectedFile.getName());
                System.out.println(type);
                msgType = "img";
                client.sendImgToServer(selectedFile, type);
                System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            }while (preCount<vBox.getChildren().size());
        }
    }

    public void sendBtnOnAction(ActionEvent actionEvent) {
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
        msgType="msg";
        client.msgSendToServer(msg);
        msgTxt.clear();
        sndBtn.setDisable(true);
    }

    public void emojiSelectOnAction(ActionEvent actionEvent) {
        Button emojiButton = (Button) actionEvent.getSource();
        String emoji = emojiButton.getText();

        // Use the selected emoji as needed
        System.out.println("Selected Emoji: " + emoji);
        msgTxt.appendText(emoji);
    }
}
