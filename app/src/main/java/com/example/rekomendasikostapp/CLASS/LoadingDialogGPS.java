package com.example.rekomendasikostapp.CLASS;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;

import com.example.rekomendasikostapp.R;

public class LoadingDialogGPS {

    private Activity activity;
    private AlertDialog alertDialog;

    public LoadingDialogGPS(Activity myActivity){
        activity = myActivity;
    }

    public void startLoadingDialogGPS()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog_gps, null));
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void dismissDialog()
    {
        alertDialog.dismiss();
    }
}
