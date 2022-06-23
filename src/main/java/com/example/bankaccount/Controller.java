package com.example.bankaccount;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;



public class Controller implements Initializable {

    /********************************* START UP PAGE *********************************/
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button register, login, logout;
    @FXML
    private Label fullName, homeID, homeAcc, balance;
    @FXML
    public static Label staticBalance;

    //users list
    public static ArrayList<User> users = new ArrayList<>();


    @FXML
    public void registerButton() throws IOException {

        // load registration form
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Register.fxml"));
        Parent registration = (Parent) fxmlLoader.load();

        Scene scene = new Scene(registration);
        Stage stage = new Stage();

        stage.setScene(scene);
        //stage.getIcons().add(new Image("file:src/main/resources/WesternLogo.png"));
        stage.setTitle("Registration Form");
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.show();
    }

    /********************************* LOGIN *********************************/

    @FXML
    public void loginButton() throws IOException{

        //select needed columns from both tables (accounts + users)
        String sql = "select users.name, accounts.id, users.address, users.email, users.phone, accounts.account, accounts.pin from accounts, users where account = ? and pin = ? and accounts.id == users.id";


        try {
            Connection conn = DataBase.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);


            //get info from text fields
            pstmt.setInt(1, Integer.parseInt(username.getText()));
            pstmt.setInt(2, Integer.parseInt(password.getText()));
            ResultSet rs = pstmt.executeQuery();


            while (rs.next()) {
                //add info to users list
                users.add(new User(rs.getString("name"), rs.getString("id"), rs.getString("address"), rs.getString("email"), rs.getString("phone"), new Bank(rs.getInt("account"), rs.getInt("pin"))));

                System.out.println(rs.getString(1));
                System.out.println(rs.getString(2));
                System.out.println(rs.getString(3));
                System.out.println(rs.getString(4));
                System.out.println(rs.getString(5));
                System.out.println(rs.getString(6));
                System.out.println(rs.getString(7));


                // load home page
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("HomePage.fxml"));
                fxmlLoader.setController(this);
                Parent homePage = (Parent) fxmlLoader.load();

                Scene scene = new Scene(homePage);
                Stage stage = new Stage();

                stage.setScene(scene);
                stage.setTitle("MARZ BANK");

                stage.show();
            }


            fullName.setText(users.get(0).getName());
            homeID.setText(users.get(0).getId());
            homeAcc.setText(String.valueOf(users.get(0).getBank().getAccountNumber()));
            balance.setText(String.valueOf(totalBalance()));


            //display error for incorrect # or pin (doesn't work for some reason)
        } catch (SQLException | NumberFormatException | IndexOutOfBoundsException | NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("");
            alert.setTitle("MARZ BANK");
            alert.setContentText("ERROR! Incorrect username or password! Please try again.");
            alert.showAndWait();
            e.printStackTrace();
        }

        password.clear();
        username.clear();
