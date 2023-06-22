package com.playtech.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.playtech.bo.BOFactory;
import com.playtech.bo.custom.UserBO;
import com.playtech.dto.UserDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatWindowFormController implements Initializable {
    public AnchorPane chatPane;
    public TextField msgTxt;
    public JFXButton audioBtn;
    public JFXButton imgBtn;
    public JFXButton sndBtn;
    public JFXButton addBtn;
    UserBO userBO= (UserBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.USER);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void audBtnOnAction(ActionEvent actionEvent) {
    }

    public void imgBtnOnAction(ActionEvent actionEvent) {
    }

    public void sendBtnOnAction(ActionEvent actionEvent) {
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
}
