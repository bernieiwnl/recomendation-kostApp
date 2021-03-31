package com.example.rekomendasikostapp.REKOMENDASI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rekomendasikostapp.ADAPTER.KriteriaAdapter;
import com.example.rekomendasikostapp.ADAPTER.PrioritasFasilitasUmumAdapter;
import com.example.rekomendasikostapp.CLASS.FasilitasUmum;
import com.example.rekomendasikostapp.CLASS.Kriteria;
import com.example.rekomendasikostapp.R;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class PemilihanKriteriaActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerViewKriteria;
    private ArrayList<String> kriterias;
    private KriteriaAdapter kriteriaAdapter;
    private com.github.clans.fab.FloatingActionButton floatingActionButtonNext;
    private ImageView btnAdd;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private ArrayList<Kriteria> dataKriteria;

    static PemilihanKriteriaActivity pemilihanKriteriaActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemilihan_kriteria);

        //get activity reference
        pemilihanKriteriaActivity = this;

        //get object reference
        recyclerViewKriteria = (RecyclerView) findViewById(R.id.recyclerKriteria);
        floatingActionButtonNext = (FloatingActionButton) findViewById(R.id.floatingActionButtonNext);
        btnAdd = (ImageView) findViewById(R.id.buttonAdd);

        //set onclick listener
        floatingActionButtonNext.setOnClickListener(this);
        btnAdd.setOnClickListener(this);

        kriterias = new ArrayList<>();


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
            case R.id.floatingActionButtonNext:
                simpanPreferensiKriteria();
                break;
            case R.id.buttonAdd:
                Intent i = new Intent(PemilihanKriteriaActivity.this , TambahKriteriaKost.class);
                i.putExtra("dataKriteria", kriterias);
                startActivityForResult(i, 1);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK) {
            String dataKriteria = (String) data.getSerializableExtra("fasilitas");
            kriterias.add(dataKriteria);
            kriteriaAdapter = new KriteriaAdapter(PemilihanKriteriaActivity.this,  kriterias);
            recyclerViewKriteria.setAdapter(kriteriaAdapter);
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
                Intent i = new Intent(PemilihanKriteriaActivity.this, PrioritasFasilitasUmum.class);
                startActivity(i);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "" + e, Toast.LENGTH_SHORT ).show();
            }
        });
    }

    public static PemilihanKriteriaActivity getInstance(){
        return  pemilihanKriteriaActivity;
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
