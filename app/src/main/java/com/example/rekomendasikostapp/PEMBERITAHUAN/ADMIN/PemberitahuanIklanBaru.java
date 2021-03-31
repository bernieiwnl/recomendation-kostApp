package com.example.rekomendasikostapp.PEMBERITAHUAN.ADMIN;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rekomendasikostapp.ADAPTER.ImageSliderAdapter;
import com.example.rekomendasikostapp.CLASS.Kost;
import com.example.rekomendasikostapp.CLASS.Pemberitahuan;
import com.example.rekomendasikostapp.PENGELOLA.DetailKostPengelola;
import com.example.rekomendasikostapp.R;
import com.example.rekomendasikostapp.VERIFICATION.KostVerificationDetailActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PemberitahuanIklanBaru extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, MapboxMap.OnMapClickListener {

    private TextView alamatKost;
    private TextView sisaKamar;
    private TextView hargaKost;
    private TextView ukuranKamar;
    private TextView jumlahKamar;
    private TextView jenisKost;
    private ImageView imageKost;

    private TextView judulPemberitahuan;
    private TextView isiPemberitahuan;
    private TextView tanggalPemberitahuan;

    private Button btnInfoLanjut;

    private RelativeLayout sisa;
    private RelativeLayout jenis;

    private Double latitude;
    private Double longtitude;

    private MapView mapView;
    private MapboxMap map;
    private Marker kostMarker;
    private Point kostPoint;

    private NestedScrollView scrollView;

    private ArrayList<String> fotoKosts;

    private FirebaseFirestore firebaseFirestore;

    String idPemberitahuan;
    String idKost;
    String idUser;

    private SliderView sliderView;
    private ImageSliderAdapter imageSliderAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "sk.eyJ1IjoiYWd1c3RpYW5zY2F3IiwiYSI6ImNrNmoxd3JzOTA1ZnkzbXM0dXpuaG1qdzEifQ.AhyUN_JjsT70jDT4sbEyLA");
        setContentView(R.layout.activity_pemberitahuan_iklan_baru);

        //get object reference
        jumlahKamar = (TextView) findViewById(R.id.jumlahKamar);
        sisaKamar = (TextView) findViewById(R.id.sisaKamar);
        alamatKost = (TextView) findViewById(R.id.alamatKost);
        hargaKost = (TextView) findViewById(R.id.harga);
        jenisKost = (TextView) findViewById(R.id.jenisKost);
        ukuranKamar = (TextView) findViewById(R.id.ukuranKamar);
        imageKost = (ImageView) findViewById(R.id.imageKost);
        jenis = (RelativeLayout) findViewById(R.id.jenis);
        sisa = (RelativeLayout) findViewById(R.id.sisa);

        scrollView = (NestedScrollView) findViewById(R.id.scrollViewContent);

        btnInfoLanjut = (Button) findViewById(R.id.btnInfoLanjut);

        mapView = findViewById(R.id.mapKost);

        judulPemberitahuan = (TextView) findViewById(R.id.judulPemberitahuan);
        isiPemberitahuan = (TextView) findViewById(R.id.isiPemberitahuan);
        tanggalPemberitahuan = (TextView) findViewById(R.id.tanggalPemberitaahuan);

        //setonclicklistener
        btnInfoLanjut.setOnClickListener(this);

        //get intent
        idKost = getIntent().getStringExtra("idKost");
        idPemberitahuan = getIntent().getStringExtra("idPemberitahuan");
        idUser = getIntent().getStringExtra("idUser");

        //get new array
        fotoKosts = new ArrayList<String>();

        //get isntance firebase firestore
        firebaseFirestore = FirebaseFirestore.getInstance();

        //slider
        sliderView = (SliderView) findViewById(R.id.gambarKost);
        sliderView.startAutoCycle();
        sliderView.setIndicatorAnimation(IndicatorAnimations.FILL);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);

        //get data kost
        firebaseFirestore.collection("data_kost").document(idKost).addSnapshotListener(PemberitahuanIklanBaru.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){

                    //currency format
                    Locale localeID = new Locale("in", "ID");
                    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

                    Kost kost = documentSnapshot.toObject(Kost.class);
                    alamatKost.setText(kost.getAlamat() + ", " + kost.getKota() +", " + kost.getProvinsi() + " " + kost.getKode_pos());
                    hargaKost.setText(formatRupiah.format((double)kost.getHarga()) + " / " + kost.getJenis_sewa());
                    ukuranKamar.setText(kost.getUkuran_kamar());
                    jumlahKamar.setText("Ada " + kost.getJumlah_kamar() + " Kamar");
                    sisaKamar.setText("Sisa " + kost.getSisa_kamar() + " Kamar");
                    jenisKost.setText(kost.getJenis_kost());
                    latitude = kost.getLatitude();
                    longtitude = kost.getLongtitude();

                    if(kost.getSisa_kamar() < 3)
                    {
                        sisa.setBackgroundColor(Color.parseColor("#d32f2f"));
                    }

                    if(kost.getJenis_kost().equals("Perempuan"))
                    {
                        jenis.setBackgroundColor(Color.parseColor("#c2185b"));
                    }

                    for(int i = 0; i < kost.getFoto_kost().size(); i++){
                        if(i == 0)
                        {
                            Picasso.get().load(kost.getFoto_kost().get(i)).into(imageKost);
                        }
                    }

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

        firebaseFirestore.collection("pemberitahuans").document(idPemberitahuan).addSnapshotListener(PemberitahuanIklanBaru.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                Pemberitahuan data = documentSnapshot.toObject(Pemberitahuan.class);

                judulPemberitahuan.setText(data.getJudul());
                isiPemberitahuan.setText(data.getDeskripsi());

                //update tanggal kost
                SimpleDateFormat format = new SimpleDateFormat("EEEE, dd MMMM yyyy");
                tanggalPemberitahuan.setText(format.format(data.getTime().getTime()));

            }
        });

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

        setRead(idPemberitahuan);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnInfoLanjut:
                Intent intent = new Intent(PemberitahuanIklanBaru.this,  KostVerificationDetailActivity.class);
                intent.putExtra("idKost", idKost);
                intent.putExtra("idUser", idUser);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
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


    private void setRead(String idPemberitahuan){

        Map<String, Object> pemberitahuan = new HashMap<>();
        pemberitahuan.put("status", "read");
        firebaseFirestore.collection("pemberitahuans").document(idPemberitahuan).update(pemberitahuan);

    }
}
