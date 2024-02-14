package com.example.smartphone.model;

public class Manufacturer {
    private String name;
    private String image;
    private String phone;

    public Manufacturer() {
    }

    public Manufacturer(String name, String image, String phone) {
        this.name = name;
        this.image = image;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
