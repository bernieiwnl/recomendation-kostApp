package com.example.rekomendasikostapp.PEMBERITAHUAN.PENGGUNA;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rekomendasikostapp.CLASS.Kost;
import com.example.rekomendasikostapp.CLASS.Pemberitahuan;
import com.example.rekomendasikostapp.CLASS.Pemesanan;
import com.example.rekomendasikostapp.PEMBERITAHUAN.PENGELOLA.PemberitahuanPemesanan;
import com.example.rekomendasikostapp.PEMESANAN.DetailImageTransfer;
import com.example.rekomendasikostapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PemberitahuanKirimPembayaran extends AppCompatActivity implements View.OnClickListener {

    private TextView alamatKost;
    private TextView sisaKamar;
    private TextView hargaKost;
    private TextView ukuranKamar;
    private TextView jumlahKamar;
    private TextView jenisKost;
    private ImageView imageKost;
    private TextView nomorRekening;
    private TextView namaRekening;
    private TextView minimalPembayaran;

    private RelativeLayout sisa;
    private RelativeLayout jenis;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;


    private TextView tanggalPemesanan;
    private TextView tanggalKedatangan;
    private TextView namaPemesan;
    private TextView nomorTelepon;
    private TextView durasiPenyewaan;



    private TextView judulPemberitahuan;
    private TextView isiPemberitahuan;
    private TextView tanggalPemberitahuan;

    private Button btnDetailPembayaran;

    String idKost;
    String idPemesanan;
    String idPengelola;
    String idUser;
    String idPemberitahuan;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemberitahuan_kirim_pembayaran);

        jumlahKamar = (TextView) findViewById(R.id.jumlahKamar);
        sisaKamar = (TextView) findViewById(R.id.sisaKamar);
        alamatKost = (TextView) findViewById(R.id.alamatKost);
        hargaKost = (TextView) findViewById(R.id.harga);
        jenisKost = (TextView) findViewById(R.id.jenisKost);
        ukuranKamar = (TextView) findViewById(R.id.ukuranKamar);
        imageKost = (ImageView) findViewById(R.id.imageKost);
        jenis = (RelativeLayout) findViewById(R.id.jenis);
        sisa = (RelativeLayout) findViewById(R.id.sisa);
        nomorRekening = (TextView) findViewById(R.id.nomorRekening);
        namaRekening = (TextView) findViewById(R.id.atasNama);
        minimalPembayaran = (TextView) findViewById(R.id.minimalPembayaran);

        tanggalPemesanan = (TextView) findViewById(R.id.tanggalPemesanan);
        tanggalKedatangan = (TextView) findViewById(R.id.tanggalKedatangan);
        namaPemesan = (TextView) findViewById(R.id.namaPemesan);
        nomorTelepon = (TextView) findViewById(R.id.nomorTelepon);
        durasiPenyewaan = (TextView) findViewById(R.id.lamaKost);

        judulPemberitahuan = (TextView) findViewById(R.id.judulPemberitahuan);
        isiPemberitahuan = (TextView) findViewById(R.id.isiPemberitahuan);
        tanggalPemberitahuan = (TextView) findViewById(R.id.tanggalPemberitaahuan);


        btnDetailPembayaran = (Button) findViewById(R.id.detailBuktiTransfer);


        btnDetailPembayaran.setOnClickListener(this);



        //get intent
        idKost = getIntent().getStringExtra("idKost");
        idPemesanan = getIntent().getStringExtra("idPemesanan");
        idUser = getIntent().getStringExtra("idUser");
        idPengelola = getIntent().getStringExtra("idPengelola");
        idPemberitahuan = getIntent().getStringExtra("idPemberitahuan");

        // firesbase instance;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        //get data pembayaran
        firebaseFirestore.collection("pemesanans").document(idPemesanan).addSnapshotListener(PemberitahuanKirimPembayaran.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){
                    Pemesanan dataPemesanan = documentSnapshot.toObject(Pemesanan.class);

                    SimpleDateFormat format = new SimpleDateFormat("EEEE, dd MMMM yyyy");

                    tanggalKedatangan.setText(format.format(dataPemesanan.getTanggal_kedatangan().getTime()));
                    tanggalPemesanan.setText(format.format(dataPemesanan.getTanggal_pemesanan().getTime()));
                    namaPemesan.setText(dataPemesanan.getNama_pemesan());
                    nomorTelepon.setText(dataPemesanan.getNomor_telepon());
                    durasiPenyewaan.setText(dataPemesanan.getDurasi_penyewaan());
                }
            }
        });

        firebaseFirestore.collection("pemberitahuans").document(idPemberitahuan).addSnapshotListener(PemberitahuanKirimPembayaran.this, new EventListener<DocumentSnapshot>() {
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


        //get data kost
        firebaseFirestore.collection("data_kost").document(idKost).addSnapshotListener(PemberitahuanKirimPembayaran.this, new EventListener<DocumentSnapshot>() {
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
                    nomorRekening.setText("No Rekening " + kost.getNama_bank() + " : " + kost.getNomor_rekening());
                    namaRekening.setText("A/N : " + kost.getNama_rekening());

                    double minBayar = (double)kost.getHarga();

                    minimalPembayaran.setText("Sebesar : " + formatRupiah.format(minBayar));

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

        setRead(idPemberitahuan);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.detailBuktiTransfer:
                Intent intent = new Intent(PemberitahuanKirimPembayaran.this , DetailImageTransfer.class);
                intent.putExtra("idPemesanan", idPemesanan);
                startActivity(intent);
                break;
        }
    }


    private void setRead(String idPemberitahuan){

        Map<String, Object> pemberitahuan = new HashMap<>();
        pemberitahuan.put("status", "read");
        firebaseFirestore.collection("pemberitahuans").document(idPemberitahuan).update(pemberitahuan);

    }
}
