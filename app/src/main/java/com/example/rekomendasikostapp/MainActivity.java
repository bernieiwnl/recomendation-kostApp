package com.example.rekomendasikostapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Animation smalltobig,fleft,fhelper;
    ImageView ivSplash;
    TextView ivTitle, ivSubTitle, btnGetStarted;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set object
        ivSplash = (ImageView) findViewById(R.id.img_splash);

        ivTitle = (TextView)  findViewById(R.id.txtLTitle);
        ivSubTitle = (TextView) findViewById(R.id.txtSubtitle);
        btnGetStarted = (TextView) findViewById(R.id.btnGetStarted);

        //load fonts
        Typeface fontsLogo = Typeface.createFromAsset(getAssets(), "fonts/Fredoka.ttf");
        Typeface fontsSubtitle = Typeface.createFromAsset(getAssets(), "fonts/MontserratLight.ttf");
        Typeface fontsBtn = Typeface.createFromAsset(getAssets(), "fonts/MontserratMedium.ttf");
        ivTitle.setTypeface(fontsLogo);
        ivSubTitle.setTypeface(fontsSubtitle);
        btnGetStarted.setTypeface(fontsBtn);

        smalltobig = AnimationUtils.loadAnimation(this, R.anim.smalltobig);
        fleft = AnimationUtils.loadAnimation(this, R.anim.righttoleft);
        fhelper = AnimationUtils.loadAnimation(this, R.anim.fhelper);

        ivTitle.setTranslationX(400);
        ivSubTitle.setTranslationX(400);
        btnGetStarted.setTranslationX(400);

        ivTitle.setAlpha(0);
        ivSubTitle.setAlpha(0);
        btnGetStarted.setAlpha(0);


        ivSplash.startAnimation(smalltobig);

        ivTitle.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        ivSubTitle.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
        btnGetStarted.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(900).start();

        if(fAuth.getInstance().getCurrentUser() != null)
        {
            startActivity( new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        }

    }

    public void toLoginActivity(View view) {
        Intent intent = new Intent(this,StartActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.righttoleft, R.anim.fhelper);
        finish();
    }
}
