package com.company.Login;

import com.company.Accounts.AccountController;
import com.company.Database.DB;
import com.company.Helpers.Loader;
import com.company.Home.HomeController;
import com.company.Main;
import com.company.Navigation.NavController;
import com.company.Program;
import com.company.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.io.IOException;


public class LoginController {

    @FXML
    private TextField socialNumberField;
    @FXML
    private TextField passwordField;

    @FXML
    public void loginRequest(){
        User tempUser = DB.getMatchingUser(socialNumberField.getText(),passwordField.getText());
        if (tempUser != null){
            Program.setLoggedInUser(tempUser);
            showHomePage();
        }
        else{
            Alert alert = new Alert(
                    Alert.AlertType.ERROR,"Vad orsakar detta?\n" +
                    "Du kanske har skrivit fel eller så är du inte kund hos oss än",ButtonType.OK);
            alert.setHeaderText("Fel vid inloggning \nVänligen försök igen");
            alert.showAndWait();
        }
        System.out.println("LoginRequest Sent");
    }

    private void showHomePage(){
        try {
            FXMLLoader navPageLoader = new FXMLLoader(getClass().getResource("../Navigation/nav.fxml"));
            Parent homePageRoot = navPageLoader.load();
            NavController controller = navPageLoader.getController();
            controller.displayHomePage();
            Scene scene = new Scene(homePageRoot,800,600);
            Main.stage.setScene(scene);
            Main.stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
