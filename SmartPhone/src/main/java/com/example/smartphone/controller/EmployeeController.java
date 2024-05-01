package com.example.smartphone.controller;

import com.example.smartphone.model.Employee;
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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

public class EmployeeController {
    @FXML
    private TableColumn<Employee, String> birthdayTableColumn;

    @FXML
    private DatePicker birthdayDatePicker;

    @FXML
    private Button filterBirthdayButton;

    @FXML
    private Button sendBirthdayMailButton;
    @FXML
    private Label actionStatusLabel;

    @FXML
    private Button addButton;

    @FXML
    private TableColumn<Employee, String> addressTableColumn;

    @FXML
    private TableColumn<Employee, String> roleTableColumn;

    @FXML
    private TextField addressTextField;

    @FXML
    private TableColumn<Employee, Integer> orderNumberTableColumn;



    @FXML
    private TableColumn<Employee, String> deleteTableColumn;

    @FXML
    private TableColumn<Employee, String> emailTableColumn;

    @FXML
    private TextField emailTextField;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private TableView<Employee> employeeTableView;

    @FXML
    private TableColumn<Employee, Integer> idTableColumn;

    @FXML
    private TextField idTextField;


    @FXML
    private TableColumn<Employee, String> phoneColumnTable;

    @FXML
    private TableColumn<Employee, String> userphoneColumnTable;

    @FXML
    private TextField nameTextField;

    @FXML
    private Pagination paginationPagination;

    @FXML
    private TableColumn<Employee, String> phoneTableColumn;

    @FXML
    private TextField phoneTextField;

    @FXML
    private TextField usernameTextField;

    @FXML
    private TableColumn<Employee, Double> basicSalaryTableColumn;

    @FXML
    private TableColumn<Employee, String> joinDateTableColumn;

    @FXML
    private TextField salaryTextField;

    @FXML
    private TextField searchKeywordTextField;

    @FXML
    private Button updateButton;

    @FXML
    private DatePicker joinDateDatePicker;

    @FXML
    private ComboBox<String> searchComboBox;

    @FXML
    private Label totalLabel;


    private int currentPage = 1;
    Connection connection = JDBCConnect.getJDBCConnection();

    ObservableList<Employee> employeeObservableList = FXCollections.observableArrayList();
    private final int itemsPerPage = 12;

    public void initialize() {
        if (GetData.role.equalsIgnoreCase("admin") || GetData.role.equalsIgnoreCase("manager")) {
            addButton.setDisable(false);
            updateButton.setDisable(false);
        } else {
            addButton.setDisable(true);
            updateButton.setDisable(true);
        }
        setupTable();
        setupPagination();
        setIdAdd();
        selectedRecord();
        addSearchByToComboBox();
        addRoleToComboBox();
        joinDateDatePicker.setValue(LocalDate.now());

        phoneTextField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!isNumeric(event.getCharacter())) {
                event.consume();
            }
        });

        salaryTextField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!isNumericSalary(event.getCharacter())) {
                event.consume();
            }
        });

    }

    private boolean isNumericSalary(String str){
        return str.matches("\\d*(\\.\\d*)?");
    }

    private boolean isNumeric(String str) {
        return str.matches("\\d*"); // Check if the given string contains only digits
    }


    private void addSearchByToComboBox() {
        searchComboBox.getItems().addAll("Name", "Username", "Role", "Phone", "Email");
        searchComboBox.setValue("Name");
    }

    /**
     * Gets data from the database and creates an ObservableList of Distributor objects.
     * This method retrieves distributor information from the database and creates an ObservableList
     * to store the Distributor objects for display in the table view.
     *
     * @return The ObservableList of Distributor objects containing data from the database.
     */
    private ObservableList<Employee> getEmployeeObservableList() {
        ObservableList<Employee> observableList = FXCollections.observableArrayList();
        String sql = "SELECT e.employeeId,e.employeeName,e.phoneNumber,e.email,e.birthday,e.address,r.roleName,e.salary,e.username,e.joinDate " +
                "FROM employee e " +
                "JOIN role r ON e.roleId =r.roleId ORDER BY employeeId";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("employeeId");
                String name = resultSet.getString("employeeName");
                String phone = resultSet.getString("phoneNumber");
                String email = resultSet.getString("email");
                String address = resultSet.getString("address");
                String role = resultSet.getString("roleName");
                double salary = resultSet.getDouble("salary");
                String joinDate = resultSet.getString("joinDate");
                String username = resultSet.getString("username");
                String birthday = resultSet.getString("birthday");

                // add to list
                observableList.add(new Employee(id, name, username, role, phone, email, salary, joinDate, address, birthday));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return observableList;
    }

    /**
     * Sets up the table view with the Distributor data and defines the columns.
     * This method sets up the table view with data from the database and defines the columns
     * for id, name, phone, address, and email. It also sets up the delete button column and handles its actions.
     */
    private void setupTable() {
        employeeObservableList = getEmployeeObservableList();
        idTableColumn.setCellValueFactory(new PropertyValueFactory<>("phoneId"));
        phoneColumnTable.setCellValueFactory(new PropertyValueFactory<>("phoneName"));
        roleTableColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        phoneTableColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailTableColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        basicSalaryTableColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));
        addressTableColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        userphoneColumnTable.setCellValueFactory(new PropertyValueFactory<>("username"));
        joinDateTableColumn.setCellValueFactory(new PropertyValueFactory<>("joinDate"));
        birthdayTableColumn.setCellValueFactory(new PropertyValueFactory<>("birthday"));
        orderNumberTableColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(employeeTableView.getItems().indexOf(param.getValue()) + 1 + (currentPage - 1) * itemsPerPage));

