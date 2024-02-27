package com.example.smartphone.controller;
import com.example.smartphone.model.Event;
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
import javafx.scene.layout.HBox;
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

public class EventController {

    @FXML
    private Label actionStatusLabel;

    @FXML
    private Button addButton;

    @FXML
    private TableColumn<Event, String> addressTableColumn;

    @FXML
    private TextField addressTextField;

    @FXML
    private Button close;

    @FXML
    private DatePicker startDateDatePicker;

    @FXML
    private DatePicker endDateDatePicker;

    @FXML
    private TableColumn<Event, String> startDateTableColumn;

    @FXML
    private TableColumn<Event, String> endDateTableColumn;

    @FXML
    private TableColumn<Event, Integer> orderNumberTableColumn;

    @FXML
    private TableColumn<Event, String> actionTableColumn;

    @FXML
    private TableView<Event> eventTableView;

    @FXML
    private TableColumn<Event, Integer> idTableColumn;

    @FXML
    private TextField idTextField;


    @FXML
    private TableColumn<Event, String> nameTableColumn;

    @FXML
    private TextField nameTextField;

    @FXML
    private Pagination paginationPagination;

    @FXML
    private TextField searchKeywordTextField;

    @FXML
    private ComboBox<Integer> startHourComboBox;

    @FXML
    private ComboBox<Integer> endHourComboBox;

    @FXML
    private ComboBox<Integer> startMinuteComboBox;

    @FXML
    private ComboBox<Integer> endMinuteComboBox;

    @FXML
    private ComboBox<String> startTimeNotationComboBox;

    @FXML
    private ComboBox<String> endTimeNotationComboBox;

    @FXML
    private TableColumn<Event, String> startTimeTableColumn;

    @FXML
    private TableColumn<Event, String> endTimeTableColumn;

    @FXML
    private TextField discountTextField;

    @FXML
    private TableColumn<Event, String> discountTableColumn;

    @FXML
    private Label totalLabel;

    @FXML
    private Button updateButton;

    Connection connection = JDBCConnect.getJDBCConnection();
    ObservableList<Event> eventObservableList = FXCollections.observableArrayList();
    private int currentPage = 1;
    private static final int itemsPerPage = 12; // final variable to specify number of items per page

    public void initialize() {
        setIdAdd();
        selectedRecord();
        setupTable();
        setupPagination();
        setStartHourComboBox();
        setEndHourComboBox();
        setStartTimeNotationComboBox();
        setEndTimeNotationComboBox();
        setStartMinuteComboBox();
        setEndMinuteComboBox();
        validateFields();
    }

    @FXML
    private Label validateFields;

