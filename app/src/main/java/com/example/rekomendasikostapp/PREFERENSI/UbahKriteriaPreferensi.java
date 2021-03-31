package com.example.rekomendasikostapp.PREFERENSI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rekomendasikostapp.ADAPTER.KriteriaAdapter;
import com.example.rekomendasikostapp.ADAPTER.PrioritasFasilitasKamarAdapter;
import com.example.rekomendasikostapp.CLASS.FasilitasKamar;
import com.example.rekomendasikostapp.CLASS.Kriteria;
import com.example.rekomendasikostapp.CLASS.Preferensi;
import com.example.rekomendasikostapp.CLASS.SubKriteria;
import com.example.rekomendasikostapp.R;
import com.example.rekomendasikostapp.REKOMENDASI.PemilihanKriteriaActivity;
import com.example.rekomendasikostapp.REKOMENDASI.PrioritasFasilitasUmum;
import com.example.rekomendasikostapp.REKOMENDASI.TambahKriteriaKost;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Collections;

public class UbahKriteriaPreferensi extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerViewKriteria;
    private ArrayList<String> kriterias;
    private KriteriaAdapter kriteriaAdapter;
    private FloatingActionButton buttonSimpanPreferensi;
    private ImageView buttonAdd;


    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private ArrayList<Kriteria> dataKriteria;

    static UbahKriteriaPreferensi ubahKriteriaPreferensi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_kriteria_preferensi);

        //get activity reference
        ubahKriteriaPreferensi = this;

        //get object reference
        recyclerViewKriteria = (RecyclerView) findViewById(R.id.recyclerKriteria);
        buttonSimpanPreferensi = (FloatingActionButton) findViewById(R.id.buttonSimpanPreferensi);
        buttonAdd = (ImageView) findViewById(R.id.buttonAdd);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        //set onclick listener
        buttonSimpanPreferensi.setOnClickListener(this);
        buttonAdd.setOnClickListener(this);

        kriterias = new ArrayList<>();
        firebaseFirestore.collection("preferensi").document(firebaseAuth.getCurrentUser().getUid()).addSnapshotListener(UbahKriteriaPreferensi.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                final Preferensi dataPreferensiKamar = documentSnapshot.toObject(Preferensi.class);
                kriterias.clear();
                for(Kriteria data : dataPreferensiKamar.getPreferensiKriteria())
                {
                    kriterias.add(data.getNamaKriteria());
                }
                kriteriaAdapter = new KriteriaAdapter(UbahKriteriaPreferensi.this,  kriterias);
                recyclerViewKriteria.setAdapter(kriteriaAdapter);
            }
        });

        recyclerViewKriteria.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerViewKriteria.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerViewKriteria);
    }


    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();

            Collections.swap(kriterias, fromPosition, toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            kriterias.remove(viewHolder.getAdapterPosition());
            recyclerViewKriteria.getAdapter().notifyDataSetChanged();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.buttonSimpanPreferensi:
                simpanPreferensiKriteria();
                break;
            case R.id.buttonAdd:
                Intent i = new Intent(UbahKriteriaPreferensi.this , TambahKriteriaKost.class);
                i.putExtra("dataKriteria", kriterias);
                startActivityForResult(i, 1);
                break;
        }
    }

    private void simpanPreferensiKriteria(){

        checkBobotKriteria();

        //get instance firebase firestore and auth
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseFirestore.collection("preferensi").document(firebaseAuth.getCurrentUser().getUid()).update("preferensiKriteria", dataKriteria).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
               finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_SHORT ).show();
            }
        });
    }

    public static UbahKriteriaPreferensi getInstance(){
        return  ubahKriteriaPreferensi;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK) {
            String dataKriteria = (String) data.getSerializableExtra("fasilitas");
            kriterias.add(dataKriteria);
            kriteriaAdapter = new KriteriaAdapter(UbahKriteriaPreferensi.this,  kriterias);
            recyclerViewKriteria.setAdapter(kriteriaAdapter);
        }

    }

    private void checkBobotKriteria(){
        // buat tampungan untuk subkriteria
        dataKriteria = new ArrayList<>();
        //ambil subkriteria yang sudah diurutkan
        for(int i = 0; i < kriterias.size(); i++){
            double bobot = 0.0;
            double totalBobot = 0.0;
            // ambil bobot subkriteria yang sudah diurutkan
            for(int j = i + 1 ; j <= kriterias.size(); j++)
            {
                bobot = bobot + (1.0/ j); //menggunakan rumus rank order centroid
            }
            totalBobot = bobot / kriterias.size();
            String namaKriteria = kriterias.get(i);
            dataKriteria.add(new Kriteria(namaKriteria,totalBobot));
        }
    }
}