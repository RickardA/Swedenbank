package com.company;

import com.company.Annotations.Column;

public class Account {

    @Column
    private String account_number;
    @Column
    private String account_name;
    @Column
    private double balance;

    public String getAccount_number() {
        return account_number;
    }

    public String getAccount_name() {
        return account_name;
    }

    public double getBalance() {
        return balance;
    }
}
