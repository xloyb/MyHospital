package org.example.miniprojet.controller;

import org.example.miniprojet.models.Personnel;


public class SessionManager {
    private static SessionManager instance;
    private int loggedInCIN = -1;
    private String loggedInName;
    private String fonction;




    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setLoggedInPersonnel(int cin, String name,String fonction) {
        this.loggedInCIN = cin;
        this.loggedInName = name;
        this.fonction = fonction;
    }

    public void setLoggedInPersonnel(Personnel personnel) {
        this.loggedInCIN = Integer.parseInt(personnel.getCin());
        this.loggedInName = personnel.getNom();
        this.fonction = personnel.getFonction();
    }

    public String getFonction() {
        return fonction;
    }

    public void setFonction(String fonction) {
        this.fonction = fonction;
    }

    public int getLoggedInCIN() {
        return loggedInCIN;
    }

    public String getLoggedInName() {
        return loggedInName;
    }

    public void clearSession() {
        this.loggedInCIN = -1;
        this.loggedInName = null;
        this.fonction = null;
    }

}
