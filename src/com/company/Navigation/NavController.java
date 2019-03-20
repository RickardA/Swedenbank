package com.company.Navigation;

import com.company.Account;
import com.company.Accounts.AccountController;
import com.company.Home.HomeController;
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
        accountController.setAccountToShow(choosenAccount);
         displayUI("../Accounts/account.fxml",accountController);
    }
}
