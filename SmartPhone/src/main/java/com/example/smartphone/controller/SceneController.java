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
import javafx.stage.StageStyle;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class SceneController {
    @FXML
    public static int roleIdReset;
    @FXML
    private TextField tfUsernameLogin;
    @FXML
    private PasswordField tfPasswordLogin;
    @FXML
    private Button btnCon;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private ComboBox<String> roleComboBox;
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
    private Map<Integer, String> getRoleMap() {
        Connection con = JDBCConnect.getJDBCConnection();
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        Map<Integer, String> roleMap = new HashMap<>();
        String sql = "SELECT *FROM `role`";
        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                int roleId = resultSet.getInt("roleId");
                String roleName = resultSet.getString("roleName");

                roleMap.put(roleId, roleName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roleMap;
    }
    private void addRoleToComboBox() {
        Map<Integer, String> roleMap = getRoleMap();
        roleComboBox.getItems().addAll(roleMap.values());
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
        if (isFilledFields()) {
            try {
                String sql = "SELECT employeeId FROM employee WHERE username = ? AND roleId = ?";
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, tfUsernameLogin.getText());
                Map<Integer, String> roleMap = getRoleMap();
                String selectedRole = roleComboBox.getValue();
                int selectedId = getKeyFromValue(roleMap, selectedRole);

                preparedStatement.setInt(2, selectedId);


                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    GetData.empId = resultSet.getInt("employeeId");

                    String storedHashedPassword = getHashedPassword(tfUsernameLogin.getText(), selectedId);
                    String enteredPassword = tfPasswordLogin.getText();

                    if (storedHashedPassword != null && compareHashes(enteredPassword, storedHashedPassword)) {
                        GetData.username = usernameTextField.getText();
                        GetData.role = roleComboBox.getValue();
                        GetData.showSuccessAlert("Login", "Login Successfully");
                        loginButton.getScene().getWindow().hide();

                        // Open the main app window
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demojavafxproject/home.fxml"));
                        Parent root = loader.load();
                        LoginController loginControllerr = loader.getController();
                        loginController.initialize();  // Initialize the main app controller

                        Stage logincontroller = new Stage();
                        logincontroller.setScene(new Scene(root));
                        logincontroller.initStyle(StageStyle.TRANSPARENT);
                        logincontroller.show();
                    } else {
                        GetData.showConfirmationAlert("Login failed!", "Username or password is wrong");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getHashedPassword(String username, int roleId) throws SQLException {
        String sql = "SELECT password FROM employee WHERE username = ? AND roleId = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, username);
        preparedStatement.setInt(2, roleId);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getString("password");
        }
        return null;
    }

    private boolean compareHashes(String input, String storedHash) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");/-strong/-heart:>:o:-((:-hbyte[] inputHashBytes = md.digest(input.getBytes());

        StringBuilder inputHash = new StringBuilder();
        for (byte b : inputHashBytes) {
            inputHash.append(String.format("%02x", b));
        }

        return inputHash.toString().equals(storedHash);
    }
    }
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.show();
    }
private String getHashedPassword(String username, int roleId) throws SQLException {
    Connection con = JDBCConnect.getJDBCConnection();
    ResultSet rs = null;

    String sql = "SELECT password FROM employee WHERE username = ? AND roleId = ?";
    PreparedStatement preparedStatement = con.prepareStatement(sql);
    preparedStatement.setString(1, username);
    preparedStatement.setInt(2, roleId);
    ResultSet resultSet = preparedStatement.executeQuery();

    if (resultSet.next()) {
        return resultSet.getString("password");
    }
    return null;
}

private boolean compareHashes(String input, String storedHash) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    byte[] inputHashBytes = md.digest(input.getBytes());

    StringBuilder inputHash = new StringBuilder();
    for (byte b : inputHashBytes) {
        inputHash.append(String.format("%02x", b));
    }

    return inputHash.toString().equals(storedHash);
}
private int getKeyFromValue(Map<Integer, String> map, String value) {
    // iterate over the entries of the map object
    for (Map.Entry<Integer, String> entry : map.entrySet()) {
        // check if entry of the map equal with the value param of ComboBox then return the key
        if (entry.getValue().equals(value)) {
            return entry.getKey();
        }
    }
    return -1;
}
    private boolean isFilledFields() {
        if (tfUsernameLogin.getText().isEmpty()
                || roleComboBox.getValue() == null
                || tfPasswordLogin.getText().isEmpty()) {
            GetData.showConfirmationAlert("Warning message", "Please fill all required fields!");
            return false;
        } else {
            return true;
        }
    }
}
