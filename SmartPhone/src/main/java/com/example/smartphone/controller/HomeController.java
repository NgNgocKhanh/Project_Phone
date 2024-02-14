package com.example.smartphone.controller;

import com.example.smartphone.model.MyPageManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
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
    @FXML
    private Pagination pagination;
    private MyPageManager pageManager;


    private static String username;
    public static void setUsername(String username) {
        HomeController.username = username;
    }
    @FXML
    private Label usernameLabel;
    public void initialize() {
        usernameLabel.setText(username);
        pagination = new Pagination();
        pageManager = new MyPageManager(10); // 10 là số mục mỗi trang
        pagination.setPageFactory(pageManager::createPage);
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
    private Button addOrderButton;
    @FXML
    private Button inventoryButton;
    @FXML
    private Button logOutButton;
    private Button activeButton;
    @FXML
    void logoutClick(MouseEvent event) {
        ButtonType resultConfirm = GetData.showConfirmationAlert("Confirmation message", "Are you sure you want to logout?");
        // if user confirm delete then delete
        if (resultConfirm.equals(ButtonType.OK)) {
            // Close the main app window
            Stage HomeAppStage = (Stage) fullBorderPane.getScene().getWindow();
            HomeAppStage.close();

            // Open the login window
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/smartphone/login.fxml"));
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
    @FXML
    void phonePage(MouseEvent event){
        loadPage("phone-view");
        setActiveButton(phoneButton);
    }
    @FXML
    void distributorPage(MouseEvent event) {
        loadPage("distributor-view");
        setActiveButton(distributorButton);
    }
    @FXML
    void eventPage(MouseEvent event) {
        loadPage("event-view");
        setActiveButton(eventButton);
    }
    @FXML
    void addOrderPage(MouseEvent event) {
        loadPage("add-order-view");
        setActiveButton(addOrderButton);
    }
    @FXML
    void inventoryPage(MouseEvent event) {
        loadPage("iventory-view");
        setActiveButton(inventoryButton);
    }
    private void loadPage(String page) {
        Parent root = null;
        try {
            // Create a File object for the FXML file
            File fxmlFile = new File("src/main/resources/com/example/smartphone/" + page + ".fxml");

            // Get the absolute path of the FXML file
            String absolutePath = fxmlFile.getAbsolutePath();

            root = FXMLLoader.load(new File(absolutePath).toURI().toURL());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Cannot navigate page");
        }
        fullBorderPane.setCenter(root);
    }
    private void setActiveButton(Button button) {
        if (activeButton != null) {
            activeButton.setStyle("-fx-background-color: white;");
        }
        button.setStyle("-fx-background-color: #515151;");
        activeButton = button;
    }
}