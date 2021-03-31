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

import com.example.rekomendasikostapp.ADMIN.ADAPTER.AdminMasterLingkunganAdapter;
import com.example.rekomendasikostapp.CLASS.AksesLingkungan;
import com.example.rekomendasikostapp.MASTERDATA.InsertMasterDataLingkungan;
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

public class AdminAksesLingkungan extends AppCompatActivity implements View.OnClickListener, AdminMasterLingkunganAdapter.OnItemClickListener {

    private RecyclerView recyclerViewAksesLingkungan;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private ArrayList<AksesLingkungan> aksesLingkungans;
    private AdminMasterLingkunganAdapter adminMasterLingkunganAdapter;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_akses_lingkungan);

        // get object reference
        recyclerViewAksesLingkungan = (RecyclerView) findViewById(R.id.recyclerAksesLingkungan);
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButtonLingkungan);

        fab.setOnClickListener(this);


        //firebase instance reference
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        // set layout recyclerview
        recyclerViewAksesLingkungan.setLayoutManager(new GridLayoutManager(this , 3));

        //new array list
        aksesLingkungans = new ArrayList<AksesLingkungan>();

        //retreive data from firestore
        firebaseFirestore.collection("akses_lingkungan").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(!queryDocumentSnapshots.isEmpty()){
                    aksesLingkungans.clear();
                    for(QueryDocumentSnapshot query : queryDocumentSnapshots){
                        AksesLingkungan data = query.toObject(AksesLingkungan.class);
                        aksesLingkungans.add(data);
                    }
                    adminMasterLingkunganAdapter = new AdminMasterLingkunganAdapter(getApplicationContext(), aksesLingkungans);
                    recyclerViewAksesLingkungan.setAdapter(adminMasterLingkunganAdapter);
                    adminMasterLingkunganAdapter.setOnItemClickListener(AdminAksesLingkungan.this);
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.floatingActionButtonLingkungan:
                Intent intent = new Intent(getApplicationContext(), InsertMasterDataLingkungan.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onAksesItemClick(int position) {

    }

    @Override
    public void onAksesDeleteClick(int position) {
        final AksesLingkungan selected = aksesLingkungans.get(position);
        StorageReference imageRef = firebaseStorage.getReferenceFromUrl(selected.getFoto_url());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                firebaseFirestore.collection("akses_lingkungan").document(selected.getIdfasilitas()).delete();
            }
        });
    }
}
