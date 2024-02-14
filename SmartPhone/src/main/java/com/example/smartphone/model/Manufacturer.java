package com.example.smartphone.model;

public class Manufacturer {
    private int manufacturer_id;
    private String name;
    private String image;
    private String phone;

    public Manufacturer() {
    }
    public Manufacturer(int manufacturer_id, String name, String image, String phone) {
        this.manufacturer_id = manufacturer_id;
        this.name = name;
        this.image = image;
        this.phone = phone;
    }

    public int getManufacturer_id() {
        return manufacturer_id;
    }

    public void setManufacturer_id(int manufacturer_id) {
        this.manufacturer_id = manufacturer_id;
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
