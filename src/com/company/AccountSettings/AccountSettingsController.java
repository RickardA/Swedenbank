package com.company.AccountSettings;

import com.company.Account;
import com.company.Interfaces.ParentController;
import com.company.Navigation.NavController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class AccountSettingsController implements ParentController {
    private NavController navController;

    @FXML
    private VBox accountNameBox;
    @FXML
    private VBox accountNumberBox;
    @FXML
    private VBox savingsButtonBox;
    @FXML
    private VBox cardButtonBox;
    @FXML
    private VBox salaryButtonBox;
    @FXML
    private VBox deleteButtonBox;
    @FXML
    private Button applyButton;
    @FXML
    private Button createAccountButton;
    @FXML
    private TextField accountNameField;
    @FXML
    private Text accountNumberText;
    @FXML
    private RadioButton savingsButton;
    @FXML
    private RadioButton cardButton;
    @FXML
    private RadioButton salaryButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Text saldoText;
    private Account account;
    private ToggleGroup toggleGroup = new ToggleGroup();

    @FXML
    private void initialize(){
        setup();
    }

    private void setup(){
        accountNameField.setText(account.getAccount_name());
        accountNumberText.setText(account.getAccount_number());
        saldoText.setText(""+account.getBalance());
        savingsButton.setToggleGroup(toggleGroup);
        cardButton.setToggleGroup(toggleGroup);
        salaryButton.setToggleGroup(toggleGroup);
        switch(account.getType()){
            case "Sparkonto":
                savingsButton.fire();
                break;
            case "Lönekonto":
                salaryButton.fire();
                break;
            case "Kort":
                cardButton.fire();
                break;
        }
        applyButton.setOnMousePressed(event -> applyChanges());
    }

    private void applyChanges(){

    }

    public void setAccount(Account account){
        this.account = account;
    }

    @Override
    public void setParent(Object parent) {
        navController = (NavController)parent;
    }
}
