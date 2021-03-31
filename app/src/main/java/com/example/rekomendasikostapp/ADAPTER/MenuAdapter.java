package com.example.rekomendasikostapp.ADAPTER;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rekomendasikostapp.CLASS.Menu;
import com.example.rekomendasikostapp.LIST.Adapter.ListBankAdapter;
import com.example.rekomendasikostapp.LIST.Adapter.ListProvinsiAdapter;
import com.example.rekomendasikostapp.PREFERENSI.UbahAksesPreferensi;
import com.example.rekomendasikostapp.PREFERENSI.UbahKamarPreferensi;
import com.example.rekomendasikostapp.PREFERENSI.UbahKriteriaPreferensi;
import com.example.rekomendasikostapp.PREFERENSI.UbahUmumPreferensi;
import com.example.rekomendasikostapp.R;

import java.util.ArrayList;
import java.util.List;


public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Menu> listMenu;

    public MenuAdapter(Context c, ArrayList<Menu> listMenu){
        this.context = c;
        this.listMenu = listMenu;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MenuAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.list_menu_preferensi , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.namaMenu.setText(listMenu.get(position).getNamaMenu());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listMenu.get(position).getNamaMenu().equals("Preferensi Kriteria Kost")){
                    Intent i  = new Intent(context.getApplicationContext(), UbahKriteriaPreferensi.class);
                    context.startActivity(i);
                }
                if(listMenu.get(position).getNamaMenu().equals("Preferensi Kriteria Fasilitas Umum")){
                    Intent i  = new Intent(context.getApplicationContext(), UbahUmumPreferensi.class);
                    context.startActivity(i);
                }
                if(listMenu.get(position).getNamaMenu().equals("Preferensi Kriteria Fasilitas Kamar")){
                    Intent i  = new Intent(context.getApplicationContext(), UbahKamarPreferensi.class);
                    context.startActivity(i);
                }
                if(listMenu.get(position).getNamaMenu().equals("Preferensi Kriteria Akses Lokasi")){
                    Intent i  = new Intent(context.getApplicationContext(), UbahAksesPreferensi.class);
                    context.startActivity(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listMenu.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView namaMenu;

        public MyViewHolder(View view){
            super(view);
            namaMenu = (TextView) view.findViewById(R.id.namaMenu);
        }
    }


}
