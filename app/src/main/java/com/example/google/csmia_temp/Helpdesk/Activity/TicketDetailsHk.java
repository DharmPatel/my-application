package com.example.google.csmia_temp.Helpdesk.Activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.google.csmia_temp.DatabaseHelper;
import com.example.google.csmia_temp.Helpdesk.HelpDeskClient;
import com.example.google.csmia_temp.Helpdesk.HelpdeskApi;
import com.example.google.csmia_temp.Helpdesk.Model.Ticket_Log;
import com.example.google.csmia_temp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.google.csmia_temp.applicationClass.getContext;

public class TicketDetailsHk extends AppCompatActivity {
        TextView tv_ticket_code,tv_dept_nm,tv_site_name,tv_loc,tv_subloc,tv_service_area,tv_product_name,tv_component,
        tv_problem_title,tv_risk_priority,tv_created_on,tv_updated_on,tv_status,tv_incident_type,tv_resolution_time,
        tv_affected_party,tv_incident_descp,tv_updated_remark,tv_created_by,tv_updated_by;
        Spinner sp_status;
        EditText et_comment;
        Button btn_update,btn_close;
        String Upt_status,comment;
        String Group_Name,ticket_code,depnm,sitenm,loc,subloc,serviceare,prductnm,comp,probtl,riskpro,createon,updatedon,status,incidenttype,resolutiontm,affectedparty,
         incidentdescp,updatedremk,createdby,updatedby="",usertype="",tkt_sr_id;
        ProgressDialog progressDialog;
        SimpleDateFormat simpleDateFormat;
        String Date;
        ArrayList<Ticket_Log> ticketLogArrayList;
        LinearLayout ll_layout,cd_status_comment;
        ArrayList<String> status_list;
        String UserName = "",selected_status,Comment,StartOn;
        static EditText et_startOn;
        public static String StartOnDate;
        public static String StartOnTime;
        Calendar calendar;
        String User_Id;
        SharedPreferences settings;
        ArrayList<String> updateStatus=new ArrayList<>();
        FloatingActionButton fab;
        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketdetails);
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        User_Id = settings.getString("userId", null);
        init();
        getIntentData();
        setData();

