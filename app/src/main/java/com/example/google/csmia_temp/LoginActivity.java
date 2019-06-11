package com.example.google.csmia_temp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.google.csmia_temp.ConstantList.UrlList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity {
    EditText etUserName, etPass;
    Button btnRegister, btnBack, btnVersion;
    DatabaseHelper myDb;
    SQLiteDatabase db;
    private ProgressDialog pDialog;
    String passwordServer, usernameServer;
    String sharedUserName, sharedPassword;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    String companyId;
    Snackbar snackbar;
    MCrypt mCrypt;
    String LastImageName="";
    private int STORAGE_PERMISSION_CODE = 23;
    public boolean internetConnection = false;
    boolean permissionDenided = false;
    HttpHandler sh;
    String msg = "Without this permissions the app \n" +
            "is unable to perfrom all the features.\n" +
            "Are you sure you want to deny this permissions?\n";
    private static final String TAG = LoginActivity.class.getSimpleName();
    static final boolean LOG = new applicationClass().checkLog();


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        try {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            settings = PreferenceManager.getDefaultSharedPreferences(this);
            mCrypt = new MCrypt();
            etUserName = (EditText) findViewById(R.id.etUserName);
            etPass = (EditText) findViewById(R.id.etPass);
            btnRegister = (Button) findViewById(R.id.btnReg);
            btnBack = (Button) findViewById(R.id.btnBack);
            btnVersion = (Button) findViewById(R.id.btnVersion);
            sharedUserName = settings.getString("Username", null);
            sharedPassword = settings.getString("Password", null);
            myDb = new DatabaseHelper(getApplicationContext());
            requestStoragePermission();
            //createPackage(getApplicationContext());
            btnVersion.setText("Version v" + BuildConfig.VERSION_NAME);
            btnVersion.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(getApplicationContext(), "" + new applicationClass().urlString(), Toast.LENGTH_SHORT).show();
                    return false;
                }
            });

            etPass.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if (event.getRawX() >= etPass.getRight() - etPass.getTotalPaddingRight()) {
                            // your action for drawable click event
                            //Toast.makeText(getApplicationContext(),"Clicked",Toast.LENGTH_SHORT).show();
                            etPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            etPass.setSelection(etPass.getText().length());

                            // etPass.setTransformationMethod(null);
                            return true;
                        }
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (event.getRawX() >= etPass.getRight() - etPass.getTotalPaddingRight()) {
                            etPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            etPass.setSelection(etPass.getText().length());
                            return true;
                        }
                    }
                    return false;
                }
            });


            if (sharedUserName != null) {
                loginShared();
            }
            if (sharedUserName == null) {
                btnRegister.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        usernameServer = etUserName.getText().toString();
                        passwordServer = etPass.getText().toString();
                        sharedUserName = settings.getString("Username", null);
                        sharedPassword = settings.getString("Password", null);

                        if (usernameServer.equals("")||passwordServer.equals("")) {
                            Toast.makeText(getApplicationContext(), "Please Enter Username and Password", Toast.LENGTH_LONG).show();
                        } else if (usernameServer != "") {
                            myDb = new DatabaseHelper(getBaseContext());
                            db = myDb.getWritableDatabase();
                            String selectQuery = "SELECT * FROM Login_Details  WHERE Username='" + usernameServer + "' and Password = '" + passwordServer + "'";
                            Cursor cursor = db.rawQuery(selectQuery, null);
                            if (cursor.getCount() != 0) {
                                cursor.moveToFirst();
                                if (cursor.getString(cursor.getColumnIndex("Username")).equals(usernameServer) && passwordServer.equals(cursor.getString(cursor.getColumnIndex("Password")))) {
                                    Intent intent = new Intent(LoginActivity.this, HomePage.class);
                                    intent.putExtra("User_Id", cursor.getString(cursor.getColumnIndex("User_Id")));
                                    intent.putExtra("Company_Customer_Id", cursor.getString(cursor.getColumnIndex("Company_Customer_Id")));
                                    editor = settings.edit();
                                    editor.putString("Username", cursor.getString(cursor.getColumnIndex("Username")));
                                    editor.putString("Password", cursor.getString(cursor.getColumnIndex("Password")));
                                    editor.commit();
                                    startActivity(intent);
                                    finish();
                                }
                            } else {
                                new DownloadWebPageTask().execute();
                            }
                            cursor.close();
                            db.close();

                        }
                    }
                });
            }
        } catch (Exception e) {
            Log.d("Login113", "ERROR==" + e);
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error code: Login113", Toast.LENGTH_SHORT).show();
        }


    }

    private void getSiteImages(final String Image_Name){


        ImageRequest request = new ImageRequest(UrlList.IMAGEURL+Image_Name,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {

                        createPackage(getApplicationContext(), bitmap, Image_Name);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {

                    }
                });
// Access the RequestQueue through your singleton class.
        applicationClass.getInstance().addToRequestQueue(request);
    }


    public void LoginFunction(){
        String data = "";
        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Logging In. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setMax(100);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setCancelable(false);
        pDialog.show();
        try {
            data = URLEncoder.encode("Username", "UTF-8") + "=" + URLEncoder.encode(usernameServer, "UTF-8")+"&"+
                    URLEncoder.encode("Password", "UTF-8") + "=" + URLEncoder.encode(passwordServer, "UTF-8") ;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.d("PPMURLDATA", UrlList.LOGINURL+data);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                UrlList.LOGINURL+data, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    JSONArray UserDetails = response.getJSONArray("UserDetails");

                    try {
                        for (int j = 0; j < UserDetails.length(); j++) {
                            JSONObject obj = (JSONObject) UserDetails.get(j);
                            int Mobile_Access = (int) obj.get("Mobile_Access");
                            int Asset_View = (int) obj.get("Asset_View");
                            if (Mobile_Access == 1) {
                                StringBuilder stringBuilder = new StringBuilder();
                                String UserId = obj.get("Auto_Id").toString();
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("User_Id", obj.get("Auto_Id").toString());
                                contentValues.put("Employee_Id", "");
                                contentValues.put("Company_Customer_Id", "");
                                contentValues.put("Username", usernameServer);
                                contentValues.put("Password", passwordServer);
                                contentValues.put("Employee_Name", obj.get("Employee_Name").toString());
                                contentValues.put("Asset_View",Asset_View);
                                contentValues.put("Verify", 1);
                                db = myDb.getWritableDatabase();
                                long insertuser = db.insert("Login_Details", null, contentValues);
                                db.close();
                                if (insertuser == -1) {
                                    Log.d("userInsert", "User not Inserted");
                                } else {
                                    Log.d("userInsert", "User Inserted Inserted");
                                }

                                try {
                                    JSONArray siteDetails = response.getJSONArray("SiteDetails");

                                    for (int i = 0; i < siteDetails.length(); i++) {
                                        JSONObject c = siteDetails.getJSONObject(i);
                                        String Site_Name_Label = c.getString("Site_Label");
                                        String Site_Name_Value = c.getString("Site_Value");
                                        String Site_Location_Id = c.getString("Site_Location_Id");
                                        String Site_DB_Name = c.getString("Site_DB_Name");
                                        String Master_DB = c.getString("Master_DB");
                                        //for (int k = 0; k < Site_Name_Label_OptionList.length; k++) {
                                        ContentValues values = new ContentValues();
                                        values.put("Site_Name_Label", Site_Name_Label);
                                        values.put("Site_Name_Value", Site_Name_Value);
                                        values.put("Auto_Id", Site_Location_Id);
                                        values.put("Site_DB_Name", Site_DB_Name);
                                        values.put("Master_DB", Master_DB);
                                        values.put("Assigned_To_User_Id", UserId);
                                        String querySiteCheck = "Select * from Site_Details where Site_Name_Label='" + Site_Name_Label + "' and Assigned_To_User_Id='" + UserId + "'";
                                        db = myDb.getWritableDatabase();
                                        Cursor cursorSiteCheck = db.rawQuery(querySiteCheck, null);
                                        if (cursorSiteCheck.getCount() == 0) {
                                            long insertuser1 = db.insert("Site_Details", null, values);
                                            if (insertuser1 == -1) {
                                                Log.d("userInsert1", "User not Inserted");
                                            }
                                        }
                                        db.close();
                                        // }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    JSONArray UserSiteLinking = response.getJSONArray("UserSiteLinking");
                                    for (int i = 0; i < UserSiteLinking.length(); i++) {
                                        JSONObject UserSiteLinkingObject = UserSiteLinking.getJSONObject(i);
                                        String Linking_AutoId = UserSiteLinkingObject.getString("Auto_Id");
                                        String User_Id = UserSiteLinkingObject.getString("User_Id");
                                        String Site_Location_Id = UserSiteLinkingObject.getString("Site_Location_Id");
                                        String User_Group_Id = UserSiteLinkingObject.getString("User_Group_Id");
                                        String User_Role_Id = UserSiteLinkingObject.getString("User_Role_Id");
                                        String User_Right_Id = UserSiteLinkingObject.getString("User_Right_Id");
                                        String Created_DateTime = UserSiteLinkingObject.getString("Created_DateTime");
                                        String Deleted_DateTime = UserSiteLinkingObject.getString("Deleted_DateTime");
                                        String Record_Status = UserSiteLinkingObject.getString("Record_Status");

                                        ContentValues values = new ContentValues();
                                        values.put("Linking_Auto_Id", Linking_AutoId);
                                        values.put("User_Id", User_Id);
                                        values.put("Site_Location_Id", Site_Location_Id);
                                        values.put("User_Group_Id", User_Group_Id);
                                        values.put("User_Role_Id", User_Role_Id);
                                        values.put("User_Right_Id", User_Right_Id);
                                        values.put("Created_DateTime", Created_DateTime);
                                        values.put("Deleted_DateTime", Deleted_DateTime);
                                        values.put("Record_Status", Record_Status);

                                        String querySiteCheck = "Select * from UserSiteLinking where Linking_Auto_Id='" + Linking_AutoId + "'"; //and Assigned_To_User_Id='" + UserId + "'
                                        db = myDb.getWritableDatabase();
                                        Cursor cursorSiteCheck = db.rawQuery(querySiteCheck, null);
                                        if (cursorSiteCheck.getCount() == 0) {
                                            long insertuser1 = db.insert("UserSiteLinking", null, values);
                                            if (insertuser1 == -1) {
                                                Log.d("UserSiteLinking", "UserSiteLinking not Inserted");
                                            } else {
                                                Log.d("UserSiteLinking", "UserSiteLinking Inserted");
                                            }
                                        }
                                        db.close();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    JSONArray UserGroup = response.getJSONArray("UserGroup");
                                    for (int i = 0; i < UserGroup.length(); i++) {
                                        JSONObject c = UserGroup.getJSONObject(i);
                                        String User_Group_Id = c.getString("Auto_Id");
                                        String Group_Name = c.getString("Group_Name");
                                        ContentValues contentValues1 = new ContentValues();
                                        contentValues1.put("User_Group_Id", User_Group_Id);
                                        contentValues1.put("Group_Name", Group_Name);
                                        db = myDb.getWritableDatabase();
                                        String UserGroupQuery = "Select * from User_Group Where Group_Name='" + Group_Name + "' ";
                                        Cursor cursor1 = db.rawQuery(UserGroupQuery, null);
                                        if (cursor1.getCount() == 0) {
                                            long insertusergroup = db.insert("User_Group", null, contentValues1);
                                        }
                                        db.close();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                try{
                                    JSONArray EmailDetails = response.getJSONArray("EmailList");
                                    for (int i = 0; i < EmailDetails.length(); i++) {
                                        JSONObject c = EmailDetails.getJSONObject(i);
                                        String uuid = c.getString("Auto_Id");
                                        String Site_Location_Id = c.getString("Site_Location_Id");
                                        String Employee_Email = c.getString("Employee_Email");
                                        String Recipient_Type = c.getString("Recipient_Type");
                                        String Email_For = c.getString("Email_For");
                                        String Created_DateTime = c.getString("Created_DateTime");
                                        String Deleted_DateTime = c.getString("Deleted_DateTime");
                                        String Record_Status = c.getString("Record_Status");
                                        ContentValues contentValues1 = new ContentValues();
                                        contentValues1.put("Email_Auto_Id",uuid);
                                        contentValues1.put("Site_Location_Id", Site_Location_Id);
                                        contentValues1.put("Employee_Email", Employee_Email);
                                        contentValues1.put("Recipient_Type", Recipient_Type);
                                        contentValues1.put("Email_For",Email_For);
                                        contentValues1.put("Created_DateTime", Created_DateTime);
                                        contentValues1.put("Deleted_DateTime", Deleted_DateTime);
                                        contentValues1.put("Record_Status", Record_Status);
                                        db = myDb.getWritableDatabase();
                                        String UserGroupQuery = "Select * from EmailList Where Employee_Email='" + Employee_Email + "'";
                                        Cursor cursor1 = db.rawQuery(UserGroupQuery, null);
                                        if (cursor1.getCount() == 0) {
                                            long insertemaillist = db.insert("EmailList", null, contentValues1);
                                        }
                                        db.close();
                                    }

                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                try{
                                    JSONArray SMSMaster = response.getJSONArray("SMSMaster");
                                    for (int i = 0; i < SMSMaster.length(); i++) {
                                        JSONObject c = SMSMaster.getJSONObject(i);
                                        String uuid = c.getString("Auto_Id");
                                        //String Site_Location_Id = c.getString("Site_Location_Id");
                                        String Username = c.getString("UserName");
                                        String Password = c.getString("Password");
                                        String Type = c.getString("Type");
                                        String Source = c.getString("Source");
                                        String URL = c.getString("URL");
                                        ContentValues contentValues1 = new ContentValues();
                                        contentValues1.put("Auto_Id",uuid);
                                        //contentValues1.put("Site_Location_Id", Site_Location_Id);
                                        contentValues1.put("Username", Username);
                                        contentValues1.put("Password",Password);
                                        contentValues1.put("Type", Type);
                                        contentValues1.put("Source", Source);
                                        contentValues1.put("URL", URL);
                                        db = myDb.getWritableDatabase();
                                        String UserGroupQuery = "Select * from SMS_Master";
                                        Cursor cursor1 = db.rawQuery(UserGroupQuery, null);
                                        if (cursor1.getCount() == 0) {
                                            long insertemaillist = db.insert("SMS_Master", null, contentValues1);
                                        }
                                        db.close();
                                    }

                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                try{
                                    JSONArray SMSAlert = response.getJSONArray("SMSAlert");
                                    for (int i = 0; i < SMSAlert.length(); i++) {
                                        JSONObject c = SMSAlert.getJSONObject(i);
                                        String uuid = c.getString("Auto_Id");
                                        String Site_Location_Id = c.getString("Site_Location_Id");
                                        String Mobile_Number = c.getString("Mobile");
                                        String User_Group_Id = c.getString("User_Group_Id");

                                        ContentValues contentValues1 = new ContentValues();
                                        contentValues1.put("Auto_Id",uuid);
                                        contentValues1.put("Site_Location_Id", Site_Location_Id);
                                        contentValues1.put("Mobile_Number", Mobile_Number);
                                        contentValues1.put("User_Group_Id", User_Group_Id);

                                        db = myDb.getWritableDatabase();
                                        String UserGroupQuery = "Select * from SMS_Alert Where Site_Location_Id='" + Site_Location_Id + "'";
                                        Cursor cursor1 = db.rawQuery(UserGroupQuery, null);
                                        if (cursor1.getCount() == 0) {
                                            long insertemaillist = db.insert("SMS_Alert", null, contentValues1);
                                        }
                                        db.close();
                                    }

                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                                try {
                                    JSONArray imageList = response.getJSONArray("ImageDetails");
                                    for (int i = 0; i < imageList.length(); i++) {
                                        JSONObject c = imageList.getJSONObject(i);
                                        String Auto_Id = c.getString("Auto_Id");
                                        String Site_Location_Id = c.getString("Site_Location_Id");
                                        String Image_Name = c.getString("Image_Name");
                                        String Record_Status = c.getString("Record_Status");
                                        ContentValues contentValues1 = new ContentValues();
                                        contentValues1.put("Auto_Id", Auto_Id);
                                        contentValues1.put("Site_Location_Id", Site_Location_Id);
                                        contentValues1.put("Image_Name", Image_Name);
                                        contentValues1.put("Record_Status", Record_Status);
                                        db = myDb.getWritableDatabase();
                                        String UserGroupQuery = "Select * from site_imagelist Where Site_Location_Id='" + Site_Location_Id + "' AND  Image_Name='"+Image_Name+"'";
                                        Cursor cursor1 = db.rawQuery(UserGroupQuery, null);
                                        if (cursor1.getCount() == 0) {
                                            long insertusergroup = db.insert("site_imagelist", null, contentValues1);
                                        }
                                        db.close();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }





                                try{
                                    db = myDb.getWritableDatabase();
                                    String UserGroupQuery = "Select Image_Name from site_imagelist";
                                    Cursor cursor1 = db.rawQuery(UserGroupQuery, null);
                                    if (cursor1.moveToFirst()) {
                                        do {
                                            String Image_Name=cursor1.getString(cursor1.getColumnIndex("Image_Name"));
                                            Log.d("ImageName",Image_Name);

                                            if(!checkImages(Image_Name)){
                                                //Bitmap bitmap = sh.bannerImage(Image_Name);
                                                getSiteImages(Image_Name);

                                            }
                                            if(cursor1.getPosition() == (cursor1.getCount()-1)){
                                                LastImageName = Image_Name;
                                            }


                                        } while (cursor1.moveToNext());
                                    }
                                    cursor1.close();
                                    db.close();

                                    if (pDialog.isShowing())
                                        pDialog.dismiss();

                                    try {
                                        if (pDialog.isShowing())
                                            pDialog.dismiss();
                                        myDb = new DatabaseHelper(getBaseContext());
                                        db = myDb.getWritableDatabase();
                                        String selectQuery = "SELECT * FROM Login_Details  WHERE Username='" + usernameServer + "' and Password = '" + passwordServer + "'";
                                        Cursor cursor = db.rawQuery(selectQuery, null);
                                        if (cursor.getCount() != 0) {
                                            while (cursor.moveToNext()) {
                                                if (cursor.getString(4).equals(usernameServer) && passwordServer.equals(cursor.getString(5))) {
                                                    Intent intent = new Intent(LoginActivity.this, HomePage.class);
                                                    intent.putExtra("User_Id", cursor.getString(1));
                                                    new applicationClass().setUserId(cursor.getString(1));
                                                    intent.putExtra("Company_Customer_Id", cursor.getString(3));
                                                    editor = settings.edit();
                                                    editor.putString("Username", cursor.getString(4));
                                                    editor.putString("Password", cursor.getString(5));
                                                    editor.commit();
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Username or Password Incorrect", Toast.LENGTH_LONG).show();
                                        }
                                        cursor.close();
                                        db.close();
                                    } catch (Exception e) {
                                        Log.d("Login253", "ERROR==" + e);
                                        e.printStackTrace();
                                        Toast.makeText(getApplicationContext(), "Error code: Login253", Toast.LENGTH_SHORT).show();
                                    }


                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            } else {
                                if (pDialog.isShowing())
                                    pDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Access Not Granted", Toast.LENGTH_SHORT).show();                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                error.printStackTrace();
                Toast.makeText(getApplicationContext(),
                        "Username or Password Incorrect", Toast.LENGTH_SHORT).show();
                // hide the progress dialog

            }
        });
      /*  jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });*/

        // Adding request to request queue
        applicationClass.getInstance().addToRequestQueue(jsonObjReq);


    }


/*
    private class getUserDetails extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                pDialog = new ProgressDialog(LoginActivity.this);
                pDialog.setMessage("Logging In. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pDialog.setCancelable(false);
                pDialog.show();
            } catch (Exception e) {
                Log.d("Login134", "ERROR==" + e);
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... arg0) {
            String resturnValue = null;
            try {
                resturnValue = null;
                sh = new HttpHandler();


                String jsonStrSite = sh.LoginDetails(usernameServer, passwordServer);
               */
/* String jsonImageList = sh.imageList();
                JSONArray imageList = new JSONArray(jsonImageList);*//*

                JSONObject jsonObjSite = new JSONObject(jsonStrSite);
                JSONArray UserDetails = jsonObjSite.getJSONArray("UserDetails");
                try {
                    for (int j = 0; j < UserDetails.length(); j++) {
                        JSONObject obj = (JSONObject) UserDetails.get(j);
                        int Mobile_Access = (int) obj.get("Mobile_Access");
                        int Asset_View = (int) obj.get("Asset_View");
                        if (Mobile_Access == 1) {
                            StringBuilder stringBuilder = new StringBuilder();
                            String UserId = obj.get("Auto_Id").toString();
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("User_Id", obj.get("Auto_Id").toString());
                            contentValues.put("Employee_Id", "");
                            contentValues.put("Company_Customer_Id", "");
                            contentValues.put("Username", usernameServer);
                            contentValues.put("Password", passwordServer);
                            contentValues.put("Employee_Name", obj.get("Employee_Name").toString());
                            contentValues.put("Asset_View",Asset_View);
                            contentValues.put("Verify", 1);
                            db = myDb.getWritableDatabase();
                            long insertuser = db.insert("Login_Details", null, contentValues);
                            db.close();
                            if (insertuser == -1) {
                                Log.d("userInsert", "User not Inserted");
                            } else {
                                Log.d("userInsert", "User Inserted Inserted");
                            }

                            try {
                                JSONArray siteDetails = jsonObjSite.getJSONArray("SiteDetails");

                                for (int i = 0; i < siteDetails.length(); i++) {
                                    JSONObject c = siteDetails.getJSONObject(i);
                                    String Site_Name_Label = c.getString("Site_Label");
                                    String Site_Name_Value = c.getString("Site_Value");
                                    String Site_Location_Id = c.getString("Site_Location_Id");

                                    //for (int k = 0; k < Site_Name_Label_OptionList.length; k++) {
                                        ContentValues values = new ContentValues();
                                        values.put("Site_Name_Label", Site_Name_Label);
                                        values.put("Site_Name_Value", Site_Name_Value);
                                        values.put("Auto_Id", Site_Location_Id);
                                        values.put("Assigned_To_User_Id", UserId);
                                        String querySiteCheck = "Select * from Site_Details where Site_Name_Label='" + Site_Name_Label + "' and Assigned_To_User_Id='" + UserId + "'";
                                        db = myDb.getWritableDatabase();
                                        Cursor cursorSiteCheck = db.rawQuery(querySiteCheck, null);
                                        if (cursorSiteCheck.getCount() == 0) {
                                            long insertuser1 = db.insert("Site_Details", null, values);
                                            if (insertuser1 == -1) {
                                                Log.d("userInsert1", "User not Inserted");
                                            }
                                        }
                                        db.close();
                                   // }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                JSONArray UserSiteLinking = jsonObjSite.getJSONArray("UserSiteLinking");
                                for (int i = 0; i < UserSiteLinking.length(); i++) {
                                    JSONObject UserSiteLinkingObject = UserSiteLinking.getJSONObject(i);
                                    String Linking_AutoId = UserSiteLinkingObject.getString("Auto_Id");
                                    String User_Id = UserSiteLinkingObject.getString("User_Id");
                                    String Site_Location_Id = UserSiteLinkingObject.getString("Site_Location_Id");
                                    String User_Group_Id = UserSiteLinkingObject.getString("User_Group_Id");
                                    String User_Role_Id = UserSiteLinkingObject.getString("User_Role_Id");
                                    String User_Right_Id = UserSiteLinkingObject.getString("User_Right_Id");
                                    String Created_DateTime = UserSiteLinkingObject.getString("Created_DateTime");
                                    String Deleted_DateTime = UserSiteLinkingObject.getString("Deleted_DateTime");
                                    String Record_Status = UserSiteLinkingObject.getString("Record_Status");

                                    ContentValues values = new ContentValues();
                                    values.put("Linking_Auto_Id", Linking_AutoId);
                                    values.put("User_Id", User_Id);
                                    values.put("Site_Location_Id", Site_Location_Id);
                                    values.put("User_Group_Id", User_Group_Id);
                                    values.put("User_Role_Id", User_Role_Id);
                                    values.put("User_Right_Id", User_Right_Id);
                                    values.put("Created_DateTime", Created_DateTime);
                                    values.put("Deleted_DateTime", Deleted_DateTime);
                                    values.put("Record_Status", Record_Status);

                                    String querySiteCheck = "Select * from UserSiteLinking where Linking_Auto_Id='" + Linking_AutoId + "'"; //and Assigned_To_User_Id='" + UserId + "'
                                    db = myDb.getWritableDatabase();
                                    Cursor cursorSiteCheck = db.rawQuery(querySiteCheck, null);
                                    if (cursorSiteCheck.getCount() == 0) {
                                        long insertuser1 = db.insert("UserSiteLinking", null, values);
                                        if (insertuser1 == -1) {
                                            Log.d("UserSiteLinking", "UserSiteLinking not Inserted");
                                        } else {
                                            Log.d("UserSiteLinking", "UserSiteLinking Inserted");
                                        }
                                    }
                                    db.close();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                JSONArray UserGroup = jsonObjSite.getJSONArray("UserGroup");
                                for (int i = 0; i < UserGroup.length(); i++) {
                                    JSONObject c = UserGroup.getJSONObject(i);
                                    String User_Group_Id = c.getString("Auto_Id");
                                    String Group_Name = c.getString("Group_Name");
                                    ContentValues contentValues1 = new ContentValues();
                                    contentValues1.put("User_Group_Id", User_Group_Id);
                                    contentValues1.put("Group_Name", Group_Name);
                                    db = myDb.getWritableDatabase();
                                    String UserGroupQuery = "Select * from User_Group Where Group_Name='" + Group_Name + "' ";
                                    Cursor cursor1 = db.rawQuery(UserGroupQuery, null);
                                    if (cursor1.getCount() == 0) {
                                        long insertusergroup = db.insert("User_Group", null, contentValues1);
                                    }
                                    db.close();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            try {
                                JSONArray imageList = jsonObjSite.getJSONArray("ImageDetails");
                                for (int i = 0; i < imageList.length(); i++) {
                                    JSONObject c = imageList.getJSONObject(i);
                                    String Auto_Id = c.getString("Auto_Id");
                                    String Site_Location_Id = c.getString("Site_Location_Id");
                                    String Image_Name = c.getString("Image_Name");
                                    String Record_Status = c.getString("Record_Status");
                                    ContentValues contentValues1 = new ContentValues();
                                    contentValues1.put("Auto_Id", Auto_Id);
                                    contentValues1.put("Site_Location_Id", Site_Location_Id);
                                    contentValues1.put("Image_Name", Image_Name);
                                    contentValues1.put("Record_Status", Record_Status);
                                    db = myDb.getWritableDatabase();
                                    String UserGroupQuery = "Select * from site_imagelist Where Site_Location_Id='" + Site_Location_Id + "' AND  Image_Name='"+Image_Name+"'";
                                    Cursor cursor1 = db.rawQuery(UserGroupQuery, null);
                                    if (cursor1.getCount() == 0) {
                                        long insertusergroup = db.insert("site_imagelist", null, contentValues1);
                                    }
                                    db.close();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                           */
/* try {
                                *//*
*/
/*String jsonImageList = sh.imageList();
                                JSONArray imageList = new JSONArray(jsonImageList);*//*
*/
/*
                                JSONArray imageList = jsonObjSite.getJSONArray("ImageDetails");
                                for (int i = 0; i < imageList.length(); j++) {
                                    JSONObject c = imageList.getJSONObject(i);

                                    String Auto_Id = c.getString("Auto_Id");
                                    String Site_Location_Id = c.getString("Site_Location_Id");
                                    String Image_Name = c.getString("Image_Name");
                                    String Record_Status = c.getString("Record_Status");

                                    ContentValues contentValues1 = new ContentValues();
                                    contentValues1.put("Auto_Id", Auto_Id);
                                    contentValues1.put("Site_Location_Id", Site_Location_Id);
                                    contentValues1.put("Image_Name", Image_Name);
                                    contentValues1.put("Record_Status", Record_Status);
                                    db = myDb.getWritableDatabase();
                                    String UserGroupQuery = "Select * from site_imagelist Where Site_Location_Id='" + Site_Location_Id + "' AND  Image_Name='"+Image_Name+"'";
                                    if(!db.isOpen()){
                                        db = myDb.getWritableDatabase();
                                    }
                                    Cursor cursor1 = db.rawQuery(UserGroupQuery, null);

                                    if (cursor1.getCount() == 0) {

                                        long insertusergroup = db.insert("site_imagelist", null, contentValues1);
                                    }
                                    db.close();
                                   *//*
*/
/* Bitmap bitmap = sh.bannerImage(imageList.get(i).toString());
                                    createPackage(getApplicationContext(), bitmap, imageList.get(i).toString());*//*
*/
/*
                                }

                            }catch (Exception e){
                                e.printStackTrace();
                            }*//*


                          */
/*  *//*



                        } else {
                            resturnValue = "Access Not Granted";
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }




            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


            try{
                sh = new HttpHandler();
                db = myDb.getWritableDatabase();
                String UserGroupQuery = "Select Image_Name from site_imagelist";
                Cursor cursor1 = db.rawQuery(UserGroupQuery, null);
                if (cursor1.moveToFirst()) {
                    do {
                        String Image_Name=cursor1.getString(cursor1.getColumnIndex("Image_Name"));
                        if(!checkImages(Image_Name)){
                            Bitmap bitmap = sh.bannerImage(Image_Name);

                            createPackage(getApplicationContext(), bitmap, Image_Name);
                        }


                    } while (cursor1.moveToNext());
                }
                cursor1.close();
                db.close();



            }catch (Exception e){
                e.printStackTrace();
            }

            return resturnValue;
        }

        protected void onProgressUpdate(String... progress) {
            try {

                pDialog.setProgress(Integer.parseInt(progress[0]));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result == null) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                String SAutoId = "", CompanyId = "";
                try {
                    if (pDialog.isShowing())
                        pDialog.dismiss();
                    myDb = new DatabaseHelper(getBaseContext());
                    db = myDb.getWritableDatabase();
                    String selectQuery = "SELECT * FROM Login_Details  WHERE Username='" + usernameServer + "' and Password = '" + passwordServer + "'";
                    Cursor cursor = db.rawQuery(selectQuery, null);
                    if (cursor.getCount() != 0) {
                        while (cursor.moveToNext()) {
                            if (cursor.getString(4).equals(usernameServer) && passwordServer.equals(cursor.getString(5))) {
                                Intent intent = new Intent(LoginActivity.this, HomePage.class);
                                intent.putExtra("User_Id", cursor.getString(1));
                                new applicationClass().setUserId(cursor.getString(1));
                                intent.putExtra("Company_Customer_Id", cursor.getString(3));
                                editor = settings.edit();
                                editor.putString("Username", cursor.getString(4));
                                editor.putString("Password", cursor.getString(5));
                                editor.commit();
                                startActivity(intent);
                                finish();
                            }
                        }
                    } else
                        Toast.makeText(LoginActivity.this, "Username or Password Incorrect", Toast.LENGTH_LONG).show();
                    cursor.close();
                    db.close();
                } catch (Exception e) {
                    Log.d("Login253", "ERROR==" + e);
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error code: Login253", Toast.LENGTH_SHORT).show();
                }

            } else {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Access Not Granted", Toast.LENGTH_SHORT).show();

            }
        }

    }
*/

    public void loginShared() {

        try {

            if (sharedUserName == null) {
            } else {
                myDb = new DatabaseHelper(getBaseContext());
                db = myDb.getWritableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM Login_Details  WHERE Username='" + sharedUserName + "' and Password = '" + sharedPassword + "'", null);
                while (cursor.moveToNext()) {
                    if (cursor.getString(4).equals(sharedUserName) && sharedPassword.equals(cursor.getString(5))) {
                        Intent intent = new Intent(LoginActivity.this, HomePage.class);
                        intent.putExtra("User_Id", cursor.getString(1));
                        new applicationClass().setUserId(cursor.getString(1));
                        intent.putExtra("Company_Customer_Id", cursor.getString(3));
                        intent.putExtra("Asset_View", cursor.getInt(cursor.getColumnIndex("Asset_View")));
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Username and Password not matching.", Toast.LENGTH_SHORT).show();
                    }
                }
                cursor.close();
                db.close();
            }
        } catch (Exception e) {
            Log.d("Login286", "ERROR==" + e);
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error code: Login286", Toast.LENGTH_SHORT).show();
        }
    }

    private class DownloadWebPageTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Checking internet connectivity. Please Wait..");
            pDialog.setIndeterminate(false);
            pDialog.setMax(100);
            pDialog.setProgress(0);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // we use the OkHttp library from https://github.com/square/okhttp

            try {
                //URL url = new URL("https://www.google.com/");
                URL url = new URL("https://google.com/");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                //SSLCerts.sslreq();
                connection.setConnectTimeout(1500);
                connection.connect();
                //internetConnection = connection.getResponseCode() == 200;
                if (connection.getResponseCode() == 200) {
                    internetConnection = true;
                } else {
                    internetConnection = false;
                }
            } catch (Exception E) {
                E.printStackTrace();
                internetConnection = false;
            }

            return internetConnection;


        }

        @Override
        protected void onPostExecute(Boolean result) {

            if (result == true) {
                pDialog.dismiss();
                //new getUserDetails().execute();
                LoginFunction();
            } else {
                pDialog.dismiss();
                int color;
                color = Color.RED;
                snackbar = Snackbar
                        .make(findViewById(R.id.relativeLayoutLogin), "You are not connected to internet", Snackbar.LENGTH_LONG);
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(color);
                snackbar.show();
                pDialog.dismiss();
            }
        }


    }


    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //createPackage(getApplicationContext());
                //requestStoragePermission();
            } else {
                //requestStoragePermission();
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            if(permissionDenided == false){

            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Permission Denied")
                    .setMessage(msg)
                    .setPositiveButton("Re-TRY",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //db = myDb.getWritableDatabase();
                                    requestStoragePermission();

                                }
                            }).setNegativeButton("I'M SURE",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    dialog.dismiss();
                                    permissionDenided = true;
                                }
                            }).create();
            dialog.show();
            }

            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.

        }
    }

    public void createPackage(Context cntxt,Bitmap image,String ImageName){
        try {
            Log.d("Test","Folder crerewrefjgkj");
            File mydir = cntxt.getDir("images", Context.MODE_PRIVATE); //Creating an internal dir;
            if(!mydir.exists()){
                mydir.mkdirs();
            }
            String currentDBPath = "//data//" + cntxt.getPackageName()
                    + "//app_images//";
            Log.d("TestUrl",currentDBPath);
            File checkfolder = new File(currentDBPath);
            Log.d("TestUrl",checkfolder.exists()+"");

            File fileWithinMyDir = new File(mydir, ImageName);

            if(!fileWithinMyDir.exists()){

            FileOutputStream out = new FileOutputStream(fileWithinMyDir);

            image.compress(Bitmap.CompressFormat.JPEG, 90, out);

            out.flush();

            out.close();
            }else {
                Log.d("Image Exists",fileWithinMyDir.exists()+"");
            }


            //File fileWithinMyDir = new File(mydir, "myfile");
            /*File fileWithinMyDir = new File(mydir, "myfile"); //Getting a file within the dir.
            FileOutputStream out = new FileOutputStream(fileWithinMyDir); *///Use the stream as usual to write into the file.
            //Toast.makeText(cntxt,"Folder mydir created",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(LastImageName.equalsIgnoreCase(ImageName)){
            intentHomepage();
        }
    }

    private void intentHomepage(){
        try {
            if (pDialog.isShowing())
                pDialog.dismiss();
            myDb = new DatabaseHelper(getBaseContext());
            db = myDb.getWritableDatabase();
            String selectQuery = "SELECT * FROM Login_Details  WHERE Username='" + usernameServer + "' and Password = '" + passwordServer + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() != 0) {
                while (cursor.moveToNext()) {
                    if (cursor.getString(4).equals(usernameServer) && passwordServer.equals(cursor.getString(5))) {
                        Intent intent = new Intent(LoginActivity.this, HomePage.class);
                        intent.putExtra("User_Id", cursor.getString(1));
                        new applicationClass().setUserId(cursor.getString(1));
                        intent.putExtra("Company_Customer_Id", cursor.getString(3));
                        editor = settings.edit();
                        editor.putString("Username", cursor.getString(4));
                        editor.putString("Password", cursor.getString(5));
                        editor.commit();
                        startActivity(intent);
                        finish();
                    }
                }
            } else
                Toast.makeText(LoginActivity.this, "Username or Password Incorrect", Toast.LENGTH_LONG).show();
            cursor.close();
            db.close();
        } catch (Exception e) {
            Log.d("Login253", "ERROR==" + e);
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error code: Login253", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkImages(String Image){
        boolean imageFound = false;
        File[] files;
        File mydir = getApplicationContext().getDir("images", Context.MODE_PRIVATE); //Creating an internal dir;
        if(mydir.exists()){
            files = mydir.listFiles();
            //Log.d("Files", "Size: " + files.length);
            for (int i = 0; i < files.length; i++)
            {
                if(files[i].getName().equals(Image)){
                    imageFound = true;
                    break;
                }else {
                    imageFound = false;
                }

            }

        }else {
            imageFound = false;
        }
        if(LastImageName.equalsIgnoreCase(Image)){
            intentHomepage();
        }

        return imageFound;
    }
}
