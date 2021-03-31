package com.example.rekomendasikostapp.FRAGMENT;


import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rekomendasikostapp.ADAPTER.AlternatifAdapter;
import com.example.rekomendasikostapp.CLASS.Alternatif;
import com.example.rekomendasikostapp.CLASS.FasilitasUmum;
import com.example.rekomendasikostapp.CLASS.Kost;
import com.example.rekomendasikostapp.CLASS.Preferensi;
import com.example.rekomendasikostapp.CLASS.RankOrderCentroid;
import com.example.rekomendasikostapp.LOCATION.UpdateLokasi;
import com.example.rekomendasikostapp.PENCARIAN.PencarianKostActivity;
import com.example.rekomendasikostapp.PENCARIAN.PencarianLokasiKost;
import com.example.rekomendasikostapp.PREFERENSI.MenuPreferensi;
import com.example.rekomendasikostapp.PREFERENSI.UbahKriteriaPreferensi;
import com.example.rekomendasikostapp.R;
import com.example.rekomendasikostapp.REKOMENDASI.PemilihanKriteriaActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {


    public HomeFragment() {
        // Required empty public constructor
    }

    private ArrayList<Alternatif> alternatifs;
    ArrayList<String> fasilitasUmumID;
    ArrayList<String> fasilitasKamarID;
    ArrayList<String> fasilitasAksesID;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private RankOrderCentroid rankOrderCentroid;


    private RecyclerView recyclerViewAlternatif;
    private AlternatifAdapter alternatifAdapter;

    private TextView searchBar;
    private TextView rekomendasi;
    private TextView changeLoc;

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        //get object referecne
        recyclerViewAlternatif = view.findViewById(R.id.recylerKostTerbaru);
        searchBar = view.findViewById(R.id.searchBar);
        rekomendasi = view.findViewById(R.id.rekomendasi);
        changeLoc = view.findViewById(R.id.changeLoc);

        //firebase instance
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        //get loc
        firebaseFirestore.collection("preferensi").document(firebaseAuth.getCurrentUser().getUid()).addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){
                    Preferensi data = documentSnapshot.toObject(Preferensi.class);
                    changeLoc.setText(data.getNamaLokasi());
                }
            }
        });

        //panggil class roc
        rankOrderCentroid = new RankOrderCentroid();


        //set onclick listener
        searchBar.setOnClickListener(this);
        rekomendasi.setOnClickListener(this);
        changeLoc.setOnClickListener(this);

        //new arraylist
        alternatifs = new ArrayList<>();
         fasilitasUmumID = new ArrayList<>();
         fasilitasKamarID = new ArrayList<>();
         fasilitasAksesID = new ArrayList<>();

        //set layout manager
        recyclerViewAlternatif.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewAlternatif.setNestedScrollingEnabled(false);


        hitungSpk();

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
        recyclerViewAlternatif.addItemDecoration(dividerItemDecoration);


        return view;
    }


    private void hitungSpk() {

        firebaseFirestore.collection("preferensi").document(firebaseAuth.getCurrentUser().getUid()).addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                final Preferensi dataPreferensi = documentSnapshot.toObject(Preferensi.class);

                firebaseFirestore.collection("data_kost").whereEqualTo("isVerification", true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            Kost data = doc.toObject(Kost.class);

                            fasilitasAksesID.clear();
                            fasilitasKamarID.clear();
                            fasilitasUmumID.clear();

                            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                            long diff = timestamp.getTime() - data.getUpdated_at().getTime();
                            long seconds = diff / 1000;
                            long minutes = seconds / 60;
                            long hours = minutes / 60;
                            long days = hours / 24;
                            Log.d("DAYS", "Day " + days);

                            if (days < 30) {
                                if (distance(dataPreferensi.getLatitude(), dataPreferensi.getLongtitude(), data.getLatitude(), data.getLongtitude()) <= 1500.0) {
                                    if (data.getIdUser() != firebaseAuth.getCurrentUser().getUid()) {

                                        double nilaiHarga = 0.0;
                                        double nilaiFasilitasUmum = 0.0;
                                        double nilaiFasilitasKamar = 0.0;
                                        double nilaiFasilitasAkses = 0.0;
                                        double nilaiJarak = 0.0;
                                        double nilaiUkuranKamar = 0.0;
                                        double nilaialternatif = 0.0;

                                        for (int i = 0; i < dataPreferensi.getPreferensiKriteria().size(); i++) {

                                            if (dataPreferensi.getPreferensiKriteria().get(i).getNamaKriteria().equals("Harga")) {
                                                nilaiHarga = dataPreferensi.getPreferensiKriteria().get(i).getBobotKriteria() * rankOrderCentroid.bobotHarga(data.getHarga());
                                                Log.d("NILAI HARGA", "" + dataPreferensi.getPreferensiKriteria().get(i).getBobotKriteria() + " * " + rankOrderCentroid.bobotHarga(data.getHarga()) + " " + nilaiHarga);
                                            } else if (dataPreferensi.getPreferensiKriteria().get(i).getNamaKriteria().equals("Ukuran Kamar")) {
                                                nilaiUkuranKamar = dataPreferensi.getPreferensiKriteria().get(i).getBobotKriteria() * rankOrderCentroid.bobotUkuranKamar(data.getUkuran_kamar());
                                                Log.d("NILAI UKURAN KAMAR", "" + dataPreferensi.getPreferensiKriteria().get(i).getBobotKriteria() + " * " + rankOrderCentroid.bobotUkuranKamar(data.getUkuran_kamar()) + " " + nilaiUkuranKamar);
                                            } else if (dataPreferensi.getPreferensiKriteria().get(i).getNamaKriteria().equals("Fasilitas Umum")) {
                                                nilaiFasilitasUmum = dataPreferensi.getPreferensiKriteria().get(i).getBobotKriteria() * rankOrderCentroid.bobotFasilitas(data.getFasilitas_umums(), dataPreferensi.getPreferensiUmum());
                                                Log.d("NILAI FASILITAS UMUM", "" + dataPreferensi.getPreferensiKriteria().get(i).getBobotKriteria() + " * " + rankOrderCentroid.bobotFasilitas(data.getFasilitas_umums(), dataPreferensi.getPreferensiUmum()) + " " + nilaiFasilitasUmum);
                                            } else if (dataPreferensi.getPreferensiKriteria().get(i).getNamaKriteria().equals("Fasilitas Kamar")) {
                                                nilaiFasilitasKamar = dataPreferensi.getPreferensiKriteria().get(i).getBobotKriteria() * rankOrderCentroid.bobotFasilitas(data.getFasilitas_kamars(), dataPreferensi.getPreferensiKamar());
                                                Log.d("NILAI FASILITAS KAMAR", "" + dataPreferensi.getPreferensiKriteria().get(i).getBobotKriteria() + " * " + rankOrderCentroid.bobotFasilitas(data.getFasilitas_kamars(), dataPreferensi.getPreferensiKamar()) + " " + nilaiFasilitasKamar);
                                            } else if (dataPreferensi.getPreferensiKriteria().get(i).getNamaKriteria().equals("Akses Lokasi / Lingkungan")) {
                                                nilaiFasilitasAkses = dataPreferensi.getPreferensiKriteria().get(i).getBobotKriteria() * rankOrderCentroid.bobotFasilitas(data.getAkses_lingkungans(), dataPreferensi.getPreferensiAkses());
                                                Log.d("NILAI FASILITAS AKSES", "" + dataPreferensi.getPreferensiKriteria().get(i).getBobotKriteria() + " * " + rankOrderCentroid.bobotFasilitas(data.getAkses_lingkungans(), dataPreferensi.getPreferensiAkses()) + " " + nilaiFasilitasAkses);
                                            } else if (dataPreferensi.getPreferensiKriteria().get(i).getNamaKriteria().equals("Jarak")) {
                                                nilaiJarak = dataPreferensi.getPreferensiKriteria().get(i).getBobotKriteria() * rankOrderCentroid.bobotJarak(distance(dataPreferensi.getLatitude(), dataPreferensi.getLongtitude(), data.getLatitude(), data.getLongtitude()));
                                                Log.d("JARAK", "" + rankOrderCentroid.bobotJarak(distance(dataPreferensi.getLatitude(), dataPreferensi.getLongtitude(), data.getLatitude(), data.getLongtitude())));
                                                Log.d("NILAI JARAK", "" + dataPreferensi.getPreferensiKriteria().get(i).getBobotKriteria() + " * " + rankOrderCentroid.bobotJarak(distance(dataPreferensi.getLatitude(), dataPreferensi.getLongtitude(), data.getLatitude(), data.getLongtitude())) + " " + nilaiJarak);
                                            }
                                        }

                                        nilaialternatif = nilaiHarga + nilaiFasilitasUmum + nilaiFasilitasKamar + nilaiFasilitasAkses + nilaiJarak + nilaiUkuranKamar;
                                        Log.d("NILAI ALTERNATIF", "" + nilaialternatif);



                                        for (int j = 0; j < dataPreferensi.getPreferensiUmum().size(); j++) {
                                            fasilitasUmumID.add(dataPreferensi.getPreferensiUmum().get(j).getIdSubKriteria());
                                        }

                                        for (int k = 0; k < dataPreferensi.getPreferensiKamar().size(); k++) {
                                            fasilitasKamarID.add(dataPreferensi.getPreferensiKamar().get(k).getIdSubKriteria());
                                        }

                                        for (int l= 0; l < dataPreferensi.getPreferensiAkses().size(); l++) {
                                            fasilitasAksesID.add(dataPreferensi.getPreferensiAkses().get(l).getIdSubKriteria());
                                        }


                                        //sort fasilitas umum
                                        Collections.sort(fasilitasUmumID);
                                        Collections.sort(data.getFasilitas_umums());

                                        //sort fasilitas kamar
                                        Collections.sort(fasilitasKamarID);
                                        Collections.sort(data.getFasilitas_kamars());

                                        //sor fasilitas akses
                                        Collections.sort(fasilitasAksesID);
                                        Collections.sort(data.getAkses_lingkungans());

                                        boolean tampil = false;

                                        if(fasilitasUmumID.equals(data.getFasilitas_umums()) && fasilitasKamarID.equals(data.getFasilitas_kamars()) && fasilitasAksesID.equals(data.getAkses_lingkungans()))
                                        {
                                           tampil = true;
                                        }
                                        else if(fasilitasUmumID.isEmpty() && fasilitasKamarID.equals(data.getFasilitas_kamars()) && fasilitasAksesID.equals(data.getAkses_lingkungans()))
                                        {
                                            tampil = true;
                                        }
                                        else if(fasilitasUmumID.isEmpty() && fasilitasKamarID.isEmpty() && fasilitasAksesID.isEmpty())
                                        {
                                            tampil = true;
                                        }
                                        else if(fasilitasUmumID.equals(data.getFasilitas_umums()) && fasilitasKamarID.isEmpty() && fasilitasAksesID.isEmpty())
                                        {
                                            tampil = true;
                                        }

                                        if(tampil){
                                            alternatifs.add(new Alternatif(data.getIdKost(),
                                                    data.getHarga(),
                                                    data.getIdUser(),
                                                    data.getAlamat(),
                                                    data.isVerification(),
                                                    data.getJenis_kost(),
                                                    data.getJenis_sewa(),
                                                    data.getJumlah_kamar(),
                                                    data.getKeterangan(),
                                                    data.getKode_pos(),
                                                    data.getKota(),
                                                    data.getLatitude(),
                                                    data.getLongtitude(),
                                                    data.getNama_pengelola(),
                                                    data.getNomor_telepon(),
                                                    data.getProvinsi(),
                                                    data.getSisa_kamar(),
                                                    data.getUkuran_kamar(),
                                                    data.getFoto_kost(),
                                                    data.getPemesanans(),
                                                    data.getAkses_lingkungans(),
                                                    data.getFasilitas_kamars(),
                                                    data.getFasilitas_umums(),
                                                    nilaialternatif));
                                        }
                                    }
                                }
                            }
                        }

                        //sort high to low
                        Collections.sort(alternatifs, new Comparator<Alternatif>() {
                            @Override
                            public int compare(Alternatif o1, Alternatif o2) {
                                return o2.getNilai_alternatif().compareTo(o1.getNilai_alternatif());
                            }
                        });

                        alternatifAdapter = new AlternatifAdapter(getActivity(), alternatifs);
                        recyclerViewAlternatif.setAdapter(alternatifAdapter);

                    }
                });
            }
        });

    }

    private double distance(double lat1, double long1, double lat2, double long2) {
        Location locationA = new Location("point A");

        locationA.setLatitude(lat1);
        locationA.setLongitude(long1);

        Location locationB = new Location("point B");

        locationB.setLatitude(lat2);
        locationB.setLongitude(long2);

        double distance = locationA.distanceTo(locationB);
        Log.d("DISTANCE",  "" + distance);
        return distance;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.changeLoc:
                Intent intentLoc = new Intent(getContext(), UpdateLokasi.class);
                getActivity().startActivity(intentLoc);
                break;
            case R.id.searchBar:
                Intent intent = new Intent(getContext(), PencarianLokasiKost.class);
                getActivity().startActivity(intent);
                break;
            case R.id.rekomendasi:
                startActivity(new Intent(getContext(), MenuPreferensi.class));
                break;
        }
    }
}
