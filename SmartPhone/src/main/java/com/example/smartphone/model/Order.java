package com.example.smartphone.model;

public class Order {
    private int order_id;
    private int user_id;
    private int status_id;
    private String date_order;

    public Order() {
    }

    public Order(int order_id, int user_id, int status_id, String date_order) {
        this.order_id = order_id;
        this.user_id = user_id;
        this.status_id = status_id;
        this.date_order = date_order;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
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

    public String getDate_order() {
        return date_order;
    }

    public void setDate_order(String date_order) {
        this.date_order = date_order;
    }
}
