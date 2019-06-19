package com.example.google.csmia_temp.Helpdesk.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.google.csmia_temp.DatabaseHelper;
import com.example.google.csmia_temp.Helpdesk.Adapter.HelpdeskIMSAdapter;
import com.example.google.csmia_temp.Helpdesk.Adapter.HelpdeskTicketAdapter;
import com.example.google.csmia_temp.Helpdesk.HelpDeskClient;
import com.example.google.csmia_temp.Helpdesk.HelpdeskApi;
import com.example.google.csmia_temp.Helpdesk.Model.HkTicket;
import com.example.google.csmia_temp.Helpdesk.Model.Ticket;
import com.example.google.csmia_temp.Helpdesk.Model.TicketLog;
import com.example.google.csmia_temp.Helpdesk.Model.Ticket_Log;
import com.example.google.csmia_temp.NFC;
import com.example.google.csmia_temp.R;
import com.example.google.csmia_temp.RecyclerTouchListener;
import com.example.google.csmia_temp.applicationClass;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class NewTicketActivity extends AppCompatActivity {
    static final boolean LOG = new applicationClass().checkLog();
    private static final String TAG = NewTicketActivity.class.getSimpleName();
    NfcAdapter mNfcAdapter;
    NFC nfc;
    RecyclerView rc;
    HelpdeskTicketAdapter helpdeskticketAdapter;
    ArrayList<HkTicket> hkticketArrayList;
    ProgressDialog progressDialog;
    LinearLayout assetLinearLayout;
    String Scan_Type="",User_Id="",date="",assetsId,updatedtime,assetsCode,assetsName,Asset_Status_Id,Asset_Id,User_Group_Id,Group_Name,building_code,building_name,floor_code,floor_name,room_area,Site_Name,Site_URL
    ,Asset_Location, Building_Id, Floor_Id,Room_Id;
    DatabaseHelper myDb;
    SharedPreferences settings;
    LinearLayout ll_nfc;
    SQLiteDatabase db;
    String str_service_are_nm, Asset_Name;
    boolean checknfcopen = false;
    String Site_DB_Name,Master_DB,Site_Id;
    ArrayList<Ticket_Log> ticketLogArrayList;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_list);
        hkticketArrayList=new ArrayList<>();
        ticketLogArrayList=new ArrayList<>();
        myDb = new DatabaseHelper(this);
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        User_Id = settings.getString("userId", null);
        Scan_Type = myDb.ScanType(User_Id);
        Master_DB = settings.getString("site_db_name",null);
