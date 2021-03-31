package com.example.rekomendasikostapp.PENGELOLA.ADAPTER;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rekomendasikostapp.CLASS.AksesLingkungan;
import com.example.rekomendasikostapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DaftarFasilitasAkses  extends RecyclerView.Adapter<DaftarFasilitasAkses.MyViewHolder> {

    private ArrayList<AksesLingkungan> aksesLingkungans;
    private Context c;

    public DaftarFasilitasAkses(Context c, ArrayList<AksesLingkungan> aksesLingkungans){
        this.c = c;
        this.aksesLingkungans = aksesLingkungans;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DaftarFasilitasAkses.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.pengelola_fasilitas_akses_layout , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.nama_akses.setText(aksesLingkungans.get(position).getNama_akses());
        Picasso.get().load(aksesLingkungans.get(position).getFoto_url()).into(holder.icon);
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
}
