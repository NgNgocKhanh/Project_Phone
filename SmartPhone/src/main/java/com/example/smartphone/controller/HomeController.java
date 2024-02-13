package com.example.smartphone.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController {
    private static String username; // Biến static để lưu trữ tên người dùng

    // Phương thức để thiết lập tên người dùng
    public static void setUsername(String username) {
        HomeController.username = username;
    }
    @FXML
    private Label usernameLabel;
    public void initialize() {
        usernameLabel.setText("username");
    }
}