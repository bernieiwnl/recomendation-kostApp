package com.example.rekomendasikostapp.VERIFICATION;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.example.rekomendasikostapp.CLASS.Kost;
import com.example.rekomendasikostapp.PENGELOLA.ADAPTER.DaftarKostPengelolaAdapter;
import com.example.rekomendasikostapp.PENGELOLA.ListKostPengelola;
import com.example.rekomendasikostapp.R;
import com.example.rekomendasikostapp.VERIFICATION.ADAPTER.KostVerificationAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class KostVerificationActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private RecyclerView recyclerViewKost;
    private ArrayList<Kost> kosts;
    private KostVerificationAdapter kostVerificationAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kost_verification);


        //get object reference
        recyclerViewKost = (RecyclerView) findViewById(R.id.recylerKost);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);

        //set new array
        kosts = new ArrayList<>();

        //get firebase instance
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        // set layout recyclerview
        recyclerViewKost.setLayoutManager(new LinearLayoutManager(this));

        //set refresh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        //retreive data from database
                        firebaseFirestore.collection("data_kost").whereEqualTo("isVerification", false).addSnapshotListener(KostVerificationActivity.this,new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                if(!queryDocumentSnapshots.isEmpty()) {
                                    kosts.clear();
                                    for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                                        Kost data = doc.toObject(Kost.class);
                                        kosts.add(data);
                                    }
                                    kostVerificationAdapter = new KostVerificationAdapter(getApplicationContext() , kosts);
                                    recyclerViewKost.setAdapter(kostVerificationAdapter);
                                }
                                else{
                                    Log.d("Tag", "Error Geting Query" + e);
                                    recyclerViewKost.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                }, 1000);
            }
        });

        //retreive data from database
        firebaseFirestore.collection("data_kost").whereEqualTo("isVerification", false).addSnapshotListener(KostVerificationActivity.this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(!queryDocumentSnapshots.isEmpty()) {
                    kosts.clear();
                    for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                        Kost data = doc.toObject(Kost.class);
                        kosts.add(data);
                    }
                    kostVerificationAdapter = new KostVerificationAdapter(getApplicationContext() , kosts);
                    recyclerViewKost.setAdapter(kostVerificationAdapter);
                }
                else{
                    Log.d("Tag", "Error Geting Query" + e);
                    recyclerViewKost.setVisibility(View.GONE);
                }
            }
        });

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerViewKost.addItemDecoration(dividerItemDecoration);

    }
}
