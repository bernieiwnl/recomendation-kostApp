package com.example.rekomendasikostapp.REKOMENDASI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.rekomendasikostapp.ADAPTER.PrioritasAksesLingkunganAdapter;
import com.example.rekomendasikostapp.CLASS.AksesLingkungan;
import com.example.rekomendasikostapp.CLASS.SubKriteria;
import com.example.rekomendasikostapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TambahSubKriteriaFasilitasAkses extends AppCompatActivity {

    private RecyclerView recyclerViewKriteria;
    private PrioritasAksesLingkunganAdapter prioritasAksesLingkunganAdapter;
    private FirebaseFirestore firebaseFirestore;
    private ArrayList<AksesLingkungan> aksesLingkungans;

    private ArrayList<AksesLingkungan> fasilitasAkses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_sub_kriteria_fasilitas_akses);

        recyclerViewKriteria = (RecyclerView) findViewById(R.id.recyclerKriteria);

        //get new array
        aksesLingkungans = new ArrayList<>();
        fasilitasAkses = new ArrayList<>();

        fasilitasAkses = (ArrayList<AksesLingkungan>) getIntent().getSerializableExtra("aksesLingkungans");

        //get instance firebstore
        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("akses_lingkungan").addSnapshotListener(TambahSubKriteriaFasilitasAkses.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                    AksesLingkungan data = doc.toObject(AksesLingkungan.class);
                    if(!fasilitasAkses.contains(data)){
                        aksesLingkungans.add(data);
                    }
                }
                prioritasAksesLingkunganAdapter = new PrioritasAksesLingkunganAdapter(TambahSubKriteriaFasilitasAkses.this, aksesLingkungans);
                recyclerViewKriteria.setAdapter(prioritasAksesLingkunganAdapter);
            }
        });

        recyclerViewKriteria.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerViewKriteria.addItemDecoration(dividerItemDecoration);
    }
}