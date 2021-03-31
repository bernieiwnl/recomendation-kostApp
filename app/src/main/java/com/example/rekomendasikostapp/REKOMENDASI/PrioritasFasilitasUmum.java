package com.example.rekomendasikostapp.REKOMENDASI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rekomendasikostapp.ADAPTER.PrioritasFasilitasUmumAdapter;
import com.example.rekomendasikostapp.CLASS.Alternatif;
import com.example.rekomendasikostapp.CLASS.FasilitasUmum;
import com.example.rekomendasikostapp.CLASS.Kriteria;
import com.example.rekomendasikostapp.CLASS.SubKriteria;
import com.example.rekomendasikostapp.PREFERENSI.UbahUmumPreferensi;
import com.example.rekomendasikostapp.R;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

public class PrioritasFasilitasUmum extends AppCompatActivity  implements View.OnClickListener {

    private RecyclerView recyclerViewKriteria;
    private PrioritasFasilitasUmumAdapter prioritasFasilitasUmumAdapter;
    private ArrayList<FasilitasUmum> fasilitasUmums;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private FloatingActionButton buttonLanjutkan;

    private ImageView btnAdd;

    private ArrayList<SubKriteria> subKriteriaFasilitasUmum;
    private ArrayList<String> fasilitasUmumID;

    static PrioritasFasilitasUmum prioritasFasilitasUmum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prioritas_fasilitas_umum);

        //get activity reference
        prioritasFasilitasUmum = this;

        // get object reference
        recyclerViewKriteria = (RecyclerView) findViewById(R.id.recyclerKriteria);
        buttonLanjutkan = (FloatingActionButton) findViewById(R.id.buttonLanjutkan);
        btnAdd = (ImageView) findViewById(R.id.buttonAdd);

        //set onClick
        buttonLanjutkan.setOnClickListener(this);
        btnAdd.setOnClickListener(this);

        //get new array
        fasilitasUmums = new ArrayList<>();
        fasilitasUmumID = new ArrayList<>();

        //get instance firebstore
        firebaseFirestore = FirebaseFirestore.getInstance();


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

            Collections.swap(fasilitasUmums, fromPosition, toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            fasilitasUmums.remove(viewHolder.getAdapterPosition());
            fasilitasUmumID.remove(viewHolder.getAdapterPosition());
            recyclerViewKriteria.getAdapter().notifyDataSetChanged();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonLanjutkan:
                simpanPrefrerensiFasilitasUmum();
                break;
            case R.id.buttonAdd:
                Intent i = new Intent(getApplicationContext(), TambahSubKriteriaFasilitasUmum.class);
                i.putExtra("fasilitasUmum", fasilitasUmumID);
                startActivityForResult(i, 1);
                break;
        }
    }

    private void simpanPrefrerensiFasilitasUmum(){

        checkBobotSubKriteria();

        //get instance firebase firestore and auth
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseFirestore.collection("preferensi").document(firebaseAuth.getCurrentUser().getUid()).update("preferensiUmum", subKriteriaFasilitasUmum).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent i = new Intent(PrioritasFasilitasUmum.this, PrioritasFasilitasKamar.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK) {
            FasilitasUmum dataUmum = (FasilitasUmum) data.getSerializableExtra("fasilitas");
            String dataa = data.getStringExtra("fasilitasID");
            fasilitasUmums.add(dataUmum);
            fasilitasUmumID.add(dataa);
            prioritasFasilitasUmumAdapter = new PrioritasFasilitasUmumAdapter(PrioritasFasilitasUmum.this, fasilitasUmums);
            recyclerViewKriteria.setAdapter(prioritasFasilitasUmumAdapter);
        }

    }


    private void checkBobotSubKriteria(){
        // buat tampungan untuk subkriteria
        subKriteriaFasilitasUmum = new ArrayList<>();
        //ambil subkriteria yang sudah diurutkan
        for(int i = 0; i < fasilitasUmums.size(); i++){
            double bobot = 0.0;
            double totalBobot = 0.0;
            // ambil bobot subkriteria yang sudah diurutkan
            for(int j = i + 1 ; j <= fasilitasUmums.size(); j++)
            {
                bobot = bobot + (1.0/ j); //menggunakan rumus rank order centroid
            }
            totalBobot = bobot / fasilitasUmums.size();
            subKriteriaFasilitasUmum.add(new SubKriteria(fasilitasUmums.get(i).getIdfasilitas(), fasilitasUmums.get(i).getNama_fasilitas(), totalBobot));
        }
    }

    public static PrioritasFasilitasUmum getInstance(){
        return   prioritasFasilitasUmum;
    }

}
