package com.example.rekomendasikostapp.LIST;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;


import com.example.rekomendasikostapp.LIST.Adapter.ListBankAdapter;
import com.example.rekomendasikostapp.R;

import java.util.ArrayList;

public class ListBankActivity extends AppCompatActivity {

    private RecyclerView recyclerViewBank;
    private ArrayList<String> listBank;
    private ListBankAdapter listBankAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_bank);

        //create new list
        //new ArrayList
        listBank = new ArrayList<String>();

        //get object reference
        recyclerViewBank = (RecyclerView) findViewById(R.id.recylerBank);

        //set layout manager
        recyclerViewBank.setLayoutManager(new LinearLayoutManager(this));

        //get array list
        listBank.add("BCA");
        listBank.add("BNI");
        listBank.add("BRI");
        listBank.add("BTN");
        listBank.add("BTPN");
        listBank.add("DANAMON");
        listBank.add("Mandiri");


        //set to adapter
        listBankAdapter = new ListBankAdapter(ListBankActivity.this, listBank);
        recyclerViewBank.setAdapter(listBankAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerViewBank.addItemDecoration(dividerItemDecoration);
    }
}
