package com.example.smartphone;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class RegisterController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to Store Phone !");
    }
    @FXML
    private Button exitButton;

    @FXML
    private void handleExitButton(ActionEvent event) {
        Platform.exit();
    }
}
