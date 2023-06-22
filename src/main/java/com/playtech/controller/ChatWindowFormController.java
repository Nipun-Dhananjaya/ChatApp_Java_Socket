package com.playtech.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatWindowFormController implements Initializable {
    public ImageView sendBtn;
    public AnchorPane chatPane;
    public TextField msgTxt;
    public JFXListView membLst;
    public JFXButton audioBtn;
    public JFXButton imgBtn;
    public JFXButton sndBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void msgTxtOnAction(ActionEvent actionEvent) {
    }

    public void sendBtnOnAction(ActionEvent actionEvent) {
    }

    public void audBtnOnAction(ActionEvent actionEvent) {
    }

    public void imgBtnOnAction(ActionEvent actionEvent) {
    }
}
