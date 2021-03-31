package com.example.rekomendasikostapp.PENGELOLA.UPDATEKOST;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.print.PrinterId;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.rekomendasikostapp.CLASS.Kost;
import com.example.rekomendasikostapp.PENGELOLA.DetailKostPengelola;
import com.example.rekomendasikostapp.R;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;

public class UpdateMarkerKostActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, MapboxMap.OnMapClickListener {


    private Double latitude;
    private Double longtitude;
    private FirebaseFirestore firebaseFirestore;
    private String idKost;
    private MapView mapView;
    private MapboxMap map;
    private Marker kostMarker;
    private Point kostPoint;
    private FloatingActionButton floatingActionButtonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "sk.eyJ1IjoiYWd1c3RpYW5zY2F3IiwiYSI6ImNrNmoxd3JzOTA1ZnkzbXM0dXpuaG1qdzEifQ.AhyUN_JjsT70jDT4sbEyLA");
        setContentView(R.layout.activity_update_marker_kost);

        //get string extra
        idKost = getIntent().getStringExtra("idKost");

        // mapbox object reference
        mapView = findViewById(R.id.mapViewMarker);

        //get object reference
        floatingActionButtonSave = findViewById(R.id.floatActionButtonSave);


        //set on click listerener
        floatingActionButtonSave.setOnClickListener(this);

        // firestore get instance
        firebaseFirestore = FirebaseFirestore.getInstance();




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
            case R.id.floatActionButtonSave:
                //your code here
                saveMarker(latitude, longtitude , idKost);
                break;
        }

    }

    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        if(kostMarker != null){
            map.removeMarker(kostMarker);
        }

        kostMarker = map.addMarker(new MarkerOptions()
                .position(point));

        kostPoint = Point.fromLngLat(point.getLatitude(),point.getLongitude());
        latitude = point.getLatitude();
        longtitude = point.getLongitude();
        return true;
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {

        map = mapboxMap;
        map.addOnMapClickListener(this);

        // get data from firestore
        firebaseFirestore.collection("data_kost").document(idKost).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Kost data = documentSnapshot.toObject(Kost.class);
                    CameraPosition position = new CameraPosition.Builder().target(new LatLng(data.getLatitude() , data.getLongtitude())).zoom(13).tilt(20).build();
                    kostMarker = map.addMarker(new MarkerOptions().position(new LatLng(data.getLatitude() , data.getLongtitude())));
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(position), 4000);

                }

            }
        });
    }


    void saveMarker(Double latitude , Double longtitude , final String idKost){

        Map<String, Object> marker = new HashMap<>();

        marker.put("latitude" , latitude);
        marker.put("longtitude" , longtitude);

        firebaseFirestore.collection("data_kost").document(idKost).update(marker).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                finish();
            }
        });

    }
}
