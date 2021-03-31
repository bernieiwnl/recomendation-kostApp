package com.example.rekomendasikostapp.ADAPTER;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rekomendasikostapp.CLASS.Chat;
import com.example.rekomendasikostapp.CLASS.Users;
import com.example.rekomendasikostapp.MAINKOST.DetailKostPenggunaActivity;
import com.example.rekomendasikostapp.MESSAGING.MessageActivity;
import com.example.rekomendasikostapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.Executor;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    private Context c;
    private ArrayList<Users> users;

    private String lastMessaging;
    private String lasTimeMessage;
    private Integer unread;

    public UserAdapter(Context c, ArrayList<Users> users){
        this.c = c;
        this.users = users;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserAdapter.MyViewHolder(LayoutInflater.from(c).inflate(R.layout.single_list_user , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        Picasso.get().load(users.get(position).getProfile_image_url()).into(holder.imageProfile);
        holder.namaLengkap.setText(users.get(position).getFull_name());
        lastMessage(users.get(position).getIdUser(), holder.lastMessage, holder.lastTime, holder.unreadCount);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c,  MessageActivity.class);
                intent.putExtra("idUser", users.get(position).getIdUser());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.getApplicationContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageProfile;
        TextView namaLengkap;
        TextView lastMessage;
        TextView lastTime;
        TextView unreadCount;

        public MyViewHolder(View view) {
            super(view);
            imageProfile = view.findViewById(R.id.profile_image);
            namaLengkap = view.findViewById(R.id.namaLengkap);
            lastMessage = view.findViewById(R.id.pesanUser);
            lastTime = view.findViewById(R.id.timeMessage);
            unreadCount = view.findViewById(R.id.unreadCount);
        }
    }

    //check for last message
    private void lastMessage(final String userId, final TextView last_message , final TextView last_time, final TextView unreadCount)
    {
        lastMessaging = "default";
        lasTimeMessage = "00.00";


        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        String pattern = " HH:mm";
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        firebaseFirestore.collection("chats").whereEqualTo("read", "").whereEqualTo("sender", userId ).whereEqualTo("receiver", firebaseUser.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                unread = 0;
                for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                    Chat unreadChat = doc.toObject(Chat.class);
                    unread++;
                }

                switch (unread){
                    case 0:
                        unreadCount.setVisibility(View.GONE);
                        unreadCount.setText("");
                        break;
                    default:
                        unreadCount.setVisibility(View.VISIBLE);
                        unreadCount.setText(unread.toString());
                        break;
                }


            }
        });

        firebaseFirestore.collection("chats").orderBy("time", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for(QueryDocumentSnapshot data : queryDocumentSnapshots){
                    Chat chatData = data.toObject(Chat.class);
                    if(chatData.getReceiver().equals(firebaseUser.getUid()) && chatData.getSender().equals(userId) ||
                            chatData.getReceiver().equals(userId) && chatData.getSender().equals(firebaseUser.getUid())){
                        if(chatData.getType().equals("image")){
                            if(chatData.getSender().equals(firebaseUser.getUid()))
                            {
                                lastMessaging = "Anda Mengirim Foto";
                            }
                            else{
                                lastMessaging = "Mengirim Foto";
                            }
                            lasTimeMessage = simpleDateFormat.format(chatData.getTime());
                        }
                        else{
                            lastMessaging = chatData.getMessage();
                            lasTimeMessage = simpleDateFormat.format(chatData.getTime());
                        }
                    }
                }

                switch (lastMessaging){
                    case "default":
                        last_message.setText("No Message");
                        break;
                    default:
                        last_message.setText(lastMessaging);
                        break;
                }

                switch (lasTimeMessage){
                    case "00.00":
                        last_time.setText("");
                        break;
                    default:
                        last_time.setText(lasTimeMessage);
                        break;
                }

                lastMessaging = "default";
                lasTimeMessage = "00.00";
            }
        });
    }

}
