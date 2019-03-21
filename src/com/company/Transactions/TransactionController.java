package com.company.Transactions;

import com.company.Database.DB;
import com.company.Interfaces.ParentController;
import com.company.Navigation.NavController;
import com.company.Program;
import javafx.fxml.FXML;
import javafx.scene.control.*;


public class TransactionController implements ParentController {
    NavController navController;

    @FXML
    private ChoiceBox sendingAccount;
    @FXML
    private ChoiceBox recievingAccount;
    @FXML
    private TextField amountField;
    @FXML
    private TextField textField;
    @FXML
    private Button sendButton;

    @FXML
    private void initialize() {
        setup();
    }

    private void setup() {
        sendButton.setOnMousePressed(event -> {
            makeTransaction();
        });
        Program.getAccounts().forEach(account -> {
            sendingAccount.getItems().add(account.getAccount_name() + " " + account.getAccount_number());
            recievingAccount.getItems().add(account.getAccount_name() + " " + account.getAccount_number());
        });
        amountField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                amountField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    private void makeTransaction() {
        String selectedSendingAccount = extractAccountNumber(sendingAccount.getValue());
        String selectedRecievingAccount = extractAccountNumber(recievingAccount.getValue());
        String text = textField.getText();
        double amount = amountField.getText().isEmpty() ? 0 : Double.parseDouble(amountField.getText());
        if (!selectedSendingAccount.isEmpty() || !selectedRecievingAccount.isEmpty() || !text.isEmpty() || amount != 0){
            DB.makeTransaction(text, selectedSendingAccount, selectedRecievingAccount, amount);
        }else{
            Alert alert = new Alert(
                    Alert.AlertType.ERROR,"", ButtonType.OK);
            alert.setHeaderText("Vänligen fyll i alla fält");
            alert.showAndWait();
        }
        resetInputs();
    }

    private void resetInputs() {
        sendingAccount.getSelectionModel().clearSelection();
        recievingAccount.getSelectionModel().clearSelection();
        textField.setText("");
        amountField.setText("");
    }

    private String extractAccountNumber(Object s) {
        String extractedNumber;
        if (s != null) {
            extractedNumber = s.toString().replaceFirst(".*\\s", "");
        }else{
            extractedNumber = "";
        }
        return extractedNumber;
    }

    @Override
    public void setParent(Object parent) {
        navController = (NavController) parent;
    }
}
