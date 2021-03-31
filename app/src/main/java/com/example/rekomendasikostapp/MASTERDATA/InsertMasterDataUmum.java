package com.example.rekomendasikostapp.MASTERDATA;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rekomendasikostapp.ADMIN.AdminFasilitasUmum;
import com.example.rekomendasikostapp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InsertMasterDataUmum extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageIcon;
    private EditText editNamaFasilitas;
    private FloatingActionButton btnSimpatMasterData;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    private StorageTask storageTask;
    private Uri Uimage;
    private String fCurrentPath ;
    private static final int GALLERY_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_master_data_umum);

        imageIcon = (ImageView) findViewById(R.id.imageIcon);
        editNamaFasilitas = (EditText) findViewById(R.id.namaFasilitas);
        btnSimpatMasterData = (FloatingActionButton) findViewById(R.id.btnSimpanMaster);
        btnSimpatMasterData.setOnClickListener(this);
        imageIcon.setOnClickListener(this);
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("icon_fasilitas");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSimpanMaster:
                insertFasilitas();
                break;
            case R.id.imageIcon:
                openImage();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            setPic();
            Uimage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uimage));
                imageIcon.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageIcon.setImageBitmap(bitmap);
        }
    }

    private void insertFasilitas(){
        String nama_fasilitas = editNamaFasilitas.getText().toString().trim();
        ArrayList<String> idkost = new ArrayList<>();
        Map<String, Object> master = new HashMap<>();
        master.put("nama_fasilitas" , nama_fasilitas);
        master.put("idkosts", idkost);
        firebaseFirestore.collection("fasilitas_umum").add(master).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                uploadIcon(documentReference.getId());
                Map<String, Object> id = new HashMap<>();
                id.put("idfasilitas" ,documentReference.getId());
                firebaseFirestore.collection("fasilitas_umum").document(documentReference.getId()).update(id);
                finish();
            }
        });
    }

    private void openImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent , GALLERY_REQUEST_CODE );
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadIcon(final String idFasilitas) {
        if(Uimage != null){
            final StorageReference fileReference = storageReference.child(idFasilitas + "." + getFileExtension(Uimage));
            storageTask = fileReference.putFile(Uimage);

            storageTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot , Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw  task.getException();
                    }
                    return  fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();
                        Log.e("idFasilitas" , "" + mUri);
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("foto_url", mUri);
                        firebaseFirestore.collection("fasilitas_umum").document(idFasilitas).update(map);
                        Toast.makeText(InsertMasterDataUmum.this,
                                "Sukses Upload Selfie", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(InsertMasterDataUmum.this,
                                "Gagal!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    private void setPic() {
        // Get the dimensions of the View
        int targetW = imageIcon.getWidth();
        int targetH = imageIcon.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(fCurrentPath, bmOptions);
        imageIcon.setImageBitmap(bitmap);
    }
}
