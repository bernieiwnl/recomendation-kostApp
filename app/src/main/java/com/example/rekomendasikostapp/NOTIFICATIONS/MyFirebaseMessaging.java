package com.example.rekomendasikostapp.NOTIFICATIONS;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import com.example.rekomendasikostapp.MESSAGING.MessageActivity;
import com.example.rekomendasikostapp.PEMBERITAHUAN.ADMIN.PemberitahuanIklanBaru;
import com.example.rekomendasikostapp.PEMBERITAHUAN.PENGELOLA.PemberitahuanBuktiPembayaran;
import com.example.rekomendasikostapp.PEMBERITAHUAN.PENGELOLA.PemberitahuanPemesanan;
import com.example.rekomendasikostapp.PEMBERITAHUAN.PENGELOLA.PemberitahuanVerifikasiKost;
import com.example.rekomendasikostapp.PEMBERITAHUAN.PENGGUNA.PemberitahuanPemesananBerhasil;
import com.example.rekomendasikostapp.PENGELOLA.DetailKostPengelola;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;

public class MyFirebaseMessaging extends MyFirebaseService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String sented = remoteMessage.getData().get("receiver");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null && sented.equals(firebaseUser.getUid()))
        {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q )
            {
//                if(!isForeground(getApplicationContext()))
//                {
//                    sendOreoNotification(remoteMessage);
//                }

                sendOreoNotification(remoteMessage);
            }
            else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
