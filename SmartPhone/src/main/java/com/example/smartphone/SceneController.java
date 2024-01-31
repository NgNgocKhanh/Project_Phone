package com.example.smartphone;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneController {
    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Label loginMessageLabel;
    public void SwitchToRegister(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("register.fxml"));
            root = fxmlLoader.load();
            stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loginButtonOnAction(ActionEvent event){
            loginMessageLabel.setText("You try to login !");
    }
    
}
