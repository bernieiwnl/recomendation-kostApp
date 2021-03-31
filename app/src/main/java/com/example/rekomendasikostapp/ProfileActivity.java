package com.example.rekomendasikostapp;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rekomendasikostapp.CLASS.Users;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText txtNamaLengkap, txtEmail, txtPassword, txtNoTelepon ;
    private ImageView imageBack, imageSave , imageProfile;
    private String fCurrentPath , idUser;

    private FirebaseFirestore dbReference;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private StorageTask storageTask;
    private Uri Uimage;
    private static final int GALLERY_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Get Object Reference and set object click listener
        txtNamaLengkap = (EditText) findViewById(R.id.editNamaLengkap);
        txtEmail = (EditText) findViewById(R.id.editEmail);
        txtNoTelepon = (EditText) findViewById(R.id.editTelepon);
        txtPassword = (EditText) findViewById(R.id.editPassword);

        imageProfile = (ImageView) findViewById(R.id.profile_image);
        imageProfile.setOnClickListener(this);

        imageBack = (ImageView) findViewById(R.id.imageViewBack);
        imageBack.setOnClickListener(this);

        imageSave = (ImageView) findViewById(R.id.imageViewSave);
        imageSave.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        dbReference = FirebaseFirestore.getInstance();

        idUser = firebaseAuth.getCurrentUser().getUid();

        //set Storage Reference
        storageReference = FirebaseStorage.getInstance().getReference("users_profile_picture");


        //verifying authentication
        if(firebaseAuth.getCurrentUser() == null){
            startActivity( new Intent(getApplicationContext(), LoginActivity.class));
            Toast.makeText( getApplicationContext() , "Signed Out Automatically" , Toast.LENGTH_SHORT).show();
            finish();
        }

        //get users authentication
        dbReference.getInstance().collection("users")
                .document(firebaseAuth.getCurrentUser().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    Users users = documentSnapshot.toObject(Users.class);
                    txtNamaLengkap.setText(users.getFull_name());
                    txtEmail.setText(users.getEmail());
                    txtNoTelepon.setText(users.getPhone());
                    txtPassword.setText(users.getPassword());
                    if(users.getProfile_image_url() != null){
                        Picasso.get().load(users.getProfile_image_url()).into(imageProfile);
                    }

                }
                else{
                    Toast.makeText( getApplicationContext() , "Users Not Exists" , Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.profile_image:
                openImage();
                break;

            case R.id.imageViewSave:
                uploadImage();
                break;

            case R.id.imageViewBack:
                finish();
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
                imageProfile.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageProfile.setImageBitmap(bitmap);
        }
    }


    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {

        if(Uimage != null){
            final StorageReference fileReference = storageReference.child(idUser + "." + getFileExtension(Uimage));
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
                        Log.e("idUser" , "" + mUri);
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("profile_image_url", mUri);
                        dbReference.collection("users").document(idUser).update(map);
                        Toast.makeText(ProfileActivity.this,
                                "Sukses!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(ProfileActivity.this,
                                "Gagal!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    private void openImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent , GALLERY_REQUEST_CODE );
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = imageProfile.getWidth();
        int targetH = imageProfile.getHeight();

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
        imageProfile.setImageBitmap(bitmap);
    }
}
