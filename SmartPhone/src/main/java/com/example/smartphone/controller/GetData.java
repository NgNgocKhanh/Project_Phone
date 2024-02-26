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