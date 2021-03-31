package com.example.rekomendasikostapp.FRAGMENT;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rekomendasikostapp.CLASS.Users;
import com.example.rekomendasikostapp.INSERTKOST.InsertKost_2;
import com.example.rekomendasikostapp.LoginActivity;
import com.example.rekomendasikostapp.PEMESANAN.ListPemesananKost;
import com.example.rekomendasikostapp.PENGELOLA.ListKostPengelola;
import com.example.rekomendasikostapp.ProfileActivity;
import com.example.rekomendasikostapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {

    TextView textDisplayName , textEmail , textAccount, textEditProfile ;
    ImageView menuKonfirmasiPemesanan;
    ImageView txtAddKost , txtDaftarPengelola;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore dbReference;
    ImageView profilePicture;
    LinearLayout signOut;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //Get Object Reference and set click listener


        textEditProfile = (TextView) view.findViewById(R.id.ubahProfile);
        menuKonfirmasiPemesanan = (ImageView) view.findViewById(R.id.menuKonfirmasi);
        txtAddKost = (ImageView) view.findViewById(R.id.btnDaftarKost);
        txtDaftarPengelola = (ImageView) view.findViewById(R.id.menuDaftarKost);
        textDisplayName = (TextView) view.findViewById(R.id.txtDiasplayName);
        textEmail = (TextView) view.findViewById(R.id.txtEmail);
        textAccount = (TextView) view.findViewById(R.id.txtStatusAccount);
        profilePicture = (ImageView) view.findViewById(R.id.profilePicture);
        signOut = (LinearLayout) view.findViewById(R.id.signOut);

        signOut.setOnClickListener(this);
        txtAddKost.setOnClickListener(this);
        txtDaftarPengelola.setOnClickListener(this);
        textEditProfile.setOnClickListener(this);
        menuKonfirmasiPemesanan.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        dbReference = FirebaseFirestore.getInstance();

        dbReference.getInstance().collection("users")
                .document(firebaseAuth.getCurrentUser().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Users users = documentSnapshot.toObject(Users.class);
                    textDisplayName.setText(users.getFull_name());
                    textEmail.setText(users.getEmail());
                    textAccount.setText(users.getPhone());
                    if(users.getProfile_image_url() != null){
                        Picasso.get().load(users.getProfile_image_url()).into(profilePicture);
                    }

                }
                else{
                    Toast.makeText( getActivity(), "Users Not Exists" , Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        // Inflate the layout for this fragment101
        return view;

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.menuDaftarKost:
                daftarKostPengelola();
                break;
            case R.id.signOut:
                signOut();
                break;

            case R.id.ubahProfile:
                ubahProfile();
                break;

            case R.id.btnDaftarKost:
                daftarKosts();
                break;
            case R.id.menuKonfirmasi:
                konfirmasiKost();
                break;

        }
    }

    public void signOut(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getContext(), LoginActivity.class));
        getActivity().finish();
    }

    public void ubahProfile(){
        startActivity(new Intent(getContext(), ProfileActivity.class));
    }

    public void daftarKosts(){
        startActivity(new Intent(getContext(), InsertKost_2.class));
    }

    public void daftarKostPengelola(){
        startActivity(new Intent(getContext(), ListKostPengelola.class));
    }

    public void konfirmasiKost(){
        startActivity(new Intent(getContext(), ListPemesananKost.class));
    }


}
