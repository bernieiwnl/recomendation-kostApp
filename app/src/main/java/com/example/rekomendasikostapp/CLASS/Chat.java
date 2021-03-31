package com.example.rekomendasikostapp.CLASS;

import java.sql.Timestamp;
import java.util.Date;

public class Chat {

    private String sender;
    private String receiver;
    private String message;
    private Date time;
    private String read;
    private String idChat;
    private String type;

    public Chat(){

    }

    public Chat(String idChat, String sender, String receiver, String message, Date time, String read, String type){
        this.setIdChat(idChat);
        this.setMessage(message);
        this.setReceiver(receiver);
        this.setSender(sender);
        this.setTime(time);
        this.setRead(read);
        this.setType(type);
    }


    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getIdChat() {
        return idChat;
    }

    public void setIdChat(String idChat) {
        this.idChat = idChat;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
