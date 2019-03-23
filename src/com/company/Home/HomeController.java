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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.TableHeaderRow;
import javafx.scene.input.MouseButton;

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
    private ContextMenu cm;

    @FXML
    private void initialize() {
        setup();
        loadAccountInformation();
        showAccountInformation();
        createAccountMenu();
    }

    private void setup() {
        accountNameColumn.setCellValueFactory(new PropertyValueFactory<>("account_name"));
        accountBalanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));
        accountNumberColumn.setCellValueFactory(new PropertyValueFactory<>("account_number"));
        accountsTable.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                showSelectedAccount();
            }
            else if (event.getButton() == MouseButton.SECONDARY){
                showItemOptions(event.getScreenX(),event.getScreenY());
            }
        });
    }

    private void showItemOptions(double x,double y){
        cm.show(accountsTable,x,y);
    }

    private void createAccountMenu(){
        cm = new ContextMenu();
        MenuItem settings = new MenuItem("Inställningar");
        settings.setOnAction(event -> parentController.displayAccountSettingsPage((Account) accountsTable.getSelectionModel().getSelectedItem()));
        cm.getItems().add(settings);
    }

    private void loadAccountInformation() {
        Program.getLoggedInUser().setAccounts(DB.getUsersAccount(Program.getLoggedInUser()));
    }

    private void showAccountInformation(){
        accountsTable.setItems(Program.getLoggedInUser().getAccounts());
    }

    private void showSelectedAccount() {
        if (accountsTable.getSelectionModel().getSelectedItem() != null) {
            parentController.switchToChoosenAccount((Account) accountsTable.getSelectionModel().getSelectedItem());
        }
    }

    @Override
    public void setParent(Object parent) {
        parentController = (NavController) parent;
    }
}
