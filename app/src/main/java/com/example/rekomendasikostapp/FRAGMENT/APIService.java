package com.example.rekomendasikostapp.FRAGMENT;

import com.example.rekomendasikostapp.NOTIFICATIONS.MyResponse;
import com.example.rekomendasikostapp.NOTIFICATIONS.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAck22PHI:APA91bHThKXy8zWSunmABfDeIoyO44W_D0MGen07ssMC-j_XlV71HgZx9Qv_LBWK-nVFc12O1kfruPel7UtDMO_dm4AfMnGYqAEO7bkATAz_B6-rF6gXpw3ThvSziha5jPMRRnbFOMRH"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
