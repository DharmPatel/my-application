package com.example.google.csmia_temp.Helpdesk.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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
import android.widget.Toast;

import com.example.google.csmia_temp.DatabaseHelper;
import com.example.google.csmia_temp.Helpdesk.Activity.NewTicketActivity;
import com.example.google.csmia_temp.Helpdesk.Activity.TicketDetailsHk;
import com.example.google.csmia_temp.Helpdesk.Adapter.HelpdeskTicketAdapter;
import com.example.google.csmia_temp.Helpdesk.HelpDeskClient;
import com.example.google.csmia_temp.Helpdesk.HelpdeskApi;
import com.example.google.csmia_temp.Helpdesk.Model.HkTicket;
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

public class HelpdeskAll_Tkt extends Fragment {
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
    LinearLayout linearLayout;
    Boolean isVisible = false;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all__ticket, container, false);
        linearLayout = view.findViewById(R.id.main_layout);
        rc = (RecyclerView) view.findViewById(R.id.AllTicketrecyclerview);
        assetLinearLayout= (LinearLayout) view.findViewById(R.id.main_layout);
        hkticketArrayList=new ArrayList<>();
        ticketLogArrayList=new ArrayList<>();
        myDb = new DatabaseHelper(getActivity());
        settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        User_Id = settings.getString("userId", null);
        Scan_Type = myDb.ScanType(User_Id);
        Master_DB = settings.getString("site_db_name",null);
        Log.d("Master_DB",""+Master_DB);
//        site_details();
        Site_Id = myDb.Site_Location_Id(User_Id);
//        init();

        return view;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void FetchTickets(){
        progressDialog = new ProgressDialog(getActivity());
        show_pDialog();
        final Retrofit retrofit = HelpDeskClient.getClient();
        final HelpdeskApi helpdeskApi = retrofit.create(HelpdeskApi.class);
        Call<ArrayList<HkTicket>> call;
        call = helpdeskApi.getHelpdkTkt(Site_Id,Master_DB,"SM",User_Id);
        Log.d("Master_DB2",""+Master_DB);
        call.enqueue(new Callback<ArrayList<HkTicket>>() {
            @Override
            public void onResponse(Call<ArrayList<HkTicket>> call, retrofit2.Response<ArrayList<HkTicket>> response) {
                progressDialog.dismiss();
                if(response.code()==200){
                    hkticketArrayList=response.body();
                    Collections.reverse(hkticketArrayList);
                    helpdeskticketAdapter = new HelpdeskTicketAdapter(getActivity(), hkticketArrayList  );
                    rc.setHasFixedSize(true);
                    Log.d("response",""+response);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
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
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        if(isVisible){
            new NewTicketActivity().setNfcStatus(false);
            if (isConnected()) {
                FetchTickets();
            } else {
                Snackbar snackbar = Snackbar.make(assetLinearLayout, "Please check your internet connection !!", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
        Log.d("Test Visible",isVisible+" "+isVisibleToUser);
    }

    public void show_pDialog() {
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();
    }
    public void clickData() {
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
                Intent intent = new Intent(getActivity(), TicketDetailsHk.class);
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
    public boolean isConnected() {
        ConnectivityManager connectivity = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }
    /*public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null) return true;
        else return false;
    }*/
   /* @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onRestart() {
        super.onRestart();
        if (isConnected()) {
            FetchTickets();
        } else {
            Snackbar snackbar = Snackbar.make(assetLinearLayout, "Please check your internet connection !!", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }*/
}
