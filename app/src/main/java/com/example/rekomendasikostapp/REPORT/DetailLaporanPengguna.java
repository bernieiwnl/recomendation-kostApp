package com.example.rekomendasikostapp.REPORT;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rekomendasikostapp.CLASS.Kost;
import com.example.rekomendasikostapp.CLASS.Laporan;
import com.example.rekomendasikostapp.CLASS.Users;
import com.example.rekomendasikostapp.MAINKOST.DetailKostPenggunaActivity;
import com.example.rekomendasikostapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DetailLaporanPengguna extends AppCompatActivity implements View.OnClickListener {

    //kost
    private TextView alamatKost;
    private TextView sisaKamar;
    private TextView hargaKost;
    private TextView ukuranKamar;
    private TextView jumlahKamar;
    private TextView jenisKost;
    private ImageView imageKost;
    private RelativeLayout sisa;
    private RelativeLayout jenis;
    private RelativeLayout kostContent;

    //keluhan
    private TextView namaPengguna;
    private TextView tanggalKeluhan;
    private TextView jenisKeluhan;
    private TextView deskripsiKeluhan;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;


    private String idLaporan;
    private String idKost;
    private String idUser;
    private String idPengelola;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_laporan_pengguna);

        //get object reference
        jumlahKamar = (TextView) findViewById(R.id.jumlahKamar);
        sisaKamar = (TextView) findViewById(R.id.sisaKamar);
        alamatKost = (TextView) findViewById(R.id.alamatKost);
        hargaKost = (TextView) findViewById(R.id.harga);
        jenisKost = (TextView) findViewById(R.id.jenisKost);
        ukuranKamar = (TextView) findViewById(R.id.ukuranKamar);
        imageKost = (ImageView) findViewById(R.id.imageKost);
        jenis = (RelativeLayout) findViewById(R.id.jenis);
        sisa = (RelativeLayout) findViewById(R.id.sisa);
        kostContent = (RelativeLayout) findViewById(R.id.kostContent);

        namaPengguna = (TextView) findViewById(R.id.namaPelapor);
        tanggalKeluhan = (TextView) findViewById(R.id.tanggalLapor);
        jenisKeluhan = (TextView) findViewById(R.id.jenisLaporan);
        deskripsiKeluhan = (TextView) findViewById(R.id.deskripsiLaporan);

        // firesbase instance;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = firebaseAuth.getInstance();

        //set onclick listener
        kostContent.setOnClickListener(this);

        //get intent
        idKost = getIntent().getStringExtra("idKost");
        idLaporan = getIntent().getStringExtra("idLaporan");
        idUser = getIntent().getStringExtra("idUser");
        idPengelola = getIntent().getStringExtra("idPengelola");

        //get data kost
        firebaseFirestore.collection("data_kost").document(idKost).addSnapshotListener(DetailLaporanPengguna.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){

                    //currency format
                    Locale localeID = new Locale("in", "ID");
                    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

                    Kost kost = documentSnapshot.toObject(Kost.class);
                    alamatKost.setText(kost.getAlamat() + ", " + kost.getKota() +", " + kost.getProvinsi() + " " + kost.getKode_pos());
                    hargaKost.setText(formatRupiah.format((double)kost.getHarga()) + " / " + kost.getJenis_sewa());
                    ukuranKamar.setText(kost.getUkuran_kamar());
                    jumlahKamar.setText("Ada " + kost.getJumlah_kamar() + " Kamar");
                    sisaKamar.setText("Sisa " + kost.getSisa_kamar() + " Kamar");
                    jenisKost.setText(kost.getJenis_kost());

                    if(kost.getSisa_kamar() < 3)
                    {
                        sisa.setBackgroundColor(Color.parseColor("#d32f2f"));
                    }

                    if(kost.getJenis_kost().equals("Perempuan"))
                    {
                        jenis.setBackgroundColor(Color.parseColor("#c2185b"));
                    }

                    for(int i = 0; i < kost.getFoto_kost().size(); i++){
                        if(i == 0)
                        {
                            Picasso.get().load(kost.getFoto_kost().get(i)).into(imageKost);
                        }
                    }
                }
                else{
                    Log.d("TAG", "Query Not Found" + e);
                }
            }
        });

        //get data user
        firebaseFirestore.collection("users").document(idUser).addSnapshotListener(DetailLaporanPengguna.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                Users users = documentSnapshot.toObject(Users.class);
                namaPengguna.setText(users.getFull_name());
            }
        });


        //get data keluhan
        firebaseFirestore.collection("reports").document(idLaporan).addSnapshotListener(DetailLaporanPengguna.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                Laporan laporans = documentSnapshot.toObject(Laporan.class);
                SimpleDateFormat format = new SimpleDateFormat("EEEE, dd MMMM yyyy");
                tanggalKeluhan.setText(format.format(laporans.getTanggal_laporan().getTime()));
                jenisKeluhan.setText(laporans.getJenis_laporan());
                deskripsiKeluhan.setText(laporans.getDeskripsi());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.kostContent:
                Intent i = new Intent(DetailLaporanPengguna.this, DetailKostPenggunaActivity.class);
                i.putExtra("idKost", idKost);
                startActivity(i);
                break;


        }
    }
}
