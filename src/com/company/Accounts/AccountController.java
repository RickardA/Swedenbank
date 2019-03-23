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
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

public class AccountController implements ParentController {

    private NavController parentController;
    private Account account;

    @FXML
    private TableColumn<Transaction, String> transactionDateColumn;
    @FXML
    private TableColumn<Transaction, String> transactionTypeColumn;
    @FXML
    private TableColumn<Transaction,String> transactionTextColumn;
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
    private ToggleButton showAllTransactions;


    @FXML
    private void initialize() {
        setup();
        showTransactions(true);
    }

    public void setAccountToShow(Account chosenAccount){
        account = chosenAccount;
    }

    public void showTransactions(Boolean limit){
        account.setTransactions(DB.getAccountTransactions(Program.getLoggedInUser(),account.getAccount_number(),limit));
        showAccountInformation();
    }

    private void setup() {
        accountName.setText(account.getAccount_name());
        accountNumber.setText("Nr: "+ account.getAccount_number());
        accountBalance.setText("Saldo: "+account.getBalance());
        transactionDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        transactionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        transactionTextColumn.setCellValueFactory(new PropertyValueFactory<>("transaction_name"));
        transactionAmountColumn.setCellValueFactory(new PropertyValueFactory<>("transaction_ammount"));
        showAllTransactions.setOnMousePressed(event -> {
            if (showAllTransactions.isSelected()){
                showTransactions(true);
            }else{
                showTransactions(false);
            }
        });
    }

     private void showAccountInformation(){
        transactionTable.setItems(account.getTransactions());
       /* setTransactionColor();*/
    }

    /*private void setTransactionColor(){
        AtomicInteger counter = new AtomicInteger(0);
        List<Transaction> outgoingTransactions = calculateOutgoingTransactions();
        transactionTable.setRowFactory(tv -> {
            int index = counter.getAndIncrement();
            System.out.println(index);
            TableRow<Transaction> row = new TableRow<>();
            if(index <= outgoingTransactions.size()-1) {
                BooleanBinding outgoing = row.itemProperty().isEqualTo(outgoingTransactions.get(index));
                row.styleProperty().bind(Bindings.when(outgoing)
                        .then("-fx-background-color: red ;")
                        .otherwise("-fx-background-color: green ;"));
            }
            return row ;
        });
    }*/

   /* private List<Transaction> calculateOutgoingTransactions(){
        List<Transaction> outgoingTransactions = new ArrayList<>();
        account.getTransactions().forEach(transaction -> {
            if(transaction.getAccount_type().trim().equals(account.getAccount_number().trim())){
                System.out.println("printing from something fun " + transaction.getAccount() + " " + account.getAccount_number());
                outgoingTransactions.add(transaction);
            }
        });
        return outgoingTransactions;
    }*/

    @Override
    public void setParent(Object parent) {
        parentController = (NavController) parent;
    }
}
