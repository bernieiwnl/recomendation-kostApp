package com.example.rekomendasikostapp.PENGELOLA;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.rekomendasikostapp.ADAPTER.ImageSliderAdapter;
import com.example.rekomendasikostapp.CLASS.AksesLingkungan;
import com.example.rekomendasikostapp.CLASS.AlertDialogUpdate;
import com.example.rekomendasikostapp.CLASS.FasilitasKamar;
import com.example.rekomendasikostapp.CLASS.FasilitasUmum;
import com.example.rekomendasikostapp.CLASS.Kost;
import com.example.rekomendasikostapp.PENGELOLA.ADAPTER.DaftaFasilitasKamar;
import com.example.rekomendasikostapp.PENGELOLA.ADAPTER.DaftarFasilitasAkses;
import com.example.rekomendasikostapp.PENGELOLA.ADAPTER.DaftarFasilitasUmum;
import com.example.rekomendasikostapp.PENGELOLA.UPDATEKOST.UpdateDataKostActivity;
import com.example.rekomendasikostapp.PENGELOLA.UPDATEKOST.UpdateFasilitasKostActivity;
import com.example.rekomendasikostapp.PENGELOLA.UPDATEKOST.UpdateFotoKostActivity;
import com.example.rekomendasikostapp.PENGELOLA.UPDATEKOST.UpdateMarkerKostActivity;
import com.example.rekomendasikostapp.R;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class DetailKostPengelola extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, MapboxMap.OnMapClickListener {

    private TextView textAlamatKost;
    private TextView textTitleAlamatKost;
    private TextView textHarga;
    private TextView textUkuruanKamar;
    private TextView textJumlahKamar;
    private TextView textSisaKamar;
    private TextView textJenisKost;
    private TextView textUpdateTanggalKost;
    private TextView textKeterangan;

    private Double latitude;
    private Double longtitude;

    private MapView mapView;
    private MapboxMap map;
    private Marker kostMarker;
    private Point kostPoint;

    private NestedScrollView scrollView;
    private RecyclerView recyclerViewKamar;
    private RecyclerView recyclerViewUmum;
    private RecyclerView recyclerViewAkses;
    private SliderView sliderView;
    private ImageSliderAdapter imageSliderAdapter;

    private FloatingActionButton floatingActionButtonFoto;
    private FloatingActionButton floatingActionButtonData;
    private FloatingActionButton floatingActionButtonFasilitas;
    private FloatingActionButton floatingActionButtonMarker;

    private ArrayList<String> fotoKosts;

    private DaftarFasilitasUmum fasilitasUmumAdapter;
    private DaftaFasilitasKamar fasilitasKamarAdapter;
    private DaftarFasilitasAkses aksesLingkunganAdapter;

    private String idKost;

    //firestore and auth instance
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    //collection reference
    private CollectionReference fasilitasKamarRef = firebaseFirestore.collection("fasilitas_kamar");
    private CollectionReference fasilitasUmumRef = firebaseFirestore.collection("fasilitas_umum");
    private CollectionReference aksesLingkunganRef = firebaseFirestore.collection("akses_lingkungan");

    private ArrayList<FasilitasUmum> fasilitasUmums;
    private ArrayList<FasilitasKamar> fasilitasKamars;
    private ArrayList<AksesLingkungan> aksesLingkungans;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "sk.eyJ1IjoiYWd1c3RpYW5zY2F3IiwiYSI6ImNrNmoxd3JzOTA1ZnkzbXM0dXpuaG1qdzEifQ.AhyUN_JjsT70jDT4sbEyLA");
        setContentView(R.layout.activity_detail_kost_pengelola);

        //get object reference
        textAlamatKost = (TextView) findViewById(R.id.alamatKost);
        textTitleAlamatKost = (TextView) findViewById(R.id.titleAlamatKost);
        textHarga = (TextView) findViewById(R.id.harga);
        textUkuruanKamar = (TextView) findViewById(R.id.ukuranKamar);
        textJumlahKamar = (TextView) findViewById(R.id.jumlahKamar);
        textSisaKamar = (TextView) findViewById(R.id.sisaKamar);
        textJenisKost = (TextView) findViewById(R.id.jenisKost);
        textKeterangan = (TextView) findViewById(R.id.keteranganKost);
        textUpdateTanggalKost = (TextView) findViewById(R.id.update);

        scrollView = (NestedScrollView) findViewById(R.id.scrollView);
        recyclerViewUmum = (RecyclerView) findViewById(R.id.recyclerFasilitasUmum);
        recyclerViewKamar = (RecyclerView) findViewById(R.id.recyclerFasilitasKamar);
        recyclerViewAkses = (RecyclerView) findViewById(R.id.recyclerAksesLingkungan);

        floatingActionButtonData = (FloatingActionButton) findViewById(R.id.dataKost);
        floatingActionButtonFoto = (FloatingActionButton) findViewById(R.id.fotoKost);
        floatingActionButtonFasilitas = (FloatingActionButton) findViewById(R.id.fasilitasKost);
        floatingActionButtonMarker = (FloatingActionButton) findViewById(R.id.markerKost);

        // mapbox object reference
        mapView = findViewById(R.id.mapKost);

        //float action button setonclick listener
        floatingActionButtonMarker.setOnClickListener(this);
        floatingActionButtonFasilitas.setOnClickListener(this);
        floatingActionButtonFoto.setOnClickListener(this);
        floatingActionButtonData.setOnClickListener(this);

        //slider
        sliderView = (SliderView) findViewById(R.id.gambarKost);
        sliderView.startAutoCycle();
        sliderView.setIndicatorAnimation(IndicatorAnimations.FILL);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);


        //get intent idKost
        idKost = getIntent().getStringExtra("idKost");

        //get object reference
        recyclerViewUmum = (RecyclerView) findViewById(R.id.recyclerFasilitasUmum);
        recyclerViewKamar = (RecyclerView) findViewById(R.id.recyclerFasilitasKamar);
        recyclerViewAkses = (RecyclerView) findViewById(R.id.recyclerAksesLingkungan);

        //set grid layout manager
        recyclerViewUmum.setLayoutManager(new GridLayoutManager(this,3));
        recyclerViewKamar.setLayoutManager(new GridLayoutManager(this , 3));
        recyclerViewAkses.setLayoutManager(new GridLayoutManager(this, 3));

        //get new array
        fotoKosts = new ArrayList<String>();
        fasilitasUmums = new ArrayList<FasilitasUmum>();
        fasilitasKamars = new ArrayList<FasilitasKamar>();
        aksesLingkungans = new ArrayList<AksesLingkungan>();


        firebaseFirestore.collection("data_kost").document(idKost).addSnapshotListener(DetailKostPengelola.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){
                    //currency format
                    Locale localeID = new Locale("in", "ID");
                    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

                    Kost kost = documentSnapshot.toObject(Kost.class);
                    textTitleAlamatKost.setText(kost.getAlamat());
                    textAlamatKost.setText(kost.getAlamat() + ", " + kost.getKota() +", " + kost.getProvinsi() + " " + kost.getKode_pos());
                    textHarga.setText(formatRupiah.format((double)kost.getHarga()) + " / " + kost.getJenis_sewa());
                    textUkuruanKamar.setText(kost.getUkuran_kamar());
                    textJumlahKamar.setText("Ada " + kost.getJumlah_kamar() + " Kamar");
                    textSisaKamar.setText("Sisa " + kost.getSisa_kamar() + " Kamar");
                    textJenisKost.setText(kost.getJenis_kost());
                    textKeterangan.setText(kost.getKeterangan());

                    SimpleDateFormat format = new SimpleDateFormat("EEEE, dd MMMM yyyy");
                    textUpdateTanggalKost.setText( "Terakhir diupdate " + format.format(kost.getUpdated_at().getTime()));

                    latitude = kost.getLatitude();
                    longtitude = kost.getLongtitude();
                    fotoKosts.clear();
                    for(int i = 0; i < kost.getFoto_kost().size(); i++){
                        fotoKosts.add(kost.getFoto_kost().get(i));
                    }
                    imageSliderAdapter = new ImageSliderAdapter(getApplicationContext() , fotoKosts);
                    sliderView.setSliderAdapter(imageSliderAdapter);
                }
                else{
                    Log.d("TAG", "Query Not Found" + e);
                }
            }
        });

        //get query fasilitas
        getQueryAksesLingkungan();
        getQueryFasilitasKamar();
        getQueryFasilitasUmum();


        //set touch with scrollview
        mapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        scrollView.requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return mapView.onTouchEvent(event);
            }
        });

        //map callback
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        // Map is set up and the style has loaded. Now you can add data or make other map adjustments



                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
         switch (v.getId()){
             case R.id.fotoKost:
                 Intent a = new Intent(DetailKostPengelola.this, UpdateFotoKostActivity.class);
                 a.putExtra("idKost" , idKost);
                 a.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 startActivity(a);
                 break;
             case R.id.dataKost:
                 Intent b = new Intent(DetailKostPengelola.this, UpdateDataKostActivity.class);
                 b.putExtra("idKost" , idKost);
                 b.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 startActivity(b);
                 break;
             case R.id.fasilitasKost:
                 Intent c = new Intent(DetailKostPengelola.this, UpdateFasilitasKostActivity.class);
                 c.putExtra("idKost" , idKost);
                 c.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 startActivity(c);
                 break;
             case R.id.markerKost:
                 Intent d = new Intent(DetailKostPengelola.this, UpdateMarkerKostActivity.class);
                 d.putExtra("idKost" , idKost);
                 d.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 startActivity(d);
                 break;
         }
    }

    void showDialog(){
        final AlertDialogUpdate alertDialogUpdate = new AlertDialogUpdate(DetailKostPengelola.this , idKost);
        alertDialogUpdate.showUpdateDialog();
    }

    void getQueryFasilitasKamar(){

        //get query firestore
        fasilitasKamarRef.whereArrayContains("idkosts", idKost).addSnapshotListener(DetailKostPengelola.this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    fasilitasKamars.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        FasilitasKamar data = doc.toObject(FasilitasKamar.class);
                        fasilitasKamars.add(data);
                    }
                    //set adapter
                    fasilitasKamarAdapter = new DaftaFasilitasKamar(getApplicationContext(), fasilitasKamars);
                    recyclerViewKamar.setAdapter(fasilitasKamarAdapter);
                }
            }
        });

    }

    void getQueryFasilitasUmum() {
        //get query firestore
        fasilitasUmumRef.whereArrayContains("idkosts", idKost).addSnapshotListener(DetailKostPengelola.this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    fasilitasUmums.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        FasilitasUmum data = doc.toObject(FasilitasUmum.class);
                        fasilitasUmums.add(data);
                    }
                    //set adapter
                    fasilitasUmumAdapter = new DaftarFasilitasUmum(getApplicationContext(), fasilitasUmums);
                    recyclerViewUmum.setAdapter(fasilitasUmumAdapter);
                }
            }
        });
    }


    void getQueryAksesLingkungan(){
        //get query firestore
        aksesLingkunganRef.whereArrayContains("idkosts", idKost).addSnapshotListener(DetailKostPengelola.this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    aksesLingkungans.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        AksesLingkungan data = doc.toObject(AksesLingkungan.class);
                        aksesLingkungans.add(data);
                    }
                    //set adapter
                    aksesLingkunganAdapter = new DaftarFasilitasAkses(getApplicationContext(), aksesLingkungans);
                    recyclerViewAkses.setAdapter(aksesLingkunganAdapter);
                }
            }
        });
    }

    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        return false;
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        map = mapboxMap;
        map.addOnMapClickListener(this);
        CameraPosition position = new CameraPosition.Builder().target(new LatLng(latitude , longtitude)).zoom(13).tilt(20).build();
        kostMarker = map.addMarker(new MarkerOptions().position(new LatLng(latitude , longtitude)));
        map.animateCamera(CameraUpdateFactory.newCameraPosition(position), 4000);
    }
}
