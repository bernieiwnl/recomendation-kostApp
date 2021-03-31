package com.example.rekomendasikostapp.NOTIFICATIONS;

public class Data {

    private String title;
    private String body;
    private int icon;

    private String sender;
    private String receiver;
    private String idKost;
    private String idPemberitahuan;
    private String idPemesanan;
    private String idKeluhan;
    private String jenis_notifikasi;


    public Data(){

    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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

    public String getIdKost() {
        return idKost;
    }

    public void setIdKost(String idKost) {
        this.idKost = idKost;
    }

    public String getIdPemberitahuan() {
        return idPemberitahuan;
    }

    public void setIdPemberitahuan(String idPemberitahuan) {
        this.idPemberitahuan = idPemberitahuan;
    }

    public String getIdPemesanan() {
        return idPemesanan;
    }

    public void setIdPemesanan(String idPemesanan) {
        this.idPemesanan = idPemesanan;
    }

    public String getIdKeluhan() {
        return idKeluhan;
    }

    public void setIdKeluhan(String idKeluhan) {
        this.idKeluhan = idKeluhan;
    }

    public String getJenis_notifikasi() {
        return jenis_notifikasi;
    }

    public void setJenis_notifikasi(String jenis_notifikasi) {
        this.jenis_notifikasi = jenis_notifikasi;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