    private void validateFields() {
        nameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateName(newValue);
        });

        addressTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateAddress(newValue);
        });

        // Add a listener to the discount text field for numeric input validation
        discountTextField.setTextFormatter(createDecimalTextFormatter());

    }

    private TextFormatter<String> createDecimalTextFormatter() {
        return new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*\\.?\\d*")) {
                discountTextField.setStyle(""); // Clear any validation style
                return change;
            } else {
                discountTextField.setStyle("-fx-border-color: red;");
                return null; // Reject input that contains non-numeric characters or multiple decimal points
            }
        });
    }


    private void validateName(String newValue) {
        if (newValue.isEmpty()) {
            nameTextField.setStyle("-fx-border-color: red;");
        } else {
            nameTextField.setStyle(""); // Clear red border if valid
        }
    }

    private void validateAddress(String newValue) {
        if (newValue.isEmpty()) {
            addressTextField.setStyle("-fx-border-color: red;");
        } else {
            addressTextField.setStyle(""); // Clear red border if valid
        }
    }

    /**
     * Retrieves a list of events from the database and returns it as an ObservableList.
     * Each car in the list is represented by the Event model class.
     *
     * @return The ObservableList of event objects fetched from the database.
     */
    private ObservableList<Event> getListEvent() {
        ObservableList<Event> observableList = FXCollections.observableArrayList();
        String sql = "SELECT eventId, eventName,discount, startDate, startTime,endDate,endTime, address FROM `event`";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                // iterate through the resultSet from db and add to list
                int id = resultSet.getInt("eventId");
                String name = resultSet.getString("eventName");
                double discount = resultSet.getDouble("discount");
                String startDate = resultSet.getString("startDate");
                String address = resultSet.getString("address");
                String startTime = resultSet.getString("startTime");
                String endDate = resultSet.getString("endDate");
                String endTime = resultSet.getString("endTime");

                // add to list
                observableList.add(new Event(id, name, discount, startDate, startTime, endDate, endTime, address));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return observableList;
    }

    /**
     * Sets up the car table with the data fetched from the database.
     * It populates the table columns with cell factories and binds the data to the TableView.
     * The method also configures the delete button cell in the table to handle deletion of cars.
     */
    private void setupTable() {
        eventObservableList = getListEvent();
        idTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        discountTableColumn.setCellValueFactory(new PropertyValueFactory<>("discount"));
        startDateTableColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        startTimeTableColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endDateTableColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        endTimeTableColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        addressTableColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        orderNumberTableColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(eventTableView.getItems().indexOf(param.getValue()) + 1 + (currentPage - 1) * itemsPerPage));

        totalLabel.setText("Total: " + eventObservableList.size());

        // Create a custom cell factory for the startDateTableColumn
        startDateTableColumn.setCellFactory(new Callback<TableColumn<Event, String>, TableCell<Event, String>>() {
            @Override
            public TableCell<Event, String> call(TableColumn<Event, String> column) {
                return new TableCell<Event, String>() {
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

        // Create a custom cell factory for the endDateTableColumn
        endDateTableColumn.setCellFactory(new Callback<TableColumn<Event, String>, TableCell<Event, String>>() {
            @Override
            public TableCell<Event, String> call(TableColumn<Event, String> column) {
                return new TableCell<Event, String>() {
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


        // Modify the existing columnTableCellCallback
        Callback<TableColumn<Event, String>, TableCell<Event, String>> columnTableCellCallback = (param) -> {
            final TableCell<Event, String> actionCell = new TableCell<>() { // create table cell
                @Override
                public void updateItem(String item, boolean isEmpty) {
                    super.updateItem(item, isEmpty);
                    // if the cell is not empty, the buttons will not be displayed
                    if (isEmpty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        final Button deleteButton = new Button("Delete"); // create delete button
                        deleteButton.getStyleClass().add("delete-button");
                        deleteButton.setOnAction(event -> {
                            Event events = getTableView().getItems().get(getIndex());

                            ButtonType resultConfirm = GetData.showConfirmationAlert("Confirmation message", "Are you sure you want to delete this event?");
                            // if user confirms delete, then delete the event
                            if (resultConfirm.equals(ButtonType.OK)) {
                                deleteFromDatabase(events.getId());
                            }
                        });

                        HBox buttonContainer = new HBox(deleteButton); // put both buttons in an HBox
                        buttonContainer.setSpacing(10); // Set spacing between buttons
                        setGraphic(buttonContainer);
                        setText(null);
                    }
                }
            };

            return actionCell;
        };

        actionTableColumn.setCellFactory(columnTableCellCallback);


        // create FilteredList to filter and search car by searchKeyword
        FilteredList<Event> filteredList = new FilteredList<>(eventObservableList, b -> true); // b->true : means all elements in the list will be included in the filteredList

        // listen to changes in the searchKeyword to update the tableView
        searchKeywordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(events -> {
                if (newValue == null || newValue.trim().isEmpty()) {
                    totalLabel.setText("Total: " + eventObservableList.size());
                    return true;
                }


                String searchKeyword = newValue.toLowerCase();

                if (events.getName().toLowerCase().contains(searchKeyword)) {
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
     * Updates the event table data based on the selected page index in the pagination control.
     * It displays the appropriate subset of events for the current page.
     *
     * @param pageIndex The index of the current page in the pagination control.
     */
    private void updateTableData(int pageIndex) {
        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, eventObservableList.size());

        // Clear the table and re-add the events for the current page
        eventTableView.getItems().clear();
        eventTableView.getItems().addAll(eventObservableList.subList(fromIndex, toIndex));
    }



    /**
     * Sets up the pagination control for the event table.
     * Calculates the total number of pages and updates the current page when pagination changes.
     * It also updates the table data based on the current page index.
     */
    private void setupPagination() {
        int totalPages = (eventObservableList.size() / itemsPerPage) + (eventObservableList.size() % itemsPerPage > 0 ? 1 : 0);
        paginationPagination.setPageCount(totalPages);

        paginationPagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
            updateTableData(newValue.intValue());
        });
    }

    /**
     * Updates the pagination control based on the filtered event list.
     * It adjusts the page count and current page index to reflect the filtered data.
     *
     * @param filteredList The filtered list of cars based on the search keyword.
     */
    private void updatePagination(FilteredList<Event> filteredList) {
        paginationPagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            currentPage = newIndex.intValue() + 1;
        });

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

        SortedList<Event> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(eventTableView.comparatorProperty());

        eventTableView.setItems(FXCollections.observableArrayList(sortedList.subList(fromIndex, toIndex)));
    }

    /**
     * Sets the next available eventId in the ID TextField for adding a new event record.
     * It queries the database to get the last eventId and increments it by one.
     * The method sets the new eventId in the ID TextField.
     *
     * @return The next available eventId for adding a new event record.
     */
    private int setIdAdd() {
        String sql = "SELECT eventId FROM event ORDER BY eventId DESC LIMIT 1";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                int eventIdIncrease = resultSet.getInt("eventId") + 1;
                idTextField.setText(String.valueOf(eventIdIncrease));
                return eventIdIncrease;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Populates the startTimeNotationComboBox
     */
    private void setStartTimeNotationComboBox() {
        startTimeNotationComboBox.getItems().addAll("A.M", "P.M");
        startTimeNotationComboBox.setValue("A.M");
    }

    /**
     * Populates the endTimeNotationComboBox
     */
    private void setEndTimeNotationComboBox() {
        endTimeNotationComboBox.getItems().addAll("A.M", "P.M");
        endTimeNotationComboBox.setValue("A.M");
    }

    /**
     * Populates the startHourComboBox
     */
    private void setStartHourComboBox() {
        startHourComboBox.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        startHourComboBox.setValue(7);
    }

    /**
     * Populates the endHourComboBox
     */
    private void setEndHourComboBox() {
        endHourComboBox.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        endHourComboBox.setValue(7);
    }

    /**
     * Populates the startMinuteComboBox
     */
    private void setStartMinuteComboBox() {
        startMinuteComboBox.getItems().addAll(0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55);
        startMinuteComboBox.setValue(0);
    }

    /**
     * Populates the endMinuteComboBox
     */
    private void setEndMinuteComboBox() {
        endMinuteComboBox.getItems().addAll(0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55);
        endMinuteComboBox.setValue(0);
    }


    /**
     * Resets the form by clearing the input fields and resetting the action status.
     * It sets the form to add mode and enables the add button while disabling the update button.
     */
    public void resetForm() {
        searchKeywordTextField.clear();
        setIdAdd(); // set new value for id field
        nameTextField.clear();
        addressTextField.clear();
        discountTextField.clear();
        startDateDatePicker.setValue(null);
        startDateDatePicker.getEditor().clear();
        endDateDatePicker.setValue(null);
        endDateDatePicker.getEditor().clear();
        startHourComboBox.setValue(7);
        startMinuteComboBox.setValue(0);
        startTimeNotationComboBox.setValue("A.M");
        endHourComboBox.setValue(7);
        endMinuteComboBox.setValue(0);
        endTimeNotationComboBox.setValue("A.M");

        actionStatusLabel.setText("Adding New Event");
        addButton.setDisable(false);
        updateButton.setDisable(true);
    }

    /**
     * Listens to the selected event record in the eventTableView and populates the form fields with the data.
     * When an event record is selected, it updates the form fields with the event details for editing.
     * The method also sets the action status to "Updating Event" and enables the update button.
     */
    private void selectedRecord() {
        eventTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Event>() {
            @Override
            public void changed(ObservableValue<? extends Event> observableValue, Event oldValue, Event newValue) {
                if (newValue != null) {
                    idTextField.setText(String.valueOf(newValue.getId()));
                    nameTextField.setText(String.valueOf(newValue.getName()));
                    addressTextField.setText(String.valueOf(newValue.getAddress()));
                    discountTextField.setText(String.valueOf(newValue.getDiscount()));

                    // Set value for the startDateDatePicker
                    String startDateString = newValue.getStart_date();
                    if (startDateString != null && !startDateString.isEmpty()) {
                        LocalDate startDate = LocalDate.parse(startDateString);
                        startDateDatePicker.setValue(startDate);
                    } else {
                        startDateDatePicker.setValue(null);
                    }

                    // Set value for the endDateDatePicker
                    String endDateString = newValue.getEnd_date();
                    if (endDateString != null && !endDateString.isEmpty()) {
                        LocalDate endDate = LocalDate.parse(endDateString);
                        endDateDatePicker.setValue(endDate);
                    } else {
                        endDateDatePicker.setValue(null);
                    }


                    String[] startTimeParts = newValue.getStart_time().split(":");
                    int startHour = Integer.parseInt(startTimeParts[0]);
                    int startMinute = Integer.parseInt(startTimeParts[1]);

                    // Convert 24-hour format to 12-hour format
                    int startHour12 = startHour > 12 ? startHour - 12 : (startHour == 0 ? 12 : startHour);
                    String startTimeNotation = startHour >= 12 ? "P.M" : "A.M";

                    startHourComboBox.setValue(startHour12);
                    startMinuteComboBox.setValue(startMinute);
                    startTimeNotationComboBox.setValue(startTimeNotation);

                    String[] endTimeParts = newValue.getEnd_time().split(":");
                    int endHour = Integer.parseInt(endTimeParts[0]);
                    int endMinute = Integer.parseInt(endTimeParts[1]);

                    // Convert 24-hour format to 12-hour format
                    int endHour12 = endHour > 12 ? endHour - 12 : (endHour == 0 ? 12 : endHour);
                    String endTimeNotation = endHour >= 12 ? "P.M" : "A.M";

                    endHourComboBox.setValue(endHour12);
                    endMinuteComboBox.setValue(endMinute);
                    endTimeNotationComboBox.setValue(endTimeNotation);

                    addButton.setDisable(true);
                    updateButton.setDisable(false);
                    actionStatusLabel.setText("Updating Event");
                } else {
                    resetForm();
                }
            }
        });
    }

    /**
     * Checks if all required fields in the form are filled.
     * It validates that the name,address, date, time fields are not empty.
     *
     * @return true if all required fields are filled, false otherwise.
     */
    private boolean isFilledFields() {
        if (nameTextField.getText().isEmpty()
                || discountTextField.getText().isEmpty()
                || addressTextField.getText().isEmpty()
                || startDateDatePicker.getEditor() == null
                || startTimeNotationComboBox.getItems().isEmpty()
                || startHourComboBox.getItems().isEmpty()
                || startMinuteComboBox.getItems().isEmpty()
                || endDateDatePicker.getEditor() == null
                || endHourComboBox.getItems().isEmpty()
                || endMinuteComboBox.getItems().isEmpty()
                || endTimeNotationComboBox.getItems().isEmpty()
        ) {
            // alert error if required fields no filled
            GetData.showWarningAlert("Warning message", "Please fill all required fields!");
            return false;
        } else {
            return true;
        }
    }

    /**
     * Adds a new event record to the database based on the data entered in the form.
     * It inserts a new event record with name,address,date,time.
     * The method also updates the event table and resets the form after successful insertion.
     */
    public void addToDatabase() {
        String sql = "INSERT INTO `event`(eventName,discount,startDate,startTime,endDate,endTime,address) VALUES (?,?,?,?,?,?,?);";

        try {
            // check required fields filled or not
            if (isFilledFields()) {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, nameTextField.getText());
                preparedStatement.setString(2, discountTextField.getText());
                preparedStatement.setString(3, startDateDatePicker.getValue().toString());

                int selectedStartHour = startHourComboBox.getValue();
                int selectedStartMinute = startMinuteComboBox.getValue();
                String selectedStartTimeNotation = startTimeNotationComboBox.getValue();
                int startHour24 = selectedStartTimeNotation.equals("P.M") ? selectedStartHour + 12 : selectedStartHour;
                String formattedStartTime = String.format("%02d:%02d:00", startHour24, selectedStartMinute);
                preparedStatement.setString(4, formattedStartTime);

                preparedStatement.setString(5, endDateDatePicker.getValue().toString());
                int selectedEndHour = endHourComboBox.getValue();
                int selectedEndMinute = endMinuteComboBox.getValue();
                String selectedEndTimeNotation = endTimeNotationComboBox.getValue();
                int endHour24 = selectedEndTimeNotation.equals("P.M") ? selectedEndHour + 12 : selectedEndHour;
                String formattedEndTime = String.format("%02d:%02d:00", endHour24, selectedEndMinute);
                preparedStatement.setString(6, formattedEndTime);


                preparedStatement.setString(7, addressTextField.getText());

                // execute
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    // alert success if add data successfully
                    GetData.showSuccessAlert("Success message", "Added successfully!");

                    // update the table and reset the form
                    setupTable();
                    resetForm();
                } else {
                    // alert error if adding data failed
                    GetData.showErrorAlert("Error message", "Failed to add event record.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // alert error if an exception occurs
            GetData.showErrorAlert("Error message", "An error occurred while adding event record.");
        }
    }

    /**
     * Updates an existing event record in the database based on the data entered in the form.
     * It updates the event record with the new name,date,time,address.
     * The method also updates the event table and resets the form after successful update.
     */
    public void updateToDatabase() {
        String sql = "UPDATE `event` SET eventName = ?,discount = ?, startDate = ?, startTime = ?, endDate = ?, endTime = ?, address = ? WHERE eventId = ?";

        try {
            if (isFilledFields()) {
                ButtonType resultConfirm = GetData.showConfirmationAlert("Confirmation message", "Are you sure you want to update?");
                // if user confirm delete then delete
                if (resultConfirm.equals(ButtonType.OK)) {
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, nameTextField.getText());
                    preparedStatement.setString(2, discountTextField.getText());

                    DateTimeFormatter startDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate startDateValue = startDateDatePicker.getValue();
                    String startDateString = startDateValue != null ? startDateValue.format(startDateFormatter) : null;
                    preparedStatement.setString(3, startDateString);

                    int selectedStartHour = startHourComboBox.getValue();
                    int selectedStartMinute = startMinuteComboBox.getValue();
                    String selectedStartTimeNotation = startTimeNotationComboBox.getValue();
                    int startHour24 = selectedStartTimeNotation.equals("P.M") ? selectedStartHour + 12 : selectedStartHour;
                    String formattedStartTime = String.format("%02d:%02d:00", startHour24, selectedStartMinute);
                    preparedStatement.setString(4, formattedStartTime);

                    DateTimeFormatter endDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate endDateValue = endDateDatePicker.getValue();
                    String endDateString = endDateValue != null ? endDateValue.format(endDateFormatter) : null;
                    preparedStatement.setString(5, endDateString);

                    int selectedEndHour = endHourComboBox.getValue();
                    int selectedEndMinute = endMinuteComboBox.getValue();
                    String selectedEndTimeNotation = endTimeNotationComboBox.getValue();
                    int endHour24 = selectedEndTimeNotation.equals("P.M") ? selectedEndHour + 12 : selectedEndHour;
                    String formattedEndTime = String.format("%02d:%02d:00", endHour24, selectedEndMinute);
                    preparedStatement.setString(6, formattedEndTime);

                    preparedStatement.setString(7, addressTextField.getText());
                    preparedStatement.setString(8, idTextField.getText());

                    int rowAffected = preparedStatement.executeUpdate();
                    // if updated success then alert
                    if (rowAffected > 0) {
                        GetData.showSuccessAlert("Success message", "Updated successfully!");

                        setupTable();
                        resetForm();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMailEvent(String eventName, double discount, String startDate, String startTime, String endDate, String endTime, String eventAddress) {
        String subject = "Welcome to our event!";

//        String sqlEvent = "SELECT eventName,discount, startDate, endDate, startTime, endTime, address FROM event";
        String sqlCustomer = "SELECT customerName, email FROM customer";

        try {
            Statement statement = connection.createStatement();

            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date startDates = inputDateFormat.parse(startDate);
            Date endDates = inputDateFormat.parse(endDate);
            String formattedStartDate = outputDateFormat.format(startDates);
            String formattedEndDate = outputDateFormat.format(endDates);

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
                    "        .header {" +
                    "            background-color: #323232;" +
                    "            color: #ffffff;" +
                    "            padding: 2px 0;" +
                    "            font-size: 35px;" +
                    "            text-align: center;" +
                    "            margin-bottom: 20px;" +
                    "        }" +
                    "    </style>" +
                    "</head>" +
                    "<body>" +
                    "    <div class='container'>" +
                    "        <div class='header'>" +
                    "            <h1>You're invited!</h1>" +
                    "        </div>" +
                    "        <div class='content'>" +
                    "            <b>Dear you,</b>" +
                    "            <p>We hope this message finds you well. We are thrilled to extend a warm invitation to you for an exclusive" +
                    "                event, <b>\"{{event_name}}\"</b> that is set to rev up your automotive enthusiasm! As a" +
                    "                valued member of our community, we would be honored to have you join us for an unforgettable experience" +
                    "                showcasing the latest and greatest in the world of automobiles.</p>" +
                    "            <p>The event promises a captivating display of cutting-edge vehicles, groundbreaking technologies, and a" +
                    "                unique opportunity to connect with fellow enthusiasts and experts in the field. From luxury sedans to" +
                    "                powerful off-road vehicles, from electric marvels to classic icons, you'll witness a comprehensive" +
                    "                spectrum of automotive innovation.</p>" +
                    "            <p>Event Details: <br>" +
                    "                Date: {{start_date}} (ends {{end_date}}) <br>" +
                    "                Time: {{start_time}} - {{end_time}} <br>" +
                    "                Venue: {{event_address}}</p>" +
                    "            <p>Get a <b>{{discount}}% discount</b> on all purchases made at the event! Just present this email at the" +
                    "                registration desk.</p>" +
                    "            <p>Our aim is to provide an engaging and informative environment where you can immerse yourself in the world" +
                    "                of cars, network with industry professionals, and explore the trends shaping the future of mobility." +
                    "                Moreover, complimentary refreshments and exciting giveaways await you as a token of our appreciation for" +
                    "                your continuous support.</p>" +
                    "            <p>Please RSVP by [RSVP Deadline] to ensure your spot at this exceptional gathering. Kindly respond to this" +
                    "                email or contact us at [RSVP Contact Information] to confirm your attendance and the number of guests" +
                    "                you'll be bringing along.</p>" +
                    "            <p>We look forward to sharing our passion for automobiles with you at this event. Your presence would truly" +
                    "                make the occasion special. If you have any questions or require additional information, please don't" +
                    "                hesitate to reach out.</p>" +
                    "            <p>Thank you for being an essential part of our automotive community. We anticipate your presence at" +
                    "                <b>\"{{event_name}}\"</b> and look forward to sharing this remarkable experience with you.</p>" +
                    "            <p>Best regards,</p>" +
                    "            <p>Car Shop</p>" +
                    "        </div>" +
                    "    </div>" +
                    "</body>" +
                    "</html>";
            ResultSet customerResultSet = statement.executeQuery(sqlCustomer);
            String customizedMessage = "";
            List<String> mailList = new ArrayList<>();
            while (customerResultSet.next()) {
                mailList.add(customerResultSet.getString("email"));
                // Replace placeholders with actual values
                customizedMessage = message
                        .replace("{{event_name}}", eventName)
                        .replace("{{discount}}", String.valueOf(discount))
                        .replace("{{start_date}}", formattedStartDate)
                        .replace("{{end_date}}", formattedEndDate)
                        .replace("{{start_time}}", startTime)
                        .replace("{{end_time}}", endTime)
                        .replace("{{event_address}}", eventAddress);
            }

            // Inside your sendMailEvent method
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes an event record from the database based on the provided eventId.
     *
     * @param id The ID of the event record to be deleted.
     */
    private void deleteFromDatabase(int id) {
        String sql = "DELETE FROM `event` WHERE eventId = " + id;
        try {
            Statement statement = connection.createStatement();
            int rowAffected = statement.executeUpdate(sql);

            if (rowAffected > 0) {
                GetData.showSuccessAlert("Success message", "Deleted successfully!");

                setupTable();
                resetForm();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

