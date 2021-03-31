package com.example.rekomendasikostapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rekomendasikostapp.LOCATION.SimpanLokasi;
import com.example.rekomendasikostapp.REKOMENDASI.PemilihanKriteriaActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.opencensus.tags.Tag;

public class RegisterActivity extends AppCompatActivity {


    private TextInputEditText textFullName, textEmail, textPassword, textPhone;
    private Button buttonRegister;
    private TextView textTitle, textBuatAkun , textLogin , textLoginTo;
    private static final String TAG = "RegisterActivity";
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Get Object Reference
        textTitle = (TextView) findViewById(R.id.txtLTitle);
        textBuatAkun = (TextView) findViewById(R.id.txtBuatAkun);
        textFullName = (TextInputEditText) findViewById(R.id.txtFullName);
        textEmail = (TextInputEditText) findViewById(R.id.txtEmail);
        textPassword = (TextInputEditText) findViewById(R.id.txtPassword);
        textPhone = (TextInputEditText) findViewById(R.id.txtPhone);
        textLogin = (TextView) findViewById(R.id.txtLogin);
        textLoginTo = (TextView) findViewById(R.id.txtLoginTo);

        buttonRegister = (Button) findViewById(R.id.btnRegister);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //load fonts
        Typeface fontsLogo = Typeface.createFromAsset(getAssets(), "fonts/Fredoka.ttf");
        Typeface fontsSubtitle = Typeface.createFromAsset(getAssets(), "fonts/MontserratLight.ttf");
        Typeface fontsBtn = Typeface.createFromAsset(getAssets(), "fonts/MontserratMedium.ttf");

        //set fonts
        textTitle.setTypeface(fontsLogo);
        textBuatAkun.setTypeface(fontsSubtitle);

        // session login
        if(firebaseAuth.getCurrentUser() != null)
        {
            startActivity( new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        }

        // validation register
        textEmail.addTextChangedListener(registerTextWatcher);
        textPassword.addTextChangedListener(registerTextWatcher);
        textPhone.addTextChangedListener(registerTextWatcher);
        textFullName.addTextChangedListener(registerTextWatcher);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (validation())
            {
                final String fullName = textFullName.getText().toString().trim();
                final String password = textPassword.getText().toString().trim();
                final String email = textEmail.getText().toString().trim();
                final String phone = textPhone.getText().toString().trim();

                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        Map<String, Object> users = new HashMap<>();
                        users.put("full_name", fullName);
                        users.put("email", email);
                        users.put("password", password);
                        users.put("phone", phone);
                        users.put("account", "Pengguna");
                        users.put("profile_image_url", "https://firebasestorage.googleapis.com/v0/b/rekomendasi-kost-app.appspot.com/o/users_profile_picture%2Fdefault.png?alt=media&token=7629b894-ca33-4fdf-aae4-10deab9a6883");
                        users.put("status", "");
                        users.put("verified", false);
                        users.put("idUser",  firebaseAuth.getCurrentUser().getUid());
                        final String userId = firebaseAuth.getCurrentUser().getUid();

                        firebaseFirestore.collection("users").document(userId).set(users).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + userId);

                                ArrayList<String> preferensiKriteria = new ArrayList<>();
                                ArrayList<String> preferensiUmum = new ArrayList<>();
                                ArrayList<String> preferensiKamar = new ArrayList<>();
                                ArrayList<String> preferensiLingkungan = new ArrayList<>();

                                Map<String, Object> preferensi = new HashMap<>();
                                preferensi.put("preferensiKriteria", preferensiKriteria);
                                preferensi.put("preferensiUmum", preferensiUmum);
                                preferensi.put("preferensiKamar", preferensiKamar);
                                preferensi.put("preferensiAkses", preferensiLingkungan);

                                firebaseFirestore.collection("preferensi").document(userId).set(preferensi).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(RegisterActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), SimpanLokasi.class));
                                        finish();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(RegisterActivity.this, "Error !" + task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                    }
                    }
                });
            }
                }
        });
    }

    private TextWatcher registerTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String emailInput = textEmail.getText().toString().trim();
            String passwordInput = textPassword.getText().toString().trim();
            String phoneInput = textPhone.getText().toString().trim();
            String nameInput = textFullName.getText().toString().trim();
            buttonRegister.setEnabled(!emailInput.isEmpty() && !passwordInput.isEmpty() && !phoneInput.isEmpty() && !nameInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    public boolean validation(){

        String v_fullname = textFullName.getText().toString().trim();
        String v_password = textPassword.getText().toString().trim();
        String v_email = textPassword.getText().toString().trim();
        String v_phone = textPhone.getText().toString().trim();

        if(TextUtils.isEmpty(v_fullname))
        {
             textFullName.setError("Full Name is required");
             textFullName.requestFocus();
             return false;
        }
        else if(TextUtils.isEmpty(v_password))
        {
            textPassword.setError("Password is required");
            textPassword.requestFocus();
            return false;
        }
        else if(TextUtils.isEmpty(v_email))
        {
            textEmail.setError("Email is required");
            return false;
        }
        else if(TextUtils.isEmpty(v_phone))
        {
            textPhone.setError("Phone Number is required");
            return false;
        }
        return true;
    }

    public void toLoginActivity(View view) {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
