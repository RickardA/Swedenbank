package com.company;

import com.company.Annotations.Column;

import java.sql.Timestamp;

public class Transaction {

    @Column
    private String transaction_name;
    @Column
    private String account;
    @Column
    private String type;
    @Column
    private double transaction_ammount;
    @Column
    private Timestamp date;

    public String getTransaction_name() {
        return transaction_name;
    }

    public String getAccount() {
        return account;
    }

    public String getType() {
        return type;
    }

    public double getTransaction_ammount() {
        return transaction_ammount;
    }

    public Timestamp getDate() {
        return date;
    }
}
