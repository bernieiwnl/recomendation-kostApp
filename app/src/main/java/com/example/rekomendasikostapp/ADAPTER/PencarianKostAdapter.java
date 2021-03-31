package com.example.rekomendasikostapp.ADAPTER;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rekomendasikostapp.CLASS.Kost;
import com.example.rekomendasikostapp.MAINKOST.DetailKostPenggunaActivity;
import com.example.rekomendasikostapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PencarianKostAdapter extends RecyclerView.Adapter<PencarianKostAdapter.MyViewHolder> {

    private Context c;
    private ArrayList<Kost> kosts;
    private double latitude;
    private double longtitude;

    public PencarianKostAdapter(Context c, ArrayList<Kost> kosts , double latitude , double longtitude){
        this.kosts = kosts;
        this.c = c;
        this.longtitude = longtitude;
        this.latitude = latitude;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PencarianKostAdapter.MyViewHolder(LayoutInflater.from(c).inflate(R.layout.single_list_kost_terbaru, parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.alamatKost.setText(kosts.get(position).getAlamat() + ", " + kosts.get(position).getKota() + ", " + kosts.get(position).getProvinsi() + " " + kosts.get(position).getKode_pos());
        holder.jenisKost.setText("Kost untuk " + kosts.get(position).getJenis_kost());
        holder.jenisSewa.setText("/ " + kosts.get(position).getJenis_sewa());
        holder.hargaKost.setText("Rp." + kosts.get(position).getHarga().toString());
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
        TextView jenisSewa;
        TextView hargaKost;

        public MyViewHolder(View view) {
            super(view);

            gambarKost = (ImageView) view.findViewById(R.id.gambarKost);
            alamatKost = (TextView) view.findViewById(R.id.alamatKost);
            jenisKost = (TextView) view.findViewById(R.id.jenisKost);
            jenisSewa = (TextView) view.findViewById(R.id.jenisSewa);
            hargaKost = (TextView) view.findViewById(R.id.hargaKost);

        }
    }


}
