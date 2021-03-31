package com.example.rekomendasikostapp.REKOMENDASI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.rekomendasikostapp.ADAPTER.PrioritasFasilitasKamarAdapter;
import com.example.rekomendasikostapp.CLASS.FasilitasKamar;
import com.example.rekomendasikostapp.CLASS.FasilitasUmum;
import com.example.rekomendasikostapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TambahSubKriteriaFasilitasKamar extends AppCompatActivity {

    private RecyclerView recyclerViewKriteria;
    private PrioritasFasilitasKamarAdapter prioritasFasilitasKamarAdapter;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private com.github.clans.fab.FloatingActionButton buttonLanjutkan;

    private ArrayList<FasilitasKamar> fasilitasKamars;
    private ArrayList<String> dataKamar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_sub_kriteria_fasilitas_kamar);

        // get object reference
        recyclerViewKriteria = (RecyclerView) findViewById(R.id.recyclerKriteria);

        //get new array
        fasilitasKamars = new ArrayList<>();
        dataKamar = new ArrayList<>();

        //get instance firebstore
        firebaseFirestore = FirebaseFirestore.getInstance();

        dataKamar = (ArrayList<String>) getIntent().getSerializableExtra("fasilitasKamar");


        //get data from firestore
        firebaseFirestore.collection("fasilitas_kamar").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(QueryDocumentSnapshot doc : task.getResult()){
                    FasilitasKamar data = doc.toObject(FasilitasKamar.class);
                    if(!dataKamar.contains(data.getIdfasilitas())){
                        fasilitasKamars.add(data);
                    }
                }
                prioritasFasilitasKamarAdapter = new PrioritasFasilitasKamarAdapter(TambahSubKriteriaFasilitasKamar.this, fasilitasKamars);
                recyclerViewKriteria.setAdapter(prioritasFasilitasKamarAdapter);
            }
        });

        recyclerViewKriteria.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerViewKriteria.addItemDecoration(dividerItemDecoration);
    }
}