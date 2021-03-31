package com.example.rekomendasikostapp.PEMESANAN;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rekomendasikostapp.CLASS.Kost;
import com.example.rekomendasikostapp.CLASS.Pemesanan;
import com.example.rekomendasikostapp.CLASS.Users;
import com.example.rekomendasikostapp.FRAGMENT.APIService;
import com.example.rekomendasikostapp.MESSAGING.MessageActivity;
import com.example.rekomendasikostapp.MainActivity;
import com.example.rekomendasikostapp.NOTIFICATIONS.Client;
import com.example.rekomendasikostapp.NOTIFICATIONS.Data;
import com.example.rekomendasikostapp.NOTIFICATIONS.MyResponse;
import com.example.rekomendasikostapp.NOTIFICATIONS.Sender;
import com.example.rekomendasikostapp.NOTIFICATIONS.Token;
import com.example.rekomendasikostapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PemesananActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText inputDurasi;
    private TextInputEditText inputKedatangan;
    private TextInputEditText inputNamaPemesan;
    private TextInputEditText inputNomorTeleponPemesan;

    private TextView alamatKost;
    private TextView sisaKamar;
    private TextView hargaKost;
    private TextView ukuranKamar;
    private TextView jumlahKamar;
    private TextView jenisKost;
    private ImageView imageKost;
    private DatePickerDialog.OnDateSetListener date;
    private Calendar myCalendar;
    private FirebaseFirestore firebaseFirestore;
    private RelativeLayout sisa;
    private RelativeLayout jenis;
    private Button buttonPesan;


    private FirebaseAuth firebaseAuth;

    APIService apiService;

    private boolean notify = false;


    String idKost;
    String idUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemesanan);
        apiService = Client.getRetrofit("https://fcm.googleapis.com/").create(APIService.class);

        myCalendar = Calendar.getInstance();

        //get object reference
        inputDurasi = (TextInputEditText) findViewById(R.id.inputLamaKost);
        inputKedatangan = (TextInputEditText) findViewById(R.id.inputTanggalKost);
        inputNamaPemesan = (TextInputEditText) findViewById(R.id.inputNamaPemesan);
        inputNomorTeleponPemesan = (TextInputEditText) findViewById(R.id.inputNomorTelepon);

        jumlahKamar = (TextView) findViewById(R.id.jumlahKamar);
        sisaKamar = (TextView) findViewById(R.id.sisaKamar);
        alamatKost = (TextView) findViewById(R.id.alamatKost);
        hargaKost = (TextView) findViewById(R.id.harga);
        jenisKost = (TextView) findViewById(R.id.jenisKost);
        ukuranKamar = (TextView) findViewById(R.id.ukuranKamar);
        imageKost = (ImageView) findViewById(R.id.imageKost);
        jenis = (RelativeLayout) findViewById(R.id.jenis);
        sisa = (RelativeLayout) findViewById(R.id.sisa);

        buttonPesan = (Button) findViewById(R.id.btnPesan);


        //get intent
        idKost = getIntent().getStringExtra("idKost");
        idUser = getIntent().getStringExtra("idUser");

        //set onClick Listener
        inputDurasi.setOnClickListener(this);
        inputKedatangan.setOnClickListener(this);
        buttonPesan.setOnClickListener(this);


        // firesbase instance;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = firebaseAuth.getInstance();


        //get data user
        firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).addSnapshotListener(PemesananActivity.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){
                    Users datausers = documentSnapshot.toObject(Users.class);

                    inputNamaPemesan.setText(datausers.getFull_name());
                    inputNomorTeleponPemesan.setText(datausers.getPhone());
                }
            }
        });


        //get data kost
        firebaseFirestore.collection("data_kost").document(idKost).addSnapshotListener(PemesananActivity.this, new EventListener<DocumentSnapshot>() {
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


        //create date picker
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                setDate();
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.inputLamaKost:
                Intent i = new Intent(PemesananActivity.this, ListDurasiPemesanan.class);
                i.putExtra("idKost", idKost);
                startActivityForResult(i, 1);
                break;
            case R.id.inputTanggalKost:
                DatePickerDialog dialog = new DatePickerDialog(PemesananActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));

                // get 2 month
                Calendar calendar2Month = Calendar.getInstance();
                calendar2Month.add(Calendar.MONTH, +2);
                Date date2month = calendar2Month.getTime();

                dialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
                dialog.getDatePicker().setMaxDate(date2month.getTime());
                dialog.show();
                break;
            case R.id.btnPesan:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Apakah kamu yakin untuk memesan kost ini?").setCancelable(false).setPositiveButton("Konfirmasi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        insertPemesanan();
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
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK){
            inputDurasi.setText(data.getStringExtra("durasi"));
        }
    }

    private void setDate()
    {
        SimpleDateFormat format = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        inputKedatangan.setText(format.format(myCalendar.getTime()));
    }

    private void insertPemesanan()
    {
        String lamaKost = inputDurasi.getText().toString().trim();
        String namaPemesan = inputNamaPemesan.getText().toString().trim();
        String nomorTelepon = inputNomorTeleponPemesan.getText().toString().trim();
        final Timestamp tanggalPemesanan = new Timestamp(System.currentTimeMillis());

        if(TextUtils.isEmpty(namaPemesan))
        {
            inputNamaPemesan.setError("Nama pemesan wajib diisi");
            return;
        }
        else if(TextUtils.isEmpty(nomorTelepon))
        {
            inputNomorTeleponPemesan.setError("Nomor telepon wajib diisi");
            return;
        }
        else if(TextUtils.isEmpty(lamaKost))
        {
            inputDurasi.setError("Lama kost wajib diisi");
            return;
        }

        Map<String, Object> pemesanans = new HashMap<>();
        pemesanans.put("idKost", idKost);
        pemesanans.put("idPengelola", idUser);
        pemesanans.put("idUser", firebaseAuth.getCurrentUser().getUid());
        pemesanans.put("bukti_url", "");
        pemesanans.put("durasi_penyewaan", lamaKost);
        pemesanans.put("nama_pemesan", namaPemesan);
        pemesanans.put("nomor_telepon", nomorTelepon);
        pemesanans.put("tanggal_pemesanan", tanggalPemesanan);
        pemesanans.put("tanggal_kedatangan", myCalendar.getTime());
        pemesanans.put("status", "Menunggu Pembayaran");

        //insert data
        firebaseFirestore.collection("pemesanans").add(pemesanans).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                //set idpemesanan
                firebaseFirestore.collection("pemesanans").document(documentReference.getId()).update("idPemesanan", documentReference.getId());

                //sent pemberitahuan ke user pemilik kost
                sendPemberitahuan(documentReference.getId(), "Pemesanan");

                Intent i = new Intent(PemesananActivity.this, PembayaranActivity.class);
                i.putExtra("idPemesanan", documentReference.getId());
                i.putExtra("idKost", idKost);
                startActivity(i);
                finish();
            }
        });
    }

    private void sendPemberitahuan(final String idPemesanan , String jenis)
    {
        //pemberitahuan ke pengelola
        Map<String, Object> pemberitahuan = new HashMap<>();
        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        pemberitahuan.put("idUser", idUser);
        pemberitahuan.put("idKost", idKost);
        pemberitahuan.put("idSender", firebaseAuth.getCurrentUser().getUid());
        pemberitahuan.put("idPemesanan", idPemesanan);
        pemberitahuan.put("jenis_pemberitahuan", jenis);
        pemberitahuan.put("judul", "Ada Pemesanan Baru");
        pemberitahuan.put("deskripsi", "Terdapat pemesan baru yang ingin menyewa tempat kostmu. Untuk konfirmasi pemesanan tunggu pemesan mengirimkan bukti transfer ke halaman chat anda. Konfirmasikan kepada pemesan melalui halaman chat yang diesidakan.");
        pemberitahuan.put("status", "unread");
        pemberitahuan.put("time", timestamp);

        firebaseFirestore.collection("pemberitahuans").add(pemberitahuan).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                firebaseFirestore.collection("pemberitahuans").document(documentReference.getId()).update("idPemberitahuan", documentReference.getId());
            }
        });

        //pemberitahuan ke user
        Map<String, Object> pembayaran = new HashMap<>();
        final Timestamp timestamp1 = new Timestamp(System.currentTimeMillis());

        pembayaran.put("idUser", firebaseAuth.getCurrentUser().getUid());
        pembayaran.put("idKost", idKost);
        pembayaran.put("idPemesanan", idPemesanan);
        pembayaran.put("idSender", idUser);
        pembayaran.put("jenis_pemberitahuan", "Pembayaran User");
        pembayaran.put("judul", "Pembayaran Pemesanan Kost");
        pembayaran.put("deskripsi", "Konfirmasi pemesanan kost akan diverifikasi oleh pengelola kost, jika kamu sudah membayar dan menyertakan bukti transfer pembayaran. Pembayaran dapat dilakukan melalui atm / bank sesuai dengan nomor rekening yang disediakan");
        pembayaran.put("status", "unread");
        pembayaran.put("time", timestamp1);

        firebaseFirestore.collection("pemberitahuans").add(pembayaran).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                firebaseFirestore.collection("pemberitahuans").document(documentReference.getId()).update("idPemberitahuan", documentReference.getId());
            }
        });

    }

    private void sendNotification(final String title, final String body, final Integer icon, final String sender, final String receiver, final String idKost, final String idPemberitahuan, final String idPemesanan, final String idKeluhan, final String jenis_notifikasi)
    {

        firebaseFirestore.collection("token").document(receiver).addSnapshotListener(PemesananActivity.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                Token tokenData = documentSnapshot.toObject(Token.class);
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
                                Toast.makeText(PemesananActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MyResponse> call, Throwable t) {

                    }
                });
            }
        });

    }



}
