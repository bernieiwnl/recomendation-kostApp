package com.example.rekomendasikostapp.FRAGMENT;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.rekomendasikostapp.ADAPTER.UserAdapter;
import com.example.rekomendasikostapp.CLASS.Chat;
import com.example.rekomendasikostapp.CLASS.ChatList;
import com.example.rekomendasikostapp.CLASS.Users;
import com.example.rekomendasikostapp.NOTIFICATIONS.Token;
import com.example.rekomendasikostapp.R;
import com.firebase.ui.auth.data.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PesanFragment extends Fragment {


    public PesanFragment() {
        // Required empty public constructor
    }

    private RecyclerView recyclerViewUserChat;
    private ArrayList<Users> users;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private ImageView empty_message;


    private List<ChatList> userList;

    UserAdapter userAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pesan, container, false);


        recyclerViewUserChat = (RecyclerView) view.findViewById(R.id.userChat);
        empty_message = (ImageView) view.findViewById(R.id.empty_message);

        recyclerViewUserChat.setHasFixedSize(true);
        recyclerViewUserChat.setLayoutManager(new LinearLayoutManager(getActivity()));

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        userList = new ArrayList<>();
        users = new ArrayList<>();

        firebaseFirestore.collection("chatlist").document(firebaseAuth.getCurrentUser().getUid()).collection("chats").orderBy("time", Query.Direction.ASCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                userList.clear();
                for(QueryDocumentSnapshot data : queryDocumentSnapshots){
                    ChatList chatList = data.toObject(ChatList.class);
                    userList.add(chatList);
                }

                if(userList.isEmpty())
                {
                    recyclerViewUserChat.setVisibility(View.GONE);
                    empty_message.setVisibility(View.VISIBLE);
                }
                else
                {
                    recyclerViewUserChat.setVisibility(View.VISIBLE);
                    empty_message.setVisibility(View.GONE);
                }

                chatList();
            }
        });

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        recyclerViewUserChat.addItemDecoration(dividerItemDecoration);

        return view;
    }

    private void chatList() {
        firebaseFirestore.collection("users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                users.clear();
                for(QueryDocumentSnapshot dataUsers : queryDocumentSnapshots){
                    Users usersData = dataUsers.toObject(Users.class);
                    for(ChatList chatList : userList){
                        if(usersData.getIdUser().equals(chatList.getId())){
                            users.add(usersData);
                        }
                    }
                }
                userAdapter = new UserAdapter(getContext() , users);
                recyclerViewUserChat.setAdapter(userAdapter);
            }
        });
    }
}


