package com.company;

import com.company.Database.Database;
import javafx.collections.ObservableList;

public class Program {
    private static User loggedInUser;

    public Program(){
        Thread databaseThread = new Thread(Database.getInstance());
        databaseThread.setDaemon(true);
        databaseThread.start();
    }

    public static void setLoggedInUser(User user){
        loggedInUser = user;
        System.out.println("User "+loggedInUser.getFirst_name()+" logged in!");
    }

    public static User getLoggedInUser(){
        return loggedInUser;
    }


}
