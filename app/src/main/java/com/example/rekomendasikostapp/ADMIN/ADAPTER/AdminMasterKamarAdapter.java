package com.example.rekomendasikostapp.ADMIN.ADAPTER;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rekomendasikostapp.CLASS.FasilitasKamar;
import com.example.rekomendasikostapp.INSERTKOST.InsertKost_3;
import com.example.rekomendasikostapp.MASTERDATA.UpdateMasterDataKamar;
import com.example.rekomendasikostapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdminMasterKamarAdapter extends RecyclerView.Adapter<AdminMasterKamarAdapter.MyViewHolder> {

    private ArrayList<FasilitasKamar> fasilitasKamars;
    private Context context;
    private OnItemClickListener clickListener;

    public  AdminMasterKamarAdapter(Context c, ArrayList<FasilitasKamar> fasilitasKamars){
        this.fasilitasKamars = fasilitasKamars;
        this.context = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdminMasterKamarAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.admin_layout_fasilitas_kamar, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.nama_akses.setText(fasilitasKamars.get(position).getNama_fasilitas());
        Picasso.get().load(fasilitasKamars.get(position).getFoto_url()).into(holder.foto_url);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,  UpdateMasterDataKamar.class);
                intent.putExtra("idFasilitas", fasilitasKamars.get(position).getIdfasilitas());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.getApplicationContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fasilitasKamars.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener , View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        TextView nama_akses;
        ImageView foto_url;

        public MyViewHolder(View view){
            super(view);

            nama_akses = (TextView) view.findViewById(R.id.namaFasilitas);
            foto_url = (ImageView) view.findViewById(R.id.imageIcon);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if(clickListener != null){
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    clickListener.onKamarItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem Delete = menu.add(Menu.NONE, 1 , 1 , "Delete");

            Delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(clickListener != null){
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    switch (item.getItemId()){
                        case 1:
                            clickListener.onKamarDeleteClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener{
        void onKamarItemClick(int position);
        void onKamarDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        clickListener = listener;
    }


}
