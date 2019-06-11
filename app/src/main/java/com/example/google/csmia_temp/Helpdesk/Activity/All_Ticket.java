package com.example.google.csmia_temp.Helpdesk.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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
import com.example.google.csmia_temp.Helpdesk.Adapter.HelpdeskAdapter;
import com.example.google.csmia_temp.Helpdesk.HelpDeskClient;
import com.example.google.csmia_temp.Helpdesk.HelpdeskApi;
import com.example.google.csmia_temp.Helpdesk.Model.Ticket;
import com.example.google.csmia_temp.R;
import com.example.google.csmia_temp.RecyclerTouchListener;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class All_Ticket extends Fragment {
    DatabaseHelper myDb;
    SQLiteDatabase db;
    ArrayList<Ticket> ticketArrayList;
    HelpdeskAdapter helpdeskAdapter;
    RecyclerView rc;
    SharedPreferences settings;
    String User_Id, User_Group_Id, User_Group_Name,Site_Location_Id;
    ProgressDialog progressDialog;
    public boolean internetConnection = false;
    LinearLayout linearLayout;
    Boolean isVisible = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all__ticket, container, false);
        linearLayout = view.findViewById(R.id.main_layout);
        rc = (RecyclerView) view.findViewById(R.id.AllTicketrecyclerview);
        myDb = new DatabaseHelper(getActivity());
        settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        User_Id = settings.getString("userId", null);
        User_Group_Id = myDb.UserGroupId(User_Id);
        Site_Location_Id = myDb.Site_Location_Id(User_Id);
        User_Group_Name = myDb.UserGroupName(User_Group_Id);
        ticketArrayList=new ArrayList<>();

        //new DataDownload().execute();
        //fetchTicket();
        return view;
    }

    @SuppressLint("StaticFieldLeak")
    private class DataDownload extends AsyncTask<Void, Void, Boolean>{


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
               // SSLCerts.sslreq();
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
            if (result == true) {
                progressDialog.dismiss();
                fetchTicket();
            } else {
                progressDialog.dismiss();
                //Toast.makeText(getActivity(),"You are not connected to internet",Toast.LENGTH_SHORT).show();
                int color;
                color = Color.RED;
                Snackbar snackbar = Snackbar
                        .make(linearLayout, "You are not connected to internet!!", Snackbar.LENGTH_LONG);
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(color);
                snackbar.show();
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        if(isVisible){
            new TicketTabActivity().setNfcStatus(false);
            new DataDownload().execute();
        }
        Log.d("Test Visible",isVisible+" "+isVisibleToUser);
    }

    private void fetchTicket(){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();
        final Retrofit retrofit = HelpDeskClient.getClient();
        final HelpdeskApi helpdeskApi = retrofit.create(HelpdeskApi.class);
        Call<ArrayList<Ticket>> call;
        call = helpdeskApi.getAllTicket(myDb.getBuildName(Site_Location_Id));
        call.enqueue(new Callback<ArrayList<Ticket>>() {
            @Override
            public void onResponse(Call<ArrayList<Ticket>> call, Response<ArrayList<Ticket>> response) {
                if (response.code() == 200){
                    progressDialog.dismiss();
                    ticketArrayList=response.body();
                    Collections.reverse(ticketArrayList);
                    helpdeskAdapter = new HelpdeskAdapter(getActivity(), ticketArrayList  );
                    rc.setHasFixedSize(true);
                    Log.d("response_of_all_ticket",""+response);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
                    rc.setLayoutManager(layoutManager);
                    layoutManager.setReverseLayout(true);
                    layoutManager.setStackFromEnd(true);
                    rc.setItemAnimator(new DefaultItemAnimator());
                    rc.setAdapter(helpdeskAdapter);
                    helpdeskAdapter.notifyDataSetChanged();
                    clickData();
                }else if (response.code() == 404){
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Requested resource not found", Toast.LENGTH_SHORT).show();
                }else if(response.code()==500){
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Something went wrong at server end" , Toast.LENGTH_SHORT).show();

                }else {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Internet Connection Lost.Synchronization failed." , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Ticket>> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    public void clickData() {

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
               /* intent.putParcelableArrayListExtra("data", (ArrayList<? extends Parcelable>) ticketArrayList);
                intent.putExtra("pos",position);*/
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
