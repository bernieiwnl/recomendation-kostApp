package com.example.rekomendasikostapp.ADAPTER;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.rekomendasikostapp.CLASS.FotoKost;
import com.example.rekomendasikostapp.R;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageSliderAdapter extends SliderViewAdapter<ImageSliderAdapter.SliderAdapterVH> {

    private Context context;
    private ArrayList<String> fotoKosts;

    public ImageSliderAdapter(Context c, ArrayList<String> fotoKosts){
        this.context = c;
        this.fotoKosts = fotoKosts;
    }

    public void renewItems(ArrayList<String> fotoKosts){
        this.fotoKosts = fotoKosts;
        notifyDataSetChanged();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image_slider ,parent,false);
        return new SliderAdapterVH(v);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
        Picasso.get().load(fotoKosts.get(position)).into(viewHolder.imageViewBackground);
    }

    @Override
    public int getCount() {
        return fotoKosts.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.imageFotoKost);
            this.itemView = itemView;
        }
    }

}
