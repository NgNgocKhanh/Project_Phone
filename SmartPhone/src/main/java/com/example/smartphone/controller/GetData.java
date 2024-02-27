package com.example.smartphone.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;
import java.util.Properties;

public class GetData {
    public static int empId;
    public static String username;
    public static String role;
    public static String path;
    public static int orderId;
    public static String randomCode;
    public static void showSuccessAlert(String title, String contentText) {
        // create an ImageView with the absolute image path
        String imageName = "success-icon-green.png";

        // Create a File object for the FXML file
        File imageFile = new File("src/main/resources/com/example/demojavafxproject/images/" + imageName);

        // Get the absolute path of the FXML file
        String imagePath = imageFile.getAbsolutePath();

        Image iconSuccessImage = new Image(imagePath);
        ImageView iconSuccessImageView = new ImageView(iconSuccessImage);
        iconSuccessImageView.setFitHeight(64);
        iconSuccessImageView.setFitWidth(64);

        // create an Alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(contentText);
        alert.setHeaderText(null);

        // set the image icon for the alert
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(iconSuccessImage);
        alertStage.initModality(Modality.NONE);
        alertStage.setAlwaysOnTop(true);

        // create a StackPane to hold the Imageview
        StackPane iconContainer = new StackPane(iconSuccessImageView);

        // set teh custom icon container as the graphic for the Alert
        alert.getDialogPane().setGraphic(iconContainer);

        // show the alert and wait for the user to close it
        alert.showAndWait();
    }
    public static void showWarningAlert(String title, String contentText) {
        Image iconWarningImage = new Image(GetData.class.getResourceAsStream("/com/example/smartphone/image_icons/warning-icon-yellow.png"));
        ImageView iconWarningImageView = new ImageView(iconWarningImage);
        iconWarningImageView.setFitHeight(64);
        iconWarningImageView.setFitWidth(64);

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(contentText);
        alert.setHeaderText(null);

        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(iconWarningImage);
        alertStage.initModality(Modality.NONE);
        alertStage.setAlwaysOnTop(true);

        StackPane iconContainer = new StackPane(iconWarningImageView);
        alert.getDialogPane().setGraphic(iconContainer);

        alert.showAndWait();
    }

    public static void showErrorAlert(String title, String contentText) {
        Image iconErrorImage = new Image(GetData.class.getResourceAsStream("/com/example/smartphone/image_icons/error-icon-red.png"));
        ImageView iconErrorImageView = new ImageView(iconErrorImage);
        iconErrorImageView.setFitHeight(64);
        iconErrorImageView.setFitWidth(64);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(contentText);
        alert.setHeaderText(null);

        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(iconErrorImage);
        alertStage.initModality(Modality.NONE);
        alertStage.setAlwaysOnTop(true);

        StackPane iconContainer = new StackPane(iconErrorImageView);
        alert.getDialogPane().setGraphic(iconContainer);

        alert.showAndWait();
    }

    public static ButtonType showConfirmationAlert(String title, String contentText) {
        Image iconConfirmationImage = new Image(GetData.class.getResourceAsStream("/com/example/smartphone/image_icons/confirm-icon-blue.png"));
        ImageView iconConfirmationImageView = new ImageView(iconConfirmationImage);
        iconConfirmationImageView.setFitHeight(64);
        iconConfirmationImageView.setFitWidth(64);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(contentText);
        alert.setHeaderText(null);

        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(iconConfirmationImage);
        alertStage.initModality(Modality.NONE);
        alertStage.setAlwaysOnTop(true);

        StackPane iconContainer = new StackPane(iconConfirmationImageView);
        alert.getDialogPane().setGraphic(iconContainer);

        return alert.showAndWait().orElse(ButtonType.CANCEL);
    }


}