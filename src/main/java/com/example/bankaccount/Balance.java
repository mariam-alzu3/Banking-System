package com.example.bankaccount;

public class Balance {
    private double amount;
    private int type;

    public Balance(double amount, int type){
        this.amount = amount;
        this.type = type;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmount(){
        return amount;
    }

    public void setType(int type){
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
