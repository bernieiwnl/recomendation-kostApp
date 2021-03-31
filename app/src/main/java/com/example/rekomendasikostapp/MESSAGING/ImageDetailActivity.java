package com.example.rekomendasikostapp.MESSAGING;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.rekomendasikostapp.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

public class ImageDetailActivity extends AppCompatActivity {

    private PhotoView photoView;
    private String imageuri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        imageuri = getIntent().getStringExtra("imageuri");

        PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);
        Picasso.get().load(imageuri).into(photoView);
    }
}
