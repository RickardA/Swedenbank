package com.company.Home;

import com.company.Account;
import com.company.Database.DB;
import com.company.Helpers.Loader;
import com.company.Interfaces.ParentController;
import com.company.Navigation.NavController;
import com.company.Program;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.TableHeaderRow;

public class HomeController implements ParentController {

    @FXML
    private TableColumn<Account, String> accountNameColumn;
    @FXML
    private TableColumn<Account, String> accountBalanceColumn;
    @FXML
    private TableColumn<Account,String> accountNumberColumn;
    @FXML
    private TableView accountsTable;
    private NavController parentController;

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
        parentController.switchToChoosenAccount((Account) accountsTable.getSelectionModel().getSelectedItem());
    }

    @Override
    public void setParent(Object parent) {
        parentController = (NavController) parent;
    }
}
