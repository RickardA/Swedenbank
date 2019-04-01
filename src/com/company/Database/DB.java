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
        PreparedStatement ps = preparedStatement("SELECT account_number,account_name,balance,type FROM accountinformation\n" +
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
        CallableStatement cs = callableStatement("{CALL add_transaction(?,?,?,?,?)}");
        try {
            cs.setString(1, transactionName);
            cs.setString(2, sendingAccount);
            cs.setString(3, recievingAccount);
            cs.setDouble(4, amount);
            cs.registerOutParameter(5,Types.INTEGER);
            cs.executeUpdate();
            int result = cs.getInt(5);
            Messsage.printSuccess("Överföringen har genomförts");
        } catch (SQLException e) {
            if (e.getErrorCode() == 1264){
                Messsage.printError("Det går inte att genomföra överföringen \nDetta beror på att det inte finns tillräckligt med pengar på kontot");
            }else{
                e.printStackTrace();

            }
        }
    }

    public static boolean makeCardPayment(String socialNumber, Double amount) {
        CallableStatement cs = callableStatement("{CALL add_card_transaction(?,?,?)}");
        try {
            cs.setString(1, socialNumber);
            cs.setDouble(2, amount);
            cs.registerOutParameter(3, Types.INTEGER);
            cs.executeUpdate();
            int result = cs.getInt(3);
            if (result == 1){
                Messsage.printSuccess("Betalningen har genomförts");
                return true;
            }else{
                return false;
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1264){
                Messsage.printError("Det går inte att genomföra betalning \nDetta beror på att det inte finns tillräckligt med pengar på kontot");
            }else{
                e.printStackTrace();

            }
        }
        return false;
    }


    public static int addSalaryTransaction(String socialNumber, Double amount, String senderAccount, Date date) {
        int result = 0;
        CallableStatement cs = callableStatement("{CALL add_employee_salary(?,?,?,?,?)}");
        try {
            cs.setString(1, socialNumber);
            cs.setDouble(2, amount);
            cs.setString(3, senderAccount);
            cs.setDate(4, date);
            cs.registerOutParameter(5,Types.INTEGER);
            cs.executeUpdate();
            result = cs.getInt(5);
            if (result == 1){
                Messsage.printSuccess("Lön upplagd");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
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
            ps.setString(1,startingDate.toString() + " 01:00:00");
            ps.setString(2,endingDate.toString() + "01:00:00");
            ps.setString(3,text);
            ps.setString(4,sendingAccount);
            ps.setDouble(5,amount);
            ps.setString(6,text);
            ps.setString(7,recievingAccount);
            ps.setDouble(8,amount);
            ps.executeUpdate();
            Messsage.printSuccess("Automatisk överföring upplagd \n Första överföring sker om 1 månad");
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

    public static boolean changeAccountType(String accountType,String accountNumber){
        CallableStatement ps = callableStatement("{CALL change_account_type(?,?,?,?) }");
        try {
            ps.setString(1,accountType);
            ps.setString(2,accountNumber);
            ps.setString(3,Program.getLoggedInUser().getSocial_number());
            ps.registerOutParameter(4, java.sql.Types.INTEGER);
            ps.executeUpdate();
            int result =  ps.getInt(4);
            if (result == 0){
                return false;
            }else{
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void changeAccountName(String accountName, String accountNumber){
        CallableStatement ps = callableStatement("{CALL change_account_name(?,?) }");
        try {
            ps.setString(1,accountNumber);
            ps.setString(2,accountName);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteAccount(String accountNumber){
        CallableStatement cs = callableStatement("{CALL delete_account(?,?,?)}");
        try {
            cs.setString(1,accountNumber);
            cs.registerOutParameter(2,Types.INTEGER);
            cs.setString(3,Program.getLoggedInUser().getSocial_number());
            cs.executeUpdate();
            int result = cs.getInt(2);
            if (result == 0){
                return false;
            }else{
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean createAccount(String accountName){
        CallableStatement cs = callableStatement("{CALL create_account(?,?)}");
        try {
            cs.setString(1,accountName);
            cs.setString(2,Program.getLoggedInUser().getSocial_number());
            cs.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void makePayment(String sendingAccount,String recievingAccount,Double amount,String text,Date executionDate){
        String eventID = UUID.randomUUID().toString();
        eventID = eventID.replaceAll("-","");
        PreparedStatement ps = preparedStatement("CREATE EVENT " + eventID + "\n" +
                "\tON SCHEDULE AT ?\n" +
                "\tDO BEGIN\n" +
                "\t\t INSERT INTO transactions SET transaction_name = ?,`account` = ?,\n" +
                "\t\t  `type` = 'Utgående', transaction_ammount = -?; \n" +
                "\t\t     \n" +
                "\t\t  INSERT INTO transactions SET transaction_name = ?,`account` = ?,\n" +
                "\t\t  `type` = 'Inkommande', transaction_ammount = ?;   \n" +
                "\tEND");
        try {
            ps.setString(1,executionDate.toString() + " 01:00:00");
            ps.setString(2,text);
            ps.setString(3,sendingAccount);
            ps.setDouble(4,amount);
            ps.setString(5,text);
            ps.setString(6,recievingAccount);
            ps.setDouble(7,amount);
            ps.executeUpdate();
            Messsage.printSuccess("Planerad överföring upplagd \n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<Transaction> getLatestTransactions() {
        List<Transaction> transactions;
        ObservableList<Transaction> transactions1 = null;
        PreparedStatement ps;
        ps = preparedStatement("SELECT * FROM transactions_user WHERE social_number = ? ORDER BY `date` DESC LIMIT 5");
        try {
            ps.setString(1,Program.getLoggedInUser().getSocial_number());
            transactions = (List<Transaction>) new ObjectMapper<>(Transaction.class).map(ps.executeQuery());
            transactions1 = FXCollections.observableArrayList(transactions);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions1;
    }

}


