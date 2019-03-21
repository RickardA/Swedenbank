package com.company.Database;

import com.company.Account;
import com.company.Program;
import com.company.Transaction;
import com.company.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.rmi.activation.ActivationGroup_Stub;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public abstract class DB {

    public static PreparedStatement preparedStatement(String SQLQuery) {
        return Database.getInstance().prepareStatement(SQLQuery);
    }

    public static CallableStatement callableStatement(String SQLQuery) {
        return Database.getInstance().callableStatement(SQLQuery);
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

    public static ObservableList<Account> getUsersAccount(User user) {
        List<Account> accounts;
        ObservableList<Account> accounts1 = null;
        PreparedStatement ps = preparedStatement("SELECT account_number,account_name,balance FROM accountinformation\n" +
                "WHERE social_number = ?");
        try {
            ps.setString(1, user.getSocial_number());
            accounts = (List<Account>) new ObjectMapper<>(Account.class).map(ps.executeQuery());
            accounts1 = FXCollections.observableArrayList(accounts);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts1;
    }

    public static ObservableList<Transaction> getAccountTransactions(User user, String accountNumber, Boolean limit) {
        System.out.println("getting account transactions " + accountNumber);
        List<Transaction> transactions;
        ObservableList<Transaction> transactions1 = null;
        PreparedStatement ps;
        if (limit) {
            ps = preparedStatement("SELECT * FROM transactions WHERE " +
                    "account = ? ORDER BY date DESC LIMIT 10");
        } else {
            ps = preparedStatement("SELECT * FROM transactions WHERE " +
                    "account = ? ORDER BY date DESC");
        }
        try {
            ps.setString(1, accountNumber);
            transactions = (List<Transaction>) new ObjectMapper<>(Transaction.class).map(ps.executeQuery());
            transactions1 = FXCollections.observableArrayList(transactions);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions1;
    }

    public static void makeTransaction(String transactionName, String sendingAccount, String recievingAccount, Double amount) {
        CallableStatement cs = callableStatement("{CALL add_transaction(?,?,?,?)}");

        try {
            cs.setString(1, transactionName);
            cs.setString(2, sendingAccount);
            cs.setString(3, recievingAccount);
            cs.setDouble(4, amount);
            cs.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void makeCardPayment(String socialNumber, Double amount) {
        CallableStatement cs = callableStatement("{CALL add_card_transaction(?,?)}");
        try {
            cs.setString(1, socialNumber);
            cs.setDouble(2, amount);
            cs.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createSchedueldEvent(String socialNumber, Double amount, String sender, String date) {
        String personnummer = Program.getLoggedInUser().getSocial_number();
        PreparedStatement ps = preparedStatement(
                "CREATE EVENT userEvent"+ personnummer + " \n" +
                        "ON SCHEDULE EVERY 1 DAY \n" +
                        "DO BEGIN \n" +
                        "CALL check_transactions(?);\n" +
                        "END");
        try {
            ps.setString(1, "user" +personnummer);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createSchedueldTransaction(String socialNumber, Double amount, String senderAccount, String recieverAccount, String date, String text, String type) {
        String personnummer = Program.getLoggedInUser().getSocial_number();
        PreparedStatement ps = preparedStatement("CREATE TABLE user" + personnummer + " ( \n" +
                "        id INT(11) NOT NULL AUTO_INCREMENT, \n" +
                "        social_number VARCHAR(13),\n" +
                "        amount DOUBLE,  \n" +
                "        sender_account VARCHAR(50),\n" +
                "        recieving_account VARCHAR(50),\n" +
                "        transaction_date VARCHAR(50),\n" +
                "        `text` VARCHAR(50),\n" +
                "\t\t  PRIMARY KEY (`id`));");
        try {
            ps.executeUpdate();
            addSchedueldTransaction(socialNumber, amount, senderAccount, recieverAccount, date, text, type);
        } catch (SQLException e) {
            if (e.getErrorCode() == 1050) {
                 //1050 if already exists
                addSchedueldTransaction(socialNumber, amount, senderAccount, recieverAccount, date, text, type);
            } else {
                e.printStackTrace();
            }
        }
    }

    private static void addSchedueldTransaction(String socialNumber, Double amount, String senderAccount, String recieverAccount, String date, String text, String type) {
        String personnummer = Program.getLoggedInUser().getSocial_number();
        switch (type) {
            case "Lön":
                PreparedStatement ps = preparedStatement("INSERT INTO user"+personnummer+ " SET social_number = ?,amount = ?,sender_account = ?,transaction_date = ?, text=?");
                try {
                    ps.setString(1,socialNumber);
                    ps.setDouble(2,amount);
                    ps.setString(3,senderAccount);
                    ps.setString(4,date);
                    ps.setString(5,text);
                    ps.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case "Överföring":
                break;
        }
    }
}

