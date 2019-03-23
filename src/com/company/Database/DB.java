package com.company.Database;

import com.company.Account;
import com.company.Helpers.Messsage;
import com.company.Program;
import com.company.Transaction;
import com.company.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.util.List;
import java.util.UUID;

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
            Messsage.printSuccess("Överföringen har genomförts");
        } catch (SQLException e) {
            if (e.getErrorCode() == 1264){
                Messsage.printError("Det går inte att genomföra överföringen \nDetta beror på att det inte finns tillräckligt med pengar på kontot");
            }else{
                e.printStackTrace();

            }
        }
    }

    public static void makeCardPayment(String socialNumber, Double amount) {
        CallableStatement cs = callableStatement("{CALL add_card_transaction(?,?)}");
        try {
            cs.setString(1, socialNumber);
            cs.setDouble(2, amount);
            cs.executeUpdate();
            Messsage.printSuccess("Betalningen har genomförts");
        } catch (SQLException e) {
            if (e.getErrorCode() == 1264){
                Messsage.printError("Det går inte att genomföra betalning \nDetta beror på att det inte finns tillräckligt med pengar på kontot");
            }else{
                e.printStackTrace();

            }
        }
    }


    public static void addSalaryTransaction(String socialNumber, Double amount, String senderAccount, Date date) {
        CallableStatement cs = callableStatement("{CALL add_employee_salary(?,?,?,?)}");
        try {
            cs.setString(1, socialNumber);
            cs.setDouble(2, amount);
            cs.setString(3, senderAccount);
            cs.setDate(4, date);
            cs.executeUpdate();
            Messsage.printSuccess("Lön upplagd");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addAutomaticTransaction(String sendingAccount,String recievingAccount,Double amount,String text,Date startingDate ,Date endingDate){
        String eventID = UUID.randomUUID().toString();
        eventID = eventID.replaceAll("-","");
        PreparedStatement ps = preparedStatement("CREATE EVENT " + eventID + "\n" +
                "\t ON SCHEDULE\n" +
                "\t\tEVERY 1 MONTH STARTS ? ENDS ?\n" +
                "\tON COMPLETION NOT PRESERVE\n" +
                "\tDO BEGIN\n" +
                "\t\t INSERT INTO transactions SET transaction_name = ?,`account` = ?,\n" +
                "\t\t  `type` = 'Utgående', transaction_ammount = -?; \n" +
                "\t\t     \n" +
                "\t\t  INSERT INTO transactions SET transaction_name = ?,`account` = ?,\n" +
                "\t\t  `type` = 'Inkommande', transaction_ammount = ?;   \n" +
                "\tEND");
        try {
            ps.setDate(1,startingDate);
            ps.setDate(2,endingDate);
            ps.setString(3,text);
            ps.setString(4,sendingAccount);
            ps.setDouble(5,amount);
            ps.setString(6,text);
            ps.setString(7,recievingAccount);
            ps.setDouble(8,amount);
            ps.executeUpdate();
            Messsage.printSuccess("Automatisk överföring upplagd");
            addAutomaticTransactionToUser(eventID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addAutomaticTransactionToUser(String eventID){
        PreparedStatement ps = preparedStatement("{CALL add_event_info(?,?)}");
        try {
            ps.setString(1,eventID);
            ps.setString(2, Program.getLoggedInUser().getSocial_number());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


