package com.example.rekomendasikostapp.ADAPTER;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rekomendasikostapp.CLASS.Users;
import com.example.rekomendasikostapp.R;
import com.example.rekomendasikostapp.VERIFIKASIUSER.DetailVerifikasiUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VerifikasiUserAdapter extends RecyclerView.Adapter<VerifikasiUserAdapter.MyViewHolder> {

    ArrayList<Users> users;
    Context c;

    public VerifikasiUserAdapter(Context c, ArrayList<Users> users){
        this.c = c;
        this.users = users;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VerifikasiUserAdapter.MyViewHolder(LayoutInflater.from(c).inflate(R.layout.single_list_verifikasi_user, parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.namaLengkap.setText(users.get(position).getFull_name());
        holder.alamatEmail.setText(users.get(position).getEmail());
        holder.nomorTelepon.setText(users.get(position).getPhone());
        Picasso.get().load(users.get(position).getProfile_image_url()).into(holder.imageProfile);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c,  DetailVerifikasiUser.class);
                intent.putExtra("idUser", users.get(position).getIdUser());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.getApplicationContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageProfile;
        TextView namaLengkap;
        TextView alamatEmail;
        TextView nomorTelepon;

        public MyViewHolder(View view) {
            super(view);
            imageProfile = view.findViewById(R.id.profile_image);
            namaLengkap = view.findViewById(R.id.namaLengkap);
            alamatEmail = view.findViewById(R.id.alamatEmail);
            nomorTelepon = view.findViewById(R.id.nomorTelepon);

        }
    }

}
