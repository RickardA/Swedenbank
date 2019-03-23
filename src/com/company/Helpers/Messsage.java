package com.company.Helpers;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public abstract class Messsage {

    public static void printError(String message){
        Alert alert = new Alert(
                Alert.AlertType.ERROR,"", ButtonType.OK);
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    public static void printSuccess(String message){
        Alert alert = new Alert(
                Alert.AlertType.INFORMATION,"", ButtonType.OK);
        alert.setHeaderText(message);
        alert.showAndWait();
    }

}
