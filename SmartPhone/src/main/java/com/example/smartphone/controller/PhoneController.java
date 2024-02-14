package com.example.smartphone.controller;

import com.example.smartphone.model.MyPageManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;

public class PhoneController {
    @FXML
    private Pagination pagination;
    private MyPageManager pageManager;
    @FXML
    private Label usernameLabel;
    public void initialize() {
        pagination = new Pagination();
        pageManager = new MyPageManager(10); // 10 là số mục mỗi trang
        pagination.setPageFactory(pageManager::createPage);
    }
}
