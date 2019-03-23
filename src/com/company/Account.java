package com.company;

import com.company.Annotations.Column;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class Account {

    @Column
    private String account_number;
    @Column
    private String account_name;
    @Column
    private double balance;
    @Column
    private String type;

    private ObservableList<Transaction> transactions;

    public String getAccount_number() {
        return account_number;
    }

    public String getAccount_name() {
        return account_name;
    }

    public double getBalance() {
        return balance;
    }

    public void setTransactions(ObservableList<Transaction> transactions) {
        this.transactions = FXCollections.observableArrayList(transactions);
    }

    public ObservableList<Transaction> getTransactions(){
        return transactions;
    }

    public String getType() {
        return type;
    }
}
