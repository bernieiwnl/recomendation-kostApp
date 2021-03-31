package com.example.rekomendasikostapp.CLASS;

import java.util.ArrayList;

public class Preferensi {

    private String idUser;
    private ArrayList<Kriteria> preferensiKriteria;
    private ArrayList<SubKriteria> preferensiUmum;
    private ArrayList<SubKriteria> preferensiKamar;
    private ArrayList<SubKriteria> preferensiAkses;
    private String namaLokasi;
    private Double latitude;
    private Double longtitude;

    public Preferensi(){

    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public ArrayList<Kriteria> getPreferensiKriteria() {
        return preferensiKriteria;
    }

    public void setPreferensiKriteria(ArrayList<Kriteria> preferensiKriteria) {
        this.preferensiKriteria = preferensiKriteria;
    }

    public ArrayList<SubKriteria> getPreferensiUmum() {
        return preferensiUmum;
    }

    public void setPreferensiUmum(ArrayList<SubKriteria> preferensiUmum) {
        this.preferensiUmum = preferensiUmum;
    }

    public ArrayList<SubKriteria> getPreferensiKamar() {
        return preferensiKamar;
    }

    public void setPreferensiKamar(ArrayList<SubKriteria> preferensiKamar) {
        this.preferensiKamar = preferensiKamar;
    }

    public ArrayList<SubKriteria> getPreferensiAkses() {
        return preferensiAkses;
    }

    public void setPreferensiAkses(ArrayList<SubKriteria> preferensiAkses) {
        this.preferensiAkses = preferensiAkses;
    }

    public String getNamaLokasi() {
        return namaLokasi;
    }

    public void setNamaLokasi(String namaLokasi) {
        this.namaLokasi = namaLokasi;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(Double longtitude) {
        this.longtitude = longtitude;
    }
}
