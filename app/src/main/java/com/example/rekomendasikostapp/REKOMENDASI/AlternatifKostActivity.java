package com.example.rekomendasikostapp.REKOMENDASI;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.rekomendasikostapp.ADAPTER.AlternatifAdapter;
import com.example.rekomendasikostapp.CLASS.Alternatif;
import com.example.rekomendasikostapp.PENCARIAN.FilterKostActivity;
import com.example.rekomendasikostapp.R;
import com.github.clans.fab.FloatingActionButton;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AlternatifKostActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerViewKost;
    private ArrayList<Alternatif> alternatifs;
    private AlternatifAdapter alternatifAdapter;

    ArrayList<String> list_fasilitas_umum;
    ArrayList<String> list_fasilitas_kamar;
    ArrayList<String> list_akses_lingkungan;

    private FloatingActionButton btnFilter;

    boolean filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alternatif_kost);

        //get object reference
        Bundle bundleObject = getIntent().getExtras();
        recyclerViewKost = (RecyclerView) findViewById(R.id.recylerKost);
        btnFilter = (FloatingActionButton) findViewById(R.id.buttonFilter);


        // get serialize array from last activity
        alternatifs = (ArrayList<Alternatif>) bundleObject.getSerializable("alternatifs");

        //set layout manager
        recyclerViewKost.setLayoutManager(new LinearLayoutManager(this));

        //sort high to low
        Collections.sort(alternatifs, new Comparator<Alternatif>() {
            @Override
            public int compare(Alternatif o1, Alternatif o2) {
                return o2.getNilai_alternatif().compareTo(o1.getNilai_alternatif());
            }
        });


        //set adapter
        alternatifAdapter = new AlternatifAdapter(AlternatifKostActivity.this, alternatifs);
        recyclerViewKost.setAdapter(alternatifAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerViewKost.addItemDecoration(dividerItemDecoration);

        filter = false;

        btnFilter.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonFilter:
                startActivityForResult( new Intent(AlternatifKostActivity.this, FilterKostActivity.class), 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK)
        {
            final String hargaMin = data.getStringExtra("hargaMin");
            final String hargaMax = data.getStringExtra("hargaMax");
            String jenis_ukuran = data.getStringExtra("jenis_ukuran");
            String jenis_kost = data.getStringExtra("jenis_kost");

            list_fasilitas_kamar = new ArrayList<String>();
            list_fasilitas_umum = new ArrayList<String>();
            list_akses_lingkungan = new ArrayList<String>();

            list_akses_lingkungan = data.getStringArrayListExtra("list_akses_lingkungan");
            list_fasilitas_umum = data.getStringArrayListExtra("list_fasilitas_umum");
            list_fasilitas_kamar = data.getStringArrayListExtra("list_fasilitas_kamar");

            Log.d("FILTER", hargaMax);
            Log.d("FILTER", hargaMin);
            Log.d("FILTER", "" + jenis_kost);
            Log.d("FILTER", ""+ jenis_ukuran);
            Log.d("FILTER A", "" + list_akses_lingkungan.size());
            Log.d("FILTER B", "" + list_fasilitas_kamar.size());
            Log.d("FILTER C", "" + list_fasilitas_umum.size());


            // filter
            ArrayList<Alternatif> filterAlternatif = new ArrayList<>();
            filterAlternatif.clear();
            for(Alternatif dataAlternatif : alternatifs){
                if(dataAlternatif.getHarga() <= Integer.parseInt(hargaMax)
                        && dataAlternatif.getHarga() >= Integer.parseInt(hargaMin)
                        && dataAlternatif.getJenis_kost().equals(jenis_kost)
                        && dataAlternatif.getUkuran_kamar().equals(jenis_ukuran)
                        && dataAlternatif.getFasilitas_umums().containsAll(list_fasilitas_umum)
                        && dataAlternatif.getFasilitas_kamars().containsAll(list_fasilitas_kamar)
                        && dataAlternatif.getAkses_lingkungans().containsAll(list_akses_lingkungan)
                       ){
                    filterAlternatif.add(dataAlternatif);
                }
            }

            alternatifAdapter = new AlternatifAdapter(AlternatifKostActivity.this, filterAlternatif);
            recyclerViewKost.setAdapter(alternatifAdapter);
            filter = true;

        }

    }
}
