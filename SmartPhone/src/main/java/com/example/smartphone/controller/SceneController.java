package com.example.smartphone.controller;

import dao.JDBCConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SceneController implements Initializable {
    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Label loginMessageLabel;

    public void SwitchToRegister(ActionEvent event) {
        try {
            String fxmlFileName = "register.fxml";
            File fxmlFile = new File("src/main/resources/com/example/SmartPhone/" + fxmlFileName);

            String absolutePath = fxmlFile.getAbsolutePath();
            Parent root = FXMLLoader.load(new File(absolutePath).toURI().toURL());
            stage.initStyle(StageStyle.TRANSPARENT);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void loginButtonOnAction(ActionEvent event) throws SQLException {
        login();
    }
    public TextField tfEmailLogin;

    public TextField tfUsernameLogin;
    public PasswordField tfPasswordLogin;
    public Button btnCon;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnCon.setOnAction(actionEvent -> {
            try {
                login();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void login() throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        Connection con = JDBCConnect.getJDBCConnection();
        try {
            preparedStatement = con.prepareStatement("SELECT * FROM  user WHERE email = ? AND  username = ? AND password = ?");
            preparedStatement.setString(1, tfEmailLogin.getText());
            preparedStatement.setString(2, tfUsernameLogin.getText());
            preparedStatement.setString(3, tfPasswordLogin.getText());
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("home.fxml"));
                Parent root = fxmlLoader.load();
                Scene scene = new Scene(root, 520, 400);
                Stage stage = (Stage) tfUsernameLogin.getScene().getWindow();
                stage.setScene(scene);
                stage.show();

            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Login Failed !", ButtonType.OK);
                alert.show();
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
