package com.company;

import com.company.Annotations.Column;
import com.company.Database.DB;
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

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
        DB.changeAccountName(account_name,this.getAccount_number());
    }

    public boolean setType(String type) {
        Boolean result = DB.changeAccountType(type,this.getAccount_number());
        if(result == true){
            this.type = type;
            return true;
        }else{
            return false;
        }
    }
}