//        Stage stage = (Stage) login.getScene().getWindow();
//        stage.close();
    }


    /********************************* TRANSACTIONS WINDOW *********************************/

    @FXML
    private TableView<Transactions> transactionsTable;

    @FXML
    private TableColumn<Transactions, String> dateCol;

    @FXML
    private TableColumn<Transactions, Integer> refNumCol;

    @FXML
    private TableColumn<Transactions, String> typeCol;

    @FXML
    private TableColumn<Transactions, Double> amountCol;

    private ObservableList<Transactions> transactionsData;

    @FXML
    private void viewTransactions() throws IOException {
        //////////////////////load transactions window//////////////////////
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Transactions.fxml"));
        fxmlLoader.setController(this);
        Parent transactionsWindow = (Parent) fxmlLoader.load();

        Scene scene = new Scene(transactionsWindow);
        Stage stage = new Stage();

        stage.setScene(scene);
        //stage.getIcons().add(new Image("file:src/main/resources/WesternLogo.png"));
        stage.setTitle("Transactions");
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.show();
        ////////////////////////////////////////////////////////////

        transactionsData = FXCollections.observableArrayList();


        try {
            String sql = "select transactions.date,transactions.amount, transaction_type.type, transactions.refNumber from transactions," +
                "transaction_type,balance where balance.refNumber == transactions.refNumber and " +
                "balance.type == transaction_type.id and balance.account = ?";

            Connection connection = DataBase.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, Controller.users.get(0).getBank().getAccountNumber());

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                transactionsData.add(new Transactions(resultSet.getString(1),resultSet.getInt(4),resultSet.getString(3),resultSet.getDouble(2)));
            }

            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        this.dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.refNumCol.setCellValueFactory(new PropertyValueFactory<>("refNumber"));
        this.typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        this.amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        this.transactionsTable.setItems(transactionsData);
    }


    //logout
    @FXML
    public void logoutButton() throws IOException {

        fullName.setText("");
        homeID.setText("");
        homeAcc.setText("");
        balance.setText("");


        //closes pop-up
        Stage stage = (Stage) logout.getScene().getWindow();
        stage.close();

        users.clear();

        /*FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("bank.fxml"));
        Parent bank = (Parent) fxmlLoader.load();

        Scene scene = new Scene(bank);
        Stage stage2 = new Stage();

        stage2.setScene(scene);
        //stage.getIcons().add(new Image("file:src/main/resources/WesternLogo.png"));
        stage.setTitle("MARZ BANK");

        stage2.show();*/
    }


    @FXML
    public void depositMoney() throws IOException {

        //////////////////////load deposit form//////////////////////
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Deposit.fxml"));

        Parent depositForm = (Parent) fxmlLoader.load();

        Scene scene = new Scene(depositForm);
        Stage stage = new Stage();

        stage.setScene(scene);
        //stage.getIcons().add(new Image("file:src/main/resources/WesternLogo.png"));
        stage.setTitle("Deposit Money");
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.show();
        ////////////////////////////////////////////////////////////
    }

    @FXML
    public void withdrawMoney() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Withdraw.fxml"));
        Parent withdrawForm = (Parent) fxmlLoader.load();

        Scene scene = new Scene(withdrawForm);
        Stage stage = new Stage();

        stage.setScene(scene);
        //stage.getIcons().add(new Image("file:src/main/resources/WesternLogo.png"));
        stage.setTitle("Withdraw Money");
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.show();
    }

    public static void insertBalance(int type, int account) {
        int ref = 0;

        String sql;

        try {
            Connection connection = DataBase.getConnection();

            sql = "select refNumber from transactions";

            ResultSet rs = connection.createStatement().executeQuery(sql);


            while (rs.next()) {
                ref =  rs.getInt(1);
            }

            System.out.println("ref number: " + ref + " account: " + account);

            sql = "insert into balance(refNumber, account, type)values(?,?,?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, ref);
            preparedStatement.setInt(2, account);
            preparedStatement.setInt(3, type);

            preparedStatement.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static double totalBalance(){
        String sql = "select transactions.amount, balance.type from transactions, balance where balance.account = ? and transactions.refNumber == balance.refNumber";
        List<Balance> balanceList = new ArrayList<>();


        try {
            Connection connection = DataBase.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, users.get(0).getBank().getAccountNumber());

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                balanceList.add(new Balance(rs.getInt(1), rs.getInt(2)));
            }

            connection.close();


        } catch (SQLException e) {
            e.printStackTrace();
        }

        double balance = 0;

        for (Balance balance1 : balanceList) {
            if (balance1.getType() == 1) {
                balance += balance1.getAmount();
                System.out.println(balance+ "-------" +balance1.getAmount());
            }

            if (balance1.getType() == 2) {
                balance -= balance1.getAmount();
                System.out.println(balance+ "-------" +balance1.getAmount());
            }
        }
        return balance;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        staticBalance = balance;
    }

}