//        site_details();
        Site_Id = myDb.Site_Location_Id(User_Id);
        init();

        if (isNetworkConnected()) {
            FetchTickets();
        } else {
            Snackbar snackbar = Snackbar.make(assetLinearLayout, "Please check your internet connection !!", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }
    private void init(){
        assetLinearLayout= (LinearLayout) findViewById(R.id.ticketLinearLayout);
        rc = (RecyclerView) findViewById(R.id.recyclerview);

    }

    private void NFC(){
        try {
            if(LOG) Log.d(TAG,"Scan_Type"+Scan_Type);
            if(!Scan_Type.equals("QR")) {
                try {
                    nfc = new NFC();
                    nfc.onCreate();
                    mNfcAdapter = NfcAdapter.getDefaultAdapter(getApplicationContext());
                    if (mNfcAdapter == null) {
                        Toast.makeText(getApplicationContext(), "This device doesn't support NFC!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        if (!mNfcAdapter.isEnabled()) {
                            final Snackbar snackbar = Snackbar
                                    .make(assetLinearLayout, "NFC is disabled", Snackbar.LENGTH_LONG)
                                    .setAction("Change Setting.", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                                        }
                                    });

                            snackbar.setDuration(20000);
                            snackbar.show();
                        } else {
                         ll_nfc.setVisibility(View.VISIBLE);
                         rc.setVisibility(View.GONE);
                        //   Toast.makeText(getApplicationContext(), "Tap device with NFC tag", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error code: aa140", Toast.LENGTH_SHORT).show();
                }
            }else {
                if(LOG) Log.d(TAG,"Scan_Type"+Scan_Type);
            }
        } catch (Exception e) {
            if(LOG) Log.d(TAG,"aa158 ERROR=="+"" + e);
            Toast.makeText(getApplicationContext(), "Error code: aa158", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
 /*   @Override
    protected void onResume() {
        super.onResume();
        try {
            if(!Scan_Type.equals("QR")) {
                nfc.setupForegroundDispatch(this, mNfcAdapter);
            }
        } catch (Exception e) {
            if(LOG) Log.d(TAG,"aa186 ERROR=="+"" + e);
            Toast.makeText(getApplicationContext(), "Error code: aa186", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }*/
   /* @Override
    protected void onPause() {
        super.onPause();
        try {
            if(!Scan_Type.equals("QR")) {
                mNfcAdapter.disableForegroundDispatch(this);
            }
        } catch (Exception e) {
            if(LOG) Log.d(TAG,"aa199 ERROR=="+"" + e);
            Toast.makeText(getApplicationContext(), "Error code: aa199", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }*/
   /* @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        try {
            if(!Scan_Type.equals("QR")) {
                Parcelable[] parcelables = intent.getParcelableArrayExtra(mNfcAdapter.EXTRA_NDEF_MESSAGES);
                if(parcelables !=null && parcelables.length>0)
                    nfc.readnfc((NdefMessage)parcelables[0]);
                if(LOG) {Log.d(TAG, "QR " + nfc.tagcontent);
                }
                if(checknfcopen==false){
                    SQLiteDatabase sqLiteDatabase=myDb.getWritableDatabase();
                    Cursor cursor1 = sqLiteDatabase.rawQuery("Select * from asset_Details where Asset_Code ='"+nfc.tagcontent+"'",null);

                    if(cursor1.getCount()==0){
                        Toast.makeText(getApplicationContext(),"No asset found for this Assetcode",Toast.LENGTH_LONG).show();
                    }else{
                        if (cursor1.moveToFirst()) {
                            do {
                                assetsId = cursor1.getString(cursor1.getColumnIndex("Asset_Id"));
                                assetsName = cursor1.getString(cursor1.getColumnIndex("Asset_Name"));
                                Building_Id = cursor1.getString(cursor1.getColumnIndex("Building_Id"));
                                Floor_Id =  cursor1.getString(cursor1.getColumnIndex("Floor_Id"));
                                Room_Id= cursor1.getString(cursor1.getColumnIndex("Room_Id"));
                            }
                            while (cursor1.moveToNext());
                        }
                    }

                    User_Group_Id = myDb.UserGroupId(User_Id);

                    Cursor cursor = sqLiteDatabase.rawQuery( "SELECT Group_Name FROM User_Group Where User_Group_Id IN ("+User_Group_Id+")",null);
                    if(cursor.getCount()==0){
                        Toast.makeText(getApplicationContext(),"No Group found for this user. ",Toast.LENGTH_LONG).show();
                    }else{
                        if (cursor.moveToFirst()) {
                            do {
                                Group_Name = cursor.getString(cursor.getColumnIndex("Group_Name"));

                            }while (cursor.moveToNext());
                        }
                    }

                    cursor = sqLiteDatabase.rawQuery( "SELECT building_code,building_name,floor_code,floor_name,room_area FROM Asset_Location Where Asset_Id ='"+assetsId+"' " ,null);
                    if(cursor.getCount()==0){
                        Toast.makeText(getApplicationContext(),"No location found for this Assetcode",Toast.LENGTH_LONG).show();
                    }else{
                        if (cursor.moveToFirst()) {
                            do {
                                building_code = cursor.getString(cursor.getColumnIndex("building_code"));
                                building_name = cursor.getString(cursor.getColumnIndex("building_name"));
                                floor_code = cursor.getString(cursor.getColumnIndex("floor_code"));
                                floor_name = cursor.getString(cursor.getColumnIndex("floor_name"));
                                room_area = cursor.getString(cursor.getColumnIndex("room_area"));
                            }while (cursor.moveToNext());
                        }
                    }
                    cursor.close();
                    sqLiteDatabase.close();
                    ll_nfc.setVisibility(View.GONE);
                    rc.setVisibility(View.VISIBLE);

                    }
            }
        }catch (Exception e){
            if(LOG){Log.d(TAG,"aa227"+"ERROR==" + e);}
            Toast.makeText(getApplicationContext(), "Error code: aa227", Toast.LENGTH_SHORT).show();
        }
    }
*/
    private void site_details() {
        db=myDb.getWritableDatabase();
        String Site_Location_Id=myDb.Site_Location_Id(User_Id);
        String query_priority_id = "SELECT Site_DB_Name, Master_DB FROM Site_Details WHERE  Auto_Id='"+Site_Location_Id+"'";
        Cursor cursor = db.rawQuery(query_priority_id, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                try {
                    do {
                        Site_DB_Name = cursor.getString(cursor.getColumnIndex("Site_DB_Name"));
                        Master_DB = cursor.getString(cursor.getColumnIndex("Master_DB"));
                    } while (cursor.moveToNext());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        cursor.close();
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void FetchTickets(){
        progressDialog = new ProgressDialog(NewTicketActivity.this);
        show_pDialog();
         final Retrofit retrofit = HelpDeskClient.getClient();
         final HelpdeskApi helpdeskApi = retrofit.create(HelpdeskApi.class);
         Call<ArrayList<HkTicket>> call;
         call = helpdeskApi.getHelpdkTkt(Site_Id,Master_DB,"SM",User_Id);
         call.enqueue(new Callback<ArrayList<HkTicket>>() {
            @Override
            public void onResponse(Call<ArrayList<HkTicket>> call, retrofit2.Response<ArrayList<HkTicket>> response) {
                progressDialog.dismiss();
                if(response.code()==200){
                    hkticketArrayList=response.body();
                    Collections.reverse(hkticketArrayList);
                    helpdeskticketAdapter = new HelpdeskTicketAdapter(NewTicketActivity.this, hkticketArrayList  );
                    rc.setHasFixedSize(true);
                    Log.d("response",""+response);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(NewTicketActivity.this, LinearLayoutManager.VERTICAL, true);
                    rc.setLayoutManager(layoutManager);
                    layoutManager.setReverseLayout(true);
                    layoutManager.setStackFromEnd(true);
                    rc.setItemAnimator(new DefaultItemAnimator());
                    rc.setAdapter(helpdeskticketAdapter);
                    helpdeskticketAdapter.notifyDataSetChanged();
                    clickData();
                } else if(response.code()==404){
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_SHORT).show();
                } else if(response.code()==500){
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end" , Toast.LENGTH_SHORT).show();
                } else {
                    {
                        progressDialog.dismiss();
                        Snackbar snackbar = Snackbar.make(assetLinearLayout, "Internet Connection Lost.Synchronization failed.", Snackbar.LENGTH_LONG);
                        snackbar.show();
/*
                        Toast.makeText(getApplicationContext(), "Internet Connection Lost.Synchronization failed." , Toast.LENGTH_SHORT).show();
*/
                    }
                }
            }
            @Override
            public void onFailure(Call<ArrayList<HkTicket>> call, Throwable t) {
                progressDialog.dismiss();
                Snackbar snackbar = Snackbar.make(assetLinearLayout, "Error !! It seems there is not proper internet connection", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });


    }

    public void show_pDialog() {
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();
    }
    public void clickData() {
        rc.addOnItemTouchListener(new RecyclerTouchListener(this, rc, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                HkTicket ticket = hkticketArrayList.get(position);
                String str_ticket_id = ticket.getTicketCode();
                String str_dept_nm = ticket.getLevel();
                String str_site_nm = ticket.getBuildingName();
                String str_location_name = ticket.getFloorName();
                String str_sub=ticket.getRoomArea();
                 str_service_are_nm = ticket.getAssetId();
                String str_product_nm = ticket.getCategoryName();
                String str_component_nm = ticket.getSubCategoryName();
                String str_problem_title = ticket.getIssues();
                String str_risk_priority = "NA";
                String str_create_on = ticket.getCreatedAt();
                String str_update_on = "NA";
                String str_status = ticket.getStatus();
                String str_user_type  =ticket.getUserType();
                String User_Group_Name= (String) ticket.getGroupName();
                String str_tkt_sr_id=ticket.getId();
               /* String str_incident_type = "NA";*/
              /*  String str_resolution_time = ticket.getRESOLUTIONTIME();
                String str_affected_party = ticket.getAffectedParty();*/
                String str_incident_description = ticket.getDesc();
               /* String str_updated_remarks = ticket.getUpdateRemarks();*/
                String str_created_by = ticket.getName();
               /* String str_updated_by = ticket.getUPDATEDBY();*/
               ticketLogArrayList=ticket.getTicketLog();
                Intent intent = new Intent(getApplicationContext(), TicketDetailsHk.class);
                intent.putExtra("Group_Name",User_Group_Name);
                intent.putExtra("id", str_ticket_id);
                intent.putExtra("dept", str_dept_nm);
                intent.putExtra("site_nm", str_site_nm);
                intent.putExtra("ocation_name", str_location_name);
                intent.putExtra("sub_loc",str_sub);
                intent.putExtra("service_are_nm", setAssetsName());
                intent.putExtra("product_nm", str_product_nm);
                intent.putExtra("component_nm", str_component_nm);
                intent.putExtra("problem_title", str_problem_title);
                intent.putExtra("risk_priority", str_risk_priority);
                intent.putExtra("create_on", str_create_on);
                intent.putExtra("update_on", str_update_on);
                intent.putExtra("status", str_status);
                intent.putExtra("user_type",str_user_type);
               /* intent.putExtra("incident_type", str_incident_type);
                intent.putExtra("resolution_time", str_resolution_time);
                intent.putExtra("affected_party", str_affected_party);*/
                intent.putExtra("incident_description", str_incident_description);
       /*         intent.putExtra("updated_remarks", str_updated_remarks);*/
                intent.putExtra("created_by", str_created_by);
               /* intent.putExtra("updated_by", str_updated_by);*/
               intent.putExtra("tktcode",str_tkt_sr_id);
                intent.putParcelableArrayListExtra("ticketLogList", ticketLogArrayList);
                startActivity(intent);
            }
            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }
    private String setAssetsName(){
        db=myDb.getWritableDatabase();
//    ("CREATE TABLE Asset_Details (Id INTEGER PRIMARY KEY ,Asset_Id TEXT,Site_Location_Id TEXT,Asset_Code TEXT,Asset_Name TEXT,Asset_Location TEXT,Building_Id TEXT,Floor_Id TEXT,Room_Id TEXT,Asset_Status_Id TEXT,Asset_Type TEXT,Status TEXT,Manual_Time TEXT, Asset_Update_Time TEXT,UpdatedStatus TEXT)");
    String query_usergroups = "SELECT Asset_Name FROM Asset_Details WHERE Asset_Id ='" + str_service_are_nm + "'";
            Cursor cursor = db.rawQuery(query_usergroups, null);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    try {
                        do {
                            Asset_Name = cursor.getString(cursor.getColumnIndex("Asset_Name"));
                        } while (cursor.moveToNext());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return Asset_Name;
    }
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null) return true;
        else return false;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onRestart() {
        super.onRestart();
        if (isNetworkConnected()) {
            FetchTickets();
        } else {
            Snackbar snackbar = Snackbar.make(assetLinearLayout, "Please check your internet connection !!", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }
}
