package com.example.rekomendasikostapp.CLASS;

import java.util.Date;

public class Laporan {

    private String idPelaporan;
    private String idKost;
    private String idPengelola;
    private String idUser;
    private String jenis_laporan;
    private Date tanggal_laporan;
    private String deskripsi;

    public Laporan (){

    }

    public Laporan(String idPelaporan, String idKost, String idPengelola, String idUser, String jenis_laporan, Date tanggal_laporan, String deskripsi)
    {
        this.setIdPelaporan(idPelaporan);
        this.setIdKost(idKost);
        this.setIdPengelola(idPengelola);
        this.setIdUser(idUser);
        this.setJenis_laporan(jenis_laporan);
        this.setTanggal_laporan(tanggal_laporan);
        this.setDeskripsi(deskripsi);
    }

    public String getIdPelaporan() {
        return idPelaporan;
    }

    public void setIdPelaporan(String idPelaporan) {
        this.idPelaporan = idPelaporan;
    }

    public String getIdKost() {
        return idKost;
    }

    public void setIdKost(String idKost) {
        this.idKost = idKost;
    }

    public String getIdPengelola() {
        return idPengelola;
    }

    public void setIdPengelola(String idPengelola) {
        this.idPengelola = idPengelola;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getJenis_laporan() {
        return jenis_laporan;
    }

    public void setJenis_laporan(String jenis_laporan) {
        this.jenis_laporan = jenis_laporan;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public Date getTanggal_laporan() {
        return tanggal_laporan;
    }

    public void setTanggal_laporan(Date tanggal_laporan) {
        this.tanggal_laporan = tanggal_laporan;
    }
}
