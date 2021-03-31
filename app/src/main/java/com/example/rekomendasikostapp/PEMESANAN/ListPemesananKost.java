package com.example.rekomendasikostapp.PEMESANAN;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.example.rekomendasikostapp.ADAPTER.PemesananKostAdapter;
import com.example.rekomendasikostapp.CLASS.Pemberitahuan;
import com.example.rekomendasikostapp.CLASS.Pemesanan;
import com.example.rekomendasikostapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ListPemesananKost extends AppCompatActivity {

    private RecyclerView recyclerViewPemesanan;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private ArrayList<Pemesanan> pemesanans;
    private PemesananKostAdapter pemesananKostAdapter;
    private ImageView empty_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pemesanan_kost);

        //get object reference
        recyclerViewPemesanan = (RecyclerView) findViewById(R.id.recylerPemesanan);
        empty_message = (ImageView) findViewById(R.id.empty_message);

        // get new array
        pemesanans = new ArrayList<>();

        // set layout recyclerview
        recyclerViewPemesanan.setLayoutManager(new LinearLayoutManager(this));

        //instance firebase
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();


        //get data from firebase
        firebaseFirestore.collection("pemesanans").whereEqualTo("idPengelola", firebaseAuth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                pemesanans.clear();
                if(queryDocumentSnapshots.isEmpty())
                {
                        recyclerViewPemesanan.setVisibility(View.GONE);
                        empty_message.setVisibility(View.VISIBLE);
                }
                else{
                    for(QueryDocumentSnapshot doc : queryDocumentSnapshots)
                    {
                        Pemesanan data = doc.toObject(Pemesanan.class);
                        pemesanans.add(data);
                    }
                }
                //sort high to low
                Collections.sort(pemesanans, new Comparator<Pemesanan>() {
                    @Override
                    public int compare(Pemesanan o1, Pemesanan o2) {
                        return o2.getTanggal_pemesanan().compareTo(o1.getTanggal_pemesanan());
                    }
                });

                pemesananKostAdapter = new PemesananKostAdapter(ListPemesananKost.this , pemesanans);
                recyclerViewPemesanan.setAdapter(pemesananKostAdapter);
            }
        });


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL);
        recyclerViewPemesanan.addItemDecoration(dividerItemDecoration);

    }
}
