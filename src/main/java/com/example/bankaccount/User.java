package com.example.bankaccount;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {
    private final StringProperty name, id, address;
    private final StringProperty email;
    private final StringProperty phoneNumber;

    private Bank bank;


    public User(String name, String id, String address, String email, String phoneNumber, Bank bank) {
        this.name = new SimpleStringProperty(name);
        this.id = new SimpleStringProperty(id);
        this.address = new SimpleStringProperty(address);
        this.email = new SimpleStringProperty(email);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.bank = bank;
    }

    //name
    public String getName(){
        return name.get();
    }

    public StringProperty nameProperty(){
        return name;
    }

    public void setName(String nm){
        name.set(nm);
    }

    //id
    public String getId() {
        return id.get();
    }

    public StringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }


    //address
    public String getAddress(){
        return address.get();
    }

    public StringProperty addressProperty(){
        return address;
    }

    public void setAddress(String ad){
        address.set(ad);
    }

    //setter for email
    public void setEmail(String em) {
        email.set(em);
    }
    public StringProperty emailProperty() {
        return email;
    }
    //getter for email
    public String getEmail() {
        return email.get();
    }

    //setter for phone number
    public void setPhoneNumber(String pn) {
        phoneNumber.set(pn);
    }
    public StringProperty phoneNumber() {
        return phoneNumber;
    }
    //getter for email
    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    //Bank
    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

}

