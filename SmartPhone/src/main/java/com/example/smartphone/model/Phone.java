package com.example.smartphone.model;

public class Phone {
    private int phone_id;
    private String phoneName;
    private String img;
    private String price;

    public String getPhoneName() {
        return phoneName;
    }

    public Phone() {
    }

    public Phone(int phone_id, String phoneName, String img, String price) {
        this.phone_id = phone_id;
        this.phoneName = phoneName;
        this.img = img;
        this.price = price;
    }

    public int getPhone_id() {
        return phone_id;
    }

    public void setPhone_id(int phone_id) {
        this.phone_id = phone_id;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
