package com.example.demo4;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
public class Dashboard implements Initializable {
    public String UniqueId, username, surname,account,type,tablename;
    public int balance , accountNumber, amount;
    @FXML
    public Label cardName, cardBalance, cardUniqueid, homeName, acName , acUniqueid;
    public TextField depositField, withdrawField, uniqueto, transferField;
    public Label AcNo;
    Alert alert = new Alert(Alert.AlertType.NONE);
    public void setUniqueId(String UniqueIdD, String username, int balance, String surname, int accountNumber) throws SQLException {
        this.UniqueId = UniqueIdD;
        this.username = username;
        this.balance = balance;
        this.surname = surname;
        this.accountNumber = accountNumber;
        System.out.println(accountNumber);
        homeName.setText("Welcome "+username.toUpperCase()+" "+surname.toUpperCase());
        cardName.setText(username.toUpperCase() + " "+surname.toUpperCase());
        cardBalance.setText(String.format("%d",balance));
        cardUniqueid.setText(UniqueId.toUpperCase());
        acName.setText(username +" "+ surname);
        acUniqueid.setText(UniqueId);
        AcNo.setText("**** **** **** "+accountNumber);
    }
    public void depositBtn() throws SQLException {
        int value = Integer.parseInt(depositField.getText());
        balance += value;
        UI = UniqueId;
        BI = balance;
        System.out.println(balance);
        sqlConnection();
        type = "Credit";
        tablename = username.toLowerCase();
        amount = value;
        account = UniqueId;
        sqlConnectiontransaction();
        list.add(new Transactions(type, UniqueId, value));
    }
    public void withdrawBtn() throws SQLException {
        int value = Integer.parseInt(withdrawField.getText());
        if(value <= balance){
            balance -= value;
            UI = UniqueId;
            BI = balance;
            sqlConnection();
            type = "Debit";
            tablename = username.toLowerCase();
            amount = value;
            account = UniqueId;
            sqlConnectiontransaction();
            list.add(new Transactions(type,UniqueId,value));

        }
        else{
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("Your Balance is too High..");
            alert.show();
            withdrawField.clear();
            withdrawField.requestFocus();
        }
    }
    String uniqueTo, UI, usernameto;
    int balanceto, BI;
    public void transferBtn() throws SQLException {
        uniqueTo = uniqueto.getText();
        int value = Integer.parseInt(transferField.getText());
        balance -= value;
        BI = balance;
        UI = UniqueId;
        sqlConnection();
        balanceto = sqlConnection1();
        balanceto += value;
        type = "From";
        tablename = username.toLowerCase();
        amount = value;
        account = UniqueId;
        sqlConnectiontransaction();
        BI = balanceto;
        UI = uniqueTo;
        sqlConnection();
        type = "To";
        tablename = usernameto.toLowerCase();
        amount = value;
        account = uniqueTo;
        sqlConnectiontransaction();
        list.add(new Transactions(type,uniqueTo,value));


    }
    void sqlConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mazebank","root","");
        PreparedStatement pst = conn.prepareStatement("UPDATE users SET balance = ? WHERE uniqueid = ?");
        pst.setString(1, String.valueOf(BI));
        pst.setString(2,UI);
        pst.executeUpdate();
        cardBalance.setText(String.format("%d",balance));
        alert.setAlertType(Alert.AlertType.INFORMATION);
        alert.setContentText("Success..");
        alert.show();
        depositField.clear();
        withdrawField.clear();
        depositField.requestFocus();
        withdrawField.requestFocus();
        transferField.clear();
        uniqueto.clear();
        uniqueto.requestFocus();
        conn.close();
    }
    int sqlConnection1() throws SQLException {
        int balanceto = 0;
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mazebank","root","");
        PreparedStatement pst = conn.prepareStatement("SELECT * FROM users WHERE uniqueid =?");
        pst.setString(1, uniqueTo);
        ResultSet rs = pst.executeQuery();
        while(rs.next()){
            balanceto =rs.getInt("balance");
            usernameto =rs.getString("username");
        }
        conn.close();
        tablename = usernameto.toLowerCase();
        return balanceto;
    }


    // Transaction History using sql table

    //step 1: transfer the values in sql table

    void sqlConnectiontransaction() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mazebank","root","");
        PreparedStatement pst = conn.prepareStatement("INSERT INTO "+tablename.toLowerCase()+" (type, account, amount) VALUES ( ?, ?, ?)");
        pst.setString(1, type);
        pst.setString(2, account);
        pst.setInt(3, (amount));
        pst.executeUpdate();
        conn.close();
    }
    void sqlHistory() throws SQLException {
        Connection conn1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/mazebank","root","");
        PreparedStatement pst1 = conn1.prepareStatement("SELECT * FROM "+tablename.toLowerCase()+" ");
        ResultSet rs = pst1.executeQuery();
        while(rs.next()){
            int s = rs.getInt("id");
            int a = rs.getInt("amount");
            String t = rs.getString("type");
            String u = rs.getString("account");

            list.add(new Transactions(t,u,a));
        }
        conn1.close();
    }

    // Transaction Table view

    @FXML
    private TableView<Transactions> table;
    @FXML
    private TableColumn<Transactions, Integer> colAmount;

    @FXML
    private TableColumn<Transactions, String> colType;

    @FXML
    private TableColumn<Transactions, String> colUnique;

    ObservableList<Transactions> list = FXCollections.observableArrayList();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colAmount.setCellValueFactory(new PropertyValueFactory<Transactions, Integer>("colAmount"));
        colUnique.setCellValueFactory(new PropertyValueFactory<Transactions, String>("colUnique"));
        colType.setCellValueFactory(new PropertyValueFactory<Transactions, String>("colType"));
        table.setItems(list);
    }



}