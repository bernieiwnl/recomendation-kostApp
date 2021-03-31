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

import com.example.rekomendasikostapp.ADAPTER.PrioritasAksesLingkunganAdapter;
import com.example.rekomendasikostapp.CLASS.AksesLingkungan;
import com.example.rekomendasikostapp.CLASS.Kriteria;
import com.example.rekomendasikostapp.CLASS.SubKriteria;
import com.example.rekomendasikostapp.HomeActivity;
import com.example.rekomendasikostapp.LOCATION.SimpanLokasi;
import com.example.rekomendasikostapp.PREFERENSI.UbahAksesPreferensi;
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

public class PrioritasAksesLingkungan extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerViewKriteria;
    private PrioritasAksesLingkunganAdapter prioritasAksesLingkunganAdapter;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FloatingActionButton buttonLanjustkan;
    private ImageView btnAdd;

    private ArrayList<AksesLingkungan> aksesLingkungans;
    private ArrayList<SubKriteria> subKriteriaAksesLingkungan;
    private ArrayList<String> aksesLingkunganID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prioritas_akses_lingkungan);
        // get object reference
        recyclerViewKriteria = (RecyclerView) findViewById(R.id.recyclerKriteria);
        buttonLanjustkan = (FloatingActionButton) findViewById(R.id.buttonLanjutkan);
        btnAdd = (ImageView) findViewById(R.id.buttonAdd);
        //set onclick
        buttonLanjustkan.setOnClickListener(this);
        btnAdd.setOnClickListener(this);

        //get new array
        aksesLingkungans = new ArrayList<>();
        aksesLingkunganID = new ArrayList<>();

        //get instance firebstore
        firebaseFirestore = FirebaseFirestore.getInstance();

        recyclerViewKriteria.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerViewKriteria.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerViewKriteria);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END,  ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();

            Collections.swap(aksesLingkungans, fromPosition, toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            aksesLingkungans.remove(viewHolder.getAdapterPosition());
            aksesLingkunganID.remove(viewHolder.getAdapterPosition());
            recyclerViewKriteria.getAdapter().notifyDataSetChanged();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            AksesLingkungan dataAkses = (AksesLingkungan) data.getSerializableExtra("aksesLingkungan");
            aksesLingkungans.add(dataAkses);
            String dataa = data.getStringExtra("fasilitasID");
            aksesLingkunganID.add(dataa);
            prioritasAksesLingkunganAdapter = new PrioritasAksesLingkunganAdapter(PrioritasAksesLingkungan.this, aksesLingkungans);
            recyclerViewKriteria.setAdapter(prioritasAksesLingkunganAdapter);
        }
    }

    private void checkBobotSubKriteria(){
        // buat tampungan untuk subkriteria
        subKriteriaAksesLingkungan = new ArrayList<>();
        //ambil subkriteria yang sudah diurutkan
        for(int i = 0; i < aksesLingkungans.size(); i++){

            double bobot = 0.0;
            double totalBobot = 0.0;

            // ambil bobot subkriteria yang sudah diurutkan
            for(int j = i + 1 ; j <= aksesLingkungans.size(); j++)
            {
                bobot = bobot + (1.0/ j); //menggunakan rumus rank order centroid
            }
            totalBobot = bobot / aksesLingkungans.size();

            subKriteriaAksesLingkungan.add(new SubKriteria(aksesLingkungans.get(i).getIdfasilitas(), aksesLingkungans.get(i).getNama_akses(), totalBobot));
        }
    }

    private void simpanPreferensiAksesLingkungan(){
        //get instance firebase firestore and auth

        checkBobotSubKriteria();

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();


        firebaseFirestore.collection("preferensi").document(firebaseAuth.getCurrentUser().getUid()).update("preferensiAkses", subKriteriaAksesLingkungan).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                HomeActivity.getInstance().finish();
                SimpanLokasi.getInstance().finish();
                PemilihanKriteriaActivity.getInstance().finish();
                PrioritasFasilitasUmum.getInstance().finish();
                PrioritasFasilitasKamar.getInstance().finish();

                Intent i = new Intent(PrioritasAksesLingkungan.this, HomeActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonAdd:
                Intent i = new Intent(getApplicationContext(), TambahSubKriteriaFasilitasAkses.class);
                i.putExtra("aksesLingkungans", aksesLingkunganID);
                startActivityForResult(i, 1);
                break;
            case R.id.buttonLanjutkan:
                simpanPreferensiAksesLingkungan();
                break;
        }
    }
}
