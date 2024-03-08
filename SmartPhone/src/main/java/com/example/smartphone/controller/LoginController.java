package com.example.smartphone.controller;

import dao.JDBCConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController{
    @FXML
    private Label usernameLabel;
    @FXML
    protected void onHelloButtonClick() {
        usernameLabel.setText("Welcome to Store Phone !");
    }

}