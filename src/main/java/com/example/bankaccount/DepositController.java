package com.example.bankaccount;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.example.bankaccount.Controller.*;

public class DepositController {

    /********************************* DEPOSIT MONEY *********************************/

    @FXML
    private TextField amountToBeDeposited;

    @FXML
    private PasswordField depositPin;

    @FXML
    private Button cancelDepButton, confirmDepButton;


    @FXML
    public void confirmDeposit() {

        if (Integer.parseInt(depositPin.getText()) == users.get(0).getBank().getPin()) {

            String sql = "insert into transactions(date, amount)values(?,?)";

            try {
                Connection connection = DataBase.getConnection();
                PreparedStatement pstmt = connection.prepareStatement(sql);


                pstmt.setString(1, getDate());
                pstmt.setDouble(2, Double.parseDouble(amountToBeDeposited.getText()));

                pstmt.execute();

                connection.close();

                Controller.insertBalance(1, users.get(0).getBank().getAccountNumber());
                System.out.println("Successful deposit");
                System.out.println("Account Balance: " + Controller.totalBalance());

                staticBalance.setText(String.valueOf(totalBalance()));

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("");
                alert.setTitle("MARZ BANK");
                alert.setContentText("You have successfully deposited $" + this.amountToBeDeposited.getText() + "\n Your total balance is now $" + Controller.totalBalance());
                alert.showAndWait();


            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("");
            alert.setTitle("MARZ BANK");
            alert.setContentText("Incorrect pin! Please Try Again!");
            alert.showAndWait();
        }

        //closes deposit form
        Stage stage = (Stage) confirmDepButton.getScene().getWindow();
        stage.close();

    }

    //cancel button for deposit
    public void cancelDeposit () {
        Stage stage = (Stage) cancelDepButton.getScene().getWindow();
        stage.close();
    }

    //date for transactions
    public String getDate(){
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM-dd-yyyy hh:mm:ss");
        return dateTimeFormatter.format(localDateTime);
    }



}
