package com.example.smartphone.controller;

import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.util.Optional;

public class LogOutHandlerController {

    private Button buttonLogout;

    public LogOutHandlerController(Button buttonLogout) {
        this.buttonLogout = buttonLogout;
        setupButton();
    }

    private void setupButton() {
        buttonLogout.setOnAction(event -> confirmLogout());
    }

    @FXML
    private void confirmLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText("Confirm Logout");
        alert.setContentText("Are you sure you want to logout?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            handleLogout();
        }
    }

    private void handleLogout() {
        Stage stage = (Stage) buttonLogout.getScene().getWindow();
        stage.close();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/smartphone/login.fxml"));
        Parent root;
        try {
            root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void cancelLogout() {
        // Xử lý khi người dùng chọn "Cancel"
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Logout Cancelled");
        alert.setHeaderText(null);
        alert.setContentText("Logout action has been cancelled.");
        alert.showAndWait();
    }
}
