package com.example.smartphone;

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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("register.fxml"));
            root = fxmlLoader.load();
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

    public TextField tname;
    public PasswordField tpass;
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
            preparedStatement = con.prepareStatement("SELECT * FROM  admin WHERE username = ? AND password = ?");
            preparedStatement.setString(1, tname.getText());
            preparedStatement.setString(2, tpass.getText());
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Login Successfully !", ButtonType.OK);
                alert.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Login Failed !", ButtonType.OK);
                alert.show();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