//                if(!isForeground(getApplicationContext()))
//                {
//                    sendNotification(remoteMessage);
//                }

                sendNotification(remoteMessage);
            }
        }
    }

    private static boolean isForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
        final String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : tasks) {
            if (ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND == appProcess.importance && packageName.equals(appProcess.processName)) {
                return true;
            }
        }
        return false;
    }


    private void sendOreoNotification(RemoteMessage remoteMessage) {

        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        String icon = remoteMessage.getData().get("icon");

        String sender = remoteMessage.getData().get("sender");
        String receiver = remoteMessage.getData().get("receiver");
        String idKost = remoteMessage.getData().get("idKost");
        String idPemberitahuan = remoteMessage.getData().get("idPemberitahuan");
        String idPemesanan = remoteMessage.getData().get("idPemesanan");
        String idKeluhan = remoteMessage.getData().get("idKeluhan");
        String jenis_notifikasi = remoteMessage.getData().get("jenis_notifikasi");

        if(jenis_notifikasi.equals("Pesan")){
            RemoteMessage.Notification notification = remoteMessage.getNotification();
            int j = Integer.parseInt(sender.replaceAll("[\\D]", ""));
            Intent intent = new Intent(this, MessageActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("idUser", sender);
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT);
            Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            OreoNotification oreoNotification = new OreoNotification(this);
            Notification.Builder builder = oreoNotification.getOreNotification(title, body, pendingIntent, defaultSound, icon);
            int i=0;
            if(j > 0)
            {
                i=j;
            }
            oreoNotification.getManager().notify(i, builder.build());
        }
        else if(jenis_notifikasi.equals("Pemesanan")){
            RemoteMessage.Notification notification = remoteMessage.getNotification();
            int j = Integer.parseInt(sender.replaceAll("[\\D]", ""));
            Intent intent = new Intent(this, PemberitahuanPemesanan.class);
            Bundle bundle = new Bundle();
            bundle.putString("idUser", receiver);
            bundle.putString("idKost", idKost);
            bundle.putString("idPemberitahuan" , idPemberitahuan);
            bundle.putString("idPemesanan", idPemesanan);
            bundle.putString("idPengelola", sender);
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT);
            Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            OreoNotification oreoNotification = new OreoNotification(this);
            Notification.Builder builder = oreoNotification.getOreNotification(title, body, pendingIntent, defaultSound, icon);
            int i=0;
            if(j > 0)
            {
                i=j;
            }
            oreoNotification.getManager().notify(i, builder.build());
        }
        else if(jenis_notifikasi.equals("Terima Pembayaran")){
            RemoteMessage.Notification notification = remoteMessage.getNotification();
            int j = Integer.parseInt(sender.replaceAll("[\\D]", ""));
            Intent intent = new Intent(this, PemberitahuanPemesanan.class);
            Bundle bundle = new Bundle();
            bundle.putString("idUser", receiver);
            bundle.putString("idKost", idKost);
            bundle.putString("idPemberitahuan" , idPemberitahuan);
            bundle.putString("idPemesanan", idPemesanan);
            bundle.putString("idPengelola", sender);
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT);
            Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            OreoNotification oreoNotification = new OreoNotification(this);
            Notification.Builder builder = oreoNotification.getOreNotification(title, body, pendingIntent, defaultSound, icon);
            int i=0;
            if(j > 0)
            {
                i=j;
            }
            oreoNotification.getManager().notify(i, builder.build());
        }
        else if(jenis_notifikasi.equals("Verifikasi Pemesanan")){
            RemoteMessage.Notification notification = remoteMessage.getNotification();
            int j = Integer.parseInt(sender.replaceAll("[\\D]", ""));
            Intent intent = new Intent(this, PemberitahuanPemesananBerhasil.class);
            Bundle bundle = new Bundle();
            bundle.putString("idSender", sender);
            bundle.putString("idPemberitahuan" , idPemberitahuan);
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT);
            Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            OreoNotification oreoNotification = new OreoNotification(this);
            Notification.Builder builder = oreoNotification.getOreNotification(title, body, pendingIntent, defaultSound, icon);
            int i=0;
            if(j > 0)
            {
                i=j;
            }
            oreoNotification.getManager().notify(i, builder.build());
        }
        else if(jenis_notifikasi.equals("Tambah Data Baru")){
            RemoteMessage.Notification notification = remoteMessage.getNotification();
            int j = Integer.parseInt(sender.replaceAll("[\\D]", ""));
            Intent intent = new Intent(this, PemberitahuanIklanBaru.class);
            Bundle bundle = new Bundle();
            bundle.putString("idKost", idKost);
            bundle.putString("idPemberitahuan" , idPemberitahuan);
            bundle.putString("idUser", sender);
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT);
            Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            OreoNotification oreoNotification = new OreoNotification(this);
            Notification.Builder builder = oreoNotification.getOreNotification(title, body, pendingIntent, defaultSound, icon);
            int i=0;
            if(j > 0)
            {
                i=j;
            }
            oreoNotification.getManager().notify(i, builder.build());
        }
        else if(jenis_notifikasi.equals("Verifikasi Kost")){
            RemoteMessage.Notification notification = remoteMessage.getNotification();
            int j = Integer.parseInt(sender.replaceAll("[\\D]", ""));
            Intent intent = new Intent(this, PemberitahuanVerifikasiKost.class);
            Bundle bundle = new Bundle();
            bundle.putString("idKost", idKost);
            bundle.putString("idPemberitahuan" , idPemberitahuan);
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT);
            Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            OreoNotification oreoNotification = new OreoNotification(this);
            Notification.Builder builder = oreoNotification.getOreNotification(title, body, pendingIntent, defaultSound, icon);
            int i=0;
            if(j > 0)
            {
                i=j;
            }
            oreoNotification.getManager().notify(i, builder.build());
        }

    }

    private void sendNotification(RemoteMessage remoteMessage) {
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        String icon = remoteMessage.getData().get("icon");

        String sender = remoteMessage.getData().get("sender");
        String receiver = remoteMessage.getData().get("receiver");
        String idKost = remoteMessage.getData().get("idKost");
        String idPemberitahuan = remoteMessage.getData().get("idPemberitahuan");
        String idPemesanan = remoteMessage.getData().get("idPemesanan");
        String idKeluhan = remoteMessage.getData().get("idKeluhan");
        String jenis_notifikasi = remoteMessage.getData().get("jenis_notifikasi");

        if(jenis_notifikasi.equals("Pesan")){
            RemoteMessage.Notification notification = remoteMessage.getNotification();
            int j = Integer.parseInt(sender.replaceAll("[\\D]", ""));
            Intent intent = new Intent(this, MessageActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("idUser", sender);
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT);
            Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this).setSmallIcon(Integer.parseInt(icon)).setContentText(title).setContentText(body).setAutoCancel(true).setSound(defaultSound).setContentIntent(pendingIntent);
            NotificationManager noti = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            int i=0;
            if(j > 0)
            {
                i=j;
            }
            noti.notify(i, builder.build());
        }
        else if(jenis_notifikasi.equals("Pemesanan")){
            RemoteMessage.Notification notification = remoteMessage.getNotification();
            int j = Integer.parseInt(sender.replaceAll("[\\D]", ""));
            Intent intent = new Intent(this, PemberitahuanPemesanan.class);
            Bundle bundle = new Bundle();
            bundle.putString("idUser", receiver);
            bundle.putString("idKost", idKost);
            bundle.putString("idPemberitahuan" , idPemberitahuan);
            bundle.putString("idPemesanan", idPemesanan);
            bundle.putString("idPengelola", sender);
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT);
            Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this).setSmallIcon(Integer.parseInt(icon)).setContentText(title).setContentText(body).setAutoCancel(true).setSound(defaultSound).setContentIntent(pendingIntent);
            NotificationManager noti = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            int i=0;
            if(j > 0)
            {
                i=j;
            }
            noti.notify(i, builder.build());
        }
        else if(jenis_notifikasi.equals("Terima Pembayaran")){
            RemoteMessage.Notification notification = remoteMessage.getNotification();
            int j = Integer.parseInt(sender.replaceAll("[\\D]", ""));
            Intent intent = new Intent(this, PemberitahuanBuktiPembayaran.class);
            Bundle bundle = new Bundle();
            bundle.putString("idUser", receiver);
            bundle.putString("idKost", idKost);
            bundle.putString("idPemberitahuan" , idPemberitahuan);
            bundle.putString("idPemesanan", idPemesanan);
            bundle.putString("idPengelola", sender);
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT);
            Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this).setSmallIcon(Integer.parseInt(icon)).setContentText(title).setContentText(body).setAutoCancel(true).setSound(defaultSound).setContentIntent(pendingIntent);
            NotificationManager noti = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            int i=0;
            if(j > 0)
            {
                i=j;
            }
            noti.notify(i, builder.build());
        }
        else if(jenis_notifikasi.equals("Verifikasi Pemesanan")){
            RemoteMessage.Notification notification = remoteMessage.getNotification();
            int j = Integer.parseInt(sender.replaceAll("[\\D]", ""));
            Intent intent = new Intent(this, PemberitahuanBuktiPembayaran.class);
            Bundle bundle = new Bundle();
            bundle.putString("idUser", receiver);
            bundle.putString("idKost", idKost);
            bundle.putString("idPemberitahuan" , idPemberitahuan);
            bundle.putString("idPemesanan", idPemesanan);
            bundle.putString("idPengelola", sender);
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT);
            Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this).setSmallIcon(Integer.parseInt(icon)).setContentText(title).setContentText(body).setAutoCancel(true).setSound(defaultSound).setContentIntent(pendingIntent);
            NotificationManager noti = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            int i=0;
            if(j > 0)
            {
                i=j;
            }
            noti.notify(i, builder.build());
        }
        else if(jenis_notifikasi.equals("Tambah Data Baru")){
            RemoteMessage.Notification notification = remoteMessage.getNotification();
            int j = Integer.parseInt(sender.replaceAll("[\\D]", ""));
            Intent intent = new Intent(this, PemberitahuanIklanBaru.class);
            Bundle bundle = new Bundle();
            bundle.putString("idKost", idKost);
            bundle.putString("idPemberitahuan" , idPemberitahuan);
            bundle.putString("idUser", sender);
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT);
            Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this).setSmallIcon(Integer.parseInt(icon)).setContentText(title).setContentText(body).setAutoCancel(true).setSound(defaultSound).setContentIntent(pendingIntent);
            NotificationManager noti = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            int i=0;
            if(j > 0)
            {
                i=j;
            }
            noti.notify(i, builder.build());
        }
        else if(jenis_notifikasi.equals("Verifikasi Kost")){
            RemoteMessage.Notification notification = remoteMessage.getNotification();
            int j = Integer.parseInt(sender.replaceAll("[\\D]", ""));
            Intent intent = new Intent(this, PemberitahuanVerifikasiKost.class);
            Bundle bundle = new Bundle();
            bundle.putString("idKost", idKost);
            bundle.putString("idPemberitahuan" , idPemberitahuan);
            intent.putExtras(bundle);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT);
            Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this).setSmallIcon(Integer.parseInt(icon)).setContentText(title).setContentText(body).setAutoCancel(true).setSound(defaultSound).setContentIntent(pendingIntent);
            NotificationManager noti = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            int i=0;
            if(j > 0)
            {
                i=j;
            }
            noti.notify(i, builder.build());
        }
    }
}
