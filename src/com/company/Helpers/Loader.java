package com.company.Helpers;

import javafx.fxml.FXMLLoader;

import java.io.IOException;

public abstract class Loader {

    public static FXMLLoader getFXMLController(String FXMLPathName){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Loader.class.getResource(FXMLPathName));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loader;
    }
}
