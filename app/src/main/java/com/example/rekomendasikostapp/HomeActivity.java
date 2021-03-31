package com.example.rekomendasikostapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.rekomendasikostapp.ADMIN.AdminProfileFragment;
import com.example.rekomendasikostapp.CLASS.Constants;
import com.example.rekomendasikostapp.CLASS.FetchLocationServices;
import com.example.rekomendasikostapp.CLASS.LoadingDialogGPS;
import com.example.rekomendasikostapp.CLASS.Users;
import com.example.rekomendasikostapp.FRAGMENT.HomeFragment;
import com.example.rekomendasikostapp.FRAGMENT.NotifikasiFragment;
import com.example.rekomendasikostapp.FRAGMENT.PesanFragment;
import com.example.rekomendasikostapp.FRAGMENT.ProfileFragment;
import com.example.rekomendasikostapp.FRAGMENT.ProfileUserFragment;
import com.example.rekomendasikostapp.NOTIFICATIONS.Token;
import com.example.rekomendasikostapp.PREFERENSI.UbahUmumPreferensi;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String user_account = "";

    private ResultReceiver resultReceiver;
    static HomeActivity homeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //get activity reference
        homeActivity = this;

        //obj reference
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();


        // notification badge chats
        firebaseFirestore.collection("chats").whereEqualTo("receiver", firebaseAuth.getCurrentUser().getUid()).whereEqualTo("read", "").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                int count = 0;
                BadgeDrawable chats = bottomNavigationView.getOrCreateBadge(R.id.navigation_chat);
                for(DocumentSnapshot doc : queryDocumentSnapshots)
                {
                    count++;
                }

                if(count == 0)
                {
                    chats.setVisible(false);
                    chats.clearNumber();
                }
                else{
                    chats.setVisible(true);
                    chats.setNumber(count);
                }

            }
        });

        //notification badge pemberitahuan
        firebaseFirestore.collection("pemberitahuans").whereEqualTo("idUser", firebaseAuth.getCurrentUser().getUid()).whereEqualTo("status", "unread").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                int count = 0;
                BadgeDrawable pemberitahuan = bottomNavigationView.getOrCreateBadge(R.id.navigation_notifikasi);
                for(DocumentSnapshot data : queryDocumentSnapshots)
                {
                    count++;
                }

                if(count == 0)
                {
                    pemberitahuan.setVisible(false);
                    pemberitahuan.clearNumber();
                }
                else{
                    pemberitahuan.setVisible(true);
                    pemberitahuan.setNumber(count);
                }
            }
        });

        firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).addSnapshotListener(HomeActivity.this,new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){
                    Users users = documentSnapshot.toObject(Users.class);
                    if(users.getAccount().equals("Admin")){
                        user_account = "Admin";
                    }
                    else if (users.getAccount().equals("Pengguna")){
                        user_account = "Pengguna";
                    }
                    else if (users.getAccount().equals("Pengelola")){
                        user_account = "Pengelola";
                    }
                }
                else{
                    Log.d("Tag" , "Error get account" + e);
                }
            }
        });



        // update token
        updateToken(FirebaseInstanceId.getInstance().getToken());

    }

    private void updateToken(String token)
    {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        Token dataToken = new Token(token);
        firebaseFirestore.collection("token").document(firebaseAuth.getCurrentUser().getUid()).set(dataToken);
        firebaseFirestore.collection("token").document(firebaseAuth.getCurrentUser().getUid()).update("userID", firebaseAuth.getCurrentUser().getUid());
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Apakah kamu yakin untuk keluar?").setCancelable(false).setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    HomeFragment homeFragment = new HomeFragment();
    PesanFragment pesanFragment = new PesanFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    AdminProfileFragment adminProfileFragment = new AdminProfileFragment();
    NotifikasiFragment notifikasiFragment = new NotifikasiFragment();
    ProfileUserFragment profileUserFragment = new ProfileUserFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.navigation_account:
                if(user_account.equals("Admin")){
                    getSupportFragmentManager().beginTransaction().replace(R.id.layoutFrame , adminProfileFragment).commit();
                }
                else if (user_account.equals("Pengelola")){
                    getSupportFragmentManager().beginTransaction().replace(R.id.layoutFrame , profileFragment).commit();
                }
                else if (user_account.equals("Pengguna")){
                    getSupportFragmentManager().beginTransaction().replace(R.id.layoutFrame , profileUserFragment).commit();
                }
                return true;
            case R.id.navigation_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.layoutFrame , homeFragment).commit();
                return true;

            case R.id.navigation_notifikasi:
                getSupportFragmentManager().beginTransaction().replace(R.id.layoutFrame, notifikasiFragment).commit();
                return true;
            case R.id.navigation_chat:
                getSupportFragmentManager().beginTransaction().replace(R.id.layoutFrame , pesanFragment).commit();
                return true;
        }
        return false;
    }

    public static HomeActivity getInstance(){
        return   homeActivity;
    }

}
