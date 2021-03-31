package com.example.rekomendasikostapp.CLASS;

import java.io.Serializable;
import java.util.List;

public class FasilitasKamar implements Serializable {

    private String nama_fasilitas;
    private String foto_url;
    private Integer nilai;
    private String idfasilitas;
    private List<String> idkosts;

    public FasilitasKamar(){

    }

    public  FasilitasKamar(String nama_fasilitas , String foto_url, Integer nilai, String idfasilitas, List<String> idkosts){
        this.setNama_fasilitas(nama_fasilitas);
        this.setFoto_url(foto_url);
        this.setNilai(nilai);
        this.setIdfasilitas(idfasilitas);
        this.setIdkosts(idkosts);
    }

    public String getNama_fasilitas() {
        return nama_fasilitas;
    }

    public void setNama_fasilitas(String nama_fasilitas) {
        this.nama_fasilitas = nama_fasilitas;
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
