package com.example.rekomendasikostapp.NOTIFICATIONS;

public class Token {

    private String token;
    private String userID;

    public Token(){

    }

    public Token(String token){
        this.setToken(token);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
