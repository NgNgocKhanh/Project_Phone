package com.example.smartphone.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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

    public static void handleSendMail(String message, String subject, List<String> customerMails) {
        // variable for gmail
        String host = "smtp.gmail.com";
        String from = "projecttest213@gmail.com";

        // get the system properties
        Properties properties = System.getProperties();

        // setting important info to properties object
        // host set
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // step 1: to get the session object
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("projecttest213@gmail.com", "pnnzreerwjctwjmn");
            }
        });

        // step 2: compose the message [text,multimedia]
        MimeMessage m = new MimeMessage(session);

        try {
            // from email
            m.setFrom(new InternetAddress(from));

            for (String email : customerMails) {
                m.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            }

            // adding subject to message
            m.setSubject(subject);

            m.setContent(message, "text/html");

            // step 3: send the message using Transport class
            Transport.send(m);

            System.out.println("sent success........");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
        // create an ImageView with the absolute image path
        String imageName = "warning-icon-yellow.png";

        // Create a File object for the FXML file
        File imageFile = new File("src/main/resources/com/example/demojavafxproject/images/" + imageName);

        // Get the absolute path of the FXML file
        String imagePath = imageFile.getAbsolutePath();

        Image iconSuccessImage = new Image(imagePath);
        ImageView iconSuccessImageView = new ImageView(iconSuccessImage);
        iconSuccessImageView.setFitHeight(64);
        iconSuccessImageView.setFitWidth(64);

        // create an Alert
        Alert alert = new Alert(Alert.AlertType.WARNING);
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

    public static void showErrorAlert(String title, String contentText) {
        // create an ImageView with the absolute image path
        String imageName = "error-icon-red.png";

        // Create a File object for the FXML file
        File imageFile = new File("src/main/resources/com/example/demojavafxproject/images/" + imageName);

        // Get the absolute path of the FXML file
        String imagePath = imageFile.getAbsolutePath();

        Image iconSuccessImage = new Image(imagePath);
        ImageView iconSuccessImageView = new ImageView(iconSuccessImage);
        iconSuccessImageView.setFitHeight(64);
        iconSuccessImageView.setFitWidth(64);

        // create an Alert
        Alert alert = new Alert(Alert.AlertType.ERROR);
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

    public static ButtonType showConfirmationAlert(String title, String contentText) {
        // create an ImageView with the absolute image path
        String imageName = "confirm-icon-blue.png";

        // Create a File object for the FXML file
        File imageFile = new File("src/main/resources/com/example/demojavafxproject/images/" + imageName);

        // Get the absolute path of the FXML file
        String imagePath = imageFile.getAbsolutePath();

        Image iconSuccessImage = new Image(imagePath);
        ImageView iconSuccessImageView = new ImageView(iconSuccessImage);
        iconSuccessImageView.setFitHeight(64);
        iconSuccessImageView.setFitWidth(64);

        // create an Alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
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

        // Show the confirmation alert and wait for the user's response (OK or Cancel)
        return alert.showAndWait().orElse(ButtonType.CANCEL);
    }

}
