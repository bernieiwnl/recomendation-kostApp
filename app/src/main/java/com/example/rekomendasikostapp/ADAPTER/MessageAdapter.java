package com.example.rekomendasikostapp.ADAPTER;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rekomendasikostapp.CLASS.Chat;
import com.example.rekomendasikostapp.MESSAGING.ImageDetailActivity;
import com.example.rekomendasikostapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

     public static final int MESSAGE_TYPE_LEFT = 0;
     public static final int MESSAGE_TYPLE_RIGHT = 1;
     public static final int IMAGE_TYPE_LEFT = 2;
     public static final int IMAGE_TYPE_RIGHT = 3;

     private Context context;
     private ArrayList<Chat> chats;
     private String image_url;

     FirebaseUser firebaseUser;

     public MessageAdapter(Context c , ArrayList<Chat> chats, String image_url){
         this.chats = chats;
         this.context = c;
         this.image_url = image_url;
     }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MESSAGE_TYPLE_RIGHT){
            View view = LayoutInflater.from(context).inflate(R.layout.messaging_item_right, parent , false);
            return new MessageAdapter.MyViewHolder(view);
        }
        else if(viewType == MESSAGE_TYPE_LEFT){
            View view = LayoutInflater.from(context).inflate(R.layout.messaging_item_left, parent , false);
            return new MessageAdapter.MyViewHolder(view);
        }
        else if(viewType == IMAGE_TYPE_RIGHT){
            View view = LayoutInflater.from(context).inflate(R.layout.messaging_image_right, parent , false);
            return new MessageAdapter.MyViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(context).inflate(R.layout.messaging_image_left, parent , false);
            return new MessageAdapter.MyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
         if(chats.get(position).getType().equals("text")){
             holder.message.setText(chats.get(position).getMessage());
         }
         else if (chats.get(position).getType().equals("image"))
         {
            Picasso.get().load(chats.get(position).getMessage()).into(holder.messageImage);

            holder.messageImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,  ImageDetailActivity.class);
                    intent.putExtra("imageuri", chats.get(position).getMessage());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.getApplicationContext().startActivity(intent);
                }
            });
         }

         if(image_url.equals("")){

         }else{
             Picasso.get().load(image_url).into(holder.imageProfile);
         }

        String pattern = "HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        holder.time.setText( simpleDateFormat.format(chats.get(position).getTime()));
        holder.read.setText(chats.get(position).getRead());


    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageProfile;
        TextView message;
        TextView time;
        TextView read;
        ImageView messageImage;

        public MyViewHolder(View view) {
            super(view);
            imageProfile = view.findViewById(R.id.imageProfile);
            message = view.findViewById(R.id.messageUser);
            time = view.findViewById(R.id.time);
            read = view.findViewById(R.id.read);
            messageImage = view.findViewById(R.id.messageImage);

        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(chats.get(position).getSender().equals(firebaseUser.getUid())){
            if(chats.get(position).getType().equals("image")){
                return IMAGE_TYPE_RIGHT;
            }
            else{
                return MESSAGE_TYPLE_RIGHT;
            }
        }
        else{
            if(chats.get(position).getType().equals("image")){
                return IMAGE_TYPE_LEFT;
            }
            else{
                return MESSAGE_TYPE_LEFT;
            }

        }
    }
}
