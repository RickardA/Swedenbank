package com.company.Card;

import com.company.Database.DB;
import com.company.Interfaces.ParentController;
import com.company.Navigation.NavController;
import com.company.Program;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

public class CardController implements ParentController {
    private NavController navController;

    @FXML
    private Button cardButton;
    @FXML
    private TextField cardAmount;

    @FXML
    private void initialize(){
        cardAmount.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                cardAmount.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        cardButton.setOnMousePressed(event -> {
            if (!cardAmount.getText().isEmpty()){
                Double amount = Double.parseDouble(cardAmount.getText());
                DB.makeCardPayment(Program.getLoggedInUser().getSocial_number(),amount);
                cardAmount.setText("");
            }else {
                Alert alert = new Alert(
                        Alert.AlertType.ERROR,"", ButtonType.OK);
                alert.setHeaderText("Vänligen fyll i belopp");
                alert.showAndWait();
            }
        });
    }

    @Override
    public void setParent(Object parent) {
        navController = (NavController)parent;
    }
}
