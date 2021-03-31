package com.example.rekomendasikostapp.NOTIFICATIONS;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.HashMap;

public class MyFirebaseService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s)
    {
        super.onNewToken(s);
        String refreshToken = FirebaseInstanceId.getInstance().getInstanceId().getResult().getToken();
        if(refreshToken !=null)
        {
            updateToken(refreshToken);
        }
    }

    private void updateToken(String refreshToken)
    {
        if(FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            Token token = new Token(refreshToken);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("userID", firebaseUser.getUid());
            hashMap.put("token", token);
            firebaseFirestore.collection("token").document(firebaseUser.getUid()).set(hashMap);
        }
    }

}
