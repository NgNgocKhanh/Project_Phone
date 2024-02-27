package com.example.smartphone.controller;

import com.example.smartphone.controller.GetData;
import dao.JDBCConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


public class RegisterController {
    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private TextField fullNameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextArea addressTextArea;

    @FXML
    private TextField phoneTextField;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private Button registerButton;



    @FXML
    private Button close;

    @FXML
    private Button loginButton;

    @FXML
    private Button minimizeButton;
    Connection connection = JDBCConnect.getJDBCConnection();
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private void initialize() {
        addRoleToComboBox();
    }

    /**
     * Registers a new employee by inserting their details into the database.
     * Checks if all required fields are filled before performing the registration.
     */

    @FXML
    void registerEmp() {
        Map<Integer, String> roleMap = getRoleMap();
        String selectedRole = roleComboBox.getValue();
        int selectedId = getKeyFromValue(roleMap, selectedRole);

        String joinDate = LocalDate.now().toString();

        if (isFilledFields()) {
            try {
                String sql = "INSERT INTO employee(employeeName, phoneNumber, email, roleId, password, username, address,joinDate,salary) " +
                        "VALUES (?,?,?,?,SHA2(?, 256),?,?,?,?)";

                PreparedStatement preparedStatement = connection.prepareStatement(sql);

                preparedStatement.setString(1, fullNameTextField.getText());
                preparedStatement.setString(2, phoneTextField.getText());
                preparedStatement.setString(3, emailTextField.getText());
                preparedStatement.setInt(4, selectedId);
                preparedStatement.setString(5, passwordTextField.getText());
                preparedStatement.setString(6, usernameTextField.getText());
                preparedStatement.setString(7, addressTextArea.getText());
                preparedStatement.setString(8, joinDate);

                if (selectedId == 1) {
                    preparedStatement.setDouble(9, 20000);
                } else if (selectedId == 2) {
                    preparedStatement.setDouble(9, 3000);
                }

                preparedStatement.executeUpdate();

                GetData.showSuccessAlert("Success message", "Register successfully!");

                preparedStatement.close();
                resetForm();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Checks if all required fields are filled in the registration form.
     *
     * @return true if all required fields are filled, false otherwise.
     */
    private boolean isFilledFields() {
        if (fullNameTextField.getText().isEmpty() || usernameTextField.getText().isEmpty() || passwordTextField.getText().isEmpty() || emailTextField.getText().isEmpty() || phoneTextField.getText().isEmpty() || roleComboBox.getValue() == null || addressTextArea.getText().isEmpty()) {
            GetData.showWarningAlert("Warning message", "Please fill all required fields!");
            return false;
        } else {
            return true;
        }
    }

    /**
     * Retrieves a map of role IDs and their corresponding names from the database.
     *
     * @return A map containing role IDs as keys and role names as values.
     */
    private Map<Integer, String> getRoleMap() {
        Map<Integer, String> roleMap = new HashMap<>();
        String sql = "SELECT *FROM `role` WHERE roleId = 1 OR roleId = 2";
        try {
            Statement statement = connection.createStatement();
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

    /**
     * Adds role names to the role ComboBox.
     */
    private void addRoleToComboBox() {
        Map<Integer, String> roleMap = getRoleMap();
        roleComboBox.getItems().addAll(roleMap.values());
    }

    /**
     * Gets the key from a map based on the given value.
     *
     * @param map   The map to search in.
     * @param value The value to search for.
     * @return The key corresponding to the given value, or -1 if not found.
     */
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

    /**
     * Resets the registration form to its initial state.
     */
    private void resetForm() {
        fullNameTextField.clear();
        usernameTextField.clear();
        passwordTextField.clear();
        emailTextField.clear();
        phoneTextField.clear();
        emailTextField.clear();
        roleComboBox.setValue(null);
        addressTextArea.clear();
    }
}
