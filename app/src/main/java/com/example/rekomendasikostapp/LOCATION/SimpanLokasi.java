package com.example.rekomendasikostapp.LOCATION;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.rekomendasikostapp.CLASS.Preferensi;
import com.example.rekomendasikostapp.R;
import com.example.rekomendasikostapp.REKOMENDASI.PemilihanKriteriaActivity;
import com.example.rekomendasikostapp.REKOMENDASI.PrioritasFasilitasKamar;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SimpanLokasi extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, MapboxMap.OnMapClickListener {

    private MapView mapView;
    private MapboxMap map;
    private Marker kostMarker;
    private Point kostPoint;
    private Double latitude, longitude;

    private EditText editNamaLokasi;
    private Button btnCariLokasi;
    private Button btnSimpanLokasi;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    static SimpanLokasi simpanLokasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "sk.eyJ1IjoiYWd1c3RpYW5zY2F3IiwiYSI6ImNrNmoxd3JzOTA1ZnkzbXM0dXpuaG1qdzEifQ.AhyUN_JjsT70jDT4sbEyLA");
        setContentView(R.layout.activity_simpan_lokasi);

        simpanLokasi = this;

        //object reference
        editNamaLokasi = (EditText) findViewById(R.id.namaLokasi);
        btnCariLokasi = (Button) findViewById(R.id.btnCariLokasi);
        btnSimpanLokasi = (Button) findViewById(R.id.btnSimpanLokasi);


        latitude = 0.0;
        longitude = 0.0;

        editNamaLokasi.addTextChangedListener(alamatTextWatcher);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        btnCariLokasi.setOnClickListener(this);
        btnSimpanLokasi.setOnClickListener(this);

        // mapbox object reference
        mapView = findViewById(R.id.mapViewMarker);
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
            case R.id.btnSimpanLokasi:
                simpanLokasiPencarian();
                break;
            case R.id.btnCariLokasi:
                getLocation();
                break;
        }
    }

    private void simpanLokasiPencarian(){

        Map<String, Object> location = new HashMap<>();

        location.put("namaLokasi", editNamaLokasi.getText().toString().trim());
        location.put("latitude", latitude);
        location.put("longtitude", longitude);



        firebaseFirestore.collection("preferensi").document(firebaseAuth.getCurrentUser().getUid()).update(location).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent i = new Intent(SimpanLokasi.this, PemilihanKriteriaActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        if(kostMarker != null){
            map.removeMarker(kostMarker);
        }

        kostMarker = map.addMarker(new MarkerOptions()
                .position(point));

        kostPoint = Point.fromLngLat(point.getLongitude(),point.getLongitude());
        latitude = point.getLatitude();
        longitude = point.getLongitude();
        return true;
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        map = mapboxMap;
        map.addOnMapClickListener(this);
    }

    private void getLocation(){

        String alamat = editNamaLokasi.getText().toString().trim();

        String alamatLengkap = alamat ;

        if(!alamat.isEmpty()){
            Geocoder geocoder = new Geocoder(SimpanLokasi.this, Locale.getDefault());
            try {
                List<Address> addressList = geocoder.getFromLocationName(alamatLengkap, 1);

                if(addressList.size() > 0){
                    Address address = addressList.get(0);

                    latitude = address.getLatitude();
                    longitude = address.getLongitude();

                    if(kostMarker != null){
                        map.removeMarker(kostMarker);
                    }

                    CameraPosition position = new CameraPosition.Builder().target(new LatLng(latitude , longitude)).zoom(15).tilt(20).build();
                    kostMarker = map.addMarker(new MarkerOptions().position(new LatLng(latitude , longitude)));
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(position), 4000);

                    Log.d("Latitude" , "" + latitude);
                    Log.d( "Longtidue", "" + longitude);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private TextWatcher alamatTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String alamat = editNamaLokasi.getText().toString().trim();
            if(!alamat.isEmpty()){
                btnCariLokasi.setVisibility(View.VISIBLE);
            }
        }
    };

    public static SimpanLokasi getInstance(){
        return   simpanLokasi;
    }
}