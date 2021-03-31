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

import com.example.rekomendasikostapp.CLASS.AksesLingkungan;
import com.example.rekomendasikostapp.R;

import java.util.ArrayList;

public class PrioritasAksesLingkunganAdapter extends RecyclerView.Adapter<PrioritasAksesLingkunganAdapter.MyViewHolder> {

    private Context c;
    private ArrayList<AksesLingkungan> aksesLingkungans;

    public PrioritasAksesLingkunganAdapter(Context c, ArrayList<AksesLingkungan> aksesLingkungans){
        this.c = c;
        this.aksesLingkungans = aksesLingkungans;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PrioritasAksesLingkunganAdapter.MyViewHolder(LayoutInflater.from(c).inflate(R.layout.singe_list_kriteria, parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.kriteria.setText(aksesLingkungans.get(position).getNama_akses());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("aksesLingkungan", aksesLingkungans.get(position));
                i.putExtra("fasilitasID", aksesLingkungans.get(position).getIdfasilitas());
                ((Activity)c).setResult(Activity.RESULT_OK, i);
                ((Activity)c).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return aksesLingkungans.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView kriteria;

        public MyViewHolder(View view){
            super(view);
            kriteria = (TextView) view.findViewById(R.id.kriteriaKost);
        }
    }

}
