package com.example.rekomendasikostapp.CLASS;

import java.util.Date;

public class Pemberitahuan {

    private String idPemberitahuan;
    private String idUser;
    private String idKost;
    private String idSender;
    private String idPemesanan;
    private String idLaporan;
    private String jenis_pemberitahuan;
    private String judul;
    private String deskripsi;
    private String status;
    private Date time;

    public Pemberitahuan(){

    }

    public Pemberitahuan(String idPemberitahuan, String idUser, String idKost, String jenis_pemberitahuan, String judul, String deskripsi, String status, String idPemesanan, Date time, String idSender, String idLaporan){
        this.setIdPemberitahuan(idPemberitahuan);
        this.setIdPemesanan(idPemesanan);
        this.setIdUser(idUser);
        this.setIdKost(idKost);
        this.setJenis_pemberitahuan(jenis_pemberitahuan);
        this.setJudul(judul);
        this.setDeskripsi(deskripsi);
        this.setStatus(status);
        this.setTime(time);
        this.setIdSender(idSender);
        this.setIdLaporan(idLaporan);
    }


    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdKost() {
        return idKost;
    }

    public void setIdKost(String idKost) {
        this.idKost = idKost;
    }

    public String getJenis_pemberitahuan() {
        return jenis_pemberitahuan;
    }

    public void setJenis_pemberitahuan(String jenis_pemberitahuan) {
        this.jenis_pemberitahuan = jenis_pemberitahuan;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdPemesanan() {
        return idPemesanan;
    }

    public void setIdPemesanan(String idPemesanan) {
        this.idPemesanan = idPemesanan;
    }

    public String getIdPemberitahuan() {
        return idPemberitahuan;
    }

    public void setIdPemberitahuan(String idPemberitahuan) {
        this.idPemberitahuan = idPemberitahuan;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getIdSender() {
        return idSender;
    }

    public void setIdSender(String idSender) {
        this.idSender = idSender;
    }

    public String getIdLaporan() {
        return idLaporan;
    }

    public void setIdLaporan(String idLaporan) {
        this.idLaporan = idLaporan;
    }
}
