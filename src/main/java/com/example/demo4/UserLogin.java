package com.example.demo4;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;
import java.sql.SQLException;
public class UserLogin {
    @FXML
    public TextField nameField;
    public TextField passField;
    public Button loginBtn;
    String UniqueID;
    Alert alert = new Alert(Alert.AlertType.NONE);
    static String username, password, surname;
    int balance, accountNumber;
    @FXML
    private void loginBtnL(){
        username = nameField.getText();
        password = passField.getText();

        if((username.isEmpty())||(password.isEmpty())){
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Username or Password couldn't be empty");
            alert.show();
        }
        else if (notNull()){
            try{
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mazebank","root","");
                PreparedStatement pst = conn.prepareStatement("SELECT * FROM users WHERE username=? and password=?");
                pst.setString(1,username);
                pst.setString(2,password);
                ResultSet rs = pst.executeQuery();
                ResultSet get_balance;
                if(rs.next()){
                    UniqueID = rs.getString("uniqueid");
                    balance = rs.getInt("balance");
                    surname = rs.getString("surname");
                    accountNumber = rs.getInt("accno");
                    System.out.println(UniqueID);
                    alert.show();
                    nameField.setText("");
                    passField.setText("");
                    nameField.requestFocus();
                }
                else{
                    alert.setAlertType(Alert.AlertType.ERROR);
                    alert.setContentText("Pls Enter valid Username and Password ");
                    alert.show();
                    nameField.setText("");
                    passField.setText("");
                    nameField.requestFocus();
                }
                Stage stage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(MazeBankApplication.class.getResource("Dashboard.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                Dashboard user = fxmlLoader.getController();
                user.setUniqueId(UniqueID,username,balance,surname, accountNumber);
                stage.setTitle("Maze Bank");
                stage.setScene(scene);
                stage.getIcons().add(new Image("C:\\Users\\ARUL S\\IdeaProjects\\demo4\\src\\main\\java\\com\\example\\demo4\\cardlogo.png"));
                stage.showAndWait();
            }
            catch (SQLException | IOException e){
                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setContentText("Something Went Wrong Please Try Again ");
                alert.show();

                nameField.setText("");
                passField.setText("");
                nameField.requestFocus();
            }

        }
    }
    public static boolean notNull(){
        if(username != null){
            if(password != null){
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }


}
