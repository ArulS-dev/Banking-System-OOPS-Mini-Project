package com.example.demo4;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import java.sql.*;

public class loginController {

    Alert alert = new Alert(Alert.AlertType.NONE);
    @FXML
    protected void onHelloButtonClick() {
        try{
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mazebank","root","");
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM users WHERE username=? and password=?");
            pst.setString(1,"Arul");
            pst.setString(2,"123");
            ResultSet rs = pst.executeQuery();
            ResultSet get_balance;
            if(rs.next()){
                String balance = rs.getString(4);
                System.out.println(balance);
                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setContentText("Login Successfull ");
                alert.show();
            }

        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}