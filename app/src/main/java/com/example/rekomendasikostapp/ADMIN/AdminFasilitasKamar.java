package com.example.rekomendasikostapp.ADMIN;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.rekomendasikostapp.ADMIN.ADAPTER.AdminMasterKamarAdapter;
import com.example.rekomendasikostapp.CLASS.FasilitasKamar;
import com.example.rekomendasikostapp.MASTERDATA.InsertMasterDataKamar;
import com.example.rekomendasikostapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AdminFasilitasKamar extends AppCompatActivity implements View.OnClickListener, AdminMasterKamarAdapter.OnItemClickListener {

    private RecyclerView recyclerViewFasilitasKamar;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private ArrayList<FasilitasKamar> fasilitasKamars;
    private AdminMasterKamarAdapter adminMasterKamarAdapter;
    private FloatingActionButton fab;
    private FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_fasilitas_kamar);

        // get object reference
        recyclerViewFasilitasKamar = (RecyclerView) findViewById(R.id.recyclerFasilitasKamar);
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButtonKamar);

        fab.setOnClickListener(this);

        //firebase instance reference
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        // set layout recyclerview
        recyclerViewFasilitasKamar.setLayoutManager(new GridLayoutManager(this , 3));

        //new array list
        fasilitasKamars = new ArrayList<FasilitasKamar>();

        //retreive data from firestore
        firebaseFirestore.collection("fasilitas_kamar").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(!queryDocumentSnapshots.isEmpty()){
                    fasilitasKamars.clear();
                    for(QueryDocumentSnapshot query : queryDocumentSnapshots){
                        FasilitasKamar data = query.toObject(FasilitasKamar.class);
                        fasilitasKamars.add(data);
                    }
                    adminMasterKamarAdapter = new AdminMasterKamarAdapter(getApplicationContext(), fasilitasKamars);
                    recyclerViewFasilitasKamar.setAdapter(adminMasterKamarAdapter);
                    adminMasterKamarAdapter.setOnItemClickListener(AdminFasilitasKamar.this);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.floatingActionButtonKamar:
                Intent intent = new Intent(getApplicationContext(), InsertMasterDataKamar.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onKamarItemClick(int position) {

    }

    @Override
    public void onKamarDeleteClick(int position) {
        final FasilitasKamar selected = fasilitasKamars.get(position);
        StorageReference imageRef = firebaseStorage.getReferenceFromUrl(selected.getFoto_url());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                 firebaseFirestore.collection("fasilitas_kamar").document(selected.getIdfasilitas()).delete();
            }
        });

    }
}
