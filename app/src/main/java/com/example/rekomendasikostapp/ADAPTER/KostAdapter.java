package com.example.rekomendasikostapp.ADAPTER;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rekomendasikostapp.CLASS.Kost;
import com.example.rekomendasikostapp.CLASS.Preferensi;
import com.example.rekomendasikostapp.MAINKOST.DetailKostPenggunaActivity;
import com.example.rekomendasikostapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class KostAdapter extends RecyclerView.Adapter<KostAdapter.MyViewHolder> {

    private Context c;
    private ArrayList<Kost> kosts;

    private String dataLokasi;
    private String namaLokasi;
    private Double latitude;
    private Double longtitude;

    public KostAdapter(Context c, ArrayList<Kost> kosts, String namaLokasi, Double latitude, Double longtitude){
        this.kosts = kosts;
        this.c = c;
        this.namaLokasi = namaLokasi;
        this.latitude = latitude;
        this.longtitude = longtitude;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new KostAdapter.MyViewHolder(LayoutInflater.from(c).inflate(R.layout.single_list_kost_terbaru, parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        //currency format
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        holder.alamatKost.setText(kosts.get(position).getAlamat() + ", " + kosts.get(position).getKota() + ", " + kosts.get(position).getProvinsi() + " " + kosts.get(position).getKode_pos());
        holder.jenisKost.setText(kosts.get(position).getJenis_kost());
        holder.jumlahKamar.setText("Ada " + kosts.get(position).getJumlah_kamar() + " Kamar");
        holder.sisaKamar.setText("Sisa " + kosts.get(position).getSisa_kamar() + " Kamar");
        holder.hargaKost.setText(formatRupiah.format((double)kosts.get(position).getHarga()) + " / " + kosts.get(position).getJenis_sewa());
        getJarakLokasi(holder.jarakLokasi, kosts.get(position).getIdKost());

        if(kosts.get(position).getSisa_kamar() < 3)
        {
            holder.sisa.setBackgroundColor(Color.parseColor("#d32f2f"));
        }

        if(kosts.get(position).getJenis_kost().equals("Perempuan"))
        {
            holder.jenis.setBackgroundColor(Color.parseColor("#c2185b"));
        }

        for(int i = 0; i < kosts.get(position).getFoto_kost().size();i++){
            if(i == 0){
                Picasso.get().load(kosts.get(position).getFoto_kost().get(i)).into(holder.gambarKost);
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c,  DetailKostPenggunaActivity.class);
                intent.putExtra("idKost", kosts.get(position).getIdKost());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.getApplicationContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return kosts.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView gambarKost;
        TextView alamatKost;
        TextView jenisKost;
        TextView hargaKost;
        TextView sisaKamar;
        TextView jumlahKamar;
        TextView jarakLokasi;

        RelativeLayout jenis;
        RelativeLayout sisa;

        public MyViewHolder(View view) {
            super(view);

            gambarKost = (ImageView) view.findViewById(R.id.gambarKost);
            alamatKost = (TextView) view.findViewById(R.id.alamatKost);
            jenisKost = (TextView) view.findViewById(R.id.jenisKost);
            hargaKost = (TextView) view.findViewById(R.id.hargaKost);
            sisaKamar = (TextView) view.findViewById(R.id.sisaKamar);
            jumlahKamar = (TextView) view.findViewById(R.id.jumlahKamar);
            jenis = (RelativeLayout) view.findViewById(R.id.jenis);
            sisa = (RelativeLayout) view.findViewById(R.id.sisa);
            jarakLokasi = (TextView) view.findViewById(R.id.jarakLokasi);
        }
    }

    private void getJarakLokasi (final TextView jarakLokasi, final String idKost){
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        dataLokasi = "Default";

        firebaseFirestore.collection("data_kost").document(idKost).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                Kost data = documentSnapshot.toObject(Kost.class);

                switch (dataLokasi) {
                    case "default":
                        jarakLokasi.setVisibility(View.GONE);
                        break;
                    default:
                        jarakLokasi.setText(new DecimalFormat("##.##").format(distance(latitude, longtitude, data.getLatitude(), data.getLongtitude())) + " Km Dari " + namaLokasi);
                        break;
                }
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

        double km = distance / 1000.0;

        Log.d("DISTANCE",  "" + km);
        return km;
    }
}
