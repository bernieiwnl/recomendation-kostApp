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
import android.widget.ImageView;

import com.example.rekomendasikostapp.ADAPTER.PrioritasFasilitasKamarAdapter;
import com.example.rekomendasikostapp.ADAPTER.PrioritasFasilitasUmumAdapter;
import com.example.rekomendasikostapp.CLASS.FasilitasKamar;
import com.example.rekomendasikostapp.CLASS.FasilitasUmum;
import com.example.rekomendasikostapp.CLASS.Preferensi;
import com.example.rekomendasikostapp.CLASS.SubKriteria;
import com.example.rekomendasikostapp.R;
import com.example.rekomendasikostapp.REKOMENDASI.PrioritasAksesLingkungan;
import com.example.rekomendasikostapp.REKOMENDASI.PrioritasFasilitasKamar;
import com.example.rekomendasikostapp.REKOMENDASI.TambahSubKriteriaFasilitasKamar;
import com.example.rekomendasikostapp.REKOMENDASI.TambahSubKriteriaFasilitasUmum;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

public class UbahKamarPreferensi extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerViewKriteria;
    private PrioritasFasilitasKamarAdapter prioritasFasilitasKamarAdapter;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private ImageView btnAdd;
    FloatingActionButton buttonSimpan;

    private ArrayList<FasilitasKamar> fasilitasKamars;
    private ArrayList<SubKriteria> subKriteriaFasilitasKamar;
    private ArrayList<String> fasilitasKamarID;

    static UbahKamarPreferensi ubahKamarPreferensi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_kamar_preferensi);

        //get activity reference
        ubahKamarPreferensi = this;

        // get object reference
        recyclerViewKriteria = (RecyclerView) findViewById(R.id.recyclerKriteria);
        btnAdd = (ImageView) findViewById(R.id.buttonAdd);
        buttonSimpan = (FloatingActionButton) findViewById(R.id.buttonSimpanPreferensi);

        //set onClick
        btnAdd.setOnClickListener(this);
        buttonSimpan.setOnClickListener(this);

        //get new array
        fasilitasKamars = new ArrayList<>();
        fasilitasKamarID = new ArrayList<>();

        //get instance firebstore
        firebaseFirestore = FirebaseFirestore.getInstance();


        //get instance firebstore
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseFirestore.collection("preferensi").document(firebaseAuth.getCurrentUser().getUid()).addSnapshotListener(UbahKamarPreferensi.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                final Preferensi dataPreferensiKamar = documentSnapshot.toObject(Preferensi.class);
                fasilitasKamars.clear();
                for(SubKriteria data : dataPreferensiKamar.getPreferensiKamar())
                {
                    fasilitasKamars.add(new FasilitasKamar(data.getNamaSubKriteria(), "", 0, data.getIdSubKriteria(), null));
                    fasilitasKamarID.add(data.getIdSubKriteria());
                }

                prioritasFasilitasKamarAdapter = new PrioritasFasilitasKamarAdapter(UbahKamarPreferensi.this, fasilitasKamars);
                recyclerViewKriteria.setAdapter(prioritasFasilitasKamarAdapter);
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

            Collections.swap(fasilitasKamars, fromPosition, toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            fasilitasKamars.remove(viewHolder.getAdapterPosition());
            fasilitasKamarID.remove(viewHolder.getAdapterPosition());
            recyclerViewKriteria.getAdapter().notifyDataSetChanged();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK) {
            FasilitasKamar dataKamar = (FasilitasKamar) data.getSerializableExtra("fasilitas");
            fasilitasKamars.add(dataKamar);
            String dataa = data.getStringExtra("fasilitasID");
            fasilitasKamarID.add(dataa);
            prioritasFasilitasKamarAdapter = new PrioritasFasilitasKamarAdapter(UbahKamarPreferensi.this, fasilitasKamars);
            recyclerViewKriteria.setAdapter(prioritasFasilitasKamarAdapter);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonAdd:
                Intent i = new Intent(getApplicationContext(), TambahSubKriteriaFasilitasKamar.class);
                i.putExtra("fasilitasKamar", fasilitasKamarID);
                startActivityForResult(i, 1);
                break;
            case R.id.buttonSimpanPreferensi:
                simpanPreferensiFasilitasKamar();
                break;
        }
    }

    private void simpanPreferensiFasilitasKamar(){
        //get instance firebase firestore and auth

        checkBobotSubKriteria();

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseFirestore.collection("preferensi").document(firebaseAuth.getCurrentUser().getUid()).update("preferensiKamar", subKriteriaFasilitasKamar).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                finish();
            }
        });
    }

    private void checkBobotSubKriteria(){
        // buat tampungan untuk subkriteria
        subKriteriaFasilitasKamar = new ArrayList<>();
        //ambil subkriteria yang sudah diurutkan
        for(int i = 0; i < fasilitasKamars.size(); i++){
            double bobot = 0.0;
            double totalBobot = 0.0;
            // ambil bobot subkriteria yang sudah diurutkan
            for(int j = i + 1 ; j <= fasilitasKamars.size(); j++)
            {
                bobot = bobot + (1.0/ j); //menggunakan rumus rank order centroid
            }
            totalBobot = bobot / fasilitasKamars.size();
            subKriteriaFasilitasKamar.add(new SubKriteria(fasilitasKamars.get(i).getIdfasilitas(), fasilitasKamars.get(i).getNama_fasilitas(), totalBobot));
        }
    }

    public static UbahKamarPreferensi getInstance(){
        return   ubahKamarPreferensi;
    }
}