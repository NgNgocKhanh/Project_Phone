package com.example.smartphone.controller;

import dao.JDBCConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class InventoryController implements Initializable {
    @FXML
    private VBox phoneContainer;


    Connection connection = JDBCConnect.getJDBCConnection();
    @FXML
    private Pagination pagination;

    private static final int ITEMS_PER_PAGE = 12;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            int totalItems = calculateTotalItems();

            pagination.setPageCount((int) Math.ceil((double) totalItems / ITEMS_PER_PAGE));
            pagination.setPageFactory(this::createPage);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private int calculateTotalItems() throws SQLException {
        int totalItems = 0;

        try {
            String countQuery = "SELECT COUNT(*) FROM phone_inventory";
            PreparedStatement countStatement = connection.prepareStatement(countQuery);
            ResultSet countResult = countStatement.executeQuery();

            if (countResult.next()) {
                totalItems = countResult.getInt(1);
            }

            countStatement.close();
            countResult.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalItems;
    }

    private VBox createPage(int pageIndex) {
        VBox page = new VBox();
        page.setSpacing(10);

        try {
            int offset = pageIndex * ITEMS_PER_PAGE; // Calculate the offset for the current page
            String query = "SELECT i.inventoryId, p.phoneName, p.price, i.quantityInStock, i.status \n" +
                    "FROM phone_inventory i\n" +
                    "JOIN phone p ON i.inventoryId = p.phoneId  " +
                    "LIMIT " + offset + ", " + ITEMS_PER_PAGE;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            int index = offset + 1; // Adjust the index for the current page
            while (resultSet.next()) {
                int inventoryId = resultSet.getInt("inventoryId");
                HBox phoneInfoHBox = createPhoneInfoHBox(index, resultSet, inventoryId);
                page.getChildren().addAll(phoneInfoHBox, new Separator());
                index++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return page;
    }


    private HBox createPhoneInfoHBox(int index, ResultSet resultSet, int inventoryId) throws SQLException {
        HBox phoneInfoHBox = new HBox(0);
        phoneInfoHBox.getStyleClass().add("custom-phone-info-hbox"); // Apply a style class to the HBox
        phoneInfoHBox.setAlignment(Pos.CENTER_LEFT);
        phoneInfoHBox.setPrefHeight(35);


        Label indexLabel = new Label(String.valueOf(index));
        indexLabel.setPrefWidth(40);
        Label nameLabel = new Label(resultSet.getString("name"));
        nameLabel.setPrefWidth(215);
        HBox.setMargin(nameLabel, new Insets(0, 0, 0, 20));
        Label modelLabel = new Label(resultSet.getString("price"));
        modelLabel.setPrefWidth(215);
        HBox.setMargin(modelLabel, new Insets(0, 0, 0, 20));

        Button minusButton = new Button("─");
        minusButton.setPrefWidth(35);
        minusButton.getStyleClass().add("custom-minus-button"); // Apply a style class to the minusButton
        minusButton.setId("minusButton" + index);
        minusButton.setOnAction(this::handleMinusButton);

        TextField quantityTextField = new TextField(resultSet.getString("quantityInStock"));
        quantityTextField.getStyleClass().add("custom-quantity-textfield"); // Apply a style class to the quantityTextField
        quantityTextField.setId("quantityTextField" + index);

        quantityTextField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!isNumeric(event.getCharacter())) {
                event.consume();
            }
        });

        quantityTextField.setPrefWidth(130);
        quantityTextField.setAlignment(Pos.CENTER);


        Button plusButton = new Button("+");
        plusButton.setPrefWidth(35);
        plusButton.getStyleClass().add("custom-plus-button"); // Apply a style class to the plusButton
        plusButton.setId("plusButton" + index);
        plusButton.setOnAction(this::handlePlusButton);

        ComboBox<String> statusComboBox = new ComboBox<>();
        statusComboBox.setPrefWidth(180);
        statusComboBox.getStyleClass().add("custom-combobox"); // Apply a style class to the statusComboBox
        statusComboBox.getItems().addAll("Available", "Sold");
        statusComboBox.setValue(resultSet.getString("status"));
        HBox.setMargin(statusComboBox, new Insets(0, 20, 0, 20));

        // Add a listener to the quantity TextField to update the status ComboBox
        quantityTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            int newQuantity = Integer.parseInt(newValue);
            if (newQuantity > 0) {
                statusComboBox.setValue("Available");
            } else {
                statusComboBox.setValue("Sold");
            }
        });


        Button updateButton = new Button("Update");
        updateButton.setPrefWidth(70);
        HBox.setMargin(updateButton, new Insets(0, 0, 0, 20));
        updateButton.getStyleClass().add("custom-update-button"); // Apply a style class to the updateButton
        // Set the handler for the updateButton here...
        updateButton.setOnAction(event -> {
            ButtonType resultConfirm = GetData.showConfirmationAlert("Confirmation message", "Are you sure you want to update this item?");
            if (resultConfirm.equals(ButtonType.OK)) {
                try {
                    int newQuantity = Integer.parseInt(quantityTextField.getText());
                    String newStatus = statusComboBox.getValue();

                    updateDatabase(inventoryId, newQuantity, newStatus);

                    // Optionally, you can provide visual feedback to the user or update the UI components.
                } catch (SQLException e) {
                    e.printStackTrace();
                    // Handle any exceptions that might occur during the update process.
                }
            }

        });

        phoneInfoHBox.getChildren().addAll(
                indexLabel, nameLabel, modelLabel,
                minusButton, quantityTextField, plusButton,
                statusComboBox, updateButton
        );

        return phoneInfoHBox;
    }

    private boolean isNumeric(String str) {
        return str.matches("\\d*"); // Check if the given string contains only digits
    }


    private void updateDatabase(int inventoryId, int newQuantity, String newStatus) throws SQLException {
        String updateQuery = "UPDATE phone_inventory SET quantityInStock = ?, status = ? WHERE inventoryId = ?";
        try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
            updateStatement.setInt(1, newQuantity);
            updateStatement.setString(2, newStatus);
            updateStatement.setInt(3, inventoryId);
            int rowAffected = updateStatement.executeUpdate();
            // if updated success then alert
            if (rowAffected > 0) {
                GetData.showSuccessAlert("Success message", "Updated successfully!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMinusButton(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String buttonId = clickedButton.getId();
        int index = Integer.parseInt(buttonId.substring("minusButton".length()));

        TextField quantityTextField = getQuantityTextField(index);
        int currentQuantity = Integer.parseInt(quantityTextField.getText());
        if (currentQuantity > 0) {
            quantityTextField.setText(String.valueOf(currentQuantity - 1));
        }
    }

    @FXML
    private void handlePlusButton(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String buttonId = clickedButton.getId();
        int index = Integer.parseInt(buttonId.substring("plusButton".length()));

        TextField quantityTextField = getQuantityTextField(index);
        int currentQuantity = Integer.parseInt(quantityTextField.getText());
        quantityTextField.setText(String.valueOf(currentQuantity + 1));
    }

    private TextField getQuantityTextField(int index) {
        return (TextField) phoneContainer.lookup("#quantityTextField" + index);
    }
}
