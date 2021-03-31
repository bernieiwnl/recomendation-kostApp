package com.example.rekomendasikostapp.CLASS;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class FetchLocationServices extends IntentService {

    private ResultReceiver resultReceiver;

    public FetchLocationServices(){
        super("FetchLocationServices");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null){
            String errorMessage ="";
            resultReceiver = intent.getParcelableExtra(Constants.RECEIVER);
            Location location = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);
            if(location == null){
                return;
            }
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addressList = null;
            try {
                addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            } catch (IOException e) {
                errorMessage = e.getMessage();
            }

            if(addressList.isEmpty()  || addressList == null){
                deliverResult(Constants.FAILURE_RESULT, errorMessage);
            }
            else{
                Address address = addressList.get(0);
                ArrayList<String> addressFragments = new ArrayList<>();
                for(int i = 0; i < address.getMaxAddressLineIndex(); i++){
                    addressFragments.add(address.getAddressLine(i));
                }
                deliverResult(Constants.SUCCESS_RESULT, TextUtils.join(Objects.requireNonNull(System.getProperty("line.separator")), addressFragments));
            }
        }
    }

    private void deliverResult(int resultCode, String addressMessage){
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY, addressMessage);
        resultReceiver.send(resultCode,bundle);
    }
}
