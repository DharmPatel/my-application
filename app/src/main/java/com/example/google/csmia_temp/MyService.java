package com.example.google.csmia_temp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
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
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.google.csmia_temp.ConstantList.DatabaseColumn;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class MyService extends Service {
    static final boolean LOG = new applicationClass().checkLog();
    static final boolean WRITE_JSON_FILE = new applicationClass().writeJsonFile();
    private static final String TAG = HomePage.class.getSimpleName();
    Handler mHandler;
    HomePage homePage;
    DatabaseHelper myDb;
    static boolean sync = false;
    SQLiteDatabase db;
    SharedPreferences settings;
    String Task_Status="Completed";
    boolean internetConnection = false;
    String URLSTRING = applicationClass.URL;
    String companyId="",site_id="",User_Id="";
    NotificationCompat.Builder builder;
    //boolean isRunning = false;
    boolean isUploading = false;
    int countvalue =0;
    int level=0;
    char res;
    String carrierName="";
    WifiManager wifiManager;
    String AssetId="",Asset_Name = "",data="",Form_Structure_Id="",Task_Id="",Form_Ticket_Id="",Type="",Subject="",Group_Id = "",Product = "",IncidentSource = "",LoggedBy = "";

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
        User_Id = settings.getString("userId", null);
        site_id = myDb.Site_Location_Id(User_Id);
        //Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        //isRunning=true;
        isUploading=true;
////////////////////////////////////////////////////////////////////////////
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isUploading){
                    Log.d("MyServices","CompletedTime");
                    //new CheckingInternetConnectivity().execute();
                    new DownloadWebPageTask().execute();
                }
                mHandler.postDelayed(this,SyncFreq() * 60 * 1000);
            }
        },SyncFreq() * 60 * 1000);
        ////////////////////////////////////////////////////////////////////////////
        return START_STICKY;
    }

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
                Task_Status="Completed";
                isUploading=false;
                uploadData();
            }else {
                if(myDb.Notification(User_Id).trim().equals("true")){
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
        intent.putExtra("User_Id",User_Id);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("notificationId", notificationId);
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    public void imageServer(String TaskId) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        JSONObject jsonImage = new JSONObject();
        Iterator it = myDb.Images(TaskId).entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> pair = (Map.Entry) it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());

            try {
                jsonImage.put("image", pair.getKey());
                jsonImage.put("imageType", pair.getValue());
                jsonImage.put("TaskId", TaskId);
                // jsonImage.put("ServerTaskId", ServerId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            params.put("image_json", jsonImage);
            params.put("Site_Location_Id", myDb.Site_Location_Id(User_Id));
            generateNoteOnSD(getApplicationContext(), "addTaskSelfiev1.0.txt", params.toString());
            client.post(new applicationClass().urlString() + "android/addTaskSelfiev1.0.php", params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    // TODO Auto-generated method stub

                    if (statusCode == 404) {
                        Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();

                    } else if (statusCode == 500) {
                        Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "Internet Connection Lost.Synchronization failed.", Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    System.out.println(responseString + " jason response1");
                    Log.d("imageResponse",":: "+responseString);
                    try {

                        JSONArray array = new JSONArray(responseString);
                        System.out.println(array.length());
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = (JSONObject) array.get(i);
                            System.out.println(obj.get("TaskId"));
                            String taskId = obj.get("TaskId").toString();
                            String data = obj.get("data").toString();
                            myDb.updateImages(taskId, data);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            });
        }
    }

    private class DataDownload extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // pDialog.setMessage("Downloading Data. Please Wait..");
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Resources res = getResources();
            // pDialog.setProgressDrawable(res.getDrawable(R.color.ColorPrimary));
            //pDialog.setIn
/*android:progressTint="#4d34ff"
        android:progressBackgroundTint="#4d34ff"*/
            // pDialog.

            /*switch (values[2]){
                case 0:
                    pDialog.setMessage("Downloading Uploaded Task. Please Wait..");
                    pDialog.setProgressPercentFormat(null);
                    break;

                case 1:
                    pDialog.setMessage("Downloading Readings. Please Wait..");
                    pDialog.setProgressPercentFormat(null);
                    break;

                case 2:
                    pDialog.setMessage("Downloading Previous Data. Please Wait..");
                    pDialog.setProgressPercentFormat(null);
                    break;

                case 3:
                    pDialog.setMessage("Downloading Activities. Please Wait..");
                    pDialog.setProgressPercentFormat(null);
                    break;

                case 4:
                    pDialog.setMessage("Downloading Assets. Please Wait..");
                    pDialog.setProgressPercentFormat(null);
                    break;

                case 5:
                    pDialog.setMessage("Downloading AssetsLoaction. Please Wait..");
                    pDialog.setProgressPercentFormat(null);
                    break;

                case 6:
                    pDialog.setMessage("Downloading Forms. Please Wait..");
                    pDialog.setProgressPercentFormat(null);
                    break;
                case 7:
                    pDialog.setMessage("Downloading  Parameters. Please Wait..");
                    pDialog.setProgressPercentFormat(null);
                    break;
                case 8:
                    pDialog.setMessage("Downloading  FeedbackScore. Please Wait..");
                    pDialog.setProgressPercentFormat(null);
                    break;
                case 9:
                    pDialog.setMessage("Downloading  Score. Please Wait..");
                    pDialog.setProgressPercentFormat(null);
                    break;
                case 10:
                    pDialog.setMessage("Downloading  CoversionFactors. Please Wait..");
                    pDialog.setProgressPercentFormat(null);
                    break;
                case 11:
                    pDialog.setMessage("Downloading  PPM Task. Please Wait..");
                    pDialog.setProgressPercentFormat(null);
                    break;

                case 12:
                    pDialog.setMessage("Getting Data From Server. Please Wait..");
                    break;

                default:
                    break;

            }

            pDialog.setMax(values[1]);
            pDialog.setProgress(values[0]);
            // pDialog.setProgressNumberFormat(null);
*/


        }
        @Override
        protected String doInBackground(String... URL) {
            HttpHandler handler = new HttpHandler();
            publishProgress(0,100,12);
            String jsonTaskDone = handler.getTaskDetailsServer(myDb.UserGroupId(User_Id), new applicationClass().yymmdd(), myDb.Site_Location_Id(User_Id));
            publishProgress(33,100,12);
            String jsonStrTask = handler.taskDataCall(myDb.UserGroupId(User_Id), myDb.Site_Location_Id(User_Id), myDb.SiteURL(User_Id));
            publishProgress(66,100,12);
            String PPMTaskjson = handler.getPPmTask(myDb.UserGroupId(User_Id), myDb.Site_Location_Id(User_Id));
            publishProgress(100,100,12);
            if (jsonTaskDone != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonTaskDone);
                    try {

                        JSONArray task = jsonObj.getJSONArray("TaskDetailsDone");
                        if(LOG)Log.d("DATasValue",task+"");

                        if (!task.toString().equals("[]")) {


                            db = myDb.getWritableDatabase();
                            String sql = "insert into Task_Details_Server(Task_Id ,Site_Location_Id,Activity_Frequency_Id,Asset_Id ,Task_Scheduled_Date ,Task_Status ,Assigned_To_User_Id, Assigned_To_User_Group_Id,Task_Start_At ,Remarks,UpdatedStatus)values(?, ?, ?, ?, ?, ?,?, ?, ?, ?,?);";
                            db.beginTransaction();
                            SQLiteStatement stmt = db.compileStatement(sql);
                            for (int i = 0; i < task.length(); i++) {

                                // pDialog.setProgress(i+1);
                                JSONObject c = task.getJSONObject(i);
                                String Auto_Id = c.getString("Auto_Id");
                                String Activity_Frequency_Id = c.getString("Activity_Frequency_Id");
                                String Asset_Id = c.getString("Asset_Id");
                                String Task_Scheduled_Date = c.getString("Task_Scheduled_Date");
                                String Task_Status = c.getString("Task_Status");
                                String Assigned_To_User_Id = c.getString("Assigned_To_User_Id");
                                String Assigned_To_User_Group_Id = c.getString("Assigned_To_User_Group_Id");
                                String Task_Start_At = c.getString("Task_Start_At");
                                String Remarks = c.getString("Remarks");
                                String Activity_Type = c.getString("Activity_Type");

                                stmt.bindString(1, Auto_Id);
                                stmt.bindString(2, myDb.Site_Location_Id(User_Id));
                                stmt.bindString(3, Activity_Frequency_Id);
                                stmt.bindString(4, Asset_Id);
                                stmt.bindString(5, Task_Scheduled_Date);
                                stmt.bindString(6, Task_Status);
                                stmt.bindString(7, Assigned_To_User_Id);
                                stmt.bindString(8, Assigned_To_User_Group_Id);
                                stmt.bindString(9, Task_Start_At);
                                stmt.bindString(10, Remarks);
                                stmt.bindString(11, "no");

                                long entryID = stmt.executeInsert();
                                stmt.clearBindings();

                                Log.d("Act_Type","Task: "+Activity_Type);
                                if(Activity_Type.equalsIgnoreCase("CheckList")){
                                    ContentValues cv = new ContentValues();
                                    cv.put("Auto_Id",Auto_Id);
                                    cv.put("Site_Location_Id",myDb.Site_Location_Id(User_Id));
                                    cv.put("Activity_Frequency_Id", Activity_Frequency_Id);
                                    cv.put("Task_Scheduled_Date", Task_Scheduled_Date);
                                    cv.put("Task_Status", Task_Status);
                                    cv.put("Task_Start_At", Task_Start_At);
                                    cv.put("Assigned_To_User_Id", Assigned_To_User_Id);
                                    cv.put("Assigned_To_User_Group_Id", Assigned_To_User_Group_Id);
                                    cv.put("Remarks", Remarks);
                                    cv.put("Activity_Type",Activity_Type);
                                    cv.put("UpdatedStatus","yes");
                                    String taskquery = "Select * from Task_Details where Auto_Id = '"+Auto_Id+"' and Activity_Type = 'CheckList'";
                                    Cursor cursor = db.rawQuery(taskquery,null);
                                    if(cursor.getCount() == 0){
                                        long insertTD = db.insert("Task_Details",null,cv);
                                        Log.d("InsertTD"," "+insertTD);
                                    }
                                    cursor.close();
                                }
                            }

                            //pDialog.setProgress(jumptime);
                           /* String sqlquery = "DELETE FROM Task_Details_Server WHERE Id NOT IN (SELECT MIN(Id) FROM Task_Details_Server GROUP BY Activity_Frequency_Id,Task_Scheduled_Date) AND Site_Location_Id='"+myDb.Site_Location_Id(User_Id)+"'";
                            db.rawQuery(sqlquery, null);
                            db.execSQL(sqlquery);*/
                            db.setTransactionSuccessful();
                            db.endTransaction();
                            db.close();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        db.endTransaction();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        db.endTransaction();
                    }


                    try {

                        JSONArray meterReading = jsonObj.getJSONArray("MeterReading");

                        if (!meterReading.toString().equals("[]")) {

                            db = myDb.getWritableDatabase();
                            String sql = "insert into Meter_Reading(Task_Id,Site_Location_Id,Asset_Id ,Form_Structure_Id ,Reading ,UOM ,Reset ,Activity_Frequency_Id ,Task_Start_At ,UpdatedStatus )values(?, ?,?,?, ?, ?, ?, ?, ?, ?);";
                            db.beginTransaction();
                            SQLiteStatement stmt = db.compileStatement(sql);
                            for (int i = 0; i < meterReading.length(); i++) {
                                JSONObject c = meterReading.getJSONObject(i);
                                //pDialog.setProgress(i+1);


                                String Asset_Id = c.getString("Asset_Id");
                                String Task_Id = c.getString("Task_Id");
                                String Activity_Frequency_Id = c.getString("Activity_Frequency_Id");
                                String Form_Structure_Id = c.getString("Form_Structure_Id");
                                String Reading = c.getString("Reading");
                                String UOM = c.getString("UOM");
                                String Task_Start_At = c.getString("Task_Start_At");
                                stmt.bindString(1, Task_Id);
                                stmt.bindString(2, myDb.Site_Location_Id(User_Id));
                                stmt.bindString(3, Asset_Id);
                                stmt.bindString(4, Form_Structure_Id);
                                stmt.bindString(5, Reading);
                                stmt.bindString(6, UOM);
                                stmt.bindString(7, "");
                                stmt.bindString(8, Activity_Frequency_Id);
                                stmt.bindString(9, Task_Start_At);
                                stmt.bindString(10, "yes");
                                long entryID = stmt.executeInsert();
                                stmt.clearBindings();
                            }

                            String sqlquery = "DELETE FROM Meter_Reading WHERE Id NOT IN (SELECT MIN(Id) FROM Meter_Reading GROUP BY Activity_Frequency_Id,Form_Structure_Id,Task_Start_At) AND Site_Location_Id='"+myDb.Site_Location_Id(User_Id)+"'";
                            db.rawQuery(sqlquery, null);
                            db.execSQL(sqlquery);
                            db.setTransactionSuccessful();
                            db.endTransaction();
                            db.close();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        db.endTransaction();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        db.endTransaction();
                    }
                    try {
                        JSONArray dataPosting = jsonObj.getJSONArray("DataPosting");
                        if (!dataPosting.toString().equals("")) {
                            db = myDb.getWritableDatabase();
                            String sql = "insert into Data_Posting(Task_Id,Site_Location_Id,Form_Id ,Form_Structure_Id ,Parameter_Id ,Value ,UOM,UpdatedStatus )values(?,?,?,?, ?, ?, ?, ?);";
                            db.beginTransaction();
                            SQLiteStatement stmt = db.compileStatement(sql);

                            for (int i = 0; i < dataPosting.length(); i++) {
                                JSONObject c1 = dataPosting.getJSONObject(i);
                                //pDialog.setProgress(i+1);

                                String Auto_Id = c1.getString("Auto_Id");
                                String Task_Id = c1.getString("Task_Id");
                                String Form_Id = c1.getString("Form_Id");
                                String Form_Structure_Id = c1.getString("Form_Structure_Id");
                                String Parameter_Id = c1.getString("Parameter_Id");
                                String Value = c1.getString("Value");
                                String UOM = c1.getString("UOM");
                                stmt.bindString(1, Task_Id);
                                stmt.bindString(2, myDb.Site_Location_Id(User_Id));
                                stmt.bindString(3, Form_Id);
                                stmt.bindString(4, Form_Structure_Id);
                                stmt.bindString(5, Parameter_Id);
                                stmt.bindString(6, Value);
                                stmt.bindString(7, UOM);
                                stmt.bindString(8, "yes");
                                long entryID = stmt.executeInsert();
                                stmt.clearBindings();
                            }

                            String sqlquery = "DELETE FROM Data_Posting WHERE Id NOT IN (SELECT MIN(Id) FROM Data_Posting GROUP BY Task_Id,Form_Structure_Id) AND Site_Location_Id='"+myDb.Site_Location_Id(User_Id)+"'";
                            db.rawQuery(sqlquery, null);
                            db.execSQL(sqlquery);

                            db.setTransactionSuccessful();
                            db.endTransaction();
                            db.close();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        db.endTransaction();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        db.endTransaction();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (jsonStrTask != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStrTask);
                    try {
                        JSONArray task = jsonObj.getJSONArray("TaskFeqeuncy");
                        if(LOG)Log.d("DATasValue123",task+"");
                        if (task != null) {

                          /*  pDialog.setMessage("Downloading Activities. Please Wait..");
                            pDialog.setMax(task.length());
                            pDialog.setProgress(0);*/

                            db = myDb.getWritableDatabase();
                            String Frequencysql = "insert into Activity_Frequency (Site_Location_Id ,Frequency_Auto_Id ,YearStartson ,TimeStartson ,TimeEndson ,Activity_Duration ,Grace_Duration_Before ,Grace_Duration_After ,RepeatEveryDay ,RepeatEveryMin ,RepeatEveryMonth ,Assign_Days ,Asset_Activity_Linking_Id,RecordStatus ,UpdatedStatus )values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
                            String AssetActivityLinkingsql = "insert into Asset_Activity_Linking (Site_Location_Id ,Auto_Id ,Asset_Id ,Activity_Id,RecordStatus ,UpdatedStatus )values(?,?,?,?,?,?);";
                            String ActivityMastersql = "insert into Activity_Master (Site_Location_Id ,Auto_Id ,Form_Id ,Activity_Name,Activity_Type,RecordStatus ,UpdatedStatus )values(?,?,?,?,?,?,?);";
                            String AssetActivityAssignedTosql = "insert into Asset_Activity_AssignedTo (Site_Location_Id ,Auto_Id ,Assigned_To_User_Id ,Assigned_To_User_Group_Id ,Asset_Activity_Linking_Id,RecordStatus ,UpdatedStatus )values(?,?,?,?,?,?,?);";
                            String PpmTasksql = "insert into PPM_Task (Auto_Id, Site_Location_Id, Activity_Frequency, Task_Date,Task_End_Date, Task_Status, Asset_Activity_Linking_Id,  Assigned_To_User_Id, Assigned_To_User_Group_Id, TimeStartson, Activity_Duration, Grace_Duration_Before, Grace_Duration_After, Record_Status, UpdatedStatus)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
                            db.beginTransaction();
                            SQLiteStatement Frequencystmt = db.compileStatement(Frequencysql);
                            SQLiteStatement AssetActivityLinkingstmt = db.compileStatement(AssetActivityLinkingsql);
                            SQLiteStatement ActivityMasterstmt = db.compileStatement(ActivityMastersql);
                            SQLiteStatement AssetActivityAssignedTostmt = db.compileStatement(AssetActivityAssignedTosql);
                            SQLiteStatement PpmTaskstmt = db.compileStatement(PpmTasksql);

                            for (int i = 0; i < task.length(); i++) {
                                //pDialog.setProgress(i+1);

                                JSONObject c = task.getJSONObject(i);

                                //////////////Activity Frequency Insert in Database//////////////////
                                String Site_Location_Id = c.getString("FrequencySiteLocation");
                                String Frequency_Auto_Id = c.getString("Frequency_Auto_Id");
                                String YearStartson = c.getString("FrequencyYearStartson");
                                String TimeStartson = c.getString("FrequencyTimeStartson");
                                String TimeEndson = c.getString("FrequencyTimeEndson");
                                int Activity_Duration = c.getInt("FrequencyActivity_Duration");
                                int Grace_Duration_Before = c.getInt("FrequencyGrace_Duration_Before");
                                int Grace_Duration_After = c.getInt("FrequencyGrace_Duration_After");
                                int RepeatEveryDay = c.getInt("FrequencyRepeatEveryDay");
                                String RepeatEveryMin = c.getString("FrequencyRepeatEveryMin");
                                String RepeatEveryMonth = c.getString("FrequencyRepeatEveryMonth");
                                String Assign_Days = c.getString("FrequencyAssignDays");
                                String FrequencyAsset_Activity_Linking_Id = c.getString("FrequencyAsset_Activity_Linking_Id");
                                String FrequencyRecordStatus = c.getString("FrequencyRecordStatus");


                                Frequencystmt.bindString(1, Site_Location_Id);
                                Frequencystmt.bindString(2, Frequency_Auto_Id);
                                Frequencystmt.bindString(3, YearStartson);
                                Frequencystmt.bindString(4, TimeStartson);
                                Frequencystmt.bindString(5, TimeEndson);
                                Frequencystmt.bindLong(6, Activity_Duration);
                                Frequencystmt.bindLong(7, Grace_Duration_Before);
                                Frequencystmt.bindLong(8, Grace_Duration_After);
                                Frequencystmt.bindLong(9, RepeatEveryDay);
                                Frequencystmt.bindString(10, RepeatEveryMin);
                                Frequencystmt.bindString(11, RepeatEveryMonth);
                                Frequencystmt.bindString(12, Assign_Days);
                                Frequencystmt.bindString(13, FrequencyAsset_Activity_Linking_Id);
                                Frequencystmt.bindString(14, FrequencyRecordStatus);
                                Frequencystmt.bindString(15, "no");

                                long FrequencyentryID = Frequencystmt.executeInsert();
                                Frequencystmt.clearBindings();

                                if(LOG)Log.d("FrequencyInseted",FrequencyentryID+"");

                                //////////////END Activity Frequency Insert in Database//////////////////

                                //////////////Asset Activity Linking Insert in Database//////////////////
                                String AssetActivityLinkingAutoId = c.getString("AssetActivityLinkingAutoId");
                                String AssetActivityLinkingAssetId = c.getString("AssetActivityLinkingAssetId");

                                String AssetActivityLinkingRecordStatus = c.getString("AssetActivityLinkingRecordStatus");
                                String AssetActivityLinkingActivity_Id = c.getString("AssetActivityLinkingActivity_Id");
                                AssetActivityLinkingstmt.bindString(1, Site_Location_Id);
                                AssetActivityLinkingstmt.bindString(2, AssetActivityLinkingAutoId);
                                AssetActivityLinkingstmt.bindString(3, AssetActivityLinkingAssetId);
                                AssetActivityLinkingstmt.bindString(4, AssetActivityLinkingActivity_Id);
                                AssetActivityLinkingstmt.bindString(5, AssetActivityLinkingRecordStatus);
                                AssetActivityLinkingstmt.bindString(6, "no");

                                long AssetActivityLinkingentryID = AssetActivityLinkingstmt.executeInsert();
                                AssetActivityLinkingstmt.clearBindings();

                                if(LOG)Log.d("AssetInserted",AssetActivityLinkingentryID+"");
                                ////////////// END Asset Activity Linking Insert in Database//////////////////


                                //////////////Activity Master Insert in Database//////////////////
                                String ActivityMasterAutoId = c.getString("ActivityMasterAutoId");
                                String ActivityMasterFormId = c.getString("ActivityMasterFormId");
                                String ActivityMasterActivityName = c.getString("ActivityMasterActivityName");
                                String ActivityMasterActivityType = c.getString("ActivityMasterActivityType");
                                String ActivityMasterRecordStatus = c.getString("ActivityMasterRecordStatus");

                                ActivityMasterstmt.bindString(1, Site_Location_Id);
                                ActivityMasterstmt.bindString(2, ActivityMasterAutoId);
                                ActivityMasterstmt.bindString(3, ActivityMasterFormId);
                                ActivityMasterstmt.bindString(4, ActivityMasterActivityName);
                                ActivityMasterstmt.bindString(5, ActivityMasterActivityType);
                                ActivityMasterstmt.bindString(6, ActivityMasterRecordStatus);
                                ActivityMasterstmt.bindString(7, "no");

                                long ActivityMasterentryID = ActivityMasterstmt.executeInsert();
                                ActivityMasterstmt.clearBindings();

                                if(LOG)Log.d("ActivityMasteInseted",ActivityMasterentryID+"");

                                //////////////END Activity Master Insert in Database//////////////////

                                ////////////// Asset Activity Assigned To Insert in Database//////////////////
                                String AssetActivityAssignedToAutoId = c.getString("AssetActivityAssignedToAutoId");
                                String AssetActivityAssignedToUserId = c.getString("AssetActivityAssignedToUserId");
                                String AssetActivityAssignedToUserGroupId = c.getString("AssetActivityAssignedToUserGroupId");
                                String AssetActivityAssignedToAsset_Activity_Linking_Id = c.getString("AssetActivityAssignedToAsset_Activity_Linking_Id");
                                String AssetActivityAssignedToRecordStatus = c.getString("AssetActivityAssignedToRecordStatus");

                                AssetActivityAssignedTostmt.bindString(1, Site_Location_Id);
                                AssetActivityAssignedTostmt.bindString(2, AssetActivityAssignedToAutoId);
                                AssetActivityAssignedTostmt.bindString(3, AssetActivityAssignedToUserId);
                                AssetActivityAssignedTostmt.bindString(4, AssetActivityAssignedToUserGroupId);
                                AssetActivityAssignedTostmt.bindString(5, AssetActivityAssignedToAsset_Activity_Linking_Id);
                                AssetActivityAssignedTostmt.bindString(6, AssetActivityAssignedToRecordStatus);
                                AssetActivityAssignedTostmt.bindString(7, "no");

                                long AssetActivityAssignedToentryID = AssetActivityAssignedTostmt.executeInsert();
                                AssetActivityAssignedTostmt.clearBindings();
                                if(LOG)Log.d("AssignedInseted",AssetActivityAssignedToentryID+"");

                                //////////////END  Asset Activity Assigned To Insert in Database//////////////////



                            }

                            String Activity_Frequencyquery = "DELETE FROM Activity_Frequency WHERE Id NOT IN (SELECT MIN(Id) FROM Activity_Frequency GROUP BY Frequency_Auto_Id) AND Site_Location_Id='"+myDb.Site_Location_Id(User_Id)+"'";
                            String Asset_Activity_Linkingquery = "DELETE FROM Asset_Activity_Linking WHERE Id NOT IN (SELECT MIN(Id) FROM Asset_Activity_Linking GROUP BY Auto_Id) AND Site_Location_Id='"+myDb.Site_Location_Id(User_Id)+"'";
                            String Activity_Mastergquery = "DELETE FROM Activity_Master WHERE Id NOT IN (SELECT MIN(Id) FROM Activity_Master GROUP BY Auto_Id) AND Site_Location_Id='"+myDb.Site_Location_Id(User_Id)+"'";
                            String Asset_Activity_AssignedToquery = "DELETE FROM Asset_Activity_AssignedTo WHERE Id NOT IN (SELECT MIN(Id) FROM Asset_Activity_AssignedTo GROUP BY Auto_Id) AND Site_Location_Id='"+myDb.Site_Location_Id(User_Id)+"'";

                            String[] statements = new String[]{Activity_Frequencyquery, Asset_Activity_Linkingquery,Activity_Mastergquery,Asset_Activity_AssignedToquery};

                            for(String sql : statements){
                                db.rawQuery(sql, null);
                                db.execSQL(sql);
                            }
                            //db.rawQuery(sqlquery, null);
                            //db.execSQL(sqlquery);
                            db.setTransactionSuccessful();
                            db.endTransaction();
                            db.close();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        db.endTransaction();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        db.endTransaction();
                    }

                    try {

                        JSONArray assetDetailsJson = jsonObj.getJSONArray("AssetDetails");
                        if(LOG)Log.d("AssetDetails"," "+assetDetailsJson.toString());

                        if (assetDetailsJson != null) {

                            db = myDb.getWritableDatabase();
                            String sql = "insert into Asset_Details (Asset_Id ,Site_Location_Id ,Asset_Code ,Asset_Name ,Asset_Location ,Building_Id,Floor_Id,Room_Id,Asset_Status_Id ,Asset_Type,Status ,Manual_Time , Asset_Update_Time ,UpdatedStatus)values (?,?, ?, ?, ?, ?, ?, ?,?,?,?, ?, ?, ?);";
                            db.beginTransaction();
                            SQLiteStatement stmt = db.compileStatement(sql);
                            for (int i = 0; i < assetDetailsJson.length(); i++) {

                                JSONObject c = assetDetailsJson.getJSONObject(i);
                                String Asset_Id = c.getString("Asset_Id");
                                String Site_Location_Id = c.getString("Site_Location_Id");
                                String Asset_Code = c.getString("Asset_Code");
                                String Asset_Name = c.getString("Asset_Name");
                                String Asset_Location = c.getString("Asset_Location");
                                String Building_Id =c.getString("Building");
                                String Floor_Id =c.getString("Floor");
                                String Room_Id =c.getString("Room_No");
                                String Asset_Status_Id = c.getString("Asset_Status_Id");
                                String Asset_Type = c.getString("Asset_Type");
                                String Status = c.getString("Status");

                                /*String selectQuery = "SELECT  * FROM Asset_Details where Asset_Id = '" + Asset_Id + "'";
                                Cursor cursor = db.rawQuery(selectQuery, null);*/
                                if(LOG)Log.d("AssetDfsdf",Asset_Location+" "+Building_Id+" "+Floor_Id+" "+Room_Id);
                                stmt.bindString(1, Asset_Id);
                                stmt.bindString(2, Site_Location_Id);
                                stmt.bindString(3, Asset_Code);
                                stmt.bindString(4, Asset_Name);
                                stmt.bindString(5, Asset_Location);
                                stmt.bindString(6, Building_Id);
                                stmt.bindString(7, Floor_Id);
                                stmt.bindString(8, Room_Id);
                                stmt.bindString(9, Asset_Status_Id);
                                stmt.bindString(10, Asset_Type);
                                stmt.bindString(11, Status);
                                stmt.bindString(12, "");
                                stmt.bindString(13, "");
                                stmt.bindString(14, "");
                                long entryID = stmt.executeInsert();
                                stmt.clearBindings();
                                //cursor.close();
                            }

                            String sqlquery = "DELETE FROM Asset_Details WHERE Id NOT IN (SELECT MIN(Id) FROM Asset_Details GROUP BY Asset_Id) AND Site_Location_Id='"+myDb.Site_Location_Id(User_Id)+"'";
                            db.rawQuery(sqlquery, null);
                            db.execSQL(sqlquery);
                            db.setTransactionSuccessful();
                            db.endTransaction();
                            db.close();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        db.endTransaction();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        db.endTransaction();
                    }


                    try {
                        JSONArray assetLocationJson = jsonObj.getJSONArray("AssetLocation");
                        if(LOG)Log.d("AssetLocation"," "+assetLocationJson.toString());
                        if (assetLocationJson != null) {

                            db = myDb.getWritableDatabase();
                            String sql = "insert into Asset_Location (Asset_Id, Building_Id ,Floor_Id ,Room_Id ,building_code, building_name, floor_code, floor_name,room_area,Site_Location_Id)values (?,?,?,?,?,?,?,?,?,?);";
                            db.beginTransaction();
                            SQLiteStatement stmt = db.compileStatement(sql);
                            for (int i = 0; i < assetLocationJson.length(); i++) {

                                JSONObject c = assetLocationJson.getJSONObject(i);
                                String Asset_Id = c.getString("Asset_Id");
                                String Building_Id = c.getString("building");
                                String Floor_Id = c.getString("floor");
                                String Room_Id = c.getString("room_no");
                                String building_code = c.getString("building_code");
                                String building_name = c.getString("building_name");
                                String floor_code = c.getString("floor_code");
                                String floor_name = c.getString("floor_name");
                                String room_area = c.getString("room_area");
                                String Site_Location_Id = c.getString("Site_Location_Id");

                                if(LOG)Log.d("Asdegrg",assetLocationJson.length()+" "+Building_Id+" "+Floor_Id+" "+building_code+" "
                                        +floor_code+" "+room_area+" "+Site_Location_Id);

                                /*String selectQuery = "SELECT * FROM Asset_Location where Site_Location_Id = '" + myDb.Site_Location_Id(User_Id) + "'";
                                Cursor cursor = db.rawQuery(selectQuery, null);*/
                                stmt.bindString(1, Asset_Id);
                                stmt.bindString(2, Building_Id);
                                stmt.bindString(3, Floor_Id);
                                stmt.bindString(4, Room_Id);
                                stmt.bindString(5, building_code);
                                stmt.bindString(6, building_name);
                                stmt.bindString(7, floor_code);
                                stmt.bindString(8, floor_name);
                                stmt.bindString(9, room_area);
                                stmt.bindString(10,Site_Location_Id);
                                long entryID = stmt.executeInsert();
                                stmt.clearBindings();
                                //cursor.close();
                            }

                            /*String sqlquery = "DELETE FROM Asset_Location WHERE Id NOT IN (SELECT MIN(Id) FROM Asset_Location GROUP BY Building_Id) AND Site_Location_Id='"+myDb.Site_Location_Id(User_Id)+"'";
                            db.rawQuery(sqlquery, null);
                            db.execSQL(sqlquery);*/
                            db.setTransactionSuccessful();
                            db.endTransaction();
                            db.close();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        db.endTransaction();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        db.endTransaction();
                    }
                    try {
                        JSONArray fromStructure = jsonObj.getJSONArray("Form_Structure");
                        /*String selectQuery1 = "SELECT  * FROM Form_Structure";
                        db = myDb.getWritableDatabase();
                        Cursor cursor1 = db.rawQuery(selectQuery1, null);
                        if (cursor1.getCount() == 0) {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("Field_Id", "");
                            db.insert("Form_Structure", null, contentValues);
                        }
                        cursor1.close();
                        db.close();*/

                        if (fromStructure != null) {

                            db = myDb.getWritableDatabase();
                            String sql = "insert into Form_Structure (Field_Id,Site_Location_Id,Form_Id,Field_Label ,Field_Type ,Field_Options ,Mandatory ,FixedValue,sid ,sections ,Display_Order ,SafeRange,Calculation,IMS_Id,Record_Status)values (?, ?,?,?, ?, ?, ?,?, ?, ?, ?, ?, ?,?,?);";
                            db.beginTransaction();
                            SQLiteStatement stmt = db.compileStatement(sql);

                            for (int k = 0; k < fromStructure.length(); k++) {

                                JSONObject c2 = fromStructure.getJSONObject(k);

                                String Field_Id = c2.getString("Field_Id");

                                String Form_Id = c2.getString("Form_Id");
                                if(LOG)Log.d("asdasdasd",Form_Id+" FormId");
                                String Field_Label = c2.getString("Field_Label");
                                String Field_Type = c2.getString("Field_Type");
                                String Field_Options = c2.getString("Field_Options");
                                String Mandatory = c2.getString("Mandatory");
                                String FixedValue = c2.getString("FixedValue");
                                String sid = c2.getString("sid");
                                String sections = c2.getString("sections");
                                String Display_Order = c2.getString("Disp_Order");
                                String Safe_Range = c2.getString("SafeRange");
                                String Calculation = c2.getString("calculation");
                                String IMS_Id = c2.getString("IMS_Id");
                                String Record_Status = c2.getString("Record_Status");
                                Log.d("IMS",IMS_Id+" IMS_ID");


                                stmt.bindString(1, Field_Id);
                                stmt.bindString(2, myDb.Site_Location_Id(User_Id));
                                stmt.bindString(3, Form_Id);
                                stmt.bindString(4, Field_Label);
                                stmt.bindString(5, Field_Type);
                                stmt.bindString(6, Field_Options);
                                stmt.bindString(7, Mandatory);
                                stmt.bindString(8, FixedValue);
                                stmt.bindString(9, sid);
                                stmt.bindString(10, sections);
                                stmt.bindString(11, Display_Order);
                                stmt.bindString(12, Safe_Range);
                                stmt.bindString(13,Calculation);
                                stmt.bindString(14, IMS_Id);
                                stmt.bindString(15,Record_Status);
                                long entryID = stmt.executeInsert();
                                stmt.clearBindings();

                            }

                            String sqlquery = "DELETE FROM Form_Structure WHERE Id NOT IN (SELECT MIN(Id) FROM Form_Structure GROUP BY Form_Id ,Field_Id) AND Site_Location_Id='"+myDb.Site_Location_Id(User_Id)+"'";
                            db.rawQuery(sqlquery, null);
                            db.execSQL(sqlquery);
                            db.setTransactionSuccessful();
                            db.endTransaction();
                            db.close();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        db.endTransaction();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        db.endTransaction();
                    }

                    try {
                        JSONArray parameter = jsonObj.getJSONArray("Parameter");
                        if (parameter != null) {

                            db = myDb.getWritableDatabase();
                            String sql = "insert into Parameter (Site_Location_Id ,Activity_Frequency_Id,Form_Id,Form_Structure_Id,Field_Limit_From ,Field_Limit_To ,Threshold_From ,Threshold_To ,Validation_Type ,Critical ,Field_Option_Id )values(?, ?, ?, ?, ?,?,?, ?, ?, ?, ?)";
                            db.beginTransaction();
                            SQLiteStatement stmt = db.compileStatement(sql);
                            for (int j = 0; j < parameter.length(); j++) {
                                //pDialog.setProgress(j+1);

                                JSONObject c1 = parameter.getJSONObject(j);
                                String Activity_Frequency_Id = c1.getString("Activity_Frequency_Id");
                                String Form_Id = c1.getString("Form_Id");
                                String Form_Structure_Id = c1.getString("Form_Structure_Id");
                                String Field_Limit_From = c1.getString("Field_Limit_From");
                                String Field_Limit_To = c1.getString("Field_Limit_To");
                                String Threshold_From = c1.getString("Threshold_From");
                                String Threshold_To = c1.getString("Threshold_To");
                                String Validation_Type = c1.getString("Validation_Type");
                                String Critical = c1.getString("Critical");
                                String Field_Option_Id = c1.getString("Field_Option_Id");
                                stmt.bindString(1, myDb.Site_Location_Id(User_Id));
                                stmt.bindString(2, Activity_Frequency_Id);
                                stmt.bindString(3, Form_Id);
                                stmt.bindString(4, Form_Structure_Id);
                                stmt.bindString(5, Field_Limit_From);
                                stmt.bindString(6, Field_Limit_To);
                                stmt.bindString(7, Threshold_From);
                                stmt.bindString(8, Threshold_To);
                                stmt.bindString(9, Validation_Type);
                                stmt.bindString(10, Critical);
                                stmt.bindString(11,Field_Option_Id);
                                long entryID = stmt.executeInsert();
                                if(LOG)Log.d(TAG,"Parameter"+"Parameter Downloading..."+entryID);
                                stmt.clearBindings();

                            }

                            String sqlquery = "DELETE FROM Parameter WHERE Id NOT IN (SELECT MIN(Id) FROM Parameter GROUP BY Activity_Frequency_Id,Form_Id,Form_Structure_Id) AND Site_Location_Id='"+myDb.Site_Location_Id(User_Id)+"'";
                            db.rawQuery(sqlquery, null);
                            db.execSQL(sqlquery);
                            db.setTransactionSuccessful();
                            db.endTransaction();
                            db.close();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        db.endTransaction();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        db.endTransaction();
                    }

                    try {
                        JSONArray FeedBackScore = jsonObj.getJSONArray("FeedBackScore");
                        if (FeedBackScore != null) {

                            db = myDb.getWritableDatabase();
                            String sql = "insert into feedback_score (Feedbaack_Auto_Id,Score,FeedBackName)values(?,?,?);";
                            db.beginTransaction();
                            SQLiteStatement stmt = db.compileStatement(sql);
                            for (int i = 0; i < FeedBackScore.length(); i++) {

                                JSONObject c = FeedBackScore.getJSONObject(i);
                                String Feedbaack_Auto_Id = c.getString("Auto_Id");
                                String Score = c.getString("Score");
                                String FeedBackName = c.getString("FeedBackName");

                                stmt.bindString(1, Feedbaack_Auto_Id);
                                stmt.bindString(2, Score);
                                stmt.bindString(3, FeedBackName);
                                long entryID = stmt.executeInsert();
                                if(LOG)Log.d(TAG, "FeedBackScore" + "FeedBackScore Downloading...");
                                stmt.clearBindings();
                            }

                            String sqlquery = "DELETE FROM feedback_score WHERE Id NOT IN (SELECT MIN(Id) FROM feedback_score GROUP BY Feedbaack_Auto_Id)";
                            db.rawQuery(sqlquery, null);
                            db.execSQL(sqlquery);
                            db.setTransactionSuccessful();
                            db.endTransaction();
                            db.close();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        db.endTransaction();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        db.endTransaction();
                    }

                    try {
                        JSONArray Score = jsonObj.getJSONArray("Score");
                        if (Score != null) {

                            db = myDb.getWritableDatabase();
                            String sql = "insert into pun_score (Score_Auto_Id,Form_Structure_Id,Option_value,Option_Id,Score)values(?,?,?,?,?);";
                            db.beginTransaction();
                            SQLiteStatement stmt = db.compileStatement(sql);
                            for (int i = 0; i < Score.length(); i++) {

                                JSONObject c = Score.getJSONObject(i);
                                String Score_Auto_Id = c.getString("Auto_Id");
                                String Form_Struct_Id = c.getString("Form_Structure_Id");
                                String Option = c.getString("Option_value");
                                String Option_Id = c.getString("Option_Id");
                                String Score_Value = c.getString("Score");

                                stmt.bindString(1, Score_Auto_Id);
                                stmt.bindString(2, Form_Struct_Id);
                                stmt.bindString(3, Option);
                                stmt.bindString(4, Option_Id);
                                stmt.bindString(5, Score_Value);
                                long entryID = stmt.executeInsert();
                                if(LOG)Log.d(TAG, "Score" + "Score Downloading..."+entryID);
                                stmt.clearBindings();
                            }

                            String sqlquery = "DELETE FROM pun_score WHERE Id NOT IN (SELECT MIN(Id) FROM pun_score GROUP BY Score_Auto_Id)";
                            db.rawQuery(sqlquery, null);
                            db.execSQL(sqlquery);
                            db.setTransactionSuccessful();
                            db.endTransaction();
                            db.close();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        db.endTransaction();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        db.endTransaction();
                    }


                    try {
                        JSONArray Conversion = jsonObj.getJSONArray("Conversion");
                        if (Conversion != null) {

                            db = myDb.getWritableDatabase();
                            String sql = "insert into Measurement_Conversion (Conversion_Auto_Id, Source_UOM, Multiplication_Factor, Add_Factor, Subtraction_Factor, Division_Factor, Target_UOM)values(?,?,?,?,?,?,?);";
                            db.beginTransaction();
                            SQLiteStatement stmt = db.compileStatement(sql);
                            for (int i = 0; i < Conversion.length(); i++) {

                                JSONObject c = Conversion.getJSONObject(i);
                                String Conversion_Auto_Id = c.getString("Auto_Id");
                                String Source_UOM = c.getString("Source_UOM");
                                String Multiplication_Factor = c.getString("Multiplication_Factor");
                                String Add_Factor = c.getString("Add_Factor");
                                String Subtraction_Factor = c.getString("Subtraction_Factor");
                                String Division_Factor = c.getString("Division_Factor");
                                String Target_UOM = c.getString("Target_UOM");

                                stmt.bindString(1, Conversion_Auto_Id);
                                stmt.bindString(2, Source_UOM);
                                stmt.bindString(3, Multiplication_Factor);
                                stmt.bindString(4, Add_Factor);
                                stmt.bindString(5, Subtraction_Factor);
                                stmt.bindString(6, Division_Factor);
                                stmt.bindString(7, Target_UOM);
                                long entryID = stmt.executeInsert();
                                if(LOG)Log.d(TAG, "Conversion" + "Conversion Values Downloading..."+entryID);
                                stmt.clearBindings();
                            }

                            String sqlquery = "DELETE FROM Measurement_Conversion WHERE Id NOT IN (SELECT MIN(Id) FROM Measurement_Conversion GROUP BY Conversion_Auto_Id)";
                            db.rawQuery(sqlquery, null);
                            db.execSQL(sqlquery);
                            db.setTransactionSuccessful();
                            db.endTransaction();
                            db.close();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        db.endTransaction();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        db.endTransaction();
                    }

                    try {
                        JSONArray AssetStatus = jsonObj.getJSONArray("AssetStatus");
                        if (AssetStatus != null) {

                            db = myDb.getWritableDatabase();
                            String sql = "insert into Asset_Status (Asset_Status_Id,Status,Task_State,Color)values(?,?,?,?);";
                            db.beginTransaction();
                            SQLiteStatement stmt = db.compileStatement(sql);
                            for (int i = 0; i < AssetStatus.length(); i++) {
                                JSONObject c = AssetStatus.getJSONObject(i);
                                if(LOG)Log.d("Asssret"," "+c.getString("Asset_Status_Id")+" "+c.getString("Status")+" "+c.getString("Task_State")+" "+c.getString("Color"));
                                String Asset_Status_Id = c.getString("Asset_Status_Id");
                                String Status = c.getString("Status");
                                String Task_State = c.getString("Task_State");
                                String Color = c.getString("Color");
                                stmt.bindString(1, Asset_Status_Id);
                                stmt.bindString(2, Status);
                                stmt.bindString(3,Task_State);
                                stmt.bindString(4,Color);
                                long entryID = stmt.executeInsert();
                                stmt.clearBindings();
                            }

                            String sqlquery = "DELETE FROM Asset_Status WHERE Id NOT IN (SELECT MIN(Id) FROM Asset_Status GROUP BY Asset_Status_Id)";
                            db.rawQuery(sqlquery, null);
                            db.execSQL(sqlquery);
                            db.setTransactionSuccessful();
                            db.endTransaction();
                            db.close();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        db.endTransaction();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        db.endTransaction();
                    }

                    try {
                        JSONArray Form_Ticket = jsonObj.getJSONArray("FormTicket");
                        if (Form_Ticket != null) {
                            db = myDb.getWritableDatabase();
                            String sql = "insert into Form_Ticket (Auto_Id ,Site_Location_Id ,Form_section_Id,Product ,Component ,Department ,Option_Selected,Assigned_To,IncidentSource,Loggedby,Ticket_Location_Id,Created_Date_Time ,Deleted_Date_Time ,Record_Status) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
                            db.beginTransaction();
                            SQLiteStatement stmt = db.compileStatement(sql);
                            for (int i = 0; i < Form_Ticket.length(); i++) {
                                JSONObject c = Form_Ticket.getJSONObject(i);

                                String Auto_Id = c.getString("Auto_Id");
                                String Form_section_Id = c.getString("Form_section_Id");
                                String Product = c.getString("Product");
                                String Component = c.getString("Component");
                                String Department = c.getString("Department");
                                String Option_Selected = c.getString("Option_Selected");
                                String Assigned_To = c.getString("Assigned_To");
                                String IncidentSource = c.getString("Incident_Source");
                                String Loggedby = c.getString("Logged_by");
                                //String Ticket_Location_Id = c.getString("Ticket_Location_Id");
                                String Created_Date_Time = c.getString("Created_Date_Time");
                                String Deleted_Date_Time = c.getString("Deleted_Date_Time");
                                String Record_Status = c.getString("Record_Status");
                                stmt.bindString(1, Auto_Id);
                                stmt.bindString(2, site_id);
                                stmt.bindString(3,Form_section_Id);
                                stmt.bindString(4, Product);
                                stmt.bindString(5, Component);
                                stmt.bindString(6, Department);
                                stmt.bindString(7, Option_Selected);
                                stmt.bindString(8, Assigned_To);
                                stmt.bindString(9, IncidentSource);
                                stmt.bindString(10, Loggedby);
                                stmt.bindString(11, Created_Date_Time);
                                stmt.bindString(12, Deleted_Date_Time);
                                stmt.bindString(13, Record_Status);
                                long entryID = stmt.executeInsert();
                                Log.d("Teasdasdasd",entryID +" "+Auto_Id);
                                stmt.clearBindings();
                            }

                            String sqlquery = "DELETE FROM Form_Ticket WHERE Id NOT IN (SELECT MIN(Id) FROM Form_Ticket GROUP BY Auto_Id)";
                            db.rawQuery(sqlquery, null);
                            db.execSQL(sqlquery);
                            db.setTransactionSuccessful();
                            db.endTransaction();
                            db.close();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        db.endTransaction();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        db.endTransaction();
                    }


                    try {
                        JSONArray Form_Ticket = jsonObj.getJSONArray("SiteLocationticket");
                        if (Form_Ticket != null) {
                            db = myDb.getWritableDatabase();
                            String sql = "insert into Site_Ticket_Location (Auto_Id ,Site_Location_Id ,Site ,Location ,Sub_Location ,Service_Area ,Created_Date_Time ,Deleted_Date_Time ,Record_Status) VALUES (?,?,?,?,?,?,?,?,?);";
                            db.beginTransaction();
                            SQLiteStatement stmt = db.compileStatement(sql);
                            for (int i = 0; i < Form_Ticket.length(); i++) {
                                JSONObject c = Form_Ticket.getJSONObject(i);

                                String Auto_Id = c.getString("Auto_Id");
                                String Site = c.getString("Site");
                                String Location = c.getString("Location");
                                String Sub_Location = c.getString("Sub_Location");
                                String Service_Area = c.getString("Service_Area");
                                String Created_Date_Time = c.getString("Created_Date_Time");
                                String Deleted_Date_Time = c.getString("Deleted_Date_Time");
                                String Record_Status = c.getString("Record_Status");
                                stmt.bindString(1, Auto_Id);
                                stmt.bindString(2, site_id);
                                stmt.bindString(3,Site);
                                stmt.bindString(4, Location);
                                stmt.bindString(5, Sub_Location);
                                stmt.bindString(6, Service_Area);
                                stmt.bindString(7, Created_Date_Time);
                                stmt.bindString(8, Deleted_Date_Time);
                                stmt.bindString(9, Record_Status);
                                long entryID = stmt.executeInsert();
                                Log.d("Teasdasdasd213",entryID +" "+Auto_Id);
                                stmt.clearBindings();
                            }

                            String sqlquery = "DELETE FROM Site_Ticket_Location WHERE Id NOT IN (SELECT MIN(Id) FROM Form_Ticket GROUP BY Auto_Id)";
                            db.rawQuery(sqlquery, null);
                            db.execSQL(sqlquery);
                            db.setTransactionSuccessful();
                            db.endTransaction();
                            db.close();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        db.endTransaction();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        db.endTransaction();
                    }

                } catch (Exception e) {
                    sync = true;
                    e.printStackTrace();

                }
            }

            if(PPMTaskjson != null){
                try {

                    try {

                        Log.d("UploadPPMJson",":: "+PPMTaskjson);
                        JSONObject jsonObj = new JSONObject(PPMTaskjson);

                        JSONArray task = jsonObj.getJSONArray("PPMTaskFeqeuncy");
                        if(LOG)Log.d("DATasValue123",task+"");
                        if (task != null) {

                            db = myDb.getWritableDatabase();
                            String AssetActivityLinkingsql = "insert into Asset_Activity_Linking (Site_Location_Id ,Auto_Id ,Asset_Id ,Activity_Id,RecordStatus ,UpdatedStatus )values(?,?,?,?,?,?);";
                            String ActivityMastersql = "insert into Activity_Master (Site_Location_Id ,Auto_Id ,Form_Id ,Activity_Name ,RecordStatus ,UpdatedStatus )values(?,?,?,?,?,?);";
                            String AssetActivityAssignedTosql = "insert into Asset_Activity_AssignedTo (Site_Location_Id ,Auto_Id ,Assigned_To_User_Id ,Assigned_To_User_Group_Id ,Asset_Activity_Linking_Id,RecordStatus ,UpdatedStatus )values(?,?,?,?,?,?,?);";
                            String PpmTasksql = "insert into PPM_Task (Auto_Id, Old_PPM_Id, Site_Location_Id, Activity_Frequency, Task_Date,Task_End_Date, Task_Status,Task_Done_At , Asset_Activity_Linking_Id,  Assigned_To_User_Id, Assigned_To_User_Group_Id, TimeStartson, Activity_Duration, Record_Status, UpdatedStatus)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
                            db.beginTransaction();
                            SQLiteStatement AssetActivityLinkingstmt = db.compileStatement(AssetActivityLinkingsql);
                            SQLiteStatement ActivityMasterstmt = db.compileStatement(ActivityMastersql);
                            SQLiteStatement AssetActivityAssignedTostmt = db.compileStatement(AssetActivityAssignedTosql);
                            SQLiteStatement PpmTaskstmt = db.compileStatement(PpmTasksql);

                            for (int i = 0; i < task.length(); i++) {

                                JSONObject c = task.getJSONObject(i);
                                if(LOG)Log.d("hbkjcnv",((i+1)*100)/task.length()+"");
                                int setValue = ((i+1)*100)/task.length();
                                //pDialog.setProgress(setValue);
                                ////////////////////////////ppm task insert in db//////////////////////////

                                String  ppm_auto_id = c.getString("Auto_Id");
                                String old_ppm_id = c.getString("Old_PPM_Id");
                                String  activity_frequency   = c.getString("Activity_Frequency");
                                String  task_date = c.getString("Task_Date");
                                String  asset_activity_linking_id = c.getString("Asset_Activity_Linking_Id");
                                String Assigned_To_User_Id = c.getString("Assigned_To_User_Id");
                                String Assigned_To_User_Group_Id = c.getString("Assigned_To_User_Group_Id");
                                String  task_status = c.getString("Task_Status");
                                String  Task_Done_At = c.getString("Task_Done_At");
                                String  timestartson = c.getString("Timestartson");
                                String  Task_End_Date = c.getString("Task_End_Date");
                                String  activity_duration = c.getString("Activity_Duration");
                                /*String  grace_duration_before = c.getString("Grace_Duration_Before");
                                String  grace_duration_after = c.getString("Grace_Duration_After");*/
                                String  ppm_Record_Status = c.getString("Record_Status");

                                PpmTaskstmt.bindString(1,ppm_auto_id);
                                PpmTaskstmt.bindString(2,old_ppm_id);
                                PpmTaskstmt.bindString(3,myDb.Site_Location_Id(User_Id));
                                PpmTaskstmt.bindString(4,activity_frequency);
                                PpmTaskstmt.bindString(5,task_date);
                                PpmTaskstmt.bindString(6,Task_End_Date);
                                PpmTaskstmt.bindString(7,task_status);
                                PpmTaskstmt.bindString(8,Task_Done_At);
                                PpmTaskstmt.bindString(9,asset_activity_linking_id);
                                PpmTaskstmt.bindString(10,Assigned_To_User_Id);
                                PpmTaskstmt.bindString(11,Assigned_To_User_Group_Id);
                                PpmTaskstmt.bindString(12,timestartson);
                                PpmTaskstmt.bindString(13,activity_duration);
                                /*PpmTaskstmt.bindString(14,grace_duration_before);
                                PpmTaskstmt.bindString(15,grace_duration_after);*/
                                PpmTaskstmt.bindString(14,ppm_Record_Status);
                                if(task_status.equals("Completed")||task_status.equals("Missed")){
                                    PpmTaskstmt.bindString(15,"yes");
                                }else {
                                    PpmTaskstmt.bindString(15,"no");
                                }


                                long PpmTaskentryID = PpmTaskstmt.executeInsert();
                                PpmTaskstmt.clearBindings();

                                if(LOG)Log.d("PpmTaskInseted",PpmTaskentryID+"");

                                ///////////////////////////////End ppm task insert in db//////////////////////////
                                //////////////Asset Activity Linking Insert in Database//////////////////
                                String AssetActivityLinkingAutoId = c.getString("AssetActivityLinkingAutoId");
                                String AssetActivityLinkingAssetId = c.getString("AssetActivityLinkingAssetId");

                                String AssetActivityLinkingRecordStatus = c.getString("AssetActivityLinkingRecordStatus");
                                String AssetActivityLinkingActivity_Id = c.getString("AssetActivityLinkingActivity_Id");
                                AssetActivityLinkingstmt.bindString(1, myDb.Site_Location_Id(User_Id));
                                AssetActivityLinkingstmt.bindString(2, AssetActivityLinkingAutoId);
                                AssetActivityLinkingstmt.bindString(3, AssetActivityLinkingAssetId);
                                AssetActivityLinkingstmt.bindString(4, AssetActivityLinkingActivity_Id);
                                AssetActivityLinkingstmt.bindString(5, AssetActivityLinkingRecordStatus);
                                AssetActivityLinkingstmt.bindString(6, "no");

                                long AssetActivityLinkingentryID = AssetActivityLinkingstmt.executeInsert();
                                AssetActivityLinkingstmt.clearBindings();

                                if(LOG)Log.d("AssetInserted",AssetActivityLinkingentryID+"");
                                ////////////// END Asset Activity Linking Insert in Database//////////////////

                                //////////////Activity Master Insert in Database//////////////////
                                String ActivityMasterAutoId = c.getString("ActivityMasterAutoId");
                                String ActivityMasterFormId = c.getString("ActivityMasterFormId");
                                String ActivityMasterActivityName = c.getString("ActivityMasterActivityName");
                                String ActivityMasterRecordStatus = c.getString("ActivityMasterRecordStatus");

                                ActivityMasterstmt.bindString(1, myDb.Site_Location_Id(User_Id));
                                ActivityMasterstmt.bindString(2, ActivityMasterAutoId);
                                ActivityMasterstmt.bindString(3, ActivityMasterFormId);
                                ActivityMasterstmt.bindString(4, ActivityMasterActivityName);
                                ActivityMasterstmt.bindString(5, ActivityMasterRecordStatus);
                                ActivityMasterstmt.bindString(6, "no");

                                long ActivityMasterentryID = ActivityMasterstmt.executeInsert();
                                ActivityMasterstmt.clearBindings();

                                if(LOG)Log.d("ActivityMasterInseted",ActivityMasterentryID+"");

                                //////////////END Activity Master Insert in Database//////////////////
                                ////////////// Asset Activity Assigned To Insert in Database//////////////////
                                String AssetActivityAssignedToAutoId = c.getString("AssetActivityAssignedToAutoId");
                                String AssetActivityAssignedToUserId = c.getString("AssetActivityAssignedToUserId");
                                String AssetActivityAssignedToUserGroupId = c.getString("AssetActivityAssignedToUserGroupId");
                                String AssetActivityAssignedToAsset_Activity_Linking_Id = c.getString("AssetActivityAssignedToAsset_Activity_Linking_Id");
                                String AssetActivityAssignedToRecordStatus = c.getString("AssetActivityAssignedToRecordStatus");

                                AssetActivityAssignedTostmt.bindString(1, myDb.Site_Location_Id(User_Id));
                                AssetActivityAssignedTostmt.bindString(2, AssetActivityAssignedToAutoId);
                                AssetActivityAssignedTostmt.bindString(3, AssetActivityAssignedToUserId);
                                AssetActivityAssignedTostmt.bindString(4, AssetActivityAssignedToUserGroupId);
                                AssetActivityAssignedTostmt.bindString(5, AssetActivityAssignedToAsset_Activity_Linking_Id);
                                AssetActivityAssignedTostmt.bindString(6, AssetActivityAssignedToRecordStatus);
                                AssetActivityAssignedTostmt.bindString(7, "no");

                                long AssetActivityAssignedToentryID = AssetActivityAssignedTostmt.executeInsert();
                                AssetActivityAssignedTostmt.clearBindings();
                                if(LOG)Log.d("AssignedInseted",AssetActivityAssignedToentryID+"");

                                //////////////END  Asset Activity Assigned To Insert in Database//////////////////

                            }
                            String Asset_Activity_Linkingquery = "DELETE FROM Asset_Activity_Linking WHERE Id NOT IN (SELECT MIN(Id) FROM Asset_Activity_Linking GROUP BY Auto_Id) AND Site_Location_Id='"+myDb.Site_Location_Id(User_Id)+"'";
                            String Activity_Mastergquery = "DELETE FROM Activity_Master WHERE Id NOT IN (SELECT MIN(Id) FROM Activity_Master GROUP BY Auto_Id) AND Site_Location_Id='"+myDb.Site_Location_Id(User_Id)+"'";
                            String Asset_Activity_AssignedToquery = "DELETE FROM Asset_Activity_AssignedTo WHERE Id NOT IN (SELECT MIN(Id) FROM Asset_Activity_AssignedTo GROUP BY Auto_Id) AND Site_Location_Id='"+myDb.Site_Location_Id(User_Id)+"'";
                            String Ppm_task_query = "DELETE FROM PPM_Task WHERE Id NOT IN (SELECT MIN(Id) FROM PPM_Task GROUP BY Auto_Id) AND Site_Location_Id='"+myDb.Site_Location_Id(User_Id)+"'";

                            String[] statements = new String[]{Asset_Activity_Linkingquery,Activity_Mastergquery,Asset_Activity_AssignedToquery,Ppm_task_query};

                            for(String sql : statements){
                                db.rawQuery(sql, null);
                                db.execSQL(sql);
                            }
                            db.setTransactionSuccessful();
                            db.endTransaction();
                            db.close();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        db.endTransaction();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        db.endTransaction();
                    }



                } catch (Exception e) {

                    e.printStackTrace();

                }
            }

            //getPPMTask();
            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //pDialog.dismiss();
            if(LOG)Log.d(TAG, "Download Finished");
            try {
                if (sync == true) {
                    Toast.makeText(getApplicationContext(), "Synchronization Failed.Synchronise The Data Again", Toast.LENGTH_SHORT).show();
                    sync = false;
                } else if (sync == false) {

                    Toast.makeText(getApplicationContext(),"Data is Uploaded",Toast.LENGTH_SHORT).show();
                    isUploading=true;
                    if (myDb.Notification(User_Id).trim().equals("true")){
                        Log.d("MyServices","AutoUploadComplete"+myDb.Notification(User_Id).toString());
                        messageStyleNotification1();
                    }
                    /*if (myDb. AutoSyncSetting().equals("true")) {
                        Log.d("MyServices","DataDownloadComplete"+"IntentToMyService");
                        startService(new Intent(getApplicationContext(), MyService.class));
                    }*/
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void uploadData() {
        countvalue++;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        JSONArray uploadData = new JSONArray();
        JSONObject Ticketdata = new JSONObject();
        JSONObject AssetDetails = new JSONObject();
        JSONObject AssetStatusChange = new JSONObject();
        uploadTicketIMS();
        try {
            uploadData = composeJSONfortaskDetailsNew(myDb.Site_Location_Id(User_Id), User_Id);
            Ticketdata.put("Ticketdata", myDb.composeJSONTicket(User_Id));
            /*Ticketdata.put("Ticketdata", myDb.composeJSONforTicket(User_Id));*/
            AssetDetails.put("AssetDetails", myDb.composeJSONforAssets(User_Id));
            AssetStatusChange.put("AssetStatusLog", myDb.composeJSONforAssetsStatusChange(myDb.Site_Location_Id(User_Id),User_Id));
            uploadData.put(Ticketdata);
            uploadData.put(AssetDetails);
            uploadData.put(AssetStatusChange);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        int networkStrength = 0;
        String carrierName = null;
        String deviceDetails = null;
        try {
            networkStrength = NetworkStrength();
            carrierName = CarrierName();
            deviceDetails = DeviceDetails();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(LOG) Log.d(TAG, "1" + networkStrength + "  " + haveNetworkConnection() + "  " + carrierName + "   " + "  " + deviceDetails);
        params.put("Sync_Info", myDb.SyncInfo(User_Id, level, networkStrength, haveNetworkConnection(), carrierName, deviceDetails));
        params.put("Sync_Count", countvalue);
        params.put("Site_Location_Id", myDb.Site_Location_Id(User_Id));
        params.put("Task_Details", uploadData);
        if(LOG)Log.d("dfhj",params.toString());

        if (WRITE_JSON_FILE) {
            generateNoteOnSD(getApplicationContext(), "uploadData" + countvalue + ".txt", params.toString());
        }
        //if(LOG)Log.d(TAG, "URL002" + myDb.SiteURL(User_Id) +" "+ new applicationClass().urlString() + new applicationClass().insertTask());
        if(LOG)Log.d("Teasdasdasd",URLSTRING + new applicationClass().insertTask());
        client.post(URLSTRING + new applicationClass().insertTask(), params, new TextHttpResponseHandler() {//http://eclockwork.in/inserttask.php
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // TODO Auto-generated method stub

                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();

                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();

                } else {
                    isUploading=true;
                    Toast.makeText(getApplicationContext(), "Internet Connection Lost.Synchronization failed.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                db = myDb.getWritableDatabase();
                if (WRITE_JSON_FILE) {
                    generateNoteOnSD(getApplicationContext(), "responseString1908" + countvalue + ".txt", responseString.toString());
                }
                try {
                    JSONObject jsonObjTask = new JSONObject(responseString);

                    try {
                        JSONObject json2 = jsonObjTask.getJSONObject("TaskDetails");
                        JSONArray task = json2.getJSONArray("TaskIds");
                        if (task != null) {
                            for (int i = 0; i < task.length(); i++) {
                                String Status = json2.get("Status").toString();
                                String TaskId = task.get(i).toString();

                                if (Status.equalsIgnoreCase("yes")) {
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("UpdatedStatus", Status);
                                    db.update("Task_Details", contentValues, "Auto_Id ='" + TaskId + "'", null);
                                    db.update("Meter_Reading", contentValues, "Task_Id ='" + TaskId + "'", null);
                                    db.update("Data_Posting", contentValues, "Task_Id ='" + TaskId + "'", null);
                                    db.update("AlertMaster", contentValues, "Task_Id ='" + TaskId + "'", null);
                                    if (new applicationClass().imageVariable().equals("yes")) {
                                        imageServer(TaskId);
                                    }
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        JSONArray Ticket = jsonObjTask.getJSONArray("Ticketdata");
                        if(LOG) Log.d(TAG, Task_Status+" Ticketdata_Response " + Ticket);
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
                        JSONArray AssetStatusLog = jsonObjTask.getJSONArray("AssetStatusLog");
                        if(LOG) Log.d(TAG, Task_Status+" AssetStatusLog " + AssetStatusLog);
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

                    /*try {
                        final JSONArray AssetDetails = jsonObjTask.getJSONArray("AssetDetails");
                        if (AssetDetails != null) {
                            db.execSQL("DELETE FROM Asset_Details WHERE Site_Location_Id='"+site_id+"'");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }*/


                } catch (Exception e) {
                    e.printStackTrace();
                }


                try {
                    String selectQuery = "SELECT  * FROM Task_Details where UpdatedStatus = 'no' and  Assigned_To_User_Id='" + User_Id + "' and Site_Location_Id='" + myDb.Site_Location_Id(User_Id) + "'";
                    Cursor cursor = db.rawQuery(selectQuery, null);
                    int cursorValue = cursor.getCount();

                    if (cursorValue > 0) {
                        uploadData();

                    } else {
                        uploadDataPPM();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


               /* try {
                    String query = "SELECT * FROM PPM_Task where UpdatedStatus = 'no' and Site_Location_Id='" + myDb.Site_Location_Id(User_Id) + "'";
                    Cursor cursor = db.rawQuery(query, null);
                    int cursorValue = cursor.getCount();
                    if (cursorValue > 0){

                    }else {
                        getPPMTask();
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }*/
                db.close();
            }

        });

    }

    public JSONArray composeJSONfortaskDetailsNew(String site_id,String UserId){
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
            String selectQuery = "SELECT  * FROM Task_Details where Task_Status <> 'Pending' and UpdatedStatus = 'no' and Assigned_To_User_Id='"+UserId+"' and Site_Location_Id='"+site_id+"' ORDER BY CASE WHEN Task_Status='Completed' THEN 1 WHEN Task_Status='Delayed' THEN 2 WHEN Task_Status='Unplanned' THEN 3 WHEN Task_Status='Cancelled' THEN 4 END,Task_Status ASC LIMIT 25"  ;
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
                    TaskJsonObject.put("Activity_Type", cursor.getString(cursor.getColumnIndex("Activity_Type")));
                    TaskJsonObject.put("Assigned_To_User_Group_Id", cursor.getString(cursor.getColumnIndex("Assigned_To_User_Group_Id")));
                    TaskJsonObject.put("Remarks", cursor.getString(cursor.getColumnIndex("Remarks")));
                    TaskJsonObject.put("GeoLoc", loggerData[6]);



                    String selectQuery1 = "SELECT  Meter_Reading.* FROM Meter_Reading LEFT JOIN  Task_Details ON Meter_Reading.Task_Id = Task_Details.Auto_Id WHERE Task_Details.Assigned_To_User_Id ='"+UserId+"' AND Meter_Reading.Task_Id ='"+TaskID+"' AND Meter_Reading.UpdatedStatus = 'no'  " ;
                    StringBuffer sbMeterForm_Structure_Id = new StringBuffer();
                    StringBuffer sbMeterReading = new StringBuffer();
                    StringBuffer sbUOM = new StringBuffer();
                    Cursor cursorMeterReading = db.rawQuery(selectQuery1, null);
                    JSONObject MeterReadingJsonObject = new JSONObject();
                    if(cursorMeterReading.getCount() !=0) {
                        if (cursorMeterReading.moveToFirst()) {
                            do {
                                sbMeterForm_Structure_Id.append(cursorMeterReading.getString(cursorMeterReading.getColumnIndex("Form_Structure_Id"))).append("|");
                                sbMeterReading.append(cursorMeterReading.getString(cursorMeterReading.getColumnIndex("Reading"))).append("|");
                                sbUOM.append(cursorMeterReading.getString(cursorMeterReading.getColumnIndex("UOM"))).append("|");
                                MeterReadingJsonObject.put("Task_Id", cursorMeterReading.getString(cursorMeterReading.getColumnIndex("Task_Id")));
                                MeterReadingJsonObject.put("Asset_Id", cursorMeterReading.getString(cursorMeterReading.getColumnIndex("Asset_Id")));
                                MeterReadingJsonObject.put("Form_Structure_Id", sbMeterForm_Structure_Id.toString());
                                MeterReadingJsonObject.put("Reading", sbMeterReading.toString());
                                MeterReadingJsonObject.put("UOM", sbUOM.toString());
                                MeterReadingJsonObject.put("Reset", cursorMeterReading.getString(cursorMeterReading.getColumnIndex("Reset")));
                                MeterReadingJsonObject.put("Activity_Frequency_Id", cursorMeterReading.getString(cursorMeterReading.getColumnIndex("Activity_Frequency_Id")));

                            } while (cursorMeterReading.moveToNext());

                        }
                        MeterReadingArray.put(MeterReadingJsonObject);
                    }
                    cursorMeterReading.close();


                    StringBuffer sbForm_Structure_Id = new StringBuffer();
                    StringBuffer sbValue = new StringBuffer();
                    StringBuffer sbRemark = new StringBuffer();
                    JSONObject DatPostingJsonObject = new JSONObject();
                    String selectQueryData = "SELECT  Data_Posting.Task_Id,Data_Posting.Form_Structure_Id,Data_Posting.Value,Task_Details.From_Id,Data_Posting.Remark FROM Data_Posting LEFT JOIN  Task_Details ON Data_Posting.Task_Id = Task_Details.Auto_Id WHERE Task_Details.Assigned_To_User_Id = '"+UserId+"' AND Data_Posting.UpdatedStatus = 'no' AND Data_Posting.Task_Id ='"+TaskID+"'";
                    Cursor cursorDataPosting = db.rawQuery(selectQueryData, null);
                    if(cursorDataPosting.getCount() !=0) {
                        if (cursorDataPosting.moveToFirst()) {
                            do {
                                DatPostingJsonObject.put("Task_Id", cursorDataPosting.getString(cursorDataPosting.getColumnIndex("Task_Id")));
                                DatPostingJsonObject.put("Form_Id", cursorDataPosting.getString(cursorDataPosting.getColumnIndex("From_Id")));
                                sbForm_Structure_Id.append(cursorDataPosting.getString(cursorDataPosting.getColumnIndex("Form_Structure_Id"))).append("|");
                                sbValue.append(cursorDataPosting.getString(cursorDataPosting.getColumnIndex("Value"))).append("|");
                                sbRemark.append(cursorDataPosting.getString(cursorDataPosting.getColumnIndex("Remark"))).append("|");
                                DatPostingJsonObject.put("Form_Structure_Id", sbForm_Structure_Id.toString());
                                DatPostingJsonObject.put("Value", sbValue.toString());
                                DatPostingJsonObject.put("Remark", sbRemark.toString());
                            } while (cursorDataPosting.moveToNext());
                        }
                        DataPostingArray.put(DatPostingJsonObject);
                    }
                    cursorDataPosting.close();

                    StringBuffer sbAlertForm_Structure_Id = new StringBuffer();
                    JSONObject AlertJsonObject = new JSONObject();
                    String selectQueryAlert = "SELECT * FROM AlertMaster WHERE Task_Id='"+TaskID+"'";
                    Cursor cursorAlert = db.rawQuery(selectQueryAlert, null);
                    if(cursorAlert.getCount() !=0) {
                        if (cursorAlert.moveToFirst()) {
                            do {
                                sbAlertForm_Structure_Id.append(cursorAlert.getString(cursorAlert.getColumnIndex("Form_Structure_Id"))).append("|");
                                AlertJsonObject.put("Form_Structure_Id", sbAlertForm_Structure_Id.toString());
                                AlertJsonObject.put("Task_Id", cursorAlert.getString(cursorAlert.getColumnIndex("Task_Id")));
                                AlertJsonObject.put("Form_Id", cursorAlert.getString(cursorAlert.getColumnIndex("Form_Id")));
                                AlertJsonObject.put("Alert_Type", cursorAlert.getString(cursorAlert.getColumnIndex("Alert_Type")));
                                AlertJsonObject.put("Critical", cursorAlert.getString(cursorAlert.getColumnIndex("Critical")));
                            } while (cursorAlert.moveToNext());
                        }
                        AlertArray.put(AlertJsonObject);
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

    public void uploadDataPPM() {


        countvalue++;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        JSONArray uploadData = new JSONArray();

        try {
            uploadData = composeJSONPPM(myDb.Site_Location_Id(User_Id), User_Id);


        } catch (Exception e) {
            e.printStackTrace();
        }
        int networkStrength = 0;
        String carrierName = null;
        String deviceDetails = null;
        try {
            networkStrength = NetworkStrength();
            carrierName = CarrierName();
            deviceDetails = DeviceDetails();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(LOG)Log.d(TAG, "1" + networkStrength + "  " + haveNetworkConnection() + "  " + carrierName + "   " + "  " + deviceDetails);
        params.put("Sync_Info", myDb.SyncInfo(User_Id, level, networkStrength, haveNetworkConnection(), carrierName, deviceDetails));
        params.put("Sync_Count", countvalue);
        params.put("Site_Location_Id", myDb.Site_Location_Id(User_Id));
        params.put("PPMTaskDetails", uploadData);
        if(LOG)Log.d("dfhj",params.toString());

        if (WRITE_JSON_FILE) {
            generateNoteOnSD(getApplicationContext(), "uploadData" + countvalue + ".txt", params.toString());
        }
        //if(LOG)Log.d(TAG, "URL002" + myDb.SiteURL(User_Id) +" "+ new applicationClass().urlString() + new applicationClass().insertTask());
        if(LOG)Log.d("URL002",URLSTRING + new applicationClass().insertTask());
        client.post(URLSTRING + new applicationClass().insertPPMTask(), params, new TextHttpResponseHandler() {//http://eclockwork.in/inserttask.php
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // TODO Auto-generated method stub

                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Internet Connection Lost.Synchronization failed.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                db = myDb.getWritableDatabase();
                if (WRITE_JSON_FILE) {
                    generateNoteOnSD(getApplicationContext(), "responseString1908" + countvalue + ".txt", responseString.toString());
                }
                try {
                    JSONObject jsonObjTask = new JSONObject(responseString);

                    try {
                        JSONObject json2 = jsonObjTask.getJSONObject("PPMTaskDetails");
                        JSONArray task = json2.getJSONArray("TaskIds");
                        if (task != null) {
                            for (int i = 0; i < task.length(); i++) {
                                String Status = json2.get("Status").toString();
                                String TaskId = task.get(i).toString();

                                if (Status.equalsIgnoreCase("yes")) {
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("UpdatedStatus", Status);
                                    db.update("PPM_Task", contentValues, "Auto_Id ='" + TaskId + "'", null);
                                    db.update("Meter_Reading", contentValues, "Task_Id ='" + TaskId + "'", null);
                                    db.update("Data_Posting", contentValues, "Task_Id ='" + TaskId + "'", null);
                                    db.update("AlertMaster", contentValues, "Task_Id ='" + TaskId + "'", null);
                                    if (new applicationClass().imageVariable().equals("yes")) {
                                        imageServer(TaskId);
                                    }
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


                try {
                    String selectQuery = "SELECT  * FROM PPM_Task where UpdatedStatus = 'no' and  Assigned_To_User_Id='" + User_Id + "' and Site_Location_Id='" + myDb.Site_Location_Id(User_Id) + "'";
                    Cursor cursor = db.rawQuery(selectQuery, null);
                    int cursorValue = cursor.getCount();
                    Log.d("PPMUpload"," "+cursorValue);
                    if (cursorValue > 0) {
                        uploadDataPPM();
                    } else {
                        deletePPMTask();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


               /* try {
                    String query = "SELECT * FROM PPM_Task where UpdatedStatus = 'no' and Site_Location_Id='" + myDb.Site_Location_Id(User_Id) + "'";
                    Cursor cursor = db.rawQuery(query, null);
                    int cursorValue = cursor.getCount();
                    if (cursorValue > 0){

                    }else {
                        getPPMTask();
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }*/
                db.close();
            }

        });

    }

    public JSONArray composeJSONPPM(String site_id,String UserId){
        JSONArray UploadArray = new JSONArray();
        JSONObject TaskDetails = new JSONObject();
        JSONObject MeterReading = new JSONObject();
        JSONObject DataValue = new JSONObject();
        JSONObject AlertData = new JSONObject();

        JSONArray PPMTaskJsonArray = new JSONArray();
        JSONArray MeterReadingArray = new JSONArray();
        JSONArray DataPostingArray = new JSONArray();
        JSONArray AlertArray = new JSONArray();
        try {
            String[]loggerData = new LoggerFile().loggerFunction(UserId);
            //String selectQuery = "SELECT * FROM Task_Details where UpdatedStatus = 'no' and Assigned_To_User_Id='"+UserId+"' and Task_Status='"+Task_Status+"' ORDER BY Task_Scheduled_Date LIMIT 20" ;
            String selectQuery = "SELECT  * FROM "+ DatabaseColumn.PPM_Task+" where "+ DatabaseColumn.Task_Status +" <> 'Pending' AND  "+ DatabaseColumn.UpdatedStatus+" = 'no' and "+ DatabaseColumn.Assigned_To_User_Id+" ='"+UserId+"' and Site_Location_Id='"+site_id+"' ORDER BY CASE WHEN "+ DatabaseColumn.Task_Status +"='Completed' THEN 1 WHEN "+ DatabaseColumn.Task_Status +"='Missed' THEN 2  END, "+ DatabaseColumn.Task_Status +" ASC"  ;

            db=myDb.getWritableDatabase();

            Cursor cursor = db.rawQuery(selectQuery, null);
            if(LOG)Log.d("Teasdasd",selectQuery + " "+cursor.getCount() );
            if(LOG)Log.d("Teasdasd","GitWorklingas");
            /*Log.d("Teasdasd","Dakshata ko dekha");
            Log.d("Teasdasd","Prathamesh ko dekha");
            Log.d("Teasdasd","Dakshata ko Dakshata");*/
            if (cursor.moveToFirst()) {
                do {
                    JSONObject PPMTaskJsonObject = new JSONObject();
                    String TaskID= cursor.getString(cursor.getColumnIndex(DatabaseColumn.Auto_Id));
                    PPMTaskJsonObject.put(DatabaseColumn.Auto_Id, cursor.getString(cursor.getColumnIndex(DatabaseColumn.Auto_Id)));
                    PPMTaskJsonObject.put(DatabaseColumn.Task_Done_At, cursor.getString(cursor.getColumnIndex(DatabaseColumn.Task_Done_At)));
                    PPMTaskJsonObject.put(DatabaseColumn.Task_Status, cursor.getString(cursor.getColumnIndex(DatabaseColumn.Task_Status)));
                    PPMTaskJsonObject.put(DatabaseColumn.Scan_Type, cursor.getString(cursor.getColumnIndex(DatabaseColumn.Scan_Type)));
                    PPMTaskJsonObject.put(DatabaseColumn.Assigned_To_User_Id, cursor.getString(cursor.getColumnIndex(DatabaseColumn.Assigned_To_User_Id)));
                    PPMTaskJsonObject.put(DatabaseColumn.Assigned_To_User_Group_Id, cursor.getString(cursor.getColumnIndex(DatabaseColumn.Assigned_To_User_Group_Id)));
                    PPMTaskJsonObject.put("GeoLoc", loggerData[6]);



                    String selectQuery1 = "SELECT  * FROM Meter_Reading  WHERE Task_Id ='"+TaskID+"' AND UpdatedStatus = 'no'  " ;
                    StringBuffer sbMeterForm_Structure_Id = new StringBuffer();
                    StringBuffer sbMeterReading = new StringBuffer();
                    StringBuffer sbUOM = new StringBuffer();
                    Cursor cursorMeterReading = db.rawQuery(selectQuery1, null);
                    JSONObject MeterReadingJsonObject = new JSONObject();
                    if(cursorMeterReading.getCount() !=0) {
                        if (cursorMeterReading.moveToFirst()) {
                            do {
                                sbMeterForm_Structure_Id.append(cursorMeterReading.getString(cursorMeterReading.getColumnIndex("Form_Structure_Id"))).append("|");
                                sbMeterReading.append(cursorMeterReading.getString(cursorMeterReading.getColumnIndex("Reading"))).append("|");
                                sbUOM.append(cursorMeterReading.getString(cursorMeterReading.getColumnIndex("UOM"))).append("|");
                                MeterReadingJsonObject.put("Task_Id", cursorMeterReading.getString(cursorMeterReading.getColumnIndex("Task_Id")));
                                MeterReadingJsonObject.put("Asset_Id", cursorMeterReading.getString(cursorMeterReading.getColumnIndex("Asset_Id")));
                                MeterReadingJsonObject.put("Form_Structure_Id", sbMeterForm_Structure_Id.toString());
                                MeterReadingJsonObject.put("Reading", sbMeterReading.toString());
                                MeterReadingJsonObject.put("UOM", sbUOM.toString());
                                MeterReadingJsonObject.put("Reset", cursorMeterReading.getString(cursorMeterReading.getColumnIndex("Reset")));
                                MeterReadingJsonObject.put("Activity_Frequency_Id", cursorMeterReading.getString(cursorMeterReading.getColumnIndex("Activity_Frequency_Id")));

                            } while (cursorMeterReading.moveToNext());

                        }
                        MeterReadingArray.put(MeterReadingJsonObject);
                    }
                    cursorMeterReading.close();


                    StringBuffer sbForm_Structure_Id = new StringBuffer();
                    StringBuffer sbValue = new StringBuffer();
                    StringBuffer sbRemark = new StringBuffer();
                    JSONObject DatPostingJsonObject = new JSONObject();
                    String selectQueryData = "SELECT  Task_Id,Form_Structure_Id,Value,Form_Id,Remark FROM Data_Posting  WHERE  Data_Posting.UpdatedStatus = 'no' AND Data_Posting.Task_Id ='"+TaskID+"'";
                    Cursor cursorDataPosting = db.rawQuery(selectQueryData, null);
                    if(cursorDataPosting.getCount() !=0) {
                        if (cursorDataPosting.moveToFirst()) {
                            do {
                                DatPostingJsonObject.put("Task_Id", cursorDataPosting.getString(cursorDataPosting.getColumnIndex("Task_Id")));
                                DatPostingJsonObject.put("Form_Id", cursorDataPosting.getString(cursorDataPosting.getColumnIndex("Form_Id")));
                                sbForm_Structure_Id.append(cursorDataPosting.getString(cursorDataPosting.getColumnIndex("Form_Structure_Id"))).append("|");
                                sbValue.append(cursorDataPosting.getString(cursorDataPosting.getColumnIndex("Value"))).append("|");
                                sbRemark.append(cursorDataPosting.getString(cursorDataPosting.getColumnIndex("Remark"))).append("|");
                                DatPostingJsonObject.put("Form_Structure_Id", sbForm_Structure_Id.toString());
                                DatPostingJsonObject.put("Value", sbValue.toString());
                                DatPostingJsonObject.put("Remark", sbRemark.toString());
                            } while (cursorDataPosting.moveToNext());
                        }
                        DataPostingArray.put(DatPostingJsonObject);
                    }
                    cursorDataPosting.close();

                    StringBuffer sbAlertForm_Structure_Id = new StringBuffer();
                    JSONObject AlertJsonObject = new JSONObject();
                    String selectQueryAlert = "SELECT * FROM AlertMaster WHERE Task_Id='"+TaskID+"'";
                    Cursor cursorAlert = db.rawQuery(selectQueryAlert, null);
                    if(cursorAlert.getCount() !=0) {
                        if (cursorAlert.moveToFirst()) {
                            do {
                                sbAlertForm_Structure_Id.append(cursorAlert.getString(cursorAlert.getColumnIndex("Form_Structure_Id"))).append("|");
                                AlertJsonObject.put("Form_Structure_Id", sbAlertForm_Structure_Id.toString());
                                AlertJsonObject.put("Task_Id", cursorAlert.getString(cursorAlert.getColumnIndex("Task_Id")));
                                AlertJsonObject.put("Form_Id", cursorAlert.getString(cursorAlert.getColumnIndex("Form_Id")));
                                AlertJsonObject.put("Alert_Type", cursorAlert.getString(cursorAlert.getColumnIndex("Alert_Type")));
                                AlertJsonObject.put("Critical", cursorAlert.getString(cursorAlert.getColumnIndex("Critical")));
                            } while (cursorAlert.moveToNext());
                        }
                        AlertArray.put(AlertJsonObject);
                    }
                    cursorAlert.close();
                    PPMTaskJsonArray.put(PPMTaskJsonObject);
                } while (cursor.moveToNext());
            }cursor.close();
            db.close();

        }catch (Exception E){
            E.printStackTrace();
        }

        try {

            TaskDetails.put("PPMTaskDetails",PPMTaskJsonArray);
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

        generateNoteOnSD(getApplicationContext(), DatabaseColumn.PPM_Task+".txt",UploadArray.toString());

        return UploadArray;
    }

    public void uploadTicketIMS(){

        String selectQuery = "SELECT * FROM Ticket_Created WHERE Status != '1' AND User_Group_Id IN ("+myDb.UserGroupId(User_Id)+")";
        Log.d("TicketQuery"," "+selectQuery);
        String groupName="",Remark="";
        try {
            db = myDb.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.d("TicketQueryCount", " " + cursor.getCount()+" "+selectQuery);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        Task_Id = cursor.getString(cursor.getColumnIndex("Task_Id"));
                        Form_Ticket_Id = cursor.getString(cursor.getColumnIndex("Form_Ticket_Id"));
                        Type = cursor.getString(cursor.getColumnIndex("Ticket_Type"));
                        Subject = cursor.getString(cursor.getColumnIndex("Ticket_Subject"));
                        //Ticket_status = cursor.getString(cursor.getColumnIndex("Ticket_status"));


                        String formTicketQuery = "SELECT * from Form_Ticket WHERE Auto_Id = '"+Form_Ticket_Id+"'";
                        Cursor formTicketCursor = db.rawQuery(formTicketQuery,null);
                        if(formTicketCursor.getCount()>0){
                            if(formTicketCursor.moveToFirst()) {
                                do {
                                    Group_Id = formTicketCursor.getString(formTicketCursor.getColumnIndex("Assigned_To"));
                                    Product = formTicketCursor.getString(formTicketCursor.getColumnIndex("Product"));
                                    IncidentSource = formTicketCursor.getString(formTicketCursor.getColumnIndex("IncidentSource"));
                                    LoggedBy = formTicketCursor.getString(formTicketCursor.getColumnIndex("Loggedby"));
                                    Form_Structure_Id = formTicketCursor.getString(formTicketCursor.getColumnIndex("Form_section_Id"));

                                    String GroupQuery = "SELECT Group_Name from User_Group where User_Group_Id = '" + Group_Id + "'";
                                    Cursor groupQueryCursor = db.rawQuery(GroupQuery, null);
                                    if (groupQueryCursor.getCount() > 0) {
                                        if (groupQueryCursor.moveToFirst()) {
                                            do {
                                                groupName = groupQueryCursor.getString(groupQueryCursor.getColumnIndex("Group_Name"));
                                            } while (groupQueryCursor.moveToNext());
                                        }
                                    }
                                    groupQueryCursor.close();

                                    String RemarkQuery = "Select * from Data_Posting where Task_Id = '" + Task_Id + "' and Form_Structure_Id = '" + Form_Structure_Id + "'";
                                    Cursor RemarkCursor = db.rawQuery(RemarkQuery, null);
                                    //String Value1 = "";
                                    Log.d("Remark", "" + RemarkCursor.getCount() + " " + RemarkQuery);

                                    if (RemarkCursor.getCount() > 0) {
                                        if (RemarkCursor.moveToFirst()) {
                                            do {
                                                //Value1 = RemarkCursor.getString(RemarkCursor.getColumnIndex("Value"));
                                                Remark = RemarkCursor.getString(RemarkCursor.getColumnIndex("Remark"));
                                                //Log.d("ValRemark", "" + Remark + " " + Value1);
                                            } while (RemarkCursor.moveToNext());
                                        }
                                    }
                                    RemarkCursor.close();
                                }while (formTicketCursor.moveToNext());
                            }
                        }
                        formTicketCursor.close();

                        String AssetDetails = "SELECT Asset_Id,Asset_Name from Task_Details where Auto_Id ='"+Task_Id+"'";
                        Cursor AssetCursor = db.rawQuery(AssetDetails, null);
                        //String Value1 = "";
                        Log.d("Asset", "" + AssetCursor.getCount() + " " + AssetDetails);

                        if (AssetCursor.getCount() > 0) {
                            if (AssetCursor.moveToFirst()) {
                                do {
                                    //Value1 = RemarkCursor.getString(RemarkCursor.getColumnIndex("Value"));
                                    AssetId = AssetCursor.getString(AssetCursor.getColumnIndex("Asset_Id"));
                                    Asset_Name = AssetCursor.getString(AssetCursor.getColumnIndex("Asset_Name"));
                                    //Log.d("ValRemark", "" + Remark + " " + Value1);
                                } while (AssetCursor.moveToNext());
                            }
                        }
                        AssetCursor.close();


                        data = URLEncoder.encode("site", "UTF-8") + "=" + URLEncoder.encode(myDb.getAssetSite(AssetId), "UTF-8") + "&" +
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

                        Log.d("JSONIMS"," 123 "+data);


                        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "https://punctualiti.in/csmia/android/raiseticket.php?" + data, null, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try {
                                    ContentValues value = new ContentValues();
                                    value.put("Status","1");
                                    if(LOG)Log.d("Response Log", "123 " + response.toString()+" "+Form_Ticket_Id);
                                    Toast.makeText(getApplicationContext(),
                                            "Ticket Generated", Toast.LENGTH_SHORT).show();
                                    if(!db.isOpen()){
                                        db = myDb.getWritableDatabase();
                                    }

                                    long d = db.update("Ticket_Created",value,"Task_Id ='"+Task_Id+"' and Form_Ticket_Id='"+Form_Ticket_Id+"'",null);
                                    if(LOG)Log.d("Test_update","21 "+d+" "+Task_Id+" "+Form_Ticket_Id);

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
                                    } /*catch (JSONException e2) {
                            // returned data is not JSONObject?
                            e2.printStackTrace();
                        }*/
                                }
                                VolleyLog.d(TAG, "Error: " + error.getMessage());
                                error.printStackTrace();
                                Toast.makeText(getApplicationContext(),
                                        "Something went wrong at server end", Toast.LENGTH_SHORT).show();
                            }}) {
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("Content-Type", "application/json");
                                return params;
                            }
                        };
                        new applicationClass().getInstance().addToRequestQueue(jsonArrayRequest);
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void generateNoteOnSD(Context context, String sFileName, String sBody) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Notevs");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            //Toast.makeText(getApplicationContext(),"FileCreated",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deletePPMTask(){
        site_id =myDb.Site_Location_Id(User_Id);
        db = myDb.getWritableDatabase();
        long resultset = db.delete("PPM_Task","Site_Location_Id='"+site_id+"'",null);
        Log.d("tetsdf"," "+resultset);
        if(resultset != -1){
            new DataDownload().execute();
        }

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
        wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifi= wifiManager.getConnectionInfo();
        int linkSpeed = wifiManager.getConnectionInfo().getRssi();
        return linkSpeed;
    }
    public String CarrierName() {
        if(haveNetworkConnection()=='W'){
            wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
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
        //isRunning=false;
        Toast.makeText(this, "AutoSync Stopped", Toast.LENGTH_LONG).show();
        //mHandler.removeCallbacks(m_Runnable);
        //mHandler.postDelayed(m_Runnable, 5000);
    }
}