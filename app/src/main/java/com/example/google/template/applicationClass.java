package com.example.google.template;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by dharm on 12/01/2017.
 */

public class applicationClass extends Application {
    public static final String TAG = applicationClass.class.getSimpleName();
    private static applicationClass mInstance;
    public static Context mContext;
    private static String UserId;
    private RequestQueue mRequestQueue;
    NFC nfc;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }

    public static synchronized applicationClass getInstance() {
        return mInstance;
    }

    public void setUserId(String UserID){
        this.UserId = UserID;

    }

    public String setUserId(){
        return UserId;
    }


    public String yymmdd()
    {
        Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(calendar.getTime());
        return date;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }


    public String yymmddhhmm()
    {
        Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date = simpleDateFormat.format(calendar.getTime());
        return date;
    }

    public String yymmddhhmmss()
    {
        Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(calendar.getTime());
        return date;
    }
    public String ddmmyyyyhhmmss()
    {
        Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String date = simpleDateFormat.format(calendar.getTime());
        return date;
    }
    public String imageVariable(){
        return "yes";
    }
    public String completedView(){
        return "yes";
    }
    public boolean checkLog(){ return false; }
    public boolean writeJsonFile(){ return false; }
    public String AutoSync(){ return "false"; }
    public int AutoSyncFreq(){ return 15; }
    public boolean fabButton(){ return false; }
    public String Notification(){ return "true"; }
    public String defaultNFC(){return "QR";}
    public String insertTask(){return "insertTask.php";}
    public String insertPPMTask(){return "ppmTaskUpload.php";}
    /*public String urlString(){
        return "http://192.168.0.56/PunNewTemplate/";
    }*/
    public String urlString(){
        return "http://punctualiti.net/microland/";
    }
}


