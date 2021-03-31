package com.example.rekomendasikostapp.VERIFIKASIUSER;

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
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.rekomendasikostapp.CLASS.LoadingDialogLogin;
import com.example.rekomendasikostapp.CLASS.LoadingDialogUpload;
import com.example.rekomendasikostapp.LoginActivity;
import com.example.rekomendasikostapp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class VerifikasiAkunActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageIdentitas , imageSelfie;
    private Button btnVerificationUser;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseFirestore firebaseFirestoreVerification;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReferenceSelfie;
    private StorageReference storageReferenceIdentitas;
    private StorageTask storageTask;
    private Uri UriImageSelfie , UriImageIdentias;
    String fCurrentPath, idUser;
    private static final int GALLERY_REQUEST_CODE_SELFIE = 2;
    private static final int GALLERY_REQUEST_CODE_IDENTITY = 3;


    private TextInputEditText txtAlamat;
    private RadioGroup radioGroupJenisIdentitas;

    private LoadingDialogLogin loadingDialogUpload = new LoadingDialogLogin(this);

    private String jenis_identitas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifikasi_akun);

        //Get Object Reference
        imageIdentitas = (ImageView) findViewById(R.id.kartu_identitas);
        imageSelfie = (ImageView) findViewById(R.id.selfie_kartu);
        btnVerificationUser = (Button) findViewById(R.id.btnVerificationUser);

        txtAlamat = (TextInputEditText) findViewById(R.id.alamatPengguna);
        radioGroupJenisIdentitas = (RadioGroup) findViewById(R.id.radioGroupJenisIdentitas);

        //set on checked radio button
        radioGroupJenisIdentitas.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.identitasSIM:
                        //your code here
                        jenis_identitas = "SIM";
                        break;
                    case R.id.identitasKTP:
                        //your code here
                        jenis_identitas = "KTP";
                        break;
                }
            }
        });

        //set on click listener
        imageSelfie.setOnClickListener(this);
        imageIdentitas.setOnClickListener(this);
        btnVerificationUser.setOnClickListener(this);


        //Db Reference   //set Storage Reference
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestoreVerification = FirebaseFirestore.getInstance();
        storageReferenceIdentitas = FirebaseStorage.getInstance().getReference("users_identity");
        storageReferenceSelfie = FirebaseStorage.getInstance().getReference("users_selfie");
        idUser = firebaseAuth.getCurrentUser().getUid();

    }

    private void verificationUser(final String idUser, String jenis_identitas){
        String alamat = txtAlamat.getText().toString().trim();

        Map<String, Object> verif = new HashMap<>();
        verif.put("idUser", idUser);
        verif.put("jenis_identitas", jenis_identitas);
        verif.put("alamat", alamat);

        firebaseFirestore.collection("verifikasi").document(idUser).set(verif).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                uploadImageIdentity(idUser);
            }
        });
    }

    private void verifStatus(final String idUser, final String jenis_identitas){
        Map<String, Object> status = new HashMap<>();

        status.put("status", "waiting");
        status.put("verification", false);

        loadingDialogUpload.startLoadingLoginDialog();

        firebaseFirestore.collection("users").document(idUser).update(status).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                 verificationUser(idUser, jenis_identitas);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST_CODE_IDENTITY && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            setPicIdentity();
            UriImageIdentias = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(UriImageIdentias));
                imageIdentitas.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageIdentitas.setImageBitmap(bitmap);
        }

        else if(requestCode == GALLERY_REQUEST_CODE_SELFIE && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            setPicSelfie();
            UriImageSelfie = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(UriImageSelfie));
                imageSelfie.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageSelfie.setImageBitmap(bitmap);
        }

    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImageIdentity(final String idUser) {

        if(UriImageIdentias != null){
            final StorageReference fileReference = storageReferenceIdentitas.child(idUser + "." + getFileExtension(UriImageIdentias));
            storageTask = fileReference.putFile(UriImageIdentias);

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
                        Log.e("idUser" , "" + mUri);
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("identitas_image_url", mUri);
                        firebaseFirestoreVerification.collection("verifikasi").document(idUser).update(map);
                        uploadImageSelfie(idUser);


                    }
                    else{
                        Toast.makeText(VerifikasiAkunActivity.this,
                                "Gagal!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void uploadImageSelfie(final String idUser) {

        if(UriImageSelfie != null){
            final StorageReference fileReference = storageReferenceSelfie.child(idUser + "." + getFileExtension(UriImageSelfie));
            storageTask = fileReference.putFile(UriImageSelfie);

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
                        Log.e("idUser" , "" + mUri);
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("selfie_image_url", mUri);
                        firebaseFirestoreVerification.collection("verifikasi").document(idUser).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                loadingDialogUpload.dismissDialog();
                                finish();
                            }
                        });

                    }
                    else{
                        Toast.makeText(VerifikasiAkunActivity.this,
                                "Gagal!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void setPicIdentity() {
        // Get the dimensions of the View
        int targetW = imageIdentitas.getWidth();
        int targetH = imageIdentitas.getHeight();

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
        imageIdentitas.setImageBitmap(bitmap);
    }

    private void setPicSelfie() {
        // Get the dimensions of the View
        int targetW = imageSelfie.getWidth();
        int targetH = imageSelfie.getHeight();

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
        imageSelfie.setImageBitmap(bitmap);
    }

    private void openImageIdentity(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent , GALLERY_REQUEST_CODE_IDENTITY );
    }

    private void openImageSelfie(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent , GALLERY_REQUEST_CODE_SELFIE );
    }


    @Override
    public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.selfie_kartu:
                    openImageSelfie();
                    break;
                case R.id.kartu_identitas:
                    openImageIdentity();
                    break;
                case R.id.btnVerificationUser:
                    verifStatus(idUser, jenis_identitas);
                    Map<String, Object> kirimPemberitahuan = new HashMap<>();
                    final Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                    kirimPemberitahuan.put("idUser", "yBE1P8R4qbQyyguVBlMXpf52Aam1");
                    kirimPemberitahuan.put("idSender", firebaseAuth.getCurrentUser().getUid());
                    kirimPemberitahuan.put("jenis_pemberitahuan", "Verifikasi Data Pengelola");
                    kirimPemberitahuan.put("judul", "Verifikasi Data Pengelola");
                    kirimPemberitahuan.put("deskripsi", "Verifikasi data pengelola hanya dapat dilakukan oleh administrator. Admin diwajibkan untuk mengecek apakah data verifikasi valid atau tidaknya. Untuk melihat detail user pengelola dapat menekan tombol dibawah ini");
                    kirimPemberitahuan.put("status", "unread");
                    kirimPemberitahuan.put("time", timestamp);

                    firebaseFirestore.collection("pemberitahuans").add(kirimPemberitahuan).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            firebaseFirestore.collection("pemberitahuans").document(documentReference.getId()).update("idPemberitahuan", documentReference.getId());
                        }
                    });
                    break;
            }
    }


}
