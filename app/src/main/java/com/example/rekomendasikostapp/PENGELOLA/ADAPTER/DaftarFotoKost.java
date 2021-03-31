package com.example.rekomendasikostapp.PENGELOLA.ADAPTER;

import android.content.Context;
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

import com.example.rekomendasikostapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DaftarFotoKost extends RecyclerView.Adapter<DaftarFotoKost.MyViewHolder> {

    private Context c;
    private ArrayList<String> fotoKosts;
    private OnItemClickListener clickListener;


    public DaftarFotoKost(Context c, ArrayList<String> fotoKosts){
        this.c = c;
        this.fotoKosts = fotoKosts;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DaftarFotoKost.MyViewHolder(LayoutInflater.from(c).inflate(R.layout.single_list_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(fotoKosts.get(position)).into(holder.icon);
    }

    @Override
    public int getItemCount() {
        return fotoKosts.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        ImageView icon;

        public MyViewHolder(View view){
            super(view);

            icon = (ImageView) view.findViewById(R.id.fotoKostIcon);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if(clickListener != null){
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    clickListener.onImageclick(position);
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
                            clickListener.onImageDeleteClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener{
        void onImageclick(int position);
        void onImageDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        clickListener = listener;
    }

}
