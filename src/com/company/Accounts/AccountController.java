package com.company.Accounts;

import com.company.Account;
import com.company.Database.DB;
import com.company.Interfaces.ParentController;
import com.company.Navigation.NavController;
import com.company.Program;
import com.company.Transaction;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

public class AccountController implements ParentController {

    private NavController parentController;
    private Account account;

    @FXML
    private TableColumn<Transaction, String> transactionNameColumn;
    @FXML
    private TableColumn<Transaction, String> transactionSenderColumn;
    @FXML
    private TableColumn<Transaction,String> transactionRecieverColumn;
    @FXML
    private TableColumn<Transaction,String> transactionAmountColumn;
    @FXML
    private TableView transactionTable;
    @FXML
    private Text accountName;
    @FXML
    private Text accountNumber;
    @FXML
    private Text accountBalance;


    @FXML
    private void initialize() {
        setup();
        loadAccountInformation();
        showAccountInformation();
    }

    public void setAccountToShow(Account chosenAccount){
        account = chosenAccount;
        account.setTransactions(DB.getAccountTransactions(Program.getLoggedInUser(),account.getAccount_number()));
    }

    private void setup() {
        System.out.println("setup is running");
        System.out.println(account.getAccount_name());
        accountName.setText(account.getAccount_name());
        accountNumber.setText("Nr: "+ account.getAccount_number());
        accountBalance.setText("Saldo: "+account.getBalance());
        transactionNameColumn.setCellValueFactory(new PropertyValueFactory<>("transaction_name"));
        transactionSenderColumn.setCellValueFactory(new PropertyValueFactory<>("sending_account"));
        transactionRecieverColumn.setCellValueFactory(new PropertyValueFactory<>("recieving_account"));
        transactionAmountColumn.setCellValueFactory(new PropertyValueFactory<>("transaction_ammount"));
    }

    private void loadAccountInformation() {
        Program.setAccounts(DB.getUsersAccount(Program.getLoggedInUser()));
    }

     private void showAccountInformation(){
        transactionTable.setItems(account.getTransactions());
    }

    @Override
    public void setParent(Object parent) {
        parentController = (NavController) parent;
    }
}
