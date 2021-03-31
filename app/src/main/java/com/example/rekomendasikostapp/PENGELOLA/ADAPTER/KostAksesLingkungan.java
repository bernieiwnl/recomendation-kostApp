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

import com.example.rekomendasikostapp.CLASS.AksesLingkungan;
import com.example.rekomendasikostapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class KostAksesLingkungan extends RecyclerView.Adapter<KostAksesLingkungan.MyViewHolder> {

    private ArrayList<AksesLingkungan> aksesLingkungans;
    private Context c;
    private String idKost;

    public KostAksesLingkungan(Context c, ArrayList<AksesLingkungan> aksesLingkungans, String idKost){
        this.c = c;
        this.aksesLingkungans = aksesLingkungans;
        this.idKost = idKost;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new KostAksesLingkungan.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.pengelola_fasilitas_akses_layout , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.nama_akses.setText(aksesLingkungans.get(position).getNama_akses());
        Picasso.get().load(aksesLingkungans.get(position).getFoto_url()).into(holder.icon);

        if(aksesLingkungans.get(position).getIdkosts().contains(idKost)){
            holder.itemView.setBackgroundColor(Color.parseColor("#424242"));
            holder.nama_akses.setTextColor(Color.parseColor("#ffffff"));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemView.setBackgroundColor(Color.parseColor("#424242"));
                holder.nama_akses.setTextColor(Color.parseColor("#ffffff"));
                // add method
                setFasilitasKost(idKost, aksesLingkungans.get(position).getIdfasilitas());
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
                holder.nama_akses.setTextColor(Color.parseColor("#000000"));
                //remove method
                unsetFasilitasKotas(idKost, aksesLingkungans.get(position).getIdfasilitas());
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return aksesLingkungans.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nama_akses;
        ImageView icon;

        public MyViewHolder(View view){
            super(view);

            nama_akses = (TextView) view.findViewById(R.id.namaFasilitas);
            icon = (ImageView) view.findViewById(R.id.imageIcon);
        }
    }


    private void setFasilitasKost(String idKost, String idFasilitas){

        final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("akses_lingkungan").document(idFasilitas).update("idkosts", FieldValue.arrayUnion(idKost)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(c.getApplicationContext(), "Sukses Menambahkan Fasilitas", Toast.LENGTH_SHORT);
            }
        });
        firebaseFirestore.collection("data_kost").document(idKost).update("akses_lingkungans", FieldValue.arrayUnion(idFasilitas));
    }


    private void unsetFasilitasKotas(String idKost, String idFasilitas){

        final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("akses_lingkungan").document(idFasilitas).update("idkosts", FieldValue.arrayRemove(idKost)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(c.getApplicationContext(), "Sukses Menghapus Fasilitas", Toast.LENGTH_SHORT);
            }
        });

        firebaseFirestore.collection("data_kost").document(idKost).update("akses_lingkungans", FieldValue.arrayRemove(idFasilitas));
    }


}
