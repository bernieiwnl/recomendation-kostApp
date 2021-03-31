package com.example.rekomendasikostapp.CLASS;

import java.util.Date;
import java.util.List;

public class Kost {

    private String idKost;
    private String idUser;
    private String alamat;
    private boolean isVerification;
    private String jenis_kost;
    private String jenis_sewa;
    private Integer jumlah_kamar;
    private String keterangan;
    private Integer kode_pos;
    private String kota;
    private Double latitude;
    private Double longtitude;
    private String nama_pengelola;
    private String nomor_telepon;
    private String provinsi;
    private Integer sisa_kamar;
    private String ukuran_kamar;
    private Integer harga;
    private List<String> foto_kost;
    private List<String> pemesanans;
    private List<String> fasilitas_umums;
    private List<String> fasilitas_kamars;
    private List<String> akses_lingkungans;
    private String nomor_rekening;
    private String nama_bank;
    private String nama_rekening;
    private Date created_at;
    private Date updated_at;
    private String status;


    public Kost(){


    }

    public Kost(String idKost, Integer harga ,String idUser, String alamat, String jenis_kost, String jenis_sewa, Integer jumlah_kamar, String keterangan, Integer kode_pos, String kota, Double latitude, Double longtitude, String nama_pengelola, String nomor_telepon, String provinsi, Integer sisa_kamar, String ukuran_kamar, List<String> fotoKost, List<String> pemesanans,  List<String> akses_lingkungans,  List<String> fasilitas_kamars,  List<String> fasilitas_umums, String nama_bank, String nomor_rekening, String nama_rekening, Date created_at, Date updated_at){
        this.setIdKost(idKost);
        this.setIdUser(idUser);
        this.setAlamat(alamat);
        this.setJenis_kost(jenis_kost);
        this.setJenis_sewa(jenis_sewa);
        this.setJumlah_kamar(jumlah_kamar);
        this.setKeterangan(keterangan);
        this.setKode_pos(kode_pos);
        this.setKota(kota);
        this.setLatitude(latitude);
        this.setLongtitude(longtitude);
        this.setNama_pengelola(nama_pengelola);
        this.setNomor_telepon(nomor_telepon);
        this.setProvinsi(provinsi);
        this.setSisa_kamar(sisa_kamar);
        this.setUkuran_kamar(ukuran_kamar);
        this.setHarga(harga);
        this.setFoto_kost(fotoKost);
        this.setPemesanans(pemesanans);
        this.setAkses_lingkungans(akses_lingkungans);
        this.setFasilitas_kamars(fasilitas_kamars);
        this.setFasilitas_umums(fasilitas_umums);
        this.setNama_bank(nama_bank);
        this.setNomor_rekening(nomor_rekening);
        this.setNama_rekening(nama_rekening);
        this.setUpdated_at(updated_at);
        this.setCreated_at(created_at);
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

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }


    public String getJenis_kost() {
        return jenis_kost;
    }

    public void setJenis_kost(String jenis_kost) {
        this.jenis_kost = jenis_kost;
    }

    public String getJenis_sewa() {
        return jenis_sewa;
    }

    public void setJenis_sewa(String jenis_sewa) {
        this.jenis_sewa = jenis_sewa;
    }

    public Integer getJumlah_kamar() {
        return jumlah_kamar;
    }

    public void setJumlah_kamar(Integer jumlah_kamar) {
        this.jumlah_kamar = jumlah_kamar;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public Integer getKode_pos() {
        return kode_pos;
    }

    public void setKode_pos(Integer kode_pos) {
        this.kode_pos = kode_pos;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
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

    public String getNama_pengelola() {
        return nama_pengelola;
    }

    public void setNama_pengelola(String nama_pengelola) {
        this.nama_pengelola = nama_pengelola;
    }

    public String getNomor_telepon() {
        return nomor_telepon;
    }

    public void setNomor_telepon(String nomor_telepon) {
        this.nomor_telepon = nomor_telepon;
    }

    public String getProvinsi() {
        return provinsi;
    }

    public void setProvinsi(String provinsi) {
        this.provinsi = provinsi;
    }

    public Integer getSisa_kamar() {
        return sisa_kamar;
    }

    public void setSisa_kamar(Integer sisa_kamar) {
        this.sisa_kamar = sisa_kamar;
    }

    public String getUkuran_kamar() {
        return ukuran_kamar;
    }

    public void setUkuran_kamar(String ukuran_kamar) {
        this.ukuran_kamar = ukuran_kamar;
    }

    public Integer getHarga() {
        return harga;
    }

    public void setHarga(Integer harga) {
        this.harga = harga;
    }

    public List<String> getFoto_kost() {
        return foto_kost;
    }

    public void setFoto_kost(List<String> foto_kost) {
        this.foto_kost = foto_kost;
    }

    public List<String> getPemesanans() {
        return pemesanans;
    }

    public void setPemesanans(List<String> pemesanans) {
        this.pemesanans = pemesanans;
    }

    public List<String> getFasilitas_umums() {
        return fasilitas_umums;
    }

    public void setFasilitas_umums(List<String> fasilitas_umums) {
        this.fasilitas_umums = fasilitas_umums;
    }

    public List<String> getFasilitas_kamars() {
        return fasilitas_kamars;
    }

    public void setFasilitas_kamars(List<String> fasilitas_kamars) {
        this.fasilitas_kamars = fasilitas_kamars;
    }

    public List<String> getAkses_lingkungans() {
        return akses_lingkungans;
    }

    public void setAkses_lingkungans(List<String> akses_lingkungans) {
        this.akses_lingkungans = akses_lingkungans;
    }

    public boolean isVerification() {
        return isVerification;
    }

    public void setVerification(boolean verification) {
        isVerification = verification;
    }

    public String getNomor_rekening() {
        return nomor_rekening;
    }

    public void setNomor_rekening(String nomor_rekening) {
        this.nomor_rekening = nomor_rekening;
    }

    public String getNama_bank() {
        return nama_bank;
    }

    public void setNama_bank(String nama_bank) {
        this.nama_bank = nama_bank;
    }

    public String getNama_rekening() {
        return nama_rekening;
    }

    public void setNama_rekening(String nama_rekening) {
        this.nama_rekening = nama_rekening;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
