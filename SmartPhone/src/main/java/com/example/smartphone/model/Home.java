package com.example.smartphone.model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;

public class Home extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        String fxmlFileName = "home.fxml";

        // Create a File object for the FXML file
        File fxmlFile = new File("src/main/resources/com/example/SmartPhone/" + fxmlFileName);

        // Get the absolute path of the FXML file
        String absolutePath = fxmlFile.getAbsolutePath();
        Parent root = FXMLLoader.load(new File(absolutePath).toURI().toURL());
        stage.setScene(new Scene(root));
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

