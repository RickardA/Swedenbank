package com.company.Transactions;

import com.company.Database.DB;
import com.company.Helpers.Error;
import com.company.Helpers.Extractor;
import com.company.Interfaces.ParentController;
import com.company.Navigation.NavController;
import com.company.Program;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.Date;
import java.time.LocalDate;


public class TransactionController implements ParentController {
    private NavController navController;

    @FXML
    private ChoiceBox sendingAccount;
    @FXML
    private ChoiceBox recievingAccountChoiceBox;
    @FXML
    private TextField amountField;
    @FXML
    private TextField textField;
    @FXML
    private Button sendButton;
    @FXML
    private RadioButton intTransactionButton;
    @FXML
    private RadioButton extTransactionButton;
    @FXML
    private TextField recievingAccountText;
    @FXML
    private RadioButton automaticTransactionButton;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    private ToggleGroup toggleGroup = new ToggleGroup();

    private String selectedSendingAccount;
    private String selectedRecievingAccount;
    private String text;
    private double amount;

    @FXML
    private void initialize() {
        setup();
    }

    private void setup() {
        sendButton.setOnMousePressed(event -> {
            if(startDatePicker.isDisabled()){
                makeTransaction();
            }else{ makeAutomaticTransaction();}
        });
        Program.getAccounts().forEach(account -> {
            sendingAccount.getItems().add(account.getAccount_name() + " " + account.getAccount_number());
            recievingAccountChoiceBox.getItems().add(account.getAccount_name() + " " + account.getAccount_number());
        });
        amountField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                amountField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        intTransactionButton.setToggleGroup(toggleGroup);
        extTransactionButton.setToggleGroup(toggleGroup);
        toggleGroup.selectedToggleProperty().addListener((observable, oldVal, newVal) -> {
            if (newVal.toString().contains("extTransactionButton")) {
                recievingAccountChoiceBox.setVisible(false);
                recievingAccountText.setVisible(true);
            } else if (newVal.toString().contains("intTransactionButton")) {
                recievingAccountChoiceBox.setVisible(true);
                recievingAccountText.setVisible(false);
            }
        });
        automaticTransactionButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean isSelected) {
                if (isSelected){
                    startDatePicker.setDisable(false);
                    endDatePicker.setDisable(false);
                }else {
                    startDatePicker.setDisable(true);
                    endDatePicker.setDisable(true);
                }
            }
        });
    }

    private void makeTransaction() {
        if (getInputInformation()) {
            DB.makeTransaction(text, selectedSendingAccount, selectedRecievingAccount, amount);
            resetInputs();
        }
    }

    private void makeAutomaticTransaction(){
        if (getInputInformation()){
            LocalDate dateFromStartPicker = startDatePicker.getValue();
            LocalDate dateFromEndPicker = endDatePicker.getValue();
            if (dateFromStartPicker != null && dateFromEndPicker != null){
                Date startDate = Date.valueOf(dateFromStartPicker);
                Date endDate =  Date.valueOf(dateFromEndPicker);
                DB.addAutomaticTransaction(selectedSendingAccount,selectedRecievingAccount,amount,text,startDate,endDate);
                resetInputs();
            }else {
                Error.printError("Vänligen välj en dag som överföringen skall utföras på");
            }

        }
    }

    private boolean getInputInformation(){
        selectedSendingAccount = Extractor.extractAccountNumber(sendingAccount.getValue());
        selectedRecievingAccount = recievingAccountChoiceBox.isVisible() ? Extractor.extractAccountNumber(recievingAccountChoiceBox.getValue()) : Extractor.checkIfEmpty(recievingAccountText.getText());
        text = textField.getText();
        amount = amountField.getText().isEmpty() ? 0 : Double.parseDouble(amountField.getText());
        if (selectedSendingAccount != null && selectedRecievingAccount != null && !text.isEmpty() && amount != 0.0) {
            if (selectedRecievingAccount.matches("\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d")){
                return true;
            }else {
                Error.printError("Fel format på kontonummer \n (xxx.xxx.xxx-x)");
                return false;
            }

        } else {
            Error.printError("Vänligen fyll i alla fält");
            return false;
        }
    }

    private void resetInputs() {
        sendingAccount.getSelectionModel().clearSelection();
        recievingAccountChoiceBox.getSelectionModel().clearSelection();
        textField.setText("");
        amountField.setText("");
        recievingAccountText.setText("");
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
    }

    @Override
    public void setParent(Object parent) {
        navController = (NavController) parent;
    }
}
