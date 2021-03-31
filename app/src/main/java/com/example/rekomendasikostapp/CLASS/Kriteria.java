package com.example.rekomendasikostapp.CLASS;

import java.io.Serializable;

public class Kriteria implements Serializable {
    private String namaKriteria;
    private Double bobotKriteria;

    public Kriteria(){

    }

    public Kriteria(String namaKriteria, Double bobotKriteria){
        this.setNamaKriteria(namaKriteria);
        this.setBobotKriteria(bobotKriteria);
    }

    public String getNamaKriteria() {
        return namaKriteria;
    }

    public void setNamaKriteria(String namaKriteria) {
        this.namaKriteria = namaKriteria;
    }

    public Double getBobotKriteria() {
        return bobotKriteria;
    }

    public void setBobotKriteria(Double bobotKriteria) {
        this.bobotKriteria = bobotKriteria;
    }
}
