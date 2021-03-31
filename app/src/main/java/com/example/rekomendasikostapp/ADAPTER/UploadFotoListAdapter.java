package com.example.rekomendasikostapp.ADAPTER;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rekomendasikostapp.R;

import java.util.List;

public class UploadFotoListAdapter extends RecyclerView.Adapter<UploadFotoListAdapter.ViewHolder> {



    public List<Bitmap> fileImageList;

    public UploadFotoListAdapter(List<Bitmap> fileImageList){
        this.fileImageList = fileImageList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

            View mView;

            public ImageView fileImageView;

        public ViewHolder(View itemView){
            super(itemView);
            mView = itemView;

            fileImageView = (ImageView) mView.findViewById(R.id.fotoKostIcon);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_photo ,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UploadFotoListAdapter.ViewHolder holder, int position) {

        Bitmap imageBitmap = fileImageList.get(position);
        holder.fileImageView.setImageBitmap(imageBitmap);

    }

    @Override
    public int getItemCount() {
        return fileImageList.size();
    }
}
