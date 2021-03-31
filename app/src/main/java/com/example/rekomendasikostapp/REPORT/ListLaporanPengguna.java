package com.example.rekomendasikostapp.REPORT;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.rekomendasikostapp.ADAPTER.LaporanPenggunaAdapter;
import com.example.rekomendasikostapp.CLASS.Laporan;
import com.example.rekomendasikostapp.CLASS.Pemesanan;
import com.example.rekomendasikostapp.CLASS.Users;
import com.example.rekomendasikostapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ListLaporanPengguna extends AppCompatActivity {

    private RecyclerView recyclerViewLaporan;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private ArrayList<Laporan> laporans;
    private LaporanPenggunaAdapter laporanPenggunaAdapter;
    private ImageView empty_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_laporan_pengguna);

        //get object reference
        recyclerViewLaporan = (RecyclerView) findViewById(R.id.recylerLaporan);
        empty_message = (ImageView) findViewById(R.id.empty_message);

        // get new array
        laporans = new ArrayList<>();

        // set layout recyclerview
        recyclerViewLaporan.setLayoutManager(new LinearLayoutManager(this));

        //instance firebase
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).addSnapshotListener(ListLaporanPengguna.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                Users docdata = documentSnapshot.toObject(Users.class);

                if(docdata.getAccount().equals("Admin")){

                    //get data from firebase
                    firebaseFirestore.collection("reports").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            laporans.clear();
                            if(queryDocumentSnapshots.isEmpty())
                            {
                                recyclerViewLaporan.setVisibility(View.GONE);
                                empty_message.setVisibility(View.VISIBLE);
                            }
                            else{
                                for(QueryDocumentSnapshot doc : queryDocumentSnapshots)
                                {
                                    Laporan data = doc.toObject(Laporan.class);
                                    laporans.add(data);
                                }
                            }

                            //sort high to low
                            Collections.sort(laporans, new Comparator<Laporan>() {
                                @Override
                                public int compare(Laporan o1, Laporan o2) {
                                    return o2.getTanggal_laporan().compareTo(o1.getTanggal_laporan());
                                }
                            });

                            laporanPenggunaAdapter = new LaporanPenggunaAdapter(ListLaporanPengguna.this , laporans);
                            recyclerViewLaporan.setAdapter(laporanPenggunaAdapter);
                        }
                    });
                }
                else if(docdata.getAccount().equals("Pengguna") || docdata.getAccount().equals("Pengelola")){
                    //get data from firebase
                    firebaseFirestore.collection("reports").document(firebaseAuth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                            laporans.clear();
                            if(!documentSnapshot.exists())
                            {
                                recyclerViewLaporan.setVisibility(View.GONE);
                                empty_message.setVisibility(View.VISIBLE);
                            }
                            else{
                                    Laporan data = documentSnapshot.toObject(Laporan.class);
                                    laporans.add(data);
                            }
                            //sort high to low
                            Collections.sort(laporans, new Comparator<Laporan>() {
                                @Override
                                public int compare(Laporan o1, Laporan o2) {
                                    return o2.getTanggal_laporan().compareTo(o1.getTanggal_laporan());
                                }
                            });

                            laporanPenggunaAdapter = new LaporanPenggunaAdapter(ListLaporanPengguna.this , laporans);
                            recyclerViewLaporan.setAdapter(laporanPenggunaAdapter);
                        }
                    });
                }
            }
        });

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL);
        recyclerViewLaporan.addItemDecoration(dividerItemDecoration);

    }
}
