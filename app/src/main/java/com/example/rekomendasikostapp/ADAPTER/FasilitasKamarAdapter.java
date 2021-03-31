package com.example.rekomendasikostapp.ADAPTER;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rekomendasikostapp.CLASS.FasilitasKamar;
import com.example.rekomendasikostapp.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FasilitasKamarAdapter extends RecyclerView.Adapter<FasilitasKamarAdapter.MyViewHolder>{

    private Context context;
    private ArrayList<FasilitasKamar> fasilitasKamars;
    private ArrayList<String> idFasilitas = new ArrayList<String>();
    private FirebaseFirestore firebaseFirestore;


    public FasilitasKamarAdapter(Context c, ArrayList<FasilitasKamar> fasilitasKamars){

        this.fasilitasKamars = fasilitasKamars;
        this.context = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FasilitasKamarAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_fasilitas_kamar, parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.nama_fasilitas.setText(fasilitasKamars.get(position).getNama_fasilitas());
        Picasso.get().load(fasilitasKamars.get(position).getFoto_url()).into(holder.imageIcon);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.itemView.setBackgroundColor(Color.parseColor("#424242"));
                holder.nama_fasilitas.setTextColor(Color.parseColor("#ffffff"));
                idFasilitas.add(fasilitasKamars.get(position).getIdfasilitas());
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
                holder.nama_fasilitas.setTextColor(Color.parseColor("#000000"));
                idFasilitas.remove(fasilitasKamars.get(position).getIdfasilitas());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return fasilitasKamars.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nama_fasilitas;
        ImageView imageIcon;

        public MyViewHolder(View view){
            super(view);
            nama_fasilitas = (TextView) view.findViewById(R.id.namaFasilitas);
            imageIcon = (ImageView) view.findViewById(R.id.imageIcon);
        }
    }

    public ArrayList<String> getArrayList(){
        return idFasilitas;
    }
}
