package com.example.bankaccount;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.example.bankaccount.Controller.staticBalance;
import static com.example.bankaccount.Controller.totalBalance;

public class WithdrawController {
    /********************************* WITHDRAW MONEY *********************************/

    @FXML
    private TextField amountToBeWithdrawn;

    @FXML
    private PasswordField withdrawPin;

    @FXML
    private Button cancelWithButton, confirmWithButton;


    @FXML
    public void confirmWithdraw() {
        if (Controller.totalBalance() > Double.parseDouble(amountToBeWithdrawn.getText())){
            if (Integer.parseInt(withdrawPin.getText()) == Controller.users.get(0).getBank().getPin()) {

                String sql = "insert into transactions(date, amount)values(?,?)";

                try {
                    Connection connection = DataBase.getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);

                    preparedStatement.setString(1, getDate());
                    preparedStatement.setDouble(2, Double.parseDouble(amountToBeWithdrawn.getText()));

                    preparedStatement.execute();

                    connection.close();

                    System.out.println("checkpoint");
                    Controller.insertBalance(2, Controller.users.get(0).getBank().getAccountNumber());
                    System.out.println("Successful withdrawal");
                    System.out.println("Account Balance: " + Controller.totalBalance());

                    staticBalance.setText(String.valueOf(totalBalance()));


                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("");
                    alert.setTitle("MARZ BANK");
                    alert.setContentText("You have successfully withdrawn $" + this.amountToBeWithdrawn.getText() + "\n Your total balance is now $" + Controller.totalBalance());
                    alert.showAndWait();



                } catch (SQLException e) {
                    e.printStackTrace();
                }

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("");
                alert.setTitle("MARZ BANK");
                alert.setContentText("Incorrect pin! Please Try Again!");
                alert.showAndWait();
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("");
            alert.setTitle("MARZ BANK");
            alert.setContentText("Insufficient Funds");
            alert.showAndWait();
        }

        //closes registration form
        Stage stage = (Stage) confirmWithButton.getScene().getWindow();
        stage.close();
    }


    //cancel button for withdrawl
    public void cancelWithdraw () {
        Stage stage = (Stage) cancelWithButton.getScene().getWindow();
        stage.close();
    }


    //date for transactions
    public String getDate(){
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM-dd-yyyy hh:mm:ss");
        return dateTimeFormatter.format(localDateTime);
    }
}
