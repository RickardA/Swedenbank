package com.company.AccountSettings;

import com.company.Account;
import com.company.Database.DB;
import com.company.Helpers.Messsage;
import com.company.Interfaces.ParentController;
import com.company.Navigation.NavController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountSettingsController implements ParentController {
    private NavController navController;

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
    private RadioButton undefinedButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Text saldoText;
    private Account account;
    private ToggleGroup toggleGroup = new ToggleGroup();
    private boolean isTypeChanged = false;
    private boolean isNameChanged = false;

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
        undefinedButton.setToggleGroup(toggleGroup);
        switch(account.getType()){
            case "Sparkonto":
                savingsButton.fire();
                break;
            case "Lönekonto":
                salaryButton.fire();
                break;
            case "Kortkonto":
                cardButton.fire();
                break;
            case "Ingen Koppling":
                undefinedButton.fire();
                break;
        }
        deleteButton.setOnMousePressed(event -> deleteAccount());
        applyButton.setOnMousePressed(event -> applyChanges());
        toggleGroup.selectedToggleProperty().addListener((observable, oldVal, newVal) -> {
            if (newVal.toString().contains(account.getType())){
                isTypeChanged = false;
            }else{
                isTypeChanged = true;
            }
            checkIfChangesIsMade();
        });
        accountNameField.textProperty().addListener((observable,oldVal,newVal) -> {
            if (newVal.equals(account.getAccount_name())){
                isNameChanged = false;
            }else{
                isNameChanged = true;
            }
            checkIfChangesIsMade();
        });
    }

    private void checkIfChangesIsMade(){
        if (isTypeChanged == true || isNameChanged == true){
            applyButton.setDisable(false);
        }else {
            applyButton.setDisable(true);
        }
    }


    private void applyChanges(){
        int success = 0;
        String type = "";
        Pattern pattern = Pattern.compile("(?:^|)'([^']*?)'(?:|$)");
        Matcher matcher = pattern.matcher(toggleGroup.getSelectedToggle().toString());
        if (matcher.find()){
           type = matcher.group(1);
            System.out.println(type);
            if(!type.equals(account.getType()) || !type.equals("Ingen Koppling")) {
                Boolean result = account.setType(type);
                System.out.println(result);
                if (result == false) {
                    Messsage.printError("Det finns redan ett konto med denna typen!");
                    applyButton.setDisable(false);
                }else
                    success++;
            }else{
                success++;
            }
        }else{
            System.out.println("Something went wrong");
        }
        if (success == 1){
            account.setAccount_name(accountNameField.getText());
            applyButton.setDisable(true);
            Messsage.printSuccess("Ändringar genomförda");
        }
    }

    public void setAccount(Account account){
        this.account = account;
    }

    private void deleteAccount(){
        boolean result = DB.deleteAccount(account.getAccount_number());
        if (result == true){
            Messsage.printSuccess("Konto raderat");
            navController.displayHomePage();
        }else {
            Messsage.printError("Kan inte radera konto, får inte inehålla pengar!");
        }
    }

    @Override
    public void setParent(Object parent) {
        navController = (NavController)parent;
    }
}
