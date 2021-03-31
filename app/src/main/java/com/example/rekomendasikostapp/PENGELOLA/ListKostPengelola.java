package com.example.rekomendasikostapp.PENGELOLA;

import androidx.annotation.NonNull;
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

import com.example.rekomendasikostapp.CLASS.AksesLingkungan;
import com.example.rekomendasikostapp.CLASS.Alternatif;
import com.example.rekomendasikostapp.CLASS.FasilitasKamar;
import com.example.rekomendasikostapp.CLASS.FasilitasUmum;
import com.example.rekomendasikostapp.CLASS.Kost;
import com.example.rekomendasikostapp.PENGELOLA.ADAPTER.DaftarKostPengelolaAdapter;
import com.example.rekomendasikostapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ListKostPengelola extends AppCompatActivity implements DaftarKostPengelolaAdapter.OnItemClickListener {

    private RecyclerView recyclerViewKost;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private ArrayList<Kost> kosts;
    private DaftarKostPengelolaAdapter daftarKostPengelolaAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_kost_pengelola);

        recyclerViewKost = (RecyclerView) findViewById(R.id.recylerKost);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();


        // set layout recyclerview
        recyclerViewKost.setLayoutManager(new LinearLayoutManager(this));



        //new array list
        kosts = new ArrayList<Kost>();

        //set refresh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        recreate();
                    }
                }, 1000);
            }
        });

        //retreive data from database
        firebaseFirestore.collection("data_kost").whereEqualTo("idUser", firebaseAuth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(!queryDocumentSnapshots.isEmpty()) {
                    kosts.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Kost data = doc.toObject(Kost.class);
                        kosts.add(data);
                    }

                    //sort high to low
                    Collections.sort(kosts, new Comparator<Kost>() {
                        @Override
                        public int compare(Kost o1, Kost o2) {
                            return o2.getCreated_at().compareTo(o1.getCreated_at());
                        }
                    });

                    daftarKostPengelolaAdapter = new DaftarKostPengelolaAdapter(getApplicationContext(), kosts);
                    recyclerViewKost.setAdapter(daftarKostPengelolaAdapter);
                    daftarKostPengelolaAdapter.setOnItemClickListener(ListKostPengelola.this);
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

    @Override
    public void onKostItemClick(int position) {

    }

    @Override
    public void onKostDeleteClick(int position) {
            final Kost dataKost = kosts.get(position);
            for(int i = 0; i < dataKost.getFoto_kost().size(); i++){
                StorageReference imageRef = firebaseStorage.getReferenceFromUrl(dataKost.getFoto_kost().get(i));
                imageRef.delete();
            }
            firebaseFirestore.collection("data_kost").document(dataKost.getIdKost()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                     firebaseFirestore.collection("fasilitas_umum").whereArrayContains("idkosts", dataKost.getIdKost()).addSnapshotListener(ListKostPengelola.this,new EventListener<QuerySnapshot>() {
                         @Override
                         public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                              for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                                  FasilitasUmum fasilitasUmum = doc.toObject(FasilitasUmum.class);
                                  firebaseFirestore.collection("fasilitas_umum").document(fasilitasUmum.getIdfasilitas()).update("idkosts", FieldValue.arrayRemove(dataKost.getIdKost()));
                              }
                         }
                     });

                     firebaseFirestore.collection("fasilitas_kamar").whereArrayContains("idkosts", dataKost.getIdKost()).addSnapshotListener(ListKostPengelola.this,new EventListener<QuerySnapshot>() {
                         @Override
                         public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                             for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                                 FasilitasKamar fasilitasKamar = doc.toObject(FasilitasKamar.class);
                                 firebaseFirestore.collection("fasilitas_kamar").document(fasilitasKamar.getIdfasilitas()).update("idkosts", FieldValue.arrayRemove(dataKost.getIdKost()));
                             }
                         }
                     });

                     firebaseFirestore.collection("akses_lingkungan").whereArrayContains("idkosts", dataKost.getIdKost()).addSnapshotListener(ListKostPengelola.this,new EventListener<QuerySnapshot>() {
                         @Override
                         public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                             for(QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                 AksesLingkungan aksesLingkungan = doc.toObject(AksesLingkungan.class);
                                 firebaseFirestore.collection("akses_lingkungan").document(aksesLingkungan.getIdfasilitas()).update("idkosts", FieldValue.arrayRemove(dataKost.getIdKost()));
                             }
                         }
                     });
                     daftarKostPengelolaAdapter.notifyDataSetChanged();
                }
            });
    }
}
