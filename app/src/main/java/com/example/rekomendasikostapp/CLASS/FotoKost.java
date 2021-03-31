package com.example.rekomendasikostapp.CLASS;

public class FotoKost {

    private String idKost;
    private String foto_url;


    public FotoKost(){

    }

    public FotoKost(String idKost, String foto_url){
        this.setFoto_url(foto_url);
        this.setIdKost(idKost);
    }

    public String getIdKost() {
        return idKost;
    }

    public void setIdKost(String idKost) {
        this.idKost = idKost;
    }

    public String getFoto_url() {
        return foto_url;
    }

    public void setFoto_url(String foto_url) {
        this.foto_url = foto_url;
    }

}
