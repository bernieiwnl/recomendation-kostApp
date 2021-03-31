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

import com.example.rekomendasikostapp.ADMIN.ADAPTER.AdminMasterUmumAdapter;
import com.example.rekomendasikostapp.CLASS.FasilitasUmum;
import com.example.rekomendasikostapp.MASTERDATA.InsertMasterDataUmum;
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

public class AdminFasilitasUmum extends AppCompatActivity implements View.OnClickListener, AdminMasterUmumAdapter.OnItemClickListener {

    private RecyclerView recyclerViewFasilitasUmum;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private ArrayList<FasilitasUmum> fasilitasUmums;
    private AdminMasterUmumAdapter adminMasterUmumAdapter;
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_fasilitas_umum);


        // get object reference
        recyclerViewFasilitasUmum = (RecyclerView) findViewById(R.id.recyclerFasilitasUmum);
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButtonUmum);

        fab.setOnClickListener(this);

        //firebase instance reference
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        // set layout recyclerview
        recyclerViewFasilitasUmum.setLayoutManager(new GridLayoutManager(this , 3));

        //new array list
        fasilitasUmums = new ArrayList<FasilitasUmum>();

        //retreive data from firestore
        firebaseFirestore.collection("fasilitas_umum").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(!queryDocumentSnapshots.isEmpty()){
                    fasilitasUmums.clear();
                    for(QueryDocumentSnapshot query : queryDocumentSnapshots){
                        FasilitasUmum data = query.toObject(FasilitasUmum.class);
                        fasilitasUmums.add(data);
                    }
                    adminMasterUmumAdapter = new AdminMasterUmumAdapter(getApplicationContext(), fasilitasUmums);
                    recyclerViewFasilitasUmum.setAdapter(adminMasterUmumAdapter);
                    adminMasterUmumAdapter.setOnItemClickListener(AdminFasilitasUmum.this);
                }
                else{
                    Log.d("TAG" , "Error Getting Query " + e);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.floatingActionButtonUmum:
                Intent intent = new Intent(getApplicationContext(), InsertMasterDataUmum.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onUmumItemClick(int position) {

    }

    @Override
    public void onUmumDeleteClick(int position) {
        final FasilitasUmum selected = fasilitasUmums.get(position);
        StorageReference imageRef = firebaseStorage.getReferenceFromUrl(selected.getFoto_url());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                firebaseFirestore.collection("fasilitas_umum").document(selected.getIdfasilitas()).delete();
            }
        });
    }
}
