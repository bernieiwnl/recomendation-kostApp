package com.example.rekomendasikostapp.CLASS;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import com.example.rekomendasikostapp.PENGELOLA.UPDATEKOST.UpdateDataKostActivity;
import com.example.rekomendasikostapp.PENGELOLA.UPDATEKOST.UpdateFasilitasKostActivity;
import com.example.rekomendasikostapp.PENGELOLA.UPDATEKOST.UpdateFotoKostActivity;
import com.example.rekomendasikostapp.PENGELOLA.UPDATEKOST.UpdateMarkerKostActivity;

public class AlertDialogUpdate {

    private Activity activity;
    private AlertDialog alertDialog;
    private String idKost;

    public AlertDialogUpdate(Activity myActivity , String id){
        activity = myActivity;
        idKost = id;
    }

    public void showUpdateDialog (){
        final String[] option = {"Foto Kost", "Data Kost","Fasilitas Kost","Marker Kost", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Ubah Data").setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                    Intent intent = new Intent(activity, UpdateFotoKostActivity.class);
                    intent.putExtra("idKost" , idKost);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                }
                else if (which == 1){
                    Intent intent = new Intent(activity, UpdateDataKostActivity.class);
                    intent.putExtra("idKost" , idKost);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                }
                else if(which == 2){
                    Intent intent = new Intent(activity, UpdateFasilitasKostActivity.class);
                    intent.putExtra("idKost" , idKost);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                }
                else if(which == 3 ){
                    Intent intent = new Intent(activity, UpdateMarkerKostActivity.class);
                    intent.putExtra("idKost" , idKost);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                }
                else if(which == 4){
                    dismissUpdateDialog();
                }
            }
        });
        alertDialog = builder.create();
        alertDialog.show();

    }

    public void dismissUpdateDialog(){
        alertDialog.dismiss();
    }

}
