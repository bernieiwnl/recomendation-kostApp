package com.example.rekomendasikostapp.CLASS;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.rekomendasikostapp.R;

public class LoadingDialogUpload {

    private Activity activity;
    private AlertDialog alertDialog;

    public LoadingDialogUpload(Activity myActivity){
        activity = myActivity;
    }

    public void startLoadingLoginDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog_proggress_upload, null));
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();
    }

    public void dismissDialog()
    {
        alertDialog.dismiss();
    }
}
