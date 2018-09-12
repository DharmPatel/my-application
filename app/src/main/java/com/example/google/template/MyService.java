package com.example.google.template;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class MyService extends Service {
    Handler mHandler;
    HomePage homePage;
    DatabaseHelper myDb;
    SQLiteDatabase db;
    SharedPreferences settings;
    String Task_Status="Completed";
    boolean internetConnection = false;
    String companyId="",site_id="",UserId="";
    NotificationCompat.Builder builder;
    boolean isRunning = false;
    boolean isUploading = false;
    int countvalue =0;
    int level=0;
    char res;
    String carrierName="";
    WifiManager wifiManager;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        homePage=new HomePage();
        myDb= new DatabaseHelper(getApplicationContext());
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        companyId = settings.getString("Company_Customer_Id", null);
        UserId = settings.getString("userId", null);
        site_id = myDb.Site_Location_Id(UserId);
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        isRunning=true;
        isUploading=true;
        this.mHandler = new Handler();
        m_Runnable.run();
        return START_STICKY;
    }
    private final Runnable m_Runnable = new Runnable() {

        public void run()
        {
            if(isRunning== true)
            {
                if(isUploading== true)
                {
                    new DownloadWebPageTask().execute();
                }
                mHandler.postDelayed(this, SyncFreq() * 60 * 1000);//10sec Or 10,000 ms
            }
            else{
                mHandler.removeCallbacks(m_Runnable);
            }
        }
    };

    public int SyncFreq(){
        int SyncFreq=0;
        try {
            String query = "SELECT SyncFreq FROM AutoSync";
            db = myDb.getWritableDatabase();
            Cursor res =db.rawQuery(query, null);
            if (res.moveToFirst()) {
                do {
                    SyncFreq=res.getInt(res.getColumnIndex("SyncFreq"));
                } while (res.moveToNext());
            }
            res.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SyncFreq;
    }
    private class DownloadWebPageTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                URL url = new URL("https://www.google.com/");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(1500);
                connection.connect();
                if(connection.getResponseCode() == 200){
                    internetConnection = true;
                }else {
                    internetConnection = false;
                }
            }catch (Exception E){
                E.printStackTrace();
                internetConnection = false;
            }

            return internetConnection;


        }
        @Override
        protected void onPostExecute(Boolean result) {

            if(result == true){
                isUploading=false;
                uploadData();
            }else {
                if(myDb.Notification(UserId).trim().equals("true")){
                    messageStyleNotification();
                }
            }
        }



    }
    public void messageStyleNotification() {

        try {
            int NOTIFICATION_ID = 1;
            builder = new NotificationCompat.Builder(this);
            builder.setSmallIcon(R.drawable.applogo);
            builder.setColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.sync));
            builder.setContentTitle("Synchrinization Failed");
            builder.setContentText("Data synchronization failed due to no internet connetion.");
            builder.setAutoCancel(true);
            PendingIntent launchIntent = getLaunchIntent(NOTIFICATION_ID, getBaseContext());
            builder.setContentIntent(launchIntent);
            buildNotification(NOTIFICATION_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void messageStyleNotification1() {
        try {
            int NOTIFICATION_ID = 2;
            builder = new NotificationCompat.Builder(this);
            builder.setSmallIcon(R.drawable.applogo);
            builder.setColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.sync));
            builder.setContentTitle("Synchrinization Successful");
            builder.setContentText("Data is synchronized successfully..");
            builder.setAutoCancel(true);
            PendingIntent launchIntent = getLaunchIntent(NOTIFICATION_ID, getBaseContext());
            builder.setContentIntent(launchIntent);
            buildNotification(NOTIFICATION_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void buildNotification(int NOTIFICATION_ID) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
    public PendingIntent getLaunchIntent(int notificationId, Context context) {

        Intent intent = new Intent(context, HomePage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("notificationId", notificationId);
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }
    public void imageServer(String TaskId, String ServerId) {

        //Create AsycHttpClient object
        try {
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();

            JSONObject jsonImage = new JSONObject();
            String image_str;

            Iterator it = myDb.Images(TaskId).entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> pair = (Map.Entry) it.next();
                System.out.println(pair.getKey() + " = " + pair.getValue());

                try {
                    jsonImage.put("image", pair.getKey());
                    jsonImage.put("imageType", pair.getValue());
                    jsonImage.put("TaskId", TaskId);
                    jsonImage.put("ServerTaskId", ServerId);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                params.put("image_json", jsonImage);
                //generateNoteOnSD(getApplicationContext(),"JsonValue1.txt",params.toString());

                client.post(new applicationClass().urlString() + "addTaskSelfie.php", params, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        // TODO Auto-generated method stub

                        if (statusCode == 404) {
                            Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                        } else if (statusCode == 500) {
                            Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        System.out.println(responseString + " jason response1");
                        try {
                            JSONArray array = new JSONArray(responseString);
                            System.out.println(array.length());
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = (JSONObject) array.get(i);
                                System.out.println(obj.get("TaskId"));
                                String taskId = obj.get("TaskId").toString();
                                String data = obj.get("data").toString();
                                myDb.updateImages(taskId, data);
                                if (obj.get("data").equals("yes")) {
                                    Toast.makeText(getApplicationContext(), " Data is Synchronised!", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "No data in SQLite DB, please do enter User name to perform Sync action", Toast.LENGTH_LONG).show();
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void uploadData(){
        countvalue++;
        myDb= new DatabaseHelper(getApplicationContext());
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        JSONArray uploadData = new JSONArray();
        JSONObject Ticketdata = new JSONObject();
        JSONObject AssetDetails = new JSONObject();
        JSONObject AssetStatusChange = new JSONObject();
        try {
            uploadData = composeJSONfortaskDetailsReconfigNew(UserId);
            Ticketdata.put("Ticketdata", myDb.composeJSONforTicket(UserId));
            AssetDetails.put("AssetDetails", myDb.composeJSONforAssets(UserId));
            AssetStatusChange.put("AssetStatusLog", myDb.composeJSONforAssetsStatusChange(myDb.Site_Location_Id(UserId),UserId));
            uploadData.put(Ticketdata);
            uploadData.put(AssetDetails);
            uploadData.put(AssetStatusChange);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        params.put("Sync_Info", myDb.SyncInfo(UserId,getBatteryPercentage(),NetworkStrength(),haveNetworkConnection(), CarrierName(), DeviceDetails()));
        params.put("Sync_Count",countvalue);
        params.put("Task_Details", uploadData);
        client.post(new applicationClass().urlString() + new applicationClass().insertTask(), params, new TextHttpResponseHandler() {//http://eclockwork.in/inserttask.php
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // TODO Auto-generated method stub

                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Internet Connection Lost.Synchronization failed.", Toast.LENGTH_LONG).show();
                    countvalue=0;
                    isUploading=true;
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                db = myDb.getWritableDatabase();
                try {
                    //generateNoteOnSD(getApplicationContext(), "responseString.txt", responseString.toString());
                    JSONObject jsonObjTask = new JSONObject(responseString);
                    try {
                        JSONArray task = jsonObjTask.getJSONArray("TaskDetails");
                        if (task != null) {
                            for (int i = 0; i < task.length(); i++) {
                                JSONObject obj = (JSONObject) task.get(i);
                                String Auto_Id = obj.get("Auto_Id").toString();
                                String server_task_id = obj.get("server_task_id").toString();
                                String data = obj.get("Status").toString();
                                if (obj.get("Status").equals("yes")) {
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("UpdatedStatus", data);
                                    db.update("Task_Details", contentValues, "Auto_Id ='" + Auto_Id + "'", null);
                                    if (new applicationClass().imageVariable().equals("yes")) {
                                        imageServer(Auto_Id, server_task_id);
                                    }
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        JSONArray meter = jsonObjTask.getJSONArray("MeterReading");
                        if (meter != null) {
                            for (int i = 0; i < meter.length(); i++) {
                                JSONObject obj = (JSONObject) meter.get(i);
                                String Task_Id = obj.get("Task_Id").toString();
                                String data = obj.get("Status").toString();
                                if (obj.get("Status").equals("yes")) {
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("UpdatedStatus", data);
                                    db.update("Meter_Reading", contentValues, "Task_Id ='" + Task_Id + "'", null);
                                }

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        JSONArray datavaluesJson = jsonObjTask.getJSONArray("DataValue");
                        if (datavaluesJson != null) {
                            for (int i = 0; i < datavaluesJson.length(); i++) {
                                JSONObject obj = (JSONObject) datavaluesJson.get(i);
                                String Task_Id = obj.get("Task_Id").toString();
                                String data = obj.get("Status").toString();
                                if (obj.get("Status").equals("yes")) {
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("UpdatedStatus", data);
                                    db.update("Data_Posting", contentValues, "Task_Id ='" + Task_Id + "'", null);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        JSONArray alert = jsonObjTask.getJSONArray("AlertData");
                        for (int i = 0; i < alert.length(); i++) {
                            JSONObject obj = (JSONObject) alert.get(i);
                            String Task_Id = obj.get("Task_Id").toString();
                            String data = obj.get("Status").toString();
                            if (obj.get("Status").equals("yes")) {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("UpdatedStatus", data);
                                db.update("AlertMaster", contentValues, "Task_Id ='" + Task_Id + "'", null);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        JSONArray Ticket = jsonObjTask.getJSONArray("Ticketdata");
                        if (Ticket != null) {
                            for (int i = 0; i < Ticket.length(); i++) {
                                JSONObject obj = (JSONObject) Ticket.get(i);
                                String ID = obj.get("ID").toString();
                                String data = obj.get("Status").toString();

                                if (obj.get("Status").equals("yes")) {
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("UpdatedStatus", data);
                                    db.update("Ticket_Master", contentValues, "ID ='" + ID + "'", null);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        final JSONArray AssetDetails = jsonObjTask.getJSONArray("AssetDetails");
                        if (AssetDetails != null) {
                            db.execSQL("DELETE FROM Asset_Details");
                                String sql = "insert into Asset_Details (Asset_Id ,Site_Location_Id ,Asset_Code ,Asset_Name ,Asset_Location ,Asset_Status_Id ,Status ,Manual_Time , Asset_Update_Time ,UpdatedStatus)values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
                                db.beginTransaction();
                                SQLiteStatement stmt = db.compileStatement(sql);
                                for (int i = 0; i < AssetDetails.length(); i++) {
                                    JSONObject c = AssetDetails.getJSONObject(i);
                                    String Asset_Id = c.getString("Asset_Id");
                                    String Site_Location_Id = c.getString("Site_Location_Id");
                                    String Asset_Code = c.getString("Asset_Code");
                                    String Asset_Name = c.getString("Asset_Name");
                                    String Asset_Location = c.getString("Asset_Location");
                                    String Asset_Status_Id = c.getString("Asset_Status_Id");
                                    String Status = c.getString("Status");

                                    String selectQuery = "SELECT  * FROM Asset_Details where Asset_Id = '" + Asset_Id + "'";
                                    Cursor cursor = db.rawQuery(selectQuery, null);
                                    stmt.bindString(1, Asset_Id);
                                    stmt.bindString(2, Site_Location_Id);
                                    stmt.bindString(3, Asset_Code);
                                    stmt.bindString(4, Asset_Name);
                                    stmt.bindString(5, Asset_Location);
                                    stmt.bindString(6, Asset_Status_Id);
                                    stmt.bindString(7, Status);
                                    stmt.bindString(8, "");
                                    stmt.bindString(9, "");
                                    stmt.bindString(10, "");
                                    long entryID = stmt.executeInsert();
                                    stmt.clearBindings();
                                    cursor.close();
                                }
                                String sqlquery = "DELETE FROM Asset_Details WHERE Id NOT IN (SELECT MIN(Id) FROM Asset_Details GROUP BY Asset_Id);";
                                db.rawQuery(sqlquery, null);
                                db.execSQL(sqlquery);
                                db.setTransactionSuccessful();
                                db.endTransaction();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    try {
                        JSONArray AssetStatusLog = jsonObjTask.getJSONArray("AssetStatusLog");
                        if (AssetStatusLog != null) {
                            for (int i = 0; i < AssetStatusLog.length(); i++) {
                                JSONObject obj = (JSONObject) AssetStatusLog.get(i);
                                String ID = obj.get("ID").toString();
                                String data = obj.get("Status").toString();
                                if (obj.get("Status").equals("yes")) {
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("UpdatedStatus", data);
                                    db.update("AssetStatusLog", contentValues, "Id ='" + ID + "'", null);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                /*String selectQuery = "SELECT  * FROM Task_Details where UpdatedStatus = 'no' and Task_Status='" + Task_Status + "' and  Assigned_To_User_Id='" + UserId + "'";
                Cursor cursor = db.rawQuery(selectQuery, null);
                int cursorValue = cursor.getCount();
                db.close();
                if (cursorValue > 0) {
                    uploadData();
                } else if (cursorValue == 0) {
                    if (Task_Status == "Completed") {
                        Task_Status = "Unplanned";
                        uploadData();
                    } else if (Task_Status == "Unplanned") {
                        Task_Status = "Cancelled";
                        uploadData();
                    } else if (Task_Status == "Cancelled") {
                        Task_Status = "Missed";
                        uploadData();
                    } else {
                        Toast.makeText(getApplicationContext(),"Data uploaded Successfully.",Toast.LENGTH_SHORT).show();
                        *//*messageStyleNotification1();*//*
                        isUploading=true;
                        if (myDb.Notification(UserId).trim().equals("true")){
                            messageStyleNotification1();
                        }
                    }
                }*/

                try {
                    String selectQuery = "SELECT  * FROM Task_Details where UpdatedStatus = 'no'";
                    Cursor cursor = db.rawQuery(selectQuery, null);
                    int cursorValue = cursor.getCount();
                    db.close();

                    if (cursorValue > 0) {
                        uploadData();
                    } else {
                        Toast.makeText(getApplicationContext(),"Data uploaded Successfully.",Toast.LENGTH_SHORT).show();
                        countvalue=0;
                        isUploading=true;
                        if (myDb.Notification(UserId).trim().equals("true")){
                            messageStyleNotification1();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }
    public JSONArray composeJSONfortaskDetailsReconfigNew(String UserId){
        JSONArray UploadArray = new JSONArray();
        JSONObject TaskDetails = new JSONObject();
        JSONObject MeterReading = new JSONObject();
        JSONObject DataValue = new JSONObject();
        JSONObject AlertData = new JSONObject();

        JSONArray TaskJsonArray = new JSONArray();
        JSONArray MeterReadingArray = new JSONArray();
        JSONArray DataPostingArray = new JSONArray();
        JSONArray AlertArray = new JSONArray();
        try {
            String[]loggerData = new LoggerFile().loggerFunction(UserId);
            //String selectQuery = "SELECT * FROM Task_Details where UpdatedStatus = 'no' and Assigned_To_User_Id='"+UserId+"' and Task_Status='"+Task_Status+"' ORDER BY Task_Scheduled_Date LIMIT 20" ;
            String selectQuery = "SELECT  * FROM Task_Details where Task_Status <> 'Pending' and UpdatedStatus = 'no' ORDER BY CASE WHEN Task_Status='Completed' THEN 1 WHEN Task_Status='Unplanned' THEN 2 WHEN Task_Status='Cancelled' THEN 3 WHEN Task_Status='Missed' THEN 4 END,Task_Status ASC LIMIT 5"  ;
            myDb= new DatabaseHelper(getApplicationContext());
            db=myDb.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    JSONObject TaskJsonObject = new JSONObject();
                    String TaskID= cursor.getString(cursor.getColumnIndex("Auto_Id"));
                    TaskJsonObject.put("Auto_Id", cursor.getString(cursor.getColumnIndex("Auto_Id")));
                    TaskJsonObject.put("Activity_Frequency_Id", cursor.getString(cursor.getColumnIndex("Activity_Frequency_Id")));
                    TaskJsonObject.put("Task_Start_At", cursor.getString(cursor.getColumnIndex("Task_Start_At")));
                    TaskJsonObject.put("Task_Scheduled_Date", cursor.getString(cursor.getColumnIndex("Task_Scheduled_Date")));
                    TaskJsonObject.put("Task_Status", cursor.getString(cursor.getColumnIndex("Task_Status")));
                    TaskJsonObject.put("Asset_Id", cursor.getString(cursor.getColumnIndex("Asset_Id")));
                    TaskJsonObject.put("Scan_Type", cursor.getString(cursor.getColumnIndex("Scan_Type")));
                    TaskJsonObject.put("Assigned_To_User_Id", cursor.getString(cursor.getColumnIndex("Assigned_To_User_Id")));
                    TaskJsonObject.put("Assigned_To_User_Group_Id", cursor.getString(cursor.getColumnIndex("Assigned_To_User_Group_Id")));
                    TaskJsonObject.put("GeoLoc", loggerData[6]);



                    String selectQuery1 = "SELECT  Meter_Reading.* FROM Meter_Reading LEFT JOIN  Task_Details ON Meter_Reading.Task_Id = Task_Details.Auto_Id WHERE Task_Details.Assigned_To_User_Id ='"+UserId+"' AND Meter_Reading.Task_Id ='"+TaskID+"' AND Meter_Reading.UpdatedStatus = 'no'  " ;
                    StringBuffer sbMeterForm_Structure_Id = new StringBuffer();
                    StringBuffer sbMeterReading = new StringBuffer();
                    StringBuffer sbUOM = new StringBuffer();
                    Cursor cursorMeterReading = db.rawQuery(selectQuery1, null);
                    JSONObject MeterReadingJsonObject = new JSONObject();
                    if (cursorMeterReading.moveToFirst()) {
                        do {
                            sbMeterForm_Structure_Id.append(cursorMeterReading.getString(cursorMeterReading.getColumnIndex("Form_Structure_Id"))).append("|");
                            sbMeterReading.append(cursorMeterReading.getString(cursorMeterReading.getColumnIndex("Reading"))).append("|");
                            sbUOM.append(cursorMeterReading.getString(cursorMeterReading.getColumnIndex("UOM"))).append("|");
                            MeterReadingJsonObject.put("Task_Id",cursorMeterReading.getString(cursorMeterReading.getColumnIndex("Task_Id")));
                            MeterReadingJsonObject.put("Asset_Id", cursorMeterReading.getString(cursorMeterReading.getColumnIndex("Asset_Id")));
                            MeterReadingJsonObject.put("Form_Structure_Id",sbMeterForm_Structure_Id.toString());
                            MeterReadingJsonObject.put("Reading", sbMeterReading.toString());
                            MeterReadingJsonObject.put("UOM",sbUOM.toString());
                            MeterReadingJsonObject.put("Reset", cursorMeterReading.getString(cursorMeterReading.getColumnIndex("Reset")));
                            MeterReadingJsonObject.put("Activity_Frequency_Id", cursorMeterReading.getString(cursorMeterReading.getColumnIndex("Activity_Frequency_Id")));

                        } while (cursorMeterReading.moveToNext());

                    }
                    MeterReadingArray.put(MeterReadingJsonObject);
                    cursorMeterReading.close();


                    StringBuffer sbForm_Structure_Id = new StringBuffer();
                    StringBuffer sbValue = new StringBuffer();
                    JSONObject DatPostingJsonObject = new JSONObject();
                    String selectQueryData = "SELECT  Data_Posting.* FROM Data_Posting LEFT JOIN  Task_Details ON Data_Posting.Task_Id = Task_Details.Auto_Id WHERE Task_Details.Assigned_To_User_Id = '"+UserId+"' AND Data_Posting.UpdatedStatus = 'no' AND Data_Posting.Task_Id ='"+TaskID+"' ";
                    Cursor cursorDataPosting = db.rawQuery(selectQueryData, null);
                    if (cursorDataPosting.moveToFirst()) {
                        do {
                            DatPostingJsonObject.put("Task_Id", cursorDataPosting.getString(cursorDataPosting.getColumnIndex("Task_Id")));
                            DatPostingJsonObject.put("Form_Id", cursorDataPosting.getString(cursorDataPosting.getColumnIndex("Form_Id")));
                            sbForm_Structure_Id.append(cursorDataPosting.getString(cursorDataPosting.getColumnIndex("Form_Structure_Id"))).append("|");
                            sbValue.append(cursorDataPosting.getString(cursorDataPosting.getColumnIndex("Value"))).append("|");
                            DatPostingJsonObject.put("Form_Structure_Id", sbForm_Structure_Id.toString());
                            DatPostingJsonObject.put("Value", sbValue.toString());
                        } while (cursorDataPosting.moveToNext());
                    }
                    DataPostingArray.put(DatPostingJsonObject);
                    cursorDataPosting.close();

                    StringBuffer sbAlertForm_Structure_Id = new StringBuffer();
                    JSONObject AlertJsonObject = new JSONObject();
                    String selectQueryAlert = "SELECT * FROM AlertMaster WHERE Created_By_Id='"+UserId+"' AND Task_Id='"+TaskID+"'";
                    Cursor cursorAlert = db.rawQuery(selectQueryAlert, null);
                    if (cursorAlert.moveToFirst()) {
                        do {
                            sbAlertForm_Structure_Id.append(cursorAlert.getString(cursorAlert.getColumnIndex("Form_Structure_Id"))).append("|");
                            AlertJsonObject.put("Form_Structure_Id",sbAlertForm_Structure_Id.toString());
                            AlertJsonObject.put("Task_Id",cursorAlert.getString(cursorAlert.getColumnIndex("Task_Id")));
                            AlertJsonObject.put("Form_Id", cursorAlert.getString(cursorAlert.getColumnIndex("Form_Id")));
                            AlertJsonObject.put("Alert_Type", cursorAlert.getString(cursorAlert.getColumnIndex("Alert_Type")));
                            AlertJsonObject.put("Critical", cursorAlert.getString(cursorAlert.getColumnIndex("Critical")));
                        } while (cursorAlert.moveToNext());
                    }
                    AlertArray.put(AlertJsonObject);
                    cursorAlert.close();
                    TaskJsonArray.put(TaskJsonObject);
                } while (cursor.moveToNext());
            }cursor.close();
            db.close();

        }catch (Exception E){
            E.printStackTrace();
        }

        try {

            TaskDetails.put("TaskDetails",TaskJsonArray);
            MeterReading.put("MeterReading",MeterReadingArray);
            DataValue.put("DataValue",DataPostingArray);
            AlertData.put("AlertData",AlertArray);

            UploadArray.put(TaskDetails);
            UploadArray.put(MeterReading);
            UploadArray.put(DataValue);
            UploadArray.put(AlertData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return UploadArray;
    }
    private int getBatteryPercentage() {

        BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                context.unregisterReceiver(this);
                int currentLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                 level = -1;
                if (currentLevel >= 0 && scale > 0) {
                    level = (currentLevel * 100) / scale;
                }
            }
        };
        IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryLevelReceiver, batteryLevelFilter);
        return level;
    }
    public int NetworkStrength(){
        wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifi= wifiManager.getConnectionInfo();
        int linkSpeed = wifiManager.getConnectionInfo().getRssi();
        return linkSpeed;
    }
    public String CarrierName() {
        if(haveNetworkConnection()=='W'){
            wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifi= wifiManager.getConnectionInfo();
            carrierName = wifi.getSSID().replace("\"","");

        }
        else{
            TelephonyManager manager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            carrierName = manager.getNetworkOperatorName();
        }
        return carrierName;
    }
    public char haveNetworkConnection() {
        res = 'M';

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected()) {
                    res = 'W';
                }
            if (ni.getTypeName().equalsIgnoreCase("MOBILE")) {
                if (ni.isConnected())
                    res = 'M';
            }
        }
        //res1= String.valueOf(res);
        return res;
    }
    public String DeviceDetails(){
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        return manufacturer+model;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning=false;
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        mHandler.removeCallbacks(m_Runnable);
        mHandler.postDelayed(m_Runnable, 5000);
    }
}