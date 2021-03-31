package com.example.rekomendasikostapp.LIST.Adapter;

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

import java.util.List;


public class ListKotaAdapter extends RecyclerView.Adapter<ListKotaAdapter.MyViewHolder> {

    private Context context;
    private List<String> listKota;

    public ListKotaAdapter(Context c, List<String> listKota){
        this.context = c;
        this.listKota = listKota;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListKotaAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.single_list_activity , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.nama_kota.setText(listKota.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("kota", listKota.get(position));
                ((Activity)context).setResult(Activity.RESULT_OK, i);
                ((Activity)context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listKota.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nama_kota;

        public MyViewHolder(View view){
            super(view);
            nama_kota = (TextView) view.findViewById(R.id.namaArray);
        }
    }
}
