package com.example.rekomendasikostapp.ADAPTER;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rekomendasikostapp.CLASS.Kost;
import com.example.rekomendasikostapp.CLASS.Laporan;
import com.example.rekomendasikostapp.CLASS.Users;
import com.example.rekomendasikostapp.R;
import com.example.rekomendasikostapp.REPORT.DetailLaporanPengguna;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class LaporanPenggunaAdapter extends RecyclerView.Adapter<LaporanPenggunaAdapter.MyViewHolder> {

    Context c;
    ArrayList<Laporan> laporans;

    String txtNamaPelapor;
    String txtAlamatKost;
    String txtimageKost;


    public  LaporanPenggunaAdapter(Context c, ArrayList<Laporan> laporans){
        this.c = c;
        this.laporans = laporans;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LaporanPenggunaAdapter.MyViewHolder(LayoutInflater.from(c).inflate(R.layout.single_list_laporan_pengguna, parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        //get data kost
        setKost(holder.alamatKost, holder.gambarKost , laporans.get(position).getIdKost());

        setPelapor(holder.namaPelapor, laporans.get(position).getIdUser());

        SimpleDateFormat format = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        holder.tanggalLapor.setText(format.format(laporans.get(position).getTanggal_laporan()));
        holder.jenisLaporan.setText(laporans.get(position).getJenis_laporan());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c,  DetailLaporanPengguna.class);
                intent.putExtra("idKost", laporans.get(position).getIdKost());
                intent.putExtra("idLaporan", laporans.get(position).getIdPelaporan());
                intent.putExtra("idUser", laporans.get(position).getIdUser());
                intent.putExtra("idPengelola", laporans.get(position).getIdPengelola());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.getApplicationContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return laporans.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView gambarKost;


        TextView namaPelapor;
        TextView tanggalLapor;
        TextView jenisLaporan;
        TextView alamatKost;


        public MyViewHolder(View view) {
            super(view);

            namaPelapor = (TextView) view.findViewById(R.id.namaPelapor);
            tanggalLapor = (TextView) view.findViewById(R.id.tanggalLapor);
            jenisLaporan = (TextView) view.findViewById(R.id.jenisLaporan);

            gambarKost = (ImageView) view.findViewById(R.id.imageKost);
            alamatKost = (TextView) view.findViewById(R.id.alamatKost);
        }
    }

    private void setPelapor(final TextView namaPelapor, String idPelapor){

        final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        txtNamaPelapor = "default";

        firebaseFirestore.collection("users").document(idPelapor).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                Users usersData = documentSnapshot.toObject(Users.class);

                txtNamaPelapor = usersData.getFull_name();

                switch (txtNamaPelapor){
                    case "default":
                        namaPelapor.setVisibility(View.GONE);
                        break;
                    default:
                        namaPelapor.setText(txtNamaPelapor);
                        break;
                }
            }
        });
    }

    private void setKost(final TextView alamatKost, final ImageView gambarKost, String idKost){

        final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        txtimageKost = "default";
        txtAlamatKost = "default";

        firebaseFirestore.collection("data_kost").document(idKost).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                Kost dataKost = documentSnapshot.toObject(Kost.class);

                txtAlamatKost = dataKost.getAlamat() + ", " + dataKost.getKota() + ", " + dataKost.getProvinsi() + " " + dataKost.getKode_pos();

                for(int i = 0; i < dataKost.getFoto_kost().size();i++){
                    if(i == 0){
                        txtimageKost = dataKost.getFoto_kost().get(i);
                    }
                }



                switch (txtimageKost){
                    case "default":
                        gambarKost.setVisibility(View.GONE);
                        break;
                    default:
                        Picasso.get().load(txtimageKost).into(gambarKost);
                        break;
                }


                switch (txtAlamatKost){
                    case "default":
                        alamatKost.setVisibility(View.GONE);
                        break;
                    default:
                        alamatKost.setText(txtAlamatKost);
                        break;
                }
            }
        });



    }


}
