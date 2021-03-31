package com.example.rekomendasikostapp.LIST;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.os.Bundle;

import com.example.rekomendasikostapp.LIST.Adapter.ListProvinsiAdapter;
import com.example.rekomendasikostapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListProvinsiActivity extends AppCompatActivity {

    RecyclerView recyclerViewProvinsi;
    List<String> listProvinsi;
    Resources res;
    ListProvinsiAdapter listProvinsiAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_provinsi);

        // get resource
        res = getResources();

        //new ArrayList
        listProvinsi = new ArrayList<String>();

        //get object reference
        recyclerViewProvinsi = (RecyclerView) findViewById(R.id.recyclerProvinsi);

        //set layout manager
        recyclerViewProvinsi.setLayoutManager(new LinearLayoutManager(this));

        //get array list
        listProvinsi = Arrays.asList(res.getStringArray(R.array.provinsi));

        //set to adapter
        listProvinsiAdapter = new ListProvinsiAdapter(ListProvinsiActivity.this, listProvinsi);
        recyclerViewProvinsi.setAdapter(listProvinsiAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerViewProvinsi.addItemDecoration(dividerItemDecoration);
    }
}
