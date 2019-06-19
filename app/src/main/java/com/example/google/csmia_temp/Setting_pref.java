package com.example.google.csmia_temp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by user on 19-09-2017.
 */

public class Setting_pref extends AppCompatActivity {
    String User_Id;
    static String SyncResult="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        User_Id = settings.getString("userId", null);
        getFragmentManager().beginTransaction().replace(android.R.id.content,new MyPreferenceFragment()).commit();
    }

    public static class MyPreferenceFragment extends PreferenceFragment{

        Preference preference_CheckForUpdate,preference_version;
        String syn_freq_value,  scan_type_val, task_reminder_val;
        String auto_upload_value="";
        String notification_value="";
        ListPreference TaskReminderLP, listPreference, listPreference1;
        int list_index, list_index_st;
        SwitchPreference Upload,notification;
        String list_val_st = "";
        DatabaseHelper mydb;
        String SiteId,User_Id;
        SQLiteDatabase db;



        private ProgressDialog pDialog;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            mydb = new DatabaseHelper(getActivity());
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
            User_Id = settings.getString("userId", null);
            SiteId = mydb.Site_Location_Id(User_Id);
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.setting_pref);

            Upload = (SwitchPreference)findPreference("auto_upload");

            if (mydb.AutoSyncSetting().equals("true")){
                Upload.setChecked(true);
            }
            else{
                Upload.setChecked(false);
            }
            Upload.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

                @Override
                public boolean onPreferenceChange(Preference preference, Object Obj) {
                    auto_upload_value = Obj.toString();
                    if (auto_upload_value == "true") {
                        SyncResult = auto_upload_value;
                        Thread t = new Thread(){
                            public void run(){
                                getActivity().startService(new Intent(getActivity(), MyService.class));
                            }
                        };
                        t.start();
                        ContentValues contentValues = new ContentValues();
                        db = mydb.getWritableDatabase();
                        contentValues.put("SyncStatus", auto_upload_value);
                        db.update("AutoSync", contentValues, null, null);
                        db.close();


                    } else {
                        SyncResult = auto_upload_value;
                        getActivity().stopService(new Intent(getActivity(), MyService.class));
                        db = mydb.getWritableDatabase();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("SyncStatus", auto_upload_value);
                        db.update("AutoSync", contentValues, null, null);
                        db.close();

                    }

                    return true;
                }
            });



            notification = (SwitchPreference)findPreference("notification");
            notification.setEnabled(true);
            if (mydb.Notification(User_Id).equals("true")){
                notification.setChecked(true);
            }
            else{
                notification.setChecked(false);
            }
            notification.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

                @Override
                public boolean onPreferenceChange(Preference preference, Object Obj) {
                    notification_value = Obj.toString();
                    Log.d("jhsdgf",notification_value);
                    if(notification_value=="true"){
                        TaskReminderLP.setEnabled(true);
                        ContentValues contentValues = new ContentValues();
                        db=mydb.getWritableDatabase();
                        contentValues.put("Notification", notification_value);
                        db.update("Settings", contentValues, "User_Id ='" + User_Id + "'", null);
                        db.close();
                    }
                    else{
                        TaskReminderLP.setEnabled(false);
                        db=mydb.getWritableDatabase();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("Notification", notification_value);
                        db.update("Settings",contentValues,"User_Id ='" + User_Id + "'", null);
                        db.close();
                    }

                    return true;
                }
            });

            TaskReminderLP = (ListPreference)findPreference("Task_Reminder");
            if (notification.isChecked() == true){
                TaskReminderLP.setEnabled(true);
            }else {
                TaskReminderLP.setEnabled(false);
            }
            TaskReminderLP.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    task_reminder_val = newValue.toString();
                    listPreference = (ListPreference)preference;
                    list_index =  listPreference.findIndexOfValue(task_reminder_val);
                    preference.setSummary(list_index >= 0 ? listPreference.getEntries()[list_index] : null);
                    Toast.makeText(getActivity(),"Selected value is:"+listPreference.getEntries()[list_index],Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

            listPreference = (ListPreference)findPreference("sync_frequency");
            listPreference.setEnabled(true);

            if (mydb.AutoSyncFreq().equals("15")){
                listPreference.setValueIndex(0);
            }else if(mydb.AutoSyncFreq().equals("30")){
                listPreference.setValueIndex(1);
            }else if(mydb.AutoSyncFreq().equals("60")){
                listPreference.setValueIndex(2);
            }else {
                listPreference.setValueIndex(3);
            }

            listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    syn_freq_value = o.toString();
                    listPreference = (ListPreference)preference;
                    list_index =  listPreference.findIndexOfValue(syn_freq_value);
                    preference.setSummary(list_index >= 0 ? listPreference.getEntries()[list_index] : null);
                    Toast.makeText(getActivity(),"Selected value is:"+listPreference.getEntries()[list_index],Toast.LENGTH_SHORT).show();
                    db=mydb.getWritableDatabase();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("SyncFreq", syn_freq_value);
                    db.update("AutoSync", contentValues, null, null);
                    db.close();
                    return true;
                }
            });

            listPreference1 = (ListPreference)findPreference("scan_type");
            listPreference1.setEnabled(false);
            if (mydb.ScanType(User_Id).equals("QR")){
                listPreference1.setValueIndex(0);
            }
            else{
                listPreference1.setValueIndex(1);
            }
            listPreference1.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    scan_type_val = o.toString();
                    listPreference1.setValueIndex(0);
                    list_index_st = listPreference1.findIndexOfValue(scan_type_val);
                    listPreference1.setSummary(list_index_st >= 0 ? listPreference1.getEntries()[list_index_st] : null);
                    list_val_st = String.valueOf(listPreference1.getEntries()[list_index_st]);
                    ContentValues contentValues = new ContentValues();
                    db=mydb.getWritableDatabase();
                    contentValues.put("Scan_Type", list_val_st);
                    db.update("Settings", contentValues, "User_Id ='" + User_Id + "'", null);
                    db.close();
                    return true;
                }
            });



            preference_CheckForUpdate = (Preference)findPreference("update");
            preference_CheckForUpdate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    new CheckingInternetConnectivity().execute();
                    return true;
                }
            });


            preference_version = (Preference)findPreference("version");
            preference_version.setSummary(BuildConfig.VERSION_NAME);
        }


        private class CheckingInternetConnectivity extends AsyncTask<Void, Void, Boolean> {
            boolean internetConnection=false;
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
                    new getApk().execute();
                }else {
                    Toast.makeText(getActivity(),"You are not connected to internet",Toast.LENGTH_SHORT).show();
                }
            }

        }

        public class getApk extends AsyncTask<String, String, String> {

            private ProgressDialog pDialog1;
            String Version="";
            String file_url="";
            String mainURL="";
            String Date="";
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog1 = new ProgressDialog(getActivity());
                pDialog1.setMessage("Checking new update...");
                pDialog1.setIndeterminate(false);
                pDialog1.setMax(100);
                pDialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pDialog1.setCancelable(true);
                pDialog1.show();
            }
            @Override
            protected String doInBackground(String... strings) {

                try {
                    int verCode = BuildConfig.VERSION_CODE;
                    HttpHandler handler = new HttpHandler();
                    String jsonStrSite= handler.apkDetails(verCode,User_Id);
                    if(jsonStrSite !=null){
                        JSONObject jsonObjSite = new JSONObject(jsonStrSite);
                        JSONArray siteDetails = jsonObjSite.getJSONArray("apk");
                        for (int i = 0; i < siteDetails.length(); i++) {
                            JSONObject c = siteDetails.getJSONObject(i);
                            file_url = c.getString("Url");
                            Version = c.getString("Version");
                            Date = c.getString("Release_Date");
                            mainURL = file_url + "" + Version + ".apk";
                            Log.d("Fdsafdasf",""+mainURL);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }



                return null;
            }
            @Override
            protected void onPostExecute(String file_url) {
                pDialog1.dismiss();
                PromptApkDialog(mainURL, Version, Date);
            }
        }

        public void PromptApkDialog(final String Url,String Version,String Date){

            try {
                if(Url.equalsIgnoreCase("")) {
                    AlertDialog dialog = new AlertDialog.Builder(getActivity())
                            .setTitle("Latest version already installed.")
                            .setMessage("Version " + BuildConfig.VERSION_NAME + " installed. You are running the latest version.")
                            .create();
                    dialog.show();
                }
                else {

                    AlertDialog dialog = new AlertDialog.Builder(getActivity())
                            .setTitle("New version available")
                            .setMessage("Version " + Version + "\n( Released : " + Date + ")")
                            .setPositiveButton("Update",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            new DownloadFileFromURL().execute(Url);
                                        }
                                    }).setNegativeButton("No, thanks",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    }).create();
                    dialog.show();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        class DownloadFileFromURL extends AsyncTask<String, String, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Downloading update. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(false);
                pDialog.show();
            }

            @Override
            protected String doInBackground(String... f_url) {
                int count;
                try {
                    URL url = new URL(f_url[0]);
;                   URLConnection conection = url.openConnection();
                    conection.connect();
                    int lenghtOfFile = conection.getContentLength();
                    InputStream input = new BufferedInputStream(url.openStream(), 8192);
                    OutputStream output = new FileOutputStream("/sdcard/multisite.apk");

                    byte data[] = new byte[1024];
                    long total = 0;
                    while ((count = input.read(data)) != -1) {
                        total += count;
                        publishProgress(""+(int)((total*100)/lenghtOfFile));
                        output.write(data, 0, count);
                    }
                    output.flush();
                    output.close();
                    input.close();

                } catch (Exception e) {
                    Log.e("Error: ", e.getMessage());
                }

                return null;
            }


            protected void onProgressUpdate(String... progress) {
                pDialog.setProgress(Integer.parseInt(progress[0]));
            }

            @Override
            protected void onPostExecute(String file_url) {
                pDialog.dismiss();
               // String path = "/storage/emulated/0/";
               // unpackZip(path, "multisite.zip");
                if(Build.VERSION.SDK_INT>=24){
                    try{
                        Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                        m.invoke(null);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                    intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory()+"/multisite.apk")), "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // without this flag android returned a intent error!
                    startActivity(intent);
                }
                else{
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory()+"/multisite.apk")), "application/vnd.android.package-archive");
                        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag android returned a intent error!
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }


        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("MyServices","SyncResult: "+SyncResult+" user_Id: "+User_Id);
        Intent intent = new Intent(getApplicationContext(),HomePage.class);
        intent.putExtra("SyncStatusResult",SyncResult);
        intent.putExtra("User_Id",User_Id);
        startActivity(intent);
    }
}
