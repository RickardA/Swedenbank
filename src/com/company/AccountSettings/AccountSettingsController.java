package com.company.AccountSettings;

import com.company.Interfaces.ParentController;
import com.company.Navigation.NavController;
import com.company.Program;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class AccountSettingsController implements ParentController {
    private NavController navController;

    @FXML
    private VBox vBox;
    @FXML
    private Button applyButton;

    @FXML
    private void initialize(){
        setup();
    }

    private void setup(){
        Program.getLoggedInUser().getAccounts().forEach(account -> {
            vBox.getChildren().add(new Text(account.getAccount_name()));
        });
    }

    @Override
    public void setParent(Object parent) {
        navController = (NavController)parent;
    }
}
