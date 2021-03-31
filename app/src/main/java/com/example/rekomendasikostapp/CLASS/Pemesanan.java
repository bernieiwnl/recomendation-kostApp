package com.example.rekomendasikostapp.CLASS;

import java.util.Date;

public class    Pemesanan {

    private String idPemesanan;
    private String idKost;
    private String idUser;
    private String idPengelola;
    private Date tanggal_pemesanan;
    private Date tanggal_kedatangan;
    private String durasi_penyewaan;
    private String nama_pemesan;
    private String nomor_telepon;
    private String status;
    private String bukti_url;

    public Pemesanan(){

    }

    public Pemesanan(String idPemesanan, String idKost, String idUser, Date tanggal_kedatangan, Date tanggal_pemesanan, String durasi_penyewaan, String nama_pemesan, String nomor_telepon, String status, String idPengelola, String bukti_url){
        this.setIdPemesanan(idPemesanan);
        this.setIdKost(idKost);
        this.setIdUser(idUser);
        this.setTanggal_kedatangan(tanggal_kedatangan);
        this.setTanggal_pemesanan(tanggal_pemesanan);
        this.setDurasi_penyewaan(durasi_penyewaan);
        this.setNama_pemesan(nama_pemesan);
        this.setNomor_telepon(nomor_telepon);
        this.setStatus(status);
        this.setIdPengelola(idPengelola);
        this.setBukti_url(bukti_url);
    }

    public String getIdPemesanan() {
        return idPemesanan;
    }

    public void setIdPemesanan(String idPemesanan) {
        this.idPemesanan = idPemesanan;
    }

    public String getIdKost() {
        return idKost;
    }

    public void setIdKost(String idKost) {
        this.idKost = idKost;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public Date getTanggal_pemesanan() {
        return tanggal_pemesanan;
    }

    public void setTanggal_pemesanan(Date tanggal_pemesanan) {
        this.tanggal_pemesanan = tanggal_pemesanan;
    }

    public Date getTanggal_kedatangan() {
        return tanggal_kedatangan;
    }

    public void setTanggal_kedatangan(Date tanggal_kedatangan) {
        this.tanggal_kedatangan = tanggal_kedatangan;
    }


    public String getNama_pemesan() {
        return nama_pemesan;
    }

    public void setNama_pemesan(String nama_pemesan) {
        this.nama_pemesan = nama_pemesan;
    }

    public String getNomor_telepon() {
        return nomor_telepon;
    }

    public void setNomor_telepon(String nomor_telepon) {
        this.nomor_telepon = nomor_telepon;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDurasi_penyewaan() {
        return durasi_penyewaan;
    }

    public void setDurasi_penyewaan(String durasi_penyewaan) {
        this.durasi_penyewaan = durasi_penyewaan;
    }

    public String getIdPengelola() {
        return idPengelola;
    }

    public void setIdPengelola(String idPengelola) {
        this.idPengelola = idPengelola;
    }

    public String getBukti_url() {
        return bukti_url;
    }

    public void setBukti_url(String bukti_url) {
        this.bukti_url = bukti_url;
    }
}
