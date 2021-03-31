package com.example.rekomendasikostapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
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

import com.example.rekomendasikostapp.CLASS.LoadingDialogLogin;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText textEmail, textPassword;
    private Button buttonLogin;
    private TextView buttonRegister, txtLTitle , txtRegister , txtRegisterTo , txtSubtitle;
    Animation smalltobe;
    FirebaseAuth fAuth;
    GoogleSignInClient fGoogleSignInClient;
    static final int GOOGLE_SIGN = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //Get Object Reference
        txtLTitle =(TextView) findViewById(R.id.txtLTitle);
        textEmail = (TextInputEditText) findViewById(R.id.txtEmail);
        textPassword = (TextInputEditText) findViewById(R.id.txtPassword);
        txtRegister = (TextView)  findViewById(R.id.textViewRegister);
        txtRegisterTo = (TextView) findViewById(R.id.textViewRegisterTo);
        txtSubtitle = (TextView) findViewById(R.id.textSubtitle);
        fAuth = FirebaseAuth.getInstance();
        buttonLogin = (Button) findViewById(R.id.txtLogin);
        smalltobe = AnimationUtils.loadAnimation(this, R.anim.smalltobig2);


        //load fonts
        Typeface fontsLogo = Typeface.createFromAsset(getAssets(), "fonts/Fredoka.ttf");
        Typeface fontsSubtitle = Typeface.createFromAsset(getAssets(), "fonts/MontserratLight.ttf");
        Typeface fontsBtn = Typeface.createFromAsset(getAssets(), "fonts/MontserratMedium.ttf");


        //set fonts
        txtLTitle.setTypeface(fontsLogo);
        txtSubtitle.setTypeface(fontsSubtitle);


        //Validation Email & Password
        textEmail.addTextChangedListener(loginTextWatcher);
        textPassword.addTextChangedListener(loginTextWatcher);

        //check if user logged in or not
        if(fAuth.getInstance().getCurrentUser() != null)
        {
            startActivity( new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        }

        //Call Loading Dialog
        final LoadingDialogLogin loadingDialogLogin = new LoadingDialogLogin(LoginActivity.this);

        //onCLick Button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = textEmail.getText().toString().trim();
                String password = textPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email))
                {
                    textEmail.setError("Email is required");
                    return;
                }

                else if(TextUtils.isEmpty(password))
                {
                    textPassword.setError("Password is required");
                    return;
                }

                //auth
                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            loadingDialogLogin.startLoadingLoginDialog();
                            Handler handler = new Handler();

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    loadingDialogLogin.dismissDialog();
                                    Toast.makeText(LoginActivity.this, "Login Succsessfully", Toast.LENGTH_SHORT).show();
                                    startActivity( new Intent(getApplicationContext(), HomeActivity.class));
                                    finish();
                                }
                            }, 5000);

                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, "Error !" + task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String emailInput = textEmail.getText().toString().trim();
            String passwordInput = textPassword.getText().toString().trim();
            buttonLogin.setEnabled(!emailInput.isEmpty() && !passwordInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Apakah kamu yakin untuk keluar?").setCancelable(false).setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
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


    public void toRegisterActivity(View view) {
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    public void toLoginActivity(){
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }
}
