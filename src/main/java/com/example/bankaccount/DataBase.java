package com.example.bankaccount;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DataBase {
    private static final String SQLITE_CONN = "jdbc:sqlite:bankAccount.sqlite";
    //private static final String SQLITE_CONN = "jdbc:sqlite:/Users/mariam/Desktop/1703-p3r.s0n@l/Coding Projects/Java/Bank Account copy/bank.sqlite";


    public static Connection getConnection() throws SQLException{
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(SQLITE_CONN);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("not connected");
        return null;
    }
}
