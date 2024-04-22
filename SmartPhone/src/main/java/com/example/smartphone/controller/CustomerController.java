package com.example.smartphone.controller;



import com.example.smartphone.model.Customer;
import dao.JDBCConnect;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class CustomerController {

    @FXML
    private Label actionStatusLabel;

    @FXML
    private Button addButton;

    @FXML
    private TableColumn<Customer, Integer> orderNumberTableColumn;

    @FXML
    private TableColumn<Customer, String> addressTableColumn;

    @FXML
    private TextField addressTextField;
    @FXML
    private TableView<Customer> customerTableView;

    @FXML
    private TableColumn<Customer, String> deleteTableColumn;

    @FXML
    private TableColumn<Customer, String> emailTableColumn;

    @FXML
    private TableColumn<Customer, Integer> idTableColumn;

    @FXML
    private TextField idTextField;

    @FXML
    private TextField emailTextField;


    @FXML
    private TableColumn<Customer, String> phoneColumnTable;

    @FXML
    private TextField nameTextField;

    @FXML
    private Pagination paginationPagination;

    @FXML
    private TableColumn<Customer, String> phoneTableColumn;

    @FXML
    private TextField phoneTextField;

    @FXML
    private TextField searchKeywordTextField;

    @FXML
    private Button updateButton;

    @FXML
    private ComboBox<String> searchComboBox;

    @FXML
    private Label totalLabel;

    @FXML
    private DatePicker dobDatePicker;

    @FXML
    private TableColumn<Customer, String> dobTableColumn;



    private int currentPage = 1;
    Connection connection = JDBCConnect.getJDBCConnection();

    ObservableList<Customer> customerObservableList = FXCollections.observableArrayList();
    private final int itemsPerPage = 12;

    public void initialize() {
        setupTable();
        setupPagination();
        setIdAdd();
        selectedRecord();
        addSearchByToComboBox();

        phoneTextField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!isNumeric(event.getCharacter())) {
                event.consume();
            }
        });
    }

    private boolean isNumeric(String str) {
        return str.matches("\\d*"); // Check if the given string contains only digits
    }

    /**
     * Populates the searchComboBox with search criteria for filtering customers.
     * The ComboBox contains options like "Name", "Email", and "Phone" for searching customers based on these criteria.
     */
    private void addSearchByToComboBox() {
        searchComboBox.getItems().addAll("Name", "Email", "Phone");
        searchComboBox.setValue("Name");
    }

    /**
     * Retrieves a list of customers from the database and returns it as an ObservableList.
     * Each customer in the list is represented by the Customer model class.
     *
     * @return The ObservableList of Customer objects fetched from the database.
     */
    private ObservableList<Customer> getCustomerObservableList() {
        ObservableList<Customer> observableList = FXCollections.observableArrayList();
        String sql = "SELECT customerId, customerName,dob, phoneNumber,email, address FROM customer ORDER BY customerId";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("customerId");
                String name = resultSet.getString("customerName");
                String phone = resultSet.getString("phoneNumber");
                String address = resultSet.getString("address");
                String email = resultSet.getString("email");
                String dob = resultSet.getString("dob");

                // add to list
                observableList.add(new Customer(id, name, dob, phone, email, address));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return observableList;
    }

    /**
     * Sets up the customer table with the data fetched from the database.
     * It populates the table columns with cell factories and binds the data to the TableView.
     * The method also configures the delete button cell in the table to handle deletion of customers.
     */
    private void setupTable() {
        customerObservableList = getCustomerObservableList();
        idTableColumn.setCellValueFactory(new PropertyValueFactory<>("phoneId"));
        phoneColumnTable.setCellValueFactory(new PropertyValueFactory<>("phoneName"));
        dobTableColumn.setCellValueFactory(new PropertyValueFactory<>("dob"));
        phoneTableColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressTableColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        emailTableColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        orderNumberTableColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(customerTableView.getItems().indexOf(param.getValue()) + 1 + (currentPage - 1) * itemsPerPage));

        totalLabel.setText("Total: " + customerObservableList.size());

        // Create a custom cell factory for the dobTableColumn
        dobTableColumn.setCellFactory(new Callback<TableColumn<Customer, String>, TableCell<Customer, String>>() {
            @Override
            public TableCell<Customer, String> call(TableColumn<Customer, String> column) {
                return new TableCell<Customer, String>() {
                    private final DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                    private final DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null && !empty) {
                            try {
                                Date date = inputFormat.parse(item);
                                setText(outputFormat.format(date));
                            } catch (ParseException e) {
                                setText("");
                            }
                        } else {
                            setText("");
                        }
                    }
                };
            }
        });

        // create a table cell to contain delete button
        Callback<TableColumn<Customer, String>, TableCell<Customer, String>> columnTableCellCallback = (param) -> {
            final TableCell<Customer, String> deleteCell = new TableCell<>() { // create table cell
                @Override
                public void updateItem(String item, boolean isEmpty) {
                    super.updateItem(item, isEmpty);
                    // if the cell is not empty, the button will not be displayed
                    if (isEmpty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        final Button deleteButton = new Button("Delete"); // create new button
                        deleteButton.getStyleClass().add("delete-button");
                        deleteButton.setOnAction(event -> {
                            Customer customer = getTableView().getItems().get(getIndex());

                            ButtonType resultConfirm = GetData.showConfirmationAlert("Confirmation message", "Are you sure you want to delete?");
                            // if user confirm delete then delete
                            if (resultConfirm.equals(ButtonType.OK)) {
                                deleteCustomerFromDatabase(customer.getId());
                            }
                        });
                        setGraphic(deleteButton);
                        setText(null);
                    }
                }
            };
            return deleteCell;
        };
        deleteTableColumn.setCellFactory(columnTableCellCallback);

        // create filter list
        FilteredList<Customer> filteredList = new FilteredList<>(customerObservableList, b -> true);

        // listen to the changes in the searchKeyword to update table view
        searchKeywordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(customer -> {
                if (newValue == null || newValue.trim().isEmpty()) {
                    totalLabel.setText("Total: " + customerObservableList.size());
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();
                String searchBy = searchComboBox.getValue().toLowerCase();

                if (searchBy.equals("name") && customer.getName().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (searchBy.equals("phone") && customer.getPhone().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (searchBy.equals("email") && customer.getEmail().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else {
                    totalLabel.setText("Total: " + filteredList.size());
                    return false;
                }
            });
            // update pagination
            updatePagination(filteredList);
        });
        // update pagination
        updatePagination(filteredList);
        updateButton.setDisable(true);
    }

    /**
     * Sets up the pagination control for the customer table.
     * Calculates the total number of pages and updates the current page when pagination changes.
     * It also updates the table data based on the current page index.
     */
    private void setupPagination() {
        int totalPages = (customerObservableList.size() / itemsPerPage) + (customerObservableList.size() % itemsPerPage > 0 ? 1 : 0);
        paginationPagination.setPageCount(totalPages); // set page count for pagination

        paginationPagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
            updateTableData(newValue.intValue());
        });
    }

    /**
     * Updates the pagination control based on the filtered customer list.
     * It adjusts the page count and current page index to reflect the filtered data.
     *
     * @param filteredList The filtered list of customers based on the search keyword.
     */
    private void updatePagination(FilteredList<Customer> filteredList) {
        int totalItems = filteredList.size();
        int pageCount = (totalItems + itemsPerPage - 1) / itemsPerPage;

        if (pageCount == 0) {
            pageCount = 1;
        }

        paginationPagination.setPageCount(pageCount);
        if (paginationPagination.getCurrentPageIndex() >= pageCount) {
            paginationPagination.setCurrentPageIndex(pageCount - 1);
        }

        // update table view base on current page
        int fromIndex = paginationPagination.getCurrentPageIndex() * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, totalItems);

        SortedList<Customer> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(customerTableView.comparatorProperty());

        customerTableView.setItems(FXCollections.observableArrayList(sortedList.subList(fromIndex, toIndex)));

    }

    /**
     * Updates the customer table data based on the selected page index in the pagination control.
     * It displays the appropriate subset of customers for the current page.
     *
     * @param pageIndex The index of the current page in the pagination control.
     */
    private void updateTableData(int pageIndex) {
        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, customerObservableList.size());

        // Clear the table and re-add the events for the current page
        customerTableView.getItems().clear();
        customerTableView.getItems().addAll(customerObservableList.subList(fromIndex, toIndex));
    }


    /**
     * Deletes a customer record from the database based on the provided customerId.
     * It checks if the customerId is related to other tables before deleting the record.
     * If there are no related records, the method deletes the customer record and updates the customer table.
     *
     * @param id The ID of the customer record to be deleted.
     */
    private void deleteCustomerFromDatabase(int id) {
        try {
            // Xóa tất cả các đơn hàng liên quan đến khách hàng
            String deleteOrdersQuery = "DELETE FROM `order` WHERE customerId = " + id;
            Statement deleteOrdersStatement = connection.createStatement();
            int rowsDeleted = deleteOrdersStatement.executeUpdate(deleteOrdersQuery);

            // Sau khi xóa đơn hàng, xóa khách hàng
            if (rowsDeleted > 0) {
                String deleteCustomerQuery = "DELETE FROM customer WHERE customerId = " + id;
                Statement deleteCustomerStatement = connection.createStatement();
                int rowsAffected = deleteCustomerStatement.executeUpdate(deleteCustomerQuery);

                if (rowsAffected > 0) {
                    GetData.showSuccessAlert("Success message", "Deleted successfully!");
                    setupTable();
                    resetForm();
                }
            } else {
                GetData.showErrorAlert("Error message", "Customer has orders and cannot be deleted!");
            }
        } catch (Exception e) {
            GetData.showErrorAlert("Error message", "Cannot delete!");
            e.printStackTrace();
        }
    }


    /**
     * Sets the next available customerId in the ID TextField for adding a new customer record.
     * It queries the database to get the last customerId and increments it by one.
     * The method sets the new customerId in the ID TextField.
     *
     * @return The next available customerId for adding a new customer record.
     */
    private int setIdAdd() {
        String sql = "SELECT customerId FROM customer ORDER BY customerID DESC LIMIT 1";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt(1) + 1;
                idTextField.setText(String.valueOf(id));
                return id;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Adds a new customer record to the database based on the data entered in the form.
     * It inserts a new customer record with name, phone, address, and email.
     * The method also updates the customer table and resets the form after successful insertion.
     */
    public void addCustomerToDatabase() {
        String sql = "INSERT INTO customer(customerName, phoneNumber, address,email,dob) VALUES (?,?,?,?,?)";
        try {
            if (isFilledFields() && validateEmail() && validatePhoneNumber()) {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, nameTextField.getText());
                preparedStatement.setString(2, phoneTextField.getText());
                preparedStatement.setString(3, addressTextField.getText());
                preparedStatement.setString(4, emailTextField.getText());
                preparedStatement.setString(5, dobDatePicker.getValue().toString());

                preparedStatement.executeUpdate();

                GetData.showSuccessAlert("Success message", "Added successfully!");

                // update table view and reset form
                setupTable();
                resetForm();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates an existing customer record in the database based on the data entered in the form.
     * It updates the customer record with the new name, phone, address, and email.
     * The method also updates the customer table and resets the form after successful update.
     */
    public void updateCustomerToDatabase() {
        String sql = "UPDATE customer SET customerName = ?,phoneNumber = ?, address = ?, email = ?,dob =? WHERE customerId = ?";
        try {
            if (isFilledFields() && validatePhoneNumber() && validateEmail()) {
                ButtonType resultConfirm = GetData.showConfirmationAlert("Confirmation message", "Are you sure you want to update?");
                // if user confirm delete then delete
                if (resultConfirm.equals(ButtonType.OK)) {
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, nameTextField.getText());
                    preparedStatement.setString(2, phoneTextField.getText());
                    preparedStatement.setString(3, addressTextField.getText());
                    preparedStatement.setString(4, emailTextField.getText());
                    preparedStatement.setString(5, dobDatePicker.getValue().toString());
                    preparedStatement.setString(6, idTextField.getText());

                    preparedStatement.executeUpdate();

                    GetData.showSuccessAlert("Success message", "Updated successfully!");

                    // update table view and reset form
                    setupTable();
                    resetForm();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Resets the form by clearing the input fields and resetting the action status.
     * It sets the form to add mode and enables the add button while disabling the update button.
     */
    public void resetForm() {
        searchKeywordTextField.clear();
        setIdAdd();
        nameTextField.clear();
        phoneTextField.clear();
        addressTextField.clear();
        emailTextField.clear();
        dobDatePicker.setValue(null);
        dobDatePicker.getEditor().clear();

        actionStatusLabel.setText("Adding New Customer");
        addButton.setDisable(false);
        updateButton.setDisable(true);
    }

    /**
     * Listens to the selected customer record in the customerTableView and populates the form fields with the data.
     * When a customer record is selected, it updates the form fields with the customer details for editing.
     * The method also sets the action status to "Updating Customer" and enables the update button.
     */
    private void selectedRecord() {
        customerTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Customer>() {
            @Override
            public void changed(ObservableValue<? extends Customer> observableValue, Customer oldValue, Customer newValue) {
                if (newValue != null) {
                    idTextField.setText(String.valueOf(newValue.getId()));
                    nameTextField.setText(String.valueOf(newValue.getName()));
                    phoneTextField.setText(String.valueOf(newValue.getPhone()));
                    addressTextField.setText(newValue.getAddress());
                    emailTextField.setText(String.valueOf(newValue.getEmail()));

                    // Set value for the dobDatePicker
                    String dobString = newValue.getDob();
                    if (dobString != null && !dobString.isEmpty()) {
                        LocalDate dob = LocalDate.parse(dobString); // Assuming dobString is in the format "yyyy-MM-dd"
                        dobDatePicker.setValue(dob);
                    } else {
                        dobDatePicker.setValue(null); // Clear the date picker if dobString is empty or null
                    }


                    addButton.setDisable(true);
                    actionStatusLabel.setText("Updating Customer");
                    updateButton.setDisable(false);
                } else {
                    resetForm();
                }
            }
        });
    }

    private boolean isFilledFields() {
        if (nameTextField.getText().isEmpty()
                || phoneTextField.getText().isEmpty()
                || emailTextField.getText().isEmpty()
                || dobDatePicker.getEditor().getText().isEmpty()
                || addressTextField.getText().isEmpty()) {
            GetData.showWarningAlert("Warning message", "Please fill all required fields!");
            return false;
        } else {
            return true;
        }
    }

    @FXML
    void filterByBirthdayAction(ActionEvent event) {
        // Get the current date
        LocalDate today = LocalDate.now();

        // Filter the distributors whose birthday is today
        FilteredList<Customer> filteredList = customerObservableList.filtered(
                customer -> {
                    LocalDate birthday = LocalDate.parse(customer.getDob());
                    return birthday.getMonth() == today.getMonth() && birthday.getDayOfMonth() == today.getDayOfMonth();
                }
        );

        // Update the table view with the filtered list
        updatePagination(filteredList);
    }

    private boolean validatePhoneNumber() {
        String phoneNumber = phoneTextField.getText().trim();
        if (!Pattern.compile("^0\\d{9}$").matcher(phoneNumber).matches()) {
            GetData.showWarningAlert("Validation Error", "Phone number must start with '0' and be 10 digits long.");
            return false;
        }
        return true;
    }

    private boolean validateEmail() {
        String email = emailTextField.getText().trim();
        if (!Pattern.compile("\\S+@\\S+\\.\\S+").matcher(email).matches()) {
            GetData.showWarningAlert("Validation Error", "Invalid email format.");
            return false;
        }
        return true;
    }
}
