package com.example.rekomendasikostapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class StartActivity extends AppCompatActivity {

    private TextView txtTitle, txtSubtitle , txtAtau;
    private Animation smalltobe;
    private Button btnPengguna, btnPengiklan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        //Relasi objek

        //txt
        txtTitle = findViewById(R.id.txtTitle);
        txtSubtitle = findViewById(R.id.txtSub);
        txtAtau = findViewById(R.id.txtAtau);

        //button
        btnPengguna = findViewById(R.id.btnPengguna);
        btnPengiklan = findViewById(R.id.btnPengiklan);


        //Load Fonts
        Typeface fontsLogo = Typeface.createFromAsset(getAssets(), "fonts/Fredoka.ttf");
        Typeface fontsSubtitle = Typeface.createFromAsset(getAssets(), "fonts/MontserratLight.ttf");
        Typeface fontsBtn = Typeface.createFromAsset(getAssets(), "fonts/MontserratMedium.ttf");

        //Set Fonts
        txtTitle.setTypeface(fontsLogo);
        txtSubtitle.setTypeface(fontsLogo);
        txtAtau.setTypeface(fontsSubtitle);

        btnPengiklan.setTypeface(fontsBtn);
        btnPengguna.setTypeface(fontsBtn);

        //Load animation
        smalltobe = AnimationUtils.loadAnimation(this, R.anim.smalltobig2);

        //start animation
        txtTitle.startAnimation(smalltobe);
        txtSubtitle.startAnimation(smalltobe);
        txtAtau.startAnimation(smalltobe);

        btnPengguna.startAnimation(smalltobe);
        btnPengiklan.startAnimation(smalltobe);


    }

    public void toLoginPengguna(View view) {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.righttoleft, R.anim.fhelper);
        finish();
    }
}
