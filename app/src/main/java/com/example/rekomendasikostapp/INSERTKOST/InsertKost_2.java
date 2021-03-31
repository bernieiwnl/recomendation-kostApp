package com.example.rekomendasikostapp.INSERTKOST;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rekomendasikostapp.ADAPTER.AksesLingkunganAdapter;
import com.example.rekomendasikostapp.ADAPTER.FasilitasKamarAdapter;
import com.example.rekomendasikostapp.ADAPTER.FasilitasUmumAdapter;
import com.example.rekomendasikostapp.CLASS.AksesLingkungan;
import com.example.rekomendasikostapp.CLASS.FasilitasKamar;
import com.example.rekomendasikostapp.CLASS.FasilitasUmum;
import com.example.rekomendasikostapp.FRAGMENT.APIService;
import com.example.rekomendasikostapp.HomeActivity;
import com.example.rekomendasikostapp.LIST.ListBankActivity;
import com.example.rekomendasikostapp.LIST.ListKotaActivity;
import com.example.rekomendasikostapp.LIST.ListProvinsiActivity;
import com.example.rekomendasikostapp.LoginActivity;
import com.example.rekomendasikostapp.MESSAGING.MessageActivity;
import com.example.rekomendasikostapp.NOTIFICATIONS.Client;
import com.example.rekomendasikostapp.NOTIFICATIONS.Data;
import com.example.rekomendasikostapp.NOTIFICATIONS.MyResponse;
import com.example.rekomendasikostapp.NOTIFICATIONS.Sender;
import com.example.rekomendasikostapp.NOTIFICATIONS.Token;
import com.example.rekomendasikostapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsertKost_2 extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, MapboxMap.OnMapClickListener {

    private TextInputEditText editNamaPengelola, editNomorTelepon, editHargaKost, editJumlahKamarKost, editSisaKamarKost, editKeteranganKost, editAlamatLengkapKost, editKodePos, editJenisBank, editNoRek, editAtasNama;
    private TextInputEditText editKotaKost, editProvinsiKost ;
    private Button btnSimpanDataKost;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private RadioGroup radioGroup;
    private RadioGroup radioSewa;
    private RadioGroup radioUkuran;
    private String jenis_kost;
    private String jenis_sewa;
    private String ukuran_kamar;
    private NestedScrollView scrollView;
    private static final String TAG = "InsertKost_2";
    private MapView mapView;
    private MapboxMap map;
    private Marker kostMarker;
    private Point kostPoint;
    private Double latitude, longitude;

    static InsertKost_2 insertKost_2;

    //list recycler view
    private RecyclerView recyclerViewFasilitasUmum , recyclerViewFasilitasKamar , recyclerViewAksesLingkungan;
    private ArrayList<FasilitasUmum> fasilitasUmums;
    private ArrayList<FasilitasKamar> fasilitasKamars;
    private ArrayList<AksesLingkungan> aksesLingkungans;
    private FasilitasUmumAdapter fasilitasUmumAdapter;
    private FasilitasKamarAdapter fasilitasKamarAdapter;
    private AksesLingkunganAdapter aksesLingkunganAdapter;


    APIService apiService;
    private boolean notify = false;

    private Button btnCariLokasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "sk.eyJ1IjoiYWd1c3RpYW5zY2F3IiwiYSI6ImNrNmoxd3JzOTA1ZnkzbXM0dXpuaG1qdzEifQ.AhyUN_JjsT70jDT4sbEyLA");
        setContentView(R.layout.activity_insert_kost_2);

        apiService = Client.getRetrofit("https://fcm.googleapis.com/").create(APIService.class);

        insertKost_2 = this;

        //Get object reference
        radioGroup = (RadioGroup) findViewById(R.id.radioJenis);
        radioSewa = (RadioGroup) findViewById(R.id.jenisSewa);
        radioUkuran = (RadioGroup) findViewById(R.id.ukuranKamar);

        editNamaPengelola = (TextInputEditText) findViewById(R.id.editNamaPengelolaKost);
        editNomorTelepon =  (TextInputEditText) findViewById(R.id.editNomorPengelolaKost);
        editHargaKost = (TextInputEditText) findViewById(R.id.editHargaKamarKost);
        editJumlahKamarKost = (TextInputEditText) findViewById(R.id.editJumlahKamarKost);
        editSisaKamarKost = (TextInputEditText) findViewById(R.id.editSisaKamarKost);
        editKeteranganKost = (TextInputEditText) findViewById(R.id.editKeteranganKost);
        editAlamatLengkapKost = (TextInputEditText) findViewById(R.id.editAlamatKost);
        editKotaKost = (TextInputEditText) findViewById(R.id.editKotaKost);
        editProvinsiKost = (TextInputEditText) findViewById(R.id.editProvinsiKost);
        editKodePos = (TextInputEditText) findViewById(R.id.editKodePos);
        editNoRek = (TextInputEditText) findViewById(R.id.editNomorRekening);
        editJenisBank = (TextInputEditText) findViewById(R.id.editJenisBank);
        editAtasNama = (TextInputEditText) findViewById(R.id.editAtasNama);

        // setonclicklistener
        editKotaKost.setOnClickListener(this);
        editProvinsiKost.setOnClickListener(this);
        editJenisBank.setOnClickListener(this);


        latitude = 0.0;
        longitude = 0.0;


        btnSimpanDataKost = (Button) findViewById(R.id.btnSimpanDataKost);
        btnCariLokasi = (Button) findViewById(R.id.btnCariLokasi);

        btnCariLokasi.setOnClickListener(this);

        scrollView = (NestedScrollView) findViewById(R.id.scrollViewInsert);

        recyclerViewFasilitasUmum = (RecyclerView) findViewById(R.id.recyclerFasilitasUmum);
        recyclerViewFasilitasKamar = (RecyclerView) findViewById(R.id.recyclerFasilitasKamar);
        recyclerViewAksesLingkungan = (RecyclerView) findViewById(R.id.recyclerAksesLingkungan);

        //get selected radio button
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioLaki:
                        //your code here
                        jenis_kost = "Laki - Laki";
                        break;
                    case R.id.radioPerempuan:
                        //your code here
                        jenis_kost = "Perempuan";
                        break;
                }
            }
        });

        radioSewa.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.sewaTahun:
                        //your code here
                        jenis_sewa = "Tahun";
                        break;
                    case R.id.sewaSemester:
                        //your code here
                        jenis_sewa = "Semester";
                        break;
                    case R.id.sewaBulanan:
                        //your code here
                        jenis_sewa = "Bulan";
                        break;
                }
            }
        });

        radioUkuran.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.ukuran2x3:
                        //your code here
                        ukuran_kamar = "2m x 3m";
                        break;
                    case R.id.ukuran3x3:
                        //your code here
                        ukuran_kamar = "3m x 3m";
                        break;
                    case R.id.ukuran3x4:
                        //your code here
                        ukuran_kamar = "3m x 4m";
                        break;
                    case R.id.ukuran4x4:
                        //your code here
                        ukuran_kamar = "4m x 4m";
                        break;
                    case R.id.ukuran4x5:
                        //your code here
                        ukuran_kamar = "4m x 5m";
                        break;
                }
            }
        });


        editAlamatLengkapKost.addTextChangedListener(alamatTextWatcher);
        editKotaKost.addTextChangedListener(alamatTextWatcher);
        editProvinsiKost.addTextChangedListener(alamatTextWatcher);
        editKodePos.addTextChangedListener(alamatTextWatcher);



        //set Onclick Listener
        btnSimpanDataKost.setOnClickListener(this);

        //firebase instance reference
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        // set recylerview layout
        recyclerViewFasilitasUmum.setLayoutManager(new GridLayoutManager(this , 3));
        recyclerViewFasilitasKamar.setLayoutManager(new GridLayoutManager(this , 3));
        recyclerViewAksesLingkungan.setLayoutManager(new GridLayoutManager(this , 3));

        // set arrayList
        fasilitasUmums = new ArrayList<FasilitasUmum>();
        fasilitasKamars = new ArrayList<FasilitasKamar>();
        aksesLingkungans = new ArrayList<AksesLingkungan>();

        //retreive data from firestore
        firebaseFirestore.collection("fasilitas_umum").addSnapshotListener(InsertKost_2.this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(!queryDocumentSnapshots.isEmpty()){
                    fasilitasUmums.clear();
                    for(QueryDocumentSnapshot query : queryDocumentSnapshots){
                        FasilitasUmum data = query.toObject(FasilitasUmum.class);
                        fasilitasUmums.add(data);
                    }
                    fasilitasUmumAdapter = new FasilitasUmumAdapter(getApplicationContext(), fasilitasUmums);
                    recyclerViewFasilitasUmum.setAdapter(fasilitasUmumAdapter);
                }
            }
        });

        //retreive data from firestore
        firebaseFirestore.collection("fasilitas_kamar").addSnapshotListener(InsertKost_2.this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(!queryDocumentSnapshots.isEmpty()){
                    fasilitasKamars.clear();
                    for(QueryDocumentSnapshot query : queryDocumentSnapshots){
                         FasilitasKamar data = query.toObject(FasilitasKamar.class);
                         fasilitasKamars.add(data);
                    }
                    fasilitasKamarAdapter = new FasilitasKamarAdapter(getApplicationContext(), fasilitasKamars);
                    recyclerViewFasilitasKamar.setAdapter(fasilitasKamarAdapter);
                }
                else{
                    Log.d("TAG" , "Error Getting Query" + e);
                }
            }
        });

        //retreive data from firestore
        firebaseFirestore.collection("akses_lingkungan").addSnapshotListener(InsertKost_2.this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(!queryDocumentSnapshots.isEmpty()){
                    aksesLingkungans.clear();
                    for(QueryDocumentSnapshot query : queryDocumentSnapshots){
                        AksesLingkungan data = query.toObject(AksesLingkungan.class);
                        aksesLingkungans.add(data);
                    }
                    aksesLingkunganAdapter = new AksesLingkunganAdapter(getApplicationContext(), aksesLingkungans);
                    recyclerViewAksesLingkungan.setAdapter(aksesLingkunganAdapter);
                }
                else{
                    Log.d("TAG" , "Error Getting Query" + e);
                }
            }
        });

        //validation authentication
        verifAuthentication();

        // mapbox object reference
        mapView = findViewById(R.id.mapViewMarker);
        mapView.onCreate(savedInstanceState);

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
            case R.id.btnSimpanDataKost:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Apakah kamu yakin untuk menyimpan data kost ini?").setCancelable(false).setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        insertDataKost();
                    }
                }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;
            case R.id.editProvinsiKost:
                startActivityForResult( new Intent(getApplicationContext(), ListProvinsiActivity.class), 1);
                break;
            case R.id.editKotaKost:
                startActivityForResult( new Intent(getApplicationContext(), ListKotaActivity.class),2);
                break;
            case R.id.editJenisBank:
                startActivityForResult( new Intent(getApplicationContext(), ListBankActivity.class),3);
                break;
            case R.id.btnCariLokasi:
                getLocation();
                break;
        }
    }

    private void getLocation(){

        String alamat = editAlamatLengkapKost.getText().toString().trim();
        String kota = editKotaKost.getText().toString().trim();
        String provinsi = editProvinsiKost.getText().toString().trim();
        String kodePos = editKodePos.getText().toString().trim();

        String alamatLengkap = alamat + " " + kota + " " + provinsi + " " + kodePos;

        if(!alamat.isEmpty() && !kota.isEmpty() && !provinsi.isEmpty() && !kodePos.isEmpty()){
            Geocoder geocoder = new Geocoder(InsertKost_2.this, Locale.getDefault());
            try {
                List<Address> addressList = geocoder.getFromLocationName(alamatLengkap, 1);

                if(addressList.size() > 0){
                    Address address = addressList.get(0);

                    latitude = address.getLatitude();
                    longitude = address.getLongitude();

                    if(kostMarker != null){
                        map.removeMarker(kostMarker);
                    }

                    kostMarker = map.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)));


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
            String alamat = editAlamatLengkapKost.getText().toString().trim();
            String kota = editKotaKost.getText().toString().trim();
            String provinsi = editProvinsiKost.getText().toString().trim();
            String kodePos = editKodePos.getText().toString().trim();

            if(!alamat.isEmpty() && !kota.isEmpty() && !provinsi.isEmpty() && !kodePos.isEmpty()){
                btnCariLokasi.setVisibility(View.VISIBLE);
            }
        }
    };

    void insertDataKost(){

        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        String nama_pengelola = editNamaPengelola.getText().toString().trim();
        String nomor_telepon = editNomorTelepon.getText().toString().trim();
        Integer harga_kost = Integer.parseInt(editHargaKost.getText().toString().trim());
        Integer jumlah_kamar = Integer.parseInt(editJumlahKamarKost.getText().toString().trim());
        Integer sisa_kamar = Integer.parseInt(editSisaKamarKost.getText().toString().trim());
        String keterangan_kost = editKeteranganKost.getText().toString().trim();
        String alamat_kost = editAlamatLengkapKost.getText().toString().trim();
        String kota = editKotaKost.getText().toString().trim();
        String provinsi = editProvinsiKost.getText().toString().trim();
        String norek = editNoRek.getText().toString().trim();
        String bank = editJenisBank.getText().toString().trim();
        String atas_nama = editAtasNama.getText().toString().trim();
        Integer kodepos = Integer.parseInt(editKodePos.getText().toString().trim());


        Map<String, Object> kosts = new HashMap<>();
        ArrayList<String> foto_kost = new ArrayList<>();
        ArrayList<String> fumum = new ArrayList<>();
        ArrayList<String> fkamar = new ArrayList<>();
        ArrayList<String> fakses = new ArrayList<>();
        kosts.put("idUser", firebaseAuth.getCurrentUser().getUid());
        kosts.put("jenis_kost", jenis_kost );
        kosts.put("nama_pengelola", nama_pengelola);
        kosts.put("nomor_telepon",  nomor_telepon);
        kosts.put("harga" , harga_kost);
        kosts.put("jenis_sewa", jenis_sewa);
        kosts.put("ukuran_kamar" , ukuran_kamar);
        kosts.put("jumlah_kamar" , jumlah_kamar);
        kosts.put("sisa_kamar", sisa_kamar);
        kosts.put("keterangan", keterangan_kost);
        kosts.put("alamat", alamat_kost);
        kosts.put("kota", kota);
        kosts.put("provinsi", provinsi);
        kosts.put("kode_pos", kodepos);
        kosts.put("latitude", latitude);
        kosts.put("longtitude", longitude);
        kosts.put("isVerification" , false);
        kosts.put("foto_kost" , foto_kost);
        kosts.put("fasilitas_umums", fumum);
        kosts.put("fasilitas_kamars", fkamar);
        kosts.put("akses_lingkungans", fakses);
        kosts.put("nomor_rekening", norek );
        kosts.put("nama_bank", bank);
        kosts.put("nama_rekening", atas_nama);
        kosts.put("updated_at", timestamp);
        kosts.put("created_at", timestamp);
        kosts.put("status", "Belum Terverifikasi");
        notify = true;

        firebaseFirestore.collection("data_kost").add(kosts).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                firebaseFirestore.collection("data_kost").document(documentReference.getId()).update("idKost", documentReference.getId());
                insertFasilitasUmum(documentReference.getId());
                insertFasilitasKamar(documentReference.getId());
                insertAksesLingkungan(documentReference.getId());
                Intent intent = new Intent(getApplicationContext(), InsertKost_3.class);
                intent.putExtra("idKosts",documentReference.getId());
                sendPemberitahuan(documentReference.getId(), "Iklan Baru");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void sendPemberitahuan(final String idKost , String jenis)
    {
        Map<String, Object> pemberitahuan = new HashMap<>();
        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        pemberitahuan.put("idUser", "yBE1P8R4qbQyyguVBlMXpf52Aam1");
        pemberitahuan.put("idKost", idKost);
        pemberitahuan.put("idSender", firebaseAuth.getCurrentUser().getUid());
        pemberitahuan.put("idPemesanan", "");
        pemberitahuan.put("jenis_pemberitahuan", jenis);
        pemberitahuan.put("judul", "Ada Kost Baru Perlu Verifikasi");
        pemberitahuan.put("deskripsi", "Ada tempat kost yang baru saja ditambahkan oleh pengelola. Untuk memverifikasi pastikan kost tersebut memiliki data yang valid. Jika data belum valid tanyakan kepada pengelola kost melalui halaman chat yang disediakan. Sebagai admin anda berhak menolak data tersebut jika dirasa kurang meyakinkan");
        pemberitahuan.put("status", "unread");
        pemberitahuan.put("time", timestamp);

        firebaseFirestore.collection("pemberitahuans").add(pemberitahuan).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                firebaseFirestore.collection("pemberitahuans").document(documentReference.getId()).update("idPemberitahuan", documentReference.getId());
            }
        });
    }

    void insertFasilitasUmum(String documentId){
        fasilitasUmumAdapter.getArrayList();
        for(int i = 0; i< fasilitasUmumAdapter.getArrayList().size(); i++)
        {
            firebaseFirestore.collection("fasilitas_umum").document(fasilitasUmumAdapter.getArrayList().get(i)).update("idkosts", FieldValue.arrayUnion(documentId));
            firebaseFirestore.collection("data_kost").document(documentId).update("fasilitas_umums", FieldValue.arrayUnion(fasilitasUmumAdapter.getArrayList().get(i)));
        }
    }

    void insertFasilitasKamar(String documentId){
        fasilitasKamarAdapter.getArrayList();

        for(int i = 0; i< fasilitasKamarAdapter.getArrayList().size(); i++)
        {
            firebaseFirestore.collection("fasilitas_kamar").document(fasilitasKamarAdapter.getArrayList().get(i)).update("idkosts", FieldValue.arrayUnion(documentId));
            firebaseFirestore.collection("data_kost").document(documentId).update("fasilitas_kamars", FieldValue.arrayUnion(fasilitasKamarAdapter.getArrayList().get(i)));
        }
    }

    void insertAksesLingkungan(String documentId){
        aksesLingkunganAdapter.getArrayList();

        for(int i = 0; i< aksesLingkunganAdapter.getArrayList().size(); i++)
        {
            firebaseFirestore.collection("akses_lingkungan").document(aksesLingkunganAdapter.getArrayList().get(i)).update("idkosts", FieldValue.arrayUnion(documentId));
            firebaseFirestore.collection("data_kost").document(documentId).update("akses_lingkungans", FieldValue.arrayUnion(aksesLingkunganAdapter.getArrayList().get(i)));
        }
    }

    void verifAuthentication(){
        if(firebaseAuth.getCurrentUser() == null){
            startActivity( new Intent(getApplicationContext(), LoginActivity.class));
            Toast.makeText( getApplicationContext() , "Signed Out Automatically" , Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        map = mapboxMap;
        map.addOnMapClickListener(this);


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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == 1 && resultCode == RESULT_OK){
                editProvinsiKost.setText(data.getStringExtra("provinsi"));
            }
            else if(requestCode == 2 && resultCode == RESULT_OK){
                editKotaKost.setText(data.getStringExtra("kota"));
            }
            else if(requestCode == 3 && resultCode == RESULT_OK){
                editJenisBank.setText(data.getStringExtra("bank"));
            }
    }



    private void sendNotification(final String title, final String body, final Integer icon, final String sender, final String receiver, final String idKost, final String idPemberitahuan, final String idPemesanan, final String idKeluhan, final String jenis_notifikasi)
    {
        FirebaseFirestore token = FirebaseFirestore.getInstance();

        Query query = token.collection("token").whereEqualTo("userID", receiver);
        query.addSnapshotListener(InsertKost_2.this ,new EventListener<QuerySnapshot>() {
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
                                    Toast.makeText(InsertKost_2.this, "Gagal", Toast.LENGTH_SHORT).show();
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

    public static InsertKost_2 getInstance(){
        return  insertKost_2;
    }

}
