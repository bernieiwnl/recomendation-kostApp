package com.example.rekomendasikostapp.PENCARIAN;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rekomendasikostapp.ADAPTER.KostAdapter;
import com.example.rekomendasikostapp.ADAPTER.PencarianKostAdapter;
import com.example.rekomendasikostapp.CLASS.Kost;
import com.example.rekomendasikostapp.R;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.util.ArrayList;

public class PencarianKostActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText searchBar;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private KostAdapter kostAdapter;
    private ArrayList<Kost> kosts;
    private FloatingActionButton floatingActionButtonFilter;
    private RecyclerView recyclerViewKost;

    ArrayList<String> list_fasilitas_umum;
    ArrayList<String> list_fasilitas_kamar;
    ArrayList<String> list_akses_lingkungan;
    ArrayList<Kost> dataSearch;

    private String namaLokasi;
    private Double latitude;
    private Double longtitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pencarian_kost);

        //get object reference;
        searchBar = (EditText) findViewById(R.id.searchBar);
        floatingActionButtonFilter = (FloatingActionButton) findViewById(R.id.buttonFilter);
        recyclerViewKost = (RecyclerView) findViewById(R.id.recylerKost);

        //get intent data
        namaLokasi = getIntent().getStringExtra("namaLokasi");
        latitude = getIntent().getDoubleExtra("latitude", 0);
        longtitude = getIntent().getDoubleExtra("longtitude", 0);

        searchBar.setText(namaLokasi);
        searchBar.setOnClickListener(this);


        // set on click listener
        floatingActionButtonFilter.setOnClickListener(this);

        //get instance
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        //set new array
        kosts = new ArrayList<>();
        //set new array search
        dataSearch = new ArrayList<>();

        //set layout manager
        recyclerViewKost.setLayoutManager(new LinearLayoutManager(this));

        //get data firebase
        firebaseFirestore.collection("data_kost").whereEqualTo("isVerification", true).addSnapshotListener(PencarianKostActivity.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for(QueryDocumentSnapshot data : queryDocumentSnapshots){
                    Kost dataKost = data.toObject(Kost.class);

                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    long diff = timestamp.getTime() - dataKost.getUpdated_at().getTime();
                    long seconds = diff / 1000;
                    long minutes = seconds / 60;
                    long hours = minutes / 60;
                    long days = hours / 24;
                    Log.d("DAYS", "Day " + days);

                    if(days < 30){
                        if(distance(latitude, longtitude, dataKost.getLatitude(), dataKost.getLongtitude()) <= 1500.0){
                            kosts.add(dataKost);
                        }
                    }
                }
                KostAdapter kostAdapter = new KostAdapter(PencarianKostActivity.this, kosts, namaLokasi, latitude, longtitude);
                recyclerViewKost.setAdapter(kostAdapter);
            }
        });


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL);
        recyclerViewKost.addItemDecoration(dividerItemDecoration);
    }

    private double distance(double lat1, double long1, double lat2, double long2) {
        Location locationA = new Location("point A");

        locationA.setLatitude(lat1);
        locationA.setLongitude(long1);

        Location locationB = new Location("point B");

        locationB.setLatitude(lat2);
        locationB.setLongitude(long2);

        double distance = locationA.distanceTo(locationB);
        Log.d("DISTANCE",  "" + distance);
        return distance;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonFilter:
                startActivityForResult( new Intent(PencarianKostActivity.this, FilterKostActivity.class), 1);
                break;
            case R.id.searchBar:
                Intent intent = new Intent(PencarianKostActivity.this, PencarianLokasiKost.class);
                startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK)
        {
            final String hargaMin = data.getStringExtra("hargaMin");
            final String hargaMax = data.getStringExtra("hargaMax");
            String jenis_kost = data.getStringExtra("jenis_kost");
            String jenis_ukuran = data.getStringExtra("jenis_ukuran");

            list_fasilitas_kamar = new ArrayList<String>();
            list_fasilitas_umum = new ArrayList<String>();
            list_akses_lingkungan = new ArrayList<String>();

            list_akses_lingkungan = data.getStringArrayListExtra("list_akses_lingkungan");
            list_fasilitas_umum = data.getStringArrayListExtra("list_fasilitas_umum");
            list_fasilitas_kamar = data.getStringArrayListExtra("list_fasilitas_kamar");

            Log.d("Jenis Kost", "" + jenis_kost);

            ArrayList<Kost> dataFilter = new ArrayList<>();
            dataFilter.clear();
            for(Kost dataAlternatif : kosts){
                if(dataAlternatif.getHarga() <= Integer.parseInt(hargaMax)
                        && dataAlternatif.getHarga() >= Integer.parseInt(hargaMin)
                        && dataAlternatif.getJenis_kost().equals(jenis_kost)
                        && dataAlternatif.getUkuran_kamar().equals(jenis_ukuran)
                        && dataAlternatif.getFasilitas_umums().containsAll(list_fasilitas_umum)
                        && dataAlternatif.getFasilitas_kamars().containsAll(list_fasilitas_kamar)
                        && dataAlternatif.getAkses_lingkungans().containsAll(list_akses_lingkungan)
                ){
                    dataFilter.add(dataAlternatif);
                }
            }

            kostAdapter = new KostAdapter(PencarianKostActivity.this, dataFilter, namaLokasi, latitude, longtitude);
            recyclerViewKost.setAdapter(kostAdapter);
        }
    }
}