//        Sp_status();
           /* et_startOn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTruitonTimePickerDialog(v);
                    showTruitonDatePickerDialog(v);
                }
            });*/
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserName();
                if(usertype.equalsIgnoreCase("Facilities")){
                    UpdateData();
                }else {
                    UpdateTicket();
                }

            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(),TicketLogActivity.class);
                intent.putParcelableArrayListExtra("ticket_logList",ticketLogArrayList);
                startActivity(intent);
            }
        });

    }
    private void data(){
        status_list =new ArrayList<String>(){};
        status_list.add("Open");
        status_list.add("Work In Progress");
        status_list.add("Closed");
    }
    public void showTruitonDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current date as the default date in the picker

            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dpd = new DatePickerDialog(getActivity(), this, year, month, day);
            /*SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date d = null;
            try {
                d = sdf.parse("21/12/2012");
            } catch (ParseException e) {
                e.printStackTrace();
            }*/
            dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            // dpd.show();

            // Create a new instance of DatePickerDialog and return it
            return dpd;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            et_startOn.setText(year+ "-" + (month + 1) + "-" + day);
            StartOnDate=et_startOn.getText().toString();
        }
    }
    public void showTruitonTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public static class TimePickerFragment extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            int seconds =c.get(Calendar.SECOND);
            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            et_startOn.setText(et_startOn.getText() + " " + hourOfDay + ":"	+ minute + ":"	+ "00");
            StartOnTime=et_startOn.getText().toString();
        }
    }
    private void init(){
        tv_ticket_code= (TextView) findViewById(R.id.tv_ticket_code);
        tv_dept_nm= (TextView) findViewById(R.id.tv_dept_nm);
        tv_site_name= (TextView) findViewById(R.id.tv_site_name);
        tv_loc= (TextView) findViewById(R.id.tv_loc);
        tv_subloc= (TextView) findViewById(R.id.tv_subloc);
        tv_service_area= (TextView) findViewById(R.id.tv_service_area);
        tv_product_name= (TextView) findViewById(R.id.tv_product_name);
        tv_status= (TextView) findViewById(R.id.tv_status);
        tv_component= (TextView) findViewById(R.id.tv_component);
        tv_problem_title= (TextView) findViewById(R.id.tv_problem_title);
        tv_risk_priority = (TextView) findViewById(R.id.tv_risk_priority);
        tv_created_on= (TextView) findViewById(R.id.tv_created_on);
        tv_updated_on= (TextView) findViewById(R.id.tv_updated_on);
        tv_status = (TextView) findViewById(R.id.tv_status);
        tv_incident_type = (TextView) findViewById(R.id.tv_incident_type);
        tv_resolution_time =(TextView) findViewById(R.id.tv_resolution_time);
        tv_affected_party = (TextView) findViewById(R.id.tv_affected_party);
        tv_incident_descp = (TextView) findViewById(R.id.tv_incident_descp);
        tv_updated_remark = (TextView) findViewById(R.id.tv_updated_remark);
        tv_created_by = (TextView) findViewById(R.id.tv_created_by);
        tv_updated_by = (TextView) findViewById(R.id.tv_updated_by);
        sp_status = (Spinner) findViewById(R.id.sp_status);
        et_comment = (EditText) findViewById(R.id.et_comment);
        btn_update= (Button) findViewById(R.id.btn_acknowledge);
        btn_close= (Button) findViewById(R.id.btn_close);
        ll_layout = (LinearLayout) findViewById(R.id.ll_layout);
        cd_status_comment= (LinearLayout) findViewById(R.id.cd_status_comment);
        fab= (FloatingActionButton) findViewById(R.id.fab);
        ticketLogArrayList =new ArrayList<>();
    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
    private void UpdateData() {
        if (isNetworkConnected()) {
            Comment = et_comment.getText().toString();
//            StartOn = et_startOn.getText().toString();
                if (Comment.isEmpty()) {
                        et_comment.setError("Please Enter Commemt");
                }else {
                   UpdateTicket();
                }
        } else {
            Snackbar snackbar = Snackbar.make(ll_layout, "Please check your internet connection !!", Snackbar.LENGTH_LONG);
            snackbar.show();

        }

    }
    private void getIntentData(){
        Intent intent =getIntent();
       /* data=intent.getParcelableArrayListExtra("data");
         pos=intent.getIntExtra("pos",0);*/
        Group_Name=intent.getStringExtra("Group_Name");
        ticket_code=intent.getStringExtra("id");
        depnm=intent.getStringExtra("dept");
        sitenm=intent.getStringExtra("site_nm");
        loc=intent.getStringExtra("ocation_name");
        subloc=intent.getStringExtra("sub_loc");
        serviceare=intent.getStringExtra("service_are_nm");
        prductnm =intent.getStringExtra("product_nm");
        comp=intent.getStringExtra("component_nm");
        probtl=intent.getStringExtra("problem_title");
        riskpro=intent.getStringExtra("risk_priority");
        createon=intent.getStringExtra("create_on");
        updatedon= intent.getStringExtra("update_on");
        status=intent.getStringExtra("status");
        incidenttype=intent.getStringExtra("incident_type");
        resolutiontm=intent.getStringExtra("resolution_time");
        affectedparty=intent.getStringExtra("affected_party");
        incidentdescp=intent.getStringExtra("incident_description");
        updatedremk=intent.getStringExtra("updated_remarks");
        createdby= intent.getStringExtra("created_by");
        updatedby= intent.getStringExtra("updated_by");
        usertype=intent.getStringExtra("user_type");
        tkt_sr_id=intent.getStringExtra("tktcode");
        ticketLogArrayList=intent.getParcelableArrayListExtra("ticketLogList");
    }
    private void setData(){
        tv_ticket_code.setText(ticket_code);
        tv_dept_nm.setText(depnm);
        tv_site_name.setText(sitenm);
        tv_loc.setText(loc);
        tv_subloc.setText(subloc);
        tv_service_area.setText(serviceare);
        tv_product_name.setText(prductnm);
        tv_component.setText(comp);
        tv_problem_title.setText(probtl);
        tv_risk_priority.setText(riskpro);
        tv_created_on.setText(createon);
        tv_updated_on.setText(updatedon);
        tv_status.setText(status);
        tv_incident_type.setText(incidenttype);
        tv_resolution_time.setText(resolutiontm);
        tv_affected_party.setText(affectedparty);
        tv_incident_descp.setText(incidentdescp);
        tv_updated_remark.setText(updatedremk);
        tv_created_by.setText(createdby);
        tv_updated_by.setText(updatedby);
        ArrayList<String> status_list =new ArrayList<String>(){};
        status_list.add("Open");
        status_list.add("Work In Progress");
        status_list.add("Closed");

            if(status.equalsIgnoreCase("Open")){
              status_list.remove("Open");
          } if(status.equalsIgnoreCase("Work In Progress")){
            status_list.remove("Open");
        }

        if(status.equalsIgnoreCase("Rejected") || status.equalsIgnoreCase("Closed")){
            /*tx_status_heading.setVisibility(View.GONE);*/
            cd_status_comment.setVisibility(View.GONE);
            btn_update.setVisibility(View.GONE);
        }

        ArrayAdapter statusAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,status_list);
        sp_status.setAdapter(statusAdapter);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        for(int i =0; i<status_list.size(); i++){
            Log.d("status_list_size"+status_list.size(),"status_list_tostring"+status_list.toString()+"status"+status);
            if(status_list.get(i).equalsIgnoreCase(status)){
                Log.d("status_list",""+status_list);
                sp_status.setSelection(i);
                break;
            }
        }
    }
    private void Sp_status(){
        ArrayAdapter statusAdapter;
        if(usertype.equalsIgnoreCase("Facilities")){
            data();
            for(int i =0; i<status_list.size(); i++){
                Log.d("status_list_size"+status_list.size(),"status_list_tostring"+status_list.toString()+"status"+status);
                if(status_list.get(i).equalsIgnoreCase(status)){
                    Log.d("status_list",""+status_list);
                    sp_status.setSelection(i);
                    break;
                }
            }
            statusAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,status_list);

        }else {
            statusAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,updateStatus);


        }
        sp_status.setAdapter(statusAdapter);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void UserName(){
        DatabaseHelper databaseHelper =new DatabaseHelper(this);
        SQLiteDatabase sqLiteDatabase =databaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery( "SELECT Username FROM Login_Details Where User_Id = '"+User_Id+"'",null);
            if(cursor.getCount()!=0){
                if (cursor.moveToFirst()) {
                    do {
                        UserName = cursor.getString(cursor.getColumnIndex("Username"));
                        }while (cursor.moveToNext());
                }
            }
        }
    @SuppressLint("SimpleDateFormat")
    private void UpdateTicket(){
        comment=et_comment.getText().toString();
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date = null;
        Upt_status=sp_status.getSelectedItem().toString();
        updatedby="JCC CIVIL";
        progressDialog = new ProgressDialog(TicketDetailsHk.this);
        show_pDialog();
        Call<ResponseBody> call;
        final Retrofit retrofit = HelpDeskClient.getClient();
        final HelpdeskApi helpdeskApi = retrofit.create(HelpdeskApi.class);
        if(usertype.equalsIgnoreCase("Facilities")){
            call=helpdeskApi.UpdatTickethelpdk(comment,tkt_sr_id,Upt_status,status,UserName,ticket_code,"NA",Date,User_Id);
        }else {
            call=helpdeskApi.UpdatTicket(ticket_code,Upt_status,Date,updatedby,comment);
        }
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                if(response.code()==200){
                    if(response.isSuccessful()){
                        Toast.makeText(TicketDetailsHk.this,"Ticket updated Successfully",Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }else if(response.code()==404){
                    Toast.makeText(TicketDetailsHk.this,"Error!!",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(TicketDetailsHk.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(TicketDetailsHk.this,"Something went wrong",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void show_pDialog() {
        progressDialog.setMessage("Updating Data...");
        progressDialog.show();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(usertype.equalsIgnoreCase("Facilities")) {
            startActivity(new Intent(TicketDetailsHk.this,NewTicketActivity .class));
            finish();
        }else {
            startActivity(new Intent(TicketDetailsHk.this,TicketTabActivity.class));
            finish();
        }
    }
}
