package com.example.rekomendasikostapp.INSERTKOST;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rekomendasikostapp.ADAPTER.UploadFotoListAdapter;
import com.example.rekomendasikostapp.CLASS.LoadingDialogLogin;
import com.example.rekomendasikostapp.PENGELOLA.ListKostPengelola;
import com.example.rekomendasikostapp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class InsertKost_3 extends AppCompatActivity implements View.OnClickListener {

    private Button  btnLanjutkan;
    private ImageView btnAddFoto;
    private RecyclerView recFoto;
    private static final int RESULT_LOAD_IMAGE1 = 1;
    private StorageReference storageReference;
    private String idKost;
    private ArrayList<Bitmap> fileImageList;
    private ArrayList<Uri> fileUriList;
    private UploadFotoListAdapter uploadFotoListAdapter;
    private StorageTask storageTask;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_kost_3);

        //storage reference
        storageReference = FirebaseStorage.getInstance().getReference();

        //firestore reference
        firebaseFirestore = FirebaseFirestore.getInstance();

        //Generate Object Reference
        btnAddFoto = (ImageView) findViewById(R.id.btnAddFoto);
        btnLanjutkan = (Button) findViewById(R.id.btnNext);
        recFoto = (RecyclerView) findViewById(R.id.recyclerFoto);

        // set on clicklistener
        btnAddFoto.setOnClickListener(this);
        btnLanjutkan.setOnClickListener(this);

        //list
        fileImageList = new ArrayList<>();
        fileUriList = new ArrayList<>();

        uploadFotoListAdapter = new UploadFotoListAdapter(fileImageList);

        //recyclerView
        recFoto.setLayoutManager( new LinearLayoutManager(this));
        recFoto.setHasFixedSize(true);
        recFoto.setAdapter(uploadFotoListAdapter);

        idKost = getIntent().getStringExtra("idKosts");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAddFoto:
                openFoto();
                break;
            case R.id.btnNext:
                uploadFoto();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE1 && resultCode == RESULT_OK){
            if(data.getClipData() != null){
                int totalItemSelected = data.getClipData().getItemCount();
                for(int i = 0; i < totalItemSelected; i++){
                    Uri fileUri = data.getClipData().getItemAt(i).getUri();
                    Bitmap bitmap = null;
                    try {
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(fileUri));
                        fileImageList.add(bitmap);
                        fileUriList.add(fileUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    uploadFotoListAdapter.notifyDataSetChanged();
                    btnLanjutkan.setEnabled(true);
                    btnLanjutkan.setBackgroundResource(R.color.colorAccent);
                }
            }
            else if(data.getData() != null){
                Uri fileUri = data.getData();
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(fileUri));
                    fileImageList.add(bitmap);
                    fileUriList.add(fileUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                uploadFotoListAdapter.notifyDataSetChanged();
                btnLanjutkan.setEnabled(true);
                btnLanjutkan.setBackgroundResource(R.color.colorAccent);
            }
        }
    }

    public void uploadFoto(){
        // call time
        long currentTime = Calendar.getInstance().getTimeInMillis();
        Integer data = 0;

        //Call Loading Dialog
        final LoadingDialogLogin loadingDialogLogin = new LoadingDialogLogin(InsertKost_3.this);
        loadingDialogLogin.startLoadingLoginDialog();

        for(int i = 0; i < fileUriList.size(); i++){
            String fileName = getFileName(fileUriList.get(i));
            final StorageReference fileToUpload = storageReference.child("foto_kost").child(currentTime + "_" + idKost + "_" + i);
            storageTask = fileToUpload.putFile(fileUriList.get(i));
            storageTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot , Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw  task.getException();
                    }
                    return  fileToUpload.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();
                        firebaseFirestore.collection("data_kost").document(idKost).update("foto_kost", FieldValue.arrayUnion(mUri)).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });
                    }
                    else{
                        Toast.makeText(InsertKost_3.this,
                                "Gagal!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            data++;
        }

        if(data == fileUriList.size()){
            loadingDialogLogin.dismissDialog();
            InsertKost_2.getInstance().finish();
            Intent intent = new Intent(getApplicationContext(), ListKostPengelola.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Apakah kamu yakin untuk membatalkan ? data kost tidak akan disimpan").setCancelable(false).setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                firebaseFirestore.collection("data_kost").document(idKost).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        finish();
                    }
                });
            }
        }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    protected  void openFoto(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE1);
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

}
