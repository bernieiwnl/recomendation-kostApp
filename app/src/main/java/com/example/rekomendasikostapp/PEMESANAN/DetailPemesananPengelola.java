package com.example.rekomendasikostapp.PEMESANAN;

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
import android.widget.Toast;

import com.example.rekomendasikostapp.CLASS.Kost;
import com.example.rekomendasikostapp.CLASS.Pemesanan;
import com.example.rekomendasikostapp.FRAGMENT.APIService;
import com.example.rekomendasikostapp.NOTIFICATIONS.Client;
import com.example.rekomendasikostapp.NOTIFICATIONS.Data;
import com.example.rekomendasikostapp.NOTIFICATIONS.MyResponse;
import com.example.rekomendasikostapp.NOTIFICATIONS.Sender;
import com.example.rekomendasikostapp.NOTIFICATIONS.Token;
import com.example.rekomendasikostapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPemesananPengelola extends AppCompatActivity implements View.OnClickListener {

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

    private TextView tanggalPemesanan;
    private TextView tanggalKedatangan;
    private TextView namaPemesan;
    private TextView nomorTelepon;
    private TextView durasiPenyewaan;

    private Button btnBuktiPembayaran;
    private Button btnKonfirmasiPembayaran;
    private Button btnBatalkanPemesanan;

    String idKost;
    String idPemesanan;
    String idPengelola;
    String idUser;

    APIService apiService;

    private boolean notify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pemesanan_pengelola);

        apiService = Client.getRetrofit("https://fcm.googleapis.com/").create(APIService.class);

        jumlahKamar = (TextView) findViewById(R.id.jumlahKamar);
        sisaKamar = (TextView) findViewById(R.id.sisaKamar);
        alamatKost = (TextView) findViewById(R.id.alamatKost);
        hargaKost = (TextView) findViewById(R.id.harga);
        jenisKost = (TextView) findViewById(R.id.jenisKost);
        ukuranKamar = (TextView) findViewById(R.id.ukuranKamar);
        imageKost = (ImageView) findViewById(R.id.imageKost);
        jenis = (RelativeLayout) findViewById(R.id.jenis);
        sisa = (RelativeLayout) findViewById(R.id.sisa);

        tanggalPemesanan = (TextView) findViewById(R.id.tanggalPemesanan);
        tanggalKedatangan = (TextView) findViewById(R.id.tanggalKedatangan);
        namaPemesan = (TextView) findViewById(R.id.namaPemesan);
        nomorTelepon = (TextView) findViewById(R.id.nomorTelepon);
        durasiPenyewaan = (TextView) findViewById(R.id.lamaKost);


        btnBuktiPembayaran = (Button) findViewById(R.id.detailBuktiTransfer);
        btnKonfirmasiPembayaran = (Button) findViewById(R.id.btnKonfirmasiPemesanan);
        btnBatalkanPemesanan = (Button) findViewById(R.id.btnBatalkanPemesanan);

        btnBuktiPembayaran.setOnClickListener(this);
        btnKonfirmasiPembayaran.setOnClickListener(this);
        btnBatalkanPemesanan.setOnClickListener(this);

        //get intent
        idKost = getIntent().getStringExtra("idKost");
        idPemesanan = getIntent().getStringExtra("idPemesanan");
        idUser = getIntent().getStringExtra("idUser");
        idPengelola = getIntent().getStringExtra("idPengelola");


        // firesbase instance;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        //get data pembayaran
        firebaseFirestore.collection("pemesanans").document(idPemesanan).addSnapshotListener(DetailPemesananPengelola.this, new EventListener<DocumentSnapshot>() {
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

                    if(dataPemesanan.getBukti_url().equals("")){
                        btnBuktiPembayaran.setVisibility(View.GONE);
                        btnKonfirmasiPembayaran.setVisibility(View.GONE);
                    }
                    else{
                        btnBuktiPembayaran.setVisibility(View.VISIBLE);
                        btnKonfirmasiPembayaran.setVisibility(View.VISIBLE);
                    }

                    if(dataPemesanan.getStatus().equals("Sudah Konfirmasi"))
                    {
                        btnKonfirmasiPembayaran.setVisibility(View.GONE);
                        btnBatalkanPemesanan.setVisibility(View.GONE);
                    }
                    else if(dataPemesanan.getStatus().equals("Menunggu Konfirmasi")){

                        btnBatalkanPemesanan.setVisibility(View.GONE);
                    }
                    else if(dataPemesanan.getStatus().equals("Pemesanan Dibatalkan"))
                    {
                        btnKonfirmasiPembayaran.setVisibility(View.GONE);
                        btnBatalkanPemesanan.setVisibility(View.GONE);
                    }
                }
            }
        });

        //get data kost
        firebaseFirestore.collection("data_kost").document(idKost).addSnapshotListener(DetailPemesananPengelola.this, new EventListener<DocumentSnapshot>() {
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
        switch (v.getId()){
            case R.id.detailBuktiTransfer:
                Intent intent = new Intent(DetailPemesananPengelola.this , DetailImageTransfer.class);
                intent.putExtra("idPemesanan", idPemesanan);
                startActivity(intent);
                break;
            case R.id.btnKonfirmasiPemesanan:
                konfirmasiPembayaran(idPemesanan, idKost);
                break;
            case R.id.btnBatalkanPemesanan:
                batalkanPemesanan(idPemesanan);
                break;
        }
    }

    private void batalkanPemesanan(final String idPemesanan)
    {
        HashMap<String, Object> konfirmasi = new HashMap<>();
        konfirmasi.put("status", "Pemesanan Dibatalkan");

        firebaseFirestore.collection("pemesanans").document(idPemesanan).update(konfirmasi).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                btnKonfirmasiPembayaran.setVisibility(View.GONE);
                btnBatalkanPemesanan.setVisibility(View.GONE);
                sendPembatalan(idPemesanan);
                Toast.makeText(getApplicationContext(), "Pemesanan Dibatalkan", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void sendPembatalan(String idPemesanan){

        //kirim pemberitahuan ke user
        Map<String, Object> kirimPemberitahuan = new HashMap<>();
        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        kirimPemberitahuan.put("idUser", idUser);
        kirimPemberitahuan.put("idKost", idKost);
        kirimPemberitahuan.put("idSender", firebaseAuth.getCurrentUser().getUid());
        kirimPemberitahuan.put("idPemesanan", idPemesanan);
        kirimPemberitahuan.put("jenis_pemberitahuan", "Pembatalan Pemesanan");
        kirimPemberitahuan.put("judul", "Pemesananmu Sudah Dibatalkan");
        kirimPemberitahuan.put("deskripsi", "Pemesanan Sudah Dibatalakan oleh pihak pengelola kost, Cari kost yang cocok buatmu hanya dengan menggunakan aplikasi ini");
        kirimPemberitahuan.put("status", "unread");
        kirimPemberitahuan.put("time", timestamp);

        firebaseFirestore.collection("pemberitahuans").add(kirimPemberitahuan).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                firebaseFirestore.collection("pemberitahuans").document(documentReference.getId()).update("idPemberitahuan", documentReference.getId());
            }
        });


        //terima pemberitahuan dari user
        Map<String, Object> terimaPemgeritahuan = new HashMap<>();
        final Timestamp timestamp1 = new Timestamp(System.currentTimeMillis());

        terimaPemgeritahuan.put("idUser", firebaseAuth.getCurrentUser().getUid());
        terimaPemgeritahuan.put("idKost", idKost);
        terimaPemgeritahuan.put("idPemesanan", idPemesanan);
        terimaPemgeritahuan.put("idSender", idUser);
        terimaPemgeritahuan.put("jenis_pemberitahuan", "Pembatalan Pemesanan");
        terimaPemgeritahuan.put("judul", "Pemesananmu Sudah Dibatalkan");
        terimaPemgeritahuan.put("deskripsi", "Pemesananmu sudah dibatalkan, Lengkapi data - data kost kamu dan iklankan kost kamu disini");
        terimaPemgeritahuan.put("status", "unread");
        terimaPemgeritahuan.put("time", timestamp1);

        firebaseFirestore.collection("pemberitahuans").add(terimaPemgeritahuan).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                firebaseFirestore.collection("pemberitahuans").document(documentReference.getId()).update("idPemberitahuan", documentReference.getId());
            }
        });

    }



    private void konfirmasiPembayaran(final String idPemesanan, final String idKost)
    {

        HashMap<String, Object> konfirmasi = new HashMap<>();
        konfirmasi.put("status", "Sudah Konfirmasi");

        firebaseFirestore.collection("pemesanans").document(idPemesanan).update(konfirmasi).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                firebaseFirestore.collection("data_kost").document(idKost).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        Kost dataKost = documentSnapshot.toObject(Kost.class);
                        int sisa_kamar = dataKost.getSisa_kamar() - 1;
                        HashMap<String, Object> updateKost = new HashMap<>();
                        updateKost.put("sisa_kamar",  sisa_kamar);

                        firebaseFirestore.collection("data_kost").document(dataKost.getIdKost()).update(updateKost).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                btnKonfirmasiPembayaran.setVisibility(View.GONE);
                                btnBatalkanPemesanan.setVisibility(View.GONE);
                                sendPemberitahuan(idPemesanan);
                                Toast.makeText(getApplicationContext(), "Konfirmasi Pemesanan Sukses", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
    }

    private void sendPemberitahuan(final String idPemesanan){

        //kirim pemberitahuan ke user
        Map<String, Object> kirimPemberitahuan = new HashMap<>();
        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        kirimPemberitahuan.put("idUser", idUser);
        kirimPemberitahuan.put("idKost", idKost);
        kirimPemberitahuan.put("idSender", firebaseAuth.getCurrentUser().getUid());
        kirimPemberitahuan.put("idPemesanan", idPemesanan);
        kirimPemberitahuan.put("jenis_pemberitahuan", "Verifikasi Pemesanan");
        kirimPemberitahuan.put("judul", "Pemesananmu Sudah Di Konfirmasi");
        kirimPemberitahuan.put("deskripsi", "Pemesanan kamu sudah di konfirmasi oleh pengelola kost. Untuk menentukan jam kedatangan ataupun hal - hal yang lain terkait pemesanan dan tempat kost silahakan bertanya kepada pengelola, Terima kasih sudah menggunakan aplikasi ini");
        kirimPemberitahuan.put("status", "unread");
        kirimPemberitahuan.put("time", timestamp);

        firebaseFirestore.collection("pemberitahuans").add(kirimPemberitahuan).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                firebaseFirestore.collection("pemberitahuans").document(documentReference.getId()).update("idPemberitahuan", documentReference.getId());
                String deskripsi = "Pemesanan kamu sudah di konfirmasi oleh pengelola kost. Untuk menentukan jam kedatangan ataupun hal - hal yang lain terkait pemesanan dan tempat kost silahakan bertanya kepada pengelola, Terima kasih sudah menggunakan aplikasi ini";
                String title = "Pemesananmu Sudah Di Konfirmasi";
//                sendNotification(title, deskripsi, R.mipmap.ic_launcher, firebaseAuth.getCurrentUser().getUid(), idUser, idKost , documentReference.getId() , idPemesanan, "", "Verifikasi Pemesanan");
            }
        });


        //terima pemberitahuan dari user
        Map<String, Object> terimaPemgeritahuan = new HashMap<>();
        final Timestamp timestamp1 = new Timestamp(System.currentTimeMillis());

        terimaPemgeritahuan.put("idUser", firebaseAuth.getCurrentUser().getUid());
        terimaPemgeritahuan.put("idKost", idKost);
        terimaPemgeritahuan.put("idPemesanan", idPemesanan);
        terimaPemgeritahuan.put("idSender", idUser);
        terimaPemgeritahuan.put("jenis_pemberitahuan", "Verifikasi Penghuni Kost");
        terimaPemgeritahuan.put("judul", "Selamat Anda Memiliki Penghuni Kost Baru");
        terimaPemgeritahuan.put("deskripsi", "Penghuni kost baru akan datang sesuai dengan tanggal kedatangan yang tertera pada form pemesanan kost. Tanyakan kepada penghuni kost baru jam kedatangan dan minta identitas penghuni kost baru seperti foto agar penjaga kost dapat mengetahui penghuni kost tersebut.");
        terimaPemgeritahuan.put("status", "unread");
        terimaPemgeritahuan.put("time", timestamp1);

        firebaseFirestore.collection("pemberitahuans").add(terimaPemgeritahuan).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                firebaseFirestore.collection("pemberitahuans").document(documentReference.getId()).update("idPemberitahuan", documentReference.getId());
            }
        });

    }


    private void sendNotification(final String title, final String body, final Integer icon, final String sender, final String receiver, final String idKost, final String idPemberitahuan, final String idPemesanan, final String idKeluhan, final String jenis_notifikasi)
    {
        FirebaseFirestore token = FirebaseFirestore.getInstance();

        Query query = token.collection("token").whereEqualTo("userID", receiver);
        query.addSnapshotListener(DetailPemesananPengelola.this,new EventListener<QuerySnapshot>() {
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
                                    Toast.makeText(DetailPemesananPengelola.this, "Gagal", Toast.LENGTH_SHORT).show();
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
