package com.example.rekomendasikostapp.PEMBERITAHUAN.PENGGUNA;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.rekomendasikostapp.CLASS.Pemberitahuan;
import com.example.rekomendasikostapp.MESSAGING.MessageActivity;
import com.example.rekomendasikostapp.PEMBERITAHUAN.PENGELOLA.PemberitahuanPenghuniBaru;
import com.example.rekomendasikostapp.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class PemberitahuanPemesananBerhasil extends AppCompatActivity implements View.OnClickListener {

    private TextView judulPemberitahuan;
    private TextView isiPemberitahuan;
    private TextView tanggalPemberitahuan;

    private FirebaseFirestore firebaseFirestore;


    String idPemberitahuan;
    String idSender;

    private Button btnChatPengelola;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemberitahuan_pemesanan_berhasil);

        //get object reference
        judulPemberitahuan = (TextView) findViewById(R.id.judulPemberitahuan);
        isiPemberitahuan = (TextView) findViewById(R.id.isiPemberitahuan);
        tanggalPemberitahuan = (TextView) findViewById(R.id.tanggalPemberitaahuan);

        btnChatPengelola = (Button) findViewById(R.id.btnChatPengelola);

        btnChatPengelola.setOnClickListener(this);

        //get intent
        idPemberitahuan = getIntent().getStringExtra("idPemberitahuan");
        idSender = getIntent().getStringExtra("idSender");



        // firesbase instance;
        firebaseFirestore = FirebaseFirestore.getInstance();

        //firestore
        firebaseFirestore.collection("pemberitahuans").document(idPemberitahuan).addSnapshotListener(PemberitahuanPemesananBerhasil.this, new EventListener<DocumentSnapshot>() {
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
            case R.id.btnChatPengelola:
                Intent i = new Intent(PemberitahuanPemesananBerhasil.this , MessageActivity.class);
                i.putExtra("idUser", idSender);
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
