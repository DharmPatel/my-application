package com.example.google.csmia_temp.notification;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.google.csmia_temp.ConstantList.Config;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";
    SharedPreferences settings,settings_token;
    SharedPreferences.Editor editor,editor_token;
    private void storeRegIdInPref(String token) {

        settings_token = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        editor_token = settings_token.edit();
        editor_token.putString("token", token);
        editor_token.apply();
    }
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        storeRegIdInPref(refreshedToken);
    }
}
