package com.example.rekomendasikostapp.CLASS;

public class Menu {
    private String namaMenu;
    private String jenisMenu;


    public Menu(String namaMenu, String jenisMenu){
        this.namaMenu = namaMenu;
        this.jenisMenu = jenisMenu;
    }

    public String getNamaMenu() {
        return namaMenu;
    }

    public void setNamaMenu(String namaMenu) {
        this.namaMenu = namaMenu;
    }

    public String getJenisMenu() {
        return jenisMenu;
    }

    public void setJenisMenu(String jenisMenu) {
        this.jenisMenu = jenisMenu;
    }
}
