package com.example.rekomendasikostapp.CLASS;

public class ChatList {
    private String id;

    public ChatList(){

    }

    public ChatList(String userId){
        this.setId(userId);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
