package com.example.rekomendasikostapp.VERIFICATION;

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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rekomendasikostapp.ADAPTER.ImageSliderAdapter;
import com.example.rekomendasikostapp.CLASS.AksesLingkungan;
import com.example.rekomendasikostapp.CLASS.FasilitasKamar;
import com.example.rekomendasikostapp.CLASS.FasilitasUmum;
import com.example.rekomendasikostapp.CLASS.Kost;
import com.example.rekomendasikostapp.FRAGMENT.APIService;
import com.example.rekomendasikostapp.MAINKOST.DetailKostPenggunaActivity;
import com.example.rekomendasikostapp.MESSAGING.MessageActivity;
import com.example.rekomendasikostapp.NOTIFICATIONS.Client;
import com.example.rekomendasikostapp.NOTIFICATIONS.Data;
import com.example.rekomendasikostapp.NOTIFICATIONS.MyResponse;
import com.example.rekomendasikostapp.NOTIFICATIONS.Sender;
import com.example.rekomendasikostapp.NOTIFICATIONS.Token;
import com.example.rekomendasikostapp.PENGELOLA.ADAPTER.DaftaFasilitasKamar;
import com.example.rekomendasikostapp.PENGELOLA.ADAPTER.DaftarFasilitasAkses;
import com.example.rekomendasikostapp.PENGELOLA.ADAPTER.DaftarFasilitasUmum;
import com.example.rekomendasikostapp.R;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KostVerificationDetailActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, MapboxMap.OnMapClickListener {

    private TextView textAlamatKost;
    private TextView textTitleAlamatKost;
    private TextView textHarga;
    private TextView textUkuruanKamar;
    private TextView textJumlahKamar;
    private TextView textSisaKamar;
    private TextView textJenisKost;


    private Button verifikasi;
    private Button tolakVerifikasi;

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

    private DaftarFasilitasUmum fasilitasUmumAdapter;
    private DaftaFasilitasKamar fasilitasKamarAdapter;
    private DaftarFasilitasAkses aksesLingkunganAdapter;

    private String idKost;
    private String idUser;


    //firestore and auth instance
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;


    //collection reference
    private CollectionReference fasilitasKamarRef = firebaseFirestore.collection("fasilitas_kamar");
    private CollectionReference fasilitasUmumRef = firebaseFirestore.collection("fasilitas_umum");
    private CollectionReference aksesLingkunganRef = firebaseFirestore.collection("akses_lingkungan");

    private ArrayList<FasilitasUmum> fasilitasUmums;
    private ArrayList<FasilitasKamar> fasilitasKamars;
    private ArrayList<AksesLingkungan> aksesLingkungans;

    APIService apiService;

    private boolean notify = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "sk.eyJ1IjoiYWd1c3RpYW5zY2F3IiwiYSI6ImNrNmoxd3JzOTA1ZnkzbXM0dXpuaG1qdzEifQ.AhyUN_JjsT70jDT4sbEyLA");
        setContentView(R.layout.activity_kost_verification_detail);

        //api service notification
        apiService = Client.getRetrofit("https://fcm.googleapis.com/").create(APIService.class);

        //get object reference
        textAlamatKost = (TextView) findViewById(R.id.alamatKost);
        textTitleAlamatKost = (TextView) findViewById(R.id.titleAlamatKost);
        textHarga = (TextView) findViewById(R.id.harga);
        textUkuruanKamar = (TextView) findViewById(R.id.ukuranKamar);
        textJumlahKamar = (TextView) findViewById(R.id.jumlahKamar);
        textSisaKamar = (TextView) findViewById(R.id.sisaKamar);
        textJenisKost = (TextView) findViewById(R.id.jenisKost);



        verifikasi = (Button) findViewById(R.id.btnVerifikasi);
        tolakVerifikasi = (Button) findViewById(R.id.btnBatalkanVerifikasi);

        scrollView = (NestedScrollView) findViewById(R.id.scrollView);

        recyclerViewUmum = (RecyclerView) findViewById(R.id.recyclerFasilitasUmum);
        recyclerViewKamar = (RecyclerView) findViewById(R.id.recyclerFasilitasKamar);
        recyclerViewAkses = (RecyclerView) findViewById(R.id.recyclerAksesLingkungan);

        // mapbox object reference
        mapView = findViewById(R.id.mapKost);

        //set onclick listener
        verifikasi.setOnClickListener(this);
        tolakVerifikasi.setOnClickListener(this);


        //slider
        sliderView = (SliderView) findViewById(R.id.gambarKost);
        sliderView.startAutoCycle();
        sliderView.setIndicatorAnimation(IndicatorAnimations.FILL);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);


        //get intent idKost
        idKost = getIntent().getStringExtra("idKost");
        idUser = getIntent().getStringExtra("idUser");

        //firebase auth inscante
        firebaseAuth = FirebaseAuth.getInstance();

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


        // get data collection from firestore
        firebaseFirestore.collection("data_kost").document(idKost).addSnapshotListener(KostVerificationDetailActivity.this, new EventListener<DocumentSnapshot>() {
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

                    idUser = kost.getIdUser();
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

    void getQueryFasilitasKamar(){
        //get query firestore
        fasilitasKamarRef.whereArrayContains("idkosts", idKost).addSnapshotListener(KostVerificationDetailActivity.this,new EventListener<QuerySnapshot>() {
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
        fasilitasUmumRef.whereArrayContains("idkosts", idKost).addSnapshotListener(KostVerificationDetailActivity.this,new EventListener<QuerySnapshot>() {
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
        aksesLingkunganRef.whereArrayContains("idkosts", idKost).addSnapshotListener(KostVerificationDetailActivity.this,new EventListener<QuerySnapshot>() {
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
            case R.id.btnVerifikasi:
                //your code here
                notify = true;
                Map<String, Object> kosts = new HashMap<>();
                kosts.put("isVerification", true);
                kosts.put("status", "Terverifikasi");
                firebaseFirestore.collection("data_kost").document(idKost).update(kosts).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(notify){
                            sendPemberitahuan("Verifikasi Kost");
                        }
                        notify = false;
                        finish();
                    }
                });
                break;
            case R.id.btnBatalkanVerifikasi:
                //your code here
                firebaseFirestore.collection("data_kost").document(idKost).update("isVerification", false).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        finish();
                    }
                });
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

    private void sendPemberitahuan(String jenis)
    {
        Map<String, Object> pemberitahuan = new HashMap<>();
        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        pemberitahuan.put("idUser", idUser);
        pemberitahuan.put("idKost", idKost);
        pemberitahuan.put("idPemesanan", "");
        pemberitahuan.put("idSender", firebaseAuth.getCurrentUser().getUid());
        pemberitahuan.put("jenis_pemberitahuan", jenis);
        pemberitahuan.put("judul", "Kost Anda Sudah Terverifikasi");
        pemberitahuan.put("deskripsi", "Selamat, kost milikmu sudah diverifikasi oleh admin. Sekarang kost milikmu sudah otomatis diiklankan dan dapat dipesan oleh pengguna.");
        pemberitahuan.put("status", "unread");
        pemberitahuan.put("time", timestamp);

        firebaseFirestore.collection("pemberitahuans").add(pemberitahuan).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                firebaseFirestore.collection("pemberitahuans").document(documentReference.getId()).update("idPemberitahuan", documentReference.getId());
                String m  = "Selamat, kost milikmu sudah diverifikasi oleh admin. Sekarang kost milikmu sudah otomatis diiklankan dan dapat dipesan oleh pengguna.";
//                sendNotification("Kost Anda Sudah Terverifikasi" , m , R.mipmap.ic_launcher, firebaseAuth.getCurrentUser().getUid(), idUser , idKost, documentReference.getId(), "" , "" , "Verifikasi Kost");
            }
        });
    }

    private void sendNotification(final String title, final String body, final Integer icon, final String sender, final String receiver, final String idKost, final String idPemberitahuan, final String idPemesanan, final String idKeluhan, final String jenis_notifikasi)
    {
        FirebaseFirestore token = FirebaseFirestore.getInstance();

        Query query = token.collection("token").whereEqualTo("userID", receiver);
        query.addSnapshotListener(KostVerificationDetailActivity.this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for(QueryDocumentSnapshot dataQuery : queryDocumentSnapshots){
                    Token tokenData = dataQuery.toObject(Token.class);
                    Data data = new Data();
                    data.setTitle(title);
                    data.setBody(body);
                    data.setIcon(icon);
                    data.setSender(sender);
                    data.setReceiver(receiver);
                    data.setIdKost(idKost);
                    data.setIdPemberitahuan(idPemberitahuan);
                    data.setIdPemesanan(idPemesanan);
                    data.setIdKeluhan(idKeluhan);
                    data.setJenis_notifikasi(jenis_notifikasi);
                    Sender dataSend = new Sender(data, tokenData.getToken());
                    apiService.sendNotification(dataSend).enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                            if(response.code()==200)
                            {
                                if(response.body().success != 1)
                                {
                                    Toast.makeText(KostVerificationDetailActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }
}
