package com.example.rekomendasikostapp.PEMESANAN;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.os.Bundle;

import com.example.rekomendasikostapp.ADAPTER.DurasiPemesananAdapter;
import com.example.rekomendasikostapp.CLASS.Kost;
import com.example.rekomendasikostapp.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Arrays;

public class ListDurasiPemesanan extends AppCompatActivity {

    RecyclerView recyclerViewDurasi;
    ArrayList<String> listDurasi;
    DurasiPemesananAdapter durasiPemesananAdapter;

    FirebaseFirestore firebaseFirestore;

    String idKost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_durasi_pemesanan);

        //new ArrayList
        listDurasi = new ArrayList<String>();

        //get object reference
        recyclerViewDurasi = (RecyclerView) findViewById(R.id.recycleViewDurasi);

        //set layout manager
        recyclerViewDurasi.setLayoutManager(new LinearLayoutManager(this));

        firebaseFirestore = FirebaseFirestore.getInstance();

        //intent
        idKost = getIntent().getStringExtra("idKost");

        firebaseFirestore.collection("data_kost").document(idKost).addSnapshotListener(ListDurasiPemesanan.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                Kost dataKost = documentSnapshot.toObject(Kost.class);

                if(dataKost.getJenis_sewa().equals("Semester"))
                {
                    listDurasi.add("1 Semester");
                    listDurasi.add("2 Semester");
                    listDurasi.add("3 Semester");
                }
                else if(dataKost.getJenis_sewa().equals("Bulan"))
                {
                    listDurasi.add("1 Bulan");
                    listDurasi.add("2 Bulan");
                    listDurasi.add("3 Bulan");
                    listDurasi.add("4 Bulan");
                }
                else if(dataKost.getJenis_sewa().equals("Tahun"))
                {
                    listDurasi.add("1 Tahun");
                    listDurasi.add("2 Tahun");
                    listDurasi.add("3 Tahun");
                    listDurasi.add("4 Tahun");
                }

            }
        });


        //set to adapter
        durasiPemesananAdapter = new DurasiPemesananAdapter(ListDurasiPemesanan.this, listDurasi);
        recyclerViewDurasi.setAdapter(durasiPemesananAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerViewDurasi.addItemDecoration(dividerItemDecoration);
    }
}
