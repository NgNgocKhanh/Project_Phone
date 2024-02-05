package com.example.smartphone;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public abstract class HomeController implements Initializable {
    @FXML
    private GridPane phoneContainer;
    private List<Phone> recommeded;
    public void initialize(URL location, ResourceBundle resources){
        recommeded=new ArrayList<>(phones());
        int colum = 0;
        int row = 1;
        try{
            for (Phone phone :recommeded){
                FXMLLoader fxmlLoader =new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("phone.fxml"));
                VBox phoneBox =fxmlLoader.load();
                PhoneController phoneController = fxmlLoader.getController();
                phoneController.setData(phone);
                if (colum==5){
                    colum =0;
                    ++row;
                }
                phoneContainer.add(phoneBox,colum,row);
                GridPane.setMargin(phoneBox,new Insets(10));
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private List<Phone> phones() {
        List<Phone> ls = new ArrayList<>();
        Phone phone = new Phone();
        phone.setPhoneName("Ip 11");
        phone.setImg("@../../../../../../../img/phone-case.png");
        phone.setPrice("10000$");
        ls.add(phone);

        return ls;
    }

}

