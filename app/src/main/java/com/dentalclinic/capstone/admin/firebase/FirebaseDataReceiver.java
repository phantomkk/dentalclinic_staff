package com.dentalclinic.capstone.admin.firebase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class FirebaseDataReceiver extends WakefulBroadcastReceiver {

    private final String TAG = "FirebaseDataReceiver";

    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "I'm in!!!");
        Bundle dataBundle = intent.getBundleExtra("data");
//        Log.d(TAG, dataBundle.toString());
//        Toast.makeText(context, "Received",Toast.LENGTH_SHORT).show();
//          intent = new Intent(context, FeedbackActivity.class);
//        startWakefulService(context, intent);
    }

}