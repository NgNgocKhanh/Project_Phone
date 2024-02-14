package com.example.smartphone.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
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
        usernameLabel.setText(username);
    }
    @FXML
    private Button logoutButton;
    @FXML
    public BorderPane fullBorderPane;
    @FXML
    private Button phoneButton;
    @FXML
    private Button distributorButton;
    @FXML
    private Button eventButton;
    @FXML
    private  Button addOderButton;
    @FXML
    private Button iventoryButton;
    @FXML
    private Button logOutButton;

    @FXML
    void logoutClick(MouseEvent event) {
        ButtonType resultConfirm = GetData.showConfirmationAlert("Confirmation message", "Are you sure you want to logout?");
        // if user confirm delete then delete
        if (resultConfirm.equals(ButtonType.OK)) {
            // Close the main app window
            Stage mainAppStage = (Stage) fullBorderPane.getScene().getWindow();
            mainAppStage.close();

            // Open the login window
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demojavafxproject/login.fxml"));
                Parent root = loader.load();

                Stage loginStage = new Stage();
                loginStage.setScene(new Scene(root));
                loginStage.initStyle(StageStyle.TRANSPARENT);
                loginStage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void loadPage(String page) {
        Parent root = null;
        try {
            // Create a File object for the FXML file
            File fxmlFile = new File("src/main/resources/com/example/demojavafxproject/" + page + ".fxml");

            // Get the absolute path of the FXML file
            String absolutePath = fxmlFile.getAbsolutePath();

            root = FXMLLoader.load(new File(absolutePath).toURI().toURL());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Cannot navigate page");
        }
        fullBorderPane.setCenter(root);
    }
}