//        salaryTableColumn.setCellValueFactory(new PropertyValueFactory<>());
        totalLabel.setText("Total: " + employeeObservableList.size());

        birthdayTableColumn.setCellFactory(new Callback<TableColumn<Employee, String>, TableCell<Employee, String>>() {
            @Override
            public TableCell<Employee, String> call(TableColumn<Employee, String> column) {
                return new TableCell<Employee, String>() {
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
        Callback<TableColumn<Employee, String>, TableCell<Employee, String>> columnTableCellCallback = (param) -> {
            final TableCell<Employee, String> deleteCell = new TableCell<>() { // create table cell
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
                        if (GetData.role.equalsIgnoreCase("admin") || GetData.role.equalsIgnoreCase("manager")) {
                            deleteButton.setDisable(false);
                        } else {
                            deleteButton.setDisable(true);
                        }
                        deleteButton.setOnAction(event -> {
                            Employee employee = getTableView().getItems().get(getIndex());

                            ButtonType resultConfirm = GetData.showConfirmationAlert("Confirmation message", "Are you sure you want to delete?");
                            // if user confirm delete then delete
                            if (resultConfirm.equals(ButtonType.OK)) {
                                deleteEmployeeFromDatabase(employee.getId());
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
        FilteredList<Employee> filteredList = new FilteredList<>(employeeObservableList, b -> true);

        // listen to the changes in the searchKeyword to update table view
        searchKeywordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(employee -> {
                if (newValue == null || newValue.trim().isEmpty()) {
                    totalLabel.setText("Total: " + employeeObservableList.size());
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();
                String searchBy = searchComboBox.getValue().toLowerCase();

                if (searchBy.equals("name") && employee.getName().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (searchBy.equals("username") && employee.getUsername().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (searchBy.equals("phone") && employee.getPhone().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (searchBy.equals("email") && employee.getEmail().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (searchBy.equals("role") && employee.getRole().toLowerCase().contains(searchKeyword)) {
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
        if (GetData.role.equalsIgnoreCase("admin") || GetData.role.equalsIgnoreCase("manager")) {
            updateButton.setDisable(true);
        }
    }

    /**
     * Sets up the pagination for the table view.
     * This method calculates the number of pages needed for pagination based on the number of items
     * in the filtered list. It updates the pagination controls and table view data when the page changes.
     */
    private void setupPagination() {
        int totalPages = (employeeObservableList.size() / itemsPerPage) + (employeeObservableList.size() % itemsPerPage > 0 ? 1 : 0);
        paginationPagination.setPageCount(totalPages); // set page count for pagination

        paginationPagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
            updateTableData(newValue.intValue());
        });
    }

    /**
     * Updates the pagination controls and table view data based on the filtered list.
     * This method updates the pagination controls and table view data based on the filtered list
     * using the current search keyword and search by criteria.
     *
     * @param filteredList The filtered list containing Distributor objects based on the search criteria.
     */
    private void updatePagination(FilteredList<Employee> filteredList) {
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

        SortedList<Employee> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(employeeTableView.comparatorProperty());

        employeeTableView.setItems(FXCollections.observableArrayList(sortedList.subList(fromIndex, toIndex)));

    }

    /**
     * Updates the data displayed in the table view based on the current pagination page index.
     * This method updates the data displayed in the table view based on the current pagination
     * page index. It fetches the appropriate data from the ObservableList and sets it in the table view.
     *
     * @param pageIndex The index of the current pagination page.
     */
    private void updateTableData(int pageIndex) {
        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, employeeObservableList.size());

        // Clear the table and re-add the events for the current page
        employeeTableView.getItems().clear();
        employeeTableView.getItems().addAll(employeeObservableList.subList(fromIndex, toIndex));
    }

    private Map<Integer, String> getRoleMap() {
        Map<Integer, String> roleMap = new HashMap<>();
        String sql = "SELECT *FROM `role`";
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

    private void addRoleToComboBox() {
        Map<Integer, String> roleMap = getRoleMap();
        roleComboBox.getItems().addAll(roleMap.values());
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

    /**
     * Deletes a distributor from the database based on its ID.
     * This method deletes a distributor from the database based on its ID and updates the table view accordingly.
     *
     * @param id The ID of the distributor to be deleted.
     */
    private void deleteEmployeeFromDatabase(int id) {
//        String updateInvoiceQuery = "UPDATE invoice SET employeeId = NULL WHERE employeeId = ?";
        String updateOrderQuery = "UPDATE `order` SET employeeId = NULL WHERE employeeID = ?";
        String deleteEmployeeQuery = "DELETE FROM employee WHERE employeeId = ?";

        try {
            // Update invoice
//            try (PreparedStatement updateInvoiceStatement = connection.prepareStatement(updateInvoiceQuery)) {
//                updateInvoiceStatement.setInt(1, id);
//                int rowUpdateInvoiceAffected = updateInvoiceStatement.executeUpdate();

                // Update order
                try (PreparedStatement updateOrderStatement = connection.prepareStatement(updateOrderQuery)) {
                    updateOrderStatement.setInt(1, id);
                    int rowUpdateOrderAffected = updateOrderStatement.executeUpdate();

                    // Delete employee
//                    if (rowUpdateInvoiceAffected > 0 && rowUpdateOrderAffected > 0) {
                        try (PreparedStatement deleteEmployeeStatement = connection.prepareStatement(deleteEmployeeQuery)) {
                            deleteEmployeeStatement.setInt(1, id);
                            int rowAffected = deleteEmployeeStatement.executeUpdate();

                            if (rowAffected > 0) {
                                GetData.showSuccessAlert("Success message", "Deleted successfully!");
                                setupTable();
                                resetForm();
                            }
                        }
//                    }
                }
//            }
        } catch (Exception e) {
            GetData.showErrorAlert("Error message", "Cannot delete!");
            e.printStackTrace();
        }
    }


    /**
     * Sets the ID for adding a new distributor by auto-incrementing the ID.
     * This method sets the ID for adding a new distributor by auto-incrementing the ID
     * based on the maximum distributor ID in the database.
     *
     * @return The newly generated ID for adding a new distributor.
     */
    private int setIdAdd() {
        String sql = "SELECT employeeId FROM employee ORDER BY employeeID DESC LIMIT 1";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
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
     * Checks if all required fields in the form are filled.
     *
     * @return true if all required fields are filled, false otherwise.
     */
    private boolean isFilledFields() {
        if (nameTextField.getText().isEmpty()
                || roleComboBox.getValue() == null
                || emailTextField.getText().isEmpty()
                || salaryTextField.getText().isEmpty()
                || phoneTextField.getText().isEmpty()
                || birthdayDatePicker.getEditor().getText().isEmpty()
                || addressTextField.getText().isEmpty()
                || joinDateDatePicker.getValue() == null) {
            GetData.showWarningAlert("Warning", "Please fill in all required fields correctly!");
            return false;
        } else {
            return true;
        }
    }

    private boolean validateFields() {
        if (!validateUsername() || !validatePhoneNumber() || !validateEmail() || !validateBasicSalary()) {
            return false;
        }
        return true;
    }

    private boolean validateUsername() {
        String username = usernameTextField.getText().trim();
        if (username.isEmpty() || username.contains(" ")) {
            GetData.showWarningAlert("Validation Error", "Username cannot be empty and should not contain spaces.");
            return false;
        }
        return true;
    }

    private boolean validateBasicSalary() {
        double salary = Double.parseDouble(salaryTextField.getText());
        if (salary < 0) {
            GetData.showWarningAlert("Validation Error", "Basic salary must be a number and greater than 0.");
            return false;
        } else {
            return true;
        }
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

    /**
     * Adds a new distributor to the database based on the input fields.
     * This method adds a new distributor to the database based on the input fields and updates the table view accordingly.
     * It performs validation to ensure all required fields are filled before adding the distributor.
     */
    public void addEmployeeToDatabase() {
//        String password ="";
        String sql = "INSERT INTO employee(employeeName, roleId,phoneNumber,email,salary,address,joinDate,password,username,birthday) VALUES (?,?,?,?,?,?,?,SHA2(?, 256),?,?)";
        try {
            if (isFilledFields() && validateFields()) {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, nameTextField.getText());
                Map<Integer, String> roleMap = getRoleMap();
                String selectedRole = roleComboBox.getValue();
                int selectedId = getKeyFromValue(roleMap, selectedRole);
                preparedStatement.setInt(2, selectedId);
                preparedStatement.setString(3, phoneTextField.getText());
                preparedStatement.setString(4, emailTextField.getText());
                preparedStatement.setString(5, salaryTextField.getText());
                preparedStatement.setString(6, addressTextField.getText());
                preparedStatement.setString(7, joinDateDatePicker.getValue().toString());
                preparedStatement.setString(8, "12345678");
                preparedStatement.setString(9, usernameTextField.getText());
                preparedStatement.setString(10, birthdayDatePicker.getValue().toString());


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
     * Updates the selected distributor record in the database based on the input fields.
     * This method updates the selected distributor record in the database based on the input fields and updates the table view accordingly.
     * It performs validation to ensure all required fields are filled before updating the distributor.
     */
    public void updateEmployeeToDatabase() {
        String sql = "UPDATE employee SET employeeName = ?,roleId = ? ,phoneNumber = ?,email = ?, salary = ? , address = ?,username = ?,joinDate = ?, birthday = ? WHERE employeeId = ?";
        try {
            if (isFilledFields() && validateFields()) {
                ButtonType resultConfirm = GetData.showConfirmationAlert("Confirmation message", "Are you sure you want to update?");
                // if user confirm delete then delete
                if (resultConfirm.equals(ButtonType.OK)) {
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, nameTextField.getText());
                    Map<Integer, String> roleMap = getRoleMap();
                    String selectedRole = roleComboBox.getValue();
                    int selectedId = getKeyFromValue(roleMap, selectedRole);
                    preparedStatement.setInt(2, selectedId);
                    preparedStatement.setString(3, phoneTextField.getText());
                    preparedStatement.setString(4, emailTextField.getText());
                    preparedStatement.setString(5, salaryTextField.getText());
                    preparedStatement.setString(6, addressTextField.getText());
                    preparedStatement.setString(7, usernameTextField.getText());
                    preparedStatement.setString(8, joinDateDatePicker.getValue().toString());
                    preparedStatement.setString(9, birthdayDatePicker.getValue().toString());
                    preparedStatement.setString(10, idTextField.getText());

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
     * Resets the form by clearing the input fields and enabling the add button.
     * This method resets the form by clearing the input fields, enabling the add button, and updating the action status label.
     */
    public void resetForm() {
        searchKeywordTextField.clear();
        setIdAdd();
        nameTextField.clear();
        roleComboBox.setValue(null);
        emailTextField.clear();
        salaryTextField.clear();
        phoneTextField.clear();
        addressTextField.clear();
        usernameTextField.clear();
        joinDateDatePicker.setValue(LocalDate.now());
        birthdayDatePicker.setValue(null);
        birthdayDatePicker.getEditor().clear();

        actionStatusLabel.setText("Adding New employee");
        if (GetData.role.equalsIgnoreCase("admin") || GetData.role.equalsIgnoreCase("manager")) {
            addButton.setDisable(false);
        }
        updateButton.setDisable(true);
    }

    /**
     * Listens for the selection of a record in the table view and updates the form accordingly.
     * This method listens for the selection of a record in the table view and updates the form
     * with the selected distributor's information for updating or editing purposes.
     */
    private void selectedRecord() {
        employeeTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Employee>() {
            @Override
            public void changed(ObservableValue<? extends Employee> observableValue, Employee oldValue, Employee newValue) {
                if (newValue != null) {
                    idTextField.setText(String.valueOf(newValue.getId()));
                    nameTextField.setText(String.valueOf(newValue.getName()));
                    phoneTextField.setText(String.valueOf(newValue.getPhone()));
                    addressTextField.setText(newValue.getAddress());
                    emailTextField.setText(newValue.getEmail());
                    salaryTextField.setText(String.valueOf(newValue.getSalary()));
                    usernameTextField.setText(String.valueOf(newValue.getUsername()));
                    roleComboBox.setValue(newValue.getRole());

                    // Set value for the birthdayDatePicker
                    String birthdayStr = newValue.getBirthday();
                    if (birthdayStr != null && !birthdayStr.isEmpty()) {
                        LocalDate dob = LocalDate.parse(birthdayStr); // Assuming birthdayDatePicker is in the format "yyyy-MM-dd"
                        birthdayDatePicker.setValue(dob);
                    } else {
                        birthdayDatePicker.setValue(null); // Clear the date picker if birthdayDatePicker is empty or null
                    }

                    // Sample date string retrieved from the database (format: "yyyy-MM-dd")
                    String dateStringFromDatabase = String.valueOf(newValue.getJoinDate());

                    // Convert the database date string to LocalDate
                    DateTimeFormatter dateFormatterFromDatabase = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate localDateFromDatabase = LocalDate.parse(dateStringFromDatabase, dateFormatterFromDatabase);

                    // Format the LocalDate to the desired format ("MM/dd/yyyy")
                    DateTimeFormatter dateFormatterToDisplay = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                    String formattedDateString = localDateFromDatabase.format(dateFormatterToDisplay);

                    // Set the formatted date to the DatePicker
                    joinDateDatePicker.getEditor().setText(formattedDateString);

                    addButton.setDisable(true);
                    actionStatusLabel.setText("Updating employee");
                    if (GetData.role.equalsIgnoreCase("admin") || GetData.role.equalsIgnoreCase("manager")) {
                        updateButton.setDisable(false);
                    }
                } else {
                    resetForm();
                }
            }
        });
    }

    @FXML
    void filterByBirthdayAction(ActionEvent event) {
        // Get the current date
        LocalDate today = LocalDate.now();

        // Filter the distributors whose birthday is today
        FilteredList<Employee> filteredList = employeeObservableList.filtered(
                employee -> {
                    LocalDate birthday = LocalDate.parse(employee.getBirthday()); // Assuming the birthday is stored in "yyyy-MM-dd" format
                    return birthday.getMonth() == today.getMonth() && birthday.getDayOfMonth() == today.getDayOfMonth();
                }
        );

        // Update the table view with the filtered list
        updatePagination(filteredList);
    }


    @FXML
    void sendBirthdayMailAction(ActionEvent event) {
        ButtonType resultConfirm = GetData.showConfirmationAlert("Confirmation message", "Are you sure you want to send mail?");
//        // if user confirm send mail then send mail
        if (resultConfirm.equals(ButtonType.OK)) {
            sendMail();
        }
    }

    private void sendMail() {
        String subject = "Happy Birthday!";
        String sql = "SELECT email " +
                "FROM employee " +
                "WHERE DATE_FORMAT(birthday, '%m-%d') = DATE_FORMAT(NOW(), '%m-%d');";

        try {
            Statement statement = connection.createStatement();
            // Read the message template
            String message = "<html>" +
                    "<head>" +
                    "    <style>" +
                    "        .container {" +
                    "            max-width: 600px;" +
                    "            margin: 0 auto;" +
                    "            padding: 20px;" +
                    "            background-color: #ffffff;" +
                    "            text-align: justify;" +
                    "        }" +
                    "    </style>" +
                    "</head>" +
                    "<body>" +
                    "    <div class='container'>" +
                    "        <img src=\"https://media.istockphoto.com/id/1410736450/vector/happy-birthday-beautiful-greeting-Phoned-scratched-calligraphy-black-text-word-golden-line.jpg?b=1&s=612x612&w=0&k=20&c=C2cwLTDz2aFEyDjDkJpd7ZWoQvMkHhWJFKo3WlmT6uI=\" width=\"600px\">" +
                    "        <div class='content'>" +
                    "            <b>Hi you,</b>" +
                    "            <p>A little birdie told us that today was your special day. On behalf of everyone at PhoneShop, we hope you" +
                    "                have the best birthday ever.</p>" +
                    "            <p>May your day be the start of a year filled with good luck, good health and much happiness. We look" +
                    "                forward to continuing to help you achieve your wellness goals this year.</p>" +
                    "            <p>Have an unforgettable birthday!</p>" +
                    "            <p>Sincerely,</p>" +
                    "            <p>Phone Shop</p>" +
                    "        </div>" +
                    "    </div>" +
                    "</body>" +
                    "</html>";
            ResultSet resultSet = statement.executeQuery(sql);
            List<String> mailList = new ArrayList<>();
            while (resultSet.next()) {
                mailList.add(resultSet.getString("email"));
            }

            if (mailList.size() == 0) {
                GetData.showWarningAlert("Warning message", "No employee whose birthday is today");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
