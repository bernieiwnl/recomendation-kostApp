package com.example.rekomendasikostapp.REKOMENDASI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.rekomendasikostapp.ADAPTER.PrioritasFasilitasUmumAdapter;
import com.example.rekomendasikostapp.CLASS.AksesLingkungan;
import com.example.rekomendasikostapp.CLASS.FasilitasUmum;
import com.example.rekomendasikostapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TambahSubKriteriaFasilitasUmum extends AppCompatActivity {

    private RecyclerView recyclerViewKriteria;
    private PrioritasFasilitasUmumAdapter prioritasFasilitasUmumAdapter;
    private ArrayList<FasilitasUmum> fasilitasUmums;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private ArrayList<String> dataUmum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_sub_kriteria_fasilitas_umum);

        // get object reference
        recyclerViewKriteria = (RecyclerView) findViewById(R.id.recyclerKriteria);

        //get new array
        fasilitasUmums = new ArrayList<>();
        dataUmum = new ArrayList<>();

        dataUmum = (ArrayList<String>) getIntent().getSerializableExtra("fasilitasUmum");

        //get instance firebstore
        firebaseFirestore = FirebaseFirestore.getInstance();

        //get data from firestore
        firebaseFirestore.collection("fasilitas_umum").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(QueryDocumentSnapshot doc : task.getResult()){
                    FasilitasUmum data = doc.toObject(FasilitasUmum.class);
                    if(!dataUmum.contains(data.getIdfasilitas())){
                        fasilitasUmums.add(data);
                    }
                }
                prioritasFasilitasUmumAdapter = new PrioritasFasilitasUmumAdapter(TambahSubKriteriaFasilitasUmum.this, fasilitasUmums);
                recyclerViewKriteria.setAdapter(prioritasFasilitasUmumAdapter);
            }
        });


        recyclerViewKriteria.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerViewKriteria.addItemDecoration(dividerItemDecoration);
    }
}