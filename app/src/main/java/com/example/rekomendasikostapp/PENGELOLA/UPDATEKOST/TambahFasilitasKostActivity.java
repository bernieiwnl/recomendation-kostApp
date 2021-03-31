package com.example.rekomendasikostapp.PENGELOLA.UPDATEKOST;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.rekomendasikostapp.ADAPTER.AksesLingkunganAdapter;
import com.example.rekomendasikostapp.ADAPTER.FasilitasKamarAdapter;
import com.example.rekomendasikostapp.ADAPTER.FasilitasUmumAdapter;
import com.example.rekomendasikostapp.CLASS.AksesLingkungan;
import com.example.rekomendasikostapp.CLASS.FasilitasKamar;
import com.example.rekomendasikostapp.CLASS.FasilitasUmum;
import com.example.rekomendasikostapp.PENGELOLA.DetailKostPengelola;
import com.example.rekomendasikostapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TambahFasilitasKostActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseFirestore firebaseFirestore;
    private RecyclerView recyclerViewUmum;
    private RecyclerView recyclerViewKamar;
    private RecyclerView recyclerViewAkses;

    private FloatingActionButton floatingActionButtonSave;

    private ArrayList<FasilitasUmum> fasilitasUmums;
    private ArrayList<FasilitasKamar> fasilitasKamars;
    private ArrayList<AksesLingkungan> aksesLingkungans;

    private String idKost;

    //list recycler view
    private FasilitasUmumAdapter fasilitasUmumAdapter;
    private FasilitasKamarAdapter fasilitasKamarAdapter;
    private AksesLingkunganAdapter aksesLingkunganAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_fasilitas_kost);

        // get intent
        idKost = getIntent().getStringExtra("idKost");

        //get object reference
        recyclerViewUmum = (RecyclerView) findViewById(R.id.recyclerFasilitasUmum);
        recyclerViewKamar = (RecyclerView) findViewById(R.id.recyclerFasilitasKamar);
        recyclerViewAkses = (RecyclerView) findViewById(R.id.recyclerAksesLingkungan);
        floatingActionButtonSave = (FloatingActionButton) findViewById(R.id.floatActionButtonSave);

        // set recylerview layout
        recyclerViewUmum.setLayoutManager(new GridLayoutManager(this , 3));
        recyclerViewKamar.setLayoutManager(new GridLayoutManager(this , 3));
        recyclerViewAkses.setLayoutManager(new GridLayoutManager(this , 3));

        // set arrayList
        fasilitasUmums = new ArrayList<FasilitasUmum>();
        fasilitasKamars = new ArrayList<FasilitasKamar>();
        aksesLingkungans = new ArrayList<AksesLingkungan>();


        //set on click listener
        floatingActionButtonSave.setOnClickListener(this);

        //get instance
        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("fasilitas_umum").addSnapshotListener(TambahFasilitasKostActivity.this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if(!queryDocumentSnapshots.isEmpty()){
                        fasilitasUmums.clear();
                        for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                            FasilitasUmum data = doc.toObject(FasilitasUmum.class);
                            if(!data.getIdkosts().contains(idKost)){
                                fasilitasUmums.add(data);
                            }
                        }
                        fasilitasUmumAdapter = new FasilitasUmumAdapter(getApplicationContext() , fasilitasUmums);
                        recyclerViewUmum.setAdapter(fasilitasUmumAdapter);
                    }
                    else
                    {
                        Log.d("TAG" , "Error Getting Query" + e);
                    }
            }
        });

        firebaseFirestore.collection("fasilitas_kamar").addSnapshotListener(TambahFasilitasKostActivity.this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(!queryDocumentSnapshots.isEmpty()){
                    fasilitasKamars.clear();
                    for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                        FasilitasKamar data = doc.toObject(FasilitasKamar.class);
                        if(!data.getIdkosts().contains(idKost)){
                            fasilitasKamars.add(data);
                        }
                    }
                    fasilitasKamarAdapter = new FasilitasKamarAdapter(getApplicationContext() , fasilitasKamars);
                    recyclerViewKamar.setAdapter(fasilitasKamarAdapter);
                }
                else
                {
                    Log.d("TAG" , "Error Getting Query" + e);
                }
            }
        });

        firebaseFirestore.collection("akses_lingkungan").addSnapshotListener(TambahFasilitasKostActivity.this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(!queryDocumentSnapshots.isEmpty()){
                    aksesLingkungans.clear();
                    for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                        AksesLingkungan data = doc.toObject(AksesLingkungan.class);
                        if(!data.getIdkosts().contains(idKost)){
                            aksesLingkungans.add(data);
                        }
                    }
                    aksesLingkunganAdapter = new AksesLingkunganAdapter(getApplicationContext() , aksesLingkungans);
                    recyclerViewAkses.setAdapter(aksesLingkunganAdapter);
                }
                else
                {
                    Log.d("TAG" , "Error Getting Query" + e);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.floatActionButtonSave:
                insertAksesLingkungan(idKost);
                insertFasilitasKamar(idKost);
                insertFasilitasUmum(idKost);
                finish();
                break;
        }
    }

    void insertFasilitasUmum(String documentId){
        fasilitasUmumAdapter.getArrayList();

        for(int i = 0; i< fasilitasUmumAdapter.getArrayList().size(); i++)
        {
            firebaseFirestore.collection("fasilitas_umum").document(fasilitasUmumAdapter.getArrayList().get(i)).update("idkosts", FieldValue.arrayUnion(documentId));
            firebaseFirestore.collection("data_kost").document(documentId).update("fasilitas_umums", FieldValue.arrayUnion(fasilitasUmumAdapter.getArrayList().get(i)));
        }
    }

    void insertFasilitasKamar(String documentId){
        fasilitasKamarAdapter.getArrayList();

        for(int i = 0; i< fasilitasKamarAdapter.getArrayList().size(); i++)
        {
            firebaseFirestore.collection("fasilitas_kamar").document(fasilitasKamarAdapter.getArrayList().get(i)).update("idkosts", FieldValue.arrayUnion(documentId));
            firebaseFirestore.collection("data_kost").document(documentId).update("fasilitas_kamars", FieldValue.arrayUnion(fasilitasKamarAdapter.getArrayList().get(i)));
        }
    }

    void insertAksesLingkungan(String documentId){
        aksesLingkunganAdapter.getArrayList();

        for(int i = 0; i< aksesLingkunganAdapter.getArrayList().size(); i++)
        {
            firebaseFirestore.collection("akses_lingkungan").document(aksesLingkunganAdapter.getArrayList().get(i)).update("idkosts", FieldValue.arrayUnion(documentId));
            firebaseFirestore.collection("data_kost").document(documentId).update("akses_lingkungans", FieldValue.arrayUnion(aksesLingkunganAdapter.getArrayList().get(i)));
        }
    }

}
