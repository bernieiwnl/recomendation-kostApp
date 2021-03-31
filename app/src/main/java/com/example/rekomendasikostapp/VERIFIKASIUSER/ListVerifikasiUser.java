package com.example.rekomendasikostapp.VERIFIKASIUSER;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.rekomendasikostapp.ADAPTER.VerifikasiUserAdapter;
import com.example.rekomendasikostapp.CLASS.Users;
import com.example.rekomendasikostapp.R;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ListVerifikasiUser extends AppCompatActivity {

    private RecyclerView recyclerViewVerifikasiUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private ArrayList<Users> users;
    private VerifikasiUserAdapter verifikasiUserAdapter;
    private ImageView empty_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_verifikasi_user);

        //get object reference
        recyclerViewVerifikasiUser = (RecyclerView) findViewById(R.id.reclyerVerifikasiUser);
        empty_message = (ImageView) findViewById(R.id.empty_message);

        // get new array
        users = new ArrayList<>();

        // set layout recyclerview
        recyclerViewVerifikasiUser.setLayoutManager(new LinearLayoutManager(this));

        //instance firebase
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();


        //get data from firebase
        firebaseFirestore.collection("users").whereEqualTo("status", "waiting").addSnapshotListener(ListVerifikasiUser.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                users.clear();
                if(queryDocumentSnapshots.isEmpty()){
                    recyclerViewVerifikasiUser.setVisibility(View.GONE);
                    empty_message.setVisibility(View.VISIBLE);
                }
                else{
                    recyclerViewVerifikasiUser.setVisibility(View.VISIBLE);
                    empty_message.setVisibility(View.GONE);

                    for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                        Users data = doc.toObject(Users.class);
                        users.add(data);
                    }

                    verifikasiUserAdapter = new VerifikasiUserAdapter(ListVerifikasiUser.this, users);
                    recyclerViewVerifikasiUser.setAdapter(verifikasiUserAdapter);

                }
            }
        });


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL);
        recyclerViewVerifikasiUser.addItemDecoration(dividerItemDecoration);
    }
}
