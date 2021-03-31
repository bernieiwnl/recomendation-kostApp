package com.example.rekomendasikostapp.CLASS;

public class Verifikasi {

    private String alamat;
    private String idUser;
    private String identitas_image_url;
    private String jenis_identitas;
    private String selfie_image_url;

    public Verifikasi(){

    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdentitas_image_url() {
        return identitas_image_url;
    }

    public void setIdentitas_image_url(String identitas_image_url) {
        this.identitas_image_url = identitas_image_url;
    }

    public String getJenis_identitas() {
        return jenis_identitas;
    }

    public void setJenis_identitas(String jenis_identitas) {
        this.jenis_identitas = jenis_identitas;
    }

    public String getSelfie_image_url() {
        return selfie_image_url;
    }

    public void setSelfie_image_url(String selfie_image_url) {
        this.selfie_image_url = selfie_image_url;
    }
}
