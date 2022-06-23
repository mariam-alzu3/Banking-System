package com.example.bankaccount;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisterController {

    /********************************* REGISTRATION FORM *********************************/
    @FXML
    private TextField name, email, phone, address, id;
    @FXML
    private RadioButton male, female, other;
    @FXML
    private PasswordField pin;
    @FXML
    private Button createButton;
    @FXML
    private AnchorPane regPane;
    @FXML
    private DatePicker dob;

    @FXML
    public void create(){

        //closes registration form
        Stage stage = (Stage) createButton.getScene().getWindow();
        stage.close();

        //sql stmt for inserting info into the users table
        String sql = "insert into users(id, name," +
                "gender,address,email,phone,dob)values(?,?,?,?,?,?,?)";

        //3 options for gender and turning the radio buttons to strings to insert into table
        String gender;
        if (male.isSelected()) {
            gender = "Male";
        } else if (female.isSelected()){
            gender = "Female";
        } else {
            gender = "Other";
        }

        try {
            Connection connection = DataBase.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql);

            //get info from the text fields to insert into table
            pstmt.setString(1, id.getText());
            pstmt.setString(2, name.getText());
            pstmt.setString(3, gender);
            pstmt.setString(4, address.getText());
            pstmt.setString(5, email.getText());
            pstmt.setString(6, phone.getText());
            pstmt.setString(7, dob.getEditor().getText());

            pstmt.execute();

            // accounts table; id and pin
            sql = "insert into accounts(id, pin)values(?,?)";

            pstmt = connection.prepareStatement(sql);

            //get id and pin from text fields
            pstmt.setString(1, id.getText());
            pstmt.setInt(2, Integer.parseInt(pin.getText()));
            pstmt.execute();

            // assign account number by first selecting the account column
            sql = "select account from accounts";

            ResultSet rs = connection.createStatement().executeQuery(sql);

            int acc = 0;
            while (rs.next()){
                acc = rs.getInt(1);
            }

            connection.close();

            //pop-up for successful registration
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Registration successful");
            alert.setTitle("MARZ BANK");
            alert.setContentText("Account details\n" +
                    "Name: " + name.getText() + "\nAccount number: " + (acc) + "\n\nnow login with your account number\n" +
                    " and pin entered during registration");
            alert.showAndWait();

            // i dont think we need this since were closing the window??
            this.name.clear();
            //this.pinVerification.clear();
            this.pin.clear();
            this.address.clear();
            this.email.clear();
            this.phone.clear();
            this.id.clear();


            //alert for wrong or missing input
        } catch (SQLException | NullPointerException | NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("");
            alert.setTitle("MARZ BANK");
            alert.setContentText("ERROR Please recheck your input data \nand make sure you fill all required fields");
            alert.showAndWait();
            e.printStackTrace();
        }
    }
}
