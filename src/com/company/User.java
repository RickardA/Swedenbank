package com.company;

import com.company.Annotations.Column;
import javafx.collections.ObservableList;

public class User {

    @Column
    String first_name;
    @Column
    String last_name;
    @Column
    String social_number;
    @Column
    String email;

    private ObservableList<Account> accounts = null;
    private ObservableList<Transaction> latestTransactions;


    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getSocial_number() {
        return social_number;
    }

    public String getEmail() {
        return email;
    }

    public ObservableList<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(ObservableList<Account> accounts) {
        this.accounts = accounts;
    }

    public ObservableList<Transaction> getLatestTransactions() {
        return latestTransactions;
    }

    public void setLatestTransactions(ObservableList<Transaction> latestTransactions) {
        this.latestTransactions = latestTransactions;
    }
}
