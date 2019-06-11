package com.example.google.csmia_temp;

import android.app.Application;
import android.content.Context;
import android.os.Build;
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
    public static String URL = "http://punctualiti.in/csmia/android/";
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

/*
    public void uploadTicket(){
        String selectQuery = "SELECT  * FROM Ticket_Created WHERE UpdatedStatus = 'no' AND User_Group_Id IN ("+myDb.UserGroupId(User_Id)+") and Task_Id = '"+Task_Id+"' and Form_Ticket_Id = '"+Ticket_Id+"'";
        Log.d("TicketQuery"," "+selectQuery);
        String groupName="",Remark="";
        try {
            database = myDb.getWritableDatabase();
            Cursor cursor = database.rawQuery(selectQuery, null);

            Log.d("TicketQueryCount", " " + cursor.getCount());


            if (cursor.getCount() > 0) {
                String GroupQuery = "SELECT Group_Name from User_Group where User_Group_Id = '" + Group_Id + "'";
                Cursor groupQueryCursor = database.rawQuery(GroupQuery, null);
                if (groupQueryCursor.getCount() > 0) {
                    if (groupQueryCursor.moveToFirst()) {
                        do {
                            groupName = groupQueryCursor.getString(groupQueryCursor.getColumnIndex("Group_Name"));
                        } while (groupQueryCursor.moveToNext());
                    }
                }
                groupQueryCursor.close();

                String RemarkQuery = "Select * from Data_Posting where Task_Id = '" + Task_Id + "' and Form_Structure_Id = '" + Form_Structure_Id + "'";
                Cursor RemarkCursor = database.rawQuery(RemarkQuery, null);
                String Value1 = "";
                Log.d("Remark", "" + RemarkCursor.getCount() + " " + RemarkQuery);

                if (RemarkCursor.getCount() > 0) {
                    if (RemarkCursor.moveToFirst()) {
                        do {
                            Value1 = RemarkCursor.getString(RemarkCursor.getColumnIndex("Value"));
                            Remark = RemarkCursor.getString(RemarkCursor.getColumnIndex("Remark"));
                            Log.d("ValRemark", "" + Remark + " " + Value1);
                        } while (RemarkCursor.moveToNext());
                    }
                }


                String data = URLEncoder.encode("site", "UTF-8") + "=" + URLEncoder.encode(myDb.getAssetSite(AssetId), "UTF-8") + "&" +
                        URLEncoder.encode("location", "UTF-8") + "=" + URLEncoder.encode(myDb.getAssetLocationFloor(AssetId), "UTF-8") + "&" +
                        URLEncoder.encode("sublocation", "UTF-8") + "=" + URLEncoder.encode(myDb.getAssetSubLocation(AssetId), "UTF-8") + "&" +
                        URLEncoder.encode("serviceArea", "UTF-8") + "=" + URLEncoder.encode(Asset_Name, "UTF-8") + "&" +
                        URLEncoder.encode("department", "UTF-8") + "=" + URLEncoder.encode(groupName, "UTF-8") + "&" +
                        URLEncoder.encode("product", "UTF-8") + "=" + URLEncoder.encode(Product, "UTF-8") + "&" +
                        URLEncoder.encode("component", "UTF-8") + "=" + URLEncoder.encode(Subject, "UTF-8") + "&" +
                        URLEncoder.encode("IncidentSource", "UTF-8") + "=" + URLEncoder.encode(IncidentSource, "UTF-8") + "&" +
                        URLEncoder.encode("ProblemTitle", "UTF-8") + "=" + URLEncoder.encode(Type, "UTF-8") + "&" +
                        URLEncoder.encode("problemDescription", "UTF-8") + "=" + URLEncoder.encode("" + Remark, "UTF-8") + "&" +
                        URLEncoder.encode("Loggedby", "UTF-8") + "=" + URLEncoder.encode(LoggedBy, "UTF-8") + "&" +
                        URLEncoder.encode("CreatedDateTime", "UTF-8") + "=" + URLEncoder.encode(new applicationClass().yymmddhhmmss(), "UTF-8");

                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "https://punctualiti.net/csmia_temp/android/raiseticket.php?"+data, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Log.d("Response Log", "123 " + response.toString());
                            Toast.makeText(getApplicationContext(),
                                    "Ticket Generated", Toast.LENGTH_SHORT).show();


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        NetworkResponse response = error.networkResponse;
                        if (error instanceof ServerError && response != null) {
                            try {
                                String res = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                // Now you can use any deserializer to make sense of data
                                Log.d("API error", "" + res);
                                //JSONObject obj = new JSONObject(res);
                            } catch (UnsupportedEncodingException e1) {
                                // Couldn't properly decode data to string
                                e1.printStackTrace();
                            } *//*catch (JSONException e2) {
                                                // returned data is not JSONObject?
                                                e2.printStackTrace();
                                            }*//*

                        }

                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        error.printStackTrace();
                        Toast.makeText(getApplicationContext(),
                                "Something went wrong at server end", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("Content-Type", "application/json");
                        return params;
                    }
                };
                new applicationClass().getInstance().addToRequestQueue(jsonArrayRequest);

            }*/

    public String imageVariable(){
        return "yes";
    }
    public String completedView(){
        return "yes";
    }
    public boolean checkLog(){ return true; }
    public boolean writeJsonFile(){ return false; }
    public String AutoSync(){ return "false"; }
    public int AutoSyncFreq(){ return 15; }
    public boolean fabButton(){ return false; }
    public String Notification(){ return "true"; }
    public String defaultNFC(){return "NFC";}
    public String insertTask(){return "insertTask.php";}
    public String insertPPMTask(){return "ppmTaskUpload.php";}

/*public String urlString(){
        return "http://192.168.1.10/punctualiti/net/csmia_temp/";
    }*/

    public String urlString(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N){
            return "http://punctualiti.in/csmia/";
        } else {
            return "http://punctualiti.in/csmia/";
        }

    }
}

