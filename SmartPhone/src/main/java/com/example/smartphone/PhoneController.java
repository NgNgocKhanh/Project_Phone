package com.example.smartphone;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Phone;

public class PhoneController {
    @FXML
    private ImageView phoneImg;

    @FXML
    private Label phoneName;

    @FXML
    private Label price;
    public void setData(Phone phone){
        Image image =new Image(getClass().getResourceAsStream(phone.getImg()));
        phoneImg.setImage(image);
        phoneName.setText(phone.getPhoneName());
        price.setText(phone.getPrice());

    }
}
