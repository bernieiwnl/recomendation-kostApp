package com.example.rekomendasikostapp.PENGELOLA.UPDATEKOST;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.rekomendasikostapp.ADAPTER.FasilitasUmumAdapter;
import com.example.rekomendasikostapp.CLASS.AksesLingkungan;
import com.example.rekomendasikostapp.CLASS.FasilitasKamar;
import com.example.rekomendasikostapp.CLASS.FasilitasUmum;
import com.example.rekomendasikostapp.PENGELOLA.ADAPTER.KostAksesLingkungan;
import com.example.rekomendasikostapp.PENGELOLA.ADAPTER.KostFasilitasKamar;
import com.example.rekomendasikostapp.PENGELOLA.ADAPTER.KostFasilitasUmum;
import com.example.rekomendasikostapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UpdateFasilitasKostActivity extends AppCompatActivity implements View.OnClickListener {

    private String idKost;
    private RecyclerView recyclerViewUmum;
    private RecyclerView recyclerViewKamar;
    private RecyclerView recyclerViewAkses;

    private KostFasilitasUmum fasilitasUmumAdapter;
    private KostFasilitasKamar fasilitasKamarAdapter;
    private KostAksesLingkungan aksesLingkunganAdapter;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private CollectionReference fasilitasKamarRef = firebaseFirestore.collection("fasilitas_kamar");
    private CollectionReference fasilitasUmumRef = firebaseFirestore.collection("fasilitas_umum");
    private CollectionReference aksesLingkunganRef = firebaseFirestore.collection("akses_lingkungan");

    private ArrayList<FasilitasUmum> fasilitasUmums;
    private ArrayList<FasilitasKamar> fasilitasKamars;
    private ArrayList<AksesLingkungan> aksesLingkungans;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_fasilitas_kost);

        //get intent extra
        idKost = getIntent().getStringExtra("idKost");

        //get object reference
        recyclerViewUmum = (RecyclerView) findViewById(R.id.recyclerFasilitasUmum);
        recyclerViewKamar = (RecyclerView) findViewById(R.id.recyclerFasilitasKamar);
        recyclerViewAkses = (RecyclerView) findViewById(R.id.recyclerAksesLingkungan);

        //set grid layout manager
        recyclerViewUmum.setLayoutManager(new GridLayoutManager(this,3));
        recyclerViewKamar.setLayoutManager(new GridLayoutManager(this , 3));
        recyclerViewAkses.setLayoutManager(new GridLayoutManager(this, 3));

        //get new array
        fasilitasUmums = new ArrayList<FasilitasUmum>();
        fasilitasKamars = new ArrayList<FasilitasKamar>();
        aksesLingkungans = new ArrayList<AksesLingkungan>();


        //call query method
        getQueryFasilitasKamar();
        getQueryFasilitasUmum();
        getQueryAksesLingkungan();
        

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
        }
    }

    void getQueryFasilitasKamar(){

        //get query firestore
        fasilitasKamarRef.addSnapshotListener(UpdateFasilitasKostActivity.this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    recyclerViewKamar.setVisibility(View.VISIBLE);
                    fasilitasKamars.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        FasilitasKamar data = doc.toObject(FasilitasKamar.class);
                        fasilitasKamars.add(data);
                    }
                    //set adapter
                    fasilitasKamarAdapter = new KostFasilitasKamar(getApplicationContext(), fasilitasKamars, idKost);
                    recyclerViewKamar.setAdapter(fasilitasKamarAdapter);
                }
                else {
                    recyclerViewKamar.setVisibility(View.GONE);
                }
            }
        });

    }

    void getQueryFasilitasUmum() {
        //get query firestore
        fasilitasUmumRef.addSnapshotListener(UpdateFasilitasKostActivity.this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    fasilitasUmums.clear();
                    recyclerViewUmum.setVisibility(View.VISIBLE);
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        FasilitasUmum data = doc.toObject(FasilitasUmum.class);
                        fasilitasUmums.add(data);
                    }
                    //set adapter
                    fasilitasUmumAdapter = new KostFasilitasUmum(getApplicationContext(), fasilitasUmums, idKost);
                    recyclerViewUmum.setAdapter(fasilitasUmumAdapter);
                }
                else {
                    recyclerViewUmum.setVisibility(View.GONE);
                }
            }
        });
    }


    void getQueryAksesLingkungan(){
        //get query firestore
        aksesLingkunganRef.addSnapshotListener(UpdateFasilitasKostActivity.this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    aksesLingkungans.clear();
                    recyclerViewAkses.setVisibility(View.VISIBLE);
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        AksesLingkungan data = doc.toObject(AksesLingkungan.class);
                        aksesLingkungans.add(data);
                    }
                    //set adapter
                    aksesLingkunganAdapter = new KostAksesLingkungan(getApplicationContext(), aksesLingkungans, idKost);
                    recyclerViewAkses.setAdapter(aksesLingkunganAdapter);
                }
                else {
                    recyclerViewAkses.setVisibility(View.GONE);
                }
            }
        });
    }

}
