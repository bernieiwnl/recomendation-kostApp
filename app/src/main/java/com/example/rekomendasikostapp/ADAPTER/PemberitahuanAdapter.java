package com.example.rekomendasikostapp.ADAPTER;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rekomendasikostapp.CLASS.Pemberitahuan;
import com.example.rekomendasikostapp.PEMBERITAHUAN.ADMIN.PemberitahuanIklanBaru;
import com.example.rekomendasikostapp.PEMBERITAHUAN.ADMIN.PemberitahuanKeluhan;
import com.example.rekomendasikostapp.PEMBERITAHUAN.ADMIN.PemberitahuanPengelola;
import com.example.rekomendasikostapp.PEMBERITAHUAN.ADMIN.PemberitahuanPengelolaBaru;
import com.example.rekomendasikostapp.PEMBERITAHUAN.PENGELOLA.PemberitahuanBuktiPembayaran;
import com.example.rekomendasikostapp.PEMBERITAHUAN.PENGELOLA.PemberitahuanPenghuniBaru;
import com.example.rekomendasikostapp.PEMBERITAHUAN.PENGELOLA.PemberitahuanSuspendKost;
import com.example.rekomendasikostapp.PEMBERITAHUAN.PENGGUNA.PemberitahuanKirimPembayaran;
import com.example.rekomendasikostapp.PEMBERITAHUAN.PENGELOLA.PemberitahuanPemesanan;
import com.example.rekomendasikostapp.PEMBERITAHUAN.PENGGUNA.PemberitahuanPemesananBerhasil;
import com.example.rekomendasikostapp.PEMBERITAHUAN.PENGGUNA.PemberitahuanTransfer;
import com.example.rekomendasikostapp.PEMBERITAHUAN.PENGELOLA.PemberitahuanVerifikasiKost;
import com.example.rekomendasikostapp.PEMBERITAHUAN.UNIVERSAL.PemberitahuanPembatalanPemesanan;
import com.example.rekomendasikostapp.R;

import java.util.ArrayList;

public class PemberitahuanAdapter extends RecyclerView.Adapter<PemberitahuanAdapter.MyViewHolder> {

    private ArrayList<Pemberitahuan> pemberitahuans;
    private Context context;

