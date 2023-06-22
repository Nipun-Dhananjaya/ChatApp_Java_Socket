package com.playtech.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.playtech.bo.BOFactory;
import com.playtech.bo.custom.UserBO;
import com.playtech.dto.UserDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginFormController implements Initializable {
    public JFXTextField usrNmTxt;
    public JFXButton lgnBtn;
    public AnchorPane root;
    UserBO userBO= (UserBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.USER);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void usrNmTxtOnAction(ActionEvent actionEvent) {
    }

    public void lgnBtnOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) root.getScene().getWindow();
        if (userBO.searchUser(new UserDTO(usrNmTxt.getText()))) {
            setUI(usrNmTxt.getText(), "/view/ChatWindowForm.fxml");
        }else{
            new Alert(Alert.AlertType.WARNING, "Username is incorrect!").show();
        }
    }
    private void setUI(String title, String path) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(new Scene(anchorPane));
        stage.setTitle(title);
    }
}
