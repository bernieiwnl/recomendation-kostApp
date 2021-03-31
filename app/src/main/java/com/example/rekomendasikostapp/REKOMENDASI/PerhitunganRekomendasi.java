package com.example.rekomendasikostapp.REKOMENDASI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.agrawalsuneet.dotsloader.loaders.LazyLoader;
import com.example.rekomendasikostapp.CLASS.Alternatif;
import com.example.rekomendasikostapp.CLASS.Kost;
import com.example.rekomendasikostapp.CLASS.Kriteria;
import com.example.rekomendasikostapp.CLASS.RankOrderCentroid;
import com.example.rekomendasikostapp.CLASS.SubKriteria;
import com.example.rekomendasikostapp.CLASS.Users;
import com.example.rekomendasikostapp.R;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PerhitunganRekomendasi extends AppCompatActivity {


    private ArrayList<Kriteria> dataKriteria;
    private ArrayList<SubKriteria> subKriteriaFasilitasKamar;
    private ArrayList<SubKriteria> subKriteriaFasilitasUmum;
    private ArrayList<SubKriteria> subKriteriaAksesLingkungan;
    private ArrayList<Alternatif> alternatifs;

    private LazyLoader lazyLoader;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private RankOrderCentroid rankOrderCentroid;


    private String jenisKost;
    private String jenisPembayaran;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perhitungan_rekomendasi);

        //get object reference
        lazyLoader = (LazyLoader) findViewById(R.id.lazyLoader);


        //firebase instance
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        //panggil class roc
        rankOrderCentroid = new RankOrderCentroid();

        //get serialize array from last actvitiy with bundle
        Bundle bundleObject = getIntent().getExtras();
        dataKriteria = (ArrayList<Kriteria>) bundleObject.getSerializable("dataKriteria");
        subKriteriaFasilitasUmum = (ArrayList<SubKriteria>) bundleObject.getSerializable("subKriteriaFasilitasUmum");
        subKriteriaFasilitasKamar = (ArrayList<SubKriteria>) bundleObject.getSerializable("subKriteriaFasilitasKamar");
        subKriteriaAksesLingkungan = (ArrayList<SubKriteria>) bundleObject.getSerializable("subKriteriaAksesLingkungan");


        //new arraylist
        alternatifs = new ArrayList<>();

        LazyLoader loader = new LazyLoader(this, 30, 20, ContextCompat.getColor(this, R.color.loader_selected),
                ContextCompat.getColor(this, R.color.loader_selected),
                ContextCompat.getColor(this, R.color.loader_selected));
        loader.setAnimDuration(500);
        loader.setFirstDelayDuration(100);
        loader.setSecondDelayDuration(200);
        loader.setInterpolator(new LinearInterpolator());
        lazyLoader.addView(loader);


        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PerhitunganRekomendasi.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            getLocationUserFromGPS();
        }

        checkLogBobot(); // check bobot kriteria

    }

    private void hitungSpk(final double latitude, final double longtitude) {
        firebaseFirestore.collection("data_kost").whereEqualTo("isVerification", true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot doc : task.getResult()) {
                    Kost data = doc.toObject(Kost.class);
                    if (data.getIdUser() != firebaseAuth.getCurrentUser().getUid()) {
                        double nilaiHarga = 0.0;
                        double nilaiFasilitasUmum = 0.0;
                        double nilaiFasilitasKamar = 0.0;
                        double nilaiFasilitasAkses = 0.0;
                        double nilaiJarak = 0.0;
                        double nilaiUkuranKamar = 0.0;
                        double nilaialternatif = 0.0;

                        for (int i = 0; i < dataKriteria.size(); i++) {
                            if (dataKriteria.get(i).getNamaKriteria().equals("Harga")) {
                                nilaiHarga = dataKriteria.get(i).getBobotKriteria() * rankOrderCentroid.bobotHarga(data.getHarga());
                                Log.d("NILAI HARGA", "" + dataKriteria.get(i).getBobotKriteria() + " * " + rankOrderCentroid.bobotHarga(data.getHarga()) + " " + nilaiHarga);
                            } else if (dataKriteria.get(i).getNamaKriteria().equals("Ukuran Kamar")) {
                                nilaiUkuranKamar = dataKriteria.get(i).getBobotKriteria() * rankOrderCentroid.bobotUkuranKamar(data.getUkuran_kamar());
                                Log.d("NILAI UKURAN KAMAR", "" + dataKriteria.get(i).getBobotKriteria() + " * " + rankOrderCentroid.bobotUkuranKamar(data.getUkuran_kamar()) + " " + nilaiUkuranKamar);
                            } else if (dataKriteria.get(i).getNamaKriteria().equals("Fasilitas Umum")) {
                                nilaiFasilitasUmum = dataKriteria.get(i).getBobotKriteria() * rankOrderCentroid.bobotFasilitas(data.getFasilitas_umums(), subKriteriaFasilitasUmum);
                                Log.d("NILAI FASILITAS UMUM", "" + dataKriteria.get(i).getBobotKriteria() + " * " + rankOrderCentroid.bobotFasilitas(data.getFasilitas_umums(), subKriteriaFasilitasUmum) + " " + nilaiFasilitasUmum);
                            } else if (dataKriteria.get(i).getNamaKriteria().equals("Fasilitas Kamar")) {
                                nilaiFasilitasKamar = dataKriteria.get(i).getBobotKriteria() * rankOrderCentroid.bobotFasilitas(data.getFasilitas_kamars(), subKriteriaFasilitasKamar);
                                Log.d("NILAI FASILITAS KAMAR", "" + dataKriteria.get(i).getBobotKriteria() + " * " + rankOrderCentroid.bobotFasilitas(data.getFasilitas_kamars(), subKriteriaFasilitasKamar) + " " + nilaiFasilitasKamar);
                            } else if (dataKriteria.get(i).getNamaKriteria().equals("Akses Lokasi / Lingkungan")) {
                                nilaiFasilitasAkses = dataKriteria.get(i).getBobotKriteria() * rankOrderCentroid.bobotFasilitas(data.getAkses_lingkungans(), subKriteriaAksesLingkungan);
                                Log.d("NILAI FASILITAS AKSES", "" + dataKriteria.get(i).getBobotKriteria() + " * " + rankOrderCentroid.bobotFasilitas(data.getAkses_lingkungans(), subKriteriaAksesLingkungan) + " " + nilaiFasilitasAkses);
                            } else if (dataKriteria.get(i).getNamaKriteria().equals("Jarak")) {
                                nilaiJarak = dataKriteria.get(i).getBobotKriteria() * rankOrderCentroid.bobotJarak(distance(data.getLatitude(), data.getLongtitude(), latitude, longtitude));
                                Log.d("JARAK", "" + rankOrderCentroid.bobotJarak(distance(data.getLatitude(), data.getLongtitude(), latitude, longtitude)));
                                Log.d("NILAI JARAK", "" + dataKriteria.get(i).getBobotKriteria() + " * " + rankOrderCentroid.bobotJarak(distance(data.getLatitude(), data.getLongtitude(), latitude, longtitude)) + " " + nilaiJarak);
                            }
                        }

                        nilaialternatif = nilaiHarga + nilaiFasilitasUmum + nilaiFasilitasKamar + nilaiFasilitasAkses + nilaiJarak + nilaiUkuranKamar;
                        Log.d("NILAI ALTERNATIF", "" + nilaialternatif);

                        alternatifs.add(new Alternatif(data.getIdKost(),
                                data.getHarga(),
                                data.getIdUser(),
                                data.getAlamat(),
                                data.isVerification(),
                                data.getJenis_kost(),
                                data.getJenis_sewa(),
                                data.getJumlah_kamar(),
                                data.getKeterangan(),
                                data.getKode_pos(),
                                data.getKota(),
                                data.getLatitude(),
                                data.getLongtitude(),
                                data.getNama_pengelola(),
                                data.getNomor_telepon(),
                                data.getProvinsi(),
                                data.getSisa_kamar(),
                                data.getUkuran_kamar(),
                                data.getFoto_kost(),
                                data.getPemesanans(),
                                data.getAkses_lingkungans(),
                                data.getFasilitas_kamars(),
                                data.getFasilitas_umums(),
                                nilaialternatif));
                    }
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(PerhitunganRekomendasi.this, AlternatifKostActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("alternatifs", alternatifs);
                        i.putExtras(bundle);
                        startActivity(i);
                        finish();
                    }
                }, 6000);
            }
        });
    }

    private double distance(double lat1, double long1, double lat2, double long2) {
        Location locationA = new Location("point A");

        locationA.setLatitude(lat1);
        locationA.setLongitude(long1);

        Location locationB = new Location("point B");

        locationB.setLatitude(lat2);
        locationB.setLongitude(long2);

        double distance = locationA.distanceTo(locationB);


        return distance;
    }

    private void checkLogBobot() {
        for (int i = 0; i < dataKriteria.size(); i++) {
            Log.d("Kriteria", "" + dataKriteria.get(i).getNamaKriteria() + " " + dataKriteria.get(i).getBobotKriteria());
        }

        for (int i = 0; i < subKriteriaFasilitasUmum.size(); i++) {
            Log.d("Sub Fasilitas Umum", "" + subKriteriaFasilitasUmum.get(i).getNamaSubKriteria() + " " + subKriteriaFasilitasUmum.get(i).getBobotSubKriteria());
        }

        for (int i = 0; i < subKriteriaFasilitasKamar.size(); i++) {
            Log.d("Sub Fasilitas Kamar", "" + subKriteriaFasilitasKamar.get(i).getNamaSubKriteria() + " " + subKriteriaFasilitasKamar.get(i).getBobotSubKriteria());
        }

        for (int i = 0; i < subKriteriaAksesLingkungan.size(); i++) {
            Log.d("Sub Fasilitas Akses", "" + subKriteriaAksesLingkungan.get(i).getNamaSubKriteria() + " " + subKriteriaAksesLingkungan.get(i).getBobotSubKriteria());
        }
    }


    private void getLocationUserFromGPS() {
        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(PerhitunganRekomendasi.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {

                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(PerhitunganRekomendasi.this).removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int lastestLocationIndex = locationResult.getLocations().size() - 1;
                            double lattitude = locationResult.getLocations().get(lastestLocationIndex).getLatitude();
                            double longtitude = locationResult.getLocations().get(lastestLocationIndex).getLongitude();

                            hitungSpk(lattitude, longtitude);

                        };
                    }
                }, Looper.getMainLooper());
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLocationUserFromGPS();
            }
            else{
                Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
