package com.example.smartphone.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;

public class Login extends Application {
    private double xOffset = 0;
    private double   yOffset = 0;
    @Override
    public void start(Stage stage) throws IOException {
        String fxmlFileName = "login.fxml";

        // Create a File manhobject for the FXML file
        File fxmlFile    = new File("src/main/resources/com/example/SmartPhone/" + fxmlFileName);

        // Get the absolute path of the FXML file
        String absolutePath = fxmlFile.getAbsolutePath();
        Parent root = FXMLLoader.load(new File(absolutePath).toURI().toURL());
        stage.setScene(new Scene(root));
        stage.initStyle(StageStyle.TRANSPARENT);
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        // Mouse dragged event handler
        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}