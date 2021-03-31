package com.example.rekomendasikostapp.LIST;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.os.Bundle;

import com.example.rekomendasikostapp.LIST.Adapter.ListKotaAdapter;
import com.example.rekomendasikostapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListKotaActivity extends AppCompatActivity {

    RecyclerView recyclerViewKota;
    List<String> listKota;
    Resources res;
    ListKotaAdapter listKotaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_kota);

        // get resource
        res = getResources();

        //new ArrayList
        listKota = new ArrayList<String>();

        //get object reference
        recyclerViewKota = (RecyclerView) findViewById(R.id.recylerKota);

        //set layout manager
        recyclerViewKota.setLayoutManager(new LinearLayoutManager(this));

        //get array list
        listKota = Arrays.asList(res.getStringArray(R.array.kota));

        //set to adapter
        listKotaAdapter = new ListKotaAdapter(ListKotaActivity.this, listKota);
        recyclerViewKota.setAdapter(listKotaAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerViewKota.addItemDecoration(dividerItemDecoration);


    }
}
