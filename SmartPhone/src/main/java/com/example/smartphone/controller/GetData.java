package com.example.smartphone.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class GetData {

    public static ButtonType showConfirmationAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Hiển thị hộp thoại và chờ cho người dùng phản hồi
        return alert.showAndWait().orElse(ButtonType.CANCEL);
    }
}
