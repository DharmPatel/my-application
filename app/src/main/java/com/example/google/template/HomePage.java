package com.example.google.template;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.google.template.ConstantList.Config;
import com.example.google.template.ConstantList.DatabaseColumn;
import com.example.google.template.ConstantList.UrlList;
import com.example.google.template.util.NotificationUtils;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import cz.msebera.android.httpclient.Header;




public class HomePage extends AppCompatActivity {

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    int NUM_PAGES;
    DatabaseHelper myDb;
    SQLiteDatabase db;
    TextView textViewNetworkstate;
    WifiManager wifiManager;
    private ProgressDialog pDialog;
    ViewPager viewPager;
    myPhoneStateListener mPhoneStatelistener;
    ImageView imageViewTask, imageViewForm, imageViewAssets, imageViewSync, imageViewTicket, imageViewWorkPermit,imageViewPPM,imageViewincidentReport,imageViewAlert;
    SharedPreferences settings;
    SharedPreferences settings1;
    JSONObject jsonObjSite;
    android.support.v7.widget.Toolbar toolbar;
    SharedPreferences.Editor editor1;
    SharedPreferences.Editor editorTaskInsert;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public static final String SYNCString = "Synchronizing Data. Please Wait..";
    String site_name;
    static boolean sync = false;
    ArrayList<HashMap<String, String>> contactList;
    LinearLayout linearLayout;
    TelephonyManager mTelephonyManager;
    private int STORAGE_PERMISSION_CODE = 23;
    Calendar calenderCurrent;
    public boolean internetConnection = false;
    int taskLimit, level, signalStrengthValue, count = 0, countvalue = 0, jumptime = 0, totalTask = 0;
    String User_Id = "", siteName = "", siteUrl, site_id = "", CompanyCustomerId = "", employeeName, Scan_Type, carrierName = "", URL = "", res1, jsonStrSite1, NFCbarcode, versionName, Task_Status = "Completed";
    boolean checkInternetforReconfig = false;
    private boolean isSpinnerInitial = true;
    StringBuffer stringBuffergroupId;
    private static final String TAG = HomePage.class.getSimpleName();
    static final boolean LOG = new applicationClass().checkLog();
    static final boolean WRITE_JSON_FILE = new applicationClass().writeJsonFile();
    char res;
    NFC nfc;
    BadgeView badgeView;
    NfcAdapter mNfcAdapter;
    TaskProvider taskProvider;
    int Asset_View;
    private String URLSTRING = new applicationClass().urlString()+"android/";
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 1000;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000;// time in milliseconds between successive task executions
    File[] files;
    JSONObject PPMresponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        taskProvider = new TaskProvider();
        myDb = new DatabaseHelper(getApplicationContext());
        editor1 = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        settings1 = PreferenceManager.getDefaultSharedPreferences(this);
        linearLayout = (LinearLayout) findViewById(R.id.MainLayout);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        contactList = new ArrayList<>();
        imageViewForm = (ImageView) findViewById(R.id.imageViewForm);
        imageViewTicket = (ImageView) findViewById(R.id.imageViewTicket);
        imageViewAssets = (ImageView) findViewById(R.id.imageViewAssets);
        imageViewSync = (ImageView) findViewById(R.id.imageViewSync);
        imageViewTask = (ImageView) findViewById(R.id.imageViewTask);
        imageViewAlert = (ImageView) findViewById(R.id.imageViewAlert);
        imageViewWorkPermit = (ImageView) findViewById(R.id.imgworkpermit);
        imageViewincidentReport = (ImageView) findViewById(R.id.imgReport);
        imageViewPPM = (ImageView) findViewById(R.id.imgViewPPM);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        requestStoragePermission();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.bg_main)));
        if (new applicationClass().fabButton() == false) {
            fab.setVisibility(View.GONE);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(HomePage.this);
                integrator.setPrompt("Scan a QRcode");
                integrator.setOrientationLocked(true);
                Intent intent = integrator.createScanIntent();
                startActivityForResult(intent, 49374);
            }
        });
        getBatteryPercentage();
        User_Id = getIntent().getStringExtra("User_Id");
        Asset_View = getIntent().getIntExtra("Asset_View",0);  // getIntent().getIntExtra("Asset_View");
        CompanyCustomerId = getIntent().getStringExtra("Company_Customer_Id");

        editor1 = settings.edit();
        editor1.putString("userId", User_Id);
        editor1.commit();

        site_id = myDb.Site_Location_Id(User_Id);
        imageViewAssets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assetActivity();
            }
        });
        imageViewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskdetailsActivity();
            }
        });
        imageViewForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checklistActivity();
            }
        });
        imageViewAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertActivity();
            }
        });
        imageViewSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                synchronizeData();
            }
        });
        imageViewTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ticketActivity();
            }
        });
        imageViewWorkPermit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workpermitActivity();
            }
        });
        imageViewincidentReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incidentReportActivity();
            }
        });
        imageViewPPM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               ppmActivity();
                //getPPMTask();
                /*Intent intent = new Intent(getApplicationContext(), ppm_activity.class);
                intent.putExtra("TAB", "TAB2");
                startActivity(intent);*/
            }
        });
        mPhoneStatelistener = new myPhoneStateListener();
        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mTelephonyManager.listen(mPhoneStatelistener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

        initToolBar();
        SettingPref();
        AutoSyncPref();
        checkNFC(myDb.ScanType(User_Id));
        setAlertCount();
        deletePreviousData();
        insertMissedTask();
        insertCancelledTask();
        Slider();
        //downloadAsset();




        //onNewIntent(getIntent());

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


                // checking for type intent filter


                Log.d("testasdasdasdasd1234",intent.getStringExtra("message"));
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");
                    if(message.equalsIgnoreCase("Update")){
                        PromptApkDialog();
                    }

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                }
            }
        };


        //new CheckDateTime().execute();
       /* if(settings.getString("day_apkupdate", null) == null){
            Toast.makeText(getApplicationContext(),"Activated",Toast.LENGTH_LONG).show();
            new getApk().execute();
        }
        else {
            if (parseDate1(settings.getString("day_apkupdate", null)).before(parseDate1(new applicationClass().yymmdd()))) {
                new getApk().execute();
            }
            Toast.makeText(getApplicationContext(),"Not Activated",Toast.LENGTH_LONG).show();
        }*/
    }



    public void assetActivity() {
        try {
            if (myDb.SiteName(User_Id) == null) {
                Toast.makeText(getApplicationContext(), "Please Select Site", Toast.LENGTH_SHORT).show();
            } else {
                String Query = "Select * from Asset_Details where Site_Location_Id='" + myDb.Site_Location_Id(User_Id) + "'";
                db = myDb.getWritableDatabase();
                Cursor cursor = db.rawQuery(Query, null);
                if (cursor.getCount() > 0) {
                    if (myDb.ScanType(User_Id).equals("QR")) {
                        Intent intent = new Intent(HomePage.this, AssetsActivity.class);
                        startActivity(intent);
                    } else if (myDb.ScanType(User_Id).equals("NFC")) {
                        if (mNfcAdapter == null) {
                            Snackbar snackbar = Snackbar.make(linearLayout, " This device doesn't support NFC!!", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        } else {
                            Intent intent = new Intent(HomePage.this, AssetsActivity.class);
                            startActivity(intent);
                        }
                    }
                } else {
                    final Snackbar snackbar = Snackbar
                            .make(linearLayout, "Data not available.", Snackbar.LENGTH_LONG)
                            .setAction("Please Synchronize", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    new CheckingInternetConnectivity().execute();
                                }
                            });

                    snackbar.setDuration(20000);
                    snackbar.show();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ppmActivity(){
        Snackbar snackbar = Snackbar.make(linearLayout, "License not activated.", Snackbar.LENGTH_LONG);
        snackbar.show();

        /*try {
            if (myDb.SiteName(User_Id) == null) {
                Toast.makeText(getApplicationContext(), "Please Select Site", Toast.LENGTH_SHORT).show();
            }else {
                String Query = "select * from PPM_Task where Site_Location_Id = '" + myDb.Site_Location_Id(User_Id) + "'";
                db = myDb.getWritableDatabase();
                Cursor cursor = db.rawQuery(Query, null);
                if (cursor.getCount() > 0) {
                    if (myDb.ScanType(User_Id).equals("QR")) {
                        Intent intent = new Intent(HomePage.this, ppm_activity.class);
                        intent.putExtra("TAB", "TAB2");
                        startActivity(intent);
                        finish();
                    } else if (myDb.ScanType(User_Id).equals("NFC")) {
                        if (mNfcAdapter == null) {
                            Snackbar snackbar = Snackbar.make(linearLayout, " This device doesn't support NFC!!", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        } else {
                            Intent intent = new Intent(HomePage.this, ppm_activity.class);
                            intent.putExtra("TAB", "TAB2");
                            startActivity(intent);
                            finish();
                        }
                    }
                }else {
                    final Snackbar snackbar = Snackbar
                            .make(linearLayout, "Data not available.", Snackbar.LENGTH_LONG)
                            .setAction("Please Synchronize", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    new CheckingInternetConnectivity().execute();
                                }
                            });

                    snackbar.setDuration(20000);
                    snackbar.show();

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }*/
    }

    public void taskdetailsActivity() {
        try {
            if (myDb.SiteName(User_Id) == null) {
                Toast.makeText(getApplicationContext(), "Please Select Site", Toast.LENGTH_SHORT).show();
            } else {
                String Query = "Select * from Activity_Frequency where Site_Location_Id='" + myDb.Site_Location_Id(User_Id) + "'";
                db = myDb.getWritableDatabase();
                Cursor cursor = db.rawQuery(Query, null);
                if (cursor.getCount() > 0) {
                    if (myDb.ScanType(User_Id).equals("QR")) {
                        Intent intent = new Intent(HomePage.this, TaskDetails.class);
                        intent.putExtra("TAB", "TAB2");
                        startActivity(intent);
                        finish();
                    } else if (myDb.ScanType(User_Id).equals("NFC")) {
                        if (mNfcAdapter == null) {
                            Snackbar snackbar = Snackbar.make(linearLayout, " This device doesn't support NFC!!", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        } else {
                            Intent intent = new Intent(HomePage.this, TaskDetails.class);
                            intent.putExtra("TAB", "TAB2");
                            startActivity(intent);
                            finish();
                        }
                    }
                } else {


                        final Snackbar snackbar = Snackbar
                                .make(linearLayout, "Data not available.", Snackbar.LENGTH_LONG)
                                .setAction("Please Synchronize", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        new CheckingInternetConnectivity().execute();
                                    }
                                });

                        snackbar.setDuration(20000);
                        snackbar.show();

                }



            }
        } catch (Exception e) {
            e.printStackTrace();
            if (LOG)Log.d("hp181", "ERROR==" + e);
            Toast.makeText(getApplicationContext(), "Error code: hp181 " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void checklistActivity() {
        //SelectQuery();
        Snackbar snackbar = Snackbar.make(linearLayout, "License not activated.", Snackbar.LENGTH_LONG);
        snackbar.show();



        /*try {
            if (myDb.SiteName(User_Id) == null) {
                Toast.makeText(getApplicationContext(), "Please Select Site", Toast.LENGTH_SHORT).show();
            } else {


                String Query = "Select * from Activity_Frequency where Site_Location_Id='" + myDb.Site_Location_Id(User_Id) + "'";
                db = myDb.getWritableDatabase();
                Cursor cursor = db.rawQuery(Query, null);
                if (cursor.getCount() > 0) {
                    if (myDb.ScanType(User_Id).equals("QR")) {
                        Intent intent = new Intent(HomePage.this, CheckList.class);
                        intent.putExtra("TAB", "TAB1");
                        startActivity(intent);
                        finish();
                    } else if (myDb.ScanType(User_Id).equals("NFC")) {
                        if (mNfcAdapter == null) {
                            Snackbar snackbar = Snackbar.make(linearLayout, " This device doesn't support NFC!!", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        } else {
                            Intent intent = new Intent(HomePage.this, CheckList.class);
                            intent.putExtra("TAB", "TAB1");
                            startActivity(intent);
                            finish();
                        }
                    }
                } else {

                   *//* if(taskProvider.getNoTaskAssigned() == false){

                        final Snackbar snackbar = Snackbar.make(linearLayout, "No Task Available For This Site", Snackbar.LENGTH_LONG);

                        snackbar.show();
                    }
                    else {*//*
                    final Snackbar snackbar = Snackbar
                            .make(linearLayout, "Data not available.", Snackbar.LENGTH_LONG)
                            .setAction("Please Synchronize", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    new CheckingInternetConnectivity().execute();
                                }
                            });

                    snackbar.setDuration(20000);
                    snackbar.show();
                    // }
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("hp181", "ERROR==" + e);
            Toast.makeText(getApplicationContext(), "Error code: hp181 " + e.toString(), Toast.LENGTH_SHORT).show();
        }*/
    }

    public void SelectQuery(){
       /*db = myDb.getWritableDatabase();
        db.delete("RefrenseTable",null, null);
        db.close();*/
        /*String Query = "Select * from User_Group";
        db=myDb.getWritableDatabase();
        Cursor res2 = db.rawQuery(Query, null);
        if (res2.getCount() == 0) {
            Toast.makeText(HomePage.this, "Nothing Found !!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            StringBuffer buffer = new StringBuffer();
            while (res2.moveToNext()) {
                buffer.append("User_Id  :  " + res2.getString(res2.getColumnIndex("User_Group_Id")) + "\n");
                buffer.append("Site_Location_Id  :  " + res2.getString(res2.getColumnIndex("Group_Name")) + "\n");


                buffer.append("\n");
            }
            showMessage("Data", buffer.toString());
        }
        db.close();*/
    }
    public void showMessage(String title, String msg)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.show();
    }
    public void Slider(){
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        SliderPager viewPagerAdapter = new SliderPager(this,getImageArray(),imagesize());
        NUM_PAGES = imagesize();
        viewPagerAdapter.notifyDataSetChanged();
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.setCurrentItem(1, true);

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };

        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(Update);
            }
        }, 500, 3000);
    }


    public int imagesize(){
        int value = 1;
        try {
            myDb = new DatabaseHelper(new applicationClass().getContext());

            db = myDb.getReadableDatabase();
            if(!db.isDbLockedByCurrentThread()){


                String UserGroupQuery = "Select COUNT(Site_Locat" +
                        "ion_Id) from site_imagelist WHERE Site_Location_Id ='"+myDb.Site_IdUserSiteLinking(User_Id)+"' AND Record_Status !='D'";
                Cursor cursor1 = db.rawQuery(UserGroupQuery, null);
                if (cursor1.moveToFirst()) {
                    do {
                        value=cursor1.getInt(0);
                    } while (cursor1.moveToNext());
                }

           /* cursor1.moveToFirst();
            value = cursor1.getInt(0);*/
                cursor1.close();
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return value;
    }
    private ArrayList<Bitmap> getImageArray(){
        ArrayList<Bitmap> bitmapImages = new ArrayList();

        File mydir = getApplicationContext().getDir("images", Context.MODE_PRIVATE); //Creating an internal dir;
        if (mydir.exists()) {
            files = mydir.listFiles();
            //Log.d("Files", "Size: " + files.length);

        }
        int indx = 0;

        db = myDb.getWritableDatabase();
        String UserGroupQuery = "Select Image_Name from site_imagelist WHERE Site_Location_Id ='" + myDb.Site_IdUserSiteLinking(User_Id) + "' AND Record_Status !='D'";
        Cursor cursor1 = db.rawQuery(UserGroupQuery, null);
        if (cursor1.getCount() != 0) {


            while (cursor1.moveToNext()) {

                for (int i = 0; i < files.length; i++) {
                    //Log.d("Files", "FileName:" + files[i].getName());
                    if (files[i].exists()) {

                        if (files[i].getName().equals(cursor1.getString(cursor1.getColumnIndex("Image_Name")))) {
                            Bitmap myBitmap = BitmapFactory.decodeFile(files[i].getAbsolutePath());
                            bitmapImages.add(myBitmap);
                        }
                   /* String[] separated = files[i].getName().split("\\.");
                    if(separated[1].equalsIgnoreCase("jpg")|separated[1].equalsIgnoreCase("png")){
                        Bitmap myBitmap = BitmapFactory.decodeFile(files[i].getAbsolutePath());
                        bitmapImages.add(myBitmap);
                        images[indx++] =i;

                    }*/

                    }
                }
            }
        }else {

        }

        cursor1.close();
        db.close();

        return bitmapImages;
    }


    public void alertActivity() {
        try {
            if (myDb.SiteName(User_Id) == null) {
                Toast.makeText(getApplicationContext(), "Please Select Site", Toast.LENGTH_SHORT).show();
            } else {
                String Query = "Select * from Asset_Details";
                db = myDb.getWritableDatabase();
                Cursor cursor = db.rawQuery(Query, null);
                if (cursor.getCount() > 0) {
                    Intent intent = new Intent(HomePage.this, AlertActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    final Snackbar snackbar = Snackbar
                            .make(linearLayout, "Data not available.", Snackbar.LENGTH_LONG)
                            .setAction("Please Synchronize", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    new CheckingInternetConnectivity().execute();
                                }
                            });

                    snackbar.setDuration(20000);
                    snackbar.show();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            if (LOG)Log.d("hp222", "ERROR==" + e);
            Toast.makeText(getApplicationContext(), "Error code: hp222 " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    public void synchronizeData() {
        try {
            if (myDb.SiteName(User_Id) == null) {
                Toast.makeText(getApplicationContext(), "Please Select Site", Toast.LENGTH_SHORT).show();
            } else {
                new CheckingInternetConnectivity().execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (LOG)Log.d("hp273", "ERROR==" + e);
            Toast.makeText(getApplicationContext(), "Error code: hp273 " + e.toString(), Toast.LENGTH_SHORT).show();

        }
    }
    public void ticketActivity() {
        Snackbar snackbar = Snackbar.make(linearLayout, "License not activated.", Snackbar.LENGTH_LONG);
        snackbar.show();
        /*try {
            if (myDb.SiteName(User_Id) == null) {
                Toast.makeText(getApplicationContext(), "Please Select Site", Toast.LENGTH_SHORT).show();
            } else {
                String Query = "Select * from Asset_Details where Site_Location_Id='" + myDb.Site_Location_Id(User_Id) + "'";
                db = myDb.getWritableDatabase();
                Cursor cursor = db.rawQuery(Query, null);
                if (cursor.getCount() > 0) {
                    Intent intent = new Intent(HomePage.this, TicketActivity.class);
                    startActivity(intent);
                } else {
                    final Snackbar snackbar = Snackbar
                            .make(linearLayout, "Data not available.", Snackbar.LENGTH_LONG)
                            .setAction("Please Synchronize", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    new CheckingInternetConnectivity().execute();
                                }
                            });

                    snackbar.setDuration(20000);
                    snackbar.show();
                }
               *//* Intent intent = new Intent(HomePage.this, TicketActivity.class);
                startActivity(intent);*//*
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("hp300", "ERROR==" + e);
            Toast.makeText(getApplicationContext(), "Error code: hp301 " + e.toString(), Toast.LENGTH_SHORT).show();
        }*/
    }
    public void workpermitActivity(){
        /*Intent intent = new Intent(this,Notification.class);
        startActivity(intent);*/
        Snackbar snackbar = Snackbar.make(linearLayout, "License not activated.", Snackbar.LENGTH_LONG);
        snackbar.show();
    }
    public void incidentReportActivity(){
        Snackbar snackbar = Snackbar.make(linearLayout, "License not activated.", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public void deletePreviousData() {
        try {
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            final SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            if(LOG)Log.d(TAG, "PreviousTime" + simpleDateFormat1.format(calendar.getTime()));
            if (myDb.Site_Location_Id(User_Id) != null) {
                db = myDb.getWritableDatabase();
                db.delete("Task_Details", "date(Task_Scheduled_Date) < date('" + simpleDateFormat.format(new Date()) + "') AND UpdatedStatus = 'yes'", null);
                db.delete("Task_Details", "date(Task_Start_At) < date('" + simpleDateFormat.format(new Date()) + "') AND UpdatedStatus = 'yes'", null);
                db.delete("Task_Details_Server", "date(Task_Scheduled_Date) < date('" + simpleDateFormat.format(new Date()) + "') AND UpdatedStatus = 'yes'", null);
                db.delete("Data_Posting", "Task_Id NOT IN(SELECT Auto_Id FROM Task_Details) AND UpdatedStatus ='yes'", null);
                db.delete("Meter_Reading", "date(Task_Start_At) < date('" + simpleDateFormat1.format(calendar.getTime()) + "') AND UpdatedStatus = 'yes'", null);
                db.delete("AlertMaster", "Task_Id NOT IN(SELECT Auto_Id FROM Task_Details) AND UpdatedStatus ='yes'", null);
                db.delete("Task_Selfie", "Task_Id NOT IN(SELECT Auto_Id FROM Task_Details) AND UpdatedStatus ='yes'", null);
                db.delete("AssetStatusLog", "date(Asset_Updated_Time) < date('" + simpleDateFormat.format(new Date()) + "') AND UpdatedStatus = 'yes'", null);
                db.delete("Ticket_Master", "date(Created_At) < date('" + simpleDateFormat.format(new Date()) + "') AND UpdatedStatus = 'yes'", null);
                db.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void SettingPref() {
        try {
            String query = "SELECT * FROM Settings Where User_Id ='" + User_Id + "'";
            db = myDb.getWritableDatabase();
            site_name = settings1.getString("siteName", null);
            Cursor res = db.rawQuery(query, null);
            if (res.getCount() == 0) {
                myDb.insertVal(User_Id, site_id,"", new applicationClass().Notification(), new applicationClass().defaultNFC(), site_name, URL,Asset_View);
            }
            res.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void AutoSyncPref() {
        try {
            boolean isInserted = false;
            String query = "SELECT * FROM AutoSync";
            db = myDb.getWritableDatabase();
            Cursor res = db.rawQuery(query, null);
            if (res.getCount() == 0) {
                isInserted = myDb.insertAutoSync(myDb.Site_Location_Id(User_Id), new applicationClass().AutoSync(), new applicationClass().AutoSyncFreq());
            }
            res.close();
            db.close();
            if (isInserted == true) {
                if (myDb.AutoSyncSetting().equals("true")) {
                    startService(new Intent(getApplicationContext(), MyService.class));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private boolean isReadStorageAllowed() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) + ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) + ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;
        return false;
    }
    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
    public void initToolBar() {
        try {
            db = myDb.getWritableDatabase();
            HashMap<String, String> spinnerArrayHash = new HashMap<>();
            List<String> spinnerArray = new ArrayList<String>();
            toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
            final Spinner spinner = (Spinner) findViewById(R.id.spinnerSite);
            TextView tvUsrname = (TextView) findViewById(R.id.tvUsername);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

            String query = "SELECT Employee_Name FROM Login_Details Where User_Id ='"+User_Id+"'";
            Cursor res =db.rawQuery(query, null);
            if (res.moveToFirst()) {
                do {
                    employeeName=res.getString(0);
                    tvUsrname.setText(res.getString(0)+"v"+ BuildConfig.VERSION_NAME);
                } while (res.moveToNext());
            }
            res.close();

            //tvUsrname.setText(settings.getString("Username", null) +"v"+ BuildConfig.VERSION_NAME);
            try {
                Cursor siteCursor = db.rawQuery("select * from Site_Details where Assigned_To_User_Id='"+User_Id+"'", null);
                if (siteCursor.getCount() == 1) {
                    if (siteCursor.moveToFirst()) {
                        do {
                            String project_name = siteCursor.getString(siteCursor.getColumnIndex("Site_Name_Label"));
                            spinnerArrayHash.put(siteCursor.getString(siteCursor.getColumnIndex("Id")), siteCursor.getString(siteCursor.getColumnIndex("Site_Name_Label")));
                            spinnerArray.add(project_name);
                        }
                        while (siteCursor.moveToNext());
                    }
                } else {
                    spinnerArray.add("Select Site");
                    if (siteCursor.moveToFirst()) {
                        do {
                            String project_name = siteCursor.getString(siteCursor.getColumnIndex("Site_Name_Label"));
                            spinnerArrayHash.put(siteCursor.getString(siteCursor.getColumnIndex("Id")), siteCursor.getString(siteCursor.getColumnIndex("Site_Name_Label")));
                            spinnerArray.add(project_name);

                        }
                        while (siteCursor.moveToNext());
                    }
                }
                siteCursor.close();

            } catch (Exception e) {
                if(LOG)Log.d("TAG", "username" + e.toString());
            }


            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(HomePage.this, R.layout.simple_spinner_item_demo, spinnerArray);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter1);
            try {
                //site_name = settings1.getString("siteName", null);

                Iterator it = spinnerArrayHash.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> pair = (Map.Entry) it.next();
                    System.out.println(pair.getKey() + " = " + pair.getValue());
                    if(LOG)Log.d(TAG, "SiteName" + myDb.SiteName(User_Id));
                    if (myDb.SiteName(User_Id).equalsIgnoreCase(pair.getValue())) {
                        int i = adapter1.getPosition(pair.getValue());
                        spinner.setSelection(i);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                    db = myDb.getWritableDatabase();
                    Cursor res = db.rawQuery("select * from Site_Details where Assigned_To_User_Id='" + User_Id + "'", null);
                    if (res.getCount() == 1) {
                        try {
                            String item = parent.getItemAtPosition(position).toString();
                            Cursor cursor = db.rawQuery("Select Auto_Id,Site_Name_Label,Site_Name_Value from Site_Details where Site_Name_Label = '" + item + "'", null);
                            if (cursor.moveToFirst()) {
                                do {
                                    site_id = cursor.getString(0);
                                    site_name = cursor.getString(1);
                                    siteUrl = cursor.getString(2);
                                }
                                while (cursor.moveToNext());
                            }
                            cursor.close();
                            stringBuffergroupId = new StringBuffer();
                            Cursor cursorLinking = db.rawQuery("Select User_Group_Id from UserSiteLinking where Site_Location_Id = '" + site_id + "' and User_Id='" + User_Id + "'", null);
                            if (cursorLinking.moveToFirst()) {
                                do {
                                    String cursorLinkingstr = cursorLinking.getString(0);
                                    stringBuffergroupId.append("'" + cursorLinkingstr + "',");

                                }
                                while (cursorLinking.moveToNext());
                            }
                            cursorLinking.close();
                            stringBuffergroupId.deleteCharAt(stringBuffergroupId.lastIndexOf(","));
                            if (LOG) Log.d("fdasfasdfdas", "ff" + stringBuffergroupId);

                            URL = new applicationClass().urlString() + siteUrl + "/android/";
                            editor1 = settings.edit();
                            editor1.putString("userId", User_Id);
                            editor1.commit();
                            myDb.UpdateSiteName(site_name, stringBuffergroupId.toString(), User_Id, URL, site_id);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            if (isSpinnerInitial) {
                                isSpinnerInitial = false;
                            } else {
                                String item = parent.getItemAtPosition(position).toString();
                                Cursor cursor = db.rawQuery("Select Auto_Id,Site_Name_Label,Site_Name_Value from Site_Details where Site_Name_Label = '" + item + "'", null);
                                try {
                                    if (cursor.moveToFirst()) {
                                        do {
                                            site_id = cursor.getString(0);
                                            site_name = cursor.getString(1);
                                            siteUrl = cursor.getString(2);

                                        }
                                        while (cursor.moveToNext());
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                stringBuffergroupId = new StringBuffer();
                                Cursor cursorLinking = db.rawQuery("Select User_Group_Id from UserSiteLinking where Site_Location_Id = '" + site_id + "' and User_Id='" + User_Id + "'", null);
                                if (cursorLinking.moveToFirst()) {
                                    do {
                                        String cursorLinkingstr = cursorLinking.getString(0);
                                        stringBuffergroupId.append("'" + cursorLinkingstr + "',");

                                    }
                                    while (cursorLinking.moveToNext());
                                }
                                cursorLinking.close();
                                stringBuffergroupId.deleteCharAt(stringBuffergroupId.lastIndexOf(","));
                                cursor.close();
                                if (position == 0) {
                                    Snackbar snackbar = Snackbar.make(linearLayout, "Please Select Site !!", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                    isSpinnerInitial = true;
                                    initToolBar();
                                } else {
                                    try {
                                        if (myDb.SiteName(User_Id) == null) {
                                            taskProvider.setNoTaskAssigned(true);
                                            editor1 = settings.edit();
                                            editor1.putString("userId", User_Id);
                                            URL = new applicationClass().urlString() + siteUrl + "/android/";
                                            editor1.commit();
                                            myDb.UpdateSiteName(site_name, stringBuffergroupId.toString(), User_Id, URL, site_id);
                                        } else {
                                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomePage.this);
                                            alertDialog.setTitle("Confirm Site...");

                                            alertDialog.setMessage("Are you sure you want to select " + site_name);
                                            alertDialog.setPositiveButton("YES",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                            taskProvider.setNoTaskAssigned(true);
                                                            editor1 = settings.edit();
                                                            editor1.putString("userId", User_Id);
                                                            URL = new applicationClass().urlString() + siteUrl + "/android/";
                                                            editor1.commit();
                                                            myDb.UpdateSiteName(site_name, stringBuffergroupId.toString(), User_Id, URL, site_id);
                                                            Intent homepage = new Intent(HomePage.this, HomePage.class);
                                                            homepage.putExtra("User_Id", User_Id);
                                                            startActivity(homepage);
                                                            finish();
                                                        }
                                                    });
                                            // Setting Negative "NO" Button
                                            alertDialog.setNegativeButton("NO",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            //dialog.cancel();
                                                            dialog.cancel();
                                                            initToolBar();
                                                            isSpinnerInitial = true;

                                                        }
                                                    });
                                            alertDialog.show();

                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    res.close();
                    db.close();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void checkNFC(String ScanType) {
        try {
            if (!ScanType.equals("QR")) {

                nfc = new NFC();
                nfc.onCreate();
                mNfcAdapter = NfcAdapter.getDefaultAdapter(getApplicationContext());
                if (mNfcAdapter == null) {
                } else {
                    if (!mNfcAdapter.isEnabled()) {
                        Toast.makeText(getApplicationContext(), "NFC is disabled.", Toast.LENGTH_SHORT).show();
                    } else {
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setAlertCount() {
        try {
            badgeView = new BadgeView(this,imageViewAlert);
            badgeView.show();
/*
            TextView textViewBadge = (TextView) findViewById(R.id.search_badge);
*/
            if (myDb.Site_Location_Id(User_Id) != null) {
                String alertQuery = "Select * FROM AlertMaster WHERE AlertMaster.Assigned_To_User_Group_Id IN (" + myDb.UserGroupId(User_Id) + ") and Site_Location_Id='" + myDb.Site_Location_Id(User_Id) + "' and ViewFlag='no' GROUP BY AlertMaster.Task_Id";
                db = myDb.getReadableDatabase();
                Cursor cursor = db.rawQuery(alertQuery, null);
                int alertCount = cursor.getCount();
                cursor.close();
                if (alertCount == 0) {
                    badgeView.setVisibility(View.GONE);
                } else {
                    badgeView.setVisibility(View.VISIBLE);
                    badgeView.setText("" + alertCount);
                }
            } else {
                badgeView.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void insertMissedTask() {
        try {
            db = myDb.getWritableDatabase();
            calenderCurrent = Calendar.getInstance();
            String pendingQuery = " SELECT * FROM Task_Details WHERE Assigned_To_User_Id = '" + User_Id + "' AND Site_Location_Id='" + myDb.Site_Location_Id(User_Id) + "' AND Asset_Status= 'WORKING'  AND Task_Status='Pending'";
            Cursor cursor = db.rawQuery(pendingQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    String TaskId = cursor.getString(cursor.getColumnIndex("Auto_Id"));
                    String EndDateTime = cursor.getString(cursor.getColumnIndex("EndDateTime"));
                    if (calenderCurrent.getTime().after(parseDate(EndDateTime))) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("Task_Status", "Missed");
                        contentValues.put("Task_Start_At", formatDate(calenderCurrent.getTime()));
                        contentValues.put("Scan_Type", "");
                        contentValues.put("UpdatedStatus", "no");
                        db.update("Task_Details", contentValues, "Auto_Id ='" + TaskId + "' AND  Task_Status='Pending'", null);
                    }
                }
                while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void insertCancelledTask() {
        try {
            db = myDb.getWritableDatabase();
            calenderCurrent = Calendar.getInstance();
            String pendingQuery = " SELECT * FROM Task_Details WHERE Assigned_To_User_Id = '" + User_Id + "' AND Site_Location_Id='" + myDb.Site_Location_Id(User_Id) + "'  AND Task_Status ='Cancelled' ";
            Cursor cursor = db.rawQuery(pendingQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    String TaskId = cursor.getString(cursor.getColumnIndex("Auto_Id"));
                    String EndDateTime = cursor.getString(cursor.getColumnIndex("EndDateTime"));

                    try {
                        if (calenderCurrent.getTime().after(parseDate(EndDateTime))) {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("UpdatedStatus", "no");
                            db.update("Task_Details", contentValues, "Auto_Id ='" + TaskId + "' AND  Task_Status='Cancelled' AND UpdatedStatus IS null", null);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public class CheckDateTime extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            String Server_Date_Time = new HttpHandler().getTimeServer();
            return Server_Date_Time;
        }

        @Override
        protected void onPostExecute(String file_url) {
            Log.d("fdsafdsfafff33"," ** "+"Server  : "+file_url+"\n"+"Current  : "+new applicationClass().yymmddhhmm());
        }
    }
*/

    public void downloadAsset() {
        try {
            String Query = "Select * from Asset_Details";
            db = myDb.getWritableDatabase();
            Cursor cursor = db.rawQuery(Query, null);

            if (cursor.getCount() > 0 && myDb.Site_Location_Id(User_Id) != null) {
                new downloadAsset().execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public class downloadAsset extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {

            try {
                HttpHandler handler = new HttpHandler();
                String jsonStrSite = handler.downloadRefrense(myDb.Site_Location_Id(User_Id), myDb.getRecord(myDb.Site_Location_Id(User_Id)));
                jsonObjSite = new JSONObject(jsonStrSite);
                JSONArray assetDetails = jsonObjSite.getJSONArray("Refrence");
                for (int i = 0; i < assetDetails.length(); i++) {
                    JSONObject c = assetDetails.getJSONObject(i);
                    String Auto_Id = c.getString("Auto_Id");
                    String Record_Id = c.getString("Record_Key");
                    String Table_Name = c.getString("Table_Name");
                    String Updated_Id = c.getString("Update_Id");
                    String DateTime = c.getString("DateTime");
                    String Site_Location_Id = c.getString("Site_Location_Id");

                    String selectQuery = "SELECT  * FROM RefrenseTable where Site_Location_Id='" + Site_Location_Id + "' AND Record_Id = '" + Record_Id + "'";
                    db = myDb.getWritableDatabase();
                    Cursor cursor = db.rawQuery(selectQuery, null);
                    if(LOG)Log.d(TAG, "RefrenseTableCount1" + cursor.getCount());
                    if (cursor.getCount() == 0) {
                        ContentValues contentValues1 = new ContentValues();
                        contentValues1.put("Auto_Id", Auto_Id);
                        contentValues1.put("Record_Id", Record_Id);
                        contentValues1.put("Table_Name", Table_Name);
                        contentValues1.put("Updated_Id", Updated_Id);
                        contentValues1.put("Site_Location_Id", Site_Location_Id);
                        contentValues1.put("DateTime", DateTime);
                        contentValues1.put("UpdatedStatus", "no");
                        long rf = db.insert("RefrenseTable", null, contentValues1);
                        if (rf == -1) {
                            if(LOG)Log.d(TAG, "RefrenseTable" + "Not Inserted");

                        } else {
                            if(LOG)Log.d(TAG, "RefrenseTable" + "Inserted   " + Auto_Id);
                        }
                    }
                    cursor.close();
                    db.close();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            PromptApkDialog();
        }
    }
    public void PromptApkDialog() {

        try {
            if (LOG)Log.d(TAG, "refrenseTableCount" + myDb.refrenseTableCount(myDb.Site_Location_Id(User_Id)));
            //if (myDb.refrenseTableCount(myDb.Site_Location_Id(User_Id)) != 0) {
                AlertDialog dialog = new AlertDialog.Builder(HomePage.this)
                        .setTitle("New update found")
                        .setMessage("Do you want to update data?")
                        .setPositiveButton("Update",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        db = myDb.getWritableDatabase();
                                        new downloadAssetDetails().execute();
                                    }
                                }).create();
                dialog.setCancelable(false);
                dialog.show();
            //}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public class downloadAssetDetails extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(HomePage.this);

           //Color.rgb()));
            //pDialog.
            pDialog.setMessage("Please Wait..");
            pDialog.setIndeterminate(false);
            pDialog.setMax(200);
            pDialog.setProgress(0);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                final StringBuffer stringBuffer = new StringBuffer();
                final StringBuffer stringBufferTableName = new StringBuffer();
                String selectQuery1 = "SELECT * from RefrenseTable where Site_Location_Id='" + myDb.Site_Location_Id(User_Id) + "' AND UpdatedStatus='no'";
                Cursor res = db.rawQuery(selectQuery1, null);
                if (res.getCount() != 0) {
                    if (res.moveToFirst()) {
                        do {
                            stringBuffer.append("'" + res.getString(res.getColumnIndex("Updated_Id")) + "'").append(",");
                            stringBufferTableName.append(res.getString(res.getColumnIndex("Table_Name"))).append(",");
                            String Table_Name = res.getString(res.getColumnIndex("Table_Name"));
                            String Updated_Id = res.getString(res.getColumnIndex("Updated_Id"));
                            if (Table_Name.equals("pun_form_structure")) {
                                db.delete("Form_Structure", "Form_Id = '" + Updated_Id + "'", null);
                                if(LOG)Log.d(TAG, "Form_Structure " + Updated_Id + " Deleted..");
                            }
                        } while (res.moveToNext());
                    }
                }
                res.close();

                HttpHandler handler = new HttpHandler();
                jsonStrSite1 = handler.downloadAssetChanges(myDb.Site_Location_Id(User_Id), stringBuffer.toString(), stringBufferTableName.toString());
                jsonObjSite = new JSONObject(jsonStrSite1);

                try {
                    JSONArray assetDetailsJson = jsonObjSite.getJSONArray("AssetDetails");
                    if(LOG)Log.d(TAG, "AssetDetailsJson" + assetDetailsJson);
                    String sql = "insert into Asset_Details (Asset_Id,Site_Location_Id ,Asset_Code ,Asset_Name ,Asset_Location ,Asset_Status_Id ,Status ,Manual_Time , Asset_Update_Time ,UpdatedStatus)values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
                    db.beginTransaction();
                    SQLiteStatement stmt = db.compileStatement(sql);
                    for (int i = 0; i < assetDetailsJson.length(); i++) {
                        JSONObject c = assetDetailsJson.getJSONObject(i);
                        String Asset_Id = c.getString("Asset_Id");
                        String Site_Location_Id = c.getString("Site_Location_Id");
                        String Asset_Code = c.getString("Asset_Code");
                        String Asset_Name = c.getString("Asset_Name");
                        String Asset_Location = c.getString("Asset_Location");
                        String Asset_Status_Id = c.getString("Asset_Status_Id");
                        String RecordStatus = c.getString("Record_Status");
                        String Status = c.getString("Status");
                        try {
                            String selectQuery = "SELECT  * FROM Asset_Details where Asset_Id = '" + Asset_Id + "'";
                            Cursor cursor = db.rawQuery(selectQuery, null);
                            if (RecordStatus.equals("D")) {
                                ContentValues values = new ContentValues();
                                values.put("UpdatedStatus", "yes");
                                db.delete("Asset_Details", "Asset_Id = '" + Asset_Id + "'", null);
                                db.update("RefrenseTable", values, "Updated_Id ='" + Asset_Id + "'", null);
                            } else {
                                if (cursor.getCount() == 0) {
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
                                    ContentValues values = new ContentValues();
                                    values.put("UpdatedStatus", "yes");

                                    ContentValues taskDetails = new ContentValues();
                                    taskDetails.put("Asset_Code", Asset_Code);
                                    taskDetails.put("Asset_Name", Asset_Name);
                                    taskDetails.put("Asset_Location", Asset_Location);
                                    taskDetails.put("Asset_Status", Status);

                                    db.update("RefrenseTable", values, "Updated_Id ='" + Asset_Id + "'", null);
                                    db.update("Task_Details", taskDetails, "Asset_Id ='" + Asset_Id + "'", null);
                                    if(LOG)Log.d(TAG, "Inserted");
                                } else {
                                    ContentValues values = new ContentValues();
                                    values.put("Asset_Id", Asset_Id);
                                    values.put("Site_Location_Id", Site_Location_Id);
                                    values.put("Asset_Code", Asset_Code);
                                    values.put("Asset_Name", Asset_Name);
                                    values.put("Asset_Location", Asset_Location);
                                    values.put("Asset_Status_Id", Asset_Status_Id);
                                    values.put("Status", Status);
                                    db.update("Asset_Details", values, "Asset_Id ='" + Asset_Id + "'", null);
                                    ContentValues values1 = new ContentValues();
                                    values1.put("UpdatedStatus", "yes");
                                    db.update("RefrenseTable", values1, "Updated_Id ='" + Asset_Id + "'", null);
                                    if(LOG)Log.d(TAG, "Updated");
                                }
                            }
                            cursor.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    String sqlquery = "DELETE FROM Asset_Details WHERE Id NOT IN (SELECT MIN(Id) FROM Asset_Details GROUP BY Asset_Id);";
                    db.rawQuery(sqlquery, null);
                    db.execSQL(sqlquery);
                    db.setTransactionSuccessful();
                    db.endTransaction();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                try {
                    JSONArray formStructureJson = jsonObjSite.getJSONArray("Form_Structure");
                    if(LOG)Log.d(TAG, "formStructureJson" + formStructureJson);
                    if (formStructureJson != null) {
                        db = myDb.getWritableDatabase();
                        String sql1 = "insert into Form_Structure (Field_Id ,Form_Id,Field_Label ,Field_Type ,Field_Options ,Mandatory ,FixedValue,sid ,sections,Record_Status,Display_Order ,FormType)values (?, ?, ?,?,?, ?, ?,?,?, ?, ?, ?);";
                        db.beginTransaction();
                        SQLiteStatement stmt1 = db.compileStatement(sql1);

                        for (int k = 0; k < formStructureJson.length(); k++) {
                            JSONObject c2 = formStructureJson.getJSONObject(k);

                            String Field_Id = c2.getString("Field_Id");
                            String Form_Id = c2.getString("Form_Id");
                            String Field_Label = c2.getString("Field_Label");
                            String Field_Type = c2.getString("Field_Type");
                            String Field_Options = c2.getString("Field_Options");
                            String Mandatory = c2.getString("Mandatory");
                            String FixedValue = c2.getString("FixedValue");
                            String sid = c2.getString("sid");
                            String sections = c2.getString("sections");
                            String Display_Order = c2.getString("Disp_Order");
                            String FormType = c2.getString("FormType");
                            String Record_Status = c2.getString("Record_Status");

                            String selectQuery = "SELECT  * FROM Form_Structure WHERE  Form_Id = '" + Form_Id + "' AND Field_Id='" + Field_Id + "'";
                            Cursor cursor = db.rawQuery(selectQuery, null);
                            stmt1.bindString(1, Field_Id);
                            stmt1.bindString(2, Form_Id);
                            stmt1.bindString(3, Field_Label);
                            stmt1.bindString(4, Field_Type);
                            stmt1.bindString(5, Field_Options);
                            stmt1.bindString(6, Mandatory);
                            stmt1.bindString(7, FixedValue);
                            stmt1.bindString(8, sid);
                            stmt1.bindString(9, sections);
                            stmt1.bindString(10, Record_Status);
                            stmt1.bindString(11, Display_Order);
                            stmt1.bindString(12, FormType);
                            long entryID = stmt1.executeInsert();
                            stmt1.clearBindings();
                            cursor.close();

                            ContentValues values1 = new ContentValues();
                            values1.put("UpdatedStatus", "yes");
                            db.update("RefrenseTable", values1, "Updated_Id ='" + Form_Id + "'", null);
                            if(LOG)Log.d(TAG, "Updated RefrenseTable formStructure");
                        }


                        String sqlquery1 = "DELETE FROM Form_Structure WHERE Id NOT IN (SELECT MIN(Id) FROM Form_Structure GROUP BY Form_Id ,Field_Id);";
                        db.rawQuery(sqlquery1, null);
                        db.execSQL(sqlquery1);
                        db.setTransactionSuccessful();
                        db.endTransaction();
                        db.close();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }



                try {
                    JSONArray Activity_FrequencyJson = jsonObjSite.getJSONArray("Activity_Frequency");
                    if(LOG)Log.d(TAG, "Activity_FrequencyJson" + Activity_FrequencyJson);
                    if (Activity_FrequencyJson != null) {
                        db = myDb.getWritableDatabase();
                        db.beginTransaction();

                        for (int k = 0; k < Activity_FrequencyJson.length(); k++) {
                            JSONObject c2 = Activity_FrequencyJson.getJSONObject(k);


                            String FrequencyRecordStatus = c2.getString("FrequencyRecordStatus");
                            String Frequency_Auto_Id = c2.getString("Frequency_Auto_Id");

                            ContentValues values = new ContentValues();
                            values.put("RecordStatus", FrequencyRecordStatus);
                            db.update("Activity_Frequency", values, "Frequency_Auto_Id ='" + Frequency_Auto_Id + "'", null);
                            db.update("Task_Details", values, "Activity_Frequency_Id ='" + Frequency_Auto_Id + "' AND Task_Status='Pending'", null);

                            ContentValues values1 = new ContentValues();
                            values1.put("UpdatedStatus", "yes");
                            db.update("RefrenseTable", values1, "Updated_Id ='" + Frequency_Auto_Id + "'", null);
                            if(LOG)Log.d(TAG, "Updated RefrenseTable formStructure");
                        }


                        db.setTransactionSuccessful();
                        db.endTransaction();
                        db.close();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                try {
                    JSONArray AssetActivityLinkingJson = jsonObjSite.getJSONArray("AssetActivityLinking");
                    if(LOG)Log.d(TAG, "Activity_FrequencyJson" + AssetActivityLinkingJson);
                    if (AssetActivityLinkingJson != null) {
                        db = myDb.getWritableDatabase();
                        db.beginTransaction();

                        for (int k = 0; k < AssetActivityLinkingJson.length(); k++) {
                            JSONObject c2 = AssetActivityLinkingJson.getJSONObject(k);


                            String AssetActivityLinkingRecordStatus = c2.getString("AssetActivityLinkingRecordStatus");
                            String AssetActivityLinkingAutoId = c2.getString("AssetActivityLinkingAutoId");

                            ContentValues values = new ContentValues();
                            values.put("RecordStatus", AssetActivityLinkingRecordStatus);
                            db.update("Asset_Activity_Linking", values, "Auto_Id ='" + AssetActivityLinkingAutoId + "'", null);
                            db.update("Activity_Frequency", values, "Asset_Activity_Linking_Id ='" + AssetActivityLinkingAutoId + "'", null);
                            db.update("Task_Details", values, "Asset_Activity_Linking_Auto_Id ='" + AssetActivityLinkingAutoId + "' AND Task_Status='Pending'", null);

                            ContentValues values1 = new ContentValues();
                            values1.put("UpdatedStatus", "yes");
                            db.update("RefrenseTable", values1, "Updated_Id ='" + AssetActivityLinkingAutoId + "'", null);
                            if(LOG)Log.d(TAG, "Updated RefrenseTable formStructure");
                        }



                        db.setTransactionSuccessful();
                        db.endTransaction();
                        db.close();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }


                try {
                    JSONArray ActivityMasterJson = jsonObjSite.getJSONArray("ActivityMaster");
                    if(LOG)Log.d(TAG, "Activity_FrequencyJson" + ActivityMasterJson);
                    if (ActivityMasterJson != null) {
                        db = myDb.getWritableDatabase();
                        db.beginTransaction();

                        for (int k = 0; k < ActivityMasterJson.length(); k++) {
                            JSONObject c2 = ActivityMasterJson.getJSONObject(k);


                            String ActivityMasterRecordStatus = c2.getString("ActivityMasterRecordStatus");
                            String ActivityMasterAutoId = c2.getString("ActivityMasterAutoId");
                            String ActivityMasterActivityName = c2.getString("ActivityMasterActivityName");
                            if(ActivityMasterRecordStatus.equals("D")){


                            ContentValues values = new ContentValues();
                            values.put("RecordStatus", ActivityMasterRecordStatus);
                            db.update("Activity_Master", values, "Auto_Id ='" + ActivityMasterAutoId + "'", null);
                                db.update("Asset_Activity_Linking", values, "Activity_Id ='" + ActivityMasterAutoId + "'", null);
                                String activityFrequencyUpdate = "SELECT Auto_Id FROM Asset_Activity_Linking WHERE Activity_Id ='" + ActivityMasterAutoId + "'";
                                Cursor activityMaster = db.rawQuery(activityFrequencyUpdate,null);
                                if(activityMaster.getCount()!=0){
                                    if (activityMaster.moveToFirst()) {
                                        do {
                                            if (LOG)Log.d("Tesingasdadasd",activityMaster.getString(0) + " "+ activityFrequencyUpdate);
                                            //activityMaster = res.getString(0);
                                            db.update("Activity_Frequency", values, "Asset_Activity_Linking_Id ='" + activityMaster.getString(0) + "'", null);
                                            db.update("Asset_Activity_AssignedTo", values, "Asset_Activity_Linking_Id ='" + activityMaster.getString(0) + "'", null);

                                        } while (activityMaster.moveToNext());
                                    }

                                }

                            db.update("Task_Details", values, "Activity_Master_Auto_Id ='" + ActivityMasterAutoId + "' AND Task_Status='Pending'", null);

                            ContentValues values1 = new ContentValues();
                            values1.put("UpdatedStatus", "yes");
                            db.update("RefrenseTable", values1, "Updated_Id ='" + ActivityMasterAutoId + "'", null);
                            if(LOG)Log.d(TAG, "Updated RefrenseTable formStructure");
                            }else {
                                ContentValues values2 = new ContentValues();
                                values2.put("RecordStatus", ActivityMasterRecordStatus);

                                ContentValues values = new ContentValues();
                                values.put("RecordStatus", ActivityMasterRecordStatus);
                                values.put("Activity_Name", ActivityMasterActivityName);
                                db.update("Asset_Activity_Linking", values2, "Activity_Id ='" + ActivityMasterAutoId + "'", null);
                                db.update("Activity_Master", values, "Auto_Id ='" + ActivityMasterAutoId + "'", null);
                                db.update("Task_Details", values, "Activity_Master_Auto_Id ='" + ActivityMasterAutoId + "' AND Task_Status='Pending'", null);

                                ContentValues values1 = new ContentValues();
                                values1.put("UpdatedStatus", "yes");
                                db.update("RefrenseTable", values1, "Updated_Id ='" + ActivityMasterAutoId + "'", null);
                            }
                        }


                        db.setTransactionSuccessful();
                        db.endTransaction();
                        db.close();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

               /* try {
                    JSONArray ActivityMasterJson = jsonObjSite.getJSONArray("ActivityMaster");
                    if(LOG)Log.d(TAG, "Activity_FrequencyJson" + ActivityMasterJson);
                    if (ActivityMasterJson != null) {
                        db = myDb.getWritableDatabase();
                        db.beginTransaction();

                        for (int k = 0; k < ActivityMasterJson.length(); k++) {
                            JSONObject c2 = ActivityMasterJson.getJSONObject(k);


                            String ActivityMasterRecordStatus = c2.getString("ActivityMasterRecordStatus");
                            String ActivityMasterAutoId = c2.getString("ActivityMasterAutoId");
                            String ActivityMasterActivityName = c2.getString("ActivityMasterActivityName");
                            if(ActivityMasterRecordStatus.equals("D")){


                                ContentValues values = new ContentValues();
                                values.put("RecordStatus", ActivityMasterRecordStatus);
                                db.update("Asset_Activity_Linking", values, "Activity_Id ='" + ActivityMasterAutoId + "'", null);
                                String activityFrequencyUpdate = "SELECT Auto_Id FROM Asset_Activity_Linking WHERE Activity_Id ='" + ActivityMasterAutoId + "'";
                                Cursor activityMaster = db.rawQuery(activityFrequencyUpdate,null);
                                if(activityMaster.getCount()!=0){
                                    if (activityMaster.moveToFirst()) {
                                        do {
                                            //activityMaster = res.getString(0);
                                            db.update("Activity_Frequency", values, "Asset_Activity_Linking_Id ='" + res.getString(0) + "'", null);

                                        } while (res.moveToNext());
                                    }

                                }

                                db.update("Task_Details", values, "Activity_Master_Auto_Id ='" + ActivityMasterAutoId + "' AND Task_Status='Pending'", null);

                                ContentValues values1 = new ContentValues();
                                values1.put("UpdatedStatus", "yes");
                                db.update("RefrenseTable", values1, "Updated_Id ='" + ActivityMasterAutoId + "'", null);
                                if(LOG)Log.d(TAG, "Updated RefrenseTable formStructure");
                            }else {
                                ContentValues values = new ContentValues();
                                values.put("RecordStatus", ActivityMasterRecordStatus);
                                values.put("Activity_Name", ActivityMasterActivityName);

                                db.update("Asset_Activity_Linking", values, "Activity_Id ='" + ActivityMasterAutoId + "'", null);
                                db.update("Task_Details", values, "Activity_Master_Auto_Id ='" + ActivityMasterAutoId + "' AND Task_Status='Pending'", null);

                                ContentValues values1 = new ContentValues();
                                values1.put("UpdatedStatus", "yes");
                                db.update("RefrenseTable", values1, "Updated_Id ='" + ActivityMasterAutoId + "'", null);
                            }
                        }



                        db.setTransactionSuccessful();
                        db.endTransaction();
                        db.close();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }*/
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            try {
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                editorTaskInsert = settings.edit();
                editorTaskInsert.putString("day",null);
                editorTaskInsert.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
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

            switch (values[2]){
                case 0:
                    pDialog.setMessage("Downloading Uploaded Task. Please Wait..");
                    pDialog.setProgressPercentFormat(null);
                    break;

                 case 1:
                     pDialog.setMessage("Downloading Readings. Please Wait..");
                     pDialog.setProgressPercentFormat(null);
                     break;

                case 2:
                    pDialog.setMessage("Downloading Pervious Data. Please Wait..");
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
                    pDialog.setMessage("Downloading Forms. Please Wait..");
                    pDialog.setProgressPercentFormat(null);
                    break;
                case 6:
                    pDialog.setMessage("Downloading  Parameters. Please Wait..");
                    pDialog.setProgressPercentFormat(null);
                    break;
                case 7:
                    pDialog.setMessage("Downloading  FeedbackScore. Please Wait..");
                    pDialog.setProgressPercentFormat(null);
                    break;
                case 8:
                    pDialog.setMessage("Downloading  Score. Please Wait..");
                    pDialog.setProgressPercentFormat(null);
                    break;
                case 9:
                    pDialog.setMessage("Downloading  CoversionFactors. Please Wait..");
                    pDialog.setProgressPercentFormat(null);
                    break;
                case 10:
                    pDialog.setMessage("Downloading  PPM Task. Please Wait..");
                    pDialog.setProgressPercentFormat(null);
                    break;

                case 11:
                    pDialog.setMessage("Getting Data From Server. Please Wait..");


                    break;

                default:
                     break;

            }

            pDialog.setMax(values[1]);
            pDialog.setProgress(values[0]);
           // pDialog.setProgressNumberFormat(null);



        }
        @Override
        protected String doInBackground(String... URL) {
            HttpHandler handler = new HttpHandler();
            publishProgress(0,100,11);
            String jsonTaskDone = handler.getTaskDetailsServer(myDb.UserGroupId(User_Id), new applicationClass().yymmdd(), myDb.Site_Location_Id(User_Id));
            publishProgress(33,100,11);
            String jsonStrTask = handler.taskDataCall(myDb.UserGroupId(User_Id), myDb.Site_Location_Id(User_Id), myDb.SiteURL(User_Id));
            publishProgress(66,100,11);
            String PPMTaskjson = handler.getPPmTask(myDb.UserGroupId(User_Id), myDb.Site_Location_Id(User_Id));
            publishProgress(100,100,11);
            if (jsonTaskDone != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonTaskDone);
                    try {

                        JSONArray task = jsonObj.getJSONArray("TaskDetailsDone");
                        if(LOG)Log.d("DATasValue",task+"");

                        if (!task.toString().equals("[]")) {
                            pDialog.setProgress(0);

                            db = myDb.getWritableDatabase();
                            String sql = "insert into Task_Details_Server(Task_Id ,Site_Location_Id,Activity_Frequency_Id ,Task_Scheduled_Date ,Task_Status ,Assigned_To_User_Id, Assigned_To_User_Group_Id,Task_Start_At ,Remarks,UpdatedStatus)values(?, ?, ?, ?, ?, ?,?, ?, ?, ?);";
                            db.beginTransaction();
                            SQLiteStatement stmt = db.compileStatement(sql);
                            for (int i = 0; i < task.length(); i++) {
                                publishProgress(i+1,task.length(),0);
                               // pDialog.setProgress(i+1);
                                JSONObject c = task.getJSONObject(i);
                                String Auto_Id = c.getString("Auto_Id");
                                String Activity_Frequency_Id = c.getString("Activity_Frequency_Id");
                                String Task_Scheduled_Date = c.getString("Task_Scheduled_Date");
                                String Task_Status = c.getString("Task_Status");
                                String Assigned_To_User_Id = c.getString("Assigned_To_User_Id");
                                String Assigned_To_User_Group_Id = c.getString("Assigned_To_User_Group_Id");
                                String Task_Start_At = c.getString("Task_Start_At");
                                String Remarks = c.getString("Remarks");


                                stmt.bindString(1, Auto_Id);
                                stmt.bindString(2, myDb.Site_Location_Id(User_Id));
                                stmt.bindString(3, Activity_Frequency_Id);
                                stmt.bindString(4, Task_Scheduled_Date);
                                stmt.bindString(5, Task_Status);
                                stmt.bindString(6, Assigned_To_User_Id);
                                stmt.bindString(7, Assigned_To_User_Group_Id);
                                stmt.bindString(8, Task_Start_At);
                                stmt.bindString(9, Remarks);
                                stmt.bindString(10, "no");
                                long entryID = stmt.executeInsert();
                                stmt.clearBindings();
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
                            pDialog.setProgress(0);
                            db = myDb.getWritableDatabase();
                            String sql = "insert into Meter_Reading(Task_Id,Site_Location_Id,Asset_Id ,Form_Structure_Id ,Reading ,UOM ,Reset ,Activity_Frequency_Id ,Task_Start_At ,UpdatedStatus )values(?, ?,?,?, ?, ?, ?, ?, ?, ?);";
                            db.beginTransaction();
                            SQLiteStatement stmt = db.compileStatement(sql);
                            for (int i = 0; i < meterReading.length(); i++) {
                                JSONObject c = meterReading.getJSONObject(i);
                                //pDialog.setProgress(i+1);
                                publishProgress(i+1,meterReading.length(),1);

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
                            pDialog.setProgress(0);
                            db = myDb.getWritableDatabase();
                            String sql = "insert into Data_Posting(Task_Id,Site_Location_Id,Form_Id ,Form_Structure_Id ,Parameter_Id ,Value ,UOM,UpdatedStatus )values(?,?,?,?, ?, ?, ?, ?);";
                            db.beginTransaction();
                            SQLiteStatement stmt = db.compileStatement(sql);

                            for (int i = 0; i < dataPosting.length(); i++) {
                                JSONObject c1 = dataPosting.getJSONObject(i);
                                //pDialog.setProgress(i+1);
                                publishProgress(i+1,dataPosting.length(),2);

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
                            pDialog.setProgress(0);
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
                                publishProgress(i+1,task.length(),3);
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
                        if (assetDetailsJson != null) {
                            pDialog.setProgress(0);
                            db = myDb.getWritableDatabase();
                            String sql = "insert into Asset_Details (Asset_Id ,Site_Location_Id ,Asset_Code ,Asset_Name ,Asset_Location ,Asset_Status_Id ,Asset_Type,Status ,Manual_Time , Asset_Update_Time ,UpdatedStatus)values (?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
                            db.beginTransaction();
                            SQLiteStatement stmt = db.compileStatement(sql);
                            for (int i = 0; i < assetDetailsJson.length(); i++) {
                                publishProgress(i+1,assetDetailsJson.length(),4);
                                pDialog.setProgress(i+1);
                                JSONObject c = assetDetailsJson.getJSONObject(i);
                                String Asset_Id = c.getString("Asset_Id");
                                String Site_Location_Id = c.getString("Site_Location_Id");
                                String Asset_Code = c.getString("Asset_Code");
                                String Asset_Name = c.getString("Asset_Name");
                                String Asset_Location = c.getString("Asset_Location");
                                String Asset_Status_Id = c.getString("Asset_Status_Id");
                                String Asset_Type = c.getString("Asset_Type");
                                String Status = c.getString("Status");

                                String selectQuery = "SELECT  * FROM Asset_Details where Asset_Id = '" + Asset_Id + "'";
                                Cursor cursor = db.rawQuery(selectQuery, null);
                                stmt.bindString(1, Asset_Id);
                                stmt.bindString(2, Site_Location_Id);
                                stmt.bindString(3, Asset_Code);
                                stmt.bindString(4, Asset_Name);
                                stmt.bindString(5, Asset_Location);
                                stmt.bindString(6, Asset_Status_Id);
                                stmt.bindString(7, Asset_Type);
                                stmt.bindString(8, Status);
                                stmt.bindString(9, "");
                                stmt.bindString(10, "");
                                stmt.bindString(11, "");
                                long entryID = stmt.executeInsert();
                                stmt.clearBindings();
                                cursor.close();
                            }
                            jumptime += 50;
                            pDialog.setProgress(jumptime);
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
                        JSONArray fromStructure = jsonObj.getJSONArray("Form_Structure");
                        String selectQuery1 = "SELECT  * FROM Form_Structure";
                        db = myDb.getWritableDatabase();
                        Cursor cursor1 = db.rawQuery(selectQuery1, null);
                        if (cursor1.getCount() == 0) {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("Field_Id", "");
                            db.insert("Form_Structure", null, contentValues);
                        }
                        cursor1.close();
                        db.close();

                        if (fromStructure != null) {
                            pDialog.setProgress(0);

                            db = myDb.getWritableDatabase();
                            String sql = "insert into Form_Structure (Field_Id,Site_Location_Id,Form_Id,Field_Label ,Field_Type ,Field_Options ,Mandatory ,FixedValue,sid ,sections ,Display_Order ,SafeRange,Calculation,Record_Status)values (?, ?,?,?, ?, ?, ?,?, ?, ?, ?, ?, ?,?);";
                            db.beginTransaction();
                            SQLiteStatement stmt = db.compileStatement(sql);

                            for (int k = 0; k < fromStructure.length(); k++) {
                                publishProgress(k+1,fromStructure.length(),5);
                                pDialog.setProgress(k+1);
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
                                String Record_Status = c2.getString("Record_Status");



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
                                stmt.bindString(14, Record_Status);
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
                            pDialog.setProgress(0);
                            db = myDb.getWritableDatabase();
                            String sql = "insert into Parameter (Site_Location_Id ,Activity_Frequency_Id,Form_Id,Form_Structure_Id,Field_Limit_From ,Field_Limit_To ,Threshold_From ,Threshold_To ,Validation_Type ,Critical ,Field_Option_Id )values(?, ?, ?, ?, ?,?,?, ?, ?, ?, ?)";
                            db.beginTransaction();
                            SQLiteStatement stmt = db.compileStatement(sql);
                            for (int j = 0; j < parameter.length(); j++) {
                                //pDialog.setProgress(j+1);
                                publishProgress(j+1,parameter.length(),6);
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
                            jumptime += 50;
                            pDialog.setProgress(jumptime);
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
                            pDialog.setProgress(0);
                            db = myDb.getWritableDatabase();
                            String sql = "insert into feedback_score (Feedbaack_Auto_Id,Score,FeedBackName)values(?,?,?);";
                            db.beginTransaction();
                            SQLiteStatement stmt = db.compileStatement(sql);
                            for (int i = 0; i < FeedBackScore.length(); i++) {
                                publishProgress(i+1,FeedBackScore.length(),7);
                                JSONObject c = FeedBackScore.getJSONObject(i);
                                String Feedbaack_Auto_Id = c.getString("Auto_Id");
                                String Score = c.getString("Score");
                                String FeedBackName = c.getString("FeedBackName");

                                stmt.bindString(1, Feedbaack_Auto_Id);
                                stmt.bindString(2, Score);
                                stmt.bindString(3, FeedBackName);
                                long entryID = stmt.executeInsert();
                                Log.d(TAG, "FeedBackScore" + "FeedBackScore Downloading...");
                                stmt.clearBindings();
                            }
                            jumptime += 20;
                            pDialog.setProgress(jumptime);
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
                            pDialog.setProgress(0);
                            db = myDb.getWritableDatabase();
                            String sql = "insert into pun_score (Score_Auto_Id,Form_Structure_Id,Option_value,Option_Id,Score)values(?,?,?,?,?);";
                            db.beginTransaction();
                            SQLiteStatement stmt = db.compileStatement(sql);
                            for (int i = 0; i < Score.length(); i++) {
                                publishProgress(i+1,Score.length(),8);
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
                                Log.d(TAG, "Score" + "Score Downloading..."+entryID);
                                stmt.clearBindings();
                            }
                            jumptime += 20;
                            pDialog.setProgress(jumptime);
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
                            pDialog.setProgress(0);
                            db = myDb.getWritableDatabase();
                            String sql = "insert into Measurement_Conversion (Conversion_Auto_Id, Source_UOM, Multiplication_Factor, Add_Factor, Subtraction_Factor, Division_Factor, Target_UOM)values(?,?,?,?,?,?,?);";
                            db.beginTransaction();
                            SQLiteStatement stmt = db.compileStatement(sql);
                            for (int i = 0; i < Conversion.length(); i++) {
                                publishProgress(i+1,Conversion.length(),9);
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
                                Log.d(TAG, "Conversion" + "Conversion Values Downloading..."+entryID);
                                stmt.clearBindings();
                            }
                            jumptime += 20;
                            pDialog.setProgress(jumptime);
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
                            pDialog.setProgress(0);
                            db = myDb.getWritableDatabase();
                            String sql = "insert into Asset_Status (Asset_Status_Id,Status,Task_State)values(?,?,?);";
                            db.beginTransaction();
                            SQLiteStatement stmt = db.compileStatement(sql);
                            for (int i = 0; i < AssetStatus.length(); i++) {
                                JSONObject c = AssetStatus.getJSONObject(i);
                                String Asset_Status_Id = c.getString("Asset_Status_Id");
                                String Status = c.getString("Status");
                                String Task_State = c.getString("Task_State");
                                stmt.bindString(1, Asset_Status_Id);
                                stmt.bindString(2, Status);
                                stmt.bindString(3,Task_State);
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

                } catch (Exception e) {
                    sync = true;
                    e.printStackTrace();

                }

            }

            if(PPMTaskjson != null){
                try {

                    try {

                        JSONObject jsonObj = new JSONObject(PPMTaskjson);

                        JSONArray task = jsonObj.getJSONArray("PPMTaskFeqeuncy");
                        if(LOG)Log.d("DATasValue123",task+"");
                        if (task != null) {
                            pDialog.setProgress(0);
                            db = myDb.getWritableDatabase();
                            String AssetActivityLinkingsql = "insert into Asset_Activity_Linking (Site_Location_Id ,Auto_Id ,Asset_Id ,Activity_Id,RecordStatus ,UpdatedStatus )values(?,?,?,?,?,?);";
                            String ActivityMastersql = "insert into Activity_Master (Site_Location_Id ,Auto_Id ,Form_Id ,Activity_Name ,RecordStatus ,UpdatedStatus )values(?,?,?,?,?,?);";
                            String AssetActivityAssignedTosql = "insert into Asset_Activity_AssignedTo (Site_Location_Id ,Auto_Id ,Assigned_To_User_Id ,Assigned_To_User_Group_Id ,Asset_Activity_Linking_Id,RecordStatus ,UpdatedStatus )values(?,?,?,?,?,?,?);";
                            String PpmTasksql = "insert into PPM_Task (Auto_Id, Site_Location_Id, Activity_Frequency, Task_Date,Task_End_Date, Task_Status,Task_Done_At , Asset_Activity_Linking_Id,  Assigned_To_User_Id, Assigned_To_User_Group_Id, TimeStartson, Activity_Duration, Grace_Duration_Before, Grace_Duration_After, Record_Status, UpdatedStatus)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
                            db.beginTransaction();
                            SQLiteStatement AssetActivityLinkingstmt = db.compileStatement(AssetActivityLinkingsql);
                            SQLiteStatement ActivityMasterstmt = db.compileStatement(ActivityMastersql);
                            SQLiteStatement AssetActivityAssignedTostmt = db.compileStatement(AssetActivityAssignedTosql);
                            SQLiteStatement PpmTaskstmt = db.compileStatement(PpmTasksql);

                            for (int i = 0; i < task.length(); i++) {
                                publishProgress(i+1,task.length(),10);
                                pDialog.setProgress(i+1);
                                JSONObject c = task.getJSONObject(i);
                                Log.d("hbkjcnv",((i+1)*100)/task.length()+"");
                                int setValue = ((i+1)*100)/task.length();
                                //pDialog.setProgress(setValue);
                                pDialog.setProgress(setValue);
                                ////////////////////////////ppm task insert in db//////////////////////////

                                String  ppm_auto_id = c.getString("Auto_Id");
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
                                String  grace_duration_before = c.getString("Grace_Duration_Before");
                                String  grace_duration_after = c.getString("Grace_Duration_After");
                                String  ppm_Record_Status = c.getString("Record_Status");

                                PpmTaskstmt.bindString(1,ppm_auto_id);
                                PpmTaskstmt.bindString(2,myDb.Site_Location_Id(User_Id));
                                PpmTaskstmt.bindString(3,activity_frequency);
                                PpmTaskstmt.bindString(4,task_date);
                                PpmTaskstmt.bindString(5,Task_End_Date);
                                PpmTaskstmt.bindString(6,task_status);
                                PpmTaskstmt.bindString(7,Task_Done_At);
                                PpmTaskstmt.bindString(8,asset_activity_linking_id);
                                PpmTaskstmt.bindString(9,Assigned_To_User_Id);
                                PpmTaskstmt.bindString(10,Assigned_To_User_Group_Id);
                                PpmTaskstmt.bindString(11,timestartson);
                                PpmTaskstmt.bindString(12,activity_duration);
                                PpmTaskstmt.bindString(13,grace_duration_before);
                                PpmTaskstmt.bindString(14,grace_duration_after);
                                PpmTaskstmt.bindString(15,ppm_Record_Status);
                                if(task_status.equals("Completed")||task_status.equals("Missed")){
                                    PpmTaskstmt.bindString(16,"yes");
                                }else {
                                    PpmTaskstmt.bindString(16,"no");
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
                    pDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Data is Synchronised",Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }
    private void getBatteryPercentage() {
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
    }
    public class myPhoneStateListener extends PhoneStateListener {
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            if (signalStrength.isGsm()) {
                if (signalStrength.getGsmSignalStrength() != 99)
                    signalStrengthValue = signalStrength.getGsmSignalStrength() * 2 - 113;
                else
                    signalStrengthValue = signalStrength.getGsmSignalStrength();
            } else {
                signalStrengthValue = signalStrength.getCdmaDbm();
            }


        }
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
        res1 = String.valueOf(res);
        return res;
    }
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Snackbar snackbar = Snackbar
                .make(linearLayout, "Please double click to exit", Snackbar.LENGTH_LONG);
        snackbar.show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home_page, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(R.id.networkCheck== item.getItemId())
        {
            if(haveNetworkConnection()=='W')
            {
                wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                int linkSpeed = wifiManager.getConnectionInfo().getRssi();
                if(linkSpeed<-70)
                {
                    Snackbar snackbar = Snackbar
                            .make(linearLayout, "Weak Signal           Signal Strength : " + linkSpeed, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                else if(linkSpeed>-50)
                {
                    Snackbar snackbar = Snackbar
                            .make(linearLayout, "Excellent Connection          Signal Strength : " + linkSpeed, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                else {

                    Snackbar snackbar = Snackbar
                            .make(linearLayout, "Good Connection         Signal Strength : " + linkSpeed, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
            else
            {
                mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                mTelephonyManager.listen(mPhoneStatelistener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
                Snackbar snackbar = Snackbar
                        .make(linearLayout, "Connected With Mobile Data " + signalStrengthValue, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }

        if (R.id.email == item.getItemId()) {
            exportDatabse(getApplicationContext());
            //new getApk().execute();

        }
        if (R.id.setting == item.getItemId()) {
            Intent intent = new Intent(getApplicationContext(), Setting_pref.class);
            startActivity(intent);
        }

        if (R.id.reconfig == item.getItemId()) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomePage.this);
            alertDialog.setTitle("Confirm ReConfig...");

            alertDialog.setMessage("Are you sure you want to ReConfig?");
            alertDialog.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            checkInternetforReconfig = true;
                            new CheckingInternetConnectivity().execute();
                        }
                    });
            // Setting Negative "NO" Button
            alertDialog.setNegativeButton("NO",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            alertDialog.show();
        }
        if (R.id.logout == item.getItemId()) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomePage.this);
            alertDialog.setTitle("Confirm Logout...");

            alertDialog.setMessage("Are you sure you want to Logout?");
            alertDialog.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            editor1 = settings.edit();
                            editor1.clear();
                            editor1.commit();
                            editorTaskInsert = settings.edit();
                            editorTaskInsert.putString("day",null);
                            editorTaskInsert.commit();
                            Intent intent = new Intent(HomePage.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();

                        }
                    });
            // Setting Negative "NO" Button
            alertDialog.setNegativeButton("NO",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            alertDialog.show();

        }
        return super.onOptionsItemSelected(item);
    }
    public void deleteTask() {
        try {
            site_id =myDb.Site_Location_Id(User_Id);
            db = myDb.getWritableDatabase();
            long resultset1 = db.delete("Task_Details","Site_Location_Id='"+site_id+"'", null);
            long resultset2 = db.delete("Data_Posting","Site_Location_Id='"+site_id+"'", null);
            long resultset3 = db.delete("Ticket_Master","Site_Location_Id='"+site_id+"'", null);
            long resultset4 = db.delete("AlertMaster","Site_Location_Id='"+site_id+"'", null);
            long resultset5 = db.delete("Meter_Reading","Site_Location_Id='"+site_id+"'", null);
            long resultset6 = db.delete("Parameter","Site_Location_Id='"+site_id+"'", null);
            long resultset7 = db.delete("Asset_Status",null, null);
            long resultset8 = db.delete("Form_Structure","Site_Location_Id='"+site_id+"'", null);
            //long resultset9 = db.delete("Task_Frequency","Site_Location_Id='"+site_id+"'", null);
            long resultset10 =db.delete("Asset_Details","Site_Location_Id='"+site_id+"'", null);
            long resultset11 = db.delete("Task_Details_Server","Site_Location_Id='"+site_id+"'", null);
            long resultset12 = db.delete("RefrenseTable","Site_Location_Id='"+site_id+"'", null);
            long resultset13 = db.delete("Task_Selfie","Site_Location_Id='"+site_id+"'", null);
            long resultset9 = db.delete("Activity_Frequency","Site_Location_Id='"+site_id+"'", null);
            long resultset14 = db.delete("Asset_Activity_Linking","Site_Location_Id='"+site_id+"'", null);
            long resultset15 = db.delete("Activity_Master","Site_Location_Id='"+site_id+"'", null);
            long resultset16 = db.delete("Asset_Activity_AssignedTo","Site_Location_Id='"+site_id+"'", null);
            db.close();
            if (resultset1 == -1 && resultset2 == -1 && resultset3 == -1 && resultset4 == -1 && resultset5 == -1 && resultset6 == -1 && resultset7 == -1 && resultset8 == -1 && resultset9 == -1 && resultset10 == -1 && resultset11 == -1 && resultset12==-1 && resultset13==-1 && resultset14==-1 && resultset15==-1 && resultset16==-1 ) {
            } else {
                checkInternetforReconfig=false;
                new DataDownload().execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String DeviceDetails() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        return manufacturer + model;
    }
    public String CarrierName() {
        if (haveNetworkConnection() == 'W') {
            wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifi = wifiManager.getConnectionInfo();
            carrierName = wifi.getSSID().replace("\"", "");

        } else {
            TelephonyManager manager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            carrierName = manager.getNetworkOperatorName();
        }
        return carrierName;
    }
    public int NetworkStrength() {
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifi = wifiManager.getConnectionInfo();
        int linkSpeed = wifiManager.getConnectionInfo().getRssi();
        return linkSpeed;
    }
    public void uploadData() {
        countvalue++;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        JSONArray uploadData = new JSONArray();
        JSONObject Ticketdata = new JSONObject();
        JSONObject AssetDetails = new JSONObject();
        JSONObject AssetStatusChange = new JSONObject();
        try {
            uploadData = composeJSONfortaskDetailsNew(myDb.Site_Location_Id(User_Id), User_Id);
            Ticketdata.put("Ticketdata", myDb.composeJSONforTicket(User_Id));
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

        if(LOG)Log.d(TAG, "1" + networkStrength + "  " + haveNetworkConnection() + "  " + carrierName + "   " + signalStrengthValue + "  " + deviceDetails);
        params.put("Sync_Info", myDb.SyncInfo(User_Id, level, networkStrength, haveNetworkConnection(), carrierName, deviceDetails));
        params.put("Sync_Count", countvalue);
        params.put("Site_Location_Id", myDb.Site_Location_Id(User_Id));
        params.put("Task_Details", uploadData);
        Log.d("dfhj",params.toString());

        if (WRITE_JSON_FILE) {
            generateNoteOnSD(getApplicationContext(), "uploadData" + countvalue + ".txt", params.toString());
        }
        if(LOG)Log.d(TAG, "URL002" + myDb.SiteURL(User_Id) +" "+ new applicationClass().urlString() + new applicationClass().insertTask());
        Log.d("Teasdasdasd",URLSTRING + new applicationClass().insertTask());
        client.post(URLSTRING + new applicationClass().insertTask(), params, new TextHttpResponseHandler() {//http://eclockwork.in/inserttask.php
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // TODO Auto-generated method stub

                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                    pDialog.dismiss();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                    pDialog.dismiss();
                } else {
                    pDialog.dismiss();
                    final Snackbar snackbar = Snackbar
                            .make(linearLayout, "Internet Connection Lost.Synchronization failed.", Snackbar.LENGTH_LONG)
                            .setAction("Sync Again.", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    new CheckingInternetConnectivity().execute();
                                }
                            });

                    snackbar.setDuration(20000);
                    snackbar.show();
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

                    try {
                        final JSONArray AssetDetails = jsonObjTask.getJSONArray("AssetDetails");
                        if (AssetDetails != null) {
                                db.execSQL("DELETE FROM Asset_Details WHERE Site_Location_Id='"+site_id+"'");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


                try {
                    String selectQuery = "SELECT  * FROM Task_Details where UpdatedStatus = 'no' and  Assigned_To_User_Id='" + User_Id + "' and Site_Location_Id='" + myDb.Site_Location_Id(User_Id) + "'";
                    Cursor cursor = db.rawQuery(selectQuery, null);
                    int cursorValue = cursor.getCount();

                    if (cursorValue > 0) {
                        pDialog.setMessage("Uploading Task. Please Wait");

                        jumptime += 25;
                        pDialog.setProgress(jumptime);
                        uploadData();

                    } else {
                        editorTaskInsert = settings.edit();
                        editorTaskInsert.putString("day", null);
                        editorTaskInsert.commit();
                        //new DataDownload().execute();
                        uploadDataPPM();
                        //getPPMTask();

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
    public void uploadDataReconfig(){
        countvalue++;
        try {
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            JSONArray uploadData = new JSONArray();
            JSONObject Ticketdata = new JSONObject();
            JSONObject AssetDetails = new JSONObject();
            JSONObject AssetStatusChange = new JSONObject();
            try {
                uploadData = composeJSONfortaskDetailsReconfigNew(myDb.Site_Location_Id(User_Id),User_Id);
                Ticketdata.put("Ticketdata", myDb.composeJSONforTicket(User_Id));
                AssetDetails.put("AssetDetails", myDb.composeJSONforAssets(User_Id));
                AssetStatusChange.put("AssetStatusLog", myDb.composeJSONforAssetsStatusChange(myDb.Site_Location_Id(User_Id),User_Id));
                uploadData.put(Ticketdata);
                uploadData.put(AssetDetails);
                uploadData.put(AssetStatusChange);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            int networkStrength = NetworkStrength();
            String carrierName = CarrierName();
            String deviceDetails = DeviceDetails();

            if(WRITE_JSON_FILE){ generateNoteOnSD(getApplicationContext(), "ReconfigData" + countvalue + ".txt", params.toString()); }
            params.put("Sync_Info", myDb.SyncInfo(User_Id, level, networkStrength, haveNetworkConnection(), carrierName, deviceDetails));
            params.put("Sync_Count",countvalue);
            params.put("Task_Details", uploadData);
            params.put("Site_Location_Id", myDb.Site_Location_Id(User_Id));
            if(LOG)Log.d(TAG, "URL002" + myDb.SiteURL(User_Id) +" "+ new applicationClass().urlString() + new applicationClass().insertTask());
            client.post(URLSTRING +new applicationClass().insertTask(), params, new TextHttpResponseHandler() {//http://eclockwork.in/inserttask.php
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    // TODO Auto-generated method stub

                    if (statusCode == 404) {
                        Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                        pDialog.dismiss();
                    } else if (statusCode == 500) {
                        Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                        pDialog.dismiss();
                    } else {
                        pDialog.dismiss();
                        //Toast.makeText(getApplicationContext(), statusCode + " Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]", Toast.LENGTH_LONG).show();
                        final Snackbar snackbar = Snackbar
                                .make(linearLayout, "Internet Connection Lost.Synchronization failed.", Snackbar.LENGTH_LONG)
                                .setAction("Sync Again.", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        new CheckingInternetConnectivity().execute();
                                    }
                                });

                        snackbar.setDuration(20000);
                        snackbar.show();
                    }
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    db = myDb.getWritableDatabase();
                    if (WRITE_JSON_FILE) {
                        generateNoteOnSD(getApplicationContext(), "responseString" + countvalue + ".txt", responseString.toString());
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
                                    if(LOG) Log.d(TAG, "" + TaskId + "  " + Status);
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

                            final JSONArray AssetDetails = jsonObjTask.getJSONArray("AssetDetails");
                            if (AssetDetails != null) {
                                try {
                                    db.execSQL("DELETE FROM Asset_Details WHERE Site_Location_Id='"+site_id+"'");
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }




                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        String selectQuery = "SELECT  * FROM Task_Details where Site_Location_Id='" + myDb.Site_Location_Id(User_Id) + "' and UpdatedStatus = 'no'";
                        Cursor cursor = db.rawQuery(selectQuery, null);
                        int cursorValue = cursor.getCount();
                        db.close();
                        if (cursorValue > 0) {
                            jumptime += 25;
                            pDialog.setProgress(jumptime);
                            uploadDataReconfig();
                            if(LOG)Log.d(TAG, "Count  " + countvalue + "  " + cursor.getCount());
                        } else {
                            deleteTask();
                            editorTaskInsert = settings.edit();
                            editorTaskInsert.putString("day", null);
                            editorTaskInsert.commit();
                            if(LOG)Log.d(TAG, "Download Statred");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    public void uploadDataPPM() {
        pDialog.setMessage("Checking for PPM Task to upload. Please Wait");

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

        if(LOG)Log.d(TAG, "1" + networkStrength + "  " + haveNetworkConnection() + "  " + carrierName + "   " + signalStrengthValue + "  " + deviceDetails);
        params.put("Sync_Info", myDb.SyncInfo(User_Id, level, networkStrength, haveNetworkConnection(), carrierName, deviceDetails));
        params.put("Sync_Count", countvalue);
        params.put("Site_Location_Id", myDb.Site_Location_Id(User_Id));
        params.put("PPMTaskDetails", uploadData);
        Log.d("dfhj",params.toString());

        if (WRITE_JSON_FILE) {
            generateNoteOnSD(getApplicationContext(), "uploadData" + countvalue + ".txt", params.toString());
        }
        if(LOG)Log.d(TAG, "URL002" + myDb.SiteURL(User_Id) +" "+ new applicationClass().urlString() + new applicationClass().insertTask());
        Log.d("Teasdasdasd",URLSTRING + new applicationClass().insertTask());
        client.post(URLSTRING + new applicationClass().insertPPMTask(), params, new TextHttpResponseHandler() {//http://eclockwork.in/inserttask.php
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // TODO Auto-generated method stub

                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                    pDialog.dismiss();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                    pDialog.dismiss();
                } else {
                    pDialog.dismiss();
                    final Snackbar snackbar = Snackbar
                            .make(linearLayout, "Internet Connection Lost.Synchronization failed.", Snackbar.LENGTH_LONG)
                            .setAction("Sync Again.", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    new CheckingInternetConnectivity().execute();
                                }
                            });

                    snackbar.setDuration(20000);
                    snackbar.show();
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

                    if (cursorValue > 0) {
                        pDialog.setMessage("Uploading PPM Task. Please Wait");

                        jumptime += 25;
                        pDialog.setProgress(jumptime);
                        uploadDataPPM();

                    } else {
                        editorTaskInsert = settings.edit();
                        editorTaskInsert.putString("day", null);
                        editorTaskInsert.commit();
                        new DataDownload().execute();
                        //getPPMTask();

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

    public JSONArray composeJSONfortaskDetailsReconfigNew(String site_id,String UserId){
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
            String selectQuery = "SELECT  * FROM Task_Details where Task_Status <> 'Pending' and  Site_Location_Id='"+myDb.Site_Location_Id(UserId)+"' and UpdatedStatus = 'no' ORDER BY CASE WHEN Task_Status='Completed' THEN 1 WHEN Task_Status='Delayed' THEN 2 WHEN Task_Status='Unplanned' THEN 3 WHEN Task_Status='Cancelled' THEN 4 END,Task_Status ASC LIMIT 25"  ;
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
                    TaskJsonObject.put("Remarks", cursor.getString(cursor.getColumnIndex("Remarks")));
                    TaskJsonObject.put("GeoLoc", loggerData[6]);



                    String selectQuery1 = "SELECT  Meter_Reading.* FROM Meter_Reading LEFT JOIN  Task_Details ON Meter_Reading.Task_Id = Task_Details.Auto_Id WHERE Meter_Reading.Task_Id ='"+TaskID+"' AND Meter_Reading.UpdatedStatus = 'no'  " ;
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
                    StringBuffer sbRemark = new StringBuffer();
                    JSONObject DatPostingJsonObject = new JSONObject();
                    String selectQueryData = "SELECT  Data_Posting.* FROM Data_Posting LEFT JOIN  Task_Details ON Data_Posting.Task_Id = Task_Details.Auto_Id WHERE Data_Posting.UpdatedStatus = 'no' AND Data_Posting.Task_Id ='"+TaskID+"' ";
                    Cursor cursorDataPosting = db.rawQuery(selectQueryData, null);
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
                    cursorDataPosting.close();

                    StringBuffer sbAlertForm_Structure_Id = new StringBuffer();
                    JSONObject AlertJsonObject = new JSONObject();
                    String selectQueryAlert = "SELECT * FROM AlertMaster WHERE Task_Id='"+TaskID+"'";
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
            generateNoteOnSD(getApplicationContext(), "addTaskSelfie.txt", params.toString());
            client.post(new applicationClass().urlString() + "android/addTaskSelfie.php", params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    // TODO Auto-generated method stub

                    if (statusCode == 404) {
                        Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                        pDialog.dismiss();
                    } else if (statusCode == 500) {
                        Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                        pDialog.dismiss();
                    } else {
                        Toast.makeText(getApplicationContext(), "Internet Connection Lost.Synchronization failed.", Toast.LENGTH_LONG).show();
                        pDialog.dismiss();
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
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            });
        }
    }
    private class CheckingInternetConnectivity extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                pDialog = new ProgressDialog(HomePage.this);
                pDialog.setMessage("Checking internet connectivity. Please Wait..");
                pDialog.setIndeterminate(false);
                pDialog.setMax(200);
                pDialog.setProgress(0);
                pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pDialog.setCancelable(true);
                pDialog.show();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }

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
                pDialog.dismiss();
                if(checkInternetforReconfig==true){
                    pDialog = new ProgressDialog(HomePage.this);
                    pDialog.setMessage("Reconfiguring Data. Please Wait..");
                    pDialog.setIndeterminate(false);
                    pDialog.setMax(TotalReconfigTask());
                    pDialog.setProgress(0);
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            // this code will be executed after 2 seconds
                            pDialog.setCancelable(true);
                        }
                    }, 10000);

                    pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    pDialog.setCancelable(false);
                    pDialog.show();
                    Task_Status="Completed";
                    emailBackgroundData(HomePage.this);
                    uploadDataReconfig();
                }else {
                    pDialog = new ProgressDialog(HomePage.this);
                    pDialog.setMessage("Checking for data to upload. Please Wait");
                    //pDialog.setMessage(SYNCString);
                    pDialog.setIndeterminate(false);
                    pDialog.setMax(TotalTask());
                    pDialog.setProgress(0);
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            // this code will be executed after 2 seconds
                            pDialog.setCancelable(true);
                        }
                    }, 10000);

                    pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    pDialog.setCancelable(false);
                    pDialog.show();
                    Task_Status="Completed";
                    uploadData();
                    //new DataDownload().execute(myDb.SiteURL(User_Id));
                }
            }else {
                pDialog.dismiss();
                int color;
                color = Color.RED;
                Snackbar snackbar = Snackbar
                        .make(linearLayout, "You are not connected to internet", Snackbar.LENGTH_LONG);
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(color);
                snackbar.show();
            }
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
    private Date parseDate(String date) {
        SimpleDateFormat inputParser = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            return inputParser.parse(date);
        } catch (ParseException e) {
            return new Date(0);
        }
    }
    int TotalTask(){
        try {
            db= myDb.getWritableDatabase();
            String selectQuery = "SELECT  * FROM Task_Details where UpdatedStatus = 'no' and Site_Location_Id='"+myDb.Site_Location_Id(User_Id)+"' and   Assigned_To_User_Id='" + User_Id + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            int cursorValue = cursor.getCount();
            db.close();
            totalTask= cursorValue;
            taskLimit = totalTask/5;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalTask;
    }
    int TotalReconfigTask(){
        try {
            db= myDb.getWritableDatabase();
            String selectQuery = "SELECT  * FROM Task_Details where Site_Location_Id='"+myDb.Site_Location_Id(User_Id)+"' and UpdatedStatus = 'no'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            int cursorValue = cursor.getCount();
            db.close();
            totalTask= cursorValue;
            taskLimit = totalTask/5;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalTask;
    }
    public String formatDate(Date date) {
        SimpleDateFormat inputParser = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return inputParser.format(date);
    }
    @SuppressWarnings("resource")
    public void exportDatabse(Context ctx) {
        File backupDB = null;
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            if (sd.canWrite()) {
                String currentDBPath = "//data//" + ctx.getPackageName()
                        + "//databases//" + "facilitymate.db" + "";
                File currentDB = new File(data, currentDBPath);
                backupDB = new File(sd, "facilitymate.db");
                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB)
                            .getChannel();
                    FileChannel dst = new FileOutputStream(backupDB)
                            .getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
            backupDB.mkdirs();

            try {
                ZipFile zipFile = new ZipFile("/storage/emulated/0/Facilitymate.zip");
                ZipParameters parameters = new ZipParameters();
                parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
                parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
                parameters.setEncryptFiles(true);
                parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
                parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
                parameters.setPassword("megavision2468");
                ArrayList<File> filesToAdd = new ArrayList<>();
                filesToAdd.add(backupDB);//change of file
                zipFile.addFiles(filesToAdd, parameters);
            }catch (Exception e){
                e.printStackTrace();
            }

            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("*/*");
            emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                    new String[]{"megavisionpvtltd@gmail.com"});
            Calendar calendar = Calendar.getInstance();
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:MM");
            String rdate = simpleDateFormat.format(calendar.getTime());
            StringBuilder sb = new StringBuilder();
            sb.append("User : ");
            sb.append(settings.getString("Username", null));
            sb.append('\n');
            sb.append("Date Time : ");
            sb.append(calendar.getTime());
            sb.append('\n');
            sb.append("Site_Name : ");
            sb.append(myDb.SiteName(User_Id));
            sb.append('\n');
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                    getResources().getString(R.string.application_name)+" FacilityMate db " + rdate);
            emailIntent.putExtra(Intent.EXTRA_TEXT,sb.toString());
            emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + "/storage/emulated/0/Facilitymate.zip"));
            startActivity(Intent.createChooser(emailIntent, "Export database"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void emailBackgroundData(Context ctx){
        {
            File backupDB = null;
            try {
                File sd = Environment.getExternalStorageDirectory();
                File data = Environment.getDataDirectory();

                if (sd.canWrite()) {
                    String currentDBPath = "//data//" + ctx.getPackageName()
                            + "//databases//" + "facilitymate.db" + "";
                    File currentDB = new File(data, currentDBPath);
                    backupDB = new File(sd, "facilitymate.db");

                    if (currentDB.exists()) {

                        FileChannel src = new FileInputStream(currentDB)
                                .getChannel();
                        FileChannel dst = new FileOutputStream(backupDB)
                                .getChannel();
                        dst.transferFrom(src, 0, src.size());
                        src.close();
                        dst.close();
                    }
                }
                backupDB.mkdirs();

                try {
                    ZipFile zipFile = new ZipFile("/storage/emulated/0/Facilitymate.zip");
                    ZipParameters parameters = new ZipParameters();
                    parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
                    parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
                    parameters.setEncryptFiles(true);
                    parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
                    parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
                    parameters.setPassword("megavision2468");
                    ArrayList<File> filesToAdd = new ArrayList<>();
                    filesToAdd.add(backupDB);//change of file
                    zipFile.addFiles(filesToAdd, parameters);
                }catch (Exception e){
                    e.printStackTrace();
                }
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Calendar calendar = Calendar.getInstance();
                            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:MM");
                            String rdate = simpleDateFormat.format(calendar.getTime());
                            StringBuilder sb = new StringBuilder();
                            sb.append("User : ");
                            sb.append(settings.getString("Username", null));
                            sb.append('\n');
                            sb.append("Date Time : ");
                            sb.append(calendar.getTime());
                            sb.append('\n');
                            sb.append("Site_Name : ");
                            sb.append(myDb.SiteName(User_Id));
                            sb.append('\n');
                            sb.append("Reconfig");

                            com.example.google.template.emailfiles.GMailSender sender = new com.example.google.template.emailfiles.GMailSender(
                                    "megavisionpvtltd@gmail.com",
                                    "chandan123");
                            sender.addAttachment("/storage/emulated/0/Facilitymate.zip");
                            sender.sendMail(getResources().getString(R.string.application_name) + " FacilityMate db", sb.toString(),
                                    "megavisionpvtltd@gmail.com",
                                    "megavisionpvtltd@gmail.com");

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                        }
                    }
                }).start();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    private class getApk extends AsyncTask<String, String, String> {
        String Version="";
        String file_url="";
        String mainURL="";
        String Date="";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(HomePage.this);
            pDialog.setMessage("Checking new update...");
            pDialog.setIndeterminate(false);
            pDialog.setMax(100);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... strings) {

            try {
                int verCode =BuildConfig.VERSION_CODE;
                HttpHandler handler = new HttpHandler();
                String jsonStrSite= handler.apkDetails(verCode, User_Id);
                if(jsonStrSite !=null){
                    JSONObject jsonObjSite = new JSONObject(jsonStrSite);
                    JSONArray siteDetails = jsonObjSite.getJSONArray("apk");
                    for (int i = 0; i < siteDetails.length(); i++) {
                        JSONObject c = siteDetails.getJSONObject(i);
                        file_url = c.getString("Url");
                        Version = c.getString("Version");
                        Date = c.getString("Release_Date");
                        mainURL = file_url + "" + Version + ".apk";
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }



            return null;
        }
        @Override
        protected void onPostExecute(String file_url) {
            //pDialog.dismiss();
            PromptApkDialog(mainURL, Version, Date);
        }
    }
    public void PromptApkDialog(final String Url,String Version,String Date){
        if(LOG)Log.d("Fadsfasdf","--"+Url);
        try {
            if(!Url.equalsIgnoreCase("")) {
                AlertDialog dialog = new AlertDialog.Builder(HomePage.this)
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
                                        editorTaskInsert = settings.edit();
                                        editorTaskInsert.putString("day_apkupdate",new applicationClass().yymmdd());
                                        editorTaskInsert.commit();
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
            pDialog = new ProgressDialog(HomePage.this);
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
                if(LOG)Log.d("fdsafdasf",""+url);
                URLConnection conection = url.openConnection();
                conection.connect();
                int lenghtOfFile = conection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                OutputStream output = new FileOutputStream("/sdcard/multisite.zip");
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
                if (LOG)Log.e("Error: ", e.getMessage());
            }

            return null;
        }


        protected void onProgressUpdate(String... progress) {
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }
        @Override
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            String path = "/storage/emulated/0/";
            unpackZip(path, "multisite.zip");
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
                intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory()+"/templatev1.0.1.apk")), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // without this flag android returned a intent error!
                startActivity(intent);
            }
            else{
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory()+"/templatev1.0.1.apk")), "application/vnd.android.package-archive");
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag android returned a intent error!
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }

    }
    private boolean unpackZip(String path, String zipname)
    {
        InputStream is;
        ZipInputStream zis;
        try
        {
            is = new FileInputStream(path + zipname);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;

            while((ze = zis.getNextEntry()) != null)
            {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int count;

                String filename = ze.getName();
                FileOutputStream fout = new FileOutputStream(path + filename);

                // reading and writing
                while((count = zis.read(buffer)) != -1)
                {
                    baos.write(buffer, 0, count);
                    byte[] bytes = baos.toByteArray();
                    fout.write(bytes);
                    baos.reset();
                }

                fout.close();
                zis.closeEntry();
            }

            zis.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        String barcode =result.getContents();
        try {
            if(LOG){ Log.d(TAG,"barcode"+barcode);}
            if (result.getContents() != null) {
                try {
                    NFCbarcode=barcode;
                    Snackbar snackbar = Snackbar.make(linearLayout, "Scan nfc to write  "+NFCbarcode, Snackbar.LENGTH_LONG);
                    snackbar.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else
                Toast.makeText(getApplicationContext(), "No barcode Found", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Bundle extras = intent.getExtras();
        String tabNumber;

        if(extras != null) {
            tabNumber = extras.getString("message");

            if(tabNumber.equalsIgnoreCase("Update")){
                PromptApkDialog();
            }


        } else {
            Log.d("TEMP", "Extras are NULL");

        }
        if(NFCbarcode!=null) {
            try {
                if (intent.hasExtra(mNfcAdapter.EXTRA_TAG)) {
                    Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                    NdefMessage ndefMessage = createNdefMessage(NFCbarcode);
                    writeNdefMessage(tag, ndefMessage);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(getApplicationContext(),"Nothing to write or read",Toast.LENGTH_LONG).show();
        }
    }

    private Date parseDate1(String date) {
        SimpleDateFormat inputParser = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return inputParser.parse(date);
        } catch (ParseException e) {
            return new Date(0);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        try {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(Config.REGISTRATION_COMPLETE));

            // register new push message receiver
            // by doing this, the activity will be notified each time a new message arrives
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(Config.PUSH_NOTIFICATION));

            // clear the notification area when the app is opened
            NotificationUtils.clearNotifications(getApplicationContext());
            //downloadAsset();
            enableForegroundDispatchSystem();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);

        try {
             disableForegroundDispatchSystem();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void enableForegroundDispatchSystem(){
        try {
            Intent intent = new Intent(this, getClass()).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            IntentFilter[] intentFilters = new IntentFilter[] {};
            mNfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void disableForegroundDispatchSystem(){
        try {
            mNfcAdapter.disableForegroundDispatch(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private NdefMessage createNdefMessage(String content){
        NdefRecord ndefRecord = createTextRecord(content);
        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{ndefRecord});
        return ndefMessage;
    }
    private NdefRecord createTextRecord(String content){
        try {

            byte[] language;
            language = Locale.getDefault().getLanguage().getBytes("UTF-8");
            final byte[] text = content.getBytes("UTF-8");
            final int languageSize = language.length;
            final int textSize = text.length;
            final ByteArrayOutputStream payload = new ByteArrayOutputStream(1+languageSize+textSize);

            payload.write((byte)(languageSize & 0x1F));
            payload.write(language, 0, languageSize);
            payload.write(text, 0 ,textSize);

            return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload.toByteArray());

        }catch (Exception e){
            if (LOG)Log.e("createTextRecord",e.getMessage());
        }
        return null;
    }
    private void formatTag(Tag tag, NdefMessage ndefMessage){
        try {
            NdefFormatable ndefFormatable = NdefFormatable.get(tag);
            if (ndefFormatable == null){
                Toast.makeText(this,"Tag is not ndef formatable",Toast.LENGTH_SHORT).show();
            }
            ndefFormatable.connect();
            ndefFormatable.format(ndefMessage);
            ndefFormatable.close();
            Snackbar snackbar = Snackbar.make(linearLayout, "Tag Written ", Snackbar.LENGTH_LONG);
            snackbar.show();
            disableForegroundDispatchSystem();

        }catch (Exception e){
            if (LOG)Log.e("formatTag",e.getMessage());
        }
    }
    private void writeNdefMessage(Tag tag, NdefMessage ndefMessage){
        try {

            if (tag == null){
                Toast.makeText(this, "Tag object cannot be null",Toast.LENGTH_SHORT).show();
                return;
            }
            Ndef ndef = Ndef.get(tag);
            if (ndef == null){
                formatTag(tag, ndefMessage);
            }
            else {
                ndef.connect();
                if (!ndef.isWritable()){
                    Toast.makeText(this,"tag is not writable!",Toast.LENGTH_SHORT).show();
                    ndef.close();
                    return;
                }
                ndef.writeNdefMessage(ndefMessage);
                ndef.close();
                Snackbar snackbar = Snackbar.make(linearLayout, "Tag Written "+NFCbarcode, Snackbar.LENGTH_LONG);
                snackbar.show();
            }

        }catch (Exception e){
            if (LOG)Log.e("writeNdefMessage",e.getMessage());
        }
    }

    public void getPPMTask(){



        String ugid = myDb.UserGroupId(User_Id);
        Log.d("UGID",ugid);
        String data = "";
        try {
            data = URLEncoder.encode("User_Group_Id", "UTF-8") + "=" + URLEncoder.encode(myDb.UserGroupId(User_Id), "UTF-8")+"&"+
                    URLEncoder.encode("Site_Location_Id", "UTF-8") + "=" + URLEncoder.encode(myDb.Site_Location_Id(User_Id), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.d("PPMURLDATA", UrlList.PPMTASKURL+data);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                UrlList.PPMTASKURL+data, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("PPMResponse", response.toString());

                PPMresponse= response;
                new InsertPPMTask().execute();


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (pDialog.isShowing()) {

                    pDialog.dismiss();
                }
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog

            }
        });

        // Adding request to request queue
        applicationClass.getInstance().addToRequestQueue(jsonObjReq);
       // new InsertPPMTask().execute();


    }


    public class InsertPPMTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            pDialog.setMessage("Downloading PPM Task. Please Wait..");
            pDialog.setIndeterminate(false);
            pDialog.setMax(100);
            pDialog.setProgress(0);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setCancelable(true);
            pDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            try {


                try {

                    // Parsing json object response
                    // response will be a json object

                    try {
                        JSONArray task = PPMresponse.getJSONArray("PPMTaskFeqeuncy");
                        if(LOG)Log.d("DATasValue123",task+"");
                        if (task != null) {

                            db = myDb.getWritableDatabase();
                            String AssetActivityLinkingsql = "insert into Asset_Activity_Linking (Site_Location_Id ,Auto_Id ,Asset_Id ,Activity_Id,RecordStatus ,UpdatedStatus )values(?,?,?,?,?,?);";
                            String ActivityMastersql = "insert into Activity_Master (Site_Location_Id ,Auto_Id ,Form_Id ,Activity_Name ,RecordStatus ,UpdatedStatus )values(?,?,?,?,?,?);";
                            String AssetActivityAssignedTosql = "insert into Asset_Activity_AssignedTo (Site_Location_Id ,Auto_Id ,Assigned_To_User_Id ,Assigned_To_User_Group_Id ,Asset_Activity_Linking_Id,RecordStatus ,UpdatedStatus )values(?,?,?,?,?,?,?);";
                            String PpmTasksql = "insert into PPM_Task (Auto_Id, Site_Location_Id, Activity_Frequency, Task_Date,Task_End_Date, Task_Status, Asset_Activity_Linking_Id,  Assigned_To_User_Id, Assigned_To_User_Group_Id, TimeStartson, Activity_Duration, Grace_Duration_Before, Grace_Duration_After, Record_Status, UpdatedStatus)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
                            db.beginTransaction();
                            SQLiteStatement AssetActivityLinkingstmt = db.compileStatement(AssetActivityLinkingsql);
                            SQLiteStatement ActivityMasterstmt = db.compileStatement(ActivityMastersql);
                            SQLiteStatement AssetActivityAssignedTostmt = db.compileStatement(AssetActivityAssignedTosql);
                            SQLiteStatement PpmTaskstmt = db.compileStatement(PpmTasksql);

                            for (int i = 0; i < task.length(); i++) {

                                JSONObject c = task.getJSONObject(i);
                                Log.d("hbkjcnv",((i+1)*100)/task.length()+"");
                                int setValue = ((i+1)*100)/task.length();
                                //pDialog.setProgress(setValue);
                                pDialog.setProgress(setValue);
                                ////////////////////////////ppm task insert in db//////////////////////////

                                String  ppm_auto_id = c.getString("Auto_Id");
                                String  activity_frequency   = c.getString("Activity_Frequency");
                                String  task_date = c.getString("Task_Date");
                                String  asset_activity_linking_id = c.getString("Asset_Activity_Linking_Id");
                                String Assigned_To_User_Id = c.getString("Assigned_To_User_Id");
                                String Assigned_To_User_Group_Id = c.getString("Assigned_To_User_Group_Id");
                                String  task_status = c.getString("Task_Status");
                                String  timestartson = c.getString("Timestartson");
                                String  Task_End_Date = c.getString("Task_End_Date");
                                String  activity_duration = c.getString("Activity_Duration");
                                String  grace_duration_before = c.getString("Grace_Duration_Before");
                                String  grace_duration_after = c.getString("Grace_Duration_After");
                                String  ppm_Record_Status = c.getString("Record_Status");

                                PpmTaskstmt.bindString(1,ppm_auto_id);
                                PpmTaskstmt.bindString(2,myDb.Site_Location_Id(User_Id));
                                PpmTaskstmt.bindString(3,activity_frequency);
                                PpmTaskstmt.bindString(4,task_date);
                                PpmTaskstmt.bindString(5,Task_End_Date);
                                PpmTaskstmt.bindString(6,task_status);
                                PpmTaskstmt.bindString(7,asset_activity_linking_id);
                                PpmTaskstmt.bindString(8,Assigned_To_User_Id);
                                PpmTaskstmt.bindString(9,Assigned_To_User_Group_Id);
                                PpmTaskstmt.bindString(10,timestartson);
                                PpmTaskstmt.bindString(11,activity_duration);
                                PpmTaskstmt.bindString(12,grace_duration_before);
                                PpmTaskstmt.bindString(13,grace_duration_after);
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

            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            super.onPostExecute(file_url);

            if (pDialog.isShowing()) {

                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Data is Synchronised", Toast.LENGTH_SHORT).show();
            }

        }
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
            String selectQuery = "SELECT  * FROM "+ DatabaseColumn.PPM_Task+" where "+DatabaseColumn.Task_Status +" <> 'Pending' AND  "+DatabaseColumn.UpdatedStatus+" = 'no' and "+DatabaseColumn.Assigned_To_User_Id+" ='"+UserId+"' and Site_Location_Id='"+site_id+"' ORDER BY CASE WHEN "+DatabaseColumn.Task_Status +"='Completed' THEN 1 WHEN "+DatabaseColumn.Task_Status +"='Missed' THEN 2  END, "+DatabaseColumn.Task_Status +" ASC"  ;

            db=myDb.getWritableDatabase();

            Cursor cursor = db.rawQuery(selectQuery, null);
            Log.d("Teasdasd",selectQuery + " "+cursor.getCount() );
            Log.d("Teasdasd","GitWorklingas");
            Log.d("Teasdasd","Dakshata ko dekha");
            Log.d("Teasdasd","Prathamesh ko dekha");
            Log.d("Teasdasd","Dakshata ko Dakshata");
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


    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

    }



}
