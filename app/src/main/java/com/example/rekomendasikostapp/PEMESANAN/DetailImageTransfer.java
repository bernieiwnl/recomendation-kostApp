package com.example.rekomendasikostapp.PEMESANAN;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.rekomendasikostapp.CLASS.Pemesanan;
import com.example.rekomendasikostapp.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

public class DetailImageTransfer extends AppCompatActivity {

    private PhotoView photoView;
    private String idPemesanan;

    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_image_transfer);

        //getintent
        idPemesanan = getIntent().getStringExtra("idPemesanan");

        //get object ref
        final PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);

        //get instance firebase
        firebaseFirestore = FirebaseFirestore.getInstance();

        //get data
        firebaseFirestore.collection("pemesanans").document(idPemesanan).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                Pemesanan data = documentSnapshot.toObject(Pemesanan.class);

                Picasso.get().load(data.getBukti_url()).into(photoView);

            }
        });


    }
}
