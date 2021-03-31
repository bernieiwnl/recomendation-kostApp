package com.example.rekomendasikostapp.FRAGMENT;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rekomendasikostapp.CLASS.Users;
import com.example.rekomendasikostapp.INSERTKOST.InsertKost_2;
import com.example.rekomendasikostapp.LoginActivity;
import com.example.rekomendasikostapp.PEMESANAN.ListHistoryPemesananKost;
import com.example.rekomendasikostapp.PEMESANAN.ListPemesananKost;
import com.example.rekomendasikostapp.PENGELOLA.ListKostPengelola;
import com.example.rekomendasikostapp.ProfileActivity;
import com.example.rekomendasikostapp.R;
import com.example.rekomendasikostapp.REPORT.ListLaporanPengguna;
import com.example.rekomendasikostapp.VERIFIKASIUSER.VerifikasiAkunActivity;
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
public class ProfileUserFragment extends Fragment implements View.OnClickListener {

    public ProfileUserFragment() {
        // Required empty public constructor
    }

    TextView textDisplayName , textEmail , textAccount, textEditProfile , txtAddKost;
    ImageView profilePicture;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    CardView menuLogout, menuHistoryPemesanan, menuLaporanPengguna, menuJadiPengelola;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_user, container, false);

        textDisplayName = (TextView) view.findViewById(R.id.txtDiasplayName);
        textEmail = (TextView) view.findViewById(R.id.txtEmail);
        textAccount = (TextView) view.findViewById(R.id.txtStatusAccount);
        profilePicture = (ImageView) view.findViewById(R.id.profilePicture);


        menuLogout = (CardView) view.findViewById(R.id.menuLogout);

        menuHistoryPemesanan = (CardView) view.findViewById(R.id.menuHistoryPemesanan);
        menuLaporanPengguna = (CardView) view.findViewById(R.id.menuLaporanPengguna);
        menuJadiPengelola = (CardView) view.findViewById(R.id.menuJadiPengelola);

        menuHistoryPemesanan.setOnClickListener(this);

        menuLogout.setOnClickListener(this);
        menuLaporanPengguna.setOnClickListener(this);
        menuJadiPengelola.setOnClickListener(this);


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

            case R.id.menuLogout:
                signOut();
                break;

            case R.id.ubahProfile:
                ubahProfile();
                break;

            case R.id.menuHistoryPemesanan:
                changeActivity(ListHistoryPemesananKost.class);
                break;
            case R.id.menuLaporanPengguna:
                changeActivity(ListLaporanPengguna.class);
                break;
            case R.id.menuJadiPengelola:
                changeActivity(VerifikasiAkunActivity.class);
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

    private void changeActivity(Class activity){
        Intent intent = new Intent(getContext(), activity);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
