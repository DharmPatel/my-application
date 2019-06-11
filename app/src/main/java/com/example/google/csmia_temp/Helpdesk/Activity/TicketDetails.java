package com.example.google.csmia_temp.Helpdesk.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.google.csmia_temp.DatabaseHelper;
import com.example.google.csmia_temp.Helpdesk.HelpDeskClient;
import com.example.google.csmia_temp.Helpdesk.HelpdeskApi;
import com.example.google.csmia_temp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TicketDetails extends AppCompatActivity {
        TextView tv_ticket_code,tv_dept_nm,tv_site_name,tv_loc,tv_subloc,tv_service_area,tv_product_name,tv_component,
        tv_problem_title,tv_risk_priority,tv_created_on,tv_updated_on,tv_status,tv_incident_type,tv_resolution_time,
        tv_affected_party,tv_incident_descp,tv_updated_remark,tv_created_by,tv_updated_by;
        Spinner sp_status;
        EditText et_comment;
        Button btn_update;
        String Upt_status,comment;
        String Group_Name,ticket_code,depnm,sitenm,loc,subloc,serviceare,prductnm,comp,probtl,riskpro,createon,updatedon,status,incidenttype,resolutiontm,affectedparty,
         incidentdescp,updatedremk,createdby,updatedby="";
        ProgressDialog progressDialog;
        SimpleDateFormat simpleDateFormat;
        String Date;
        String UserName = "";
        Calendar calendar;
        String User_Id;
        SharedPreferences settings;
        ArrayList<String> updateStatus=new ArrayList<>();
        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketdetails);
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        User_Id = settings.getString("userId", null);
        init();
        getIntentData();
        setData();
        Sp_status();

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserName();
                UpdateTicket();
            }
        });
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
        //  data.get(pos).getSTATUS();
    }

    private void Sp_status(){
            if(Group_Name.equalsIgnoreCase("Civil") || Group_Name.equalsIgnoreCase("Electrical")){
                if(status.equalsIgnoreCase("Open")){
                    updateStatus.add(0,"Open");
                    updateStatus.add(1,"Attended");
                }else if(status.equalsIgnoreCase("Attended")){
                    updateStatus.add(0,"Attended");
                }
            } else {
                if(status.equalsIgnoreCase("Open")){
                    updateStatus.add(0,"Open");
                    updateStatus.add(1,"Attended");
                    //updateStatus.add(2,"Closed");
                }else if(status.equalsIgnoreCase("Attended")){
                    updateStatus.add(0,"Attended");
                    updateStatus.add(1,"Closed");
                }
            }

        ArrayAdapter statusAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,updateStatus);
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
        Date = simpleDateFormat.format(calendar.getTime());
        Upt_status=sp_status.getSelectedItem().toString();
        updatedby="JCC CIVIL";
        progressDialog = new ProgressDialog(TicketDetails.this);
        show_pDialog();
        final Retrofit retrofit = HelpDeskClient.getClient();
        final HelpdeskApi helpdeskApi = retrofit.create(HelpdeskApi.class);
        Call<ResponseBody> call=helpdeskApi.UpdatTicket(ticket_code,Upt_status,Date,updatedby,comment);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                if(response.code()==200){
                    if(response.isSuccessful()){
                        Toast.makeText(TicketDetails.this,"Ticket updated Successfully",Toast.LENGTH_SHORT);
                        onBackPressed();
                    }
                }else if(response.code()==404){
                    Toast.makeText(TicketDetails.this,"Error!!",Toast.LENGTH_SHORT);
                }else {
                    Toast.makeText(TicketDetails.this,"Something went wrong",Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(TicketDetails.this,"Something went wrong",Toast.LENGTH_SHORT);
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
        startActivity(new Intent(TicketDetails.this,TicketTabActivity.class));
        finish();
    }
}
