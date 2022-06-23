package com.example.bankaccount;

import javafx.beans.property.*;

public class Transactions {
    private final StringProperty date;
    private final IntegerProperty refNumber;
    private final DoubleProperty amount;
    private final StringProperty type;


    public Transactions(String date, Integer refNumber, String type, Double amount) {
        this.date = new SimpleStringProperty(date);
        this.refNumber = new SimpleIntegerProperty(refNumber);
        this.type = new SimpleStringProperty(type);
        this.amount = new SimpleDoubleProperty(amount);

    }

    public String getDate(){
        return date.get();
    }

    public StringProperty dateProperty(){
        return date;
    }

    public void setDate(String date){
        this.date.set(date);
    }

    public Integer getRefNumber(){
        return refNumber.get();
    }

    public IntegerProperty refNumberProperty(){
        return refNumber;
    }

    public void setRefNumber(Integer refNumber){
        this.refNumber.set(refNumber);
    }

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public double getAmount() {
        return amount.get();
    }

    public DoubleProperty amountProperty() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount.set(amount);
    }
}