    public PemberitahuanAdapter(Context c,ArrayList<Pemberitahuan> pemberitahuans){
        this.pemberitahuans = pemberitahuans;
        this.context = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PemberitahuanAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.single_list_pemberitahuan, parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
            holder.judul.setText(pemberitahuans.get(position).getJudul());
            holder.deskripsi.setText(pemberitahuans.get(position).getDeskripsi());

            if(pemberitahuans.get(position).getStatus().equals("read")){
                holder.judul.setTypeface(Typeface.DEFAULT);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pemberitahuans.get(position).getJenis_pemberitahuan().equals("Verifikasi Kost")){
                        Intent intent = new Intent(context,  PemberitahuanVerifikasiKost.class);
                        intent.putExtra("idKost", pemberitahuans.get(position).getIdKost());
                        intent.putExtra("idPemberitahuan" , pemberitahuans.get(position).getIdPemberitahuan());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.getApplicationContext().startActivity(intent);
                    }
                    else if(pemberitahuans.get(position).getJenis_pemberitahuan().equals("Pemesanan")){
                        Intent intent = new Intent(context,  PemberitahuanPemesanan.class);
                        intent.putExtra("idKost", pemberitahuans.get(position).getIdKost());
                        intent.putExtra("idPemberitahuan" , pemberitahuans.get(position).getIdPemberitahuan());
                        intent.putExtra("idPemesanan", pemberitahuans.get(position).getIdPemesanan());
                        intent.putExtra("idUser", pemberitahuans.get(position).getIdUser());
                        intent.putExtra("idPengelola", pemberitahuans.get(position).getIdSender());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.getApplicationContext().startActivity(intent);
                    }
                    else if(pemberitahuans.get(position).getJenis_pemberitahuan().equals("Iklan Baru")){
                        Intent intent = new Intent(context,  PemberitahuanIklanBaru.class);
                        intent.putExtra("idKost", pemberitahuans.get(position).getIdKost());
                        intent.putExtra("idPemberitahuan" , pemberitahuans.get(position).getIdPemberitahuan());
                        intent.putExtra("idUser", pemberitahuans.get(position).getIdSender());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.getApplicationContext().startActivity(intent);
                    }
                    else if(pemberitahuans.get(position).getJenis_pemberitahuan().equals("Pembayaran User")){
                        Intent intent = new Intent(context,  PemberitahuanTransfer.class);
                        intent.putExtra("idKost", pemberitahuans.get(position).getIdKost());
                        intent.putExtra("idPemberitahuan" , pemberitahuans.get(position).getIdPemberitahuan());
                        intent.putExtra("idPemesanan", pemberitahuans.get(position).getIdPemesanan());
                        intent.putExtra("idUser", pemberitahuans.get(position).getIdUser());
                        intent.putExtra("idPengelola", pemberitahuans.get(position).getIdSender());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.getApplicationContext().startActivity(intent);
                    }
                    else if(pemberitahuans.get(position).getJenis_pemberitahuan().equals("Kirim Pembayaran")){
                        Intent intent = new Intent(context,  PemberitahuanKirimPembayaran.class);
                        intent.putExtra("idKost", pemberitahuans.get(position).getIdKost());
                        intent.putExtra("idPemberitahuan" , pemberitahuans.get(position).getIdPemberitahuan());
                        intent.putExtra("idPemesanan", pemberitahuans.get(position).getIdPemesanan());
                        intent.putExtra("idUser", pemberitahuans.get(position).getIdUser());
                        intent.putExtra("idPengelola", pemberitahuans.get(position).getIdSender());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.getApplicationContext().startActivity(intent);
                    }
                    else if(pemberitahuans.get(position).getJenis_pemberitahuan().equals("Terima Pembayaran")){
                        Intent intent = new Intent(context,  PemberitahuanBuktiPembayaran.class);
                        intent.putExtra("idKost", pemberitahuans.get(position).getIdKost());
                        intent.putExtra("idPemberitahuan" , pemberitahuans.get(position).getIdPemberitahuan());
                        intent.putExtra("idPemesanan", pemberitahuans.get(position).getIdPemesanan());
                        intent.putExtra("idUser", pemberitahuans.get(position).getIdSender());
                        intent.putExtra("idPengelola", pemberitahuans.get(position).getIdSender());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.getApplicationContext().startActivity(intent);
                    }
                    else if(pemberitahuans.get(position).getJenis_pemberitahuan().equals("Verifikasi Penghuni Kost")){
                        Intent intent = new Intent(context,  PemberitahuanPenghuniBaru.class);
                        intent.putExtra("idPemberitahuan" , pemberitahuans.get(position).getIdPemberitahuan());
                        intent.putExtra("idSender", pemberitahuans.get(position).getIdSender());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.getApplicationContext().startActivity(intent);
                    }
                    else if(pemberitahuans.get(position).getJenis_pemberitahuan().equals("Verifikasi Pemesanan")){
                        Intent intent = new Intent(context,  PemberitahuanPemesananBerhasil.class);
                        intent.putExtra("idPemberitahuan" , pemberitahuans.get(position).getIdPemberitahuan());
                        intent.putExtra("idSender", pemberitahuans.get(position).getIdSender());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.getApplicationContext().startActivity(intent);
                    }
                    else if(pemberitahuans.get(position).getJenis_pemberitahuan().equals("Pembatalan Pemesanan")){
                        Intent intent = new Intent(context,  PemberitahuanPembatalanPemesanan.class);
                        intent.putExtra("idPemberitahuan" , pemberitahuans.get(position).getIdPemberitahuan());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.getApplicationContext().startActivity(intent);
                    }
                    else if(pemberitahuans.get(position).getJenis_pemberitahuan().equals("Keluhan Pengguna")){
                        Intent intent = new Intent(context,  PemberitahuanKeluhan.class);
                        intent.putExtra("idKost", pemberitahuans.get(position).getIdKost());
                        intent.putExtra("idUser", pemberitahuans.get(position).getIdSender());
                        intent.putExtra("idLaporan", pemberitahuans.get(position).getIdLaporan());
                        intent.putExtra("idPemberitahuan" , pemberitahuans.get(position).getIdPemberitahuan());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.getApplicationContext().startActivity(intent);
                    }
                    else if(pemberitahuans.get(position).getJenis_pemberitahuan().equals("Peringatan Kost")){
                        Intent intent = new Intent(context,  PemberitahuanSuspendKost.class);
                        intent.putExtra("idPemberitahuan" , pemberitahuans.get(position).getIdPemberitahuan());
                        intent.putExtra("idSender", pemberitahuans.get(position).getIdSender());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.getApplicationContext().startActivity(intent);
                    }
                    else if(pemberitahuans.get(position).getJenis_pemberitahuan().equals("Verifikasi Data Pengelola Sukses")){
                        Intent intent = new Intent(context,  PemberitahuanPengelola.class);
                        intent.putExtra("idPemberitahuan" , pemberitahuans.get(position).getIdPemberitahuan());
                        intent.putExtra("idSender", pemberitahuans.get(position).getIdSender());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.getApplicationContext().startActivity(intent);
                    }
                    else if(pemberitahuans.get(position).getJenis_pemberitahuan().equals("Verifikasi Data Pengelola")){
                        Intent intent = new Intent(context,  PemberitahuanPengelolaBaru.class);
                        intent.putExtra("idPemberitahuan" , pemberitahuans.get(position).getIdPemberitahuan());
                        intent.putExtra("idSender", pemberitahuans.get(position).getIdSender());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.getApplicationContext().startActivity(intent);
                    }
                }
            });

    }

    @Override
    public int getItemCount() {
        return pemberitahuans.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView judul;
        TextView deskripsi;

        public MyViewHolder(View view){
            super(view);
            judul = (TextView) view.findViewById(R.id.judul);
            deskripsi = (TextView) view.findViewById(R.id.deskrpsi);
        }
    }


}
