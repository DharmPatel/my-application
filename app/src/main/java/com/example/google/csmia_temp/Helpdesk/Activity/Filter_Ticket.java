package com.example.google.csmia_temp.Helpdesk.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.google.csmia_temp.DatabaseHelper;
import com.example.google.csmia_temp.Helpdesk.Adapter.HelpdeskIMSAdapter;
import com.example.google.csmia_temp.Helpdesk.HelpDeskClient;
import com.example.google.csmia_temp.Helpdesk.HelpdeskApi;
import com.example.google.csmia_temp.Helpdesk.Model.Ticket;
import com.example.google.csmia_temp.NFC;
import com.example.google.csmia_temp.R;
import com.example.google.csmia_temp.RecyclerTouchListener;
import com.example.google.csmia_temp.applicationClass;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Filter_Ticket extends Fragment {
    private static final String TAG = Filter_Ticket.class.getSimpleName();
    static final boolean LOG = new applicationClass().checkLog();
    DatabaseHelper myDb;
    SQLiteDatabase db;
    SharedPreferences settings;
    String Scan_Type,User_Id,User_Group_Id, User_Group_Name;
    static String NfcCode="";
    String assetsId,assetsName,Building_Id,Floor_Id,Room_Id,building_code,building_name,floor_code,floor_name,room_area;
    NfcAdapter mNfcAdapter;
    NFC nfc;
    LinearLayout ll_nfc;
    RecyclerView rc;
    ArrayList<Ticket> ticketArrayList;
    HelpdeskIMSAdapter helpdeskIMSAdapter;
    ProgressDialog progressDialog;
    public boolean internetConnection = false, canceldialogue = false;;
    LinearLayout ticketLinearLayout;
    Boolean isVisible = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter__ticket, container, false);
        myDb = new DatabaseHelper(getActivity());
        db = myDb.getWritableDatabase();
        settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        User_Id = settings.getString("userId", null);
        User_Group_Id = myDb.UserGroupId(User_Id);
        User_Group_Name = myDb.UserGroupName(User_Group_Id);
        Scan_Type = myDb.ScanType(User_Id);

        ticketLinearLayout = view.findViewById(R.id.ticketLinearLayout);
        ll_nfc = (LinearLayout)view.findViewById(R.id.ll_nfc);
        rc = (RecyclerView) view.findViewById(R.id.recyclerview);
        ll_nfc.setVisibility(View.VISIBLE);
        rc.setVisibility(View.GONE);
        NFC();
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        if (isVisible){
            new TicketTabActivity().setNfcStatus(true);
        }

    }

    public void nfcData(String Value){
        NfcCode = Value;
        Log.d("getDetailsNfc",NfcCode);
    }

    private void NFC(){
        try {
            if(LOG) Log.d(TAG,"Scan_Type"+Scan_Type);
            if(!Scan_Type.equals("QR")) {
                try {
                    nfc = new NFC();
                    nfc.onCreate();
                    mNfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());
                    if (mNfcAdapter == null) {
                        Toast.makeText(getActivity(), "This device doesn't support NFC!", Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    } else {
                        if (!mNfcAdapter.isEnabled()) {
                            Toast.makeText(getActivity(),"NFC is disabled.",Toast.LENGTH_SHORT).show();
                        } else {
                            //Toast.makeText(getActivity(), "Tap device with NFC tag", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error code: aa140", Toast.LENGTH_SHORT).show();
                }
            }else {
                if(LOG) Log.d(TAG,"Scan_Type"+Scan_Type);
            }
        } catch (Exception e) {
            if(LOG) Log.d(TAG,"aa158 ERROR=="+"" + e);
            Toast.makeText(getActivity(), "Error code: aa158", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            if(!Scan_Type.equals("QR")){
                try {
                    nfc.stopForegroundDispatch(getActivity(), mNfcAdapter);
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("aa334","ERROR==" + e);
                    Toast.makeText(getActivity(), "Error code: aa334", Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.d("LOG3", e.getMessage());
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        try {
            if(!Scan_Type.equals("QR")){
                try {
                    nfc.setupForegroundDispatch(getActivity(), mNfcAdapter);
                    Log.d("NfcCodeHere",NfcCode);
                    if (NfcCode == null || NfcCode.equals("")){
                        ll_nfc.setVisibility(View.VISIBLE);
                        rc.setVisibility(View.GONE);
                    }else {
                        ll_nfc.setVisibility(View.GONE);
                        rc.setVisibility(View.VISIBLE);
                        new DataDownload().execute();
                    }



                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("NfcErrorOnResume",e.getMessage());
                    Log.d("aa190","ERROR==" + e);
                    Toast.makeText(getActivity(), "Error code: aa190", Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.d("LOG1", e.getMessage());
        }
    }

    private class DataDownload extends AsyncTask<Void, Void, Boolean> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Checking internet connectivity. Please Wait..");
            progressDialog.setIndeterminate(false);
            progressDialog.setMax(100);
            progressDialog.setProgress(0);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                //URL url = new URL("https://www.google.com/");
                URL url = new URL("https://google.com/");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
              //  SSLCerts.sslreq();
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
            super.onPostExecute(result);
            if (result) {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // this code will be executed after 2 seconds
                        progressDialog.setCancelable(true);
                        canceldialogue = true;
                    }
                }, 10000);
                progressDialog.dismiss();
                if(!canceldialogue) {
                    getDetails(NfcCode);
                }
            } else {
                progressDialog.dismiss();
                //Toast.makeText(getActivity(),"You are not connected to internet",Toast.LENGTH_SHORT).show();
                int color;
                color = Color.RED;
                Snackbar snackbar = Snackbar
                        .make(ticketLinearLayout, "You are not connected to internet!!", Snackbar.LENGTH_LONG);
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(color);
                snackbar.show();

            }
        }
    }

   public void getDetails(String AssetCode){

        SQLiteDatabase sqLiteDatabase = myDb.getWritableDatabase();
       Cursor cursor1 = sqLiteDatabase.rawQuery("Select * from asset_Details where Asset_Code ='"+AssetCode+"'",null);
        Log.d("getDetailsFilter",cursor1.getCount()+" "+AssetCode);
       if(cursor1.getCount()==0){
           Toast.makeText(getActivity(),"No asset found for this Assetcode",Toast.LENGTH_LONG).show();
       }else{
           if (cursor1.getCount() > 0){
               Log.d("getDetailsFilter", "abc1: "+cursor1.getCount());
               if (cursor1.moveToFirst()) {
                   do {
                       assetsId = cursor1.getString(cursor1.getColumnIndex("Asset_Id"));
                       assetsName = cursor1.getString(cursor1.getColumnIndex("Asset_Name"));
                       Building_Id = cursor1.getString(cursor1.getColumnIndex("Building_Id"));
                       Floor_Id =  cursor1.getString(cursor1.getColumnIndex("Floor_Id"));
                       Room_Id= cursor1.getString(cursor1.getColumnIndex("Room_Id"));
                       Log.d("getDetailsFilter","InWhile: "+assetsId+" "+assetsName+" "+Building_Id+" "+Floor_Id+""+Room_Id);
                   }
                   while (cursor1.moveToNext());
               }
           }

       }

       Cursor cursor = sqLiteDatabase.rawQuery( "SELECT building_code,building_name,floor_code,floor_name,room_area FROM Asset_Location Where Asset_Id ='"+assetsId+"' " ,null);
       Log.d("getDetailsFilter","AnotherCursor: "+cursor.getCount()+" "+assetsId);
       if(cursor.getCount()==0){

           Toast.makeText(getActivity(),"No location found for this Assetcode",Toast.LENGTH_LONG).show();
       }else{
           if (cursor.moveToFirst()) {
               do {
                   building_code = cursor.getString(cursor.getColumnIndex("building_code"));
                   building_name = cursor.getString(cursor.getColumnIndex("building_name"));
                   floor_code = cursor.getString(cursor.getColumnIndex("floor_code"));
                   floor_name = cursor.getString(cursor.getColumnIndex("floor_name"));
                   room_area = cursor.getString(cursor.getColumnIndex("room_area"));
                   Log.d("getDetailsFilter","InWhile1: "+building_code+" "+floor_code+" "+room_area);
               }while (cursor.moveToNext());
           }
       }
       cursor.close();
       //db.close();
       fetchTicket();
        NfcCode = "";
   }

   public void fetchTicket(){
       progressDialog = new ProgressDialog(getActivity());
       progressDialog.setMessage("Fetching Data...");
       progressDialog.show();
       final Retrofit retrofit = HelpDeskClient.getClient();
       final HelpdeskApi helpdeskApi = retrofit.create(HelpdeskApi.class);
       Call<ArrayList<Ticket>> call;
       if(User_Group_Name.equalsIgnoreCase("Civil") || User_Group_Name.equalsIgnoreCase("Electrical")){
           call = helpdeskApi.getTicket(building_name,floor_code,room_area,assetsName,User_Group_Name);
       }else {
           call = helpdeskApi.getTicket(building_name,floor_code,room_area,assetsName);
       }
       call.enqueue(new Callback<ArrayList<Ticket>>() {
           @Override
           public void onResponse(Call<ArrayList<Ticket>> call, Response<ArrayList<Ticket>> response) {
               if(response.code()==200){
                   progressDialog.dismiss();
                   ticketArrayList=response.body();
                   Collections.reverse(ticketArrayList);
                   helpdeskIMSAdapter = new HelpdeskIMSAdapter(getActivity(), ticketArrayList  );
                   rc.setHasFixedSize(true);
                   Log.d("response_Filter_Ticket",""+response.body().toString());
                    if(response.body().toString().equals("[]")){
                        rc.setVisibility(View.GONE);
                        Toast.makeText(getActivity(),"No tickets found for this Location",Toast.LENGTH_SHORT).show();
                        ll_nfc.setVisibility(View.VISIBLE);
                    }
                   LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
                   rc.setLayoutManager(layoutManager);
                   layoutManager.setReverseLayout(true);
                   layoutManager.setStackFromEnd(true);
                   rc.setItemAnimator(new DefaultItemAnimator());
                   rc.setAdapter(helpdeskIMSAdapter);
                   helpdeskIMSAdapter.notifyDataSetChanged();
                   clickData();
               } else if(response.code()==404){
                   progressDialog.dismiss();
                   Toast.makeText(getActivity(), "Requested resource not found", Toast.LENGTH_SHORT).show();
               } else if(response.code()==500){
                   progressDialog.dismiss();
                   Toast.makeText(getActivity(), "Something went wrong at server end" , Toast.LENGTH_SHORT).show();
               } else {
                   {
                       progressDialog.dismiss();
                       Toast.makeText(getActivity(), "Internet Connection Lost.Synchronization failed." , Toast.LENGTH_SHORT).show();
                   }
               }
           }

           @Override
           public void onFailure(Call<ArrayList<Ticket>> call, Throwable t) {
               progressDialog.dismiss();
           }
       });
   }

   public void clickData(){

       rc.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rc, new RecyclerTouchListener.ClickListener() {
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

               Intent intent = new Intent(getActivity(), TicketDetails.class);
               intent.putExtra("Group_Name",User_Group_Name);
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
               intent.putExtra("user_type", "");

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



}
