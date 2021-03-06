package com.example.google.csmia_temp.Helpdesk.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.google.csmia_temp.DatabaseHelper;
import com.example.google.csmia_temp.Helpdesk.Adapter.HelpdeskIMSAdapter;
import com.example.google.csmia_temp.Helpdesk.HelpDeskClient;
import com.example.google.csmia_temp.Helpdesk.HelpdeskApi;
import com.example.google.csmia_temp.Helpdesk.Model.Ticket;
import com.example.google.csmia_temp.R;
import com.example.google.csmia_temp.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class HelpdeskTickets extends AppCompatActivity {
    ProgressDialog progressDialog;
    DatabaseHelper myDb;
    SQLiteDatabase db;
    SharedPreferences settings;
    String Site_DB_Name,Master_DB,Site_Id,User_Id;
    ArrayList<Ticket> ticketArrayList;
    HelpdeskIMSAdapter helpdeskIMSAdapter;
    RecyclerView rc;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_list);
        myDb = new DatabaseHelper(getApplicationContext());
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        User_Id = settings.getString("userId", null);
        rc= (RecyclerView) findViewById(R.id.recyclerview);
        Site_Id = myDb.Site_Location_Id(User_Id);
        ticketArrayList=new ArrayList<>();
    }
  /*  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void FetchTickets(){
        progressDialog = new ProgressDialog(HelpdeskTickets.this);
        show_pDialog();
        final Retrofit retrofit = HelpDeskClient.getClient();
        final HelpdeskApi helpdeskApi = retrofit.create(HelpdeskApi.class);
        Call<ArrayList<Ticket>> call;

        call = helpdeskApi.getHelpdkTkt(Site_Id,Master_DB,"SM",User_Id);
        call.enqueue(new Callback<ArrayList<Ticket>>() {
            @Override
            public void onResponse(Call<ArrayList<Ticket>> call, retrofit2.Response<ArrayList<Ticket>> response) {
                progressDialog.dismiss();
                if(response.code()==200){
                    ticketArrayList=response.body();
                    Collections.reverse(ticketArrayList);
                    helpdeskIMSAdapter = new HelpdeskIMSAdapter(HelpdeskTickets.this, ticketArrayList  );
                    rc.setHasFixedSize(true);
                    Log.d("response",""+response);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(HelpdeskTickets.this, LinearLayoutManager.VERTICAL, true);
                    rc.setLayoutManager(layoutManager);
                    layoutManager.setReverseLayout(true);
                    layoutManager.setStackFromEnd(true);
                    rc.setItemAnimator(new DefaultItemAnimator());
                    rc.setAdapter(helpdeskIMSAdapter);
                    helpdeskIMSAdapter.notifyDataSetChanged();
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
    }*/
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
        })
      );
    }

    public void show_pDialog() {
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();
    }

}
