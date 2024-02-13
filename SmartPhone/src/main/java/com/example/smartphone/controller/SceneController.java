package com.example.smartphone.controller;

import dao.JDBCConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SceneController {
    @FXML
    private TextField tfEmailLogin;
    @FXML
    private TextField tfUsernameLogin;
    @FXML
    private PasswordField tfPasswordLogin;
    @FXML
    private Button btnCon;
    @FXML
    private Label loginMessageLabel;

    @FXML
    public void initialize() {
        btnCon.setOnAction(event -> {
            try {
                login(event);
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    public void SwitchToRegister(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/smartphone/register.fxml"));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void loginButtonOnAction(ActionEvent event) throws SQLException, IOException {
        login(event);
    }

    @FXML
    public void login(ActionEvent event) throws SQLException, IOException {
        Connection con = JDBCConnect.getJDBCConnection();
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            preparedStatement = con.prepareStatement("SELECT * FROM user WHERE email = ? AND username = ? AND password = ?");
            preparedStatement.setString(1, tfEmailLogin.getText());
            preparedStatement.setString(2, tfUsernameLogin.getText());
            preparedStatement.setString(3, tfPasswordLogin.getText());
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                String username = rs.getString("username"); // Lấy tên người dùng từ ResultSet
                HomeController.setUsername(username);
                // Load the new FXML file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/smartphone/home.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setFullScreen(true);
                stage.show();

                // Nếu cần truyền dữ liệu qua controller của FXML mới:
                HomeController homeController = loader.getController();
                // Thực hiện các hoạt động cần thiết trên homeController
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Login Failed !", ButtonType.OK);
                alert.show();
            }
        } finally {
            // Close resources
            if (rs != null) {
                rs.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.show();
    }
}
