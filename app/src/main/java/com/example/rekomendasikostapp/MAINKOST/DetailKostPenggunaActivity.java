package com.example.rekomendasikostapp.MAINKOST;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.rekomendasikostapp.ADAPTER.ImageSliderAdapter;
import com.example.rekomendasikostapp.CLASS.AksesLingkungan;
import com.example.rekomendasikostapp.CLASS.FasilitasKamar;
import com.example.rekomendasikostapp.CLASS.FasilitasUmum;
import com.example.rekomendasikostapp.CLASS.Kost;
import com.example.rekomendasikostapp.CLASS.Preferensi;
import com.example.rekomendasikostapp.CLASS.Users;
import com.example.rekomendasikostapp.MESSAGING.MessageActivity;
import com.example.rekomendasikostapp.PEMESANAN.PemesananActivity;
import com.example.rekomendasikostapp.PENGELOLA.ADAPTER.DaftaFasilitasKamar;
import com.example.rekomendasikostapp.PENGELOLA.ADAPTER.DaftarFasilitasAkses;
import com.example.rekomendasikostapp.PENGELOLA.ADAPTER.DaftarFasilitasUmum;
import com.example.rekomendasikostapp.R;
import com.example.rekomendasikostapp.REPORT.FormReportKost;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
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

import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DetailKostPenggunaActivity extends AppCompatActivity  implements View.OnClickListener, OnMapReadyCallback, MapboxMap.OnMapClickListener {

    private TextView textAlamatKost;
    private TextView textTitleAlamatKost;
    private TextView textHarga;
    private TextView textUkuruanKamar;
    private TextView textJumlahKamar;
    private TextView textSisaKamar;
    private TextView textJenisKost;
    private TextView textTanggalUpdateKost;
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

    private ArrayList<String> fotoKosts;

    private FloatingActionButton pesanKost;
    private FloatingActionButton chatPengelola;
    private FloatingActionButton laporkanKost;
    private FloatingActionButton suspendKost;
    private FloatingActionMenu menu;

    private DaftarFasilitasUmum fasilitasUmumAdapter;
    private DaftaFasilitasKamar fasilitasKamarAdapter;
    private DaftarFasilitasAkses aksesLingkunganAdapter;

    private String idKost;
    private String idUser;

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

    private ArrayList<FasilitasUmum> dataFumum;
    private ArrayList<FasilitasKamar> dataFkamar;
    private ArrayList<AksesLingkungan> dataFakses;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "sk.eyJ1IjoiYWd1c3RpYW5zY2F3IiwiYSI6ImNrNmoxd3JzOTA1ZnkzbXM0dXpuaG1qdzEifQ.AhyUN_JjsT70jDT4sbEyLA");
        setContentView(R.layout.activity_detail_kost_pengguna);
        //get object reference
        textAlamatKost = (TextView) findViewById(R.id.alamatKost);
        textTitleAlamatKost = (TextView) findViewById(R.id.titleAlamatKost);
        textHarga = (TextView) findViewById(R.id.harga);
        textUkuruanKamar = (TextView) findViewById(R.id.ukuranKamar);
        textJumlahKamar = (TextView) findViewById(R.id.jumlahKamar);
        textSisaKamar = (TextView) findViewById(R.id.sisaKamar);
        textJenisKost = (TextView) findViewById(R.id.jenisKost);
        textTanggalUpdateKost = (TextView) findViewById(R.id.update);
        pesanKost = (FloatingActionButton) findViewById(R.id.pesanKost);
        chatPengelola = (FloatingActionButton) findViewById(R.id.chatPengelola);
        laporkanKost = (FloatingActionButton) findViewById(R.id.laporkanKost);
        suspendKost = (FloatingActionButton) findViewById(R.id.suspendKost);
        menu = (FloatingActionMenu) findViewById(R.id.menu);
        textKeterangan = (TextView)findViewById(R.id.keteranganKost);

        // set onClick
        pesanKost.setOnClickListener(this);
        chatPengelola.setOnClickListener(this);
        laporkanKost.setOnClickListener(this);
        suspendKost.setOnClickListener(this);

        scrollView = (NestedScrollView) findViewById(R.id.scrollView);

        recyclerViewUmum = (RecyclerView) findViewById(R.id.recyclerFasilitasUmum);
        recyclerViewKamar = (RecyclerView) findViewById(R.id.recyclerFasilitasKamar);
        recyclerViewAkses = (RecyclerView) findViewById(R.id.recyclerAksesLingkungan);

        // mapbox object reference
        mapView = findViewById(R.id.mapKost);

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

        dataFumum = new ArrayList<FasilitasUmum>();
        dataFkamar = new ArrayList<FasilitasKamar>();
        dataFakses = new ArrayList<AksesLingkungan>();

        //check user admin
        firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).addSnapshotListener(DetailKostPenggunaActivity.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                Users dataUser = documentSnapshot.toObject(Users.class);
                if(!dataUser.getAccount().equals("Admin")){
                    suspendKost.setVisibility(View.GONE);
                }
            }
        });

        // get data collection from firestore
        firebaseFirestore.collection("data_kost").document(idKost).addSnapshotListener(DetailKostPenggunaActivity.this, new EventListener<DocumentSnapshot>() {
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
                    //update tanggal kost
                    SimpleDateFormat format = new SimpleDateFormat("EEEE, dd MMMM yyyy");
                    textTanggalUpdateKost.setText( "Terakhir diupdate " + format.format(kost.getUpdated_at().getTime()));

                    idUser = kost.getIdUser();
                    latitude = kost.getLatitude();
                    longtitude = kost.getLongtitude();
                    fotoKosts.clear();
                    for(int i = 0; i < kost.getFoto_kost().size(); i++){
                        fotoKosts.add(kost.getFoto_kost().get(i));
                    }
                    imageSliderAdapter = new ImageSliderAdapter(getApplicationContext() , fotoKosts);
                    sliderView.setSliderAdapter(imageSliderAdapter);

                    if(kost.getIdUser().equals(firebaseAuth.getCurrentUser().getUid())){
                        menu.setVisibility(View.GONE);
                    }

                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    long diff = timestamp.getTime() - kost.getUpdated_at().getTime();
                    long seconds = diff / 1000;
                    long minutes = seconds / 60;
                    long hours = minutes / 60;
                    long days = hours / 24;
                    Log.d("DAYS", "Day " + days);


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

    void getQueryFasilitasKamar(){
        //get query firestore
        fasilitasKamarRef.whereArrayContains("idkosts", idKost).addSnapshotListener(DetailKostPenggunaActivity.this,new EventListener<QuerySnapshot>() {
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
        fasilitasUmumRef.whereArrayContains("idkosts", idKost).addSnapshotListener(DetailKostPenggunaActivity.this,new EventListener<QuerySnapshot>() {
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
        aksesLingkunganRef.whereArrayContains("idkosts", idKost).addSnapshotListener(DetailKostPenggunaActivity.this,new EventListener<QuerySnapshot>() {
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pesanKost:
                //your code here
                if(!firebaseAuth.getCurrentUser().getUid().equals(idUser)) {
                    Intent intenPesan = new Intent(getApplicationContext(), PemesananActivity.class);
                    intenPesan.putExtra("idKost", idKost);
                    intenPesan.putExtra("idUser", idUser);
                    intenPesan.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intenPesan);
                }
                break;
            case R.id.chatPengelola:
                //your code here
                if(!firebaseAuth.getCurrentUser().getUid().equals(idUser)){
                    Intent intent = new Intent(getApplicationContext(),  MessageActivity.class);
                    intent.putExtra("idUser", idUser);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                break;
            case R.id.laporkanKost:
                if(!firebaseAuth.getCurrentUser().getUid().equals(idUser)){
                    Intent intent = new Intent(getApplicationContext(),  FormReportKost.class);
                    intent.putExtra("idKost", idKost);
                    intent.putExtra("idPengelola", idUser);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            case R.id.suspendKost:
                suspendKost(idKost, idUser);
                finish();
                break;

        }
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

    private void suspendKost(final String idKost, final String idUser){
        Map<String, Object> kosts = new HashMap<>();
        kosts.put("status", "Suspended");
        kosts.put("isVerification", false);
        firebaseFirestore.collection("data_kost").document(idKost).update(kosts).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Map<String, Object> kirimPemberitahuan = new HashMap<>();
                final Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                kirimPemberitahuan.put("idUser", idUser);
                kirimPemberitahuan.put("idKost", idKost);
                kirimPemberitahuan.put("idSender", firebaseAuth.getCurrentUser().getUid());
                kirimPemberitahuan.put("jenis_pemberitahuan", "Peringatan Kost");
                kirimPemberitahuan.put("judul", "Kost kamu terkena peringatan");
                kirimPemberitahuan.put("deskripsi", "Kost kamu tidak akan diiklankan lagi sampai waktu yang ditentukan oleh admin, kamu bisa menghubungi admin dengan menekan tombol dibawah ini terima kasih");
                kirimPemberitahuan.put("status", "unread");
                kirimPemberitahuan.put("time", timestamp);

                firebaseFirestore.collection("pemberitahuans").add(kirimPemberitahuan).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        firebaseFirestore.collection("pemberitahuans").document(documentReference.getId()).update("idPemberitahuan", documentReference.getId());
                    }
                });

            }
        });


    }
}
