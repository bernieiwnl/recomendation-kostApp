package com.example.rekomendasikostapp.VERIFIKASIUSER;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rekomendasikostapp.CLASS.Users;
import com.example.rekomendasikostapp.CLASS.Verifikasi;
import com.example.rekomendasikostapp.R;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class DetailVerifikasiUser extends AppCompatActivity implements View.OnClickListener {

    private TextView namaLengkap;
    private TextView alamatEmail;
    private TextView nomortelepon;
    private ImageView profile_picture;

    private TextView alamatPengguna;
    private TextView jenis_identitas;
    private ImageView fotoIdentitas;
    private ImageView fotoSelfie;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private Button buttonVerifikasi;

    String idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_verifikasi_user);


        //object
        namaLengkap = (TextView) findViewById(R.id.namaLengkap);
        alamatEmail = (TextView) findViewById(R.id.alamatEmail);
        nomortelepon = (TextView) findViewById(R.id.nomorTelepon);
        profile_picture = (ImageView) findViewById(R.id.profile_image);

        buttonVerifikasi = (Button) findViewById(R.id.btnVerifikasi);


        alamatPengguna = (TextView) findViewById(R.id.alamatPengguna);
        jenis_identitas = (TextView) findViewById(R.id.jenisIdentitas);
        fotoIdentitas = (ImageView) findViewById(R.id.fotoIdentitas);
        fotoSelfie = (ImageView) findViewById(R.id.selfieIdentitas);

        // on click listener
        buttonVerifikasi.setOnClickListener(this);

        //intent
        idUser = getIntent().getStringExtra("idUser");

        //firebase instance
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        // get data firebase
        firebaseFirestore.collection("users").document(idUser).addSnapshotListener(DetailVerifikasiUser.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                Users data = documentSnapshot.toObject(Users.class);

                namaLengkap.setText(data.getFull_name());
                alamatEmail.setText(data.getEmail());
                nomortelepon.setText(data.getPhone());
                Picasso.get().load(data.getProfile_image_url()).into(profile_picture);
            }
        });

        // get data firebase
        firebaseFirestore.collection("verifikasi").document(idUser).addSnapshotListener(DetailVerifikasiUser.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                Verifikasi verif = documentSnapshot.toObject(Verifikasi.class);
                alamatPengguna.setText(verif.getAlamat());
                jenis_identitas.setText(verif.getJenis_identitas());
                Picasso.get().load(verif.getIdentitas_image_url()).into(fotoIdentitas);
                Picasso.get().load(verif.getSelfie_image_url()).into(fotoSelfie);
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnVerifikasi:
                setThisProfileToPengelola(idUser);
                break;
        }
    }

    private void setThisProfileToPengelola(String idUser){


        Map<String, Object> kirimPemberitahuan = new HashMap<>();
        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        kirimPemberitahuan.put("idUser", idUser);
        kirimPemberitahuan.put("idSender", firebaseAuth.getCurrentUser().getUid());
        kirimPemberitahuan.put("jenis_pemberitahuan", "Verifikasi Data Pengelola Sukses");
        kirimPemberitahuan.put("judul", "Selamat Anda Sudah Menjadi Pengelola");
        kirimPemberitahuan.put("deskripsi", "Selamat kamu sudah jadi pengelola kost, Sekarang kamu dapat mengiklankan kost milikmu");
        kirimPemberitahuan.put("status", "unread");
        kirimPemberitahuan.put("time", timestamp);

        firebaseFirestore.collection("pemberitahuans").add(kirimPemberitahuan).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                firebaseFirestore.collection("pemberitahuans").document(documentReference.getId()).update("idPemberitahuan", documentReference.getId());
            }
        });

        Map<String, Object> verif = new HashMap<>();
        verif.put("status", "terverifikasi");
        verif.put("verification", true);
        verif.put("account", "Pengelola");

        firebaseFirestore.collection("users").document(idUser).update(verif).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                finish();
            }
        });
    }

}
