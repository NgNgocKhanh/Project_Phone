package com.example.smartphone.controller;
import com.example.smartphone.model.Customer;
import com.example.smartphone.model.Phone;
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

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class AddOrderViewController {
    @FXML
    private AnchorPane orderFormPage;
    @FXML
    private ImageView phoneImageView;

    @FXML
    private TableColumn<Phone, Integer> orderNumberTableColumn;

    @FXML
    private Button plusButton;

    @FXML
    private Button minusButton;

    @FXML
    private TextField searchCustomerTextField;

    @FXML
    private TextField customerIdTextField;

    @FXML
    private ComboBox<String> searchCustomerComboBox;

    @FXML
    private TableView<Customer> customerTableView;

    @FXML
    private TableColumn<Customer, Integer> customerOrderNumberTableColumn;

    @FXML
    private TableColumn<Customer, String> customerNameTableColumn;

    @FXML
    private TableColumn<Customer, String> customerPhoneTableColumn;

    @FXML
    private TableColumn<Customer, String> customerEmailTableColumn;

    @FXML
    private TableColumn<Customer, Integer> customerIdTableColumn;

    @FXML
    private Button addCustomerButton;

    @FXML
    private Label customerNameLabel;

    @FXML
    private Label customerEmailLabel;

    @FXML
    private Label customerPhoneLabel;

    @FXML
    private Pagination paginationCustomerPagination;

    @FXML
    private Button addButton;

    @FXML
    private TableView<Phone> phoneTableView;
    @FXML
    private TableColumn<Phone, String> distributorTableColumn;

    @FXML
    private Label employeeNameLabel;

    @FXML
    private TableColumn<Phone, Integer> idTableColumn;

    @FXML
    private TableColumn<Phone, String> phoneColumnTable;
    @FXML
    private TableColumn<Phone, String> modelTableColumn;

    @FXML
    private Label orderDateLabel;

    @FXML
    private Label orderIdLabel;

    @FXML
    private TextField phoneIdTextField;

    @FXML
    private TextField orderQuantityTextField;

    @FXML
    private ComboBox<String> orderStatusComboBox;

    @FXML
    private Pagination paginationPagination;

    @FXML
    private TableColumn<Phone, Double> priceTableColumn;

    @FXML
    private TextField searchKeywordTextField;
    @FXML
    private TextField totalAmoutTextField;

    @FXML
    private ComboBox<String> paymentComboBox;

    @FXML
    private ComboBox<String> paymentStatusComboBox;

    private boolean isPhoneValid(String phoneNumber) {
        // Kiểm tra số điện thoại có 10 chữ số
        return phoneNumber.matches("\\d{10}");
    }

    private boolean isValidEmail(String email) {
        // Kiểm tra địa chỉ email có đúng định dạng @gmail.com không
        return email.matches(".+@gmail\\.com");
    }

    private final int currentPage = 1;
    private final int currentCustomerPage = 1;
    Connection connection = JDBCConnect.getJDBCConnection();
    ObservableList<Phone> phoneObservableList = FXCollections.observableArrayList();
    ObservableList<Customer> customerObservableList = FXCollections.observableArrayList();
    private final int itemsPerPage = 15;
    private final int itemsPerCustomerPage = 3;
//    private int customerId;
    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("\\d{10}");
    }

    public void initialize() {
        resetForm();
        setIdAdd();
        setupTable();
        setupCustomerTable();
        setupCustomerPagination();
        selectedCustomer();

        addSearchByToComboBox();
        setupPagination();
        addStatusComboBox();
        addPaymentComboBox();
        addPaymentStatusComboBox();
        selectedRecord();
        orderDateLabel.setText("Date: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        employeeNameLabel.setText("Processing by: " + GetData.username);

        // Set the custom row factory
        phoneTableView.setRowFactory(tv -> {
            TableRow<Phone> row = new TableRow<>();
            row.itemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null && newValue.getQuantity() == 0) {
                    row.getStyleClass().add("White-row");
                    row.setDisable(true); // Disable row selection for rows with quantity 0
                } else {
                    row.getStyleClass().remove("White-row");
                    row.setDisable(false); // Enable row selection for other rows
                }
            });
            return row;
        });

        // Add an event filter to allow only numeric input for phoneTextField
        orderQuantityTextField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!isNumeric(event.getCharacter())) {
                event.consume(); // Consume the event to prevent non-numeric input
            }
        });
    }

    private boolean isNumeric(String str) {
        return str.matches("\\d*"); // Check if the given string contains only digits
    }

    private void addSearchByToComboBox() {
        searchCustomerComboBox.getItems().addAll("Name", "Email", "Phone");
        searchCustomerComboBox.setValue("Name");
    }

    public void resetForm() {
        setIdAdd();
        setupTable();
        orderStatusComboBox.setValue(null);
        paymentComboBox.setValue(null);
        paymentStatusComboBox.setValue(null);
        orderStatusComboBox.setPromptText("Status");
        paymentComboBox.setPromptText("Payment");
        paymentStatusComboBox.setPromptText("Payment Status");
        orderQuantityTextField.setText("1");
        totalAmoutTextField.clear();
        phoneImageView.setImage(null);
        GetData.path = "";
        phoneIdTextField.clear();

        addButton.setDisable(false);
    }

    private int setIdAdd() {
        String sql = "SELECT orderId FROM `order` ORDER BY orderId DESC LIMIT 1";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                int phoneIdIncrease = resultSet.getInt("orderId") + 1;
                orderIdLabel.setText("OrderID: " + phoneIdIncrease);
                return phoneIdIncrease;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private Map<Integer, String> getStatusMap() {
        // create an empty Map to store status data
        Map<Integer, String> statusMap = new HashMap<>();

        String sql = "SELECT statusId, statusName FROM status";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                // iterate through the resultSet from db and put into map
                int statusId = resultSet.getInt("statusId");
                String statusName = resultSet.getString("statusName");

                statusMap.put(statusId, statusName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusMap;
    }

    private void addStatusComboBox() {
        Map<Integer, String> statusMap = getStatusMap();
        orderStatusComboBox.getItems().addAll(statusMap.values());
    }

    private Map<Integer, String> getPaymentMap() {
        // create an empty Map to store status data
        Map<Integer, String> paymentMap = new HashMap<>();

        String sql = "SELECT paymentId, paymentType FROM payment";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                // iterate through the resultSet from db and put into map
                int paymentId = resultSet.getInt("paymentId");
                String paymentType = resultSet.getString("paymentType");

                paymentMap.put(paymentId, paymentType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paymentMap;
    }

    private void addPaymentComboBox() {
        Map<Integer, String> paymentMap = getPaymentMap();
        paymentComboBox.getItems().addAll(paymentMap.values());
    }

    private Map<Integer, String> getPaymentStatusMap() {
        // create an empty Map to store status data
        Map<Integer, String> paymentStatusMap = new HashMap<>();

        String sql = "SELECT paymentStatusId, paymentStatus FROM paymentStatus";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                // iterate through the resultSet from db and put into map
                int paymentStatusId = resultSet.getInt("paymentStatusId");
                String paymentStatus = resultSet.getString("paymentStatus");

                paymentStatusMap.put(paymentStatusId, paymentStatus);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paymentStatusMap;
    }

    private void addPaymentStatusComboBox() {
        Map<Integer, String> paymentStatusMap = getPaymentStatusMap();
        paymentStatusComboBox.getItems().addAll(paymentStatusMap.values());
    }

    private int getKeyFromValue(Map<Integer, String> map, String value) {
        // iterate over the entries of the map object
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            // check if entry of the map equal with the value param of ComboBox then return the key
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return 1;
    }

    private ObservableList<Customer> getCustomerList() {
        ObservableList<Customer> observableList = FXCollections.observableArrayList();
        String sql = "SELECT customerId, customerName, phoneNumber,email FROM customer";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("customerId");
                String name = resultSet.getString("customerName");
                String phone = resultSet.getString("phoneNumber");
                String email = resultSet.getString("email");

                observableList.add(new Customer(id, name, phone, email));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return observableList;
    }

    /**
     * Sets up the phone table with data from the database and adds filtering functionality.
     */
    private void setupCustomerTable() {
        customerObservableList = getCustomerList();
        customerIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerPhoneTableColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customerEmailTableColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        customerOrderNumberTableColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(customerTableView.getItems().indexOf(param.getValue()) + 1 + (currentCustomerPage - 1) * itemsPerCustomerPage));

        FilteredList<Customer> filteredList = new FilteredList<>(customerObservableList, b -> true);

        searchCustomerTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(customer -> {
                if (newValue == null || newValue.trim().isEmpty()) {
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();
                String searchBy = searchCustomerComboBox.getValue().toLowerCase();

                if (searchBy.equals("name") && customer.getName().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (searchBy.equals("phone") && customer.getPhone().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else if (searchBy.equals("email") && customer.getEmail().toLowerCase().contains(searchKeyword)) {
                    return true;
                } else {
                    return false;
                }
            });
            // update pagination
            updateCustomerPagination(filteredList);

        });
        // update pagination
        updateCustomerPagination(filteredList);
    }

    private void updateCustomerTableData(int pageIndex) {
        int fromIndex = pageIndex * itemsPerCustomerPage;
        int toIndex = Math.min(fromIndex + itemsPerCustomerPage, customerObservableList.size());
        customerTableView.setItems(FXCollections.observableArrayList(customerObservableList.subList(fromIndex, toIndex)));
    }

    private void setupCustomerPagination() {
        int totalPages = (customerObservableList.size() / itemsPerCustomerPage) + (customerObservableList.size() % itemsPerCustomerPage > 0 ? 1 : 0);
        paginationCustomerPagination.setPageCount(totalPages);

        paginationCustomerPagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
            updateCustomerTableData(newValue.intValue());
        });
    }


    private void updateCustomerPagination(FilteredList<Customer> filteredList) {
        int totalItems = filteredList.size();
        int pageCount = (totalItems + itemsPerCustomerPage - 1) / itemsPerCustomerPage;

        // adjust the pagination's page count and current page if needed
        if (pageCount == 0) {
            pageCount = 1;
        }
        paginationCustomerPagination.setPageCount(pageCount);

        if (paginationCustomerPagination.getCurrentPageIndex() >= pageCount) {
            paginationCustomerPagination.setCurrentPageIndex(pageCount - 1);
        }

        // update the tableView base on the current page
        int fromIndex = paginationCustomerPagination.getCurrentPageIndex() * itemsPerCustomerPage;
        int toIndex = Math.min(fromIndex + itemsPerCustomerPage, totalItems);

        SortedList<Customer> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(customerTableView.comparatorProperty());

        customerTableView.setItems(FXCollections.observableArrayList(sortedList.subList(fromIndex, toIndex)));
    }

    /**
     * Retrieves a list of all phones from the database.
     *
     * @return An ObservableList containing phone objects.
     */
    private ObservableList<Phone> getListphone() {
        ObservableList<Phone> observableList = FXCollections.observableArrayList();
        String sql = "SELECT p.phoneId, p.phoneName ,p.price, p.sellingPrice, d.distributorName, p.image, i.quantityInStock " +
                "FROM phone AS p " +
                "JOIN distributor AS d ON p.distributorId = d.distributorId " +
                "JOIN phone_inventory AS i ON p.phoneId = i.phoneId " +
                "ORDER BY p.phoneName";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                // iterate through the resultSet from db and add to list
                int id = resultSet.getInt("phoneId");
                String name = resultSet.getString("phoneName");
                double price = resultSet.getDouble("price");
                String distributorName = resultSet.getString("distributorName");
                String image = resultSet.getString("image");
                int quantity = resultSet.getInt("quantityInStock");
                double sellingPrice = resultSet.getDouble("sellingPrice");

                // add to list
                observableList.add(new Phone(id, name, image, price, sellingPrice, quantity,distributorName));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return observableList;
    }

    /**
     * Sets up the phone table with data from the database and adds filtering functionality.
     */
    private void setupTable() {
        phoneObservableList = getListphone();
        idTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        phoneColumnTable.setCellValueFactory(new PropertyValueFactory<>("phoneName"));
        priceTableColumn.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
        distributorTableColumn.setCellValueFactory(new PropertyValueFactory<>("distributor"));
        orderNumberTableColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(phoneTableView.getItems().indexOf(param.getValue()) + 1 + (currentPage - 1) * itemsPerPage));

        // create FilteredList to filter and search phone by searchKeyword
        FilteredList<Phone> filteredList = new FilteredList<>(phoneObservableList, b -> true); // b->true : means all elements in the list will be included in the filteredList

        // listen to changes in the searchKeyword to update the tableView
        searchKeywordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(phone -> {
                if (newValue == null || newValue.trim().isEmpty()) {
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();
                return phone.getPhoneName().toLowerCase().contains(searchKeyword);
            });
            // update pagination
            updatePagination(filteredList);

        });
        // update pagination
        updatePagination(filteredList);
    }

    /**
     * Updates the phone table data based on the current pagination page.
     *
     * @param pageIndex The index of the current pagination page.
     */
    private void updateTableData(int pageIndex) {
        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, phoneObservableList.size());
        phoneTableView.setItems(FXCollections.observableArrayList(phoneObservableList.subList(fromIndex, toIndex)));
    }

    /**
     * Sets up the pagination control and links it to the phone table.
     */
    private void setupPagination() {
        int totalPages = (phoneObservableList.size() / itemsPerPage) + (phoneObservableList.size() % itemsPerPage > 0 ? 1 : 0);
        paginationPagination.setPageCount(totalPages);

        paginationPagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
            updateTableData(newValue.intValue());
        });
    }

    /**
     * Updates the pagination control based on the filtered phone list.
     *
     * @param filteredList The FilteredList containing the filtered phones.
     */
    private void updatePagination(FilteredList<Phone> filteredList) {
        int totalItems = filteredList.size();
        int pageCount = (totalItems + itemsPerPage - 1) / itemsPerPage;

        // adjust the pagination's page count and current page if needed
        if (pageCount == 0) {
            pageCount = 1;
        }
        paginationPagination.setPageCount(pageCount);

        if (paginationPagination.getCurrentPageIndex() >= pageCount) {
            paginationPagination.setCurrentPageIndex(pageCount - 1);
        }

        // update the tableView base on the current page
        int fromIndex = paginationPagination.getCurrentPageIndex() * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, totalItems);

        SortedList<Phone> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(phoneTableView.comparatorProperty());

        phoneTableView.setItems(FXCollections.observableArrayList(sortedList.subList(fromIndex, toIndex)));
    }

    /**
     * Checks if all required fields (customer name, email, address, phone, order status, quantity, and total amount) are filled.
     * Displays a warning message if any required field is empty.
     *
     * @return true if all required fields are filled; otherwise, false.
     */
    private boolean isFilledAllField() {
        if (
                orderStatusComboBox.getValue() == null
                        || paymentStatusComboBox.getValue() == null
                        || paymentComboBox.getValue() == null
                        || orderQuantityTextField.getText().isEmpty()
                        || totalAmoutTextField.getText().isEmpty()
        ) {
            GetData.showWarningAlert("Warning message", "Please fill all required fields!");

            return false;
        } else if (customerIdTextField.getText().isEmpty()) {
            GetData.showWarningAlert("Warning message", "Please fill customer information!");
            return false;
        } else {
            return true;
        }
    }


    /**
     * Adds the order information to the database.
     * If the customer exists, associates the order with the existing customer.
     * If the customer does not exist, adds a new customer before adding the order.
     */
    public void addToDatabase() {
        String currentDate = LocalDate.now().toString();

        // get data from status combo box
        Map<Integer, String> statusMap = getStatusMap();
        String selectedStatusValue = orderStatusComboBox.getValue();
        int selectedStatusId = getKeyFromValue(statusMap, selectedStatusValue);

        Map<Integer, String> paymentMap = getPaymentMap();
        String selectedPaymentValue = paymentComboBox.getValue();
        int selectedPaymentId = getKeyFromValue(paymentMap, selectedPaymentValue);

        Map<Integer, String> paymentStatusMap = getPaymentStatusMap();
        String selectedPaymentStatusValue = paymentStatusComboBox.getValue();
        int selectedPaymentStatusId = getKeyFromValue(paymentStatusMap, selectedPaymentStatusValue);

        if (isFilledAllField()) {

            String sql = "INSERT INTO `order`(customerId,phoneId,orderDate,totalAmount,quantity,statusId,paymentId,paymentStatusId) VALUES (?,?,?,?,?,?,?,?)";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, customerIdTextField.getText());
                preparedStatement.setString(2, phoneIdTextField.getText());
                preparedStatement.setString(3, currentDate);
                preparedStatement.setString(4, totalAmoutTextField.getText());
                preparedStatement.setString(5, orderQuantityTextField.getText());
                preparedStatement.setString(6, String.valueOf(selectedStatusId));
                preparedStatement.setInt(7, selectedPaymentId);
                preparedStatement.setInt(8, selectedPaymentStatusId);

                preparedStatement.executeUpdate();

                GetData.showSuccessAlert("Success message", "Add order successfully!");

                resetForm(); // reset form
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Listens for the selected phone in the phone table and updates the phoneIdTextField and phoneImageView accordingly.
     */
    private void selectedRecord() {
        // catch select row event
        phoneTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Phone>() {
            @Override
            public void changed(ObservableValue<? extends Phone> observableValue, Phone oldValue, Phone newValue) {
                if (newValue != null) {
                    phoneIdTextField.setText(String.valueOf(newValue.getPhone_id()));
                    totalAmoutTextField.setText(String.valueOf((newValue.getSellingPrice() + newValue.getSellingPrice() * newValue.getPrice() / 100) * Integer.parseInt(orderQuantityTextField.getText())));
                    File imageFile = new File("C:\\25022024\\Project_Phone\\SmartPhone"+newValue.getImg());
                    System.out.println("image"+imageFile.getAbsolutePath());
                    Image image = null;
                    try {
                        image = new Image(imageFile.toURI().toString());
                        phoneImageView.setImage(image);

                    } catch (Exception e) {
                        e.printStackTrace();
                        image = new Image(imageFile.getAbsolutePath());
                        phoneImageView.setImage(image);
//                        GetData.showWarningAlert("Warning message", imageFile.getAbsolutePath());
                    }
                } else {
                    phoneImageView.setImage(null);
                    GetData.path = "";
                    phoneIdTextField.clear();
                }
            }
        });
    }

    private void selectedCustomer() {
        // catch select row event
        customerTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Customer>() {
            @Override
            public void changed(ObservableValue<? extends Customer> observableValue, Customer oldValue, Customer newValue) {
                if (newValue != null) {
                    customerIdTextField.setText(String.valueOf(newValue.getId()));
                    customerNameLabel.setText("Customer Name: " + newValue.getName());
                    customerEmailLabel.setText("Email: " + newValue.getEmail());
                    customerPhoneLabel.setText("Phone Number: " + newValue.getPhone());

                }
            }
        });
    }

    @FXML
    private void handleMinusButton(ActionEvent event) {
        String currentValue = orderQuantityTextField.getText();

        try {
            int value = Integer.parseInt(currentValue);

            if (value > 1) {
                value--;
            }
            orderQuantityTextField.setText(Integer.toString(value));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePlusButton(ActionEvent event) {
        String currentValue = orderQuantityTextField.getText();
        try {
            int value = Integer.parseInt(currentValue);
            value++;
            orderQuantityTextField.setText(Integer.toString(value));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addNewCustomerPage(MouseEvent event) {
// Create a transparent overlay
        Pane overlay = new Pane();
        overlay.setStyle("-fx-background-color: transparent;");
        overlay.setPrefSize(orderFormPage.getWidth(), orderFormPage.getHeight());

        // Add the overlay as the first child of orderFormPage
        orderFormPage.getChildren().add(0, overlay);

        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.3);
        colorAdjust.setInput(new BoxBlur(10, 10, 3));

        orderFormPage.setEffect(colorAdjust);
        orderFormPage.setDisable(true);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/smartphone/add-new-customer-form.fxml"));
            Parent root = loader.load();

            Stage addCustomerFormStage = new Stage();
            addCustomerFormStage.initStyle(StageStyle.TRANSPARENT);
            addCustomerFormStage.initModality(Modality.APPLICATION_MODAL); // Block interaction with parent
            addCustomerFormStage.setAlwaysOnTop(true);
            addCustomerFormStage.setScene(new Scene(root));

            // Close the setting page when the overlay is clicked
            overlay.setOnMouseClicked(e -> {
                addCustomerFormStage.close();

            });

            // Close the setting page event handler
            addCustomerFormStage.setOnHidden(e -> {
                setupCustomerTable();
                // Remove the color blur effect and re-enable interactions when the setting page is closed
                orderFormPage.setEffect(null);
                orderFormPage.setDisable(false);

                // Remove the overlay when the setting page is closed
                orderFormPage.getChildren().remove(overlay);
            });

            addCustomerFormStage.showAndWait(); // Show as a modal dialog
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}