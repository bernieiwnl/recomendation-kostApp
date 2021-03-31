package com.example.rekomendasikostapp.PENGELOLA.UPDATEKOST;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.rekomendasikostapp.CLASS.Kost;
import com.example.rekomendasikostapp.INSERTKOST.InsertKost_2;
import com.example.rekomendasikostapp.LIST.ListBankActivity;
import com.example.rekomendasikostapp.LIST.ListKotaActivity;
import com.example.rekomendasikostapp.LIST.ListProvinsiActivity;
import com.example.rekomendasikostapp.PENGELOLA.DetailKostPengelola;
import com.example.rekomendasikostapp.R;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class UpdateDataKostActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseFirestore firebaseFirestore;
    private TextInputEditText editNamaPengelola, editNomorTelepon, editHargaKost, editJumlahKamarKost, editSisaKamarKost, editKeteranganKost, editAlamatLengkapKost, editKotaKost, editProvinsiKost, editKodePos, editJenisBank, editNoRek, editAtasNama;;
    private RadioGroup radioGroup;
    private android.widget.RadioGroup radioSewa;
    private RadioGroup radioUkuran;
    private String jenis_kost;
    private String jenis_sewa;
    private String ukuran_kamar;
    private NestedScrollView scrollView;
    private String idKost;
    private com.github.clans.fab.FloatingActionButton floatingActionButtonSave;

    private RadioButton radioButtonLakiLaki,radioButtonPerempuan;
    private RadioButton radioButtonTahun, radioButtonSemester, radioButtonBulan;
    private RadioButton radioButton2x3, radioButton3x3, radioButton3x4, radioButton4x4, radioButton4x5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data_kost);

        //get string extra
        idKost = getIntent().getStringExtra("idKost");

        //get object reference
        editNamaPengelola = (TextInputEditText) findViewById(R.id.editNamaPengelolaKost);
        editNomorTelepon =  (TextInputEditText) findViewById(R.id.editNomorPengelolaKost);
        editHargaKost = (TextInputEditText) findViewById(R.id.editHargaKamarKost);
        editJumlahKamarKost = (TextInputEditText) findViewById(R.id.editJumlahKamarKost);
        editSisaKamarKost = (TextInputEditText) findViewById(R.id.editSisaKamarKost);
        editKeteranganKost = (TextInputEditText) findViewById(R.id.editKeteranganKost);
        editAlamatLengkapKost = (TextInputEditText) findViewById(R.id.editAlamatKost);
        editKotaKost = (TextInputEditText) findViewById(R.id.editKotaKost);
        editProvinsiKost = (TextInputEditText) findViewById(R.id.editProvinsiKost);
        editKodePos = (TextInputEditText) findViewById(R.id.editKodePos);
        editKodePos = (TextInputEditText) findViewById(R.id.editKodePos);
        editNoRek = (TextInputEditText) findViewById(R.id.editNomorRekening);
        editJenisBank = (TextInputEditText) findViewById(R.id.editJenisBank);
        editAtasNama = (TextInputEditText) findViewById(R.id.editAtasNama);

        radioGroup = (RadioGroup) findViewById(R.id.radioJenis);
        radioSewa = (RadioGroup) findViewById(R.id.jenisSewa);
        radioUkuran = (RadioGroup) findViewById(R.id.ukuranKamar);
        radioButtonPerempuan = (RadioButton) findViewById(R.id.radioPerempuan);
        radioButtonLakiLaki = (RadioButton) findViewById(R.id.radioLaki);
        radioButtonTahun = (RadioButton) findViewById(R.id.sewaTahun);
        radioButtonBulan = (RadioButton) findViewById(R.id.sewaBulanan);
        radioButtonSemester = (RadioButton) findViewById(R.id.sewaSemester);

        radioButton2x3 = findViewById(R.id.ukuran2x3);
        radioButton3x4 = findViewById(R.id.ukuran3x4);
        radioButton3x3 = findViewById(R.id.ukuran3x3);
        radioButton4x4 = findViewById(R.id.ukuran4x4);
        radioButton4x5 = findViewById(R.id.ukuran4x5);

        floatingActionButtonSave = (FloatingActionButton) findViewById(R.id.floatActionButtonSave);

        //setonclicklistener
        floatingActionButtonSave.setOnClickListener(this);
        editKotaKost.setOnClickListener(this);
        editProvinsiKost.setOnClickListener(this);
        editJenisBank.setOnClickListener(this);


        //get instance firebase firestore
        firebaseFirestore = FirebaseFirestore.getInstance();

        //get data from firebase firestore
        firebaseFirestore.collection("data_kost").document(idKost).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Kost dataKost = documentSnapshot.toObject(Kost.class);
                    if(dataKost.getJenis_kost().equals("Laki - Laki")){
                        radioButtonLakiLaki.setChecked(true);
                    }
                    else if(dataKost.getJenis_kost().equals("Perempuan")){
                        radioButtonPerempuan.setChecked(true);
                    }

                    if(dataKost.getJenis_sewa().equals("Tahun")){
                        radioButtonTahun.setChecked(true);
                    }
                    else if(dataKost.getJenis_sewa().equals("Semester")){
                        radioButtonSemester.setChecked(true);
                    }
                    else if(dataKost.getJenis_sewa().equals("Bulan")){
                        radioButtonBulan.setChecked(true);
                    }

                    if(dataKost.getUkuran_kamar().equals("2m x 3m"))
                    {
                        radioButton2x3.setChecked(true);
                    }
                    else if(dataKost.getUkuran_kamar().equals("3m x 3m")){
                        radioButton3x3.setChecked(true);
                    }
                    else if(dataKost.getUkuran_kamar().equals("3m x 4m")){
                        radioButton3x4.setChecked(true);
                    }
                    else if(dataKost.getUkuran_kamar().equals("4m x 4m")){
                        radioButton4x4.setChecked(true);
                    }
                    else if(dataKost.getUkuran_kamar().equals("4m x 5m")){
                        radioButton4x5.setChecked(true);
                    }

                    editHargaKost.setText(dataKost.getHarga().toString());
                    editJumlahKamarKost.setText(dataKost.getJumlah_kamar().toString());
                    editSisaKamarKost.setText(dataKost.getSisa_kamar().toString());
                    editKeteranganKost.setText(dataKost.getKeterangan());
                    editNamaPengelola.setText(dataKost.getNama_pengelola());
                    editNomorTelepon.setText(dataKost.getNomor_telepon());
                    editAlamatLengkapKost.setText(dataKost.getAlamat());
                    editKotaKost.setText(dataKost.getKota());
                    editProvinsiKost.setText(dataKost.getProvinsi());
                    editKodePos.setText(dataKost.getKode_pos().toString());
                    editNoRek.setText(dataKost.getNomor_rekening());
                    editJenisBank.setText(dataKost.getNama_bank());
                    editAtasNama.setText(dataKost.getNama_rekening());

                }


            }
        });


        //get selected radio button
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioLaki:
                        //your code here
                        jenis_kost = "Laki - Laki";
                        break;
                    case R.id.radioPerempuan:
                        //your code here
                        jenis_kost = "Perempuan";
                        break;
                }
            }
        });

        radioSewa.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.sewaTahun:
                        //your code here
                        jenis_sewa = "Tahun";
                        break;
                    case R.id.sewaSemester:
                        //your code here
                        jenis_sewa = "Semester";
                        break;
                    case R.id.sewaBulanan:
                        //your code here
                        jenis_sewa = "Bulan";
                        break;
                }
            }
        });

        radioUkuran.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.ukuran2x3:
                        //your code here
                        ukuran_kamar = "2m x 3m";
                        break;
                    case R.id.ukuran3x3:
                        //your code here
                        ukuran_kamar = "3m x 3m";
                        break;
                    case R.id.ukuran3x4:
                        //your code here
                        ukuran_kamar = "3m x 4m";
                        break;
                    case R.id.ukuran4x4:
                        //your code here
                        ukuran_kamar = "4m x 4m";
                        break;
                    case R.id.ukuran4x5:
                        //your code here
                        ukuran_kamar = "4m x 5m";
                        break;
                }
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.floatActionButtonSave:
                insertDataKost();
                break;
            case R.id.editProvinsiKost:
                startActivityForResult( new Intent(getApplicationContext(), ListProvinsiActivity.class), 1);
                break;
            case R.id.editKotaKost:
                startActivityForResult( new Intent(getApplicationContext(), ListKotaActivity.class),2);
                break;
            case R.id.editJenisBank:
                startActivityForResult( new Intent(getApplicationContext(), ListBankActivity.class),3);
                break;
        }
    }

    void insertDataKost(){

        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String nama_pengelola = editNamaPengelola.getText().toString().trim();
        String nomor_telepon = editNomorTelepon.getText().toString().trim();
        Integer harga_kost = Integer.parseInt(editHargaKost.getText().toString().trim());
        Integer jumlah_kamar = Integer.parseInt(editJumlahKamarKost.getText().toString().trim());
        Integer sisa_kamar = Integer.parseInt(editSisaKamarKost.getText().toString().trim());
        String keterangan_kost = editKeteranganKost.getText().toString().trim();
        String alamat_kost = editAlamatLengkapKost.getText().toString().trim();
        String kota = editKotaKost.getText().toString().trim();
        String provinsi = editProvinsiKost.getText().toString().trim();
        Integer kodepos = Integer.parseInt(editKodePos.getText().toString().trim());
        String norek = editNoRek.getText().toString().trim();
        String bank = editJenisBank.getText().toString().trim();
        String atas_nama = editAtasNama.getText().toString().trim();



        Map<String, Object> kosts = new HashMap<>();
        kosts.put("jenis_kost", jenis_kost );
        kosts.put("nama_pengelola", nama_pengelola);
        kosts.put("nomor_telepon",  nomor_telepon);
        kosts.put("harga" , harga_kost);
        kosts.put("jenis_sewa", jenis_sewa);
        kosts.put("ukuran_kamar" , ukuran_kamar);
        kosts.put("jumlah_kamar" , jumlah_kamar);
        kosts.put("sisa_kamar", sisa_kamar);
        kosts.put("keterangan", keterangan_kost);
        kosts.put("alamat", alamat_kost);
        kosts.put("kota", kota);
        kosts.put("provinsi", provinsi);
        kosts.put("kode_pos", kodepos);
        kosts.put("nomor_rekening", norek );
        kosts.put("nama_bank", bank);
        kosts.put("nama_rekening", atas_nama);
        kosts.put("updated_at", timestamp);

        firebaseFirestore.collection("data_kost").document(idKost).update(kosts).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            editProvinsiKost.setText(data.getStringExtra("provinsi"));
        }
        else if(requestCode == 2 && resultCode == RESULT_OK){
            editKotaKost.setText(data.getStringExtra("kota"));
        }
        else if(requestCode == 3 && resultCode == RESULT_OK){
            editJenisBank.setText(data.getStringExtra("bank"));
        }

    }
}
