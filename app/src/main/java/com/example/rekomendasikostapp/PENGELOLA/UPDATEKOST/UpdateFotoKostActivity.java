package com.example.rekomendasikostapp.PENGELOLA.UPDATEKOST;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.rekomendasikostapp.CLASS.Kost;
import com.example.rekomendasikostapp.CLASS.LoadingDialogLogin;
import com.example.rekomendasikostapp.CLASS.LoadingDialogUpload;
import com.example.rekomendasikostapp.PENGELOLA.ADAPTER.DaftarFotoKost;
import com.example.rekomendasikostapp.R;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class UpdateFotoKostActivity extends AppCompatActivity implements View.OnClickListener, DaftarFotoKost.OnItemClickListener {

    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firebaseFirestore;
    private String idKost;
    private RecyclerView recyclerViewFoto;
    private StorageReference storageReference;
    private StorageTask storageTask;
    private com.github.clans.fab.FloatingActionButton floatingActionButtonFoto;

    private ArrayList<String> fotoKosts;
    private ArrayList<Uri> fileUriList;
    private DaftarFotoKost daftarFotoKost;

    private static final int RESULT_LOAD_IMAGE1 = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_foto_kost);

        //get object reference
        floatingActionButtonFoto = (FloatingActionButton) findViewById(R.id.floatingActionButtonFoto);
        recyclerViewFoto = (RecyclerView) findViewById(R.id.recyclerFoto);

        //set layout recylerview
        recyclerViewFoto.setLayoutManager(new LinearLayoutManager(this));

        //get String extra
        idKost = getIntent().getStringExtra("idKost");

        //set new array
        fotoKosts = new ArrayList<String>();
        fileUriList = new ArrayList<Uri>();

        //get instance firebase
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        //set onclicklistener
        floatingActionButtonFoto.setOnClickListener(this);


        // get from firestore
        firebaseFirestore.collection("data_kost").document(idKost).addSnapshotListener( new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                 fotoKosts.clear();
                 if(documentSnapshot.exists()){
                     Kost kost = documentSnapshot.toObject(Kost.class);
                     for(int i = 0; i < kost.getFoto_kost().size(); i++){
                         fotoKosts.add(kost.getFoto_kost().get(i));
                     }
                     daftarFotoKost = new DaftarFotoKost(getApplicationContext(), fotoKosts);
                     recyclerViewFoto.setAdapter(daftarFotoKost);
                     daftarFotoKost.setOnItemClickListener(UpdateFotoKostActivity.this);
                 }
                 else{
                     recyclerViewFoto.setVisibility(View.GONE);
                     Log.d("TAG", "Error Get Query " + e);
                 }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.floatingActionButtonFoto:
                //going to gallery
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE1);
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
                    fileUriList.add(fileUri);
                }
            }
            else if(data.getData() != null){
                Uri fileUri = data.getData();
                fileUriList.add(fileUri);
            }
            uploadTask();
            fileUriList.clear();
        }
    }

    void uploadTask(){
        //call time
        long currentTime = Calendar.getInstance().getTimeInMillis();

        //Call Loading Dialog
        final LoadingDialogLogin loadingDialogLogin = new LoadingDialogLogin(UpdateFotoKostActivity.this);
        loadingDialogLogin.startLoadingLoginDialog();

        for(int i = 0; i < fileUriList.size(); i++){

            final String fileName = getFileName(fileUriList.get(i));
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
                        final String mUri = downloadUri.toString();
                        firebaseFirestore.collection("data_kost").document(idKost).update("foto_kost", FieldValue.arrayUnion(mUri)).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                loadingDialogLogin.dismissDialog();
                                daftarFotoKost.notifyDataSetChanged();
                            }
                        });
                    }
                    else{
                        Toast.makeText(UpdateFotoKostActivity.this,
                                "Gagal!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    @Override
    public void onImageclick(int position) {

    }

    @Override
    public void onImageDeleteClick(final int position) {
        final String data = fotoKosts.get(position);
        StorageReference storageReference = firebaseStorage.getReferenceFromUrl(data);
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                firebaseFirestore.collection("data_kost").document(idKost).update("foto_kost", FieldValue.arrayRemove(data)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        daftarFotoKost.notifyDataSetChanged();
                    }
                });
            }
        });
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
