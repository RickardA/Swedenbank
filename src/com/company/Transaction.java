package com.company;

import com.company.Annotations.Column;

public class Transaction {

    @Column
    private String transaction_name;
    @Column
    private String sending_account;
    @Column
    private String recieving_account;
    @Column
    private double transaction_ammount;

    public String getTransaction_name() {
        return transaction_name;
    }

    public String getSending_account() {
        return sending_account;
    }

    public String getRecieving_account() {
        return recieving_account;
    }

    public double getTransaction_ammount() {
        return transaction_ammount;
    }
}
