package com.example.rekomendasikostapp.REKOMENDASI;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.rekomendasikostapp.ADAPTER.AksesLingkunganAdapter;
import com.example.rekomendasikostapp.ADAPTER.FasilitasKamarAdapter;
import com.example.rekomendasikostapp.ADAPTER.FasilitasUmumAdapter;
import com.example.rekomendasikostapp.CLASS.AksesLingkungan;
import com.example.rekomendasikostapp.CLASS.FasilitasKamar;
import com.example.rekomendasikostapp.CLASS.FasilitasUmum;
import com.example.rekomendasikostapp.PENCARIAN.FilterKostActivity;
import com.example.rekomendasikostapp.R;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PemilihanJenisKost extends AppCompatActivity implements View.OnClickListener {

    private com.github.clans.fab.FloatingActionButton btnFilter;

    private RadioGroup radioGroupJenis;
    private android.widget.RadioGroup radioGroupSewa;
    private RadioGroup radioGroupUkuran;
    private EditText hargaMin;
    private EditText hargaMax;

    String jenis_sewa;
    String jenis_kost;
    String jenis_ukuran;

    private RecyclerView recyclerViewFasilitasUmum , recyclerViewFasilitasKamar , recyclerViewAksesLingkungan;
    private ArrayList<FasilitasUmum> fasilitasUmums;
    private ArrayList<FasilitasKamar> fasilitasKamars;
    private ArrayList<AksesLingkungan> aksesLingkungans;
    private FasilitasUmumAdapter fasilitasUmumAdapter;
    private FasilitasKamarAdapter fasilitasKamarAdapter;
    private AksesLingkunganAdapter aksesLingkunganAdapter;

    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemilihan_jenis_kost);

        //get Object Reference
        btnFilter = (FloatingActionButton) findViewById(R.id.buttonSave);
        //get object reference
        radioGroupJenis = (RadioGroup) findViewById(R.id.radioGroupJenisKost);
        radioGroupSewa = (RadioGroup) findViewById(R.id.radioGroupJenisPembayaran);
        radioGroupUkuran = (RadioGroup) findViewById(R.id.radioGroupJenisUkuran);

        hargaMax = (EditText) findViewById(R.id.hargaMaximal);
        hargaMin = (EditText) findViewById(R.id.hargaMinimal);

        recyclerViewFasilitasUmum = (RecyclerView) findViewById(R.id.recyclerFasilitasUmum);
        recyclerViewFasilitasKamar = (RecyclerView) findViewById(R.id.recyclerFasilitasKamar);
        recyclerViewAksesLingkungan = (RecyclerView) findViewById(R.id.recyclerAksesLingkungan);

        // set recylerview layout
        recyclerViewFasilitasUmum.setLayoutManager(new GridLayoutManager(this , 3));
        recyclerViewFasilitasKamar.setLayoutManager(new GridLayoutManager(this , 3));
        recyclerViewAksesLingkungan.setLayoutManager(new GridLayoutManager(this , 3));

        // set arrayList
        fasilitasUmums = new ArrayList<FasilitasUmum>();
        fasilitasKamars = new ArrayList<FasilitasKamar>();
        aksesLingkungans = new ArrayList<AksesLingkungan>();


        //firebase instance reference
        firebaseFirestore = FirebaseFirestore.getInstance();


        //retreive data from firestore
        firebaseFirestore.collection("fasilitas_umum").addSnapshotListener(PemilihanJenisKost.this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(!queryDocumentSnapshots.isEmpty()){
                    fasilitasUmums.clear();
                    for(QueryDocumentSnapshot query : queryDocumentSnapshots){
                        FasilitasUmum data = query.toObject(FasilitasUmum.class);
                        fasilitasUmums.add(data);
                    }
                    fasilitasUmumAdapter = new FasilitasUmumAdapter(getApplicationContext(), fasilitasUmums);
                    recyclerViewFasilitasUmum.setAdapter(fasilitasUmumAdapter);
                }
            }
        });

        //retreive data from firestore
        firebaseFirestore.collection("fasilitas_kamar").addSnapshotListener(PemilihanJenisKost.this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(!queryDocumentSnapshots.isEmpty()){
                    fasilitasKamars.clear();
                    for(QueryDocumentSnapshot query : queryDocumentSnapshots){
                        FasilitasKamar data = query.toObject(FasilitasKamar.class);
                        fasilitasKamars.add(data);
                    }
                    fasilitasKamarAdapter = new FasilitasKamarAdapter(getApplicationContext(), fasilitasKamars);
                    recyclerViewFasilitasKamar.setAdapter(fasilitasKamarAdapter);
                }
                else{
                    Log.d("TAG" , "Error Getting Query" + e);
                }
            }
        });

        //retreive data from firestore
        firebaseFirestore.collection("akses_lingkungan").addSnapshotListener(PemilihanJenisKost.this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(!queryDocumentSnapshots.isEmpty()){
                    aksesLingkungans.clear();
                    for(QueryDocumentSnapshot query : queryDocumentSnapshots){
                        AksesLingkungan data = query.toObject(AksesLingkungan.class);
                        aksesLingkungans.add(data);
                    }
                    aksesLingkunganAdapter = new AksesLingkunganAdapter(getApplicationContext(), aksesLingkungans);
                    recyclerViewAksesLingkungan.setAdapter(aksesLingkunganAdapter);
                }
                else{
                    Log.d("TAG" , "Error Getting Query" + e);
                }
            }
        });



        //get selected radio button
        radioGroupJenis.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.kostLakiLaki:
                        //your code here
                        jenis_kost = "Laki - Laki";
                        break;
                    case R.id.kostPerempuan:
                        //your code here
                        jenis_kost = "Perempuan";
                        break;
                }
            }
        });

        radioGroupUkuran.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.ukuran2x3:
                        //your code here
                        jenis_ukuran = "2m x 3m";
                        break;
                    case R.id.ukuran25x35:
                        //your code here
                        jenis_ukuran = "2.5m x 3.5m";
                        break;
                    case R.id.ukuran3x4:
                        //your code here
                        jenis_ukuran = "3m x 4m";
                        break;
                }
            }
        });


        radioGroupSewa.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.sewaTahun:
                        //your code here
                        jenis_sewa = "Tahun";
                        break;
                    case R.id.sewaSemester:
                        //your code here
                        jenis_sewa = "Semester";
                        break;
                    case R.id.sewaBulanan:
                        //your code here
                        jenis_sewa = "Bulan";
                        break;
                }
            }
        });

        //set on click listener
        btnFilter.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonSave:
                getFilterData();
                break;
        }
    }

    private void getFilterData(){
        Intent i = new Intent();
        i.putExtra("hargaMax", hargaMax.getText().toString().trim());
        i.putExtra("hargaMin", hargaMin.getText().toString().trim());
        i.putExtra("jenis_kost", jenis_kost);
        i.putExtra("jenis_sewa", jenis_sewa);
        i.putExtra("jenis_ukuran", jenis_ukuran);
        i.putStringArrayListExtra("list_fasilitas_umum", fasilitasUmumAdapter.getArrayList());
        i.putStringArrayListExtra("list_akses_lingkungan", aksesLingkunganAdapter.getArrayList());
        i.putStringArrayListExtra("list_fasilitas_kamar", fasilitasKamarAdapter.getArrayList());
        setResult(RESULT_OK, i);
        finish();
    }


}
