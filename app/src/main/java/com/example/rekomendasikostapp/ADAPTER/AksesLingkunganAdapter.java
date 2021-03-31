package com.example.rekomendasikostapp.ADAPTER;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rekomendasikostapp.CLASS.AksesLingkungan;
import com.example.rekomendasikostapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AksesLingkunganAdapter extends RecyclerView.Adapter<AksesLingkunganAdapter.MyViewHolder> {

    private ArrayList<AksesLingkungan> aksesLingkungans;
    private Context context;
    private ArrayList<String> idFasilitas = new ArrayList<String>();

    public AksesLingkunganAdapter(Context c, ArrayList<AksesLingkungan> aksesLingkungans){
        this.aksesLingkungans = aksesLingkungans;
        this.context = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AksesLingkunganAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_akses_lingkungan , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.nama_akses.setText(aksesLingkungans.get(position).getNama_akses());
        Picasso.get().load(aksesLingkungans.get(position).getFoto_url()).into(holder.imageIcon);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemView.setBackgroundColor(Color.parseColor("#424242"));
                holder.nama_akses.setTextColor(Color.parseColor("#ffffff"));
                idFasilitas.add(aksesLingkungans.get(position).getIdfasilitas());
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
                holder.nama_akses.setTextColor(Color.parseColor("#000000"));
                idFasilitas.remove(aksesLingkungans.get(position).getIdfasilitas());
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
        ImageView imageIcon;

        public MyViewHolder(View view){
            super(view);
            nama_akses = (TextView) view.findViewById(R.id.namaFasilitas);
            imageIcon = (ImageView) view.findViewById(R.id.imageIcon);
        }
    }

    public ArrayList<String> getArrayList(){
        return idFasilitas;
    }
}
