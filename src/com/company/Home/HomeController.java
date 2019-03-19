package com.company.Home;

import com.company.Account;
import com.company.Database.DB;
import com.company.Helpers.Loader;
import com.company.Navigation.NavController;
import com.company.Program;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class HomeController {

    @FXML
    private TableColumn<Account, String> accountNameColumn;
    @FXML
    private TableColumn<Account, String> accountBalanceColumn;
    @FXML
    private TableColumn<Account,String> accountNumberColumn;
    @FXML
    private TableView accountsTable;

    @FXML
    private void initialize() {
        setup();
        loadAccountInformation();
        showAccountInformation();
    }

    private void setup() {
        accountNameColumn.setCellValueFactory(new PropertyValueFactory<>("account_name"));
        accountBalanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));
        accountNumberColumn.setCellValueFactory(new PropertyValueFactory<>("account_number"));
        accountsTable.setOnMousePressed(event -> showSelectedAccount());
    }

    private void loadAccountInformation() {
        Program.setAccounts(DB.getUsersAccount(Program.getLoggedInUser()));
    }

    private void showAccountInformation(){
        accountsTable.setItems(Program.getAccounts());
    }

    private void showSelectedAccount() {
        System.out.println(accountsTable.getSelectionModel().getSelectedItem());
        NavController navController = Loader
                .getFXMLController("../Navigation/nav.fxml")
                .getController();
        navController.switchToChoosenAccount((Account) accountsTable.getSelectionModel().getSelectedItem());
    }
}
