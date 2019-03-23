package com.company.Company;

import com.company.Database.DB;
import com.company.Helpers.Messsage;
import com.company.Helpers.Extractor;
import com.company.Interfaces.ParentController;
import com.company.Navigation.NavController;
import com.company.Program;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Date;
import java.time.LocalDate;

public class CompanyController implements ParentController {
    private NavController navController;

    @FXML
    private TextField socialSecurityNumberField;
    @FXML
    private ChoiceBox sendingAccount;
    @FXML
    private TextField amountField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button executeButton;

    @FXML
    private void initialize(){
        setup();
    }

    private void setup(){
        executeButton.setOnMousePressed(event -> {executeSalaryPayment();});
        Program.getLoggedInUser().getAccounts().forEach(account -> {
            sendingAccount.getItems().add(account.getAccount_name() + " " + account.getAccount_number());
        });
        amountField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                amountField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    private void executeSalaryPayment(){
        String socialNumber = socialSecurityNumberField.getText();
        String sendingAccountNumber = Extractor.extractAccountNumber(sendingAccount.getValue());
        double amount = amountField.getText().isEmpty() ? 0 : Double.parseDouble(amountField.getText());
        LocalDate datFromPicker = datePicker.getValue();
        if (!socialNumber.isEmpty() && sendingAccountNumber != null && datFromPicker != null && amount != 0.0){
            if (socialNumber.matches("\\d{10}")){
                Date date = Date.valueOf(datFromPicker);
                DB.addSalaryTransaction(socialNumber,amount,sendingAccountNumber,date);
            }else{
                Messsage.printError("Personnummret måste bestå av 10 siffror \n" +
                        "i följande format : xxxxxxxxxx ");
            }
        }else{
            Messsage.printError("Vänligen fyll i alla fält");
        }
        resetInputs();
    }



    private void resetInputs() {
        sendingAccount.getSelectionModel().clearSelection();
        socialSecurityNumberField.setText("");
        amountField.setText("");
        datePicker.setValue(null);
    }



    @Override
    public void setParent(Object parent) {
        navController = (NavController)parent;
    }
}
