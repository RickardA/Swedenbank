package com.company.Navigation;

import com.company.Account;
import com.company.Accounts.AccountController;
import com.company.Helpers.Loader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class NavController {

    @FXML
    private BorderPane borderPane;
    public void displayHomePage(){
        displayUI("../Home/home.fxml");
    }

    private void displayUI(String src){
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource(src));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("printing from display ui");
        borderPane.setCenter(root);
    }

    public void switchToChoosenAccount(Account choosenAccount){
        displayUI("../Accounts/account.fxml");
        setChoosenAccount(choosenAccount);
    }

    private void setChoosenAccount(Account choosenAccount){
        AccountController accountController = Loader
                .getFXMLController("../Accounts/account.fxml")
                .getController();
        accountController.setAccountToShow(choosenAccount);
    }
}
