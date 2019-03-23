package com.company.Navigation;

import com.company.Account;
import com.company.Accounts.AccountController;
import com.company.Card.CardController;
import com.company.Company.CompanyController;
import com.company.Database.DB;
import com.company.Home.HomeController;
import com.company.Program;
import com.company.Transactions.TransactionController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Calendar;

public class NavController {

    @FXML
    private BorderPane borderPane;
    public void displayHomePage(){
        HomeController homeController = new HomeController();
        homeController.setParent(this);
        displayUI("../Home/home.fxml",homeController);
    }

    private void displayUI(String src,Object controller){
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(src));
            loader.setController(controller);
            root = loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        borderPane.setCenter(root);
    }

    public void switchToChoosenAccount(Account choosenAccount){
        AccountController accountController = new AccountController();
        accountController.setParent(this);
        accountController.setAccountToShow(choosenAccount);
         displayUI("../Accounts/account.fxml",accountController);
    }

    public void displayTransactionPage(){
        TransactionController transactionController = new TransactionController();
        transactionController.setParent(this);
        displayUI("../Transactions/transaction.fxml",transactionController);
    }

    public void displayCardTransactionPage(){
        CardController cardController = new CardController();
        cardController.setParent(this);
        displayUI("../Card/card.fxml",cardController);
    }

    public void displayCompanyPage(){
        CompanyController companyController = new CompanyController();
        companyController.setParent(this);
        displayUI("../Company/company.fxml",companyController);
    }

}
