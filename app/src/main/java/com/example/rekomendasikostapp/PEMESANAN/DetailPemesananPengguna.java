package com.example.rekomendasikostapp.PEMESANAN;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rekomendasikostapp.CLASS.Kost;
import com.example.rekomendasikostapp.CLASS.LoadingDialogLogin;
import com.example.rekomendasikostapp.CLASS.Pemesanan;
import com.example.rekomendasikostapp.FRAGMENT.APIService;
import com.example.rekomendasikostapp.MESSAGING.MessageActivity;
import com.example.rekomendasikostapp.NOTIFICATIONS.Client;
import com.example.rekomendasikostapp.NOTIFICATIONS.Data;
import com.example.rekomendasikostapp.NOTIFICATIONS.MyResponse;
import com.example.rekomendasikostapp.NOTIFICATIONS.Sender;
import com.example.rekomendasikostapp.NOTIFICATIONS.Token;
import com.example.rekomendasikostapp.PENGELOLA.DetailKostPengelola;
import com.example.rekomendasikostapp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPemesananPengguna extends AppCompatActivity implements View.OnClickListener {

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

    private RelativeLayout relativeLayoutDetailPembayaran;
    private RelativeLayout relativeLayoutBuktiPembayaran;

    //buktipembayaran
    private StorageReference storageReference;
    private StorageTask storageTask;
    private Uri Uimage;
    private String fCurrentPath ;
    private static final int GALLERY_REQUEST_CODE = 2;

    private Button btnBuktiPembayaran;
    private Button btnDetailPembayaran;
    private Button btnBatalkanPemesanan;

    String idKost;
    String idPemesanan;
    String idPengelola;
    String idUser;

    APIService apiService;

    private boolean notify = false;

    final LoadingDialogLogin loading = new LoadingDialogLogin(DetailPemesananPengguna.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pemesanan_pengguna);

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
        nomorRekening = (TextView) findViewById(R.id.nomorRekening);
        namaRekening = (TextView) findViewById(R.id.atasNama);
        minimalPembayaran = (TextView) findViewById(R.id.minimalPembayaran);

        tanggalPemesanan = (TextView) findViewById(R.id.tanggalPemesanan);
        tanggalKedatangan = (TextView) findViewById(R.id.tanggalKedatangan);
        namaPemesan = (TextView) findViewById(R.id.namaPemesan);
        nomorTelepon = (TextView) findViewById(R.id.nomorTelepon);
        durasiPenyewaan = (TextView) findViewById(R.id.lamaKost);

        relativeLayoutDetailPembayaran = (RelativeLayout) findViewById(R.id.relativeDetailPembayaran);


        btnBuktiPembayaran = (Button) findViewById(R.id.btnUploadBukti);
        btnDetailPembayaran = (Button) findViewById(R.id.detailBuktiTransfer);
        btnBatalkanPemesanan = (Button) findViewById(R.id.btnBatalkanPemesanan);

        btnBuktiPembayaran.setOnClickListener(this);
        btnDetailPembayaran.setOnClickListener(this);
        btnBatalkanPemesanan.setOnClickListener(this);


        //get intent
        idKost = getIntent().getStringExtra("idKost");
        idPemesanan = getIntent().getStringExtra("idPemesanan");
        idUser = getIntent().getStringExtra("idUser");
        idPengelola = getIntent().getStringExtra("idPengelola");

        // firesbase instance;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("bukti_pembayaran");

        //get data pembayaran
        firebaseFirestore.collection("pemesanans").document(idPemesanan).addSnapshotListener(DetailPemesananPengguna.this, new EventListener<DocumentSnapshot>() {
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
                        btnDetailPembayaran.setVisibility(View.GONE);
                    }
                    else {
                        btnDetailPembayaran.setVisibility(View.VISIBLE);
                    }


                    if(dataPemesanan.getStatus().equals("Sudah Konfirmasi")){
                        btnBuktiPembayaran.setVisibility(View.GONE);
                        btnBatalkanPemesanan.setVisibility(View.GONE);
                    }
                    else if(dataPemesanan.getStatus().equals("Menunggu Konfirmasi")){

                        btnBatalkanPemesanan.setVisibility(View.GONE);

                    }

                    else if(dataPemesanan.getStatus().equals("Pemesanan Dibatalkan"))
                    {
                        btnBuktiPembayaran.setVisibility(View.GONE);
                        btnBatalkanPemesanan.setVisibility(View.GONE);
                    }

                }
            }
        });


        //get data kost
        firebaseFirestore.collection("data_kost").document(idKost).addSnapshotListener(DetailPemesananPengguna.this, new EventListener<DocumentSnapshot>() {
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnUploadBukti:
                openImage();
                break;
            case R.id.detailBuktiTransfer:
                Intent intent = new Intent(DetailPemesananPengguna.this , DetailImageTransfer.class);
                intent.putExtra("idPemesanan", idPemesanan);
                startActivity(intent);
                break;

            case R.id.btnBatalkanPemesanan:
                batalkanPemesanan(idPemesanan);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            loading.startLoadingLoginDialog();

            Uimage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uimage));
            } catch (IOException e) {
                e.printStackTrace();
            }
            uploadBukti(idPemesanan);

        }
    }

    private void batalkanPemesanan(final String idPemesanan)
    {
        HashMap<String, Object> konfirmasi = new HashMap<>();
        konfirmasi.put("status", "Pemesanan Dibatalkan");

        firebaseFirestore.collection("pemesanans").document(idPemesanan).update(konfirmasi).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                btnDetailPembayaran.setVisibility(View.GONE);
                btnBatalkanPemesanan.setVisibility(View.GONE);
                sendPembatalan(idPemesanan);
                Toast.makeText(getApplicationContext(), "Pemesanan Dibatalkan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendPembatalan(final String idPemesanan){

        //kirim pemberitahuan ke user
        Map<String, Object> kirimPemberitahuan = new HashMap<>();
        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        kirimPemberitahuan.put("idUser", idPengelola);
        kirimPemberitahuan.put("idKost", idKost);
        kirimPemberitahuan.put("idSender", firebaseAuth.getCurrentUser().getUid());
        kirimPemberitahuan.put("idPemesanan", idPemesanan);
        kirimPemberitahuan.put("jenis_pemberitahuan", "Pembatalan Pemesanan");
        kirimPemberitahuan.put("judul", "Pemesananmu Sudah Dibatalkan");
        kirimPemberitahuan.put("deskripsi", "Pemesananmu sudah dibatalkan, Lengkapi data - data kost kamu dan iklankan kost kamu disini");
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
        terimaPemgeritahuan.put("idSender", idPengelola);
        terimaPemgeritahuan.put("jenis_pemberitahuan", "Pembatalan Pemesanan");
        terimaPemgeritahuan.put("judul", "Pemesananmu Sudah Dibatalkan");
        terimaPemgeritahuan.put("deskripsi", "Pemesanan Sudah Dibatalakan, Cari kost yang cocok buatmu hanya dengan menggunakan aplikasi ini");
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
        query.addSnapshotListener(DetailPemesananPengguna.this,new EventListener<QuerySnapshot>() {
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
                                    Toast.makeText(DetailPemesananPengguna.this, "Gagal", Toast.LENGTH_SHORT).show();
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


    private void sendPemberitahuan(final String idPemesanan)
    {
        //pemberitahuan ke pengelola
        Map<String, Object> pemberitahuanPengelola = new HashMap<>();
        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        pemberitahuanPengelola.put("idUser", idPengelola);
        pemberitahuanPengelola.put("idKost", idKost);
        pemberitahuanPengelola.put("idSender", firebaseAuth.getCurrentUser().getUid());
        pemberitahuanPengelola.put("idPemesanan", idPemesanan);
        pemberitahuanPengelola.put("jenis_pemberitahuan", "Terima Pembayaran");
        pemberitahuanPengelola.put("judul", "Pemesan Sudah Mengirimkan Bukti Transfer");
        pemberitahuanPengelola.put("deskripsi", "Calon penghuni kost kamu sudah mengirimkan bukti transfer pemesanan. Segera hubungi calon penghuni kost dan konfirmasi data pemesanan kost.");
        pemberitahuanPengelola.put("status", "unread");
        pemberitahuanPengelola.put("time", timestamp);

        firebaseFirestore.collection("pemberitahuans").add(pemberitahuanPengelola).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                firebaseFirestore.collection("pemberitahuans").document(documentReference.getId()).update("idPemberitahuan", documentReference.getId());
                String deskripsi = "Calon penghuni kost kamu sudah mengirimkan bukti transfer pemesanan. Segera hubungi calon penghuni kost dan konfirmasi data pemesanan kost.";
                String title = "Pemesan Sudah Mengirimkan Bukti Transfer";
//                sendNotification(title, deskripsi, R.mipmap.ic_launcher, firebaseAuth.getCurrentUser().getUid(), idPengelola, idKost , documentReference.getId() , idPemesanan, "", "Terima Pembayaran");
            }
        });

        //pemberitahuan ke user
        Map<String, Object> pemberitahuanUser = new HashMap<>();
        final Timestamp timestamp1 = new Timestamp(System.currentTimeMillis());

        pemberitahuanUser.put("idUser", firebaseAuth.getCurrentUser().getUid());
        pemberitahuanUser.put("idKost", idKost);
        pemberitahuanUser.put("idPemesanan", idPemesanan);
        pemberitahuanUser.put("idSender", idPengelola);
        pemberitahuanUser.put("jenis_pemberitahuan", "Kirim Pembayaran");
        pemberitahuanUser.put("judul", "Bukti Transfer Sudah Diterima Pengelola");
        pemberitahuanUser.put("deskripsi", "Bukti transfermu sudah diterima pengelola kost. Hubungi Pengelola agar konfirmasi pembayaran dapat dilaksanakan secepat mungkin, Terima kasih");
        pemberitahuanUser.put("status", "unread");
        pemberitahuanUser.put("time", timestamp1);

        firebaseFirestore.collection("pemberitahuans").add(pemberitahuanUser).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                firebaseFirestore.collection("pemberitahuans").document(documentReference.getId()).update("idPemberitahuan", documentReference.getId());
            }
        });

    }


    private void uploadBukti(final String idPemesanan) {
        if(Uimage != null){

            final StorageReference fileReference = storageReference.child(idPemesanan + "." + getFileExtension(Uimage));
            storageTask = fileReference.putFile(Uimage);

            storageTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot , Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw  task.getException();
                    }
                    return  fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();
                        Log.e("idFasilitas" , "" + mUri);
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("bukti_url", mUri);
                        map.put("status", "Menunggu Konfirmasi");
                        firebaseFirestore.collection("pemesanans").document(idPemesanan).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                loading.dismissDialog();
                                btnDetailPembayaran.setVisibility(View.VISIBLE);
                                sendPemberitahuan(idPemesanan);
                                Toast.makeText(DetailPemesananPengguna.this,
                                        "Upload Bukti Pemesanan Sukses", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else{
                        Toast.makeText(DetailPemesananPengguna.this,
                                "Gagal!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void openImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent , GALLERY_REQUEST_CODE );
    }

}
