package com.example.smartphone.model;

public class Status {
    private int id;
    private String details_status;

    public Status() {
    }

    public Status(int id, String details_status) {
        this.id = id;
        this.details_status = details_status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDetails_status() {
        return details_status;
    }

    public void setDetails_status(String details_status) {
        this.details_status = details_status;
    }
}
