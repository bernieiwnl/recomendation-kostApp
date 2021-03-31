package com.example.rekomendasikostapp.CLASS;


import java.io.Serializable;
import java.util.List;

public class AksesLingkungan implements Serializable {

    private String nama_akses;
    private String foto_url;
    private Integer nilai;
    private String idfasilitas;
    private List<String> idkosts;

    public AksesLingkungan(){

    }

    public AksesLingkungan(String nama_akses , String foto_url, Integer nilai, String idfasilitas, List<String> idkosts){
        this.setNama_akses(nama_akses);
        this.setFoto_url(foto_url);
        this.setNilai(nilai);
        this.setIdfasilitas(idfasilitas);
        this.setIdkosts(idkosts);
    }

    public AksesLingkungan(String nama_akses){
        this.setNama_akses(nama_akses);
    }

    public String getNama_akses() {
        return nama_akses;
    }

    public void setNama_akses(String nama_akses) {
        this.nama_akses = nama_akses;
    }

    public String getFoto_url() {
        return foto_url;
    }

    public void setFoto_url(String foto_url) {
        this.foto_url = foto_url;
    }

    public Integer getNilai() {
        return nilai;
    }

    public void setNilai(Integer nilai) {
        this.nilai = nilai;
    }

    public String getIdfasilitas() {
        return idfasilitas;
    }

    public void setIdfasilitas(String idfasilitas) {
        this.idfasilitas = idfasilitas;
    }


    public List<String> getIdkosts() {
        return idkosts;
    }

    public void setIdkosts(List<String> idkosts) {
        this.idkosts = idkosts;
    }
}
