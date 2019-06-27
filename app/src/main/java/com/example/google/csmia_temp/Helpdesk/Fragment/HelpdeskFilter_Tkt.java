package com.example.google.csmia_temp.Helpdesk.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import com.example.google.csmia_temp.Helpdesk.Activity.Filter_Ticket;
import com.example.google.csmia_temp.Helpdesk.Activity.NewTicketActivity;
import com.example.google.csmia_temp.Helpdesk.Activity.TicketDetailsHk;
import com.example.google.csmia_temp.Helpdesk.Adapter.HelpdeskTicketAdapter;
import com.example.google.csmia_temp.Helpdesk.HelpDeskClient;
import com.example.google.csmia_temp.Helpdesk.HelpdeskApi;
import com.example.google.csmia_temp.Helpdesk.Model.HkTicket;
import com.example.google.csmia_temp.Helpdesk.Model.Ticket;
import com.example.google.csmia_temp.Helpdesk.Model.Ticket_Log;
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
import retrofit2.Retrofit;

public class HelpdeskFilter_Tkt extends Fragment{
    static final boolean LOG = new applicationClass().checkLog();
    private static final String TAG = NewTicketActivity.class.getSimpleName();
    NfcAdapter mNfcAdapter;
    NFC nfc;
    static String NfcCode="";
    RecyclerView rc;
    HelpdeskTicketAdapter helpdeskticketAdapter;
    ArrayList<HkTicket> hkticketArrayList;
    ArrayList<HkTicket> hkFilterNullDataList;
    ProgressDialog progressDialog;
    LinearLayout assetLinearLayout;
    String Scan_Type="",User_Id="",date="",assetsId,updatedtime,assetsCode,assetsName,Asset_Status_Id,Asset_Id,User_Group_Id,Group_Name,building_code,building_name,floor_code,floor_name,room_area,Site_Name,Site_URL
            ,Asset_Location, Building_Id, Floor_Id,Room_Id,User_Group_Name;
    DatabaseHelper myDb;
    SharedPreferences settings;
    LinearLayout ll_nfc;
    SQLiteDatabase db;
    String str_service_are_nm, Asset_Name;
    boolean checknfcopen = false;
    String Site_DB_Name,Master_DB,Site_Id;
    ArrayList<Ticket_Log> ticketLogArrayList;
    Boolean isVisible = false, canceldialogue = false,internetConnection = false;
    Context context;
    LinearLayout ticketLinearLayout;
    ArrayList<HkTicket> hkTicketList ;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter__ticket, container, false);
        context=getActivity();
        myDb = new DatabaseHelper(getActivity());
        db = myDb.getWritableDatabase();
        User_Group_Id = myDb.UserGroupId(User_Id);
        User_Group_Name = myDb.UserGroupName(User_Group_Id);
        Scan_Type = myDb.ScanType(User_Id);
        ticketLinearLayout = view.findViewById(R.id.ticketLinearLayout);
        ll_nfc = (LinearLayout)view.findViewById(R.id.ll_nfc);
        rc = (RecyclerView) view.findViewById(R.id.recyclerview);
        ll_nfc.setVisibility(View.VISIBLE);
        rc.setVisibility(View.GONE);
        NFC();
        settings = PreferenceManager.getDefaultSharedPreferences(context);
        User_Id = settings.getString("userId", null);
        Master_DB = settings.getString("site_db_name",null);
        Site_Id = myDb.Site_Location_Id(User_Id);
        hkTicketList =new ArrayList<>();
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        if (isVisible){
            new NewTicketActivity().setNfcStatus(true);
        }
    }

    public void nfcData(String Value){
        NfcCode = Value;
        Log.d("getDetailsNfc",NfcCode);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void getDetails(String AssetCode, Context context){
        if (context!=null) {
            DatabaseHelper myDb = new DatabaseHelper(context);
            SQLiteDatabase db = myDb.getWritableDatabase();
            Cursor cursor1 = db.rawQuery("Select * from asset_Details where Asset_Code ='"+AssetCode+"'",null);
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

            Cursor cursor = db.rawQuery( "SELECT building_code,building_name,floor_code,floor_name,room_area FROM Asset_Location Where Asset_Id ='"+assetsId+"' " ,null);
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
            FetchTickets(context,building_name,floor_name,room_area);
            NfcCode = "";
        }else {
            Log.d("getActivty",""+getActivity());
        }
    }
    public class DataDownload extends AsyncTask<Void, Void, Boolean> {
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

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
                    getDetails(NfcCode,context);
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
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
    @Override
    public void onDetach() {
        super.onDetach();
        context = null;
    }
    public void show_pDialog() {
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void FetchTickets(final Context context, final String BuildingNm, final String FloorNm, final String RoomArea){
    /*    getDetails(NfcCode,context);*/
        /*f (isConnected()) {*/
        hkFilterNullDataList=new ArrayList<>();
            progressDialog = new ProgressDialog(context);
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
                        for (int i=0;i<hkticketArrayList.size();i++){
                            HkTicket hkTicket = hkticketArrayList.get(i);
                           if(hkTicket.getBuildingName()!=null || hkTicket.getFloorName()!=null || hkTicket.getRoomArea()!=null) {
                               hkFilterNullDataList.add(hkTicket);
                           }
                        }
                        Collections.reverse(hkFilterNullDataList);
                        for (int i=0;i<hkFilterNullDataList.size();i++){
                            HkTicket hkTicket = hkFilterNullDataList.get(i);

                         if(hkFilterNullDataList.get(i).getBuildingName().equalsIgnoreCase(BuildingNm)
                               && hkFilterNullDataList.get(i).getFloorName().equalsIgnoreCase(FloorNm)
                               && hkFilterNullDataList.get(i).getRoomArea().equalsIgnoreCase(RoomArea)){

                             hkTicketList.add(hkTicket);
                         }
                        }
                        helpdeskticketAdapter = new HelpdeskTicketAdapter(context,hkTicketList);
                        rc.setHasFixedSize(true);
                        Log.d("response",""+response);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true);
                        rc.setLayoutManager(layoutManager);
                        layoutManager.setReverseLayout(true);
                        layoutManager.setStackFromEnd(true);
                        rc.setItemAnimator(new DefaultItemAnimator());
                        rc.setAdapter(helpdeskticketAdapter);
                        helpdeskticketAdapter.notifyDataSetChanged();
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
                            Snackbar snackbar = Snackbar.make(assetLinearLayout, "Internet Connection Lost.Synchronization failed.", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }
                }
                @Override
                public void onFailure(Call<ArrayList<HkTicket>> call, Throwable t) {
                    progressDialog.dismiss();
                    Snackbar snackbar = Snackbar.make(ll_nfc, "Error !! It seems there is not proper internet connection", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            });
       /* } else {
            Snackbar snackbar = Snackbar.make(ll_nfc, "Please check your internet connection !!", Snackbar.LENGTH_LONG);
            snackbar.show();
        }*/
    }
    public boolean isConnected() {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null) return true;
        else return false;
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
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onResume() {
        super.onResume();
        context=getActivity();
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
    public void clickData(){

        rc.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rc, new RecyclerTouchListener.ClickListener() {
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
                Intent intent = new Intent(context, TicketDetailsHk.class);
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
}
