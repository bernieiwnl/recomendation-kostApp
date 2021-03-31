package com.example.rekomendasikostapp.ADAPTER;

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
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rekomendasikostapp.CLASS.Kost;
import com.example.rekomendasikostapp.CLASS.Pemesanan;
import com.example.rekomendasikostapp.MAINKOST.DetailKostPenggunaActivity;
import com.example.rekomendasikostapp.PEMESANAN.DetailPemesananPengguna;
import com.example.rekomendasikostapp.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class HistoryPemesananAdapter extends RecyclerView.Adapter<HistoryPemesananAdapter.MyViewHolder> {

    Context c;
    ArrayList<Pemesanan> pemesanans;

    Integer txtsisaKamar;
    String txtjenisKost;
    String txtimageKost;
    Integer txtjumlahKamar;


    public HistoryPemesananAdapter(Context c, ArrayList<Pemesanan> pemesanans){
        this.c = c;
        this.pemesanans = pemesanans;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HistoryPemesananAdapter.MyViewHolder(LayoutInflater.from(c).inflate(R.layout.single_list_pemesanan_kost, parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        //get data kost
        setKost(holder.gambarKost , holder.jenisKost, holder.sisaKamar , holder.jumlahKamar, holder.jenis , holder.sisa, pemesanans.get(position).getIdKost());

        holder.statusPemesanan.setText(pemesanans.get(position).getStatus());
        holder.namaPemesan.setText(pemesanans.get(position).getNama_pemesan());
        SimpleDateFormat format = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        holder.tanggalKedatangan.setText(format.format(pemesanans.get(position).getTanggal_kedatangan()));
        holder.durasiPemesanan.setText(pemesanans.get(position).getDurasi_penyewaan());

        if(pemesanans.get(position).getStatus().equals("Menunggu Konfirmasi")){
            holder.status.setBackgroundColor(Color.parseColor("#005cb2"));
        }
        else if(pemesanans.get(position).getStatus().equals("Sudah Konfirmasi")){
            holder.status.setBackgroundColor(Color.parseColor("#4b830d"));
        }
        else if(pemesanans.get(position).getStatus().equals("Pemesanan Dibatalkan")){
            holder.status.setBackgroundColor(Color.parseColor("#9a0007"));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c,  DetailPemesananPengguna.class);
                intent.putExtra("idKost", pemesanans.get(position).getIdKost());
                intent.putExtra("idPemesanan" , pemesanans.get(position).getIdPemesanan());
                intent.putExtra("idUser" , pemesanans.get(position).getIdUser());
                intent.putExtra("idPengelola" , pemesanans.get(position).getIdPengelola());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.getApplicationContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return pemesanans.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView gambarKost;
        TextView jenisKost;
        TextView sisaKamar;
        TextView jumlahKamar;
        TextView namaPemesan;
        TextView tanggalKedatangan;
        TextView durasiPemesanan;
        TextView statusPemesanan;

        RelativeLayout jenis;
        RelativeLayout sisa;
        RelativeLayout status;

        public MyViewHolder(View view) {
            super(view);

            namaPemesan = (TextView) view.findViewById(R.id.namaPemesan);
            tanggalKedatangan = (TextView) view.findViewById(R.id.tanggalKedatangan);
            durasiPemesanan = (TextView) view.findViewById(R.id.durasiPemesanan);
            statusPemesanan = (TextView) view.findViewById(R.id.statusPemesanan);

            gambarKost = (ImageView) view.findViewById(R.id.imageKost);
            jenisKost = (TextView) view.findViewById(R.id.jenisKost);
            sisaKamar = (TextView) view.findViewById(R.id.sisaKamar);
            jumlahKamar = (TextView) view.findViewById(R.id.jumlahKamar);
            jenis = (RelativeLayout) view.findViewById(R.id.jenis);
            sisa = (RelativeLayout) view.findViewById(R.id.sisa);
            status = (RelativeLayout) view.findViewById(R.id.status);
        }
    }

    private void setKost(final ImageView gambarKost, final TextView jenisKost, final TextView sisaKamar, final TextView jumlahKamar, final RelativeLayout jenis, final RelativeLayout sisa, String idKost){

        final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        txtsisaKamar = 0;
        txtjenisKost = "default";
        txtimageKost = "default";
        txtjumlahKamar = 0;

        firebaseFirestore.collection("data_kost").document(idKost).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                Kost dataKost = documentSnapshot.toObject(Kost.class);

                txtjenisKost = dataKost.getJenis_kost();
                txtsisaKamar = dataKost.getSisa_kamar();
                txtjumlahKamar = dataKost.getJumlah_kamar();
                txtimageKost = dataKost.getFoto_kost().get(0);

                for(int i = 0; i < dataKost.getFoto_kost().size();i++){
                    if(i == 0){
                        txtimageKost = dataKost.getFoto_kost().get(i);
                    }
                }

                if(dataKost.getSisa_kamar() < 3)
                {
                    sisa.setBackgroundColor(Color.parseColor("#d32f2f"));
                }

                if(dataKost.getJenis_kost().equals("Perempuan"))
                {
                    jenis.setBackgroundColor(Color.parseColor("#c2185b"));
                }

                switch (txtimageKost){
                    case "default":
                        gambarKost.setVisibility(View.GONE);
                        break;
                    default:
                        Picasso.get().load(txtimageKost).into(gambarKost);
                        break;
                }

                switch (txtsisaKamar){
                    case 0:
                        sisaKamar.setText("Sisa " + 0 + " Kamar" );
                        break;
                    default:
                        sisaKamar.setText("Sisa " + txtsisaKamar + " Kamar");
                        break;
                }
                switch (txtjumlahKamar){
                    case 0:
                        jumlahKamar.setText("Ada  " + 0 + " Kamar" );
                        break;
                    default:
                        jumlahKamar.setText("Ada  " + txtjumlahKamar + " Kamar");
                        break;
                }
                switch (txtjenisKost){
                    case "default":
                        jenisKost.setVisibility(View.GONE);
                        break;
                    default:
                        jenisKost.setText(txtjenisKost);
                        break;
                }
            }
        });
    }


}
