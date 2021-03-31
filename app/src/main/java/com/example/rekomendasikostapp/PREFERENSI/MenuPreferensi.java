package com.example.rekomendasikostapp.PREFERENSI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.rekomendasikostapp.ADAPTER.MenuAdapter;
import com.example.rekomendasikostapp.CLASS.Menu;
import com.example.rekomendasikostapp.LIST.Adapter.ListKotaAdapter;
import com.example.rekomendasikostapp.LIST.ListKotaActivity;
import com.example.rekomendasikostapp.R;
import com.mapbox.mapboxsdk.plugins.annotation.Line;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MenuPreferensi extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout menuPreferensiKriteria;
    private LinearLayout menuPreferensiKriteriaFasilitasUmum;
    private LinearLayout menuPreferensiKriteriaFasilitasKamar;
    private LinearLayout menuPreferensiKriteriaFasilitasAkses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_preferensi);

        menuPreferensiKriteria = (LinearLayout) findViewById(R.id.menuPreferensiKriteriaKost);
        menuPreferensiKriteriaFasilitasUmum = (LinearLayout) findViewById(R.id.menuPreferensiKriteriaFasilitasUmum);
        menuPreferensiKriteriaFasilitasKamar = (LinearLayout) findViewById(R.id.menuPreferensiKriteriaFasilitasKamar);
        menuPreferensiKriteriaFasilitasAkses = (LinearLayout) findViewById(R.id.menuPreferensiKriteriaFasilitasAksesLokasi);

        menuPreferensiKriteria.setOnClickListener(this);
        menuPreferensiKriteriaFasilitasUmum.setOnClickListener(this);
        menuPreferensiKriteriaFasilitasKamar.setOnClickListener(this);
        menuPreferensiKriteriaFasilitasAkses.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.menuPreferensiKriteriaKost:
                Intent a  = new Intent(getApplicationContext(), UbahKriteriaPreferensi.class);
                startActivity(a);
                break;
            case R.id.menuPreferensiKriteriaFasilitasUmum:
                Intent b  = new Intent(getApplicationContext(), UbahUmumPreferensi.class);
                startActivity(b);
                break;

            case R.id.menuPreferensiKriteriaFasilitasKamar:
                Intent c  = new Intent(getApplicationContext(), UbahKamarPreferensi.class);
                startActivity(c);
                break;

            case R.id.menuPreferensiKriteriaFasilitasAksesLokasi:
                Intent d  = new Intent(getApplicationContext(), UbahAksesPreferensi.class);
                startActivity(d);
                break;
        }
    }
}