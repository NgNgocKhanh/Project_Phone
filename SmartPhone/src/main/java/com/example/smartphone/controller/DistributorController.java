package com.example.smartphone.controller;

import com.example.smartphone.model.Distributor;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class DistributorController {
    @FXML
    private ComboBox<String> searchComboBox;

    @FXML
    private TableColumn<Distributor, String> birthdayTableColumn;

    @FXML
    private DatePicker birthdayDatePicker;
    @FXML
    private Label totalLabel;

    @FXML
    private Label actionStatusLabel;

    @FXML
    private Button addButton;

    @FXML
    private TableColumn<Distributor, String> addressTableColumn;

    @FXML
    private TableColumn<Distributor, String> emailTableColumn;

    @FXML
    private TextField addressTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TableColumn<Distributor, String> deleteTableColumn;

    @FXML
    private TableView<Distributor> distributorTableView;

    @FXML
    private TableColumn<Distributor, Integer> idTableColumn;

    @FXML
    private TextField idTextField;

    @FXML
    private TableColumn<Distributor, String> nameTableColumn;

    @FXML
    private TextField nameTextField;

    @FXML
    private Pagination paginationPagination;

    @FXML
    private TableColumn<Distributor, String> phoneTableColumn;

    @FXML
    private TextField phoneTextField;

    @FXML
    private TextField searchKeywordTextField;

    @FXML
    private TableColumn<Distributor, Integer> orderNumberTableColumn;

    @FXML
    private Button updateButton;

    @FXML
    private Button filterBirthdayButton;

    @FXML
    private Button sendBirthdayMailButton;

    private int currentPage = 1;
    Connection connection = JDBCConnect.getJDBCConnection();
    ObservableList<Distributor> distributorObservableList = FXCollections.observableArrayList();
    private final int itemsPerPage = 12;

    public void initialize() {
        setIdAdd();
        setupTable();
        setupPagination();
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


    private void addSearchByToComboBox() {
        searchComboBox.getItems().addAll("Name", "Phone", "Email");
        searchComboBox.setValue("Name");
    }

    /**
     * Gets data from the database and creates an ObservableList of Distributor objects.
     * This method retrieves distributor information from the database and creates an ObservableList
     * to store the Distributor objects for display in the table view.
     *
     * @return The ObservableList of Distributor objects containing data from the database.
     */
    private ObservableList<Distributor> getDistributorObservableList() {
        ObservableList<Distributor> observableList = FXCollections.observableArrayList();
        String sql = "SELECT *FROM distributor";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("distributorId");
                String name = resultSet.getString("distributorName");
                String phone = resultSet.getString("phoneNumber");
                String address = resultSet.getString("address");
                String email = resultSet.getString("email");
                String birthday = resultSet.getString("birthday");

                observableList.addAll(new Distributor(id, name, phone, email, address, birthday));
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
        distributorObservableList = getDistributorObservableList();
        idTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneTableColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressTableColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        emailTableColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        birthdayTableColumn.setCellValueFactory(new PropertyValueFactory<>("birthday"));
        orderNumberTableColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(distributorTableView.getItems().indexOf(param.getValue()) + 1 + (currentPage - 1) * itemsPerPage));

        totalLabel.setText("Total: " + distributorObservableList.size());

        birthdayTableColumn.setCellFactory(new Callback<TableColumn<Distributor, String>, TableCell<Distributor, String>>() {
            @Override
            public TableCell<Distributor, String> call(TableColumn<Distributor, String> column) {
                return new TableCell<Distributor, String>() {
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
        // callback method to create table cell for each row
        Callback<TableColumn<Distributor, String>, TableCell<Distributor, String>> columnTableCellCallback = (param) -> {
            final TableCell<Distributor, String> deleteCell = new TableCell<>() { // create a new table cell
                // method will be called whenever the table need to update to display the cell
                @Override
                public void updateItem(String item, boolean isEmpty) {
                    super.updateItem(item, isEmpty);
                    //if the cell is not empty, not show the button
                    if (isEmpty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        final Button deleteButton = new Button("Delete"); // create new button
                        deleteButton.getStyleClass().add("delete-button");
                        deleteButton.setOnAction(event -> {
                            Distributor distributor = getTableView().getItems().get(getIndex());

//                            boolean isRelated = isDistributorIdRelated(distributor.getId());
//                            if (isRelated) {
//                                GetData.showErrorAlert("Error message", "This distributor is related to other record and cannot delete");

//                            } else {
                            ButtonType resultConfirm = GetData.showConfirmationAlert("Confirmation message", "Are you sure you want to delete?");
                            if (resultConfirm.equals(ButtonType.OK)) {
                                deleteDistributorFromDatabase(distributor.getId());
//                                }
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

        // create filtered list
        FilteredList<Distributor> filteredList = new FilteredList<>(distributorObservableList, b -> true);
        // listen to changes in the searchKeyword to update table view
        searchKeywordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(distributor -> {
                if (newValue == null || newValue.trim().isEmpty()) {
                    totalLabel.setText("Total: " + distributorObservableList.size());
                    return true;
                }
                String searchKeyword = newValue.toLowerCase();
                String searchBy = searchComboBox.getValue().toLowerCase();

                if (searchBy.equals("name") && distributor.getName().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (searchBy.equals("phone") && distributor.getPhone().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (searchBy.equals("email") && distributor.getEmail().toLowerCase().contains(searchKeyword)) {
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
     * Sets up the pagination for the table view.
     * This method calculates the number of pages needed for pagination based on the number of items
     * in the filtered list. It updates the pagination controls and table view data when the page changes.
     */
    private void setupPagination() {
        int totalPages = (distributorObservableList.size() / itemsPerPage) + (distributorObservableList.size() % itemsPerPage > 0 ? 1 : 0);
        paginationPagination.setPageCount(totalPages);

        paginationPagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
            updateTableData(newValue.intValue());
        });
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
        int toIndex = Math.min(fromIndex + itemsPerPage, distributorObservableList.size());

        // Clear the table and re-add the events for the current page
        distributorTableView.getItems().clear();
        distributorTableView.getItems().addAll(distributorObservableList.subList(fromIndex, toIndex));
    }

    /**
     * Updates the pagination controls and table view data based on the filtered list.
     * This method updates the pagination controls and table view data based on the filtered list
     * using the current search keyword and search by criteria.
     *
     * @param filteredList The filtered list containing Distributor objects based on the search criteria.
     */
    private void updatePagination(FilteredList<Distributor> filteredList) {
        int totalItems = filteredList.size();
        int pageCount = (totalItems + itemsPerPage - 1) / itemsPerPage;

        if (pageCount == 0) {
            pageCount = 1;
        }

        paginationPagination.setPageCount(pageCount);

        if (paginationPagination.getCurrentPageIndex() >= pageCount) {
            paginationPagination.setCurrentPageIndex(pageCount - 1);
        }

        // update the table view base on current page
        int fromIndex = paginationPagination.getCurrentPageIndex() * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, filteredList.size());

        SortedList<Distributor> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(distributorTableView.comparatorProperty());

        distributorTableView.setItems(FXCollections.observableArrayList(sortedList.subList(fromIndex, toIndex)));
    }


    /**
     * Deletes a distributor from the database based on its ID.
     * This method deletes a distributor from the database based on its ID and updates the table view accordingly.
     *
     * @param id The ID of the distributor to be deleted.
     */
    private void deleteDistributorFromDatabase(int id) {

            try {
                // Xóa tất cả các sản phẩm liên quan đến nhà cung cấp
                String deleteOrdersQuery = "DELETE FROM `phone` WHERE distributorId = " + id;
                Statement deletePhonesStatement = connection.createStatement();
                int rowsDeleted = deletePhonesStatement.executeUpdate(deleteOrdersQuery);

                // Sau khi xóa sản phẩm, xóa nhà cung cấp
                if (rowsDeleted > 0) {
                    String deleteCustomerQuery = "DELETE FROM distributor WHERE distributorId = " + id;
                    Statement deletedDistributorStatement = connection.createStatement();
                    int rowsAffected = deletedDistributorStatement.executeUpdate(deleteCustomerQuery);

                    if (rowsAffected > 0) {
                        GetData.showSuccessAlert("Success message", "Deleted successfully!");
                        setupTable();
                        resetForm();
                    }
                } else {
                    GetData.showErrorAlert("Error message", "Distributor has phones and cannot be deleted!");
                }
            } catch (Exception e) {
                GetData.showErrorAlert("Error message", "Cannot delete!");
                e.printStackTrace();
            }
        }


        /**
         * Resets the form by clearing the input fields and enabling the add button.
         * This method resets the form by clearing the input fields, enabling the add button, and updating the action status label.
         */
    public void resetForm() {
        setIdAdd();
        nameTextField.clear();
        phoneTextField.clear();
        addressTextField.clear();
        emailTextField.clear();
        addButton.setDisable(false);
        updateButton.setDisable(true);
        birthdayDatePicker.setValue(null);
        birthdayDatePicker.getEditor().clear();
        actionStatusLabel.setText("Adding New Distributor");

        // Refresh the table view with the original unfiltered data
        updateTableData(paginationPagination.getCurrentPageIndex());
    }


    /**
     * Sets the ID for adding a new distributor by auto-incrementing the ID.
     * This method sets the ID for adding a new distributor by auto-incrementing the ID
     * based on the maximum distributor ID in the database.
     *
     * @return The newly generated ID for adding a new distributor.
     */
    private int setIdAdd() {
        String sql = "SELECT distributorId FROM distributor ORDER BY distributorID DESC LIMIT 1";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

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

    private boolean isFilledFields() {
        if (nameTextField.getText().isEmpty()
                || phoneTextField.getText().isEmpty()
                || emailTextField.getText().isEmpty()
                || birthdayDatePicker.getEditor().getText().isEmpty()
                || addressTextField.getText().isEmpty()) {
            GetData.showWarningAlert("Warning message", "Please fill all required fields!");
            return false;
        } else {
            return true;
        }
    }


    /**
     * Adds a new distributor to the database based on the input fields.
     * This method adds a new distributor to the database based on the input fields and updates the table view accordingly.
     * It performs validation to ensure all required fields are filled before adding the distributor.
     */
    public void addDistributorToDatabase() {
        String sql = "INSERT INTO distributor(distributorName,phoneNumber,address,email,birthday) VALUES (?,?,?,?,?)";
        try {
            if (isFilledFields() && validatePhoneNumber() && validateEmail()) {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, nameTextField.getText());
                preparedStatement.setString(2, phoneTextField.getText());
                preparedStatement.setString(3, addressTextField.getText());
                preparedStatement.setString(4, emailTextField.getText());
                preparedStatement.setString(5, birthdayDatePicker.getValue().toString());

                preparedStatement.executeUpdate();
                GetData.showSuccessAlert("Success message", "Added successfully!");

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
    public void updateDistributorToDatabase() {
        String sql = "UPDATE distributor SET distributorName = ?, phoneNumber = ?, address = ?,email = ?, birthday = ? WHERE distributorId = ?";
        try {
            if (isFilledFields() && validatePhoneNumber() && validateEmail()) {
                ButtonType resultConfirm = GetData.showConfirmationAlert("Confirmation message", "Are you sure you want to update?");
                if (resultConfirm.equals(ButtonType.OK)) {
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, nameTextField.getText());
                    preparedStatement.setString(2, phoneTextField.getText());
                    preparedStatement.setString(3, addressTextField.getText());
                    preparedStatement.setString(4, emailTextField.getText());
                    preparedStatement.setString(5, birthdayDatePicker.getValue().toString());
                    preparedStatement.setString(6, idTextField.getText());

                    int rowAffected = preparedStatement.executeUpdate();
                    if (rowAffected > 0) {
                        GetData.showSuccessAlert("Success message", "Updated successfully!");
                        setupTable();
                        resetForm();
                    }
                }
                setupTable();
                resetForm();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Listens for the selection of a record in the table view and updates the form accordingly.
     * This method listens for the selection of a record in the table view and updates the form
     * with the selected distributor's information for updating or editing purposes.
     */
    private void selectedRecord() {
        distributorTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Distributor>() {
            @Override
            public void changed(ObservableValue<? extends Distributor> observableValue, Distributor oldValue, Distributor newValue) {
                if (newValue != null) {
                    idTextField.setText(String.valueOf(newValue.getId()));
                    nameTextField.setText(String.valueOf(newValue.getName()));
                    phoneTextField.setText(String.valueOf(newValue.getPhone()));
                    addressTextField.setText(String.valueOf(newValue.getAddress()));
                    emailTextField.setText(String.valueOf(newValue.getEmail()));

                    // Set value for the birthdayDatePicker
                    String birthdayStr = newValue.getBirthday();
                    if (birthdayStr != null && !birthdayStr.isEmpty()) {
                        LocalDate dob = LocalDate.parse(birthdayStr); // Assuming birthdayDatePicker is in the format "yyyy-MM-dd"
                        birthdayDatePicker.setValue(dob);
                    } else {
                        birthdayDatePicker.setValue(null); // Clear the date picker if birthdayDatePicker is empty or null
                    }

                    addButton.setDisable(true);
                    updateButton.setDisable(false);
                    actionStatusLabel.setText("Updating Distributor");
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
        FilteredList<Distributor> filteredList = distributorObservableList.filtered(
                distributor -> {
                    LocalDate birthday = LocalDate.parse(distributor.getBirthday()); // Assuming the birthday is stored in "yyyy-MM-dd" format
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
                "FROM distributor " +
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
                    "        <img src=\"https://media.istockphoto.com/id/1410736450/vector/happy-birthday-beautiful-greeting-card-scratched-calligraphy-black-text-word-golden-line.jpg?b=1&s=612x612&w=0&k=20&c=C2cwLTDz2aFEyDjDkJpd7ZWoQvMkHhWJFKo3WlmT6uI=\" width=\"600px\">" +
                    "        <div class='content'>" +
                    "            <b>Hi you,</b>" +
                    "            <p>A little birdie told us that today was your special day. On behalf of everyone at CarShop, we hope you" +
                    "                have the best birthday ever.</p>" +
                    "            <p>May your day be the start of a year filled with good luck, good health and much happiness. We look" +
                    "                forward to continuing to help you achieve your wellness goals this year.</p>" +
                    "            <p>Have an unforgettable birthday!</p>" +
                    "            <p>Sincerely,</p>" +
                    "            <p>Car Shop</p>" +
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
                GetData.showWarningAlert("Warning message", "No distributor whose birthday is today");
            }

        } catch (Exception e) {
            e.printStackTrace();
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
}
