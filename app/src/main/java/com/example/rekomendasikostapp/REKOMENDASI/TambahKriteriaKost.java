package com.example.rekomendasikostapp.REKOMENDASI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.rekomendasikostapp.ADAPTER.KriteriaAdapter;
import com.example.rekomendasikostapp.CLASS.AksesLingkungan;
import com.example.rekomendasikostapp.CLASS.Kriteria;
import com.example.rekomendasikostapp.R;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class TambahKriteriaKost extends AppCompatActivity {

    private RecyclerView recyclerViewKriteria;
    private ArrayList<String> kriterias;
    private KriteriaAdapter kriteriaAdapter;


    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private ArrayList<String> dataKriteria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_kriteria_kost);


        //get object reference
        recyclerViewKriteria = (RecyclerView) findViewById(R.id.recyclerKriteria);

        kriterias = new ArrayList<>();
        dataKriteria = new ArrayList<>();

        dataKriteria = (ArrayList<String>) getIntent().getSerializableExtra("dataKriteria");

        kriterias.add("Harga");
        kriterias.add("Fasilitas Umum");
        kriterias.add("Fasilitas Kamar");
        kriterias.add("Akses Lokasi / Lingkungan");
        kriterias.add("Ukuran Kamar");
        kriterias.add("Jarak");

        for(String data : dataKriteria){
            if(kriterias.contains(data)){
                kriterias.remove(data);
            }
        }

        recyclerViewKriteria.setLayoutManager(new LinearLayoutManager(this));
        kriteriaAdapter = new KriteriaAdapter(TambahKriteriaKost.this,  kriterias);
        recyclerViewKriteria.setAdapter(kriteriaAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerViewKriteria.addItemDecoration(dividerItemDecoration);

    }
}