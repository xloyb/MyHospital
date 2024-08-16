package org.example.miniprojet.controller;


import org.example.miniprojet.models.Personnel;

public class LoginResult {
    private int statusCode;
    private Personnel personnel;

    public LoginResult(int statusCode, Personnel personnel) {
        this.statusCode = statusCode;
        this.personnel = personnel;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Personnel getPersonnel() {
        return personnel;
    }
}
