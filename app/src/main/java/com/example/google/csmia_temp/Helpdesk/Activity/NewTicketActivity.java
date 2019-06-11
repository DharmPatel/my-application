package com.example.google.csmia_temp.Helpdesk.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.NdefMessage;
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
import com.example.google.csmia_temp.Helpdesk.Adapter.HelpdeskAdapter;
import com.example.google.csmia_temp.Helpdesk.HelpDeskClient;
import com.example.google.csmia_temp.Helpdesk.HelpdeskApi;
import com.example.google.csmia_temp.Helpdesk.Model.Ticket;
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
    HelpdeskAdapter helpdeskAdapter;
    ArrayList<Ticket> ticketArrayList;
    ProgressDialog progressDialog;
    LinearLayout assetLinearLayout;
    String Scan_Type="",User_Id="",date="",assetsId,updatedtime,assetsCode,assetsName,Asset_Status_Id,Asset_Id,User_Group_Id,Group_Name,building_code,building_name,floor_code,floor_name,room_area,Site_Name,Site_URL
    ,Asset_Location, Building_Id, Floor_Id,Room_Id;
    DatabaseHelper myDb;
    SharedPreferences settings;
    LinearLayout ll_nfc;
    SQLiteDatabase db;
    boolean checknfcopen = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_list);
        ticketArrayList=new ArrayList<>();
        myDb = new DatabaseHelper(this);
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        User_Id = settings.getString("userId", null);
        Scan_Type = myDb.ScanType(User_Id);
        init();
        NFC();

    }

    private void init(){
        assetLinearLayout= (LinearLayout) findViewById(R.id.ticketLinearLayout);
        rc = (RecyclerView) findViewById(R.id.recyclerview);
        ll_nfc= (LinearLayout) findViewById(R.id.ll_nfc);
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
    @Override
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
    }
    @Override
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
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
                        FetchTickets();
                    }
            }
        }catch (Exception e){
            if(LOG){Log.d(TAG,"aa227"+"ERROR==" + e);}
            Toast.makeText(getApplicationContext(), "Error code: aa227", Toast.LENGTH_SHORT).show();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void FetchTickets(){
        progressDialog = new ProgressDialog(NewTicketActivity.this);
        show_pDialog();
         final Retrofit retrofit = HelpDeskClient.getClient();
         final HelpdeskApi helpdeskApi = retrofit.create(HelpdeskApi.class);
        Call<ArrayList<Ticket>> call;
        if(Group_Name.equalsIgnoreCase("Civil") || Group_Name.equalsIgnoreCase("Electrical")){
            call = helpdeskApi.getTicket1(building_name,floor_code,room_area,assetsName,Group_Name);
        }else {
            call = helpdeskApi.getTicket(building_name,floor_code,room_area,assetsName);
        }
        call.enqueue(new Callback<ArrayList<Ticket>>() {
            @Override
            public void onResponse(Call<ArrayList<Ticket>> call, retrofit2.Response<ArrayList<Ticket>> response) {
                progressDialog.dismiss();
                if(response.code()==200){
                    ticketArrayList=response.body();
                    Collections.reverse(ticketArrayList);
                    helpdeskAdapter = new HelpdeskAdapter(NewTicketActivity.this, ticketArrayList  );
                    rc.setHasFixedSize(true);
                    Log.d("response",""+response);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(NewTicketActivity.this, LinearLayoutManager.VERTICAL, true);
                    rc.setLayoutManager(layoutManager);
                    layoutManager.setReverseLayout(true);
                    layoutManager.setStackFromEnd(true);
                    rc.setItemAnimator(new DefaultItemAnimator());
                    rc.setAdapter(helpdeskAdapter);
                    helpdeskAdapter.notifyDataSetChanged();
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
                        Toast.makeText(getApplicationContext(), "Internet Connection Lost.Synchronization failed." , Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Ticket>> call, Throwable t) {
                progressDialog.dismiss();

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
                Ticket ticket = ticketArrayList.get(position);
                String str_ticket_id = ticket.getTICKETID();
                String str_dept_nm = ticket.getDEPARTMENTNAME();
                String str_site_nm = ticket.getSITENAME();
                String str_location_name = ticket.getLOCATIONNAME();
                String str_sub=ticket.getSUBLOCATIONNAME();
                String str_service_are_nm = ticket.getSERVICEAREANAME();
                String str_product_nm = ticket.getPRODUCTNAME();
                String str_component_nm = ticket.getCOMPONENTNAME();
                String str_problem_title = ticket.getPROBLEMTITLE();
                String str_risk_priority = ticket.getRISKPRIORITY();
                String str_create_on = ticket.getCREATEDON();
                String str_update_on = ticket.getUPDATEDON();
                String str_status = ticket.getSTATUS();
                String str_incident_type = ticket.getINCIDENTTYPE();
                String str_resolution_time = ticket.getRESOLUTIONTIME();
                String str_affected_party = ticket.getAffectedParty();
                String str_incident_description = ticket.getIncidentDescription();
                String str_updated_remarks = ticket.getUpdateRemarks();
                String str_created_by = ticket.getCREATEDBY();
                String str_updated_by = ticket.getUPDATEDBY();

                Intent intent = new Intent(getApplicationContext(), TicketDetails.class);
               /* intent.putParcelableArrayListExtra("data", (ArrayList<? extends Parcelable>) ticketArrayList);
                intent.putExtra("pos",position);*/
               intent.putExtra("Group_Name",Group_Name);
                intent.putExtra("id", str_ticket_id);
                intent.putExtra("dept", str_dept_nm);
                intent.putExtra("site_nm", str_site_nm);
                intent.putExtra("ocation_name", str_location_name);
                intent.putExtra("sub_loc",str_sub);
                intent.putExtra("service_are_nm", str_service_are_nm);
                intent.putExtra("product_nm", str_product_nm);
                intent.putExtra("component_nm", str_component_nm);
                intent.putExtra("problem_title", str_problem_title);
                intent.putExtra("risk_priority", str_risk_priority);
                intent.putExtra("create_on", str_create_on);
                intent.putExtra("update_on", str_update_on);
                intent.putExtra("status", str_status);
                intent.putExtra("incident_type", str_incident_type);
                intent.putExtra("resolution_time", str_resolution_time);
                intent.putExtra("affected_party", str_affected_party);
                intent.putExtra("incident_description", str_incident_description);
                intent.putExtra("updated_remarks", str_updated_remarks);
                intent.putExtra("created_by", str_created_by);
                intent.putExtra("updated_by", str_updated_by);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }
}
