package com.example.rekomendasikostapp.REPORT;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rekomendasikostapp.CLASS.Kost;
import com.example.rekomendasikostapp.PEMESANAN.PemesananActivity;
import com.example.rekomendasikostapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FormReportKost extends AppCompatActivity implements View.OnClickListener {

    private TextView alamatKost;
    private TextView sisaKamar;
    private TextView hargaKost;
    private TextView ukuranKamar;
    private TextView jumlahKamar;
    private TextView jenisKost;
    private ImageView imageKost;
    private RelativeLayout sisa;
    private RelativeLayout jenis;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;


    private AutoCompleteTextView inputJenisLaporan;
    private TextInputEditText inputDeskripsiLaporan;

    private Button btnLaporkanKost;

    private String idKost;
    private String idPengelola;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_report_kost);

        String[] jenisLaporan = new String[] {"Masalah Komunikasi", "Data Tidak Valid", "Lainnya"};

        ArrayAdapter<String> dropdownAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.single_list_jenis_laporan, jenisLaporan);


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
        btnLaporkanKost = (Button) findViewById(R.id.btnLaporkanKost);

        inputDeskripsiLaporan = (TextInputEditText) findViewById(R.id.inputDeskripsiLaporan);
        inputJenisLaporan = (AutoCompleteTextView) findViewById(R.id.inputJenisLaporan);

        //set adapter
        inputJenisLaporan.setAdapter(dropdownAdapter);

        //get intent
        idKost = getIntent().getStringExtra("idKost");
        idPengelola = getIntent().getStringExtra("idPengelola");


        //setonclicklistner
        btnLaporkanKost.setOnClickListener(this);


        // firesbase instance;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = firebaseAuth.getInstance();

        //get data kost
        firebaseFirestore.collection("data_kost").document(idKost).addSnapshotListener(FormReportKost.this, new EventListener<DocumentSnapshot>() {
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
                }
                else{
                    Log.d("TAG", "Query Not Found" + e);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnLaporkanKost:
                reportKost(idKost, firebaseAuth.getCurrentUser().getUid(), idPengelola );
                break;
        }
    }

    private void reportKost(String idKost, String idUser, String idPengelola)
    {
        final Timestamp tanggalLaporan = new Timestamp(System.currentTimeMillis());


        String jLaporkan = inputJenisLaporan.getText().toString().trim();
        String dLaporkan = inputDeskripsiLaporan.getText().toString().trim();

        Map<String, Object> report = new HashMap<>();
        report.put("idKost", idKost);
        report.put("idUser", idUser);
        report.put("idPengelola", idPengelola);
        report.put("jenis_laporan", jLaporkan);
        report.put("deskripsi", dLaporkan);
        report.put("tanggal_laporan", tanggalLaporan);

        firebaseFirestore.collection("reports").add(report).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                //set idpelaporan
                firebaseFirestore.collection("reports").document(documentReference.getId()).update("idPelaporan", documentReference.getId());
                Toast.makeText(getApplicationContext(), "Sukses Melaporkan Kost", Toast.LENGTH_SHORT).show();
                sendPemberitahuan(documentReference.getId());
                finish();
            }
        });
    }


    private void sendPemberitahuan(String idLaporan)
    {
        //pemberitahuan ke admin
        Map<String, Object> pemberitahuan = new HashMap<>();
        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        pemberitahuan.put("idUser", "yBE1P8R4qbQyyguVBlMXpf52Aam1");
        pemberitahuan.put("idKost", idKost);
        pemberitahuan.put("idSender", firebaseAuth.getCurrentUser().getUid());
        pemberitahuan.put("idPemesanan", "");
        pemberitahuan.put("idLaporan", idLaporan);
        pemberitahuan.put("jenis_pemberitahuan", "Keluhan Pengguna");
        pemberitahuan.put("judul", "Pemberitahuan Keluhan Pengguna");
        pemberitahuan.put("deskripsi", "Ada keluhan baru dari user pengguna terhadap performa kost yang di iklankan. Agar pengguna tetap nyaman menggunakan aplikasi ini lakukanlah suspend terhadap tempat kost yang dikeluhkan. Sebelum melakukan suspend validasi kebenaran performa kost tersebut.");
        pemberitahuan.put("status", "unread");
        pemberitahuan.put("time", timestamp);

        firebaseFirestore.collection("pemberitahuans").add(pemberitahuan).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                firebaseFirestore.collection("pemberitahuans").document(documentReference.getId()).update("idPemberitahuan", documentReference.getId());
            }
        });

    }
}
