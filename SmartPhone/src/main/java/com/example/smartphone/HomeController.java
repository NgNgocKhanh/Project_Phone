package com.example.smartphone;

import dao.JDBCConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public abstract class HomeController implements Initializable {
    @FXML
    private GridPane phoneContainer;
    private List<Register.Phone> recommeded;
    private TextField yourTextField = new TextField();
    public TextField flname;
    public TextField fusername;
    public void initialize(URL location, ResourceBundle resources){
        recommeded=new ArrayList<>(phones());
        int colum = 0;
        int row = 1;
        try{
            for (Register.Phone phone :recommeded){
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

    private List<Register.Phone> phones() {
        List<Register.Phone> ls = new ArrayList<>();
        Register.Phone phone = new Register.Phone();
        phone.setPhoneName("Ip 11");
        phone.setImg("@../../../../../../../img/phone-case.png");
        phone.setPrice("10000$");
        ls.add(phone);

        return ls;
    }

    public static class SceneController implements Initializable {
        @FXML
        private Stage stage;
        private Scene scene;
        private Parent root;
        @FXML
        private TextField loggedInTextField;
        @FXML
        private Label loginMessageLabel;
        public void SwitchToRegister(ActionEvent event) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("register.fxml"));
                root = fxmlLoader.load();
                stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        @FXML
        public void loginButtonOnAction(ActionEvent event) throws SQLException {
            login();
        }
        public TextField fusername;
        public TextField flname;
        public TextField tname;
        public PasswordField tpass;
        public Button btnCon;
        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
            btnCon.setOnAction(actionEvent -> {
                try {
                    login();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        public void login() throws SQLException {
            PreparedStatement preparedStatement = null;
            ResultSet rs = null;
            Connection con = JDBCConnect.getJDBCConnection();
            try {
                preparedStatement = con.prepareStatement("SELECT * FROM  admin WHERE username = ? AND password = ?");
                preparedStatement.setString(1, tname.getText());
                preparedStatement.setString(2, tpass.getText());
                rs = preparedStatement.executeQuery();
                if (rs.next()){
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("home.fxml"));
                    Parent root = fxmlLoader.load();
                    Scene scene = new Scene(root, 520, 400);
                    Stage stage = (Stage) tname.getScene().getWindow();
                    stage.setScene(scene);
                    String fullname = fusername.getText() + flname.getText();
                    loggedInTextField.setText(fullname);
                    loggedInTextField.setVisible(true);
                    stage.show();
                }else {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Login Failed !", ButtonType.OK);
                    alert.show();
                }
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        }


    }
}

