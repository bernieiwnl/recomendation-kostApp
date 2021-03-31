package com.example.rekomendasikostapp.CLASS;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class RankOrderCentroid {


    public RankOrderCentroid(){

    }

    public double bobotHarga(int harga){
        int prioritas = 0;
        double roc = 0.0;
        double bobot = 0.0;

        if(harga > 1500000){
            prioritas = 6;
        }
        else if(harga <= 1500000 && harga > 1250000){
            prioritas = 5;
        }
        else if(harga <= 1250000 && harga > 1000000){
            prioritas = 4;
        }
        else if(harga <= 1000000 && harga > 750000){
            prioritas = 3;
        }
        else if(harga <= 750000 && harga > 500000){
            prioritas = 2;
        }
        else if(harga <= 500000){
            prioritas = 1;
        }

        //memberikan bobot menggunakan rank order centroid berdasarkan prioritas yang sudah di tentukan
        for(int i = prioritas; i <= 6; i++)
        {
            roc = roc + (1.0 / i);
        }
        bobot = roc / 6;
        return bobot;
    }

    public double bobotUkuranKamar(String ukuran)
    {
        int prioritas = 0;
        double roc = 0.0;
        double bobot = 0.0;

        //memberikan prioritas seitap harga
        if(ukuran.equals("4m x 5m") ){
            prioritas = 1;
        }
        else if(ukuran.equals("4m x 4m")){
            prioritas = 2;
        }
        else if(ukuran.equals("3m x 4m")){
            prioritas = 3;
        }
        else if(ukuran.equals("3m x 3m")){
            prioritas = 4;
        }
        else if(ukuran.equals("2m x 3m")){
            prioritas = 5;
        }

        //memberikan bobot menggunakan rank order centroid berdasarkan prioritas yang sudah di tentukan
        for(int i = prioritas; i <= 5; i++)
        {
            roc = roc + (1.0 / i);
        }
        bobot = roc / 5;
        return bobot;
    }


    public double bobotJarak(double jarak)
    {
        int prioritas = 0;
        double roc = 0.0;
        double bobot = 0.0;


        if(jarak > 1000.0 ){
            prioritas = 5;
        }
//        else if(1000.0 <= jarak && jarak > 500.0 ){
//            prioritas = 4;
//        }
//        else if(500.0 <= jarak && jarak > 250.0 ){
//            prioritas = 3;
//        }
//        else if(250.0 <= jarak && jarak > 50.0){
//            prioritas = 2;
//        }

        else if(jarak <= 1000.0 && jarak > 500.0){
            prioritas = 4;
        }
        else if(jarak <= 500.0 && jarak > 250.0){
            prioritas = 3;
        }
        else if(jarak <= 250.0 && jarak > 50.0){
            prioritas = 2;
        }

        else if(jarak <= 50.0) {
            prioritas = 1;
        }

        Log.d("PRIORITY",  "" + prioritas + " " + jarak);

        //memberikan bobot menggunakan rank order centroid berdasarkan prioritas yang sudah di tentukan
        for(int i = prioritas; i <= 5; i++)
        {
            roc = roc + (1.0 / i);
        }
        bobot = roc / 5;
        return bobot;
    }

    public double bobotFasilitas(List<String> fasilitasKost, ArrayList<SubKriteria> subKriteria){

        double bobot = 0.0;

        for(int i = 0; i < subKriteria.size(); i++){

            for(int j = 0; j < fasilitasKost.size(); j++){

                if(subKriteria.get(i).getIdSubKriteria().equals(fasilitasKost.get(j))){
                    bobot = bobot + subKriteria.get(i).getBobotSubKriteria();
                }
            }
        }
        return bobot;
    }

}
