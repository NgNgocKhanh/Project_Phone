package com.example.smartphone.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class InsertDataFromXMLToMySQL {
    public static void main(String[] args) {
        try {
            // Kết nối tới cơ sở dữ liệu MySQL
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/e_project2", "root", "");

            // Load mã XML từ tệp hoặc chuỗi
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse("src/main/resources/com/example/smartphone/phone-view.fxml");

            // Lấy danh sách các phần tử sản phẩm từ mã XML
            NodeList productList = doc.getElementsByTagName("Pane");

            // Duyệt qua danh sách và chèn dữ liệu vào cơ sở dữ liệu
            for (int i = 0; i < productList.getLength(); i++) {
                Node node = productList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element product = (Element) node;
                    String name = product.getElementsByTagName("Label").item(0).getTextContent();
                    String giaban = product.getElementsByTagName("Button").item(0).getTextContent();
                    Element imageElement = (Element) product.getElementsByTagName("ImageView").item(0);
                    String images = imageElement.getAttribute("url");

                    // Chèn dữ liệu vào cơ sở dữ liệu MySQL
                    String sql = "INSERT INTO phone (name, giaban, images) VALUES (?, ?, ?)";
                    PreparedStatement statement = conn.prepareStatement(sql);
                    statement.setString(1, name);
                    statement.setString(2, giaban);
                    statement.setString(3, images);
                    statement.executeUpdate();
                }
            }

            // Đóng kết nối
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
