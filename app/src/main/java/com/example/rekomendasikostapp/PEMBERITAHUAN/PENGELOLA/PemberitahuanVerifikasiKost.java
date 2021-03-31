package com.example.rekomendasikostapp.PEMBERITAHUAN.PENGELOLA;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.rekomendasikostapp.ADAPTER.ImageSliderAdapter;
import com.example.rekomendasikostapp.CLASS.Kost;
import com.example.rekomendasikostapp.CLASS.Pemberitahuan;
import com.example.rekomendasikostapp.PENGELOLA.DetailKostPengelola;
import com.example.rekomendasikostapp.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PemberitahuanVerifikasiKost extends AppCompatActivity implements View.OnClickListener {

    private TextView judulPemberitahuan;
    private TextView isiPemberitahuan;
    private TextView tanggalPemberitahuan;
    private TextView alamatKost;
    private TextView hargaKost;
    private TextView jenisKost;

    String idPemberitahuan;
    String idKost;

    FirebaseFirestore firebaseFirestore;

    private ArrayList<String> fotoKosts;

    private Button btnInfoLanjut;

    private SliderView sliderView;
    private ImageSliderAdapter imageSliderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemberitahuan_verifikasi_kost);

        //get intent
        idPemberitahuan = getIntent().getStringExtra("idPemberitahuan");
        idKost = getIntent().getStringExtra("idKost");

        //get object reference
        judulPemberitahuan = (TextView) findViewById(R.id.judulPemberitahuan);
        isiPemberitahuan = (TextView) findViewById(R.id.isiPemberitahuan);
        tanggalPemberitahuan = (TextView) findViewById(R.id.tanggalPemberitaahuan);
        alamatKost = (TextView) findViewById(R.id.alamatKost);
        jenisKost = (TextView) findViewById(R.id.jenisKost);
        hargaKost = (TextView) findViewById(R.id.hargaKost);
        btnInfoLanjut = (Button) findViewById(R.id.btnInfoLanjut);


        //onclicklistener
        btnInfoLanjut.setOnClickListener(this);

        //get new array
        fotoKosts = new ArrayList<String>();

        //get isntance firebase firestore
        firebaseFirestore = FirebaseFirestore.getInstance();

        //slider
        sliderView = (SliderView) findViewById(R.id.gambarKost);
        sliderView.startAutoCycle();
        sliderView.setIndicatorAnimation(IndicatorAnimations.FILL);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);


        // get data collection from firestore
        firebaseFirestore.collection("data_kost").document(idKost).addSnapshotListener(PemberitahuanVerifikasiKost.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){

                    //currency format
                    Locale localeID = new Locale("in", "ID");
                    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

                    Kost kost = documentSnapshot.toObject(Kost.class);
                    alamatKost.setText(kost.getAlamat() + ", " + kost.getKota() +", " + kost.getProvinsi() + " " + kost.getKode_pos());
                    hargaKost.setText(formatRupiah.format((double)kost.getHarga()) + " / " + kost.getJenis_sewa());

                    jenisKost.setText(kost.getJenis_kost());


                    //update tanggal kost
                    SimpleDateFormat format = new SimpleDateFormat("EEEE, dd MMMM yyyy");

                    fotoKosts.clear();
                    for(int i = 0; i < kost.getFoto_kost().size(); i++){
                        fotoKosts.add(kost.getFoto_kost().get(i));
                    }
                    imageSliderAdapter = new ImageSliderAdapter(getApplicationContext() , fotoKosts);
                    sliderView.setSliderAdapter(imageSliderAdapter);
                }
                else{
                    Log.d("TAG", "Query Not Found" + e);
                }
            }
        });

        firebaseFirestore.collection("pemberitahuans").document(idPemberitahuan).addSnapshotListener(PemberitahuanVerifikasiKost.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                Pemberitahuan data = documentSnapshot.toObject(Pemberitahuan.class);

                judulPemberitahuan.setText(data.getJudul());
                isiPemberitahuan.setText(data.getDeskripsi());

                //update tanggal kost
                SimpleDateFormat format = new SimpleDateFormat("EEEE, dd MMMM yyyy");
                tanggalPemberitahuan.setText(format.format(data.getTime().getTime()));

            }
        });

        setRead(idPemberitahuan);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnInfoLanjut:
                Intent intent = new Intent(PemberitahuanVerifikasiKost.this,  DetailKostPengelola.class);
                intent.putExtra("idKost", idKost);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
    }


    private void setRead(String idPemberitahuan){

        Map<String, Object> pemberitahuan = new HashMap<>();
        pemberitahuan.put("status", "read");
        firebaseFirestore.collection("pemberitahuans").document(idPemberitahuan).update(pemberitahuan);

    }
}
