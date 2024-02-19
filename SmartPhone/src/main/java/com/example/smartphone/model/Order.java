package com.example.smartphone.model;

import java.time.LocalDateTime;

public class Order { private int user_id;
    private int order_id;
    private int status_id;
    private LocalDateTime date_order;
    private String productName;
    private double price;

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public Order(int order_id) {
        this.order_id = order_id;
    }

    public Order(int user_id, int status_id, LocalDateTime date_order, String productName, int status, double price) {
        this.user_id = user_id;
        this.status_id = status_id;
        this.date_order = date_order;
        this.productName = productName;
        this.status_id = status;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Order() {
    }


    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getStatus_id() {
        return status_id;
    }

    public void setStatus_id(int status_id) {
        this.status_id = status_id;
    }

    public LocalDateTime getDate_order() {
        return date_order;
    }

    public void setDate_order(LocalDateTime date_order) {
        this.date_order = date_order;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

}