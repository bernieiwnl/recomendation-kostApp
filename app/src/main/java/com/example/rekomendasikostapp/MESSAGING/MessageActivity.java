package com.example.rekomendasikostapp.MESSAGING;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.rekomendasikostapp.ADAPTER.MessageAdapter;
import com.example.rekomendasikostapp.CLASS.Chat;
import com.example.rekomendasikostapp.CLASS.Users;
import com.example.rekomendasikostapp.FRAGMENT.APIService;
import com.example.rekomendasikostapp.NOTIFICATIONS.Client;
import com.example.rekomendasikostapp.NOTIFICATIONS.Data;
import com.example.rekomendasikostapp.NOTIFICATIONS.MyResponse;
import com.example.rekomendasikostapp.NOTIFICATIONS.Sender;
import com.example.rekomendasikostapp.NOTIFICATIONS.Token;
import com.example.rekomendasikostapp.ProfileActivity;
import com.example.rekomendasikostapp.R;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textUserMessaging;
    private EditText editTextMessaging;
    private ImageView btnSentMessage;
    private ImageView btnUpload;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String userID;
    private String imageUrl;
    private ArrayList<Chat> chats;
    private MessageAdapter messageAdapter;
    private RecyclerView recyclerViewChat;
    private ListenerRegistration listenerRegistration;

    APIService apiService;

    private boolean notify = false;


    private StorageReference storageReference;
    private StorageTask storageTask;
    private Uri Uimage;
    private static final int GALLERY_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        apiService = Client.getRetrofit("https://fcm.googleapis.com/").create(APIService.class);

        //get object Reference
        textUserMessaging = (TextView) findViewById(R.id.namaUser);
        editTextMessaging = (EditText) findViewById(R.id.messaging);
        btnSentMessage = (ImageView) findViewById(R.id.sentMessage);
        btnUpload = (ImageView) findViewById(R.id.upload);
        recyclerViewChat = (RecyclerView) findViewById(R.id.recylerChat);

        //set layout manager
        recyclerViewChat.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setItemPrefetchEnabled(true);
        recyclerViewChat.setLayoutManager(linearLayoutManager);


        //set onClickListener
        btnSentMessage.setOnClickListener(this);
        btnUpload.setOnClickListener(this);

        //get instance firestroe
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        //set Storage Reference
        storageReference = FirebaseStorage.getInstance().getReference("chat_image");


        //get intent user
        userID = getIntent().getStringExtra("idUser");



        //get instance firebase firestore
        firebaseFirestore.collection("users").document(userID).addSnapshotListener(MessageActivity.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){
                    Users dataUser = documentSnapshot.toObject(Users.class);
                    textUserMessaging.setText(dataUser.getFull_name());
                    imageUrl = (dataUser.getProfile_image_url());
                }
            }
        });

        //read message
        chats = new ArrayList<>();

        firebaseFirestore.collection("chats").orderBy("time", Query.Direction.ASCENDING).addSnapshotListener( new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                chats.clear();
                for(QueryDocumentSnapshot data : queryDocumentSnapshots){
                    Chat chatData = data.toObject(Chat.class);
                    if(chatData.getReceiver().equals(firebaseAuth.getCurrentUser().getUid()) && chatData.getSender().equals(userID)
                            || chatData.getReceiver().equals(userID) && chatData.getSender().equals(firebaseAuth.getCurrentUser().getUid())){
                        chats.add(chatData);
                    }
                    messageAdapter = new MessageAdapter(MessageActivity.this , chats, imageUrl);
                    recyclerViewChat.setAdapter(messageAdapter);
                }
            }
        });

        seenMessage(userID);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sentMessage:
                notify = true;
                String msg = editTextMessaging.getText().toString().trim();
                if(!msg.equals("")){
                    sentMessage(firebaseAuth.getCurrentUser().getUid(), userID , msg);
                    editTextMessaging.setText("");
                }else{
                    Toast.makeText(MessageActivity.this , "You can't send empty message" , Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.upload:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent , GALLERY_REQUEST_CODE );
                break;
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null)
        {

            Uimage = data.getData();
            if(Uimage != null){
                long currentTime = Calendar.getInstance().getTimeInMillis();
                final StorageReference fileReference = storageReference.child(currentTime + "." + getFileExtension(Uimage));
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
                            sentImage(firebaseAuth.getCurrentUser().getUid(), userID , mUri);
                        }
                    }
                });
            }
        }
    }

    private void seenMessage(final String userID){
        Query query = firebaseFirestore.collection("chats");
        listenerRegistration = query.addSnapshotListener( new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for(QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
                    Chat chatSeen = queryDocumentSnapshot.toObject(Chat.class);
                    if(chatSeen.getReceiver().equals(firebaseAuth.getCurrentUser().getUid()) && chatSeen.getSender().equals(userID)){
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("read" , "Baca");
                        firebaseFirestore.collection("chats").document(queryDocumentSnapshot.getId()).update(hashMap);
                    }
                    if (e != null) {
                        Log.w("TAG", "Listen failed.", e);
                        return;
                    }
                }
            }
        });
    }

    private void sentMessage(final String sender, final String receiver, final String message){

        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        HashMap<String, Object> chatData = new HashMap<>();
        chatData.put("sender", sender);
        chatData.put("receiver", receiver);
        chatData.put("message", message);
        chatData.put("time", timestamp);
        chatData.put("read", "");
        chatData.put("type","text");

        firebaseFirestore.collection("chats").add(chatData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                HashMap<String, Object> receiver = new HashMap<>();
                receiver.put("id", userID);
                receiver.put("time", timestamp);
                HashMap<String, Object> sender = new HashMap<>();
                sender.put("id",  firebaseAuth.getCurrentUser().getUid());
                sender.put("time", timestamp);
                firebaseFirestore.collection("chatlist").document(firebaseAuth.getCurrentUser().getUid()).collection("chats").document(userID).set(receiver);
                firebaseFirestore.collection("chatlist").document(userID).collection("chats").document(firebaseAuth.getCurrentUser().getUid()).set(sender);
            }
        });

        final String messaging = message;

        firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Users user = documentSnapshot.toObject(Users.class);
                if(notify){
                    sendNotification(user.getFull_name(), message, R.mipmap.ic_launcher, sender, receiver, "", "", "", "", "Pesan");
                }
                notify = false;
            }
        });

    }

    private void sentImage(final String sender, final String receiver, final String message){

        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        HashMap<String, Object> chatData = new HashMap<>();
        chatData.put("sender", sender);
        chatData.put("receiver", receiver);
        chatData.put("message", message);
        chatData.put("time", timestamp);
        chatData.put("read", "");
        chatData.put("type","image");

        firebaseFirestore.collection("chats").add(chatData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                HashMap<String, Object> receiver = new HashMap<>();
                receiver.put("id", userID);
                receiver.put("time", timestamp);
                HashMap<String, Object> sender = new HashMap<>();
                sender.put("id",  firebaseAuth.getCurrentUser().getUid());
                sender.put("time", timestamp);
                firebaseFirestore.collection("chatlist").document(firebaseAuth.getCurrentUser().getUid()).collection("chats").document(userID).set(receiver);
                firebaseFirestore.collection("chatlist").document(userID).collection("chats").document(firebaseAuth.getCurrentUser().getUid()).set(sender);
            }
        });

        final String messaging = sender + " Mengirim Foto";
        firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Users user = documentSnapshot.toObject(Users.class);
                if(notify){
                    sendNotification(user.getFull_name(), message, R.mipmap.ic_launcher, sender, receiver, "", "", "", "", "Pesan");
                }
                notify = false;
            }
        });

    }

    private void sendNotification(final String title, final String body, final Integer icon, final String sender, final String receiver, final String idKost, final String idPemberitahuan, final String idPemesanan, final String idKeluhan, final String jenis_notifikasi)
    {
        FirebaseFirestore token = FirebaseFirestore.getInstance();

        Query query = token.collection("token").whereEqualTo("userID", receiver);
        query.addSnapshotListener(MessageActivity.this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for(QueryDocumentSnapshot dataQuery : queryDocumentSnapshots){
                    Token tokenData = dataQuery.toObject(Token.class);
                    Data data = new Data();
                    data.setTitle(title);
                    data.setBody(body);
                    data.setIcon(icon);
                    data.setSender(sender);
                    data.setReceiver(receiver);
                    data.setIdKost(idKost);
                    data.setIdPemberitahuan(idPemberitahuan);
                    data.setIdPemesanan(idPemesanan);
                    data.setIdKeluhan(idKeluhan);
                    data.setJenis_notifikasi(jenis_notifikasi);
                    Sender dataSend = new Sender(data, tokenData.getToken());
                    apiService.sendNotification(dataSend).enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                            if(response.code()==200)
                            {
                                if(response.body().success != 1)
                                {
                                    Toast.makeText(MessageActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        listenerRegistration.remove();
    }

}
