package com.company.Database;

import com.company.Account;
import com.company.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class DB {

    public static PreparedStatement preparedStatement(String SQLQuery) {
        return Database.getInstance().prepareStatement(SQLQuery);
    }

    public static User getMatchingUser(String social_number, String password) {
        User result = null;
        PreparedStatement ps = preparedStatement("SELECT * FROM users WHERE social_number = ? AND password = ?");
        try {
            ps.setString(1, social_number);
            ps.setString(2, password);
            if (ps.executeQuery().next()) {
                result = (User) new ObjectMapper<>(User.class).mapOne(ps.executeQuery());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ObservableList<Account> getUsersAccount(User user){
        List<Account> accounts = new ArrayList<>();
        ObservableList<Account> accounts1 = null;
        PreparedStatement ps = preparedStatement("SELECT account_number,account_name,balance FROM accountinformation\n" +
                "WHERE social_number = ?");
        try {
            ps.setString(1,user.getSocial_number());
            accounts = (List<Account>) new ObjectMapper<>(Account.class).map(ps.executeQuery());
            accounts1 = FXCollections.observableArrayList(accounts);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts1;
    }
}
