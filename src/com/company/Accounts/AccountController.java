package com.company.Accounts;

import com.company.Account;

public class AccountController {

    public void setAccountToShow(Account choosenAccount){
        System.out.println("The choosen account is " + choosenAccount.getAccount_name());
    }
}
