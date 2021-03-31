package com.example.rekomendasikostapp.PENGELOLA.ADAPTER;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rekomendasikostapp.CLASS.FasilitasUmum;
import com.example.rekomendasikostapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DaftarFasilitasUmum extends RecyclerView.Adapter<DaftarFasilitasUmum.MyViewHolder>  {

    private Context c;
    private ArrayList<FasilitasUmum> fasilitasUmums;

    public DaftarFasilitasUmum(Context c , ArrayList<FasilitasUmum> fasilitasUmums){
        this.c = c;
        this.fasilitasUmums = fasilitasUmums;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DaftarFasilitasUmum.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.pengelola_fasilitas_umum_layout , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.nama_fasilitas.setText(fasilitasUmums.get(position).getNama_fasilitas());
        Picasso.get().load(fasilitasUmums.get(position).getFoto_url()).into(holder.icon);
    }

    @Override
    public int getItemCount() {
        return fasilitasUmums.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nama_fasilitas;
        ImageView icon;

        public MyViewHolder(View view){
            super(view);
            nama_fasilitas = (TextView) view.findViewById(R.id.namaFasilitas);
            icon = (ImageView) view.findViewById(R.id.imageIcon);
        }
    }
}
