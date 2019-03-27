package com.company.Payment;

import com.company.Database.DB;
import com.company.Helpers.Extractor;
import com.company.Helpers.Messsage;
import com.company.Interfaces.ParentController;
import com.company.Navigation.NavController;
import com.company.Program;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Date;
import java.time.LocalDate;


public class PaymentController implements ParentController {
    private NavController navController;

    @FXML
    private ChoiceBox sendingAccount;
    @FXML
    private TextField amountField;
    @FXML
    private TextField textField;
    @FXML
    private Button sendButton;
    @FXML
    private RadioButton plusgiroButton;
    @FXML
    private RadioButton bankgiroButton;
    @FXML
    private TextField recievingAccountText;
    @FXML
    private DatePicker datePicker;

    private ToggleGroup toggleGroup = new ToggleGroup();

    private String selectedSendingAccount;
    private String selectedRecievingAccount;
    private String text;
    private Date date;
    private String accountControl;
    private String accountNumberFormat;
    private LocalDate dateFromPicker;
    private double amount;

    @FXML
    private void initialize() {
        setup();
    }

    private void setup() {
        accountControl = "\\d{1,3}\\.\\d{2}\\.\\d{2}-\\d";
        sendButton.setOnMousePressed(event -> makeTransaction());

        Program.getLoggedInUser().getAccounts().forEach(account -> {
            if (!account.getType().matches("Företagskonto")) {
                sendingAccount.getItems().add(account.getAccount_name() + " " + account.getAccount_number());
            }
        });
        amountField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                amountField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        plusgiroButton.setToggleGroup(toggleGroup);
        bankgiroButton.setToggleGroup(toggleGroup);
        toggleGroup.selectedToggleProperty().addListener((observable, oldVal, newVal) -> {
            if (newVal.toString().contains("plusgiroButton")) {
                accountControl = "\\d{1,3}\\.\\d{2}\\.\\d{2}-\\d";
                accountNumberFormat = "xxx.xx.xx-x/xx.xx.xx-x/x.xx.xx-x";
            } else if (newVal.toString().contains("bankgiroButton")) {
                accountControl = "\\d{3,4}-\\d{4}";
                accountNumberFormat = "xxx-xxxx/xxxx-xxxx";
            }
        });
    }

    private void makeTransaction() {
        LocalDate dateRightNow = LocalDate.now();
        if (getInputInformation()) {
            if (!dateFromPicker.isBefore(dateRightNow)) {
                if (dateFromPicker.equals(dateRightNow)) {
                    DB.makeTransaction(text, selectedSendingAccount, selectedRecievingAccount, amount);
                } else {
                    DB.makePayment(selectedSendingAccount, selectedRecievingAccount, amount, text, date);
                }
                resetInputs();
            }else{
                Messsage.printError("Datumet har redan passerats, vänligen välj ett annat");
            }
        }
    }

    private boolean getInputInformation() {
        selectedSendingAccount = Extractor.extractAccountNumber(sendingAccount.getValue());
        selectedRecievingAccount = Extractor.checkIfEmpty(recievingAccountText.getText());
        dateFromPicker = datePicker.getValue();
        text = textField.getText();
        amount = amountField.getText().isEmpty() ? 0 : Double.parseDouble(amountField.getText());
        if (selectedSendingAccount != null && selectedRecievingAccount != null && !text.isEmpty() && amount != 0.0 && dateFromPicker != null) {
            if (selectedRecievingAccount.matches(accountControl)) {
                date = Date.valueOf(dateFromPicker);
                return true;
            } else {
                Messsage.printError("Fel format på kontonummer, följande format är tillåtna \n" + accountNumberFormat);
                return false;
            }

        } else {
            Messsage.printError("Vänligen fyll i alla fält");
            return false;
        }
    }

    private void resetInputs() {
        sendingAccount.getSelectionModel().clearSelection();
        textField.setText("");
        amountField.setText("");
        recievingAccountText.setText("");
        datePicker.setValue(null);
    }

    @Override
    public void setParent(Object parent) {
        navController = (NavController) parent;
    }
}
