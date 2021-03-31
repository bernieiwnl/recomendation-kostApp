package com.example.rekomendasikostapp.ADAPTER;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rekomendasikostapp.R;

import java.util.ArrayList;

public class KriteriaAdapter extends RecyclerView.Adapter<KriteriaAdapter.MyViewHolder> {

    private Context c;
    private ArrayList<String> kriterias;

    public KriteriaAdapter(Context c, ArrayList<String> kriterias){
        this.c = c;
        this.kriterias = kriterias;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new KriteriaAdapter.MyViewHolder(LayoutInflater.from(c).inflate(R.layout.singe_list_kriteria, parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.kriteria.setText(kriterias.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("fasilitas", kriterias.get(position));
                ((Activity)c).setResult(Activity.RESULT_OK, i);
                ((Activity)c).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return kriterias.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView kriteria;

        public MyViewHolder(View view){
            super(view);
            kriteria = (TextView) view.findViewById(R.id.kriteriaKost);
        }
    }

    public ArrayList<String> getArrayList(){
        return kriterias;
    }
}
