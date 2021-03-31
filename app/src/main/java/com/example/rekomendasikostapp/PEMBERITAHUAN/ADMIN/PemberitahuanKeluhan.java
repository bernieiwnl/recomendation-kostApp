package com.example.rekomendasikostapp.PEMBERITAHUAN.ADMIN;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.rekomendasikostapp.CLASS.Laporan;
import com.example.rekomendasikostapp.CLASS.Pemberitahuan;
import com.example.rekomendasikostapp.CLASS.Users;
import com.example.rekomendasikostapp.R;
import com.example.rekomendasikostapp.REPORT.DetailLaporanPengguna;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class PemberitahuanKeluhan extends AppCompatActivity implements View.OnClickListener {

    private TextView judulPemberitahuan;
    private TextView isiPemberitahuan;
    private TextView tanggalPemberitahuan;

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
    private String idPemberitahuan;
    private Button btnDetailKeluhan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemberitahuan_keluhan);

        //get object reference
        namaPengguna = (TextView) findViewById(R.id.namaPelapor);
        tanggalKeluhan = (TextView) findViewById(R.id.tanggalLapor);
        jenisKeluhan = (TextView) findViewById(R.id.jenisLaporan);
        deskripsiKeluhan = (TextView) findViewById(R.id.deskripsiLaporan);

        judulPemberitahuan = (TextView) findViewById(R.id.judulPemberitahuan);
        isiPemberitahuan = (TextView) findViewById(R.id.isiPemberitahuan);
        tanggalPemberitahuan = (TextView) findViewById(R.id.tanggalPemberitaahuan);
        btnDetailKeluhan = (Button) findViewById(R.id.btnDetailKeluhan);

        //get intent
        idKost = getIntent().getStringExtra("idKost");
        idLaporan = getIntent().getStringExtra("idLaporan");
        idUser = getIntent().getStringExtra("idUser");
        idPengelola = getIntent().getStringExtra("idPengelola");
        idPemberitahuan = getIntent().getStringExtra("idPemberitahuan");

        //setonclicklistener
        btnDetailKeluhan.setOnClickListener(this);

        //instance
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("pemberitahuans").document(idPemberitahuan).addSnapshotListener(PemberitahuanKeluhan.this, new EventListener<DocumentSnapshot>() {
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

        //get data user
        firebaseFirestore.collection("users").document(idUser).addSnapshotListener(PemberitahuanKeluhan.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                Users users = documentSnapshot.toObject(Users.class);
                namaPengguna.setText(users.getFull_name());
            }
        });

        //get data keluhan
        firebaseFirestore.collection("reports").document(idLaporan).addSnapshotListener(PemberitahuanKeluhan.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                Laporan laporans = documentSnapshot.toObject(Laporan.class);
                SimpleDateFormat format = new SimpleDateFormat("EEEE, dd MMMM yyyy");
                tanggalKeluhan.setText(format.format(laporans.getTanggal_laporan().getTime()));
                jenisKeluhan.setText(laporans.getJenis_laporan());
                deskripsiKeluhan.setText(laporans.getDeskripsi());
            }
        });

        setRead(idPemberitahuan);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnDetailKeluhan:
                Intent i = new Intent(PemberitahuanKeluhan.this, DetailLaporanPengguna.class);
                i.putExtra("idKost", idKost);
                i.putExtra("idLaporan", idLaporan);
                i.putExtra("idUser", idUser);
                i.putExtra("idPengelola", idPengelola);
                startActivity(i);
                break;
        }
    }

    private void setRead(String idPemberitahuan){

        Map<String, Object> pemberitahuan = new HashMap<>();
        pemberitahuan.put("status", "read");
        firebaseFirestore.collection("pemberitahuans").document(idPemberitahuan).update(pemberitahuan);

    }
}
