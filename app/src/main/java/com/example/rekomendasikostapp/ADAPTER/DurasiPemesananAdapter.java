package com.example.rekomendasikostapp.ADAPTER;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rekomendasikostapp.R;

import java.util.ArrayList;

public class DurasiPemesananAdapter extends RecyclerView.Adapter<DurasiPemesananAdapter.MyViewHolder> {

    private ArrayList<String> durasi;
    private Context c;

    public DurasiPemesananAdapter(Context c, ArrayList<String> durasi){
        this.durasi = durasi;
        this.c = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DurasiPemesananAdapter.MyViewHolder(LayoutInflater.from(c).inflate(R.layout.single_list_durasi , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.durasiPemesanan.setText(durasi.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("durasi", durasi.get(position));
                ((Activity)c).setResult(Activity.RESULT_OK, i);
                ((Activity)c).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return durasi.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView durasiPemesanan;

        public MyViewHolder(View view){
            super(view);
            durasiPemesanan = (TextView) view.findViewById(R.id.durasiPemesanan);
        }
    }

}
