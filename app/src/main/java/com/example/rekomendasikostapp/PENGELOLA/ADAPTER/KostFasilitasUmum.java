package com.example.rekomendasikostapp.PENGELOLA.ADAPTER;

import android.content.Context;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rekomendasikostapp.CLASS.FasilitasUmum;
import com.example.rekomendasikostapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class KostFasilitasUmum extends RecyclerView.Adapter<KostFasilitasUmum.MyViewHolder> {

    private Context c;
    private ArrayList<FasilitasUmum> fasilitasUmums;
    private String idKost;

    public KostFasilitasUmum(Context c , ArrayList<FasilitasUmum> fasilitasUmums, String idKost){
        this.c = c;
        this.fasilitasUmums = fasilitasUmums;
        this.idKost = idKost;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new KostFasilitasUmum.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.pengelola_fasilitas_umum_layout , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.nama_fasilitas.setText(fasilitasUmums.get(position).getNama_fasilitas());
        Picasso.get().load(fasilitasUmums.get(position).getFoto_url()).into(holder.icon);

        if(fasilitasUmums.get(position).getIdkosts().contains(idKost)){
            holder.itemView.setBackgroundColor(Color.parseColor("#424242"));
            holder.nama_fasilitas.setTextColor(Color.parseColor("#ffffff"));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemView.setBackgroundColor(Color.parseColor("#424242"));
                holder.nama_fasilitas.setTextColor(Color.parseColor("#ffffff"));
                // add method
                setFasilitasKost(idKost, fasilitasUmums.get(position).getIdfasilitas());
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
                holder.nama_fasilitas.setTextColor(Color.parseColor("#000000"));
                //remove method
                unsetFasilitasKotas(idKost, fasilitasUmums.get(position).getIdfasilitas());
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        if(fasilitasUmums != null){
            return fasilitasUmums.size();
        }
        else{
            return 0;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nama_fasilitas;
        ImageView icon;

        public MyViewHolder(View view){
            super(view);
            nama_fasilitas = (TextView) view.findViewById(R.id.namaFasilitas);
            icon = (ImageView) view.findViewById(R.id.imageIcon);
        }

    }

    private void setFasilitasKost(String idKost, String idFasilitas){

        final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("fasilitas_umum").document(idFasilitas).update("idkosts", FieldValue.arrayUnion(idKost)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(c.getApplicationContext(), "Sukses Menambahkan Fasilitas", Toast.LENGTH_SHORT);
            }
        });
        firebaseFirestore.collection("data_kost").document(idKost).update("fasilitas_umums", FieldValue.arrayUnion(idFasilitas));
    }


    private void unsetFasilitasKotas(String idKost, String idFasilitas){

        final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("fasilitas_umum").document(idFasilitas).update("idkosts", FieldValue.arrayRemove(idKost)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(c.getApplicationContext(), "Sukses Menghapus Fasilitas", Toast.LENGTH_SHORT);
            }
        });
        firebaseFirestore.collection("data_kost").document(idKost).update("fasilitas_umums", FieldValue.arrayRemove(idFasilitas));
    }

}
