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

import com.example.rekomendasikostapp.CLASS.AksesLingkungan;
import com.example.rekomendasikostapp.MASTERDATA.UpdateMasterDataKamar;
import com.example.rekomendasikostapp.MASTERDATA.UpdateMasterDataLingkungan;
import com.example.rekomendasikostapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdminMasterLingkunganAdapter extends RecyclerView.Adapter<AdminMasterLingkunganAdapter.MyViewHolder>{

    private ArrayList<AksesLingkungan> aksesLingkungans;
    private Context context;
    private OnItemClickListener clickListener;

    public  AdminMasterLingkunganAdapter(Context c, ArrayList<AksesLingkungan> aksesLingkungans){
        this.aksesLingkungans = aksesLingkungans;
        this.context = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdminMasterLingkunganAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.admin_layout_akses_lingkungan , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.nama_akses.setText(aksesLingkungans.get(position).getNama_akses());
        Picasso.get().load(aksesLingkungans.get(position).getFoto_url()).into(holder.foto_url);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,  UpdateMasterDataLingkungan.class);
                intent.putExtra("idFasilitas", aksesLingkungans.get(position).getIdfasilitas());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.getApplicationContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return aksesLingkungans.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener , MenuItem.OnMenuItemClickListener {

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
                    clickListener.onAksesItemClick(position);
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
                            clickListener.onAksesDeleteClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener{
        void onAksesItemClick(int position);
        void onAksesDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        clickListener = listener;
    }

}
