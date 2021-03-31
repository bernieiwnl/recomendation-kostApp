package com.example.rekomendasikostapp.VERIFICATION.ADAPTER;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rekomendasikostapp.CLASS.Kost;
import com.example.rekomendasikostapp.R;
import com.example.rekomendasikostapp.VERIFICATION.KostVerificationDetailActivity;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class KostVerificationAdapter extends RecyclerView.Adapter<KostVerificationAdapter.MyViewHolder> {

    private Context c;
    private ArrayList<Kost> kosts;

    public KostVerificationAdapter(Context c, ArrayList<Kost> kosts){
        this.kosts = kosts;
        this.c = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new KostVerificationAdapter.MyViewHolder(LayoutInflater.from(c).inflate(R.layout.single_list_verifikasi_kost, parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        //currency format
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        holder.alamatKost.setText(kosts.get(position).getAlamat() + ", " + kosts.get(position).getKota() + ", " + kosts.get(position).getProvinsi() + " " + kosts.get(position).getKode_pos());
        holder.jenisKost.setText(kosts.get(position).getJenis_kost());
        holder.jumlahKamar.setText("Ada " + kosts.get(position).getJumlah_kamar() + " Kamar");
        holder.sisaKamar.setText("Sisa " + kosts.get(position).getSisa_kamar() + " Kamar");
        holder.hargaKost.setText(formatRupiah.format((double)kosts.get(position).getHarga()) + " / " + kosts.get(position).getJenis_sewa());

        if(kosts.get(position).isVerification())
        {
            holder.status.setText("Sudah Terverifikasi");
            holder.status.setTextColor(Color.parseColor("#00600f"));
        }
        else{
            holder.status.setText("Belum Terverifikasi");
            holder.status.setTextColor(Color.parseColor("#8c0032"));
        }

        if(kosts.get(position).getSisa_kamar() < 3)
        {
            holder.sisa.setBackgroundColor(Color.parseColor("#d32f2f"));
        }

        if(kosts.get(position).getJenis_kost().equals("Perempuan"))
        {
            holder.jenis.setBackgroundColor(Color.parseColor("#c2185b"));
        }

        for(int i = 0; i < kosts.get(position).getFoto_kost().size();i++){
            if(i == 0){
                Picasso.get().load(kosts.get(position).getFoto_kost().get(i)).into(holder.gambarKost);
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c,  KostVerificationDetailActivity.class);
                intent.putExtra("idKost", kosts.get(position).getIdKost());
                intent.putExtra("idUser", kosts.get(position).getIdUser());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.getApplicationContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return kosts.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView gambarKost;
        TextView alamatKost;
        TextView jenisKost;
        TextView hargaKost;
        TextView sisaKamar;
        TextView jumlahKamar;
        TextView status;

        RelativeLayout jenis;
        RelativeLayout sisa;

        public MyViewHolder(View view){
            super(view);


            gambarKost = (ImageView) view.findViewById(R.id.gambarKost);
            alamatKost = (TextView) view.findViewById(R.id.alamatKost);
            jenisKost = (TextView) view.findViewById(R.id.jenisKost);
            hargaKost = (TextView) view.findViewById(R.id.hargaKost);
            sisaKamar = (TextView) view.findViewById(R.id.sisaKamar);
            jumlahKamar = (TextView) view.findViewById(R.id.jumlahKamar);
            jenis = (RelativeLayout) view.findViewById(R.id.jenis);
            sisa = (RelativeLayout) view.findViewById(R.id.sisa);
            status = (TextView) view.findViewById(R.id.status);

        }

    }

}
