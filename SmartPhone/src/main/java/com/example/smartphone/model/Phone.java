package com.example.smartphone.model;
import lombok.*;

@Data
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Phone {
    private int phone_id;
    private String phoneName;
    private String img;
    private double price;
    private double sellingPrice;
    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPhone_id() {
        return phone_id;
    }

    public void setPhone_id(int phone_id) {
        this.phone_id = phone_id;
    }

    public String getPhoneName() {
        return phoneName;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Phone(int phone_id, String phoneName, String img, double price, double sellingPrice) {
        this.phone_id = phone_id;
        this.phoneName = phoneName;
        this.img = img;
        this.price = price;
        this.sellingPrice = sellingPrice;
    }
}
