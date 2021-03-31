package com.example.rekomendasikostapp.ADMIN;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rekomendasikostapp.CLASS.Users;
import com.example.rekomendasikostapp.LoginActivity;
import com.example.rekomendasikostapp.PEMESANAN.ListHistoryPemesananKost;
import com.example.rekomendasikostapp.R;
import com.example.rekomendasikostapp.REPORT.ListLaporanPengguna;
import com.example.rekomendasikostapp.VERIFICATION.KostVerificationActivity;
import com.example.rekomendasikostapp.VERIFIKASIUSER.ListVerifikasiUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminProfileFragment extends Fragment implements View.OnClickListener {

    TextView textDisplayName , textEmail , textAccount, textEditProfile , txtAddKost;
    ImageView profilePicture;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    ImageView masterFasilitasUmum, masterFasilitasKamar, masterAksesLingkungan;
    CardView menuLogout, menuVerifikasiUser, menuVerifikasiKost , menuHistoryPemesanan, menuLaporanPengguna;


    public AdminProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_profile, container, false);

        textDisplayName = (TextView) view.findViewById(R.id.txtDiasplayName);
        textEmail = (TextView) view.findViewById(R.id.txtEmail);
        textAccount = (TextView) view.findViewById(R.id.txtStatusAccount);
        profilePicture = (ImageView) view.findViewById(R.id.profilePicture);
        masterFasilitasUmum = (ImageView) view.findViewById(R.id.masterFasilitasUmum);
        masterFasilitasKamar = (ImageView) view.findViewById(R.id.masterFasilitasKamar);
        masterAksesLingkungan = (ImageView) view.findViewById(R.id.masterAksesLingkungan);


        menuLogout = (CardView) view.findViewById(R.id.menuLogout);
        menuVerifikasiKost = (CardView) view.findViewById(R.id.menuVerifikasiKost);
        menuVerifikasiUser = (CardView) view.findViewById(R.id.menuVerifikasiUser);
        menuHistoryPemesanan = (CardView) view.findViewById(R.id.menuHistoryPemesanan);
        menuLaporanPengguna = (CardView) view.findViewById(R.id.menuLaporanPengguna);

        //set on click listener
        masterFasilitasUmum.setOnClickListener(this);
        masterFasilitasKamar.setOnClickListener(this);
        masterAksesLingkungan.setOnClickListener(this);
        menuHistoryPemesanan.setOnClickListener(this);
        menuVerifikasiUser.setOnClickListener(this);

        menuLogout.setOnClickListener(this);
        menuLaporanPengguna.setOnClickListener(this);
        menuVerifikasiKost.setOnClickListener(this);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        // get users data
        firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                 if(documentSnapshot.exists()){
                     Users users = documentSnapshot.toObject(Users.class);
                     textDisplayName.setText(users.getFull_name());
                     textEmail.setText(users.getEmail());
                     textAccount.setText(users.getAccount());
                 }
                 else {
                     Toast.makeText( getActivity(), "Users Not Exists" , Toast.LENGTH_SHORT).show();
                 }
            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.masterFasilitasUmum:
                //your code here
                changeActivity(AdminFasilitasUmum.class);
                break;
            case R.id.masterFasilitasKamar:
                //your code here
                changeActivity(AdminFasilitasKamar.class);
                break;
            case R.id.masterAksesLingkungan:
                //your code here
                changeActivity(AdminAksesLingkungan.class);
                break;
            case R.id.menuLogout:
                //yout code here
                firebaseAuth.signOut();
                changeActivity(LoginActivity.class);
                getActivity().finish();
                break;
            case R.id.menuVerifikasiKost:
                changeActivity(KostVerificationActivity.class);
                break;
            case R.id.menuHistoryPemesanan:
                changeActivity(ListHistoryPemesananKost.class);
                break;
            case R.id.menuLaporanPengguna:
                changeActivity(ListLaporanPengguna.class);
                break;
            case R.id.menuVerifikasiUser:
                changeActivity(ListVerifikasiUser.class);
        }
    }

    private void changeActivity(Class activity){
        Intent intent = new Intent(getContext(), activity);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
