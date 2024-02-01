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
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    private Button exitButton;
    public TextField ftname;
    public TextField flname;
    public TextField fusername;
    public PasswordField fpass;
    public PasswordField fconfirmpass;
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
            if (fusername.getText().isEmpty() || fpass.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Registration Failed!\", \"Both username and password must be provided.", ButtonType.OK);
                alert.show();
                return;
            }if (!isPasswordConfirmed()) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Registration Failed!\", \"Password and Confirm Password do not match.", ButtonType.OK);
            alert.show();
            return;
        }
        if (!isUsernameConfirmed()) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Registration Failed!\", \"Username have 4 character and the first not number and character special.", ButtonType.OK);
            alert.show();
            return;
        }
            PreparedStatement preparedStatement = null;
            Connection con = JDBCConnect.getJDBCConnection();
            try {
                preparedStatement = con.prepareStatement("INSERT INTO admin (firstName, lastName ,username, password) VALUES (?, ?, ?, ?)");
                preparedStatement.setString(1, ftname.getText());
                preparedStatement.setString(2, flname.getText());
                preparedStatement.setString(3, fusername.getText());
                preparedStatement.setString(4, fpass.getText());
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
            return fpass.getText().equals(fconfirmpass.getText());
        }
        private boolean isUsernameConfirmed(){
            return fusername.getText().matches("^[a-zA-Z][a-zA-Z0-9]{4,}$");
        }
    }
