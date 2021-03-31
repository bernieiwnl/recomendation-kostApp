package com.example.rekomendasikostapp.ADAPTER;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rekomendasikostapp.CLASS.FasilitasUmum;
import com.example.rekomendasikostapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FasilitasUmumAdapter extends RecyclerView.Adapter<FasilitasUmumAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<FasilitasUmum> fasilitasUmums;
    private ArrayList<String> idFasilitas = new ArrayList<String>();

    public FasilitasUmumAdapter(Context c, ArrayList<FasilitasUmum> fasilitasUmums){

        this.fasilitasUmums = fasilitasUmums;
        this.context = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FasilitasUmumAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_fasilitas_umum, parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.nama_fasilitas.setText(fasilitasUmums.get(position).getNama_fasilitas());
        Picasso.get().load(fasilitasUmums.get(position).getFoto_url()).into(holder.image_icon);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemView.setBackgroundColor(Color.parseColor("#424242"));
                holder.nama_fasilitas.setTextColor(Color.parseColor("#ffffff"));
                idFasilitas.add(fasilitasUmums.get(position).getIdfasilitas());
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
                holder.nama_fasilitas.setTextColor(Color.parseColor("#000000"));
                idFasilitas.remove(fasilitasUmums.get(position).getIdfasilitas());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return fasilitasUmums.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nama_fasilitas;
        ImageView image_icon;

        public MyViewHolder(View view){
            super(view);

            nama_fasilitas = (TextView) view.findViewById(R.id.namaFasilitas);
            image_icon = (ImageView) view.findViewById(R.id.imageIcon);
        }

    }

    public ArrayList<String> getArrayList(){
        return idFasilitas;
    }
}
