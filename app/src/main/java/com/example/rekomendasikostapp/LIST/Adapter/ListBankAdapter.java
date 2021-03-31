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

import java.util.List;

public class ListBankAdapter extends RecyclerView.Adapter<ListBankAdapter.MyViewHolder> {

    private Context context;
    private List<String> listBank;

    public ListBankAdapter(Context c, List<String> listBank){
        this.context = c;
        this.listBank = listBank;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListBankAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.single_list_activity , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.nama_bank.setText(listBank.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("bank", listBank.get(position));
                ((Activity)context).setResult(Activity.RESULT_OK, i);
                ((Activity)context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listBank.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nama_bank;

        public MyViewHolder(View view){
            super(view);
            nama_bank = (TextView) view.findViewById(R.id.namaArray);
        }
    }

}
