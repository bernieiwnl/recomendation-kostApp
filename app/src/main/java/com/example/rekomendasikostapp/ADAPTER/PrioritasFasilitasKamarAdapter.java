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

import com.example.rekomendasikostapp.CLASS.FasilitasKamar;
import com.example.rekomendasikostapp.R;

import java.util.ArrayList;

public class PrioritasFasilitasKamarAdapter extends RecyclerView.Adapter<PrioritasFasilitasKamarAdapter.MyViewHolder> {

    private Context c;
    private ArrayList<FasilitasKamar> fasilitasKamars;

    public PrioritasFasilitasKamarAdapter(Context c, ArrayList<FasilitasKamar> fasilitasKamars){
        this.c = c;
        this.fasilitasKamars = fasilitasKamars;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PrioritasFasilitasKamarAdapter.MyViewHolder(LayoutInflater.from(c).inflate(R.layout.singe_list_kriteria, parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.kriteria.setText(fasilitasKamars.get(position).getNama_fasilitas());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("fasilitas", fasilitasKamars.get(position));
                i.putExtra("fasilitasID", fasilitasKamars.get(position).getIdfasilitas());
                ((Activity)c).setResult(Activity.RESULT_OK, i);
                ((Activity)c).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return fasilitasKamars.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView kriteria;

        public MyViewHolder(View view){
            super(view);
            kriteria = (TextView) view.findViewById(R.id.kriteriaKost);
        }
    }

}
