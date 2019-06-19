package com.example.google.csmia_temp;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
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

/**
 * Created by chandan on 10/6/2017.
 */
public class MyIntentService extends IntentService {
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

    public MyIntentService() {
        super("dasf");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        homePage=new HomePage();
        myDb= new DatabaseHelper(getApplicationContext());
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        companyId = settings.getString("Company_Customer_Id", null);
        site_id = settings.getString("site_id", null);
        UserId = settings.getString("userId", null);
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        isRunning=true;
        this.mHandler = new Handler();
        m_Runnable.run();
    }

    private final Runnable m_Runnable = new Runnable() {
        public void run()
        {
            if(isRunning== true)
            {
                Log.d("dafsfadsf","1");
                //new DownloadWebPageTask().execute();
               // mHandler.postDelayed(this, 1000);//10sec Or 10,000 ms
            }
            else{
                //mHandler.removeCallbacks(m_Runnable);
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
                uploadData();
            }else {
                if(myDb.Notification(UserId).trim().equals("true")){
                    messageStyleNotification();
                }
            }
        }



    }

    public void messageStyleNotification() {

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
    }
    public void messageStyleNotification1() {

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





    }

    /* public void uploadData(){
         Toast.makeText(getApplicationContext(),"fsdaf",Toast.LENGTH_LONG).show();
     }*/
    public void uploadData(){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        JSONArray uploadData = new JSONArray();
        JSONObject Ticketdata = new JSONObject();
        JSONObject AssetDetails = new JSONObject();
        try {
            uploadData = composeJSONfortaskDetailsReconfigNew(UserId,Task_Status);
            Ticketdata.put("Ticketdata", myDb.composeJSONTicket(UserId));
            /*Ticketdata.put("Ticketdata", myDb.composeJSONforTicket(User_Id));*/
            AssetDetails.put("AssetDetails", myDb.composeJSONforAssets(UserId));
            uploadData.put(Ticketdata);
            uploadData.put(AssetDetails);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.put("Task_Details", uploadData);
        client.post(new applicationClass().urlString() + "insertTaskv1.0.php", params, new TextHttpResponseHandler() {//http://eclockwork.in/inserttask.php
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // TODO Auto-generated method stub

                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), " Unexpected Error occcured! Syncronization failed.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                db = myDb.getWritableDatabase();
                try {
                    //generateNoteOnSD(getApplicationContext(), "responseString.txt", responseString.toString());
                    JSONObject jsonObjTask = new JSONObject(responseString);
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

                    final JSONArray AssetDetails = jsonObjTask.getJSONArray("AssetDetails");
                    if (AssetDetails != null) {
                        db.execSQL("DELETE FROM Asset_Details");
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                String selectQuery = "SELECT  * FROM Task_Details where UpdatedStatus = 'no' and Task_Status='" + Task_Status + "' and  Assigned_To_User_Id='" + UserId + "'";
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
                        Toast.makeText(getApplicationContext(),"Uploaded with"+SyncFreq()*1000+"ms",Toast.LENGTH_SHORT).show();
                        /*messageStyleNotification1();*/
                        if (myDb.Notification(UserId).trim().equals("true")){
                            messageStyleNotification1();
                        }
                    }
                }
            }

        });
    }

    public JSONArray composeJSONfortaskDetailsReconfigNew(String UserId,String Task_Status){
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

            String selectQuery = "SELECT  * FROM Task_Details where UpdatedStatus = 'no' and Task_Status='"+Task_Status+"' ORDER BY Task_Scheduled_Date LIMIT 5" ;
            db=myDb.getWritableDatabase();

            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    JSONObject TaskJsonObject = new JSONObject();
                    String TaskID= cursor.getString(cursor.getColumnIndex("Auto_Id"));
                    TaskJsonObject.put("Auto_Id", cursor.getString(cursor.getColumnIndex("Auto_Id")));
                    TaskJsonObject.put("Company_Customer_Id",cursor.getString(cursor.getColumnIndex("Company_Customer_Id")));
                    TaskJsonObject.put("Site_Location_Id",cursor.getString(cursor.getColumnIndex("Site_Location_Id")));
                    TaskJsonObject.put("Activity_Frequency_Id", cursor.getString(cursor.getColumnIndex("Activity_Frequency_Id")));
                    TaskJsonObject.put("Task_Start_At", cursor.getString(cursor.getColumnIndex("Task_Start_At")));
                    TaskJsonObject.put("Task_Scheduled_Date", cursor.getString(cursor.getColumnIndex("Task_Scheduled_Date")));
                    TaskJsonObject.put("Task_Status", cursor.getString(cursor.getColumnIndex("Task_Status")));
                    TaskJsonObject.put("Asset_Id", cursor.getString(cursor.getColumnIndex("Asset_Id")));
                    TaskJsonObject.put("Scan_Type", cursor.getString(cursor.getColumnIndex("Scan_Type")));
                    TaskJsonObject.put("Assigned_To", cursor.getString(cursor.getColumnIndex("Assigned_To")));
                    TaskJsonObject.put("Assigned_To_User_Id", cursor.getString(cursor.getColumnIndex("Assigned_To_User_Id")));
                    TaskJsonObject.put("Assigned_To_User_Group_Id", cursor.getString(cursor.getColumnIndex("Assigned_To_User_Group_Id")));
                    TaskJsonObject.put("Record_Status",loggerData[0] );
                    TaskJsonObject.put("Last_Date_Time",loggerData[1]);
                    TaskJsonObject.put("Last_IP",loggerData[2]);
                    TaskJsonObject.put("Last_User_Id",loggerData[3]);
                    TaskJsonObject.put("Update_Location",loggerData[4]);
                    TaskJsonObject.put("Apk_Web_Version", loggerData[5]);
                    TaskJsonObject.put("GeoLoc", loggerData[6]);


                    String selectQuery1 = "SELECT  Meter_Reading.* FROM Meter_Reading LEFT JOIN  Task_Details ON Meter_Reading.Task_Id = Task_Details.Auto_Id WHERE Meter_Reading.Task_Id ='"+TaskID+"' AND Meter_Reading.UpdatedStatus = 'no'  " ;

                    Cursor cursorMeterReading = db.rawQuery(selectQuery1, null);

                    if (cursorMeterReading.moveToFirst()) {
                        do {
                            JSONObject MeterReadingJsonObject = new JSONObject();

                            MeterReadingJsonObject.put("Task_Id",cursorMeterReading.getString(cursorMeterReading.getColumnIndex("Task_Id")));
                            MeterReadingJsonObject.put("Asset_Id", cursorMeterReading.getString(cursorMeterReading.getColumnIndex("Asset_Id")));
                            MeterReadingJsonObject.put("Form_Structure_Id", cursorMeterReading.getString(cursorMeterReading.getColumnIndex("Form_Structure_Id")));
                            MeterReadingJsonObject.put("Reading", cursorMeterReading.getString(cursorMeterReading.getColumnIndex("Reading")));
                            MeterReadingJsonObject.put("UOM", cursorMeterReading.getString(cursorMeterReading.getColumnIndex("UOM")));
                            MeterReadingJsonObject.put("Reset", cursorMeterReading.getString(cursorMeterReading.getColumnIndex("Reset")));
                            MeterReadingJsonObject.put("Activity_Frequency_Id", cursorMeterReading.getString(cursorMeterReading.getColumnIndex("Activity_Frequency_Id")));
                            MeterReadingJsonObject.put("Record_Status",loggerData[0] );
                            MeterReadingJsonObject.put("Last_Date_Time",loggerData[1]);
                            MeterReadingJsonObject.put("Last_IP",loggerData[2]);
                            MeterReadingJsonObject.put("Last_User_Id",loggerData[3]);
                            MeterReadingJsonObject.put("Update_Location",loggerData[4]);
                            MeterReadingJsonObject.put("Apk_Web_Version", loggerData[5]);
                            MeterReadingJsonObject.put("GeoLoc", loggerData[6]);
                            MeterReadingArray.put(MeterReadingJsonObject);
                        } while (cursorMeterReading.moveToNext());

                    }
                    cursorMeterReading.close();

                    String selectQueryData = "SELECT  Data_Posting.* FROM Data_Posting LEFT JOIN  Task_Details ON Data_Posting.Task_Id = Task_Details.Auto_Id WHERE Data_Posting.UpdatedStatus = 'no' AND Data_Posting.Task_Id ='"+TaskID+"' ";
                    Cursor cursorDataPosting = db.rawQuery(selectQueryData, null);
                    if (cursorDataPosting.moveToFirst()) {
                        do {
                            JSONObject DatPostingJsonObject = new JSONObject();
                            DatPostingJsonObject.put("Task_Id", cursorDataPosting.getString(cursorDataPosting.getColumnIndex("Task_Id")));
                            DatPostingJsonObject.put("Form_Id",cursorDataPosting.getString(cursorDataPosting.getColumnIndex("Form_Id")));
                            DatPostingJsonObject.put("Form_Structure_Id",cursorDataPosting.getString(cursorDataPosting.getColumnIndex("Form_Structure_Id")));
                            DatPostingJsonObject.put("Parameter_Id", cursorDataPosting.getString(cursorDataPosting.getColumnIndex("Parameter_Id")));
                            DatPostingJsonObject.put("Value", cursorDataPosting.getString(cursorDataPosting.getColumnIndex("Value")));
                            DatPostingJsonObject.put("Record_Status",loggerData[0] );
                            DatPostingJsonObject.put("Last_Date_Time",loggerData[1]);
                            DatPostingJsonObject.put("Last_IP",loggerData[2]);
                            DatPostingJsonObject.put("Last_User_Id",loggerData[3]);
                            DatPostingJsonObject.put("Update_Location",loggerData[4]);
                            DatPostingJsonObject.put("Apk_Web_Version", loggerData[5]);
                            DatPostingJsonObject.put("GeoLoc", loggerData[6]);
                            DataPostingArray.put(DatPostingJsonObject);
                        } while (cursorDataPosting.moveToNext());
                    }
                    cursorDataPosting.close();

                    String selectQueryAlert = "SELECT * FROM AlertMaster WHERE Task_Id='"+TaskID+"'";
                    Cursor cursorAlert = db.rawQuery(selectQueryAlert, null);
                    if (cursorAlert.moveToFirst()) {
                        do {
                            JSONObject AlertJsonObject = new JSONObject();
                            AlertJsonObject.put("Task_Id",cursorAlert.getString(cursorAlert.getColumnIndex("Task_Id")));
                            AlertJsonObject.put("Form_Id", cursorAlert.getString(cursorAlert.getColumnIndex("Form_Id")));
                            AlertJsonObject.put("Form_Structure_Id", cursorAlert.getString(cursorAlert.getColumnIndex("Form_Structure_Id")));
                            AlertJsonObject.put("Alert_Type", cursorAlert.getString(cursorAlert.getColumnIndex("Alert_Type")));
                            AlertJsonObject.put("Created_By_Id", cursorAlert.getString(cursorAlert.getColumnIndex("Created_By_Id")));
                            AlertJsonObject.put("TaskType", cursorAlert.getString(cursorAlert.getColumnIndex("TaskType")));
                            AlertJsonObject.put("Critical", cursorAlert.getString(cursorAlert.getColumnIndex("Critical")));
                            AlertJsonObject.put("Record_Status",loggerData[0] );
                            AlertJsonObject.put("Last_Date_Time",loggerData[1]);
                            AlertJsonObject.put("Last_IP",loggerData[2]);
                            AlertJsonObject.put("Last_User_Id",loggerData[3]);
                            AlertJsonObject.put("Update_Location",loggerData[4]);
                            AlertJsonObject.put("Apk_Web_Version", loggerData[5]);
                            AlertJsonObject.put("GeoLoc", loggerData[6]);
                            AlertArray.put(AlertJsonObject);
                        } while (cursorAlert.moveToNext());
                    }
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

   /* @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning=false;
        mHandler.removeCallbacks(m_Runnable);
        mHandler.postDelayed(m_Runnable, 5000);
    }*/
}
