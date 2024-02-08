package com.example.smartphone;

import dao.JDBCConnect;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to Store Phone !");
    }

    @FXML
    private Button exitButton;
    public TextField tfEmail;
    public TextField tfUsername;
    public TextField tfPhone;
    public PasswordField tfPassword;
    public PasswordField tfConfirmPassword;
    public Button btnConRegister;

    @FXML
    private void handleExitButton(ActionEvent event) {
        Platform.exit();
    }

    public void registerButtonOnAction(ActionEvent event) throws SQLException {
        register();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnConRegister.setOnAction(actionEvent -> {
            try {
                register();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void register() throws SQLException {
        if (!isPasswordConfirmed()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Registration Failed!\", \"Password and Confirm Password do not match.", ButtonType.OK);
            alert.show();
            return;
        }
        if (!isUsernameConfirmed()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Username Must be greater than 4", ButtonType.OK);
            alert.show();
            return;
        }
        if (!isCheckEmail()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Invalid Email Format", ButtonType.OK);
            alert.show();
            return;
        }
        if (!isCheckPhoneNumber()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Invalid Phone Number", ButtonType.OK);
            alert.show();
            return;
        }
            PreparedStatement preparedStatement = null;
            Connection con = JDBCConnect.getJDBCConnection();
            try {
                preparedStatement = con.prepareStatement("INSERT INTO user (username, email ,password) VALUES (?, ?, ?)");
                preparedStatement.setString(1, tfUsername.getText());
                preparedStatement.setString(2, tfEmail.getText());
                preparedStatement.setString(3, tfPassword.getText());
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "User registered successfully!", ButtonType.OK);
                    alert.show();
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Failed to register user!", ButtonType.OK);
                    alert.show();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                con.close();
            }
        }
        private boolean isPasswordConfirmed () {
            // Kiểm tra xem mật khẩu và xác nhận mật khẩu giống nhau hay không
            return tfPassword.getText().equals(tfConfirmPassword.getText());
        }
        private boolean isUsernameConfirmed(){
            return tfUsername.getText().matches("^[a-zA-Z][a-zA-Z0-9]{4,}$");
        }
    private boolean isCheckEmail() {
        // Kiểm tra định dạng email
        return tfEmail.getText().matches("^[a-zA-Z0-9._-]+@[a-zA-Z0-9-]+\\.[a-zA-Z.]{2,18}$");
    }

    private boolean isCheckPhoneNumber() {
        // Kiểm tra định dạng số điện thoại
        return tfPhone.getText().matches("^(0[1-9]\\d{8}|84[1-9]\\d{7})$");
    }
    }
