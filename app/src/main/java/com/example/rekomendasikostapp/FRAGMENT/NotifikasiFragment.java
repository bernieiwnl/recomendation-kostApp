package com.example.rekomendasikostapp.FRAGMENT;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rekomendasikostapp.ADAPTER.PemberitahuanAdapter;
import com.example.rekomendasikostapp.CLASS.Pemberitahuan;
import com.example.rekomendasikostapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotifikasiFragment extends Fragment {

    public NotifikasiFragment() {
        // Required empty public constructor
    }

    private RecyclerView recyclerViewPemberitahuan;
    private ArrayList<Pemberitahuan> pemberitahuans;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private PemberitahuanAdapter pemberitahuanAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifikasi, container, false);

        recyclerViewPemberitahuan = (RecyclerView) view.findViewById(R.id.recyclerNotifikasi);

        //new arraylist
        pemberitahuans = new ArrayList<>();

        //instance firebase firestore;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        //set layout
        recyclerViewPemberitahuan.setHasFixedSize(true);
        recyclerViewPemberitahuan.setLayoutManager(new LinearLayoutManager(getActivity()));

        //get data
        firebaseFirestore.collection("pemberitahuans").whereEqualTo("idUser", firebaseAuth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                pemberitahuans.clear();
                if (e!=null){
                    Log.d("ERROR","Error:" + e.getMessage());
                }
                else{
                    for(QueryDocumentSnapshot doc : queryDocumentSnapshots){

                        Pemberitahuan data = doc.toObject(Pemberitahuan.class);
                        pemberitahuans.add(data);
                    }
                }

                //sort high to low
                Collections.sort(pemberitahuans, new Comparator<Pemberitahuan>() {
                    @Override
                    public int compare(Pemberitahuan o1, Pemberitahuan o2) {
                        return o2.getTime().compareTo(o1.getTime());
                    }
                });

                pemberitahuanAdapter = new PemberitahuanAdapter(getContext() , pemberitahuans);
                recyclerViewPemberitahuan.setAdapter(pemberitahuanAdapter);
            }
        });


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        recyclerViewPemberitahuan.addItemDecoration(dividerItemDecoration);

        return view;

    }
}
