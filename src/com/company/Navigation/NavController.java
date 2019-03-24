package com.company.Navigation;

import com.company.Account;
import com.company.AccountSettings.AccountSettingsController;
import com.company.Accounts.AccountController;
import com.company.Card.CardController;
import com.company.Company.CompanyController;
import com.company.Home.HomeController;
import com.company.Transactions.TransactionController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

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

    public void displayAccountSettingsPage(Account account){
        AccountSettingsController accountSettingsController = new AccountSettingsController();
        accountSettingsController.setParent(this);
        accountSettingsController.setAccount(account);
        displayUI("../AccountSettings/accountSettings.fxml",accountSettingsController);
    }

    public void test(){
    }

}
