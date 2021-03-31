package com.example.rekomendasikostapp.LIST.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rekomendasikostapp.R;

import java.util.ArrayList;
import java.util.List;

public class ListProvinsiAdapter extends RecyclerView.Adapter<ListProvinsiAdapter.MyViewHolder> {

    private Context context;
    private List<String> listProvinsi;

    public ListProvinsiAdapter(Context context, List<String> listProvinsi){
         this.context = context;
         this.listProvinsi = listProvinsi;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListProvinsiAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.single_list_activity, parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
         holder.nama_provinsi.setText(listProvinsi.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("provinsi", listProvinsi.get(position));
                ((Activity)context).setResult(Activity.RESULT_OK, i);
                ((Activity)context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listProvinsi.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nama_provinsi;

        public MyViewHolder(View view){
            super(view);
            nama_provinsi = (TextView) view.findViewById(R.id.namaArray);
        }
    }
}
