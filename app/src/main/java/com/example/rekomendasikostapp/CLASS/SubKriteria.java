package com.example.rekomendasikostapp.CLASS;

import java.io.Serializable;

public class SubKriteria  implements Serializable {

    private String idSubKriteria;
    private String namaSubKriteria;
    private Double bobotSubKriteria;

    public SubKriteria (){

    }

    public SubKriteria(String idSubKriteria, String namaSubKriteria, Double bobotSubKriteria){
        this.setIdSubKriteria(idSubKriteria);
        this.setNamaSubKriteria(namaSubKriteria);
        this.setBobotSubKriteria(bobotSubKriteria);
    }

    public String getIdSubKriteria() {
        return idSubKriteria;
    }

    public void setIdSubKriteria(String idSubKriteria) {
        this.idSubKriteria = idSubKriteria;
    }

    public String getNamaSubKriteria() {
        return namaSubKriteria;
    }

    public void setNamaSubKriteria(String namaSubKriteria) {
        this.namaSubKriteria = namaSubKriteria;
    }

    public Double getBobotSubKriteria() {
        return bobotSubKriteria;
    }

    public void setBobotSubKriteria(Double bobotSubKriteria) {
        this.bobotSubKriteria = bobotSubKriteria;
    }
}
