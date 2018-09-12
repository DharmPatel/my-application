package com.example.google.template;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class PPMMultipuleTask extends AppCompatActivity {
    DatabaseHelper myDb;
    SQLiteDatabase db;
    RecyclerView lv;
    String id,assetName,activityName,assetId,date;
    PPMTaskAdapter taskDataAdapter;
    private String assetCode;
    private List<PpmTaskProvider> listItems;
    String Date;
    String companyId,SiteId,User_Id,Sitename;
    int requestCode=0;
    RecyclerView.Adapter adapter;
    PpmTaskProvider taskProvider;
    String listFromId;
    String StartDate,EndDate,listFrequnecyId,status;
    String UserGroupId,timestartson,Asset_Type,asset_activity_linking_id,Status,updatedStatus,TaskIdList,TaskId,activity_frequency,Site_Location_Id,Assigned_To_User_Group_Id,Assigned_To_User_Id ,Asset_Id,Form_Id,StartDateTime,EndDateTime,Asset_Code,Asset_Name ,Asset_Location,Asset_Status,Activity_Name,Group_Name,Task_Status;
    Date dateTimeStart,dateTimeEnd;
    Calendar calenderCurrent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ppm_multitask);
        assetCode = getIntent().getStringExtra("AssetCode");
        //status = getIntent().getStringExtra("Completed");
        //Log.d("Status1223"," "+status);
        listItems =new ArrayList<>();
        calenderCurrent = Calendar.getInstance();
        myDb=new DatabaseHelper(getApplicationContext());
        db = myDb.getReadableDatabase();
        requestCode = getIntent().getIntExtra("requestCode", 0);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        companyId = settings.getString("Company_Customer_Id", null);
        User_Id = settings.getString("userId", null);
        SiteId = myDb.Site_Location_Id(User_Id);
        Sitename = myDb.SiteName(User_Id);
        lv = (RecyclerView) findViewById(R.id.ppm_multilist);
        lv.setHasFixedSize(true);
        DividerItemDecoration myDivider = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        myDivider.setDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.list_divider));
        lv.addItemDecoration(myDivider);
        lv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new PPMTaskAdapter(listItems,getBaseContext());
        lv.setAdapter(adapter);
        int count = 0;
        try {
            db= myDb.getWritableDatabase();
            String Query = "SELECT ppm.*,\n" +
                    "              ug.Group_Name,\n" +
                    "              am.Activity_Name,\n" +
                    "              am.Form_Id,\n" +
                    "              ad.Asset_Code,\n" +
                    "              ad.Asset_Name,\n" +
                    "              ad.Asset_Type,\n" +
                    "              ad.Asset_Location,\n" +
                    "              ad.Status\n" +
                    "    FROM      ppm_task ppm \n" +
                    "    LEFT JOIN asset_activity_assignedto aaa \n" +
                    "    ON        aaa.Asset_Activity_Linking_Id = ppm.Asset_Activity_Linking_Id\n" +
                    "    LEFT JOIN asset_activity_linking aal \n" +
                    "    ON        aal.Auto_Id = ppm.Asset_Activity_Linking_Id \n" +
                    "    LEFT JOIN asset_details ad \n" +
                    "    ON        ad.Asset_Id = aal.Asset_Id \n" +
                    "    LEFT JOIN activity_master am \n" +
                    "    ON        am.Auto_Id = aal.Activity_Id \n" +
                    "    LEFT JOIN User_Group ug\n" +
                    "    ON                ug.User_Group_Id = aaa.Assigned_To_User_Group_Id \n" +
                    "where ad.Asset_Code='"+assetCode+"' and aaa.Assigned_To_User_Group_Id IN ("+myDb.UserGroupId(User_Id)+") \n" +
                    "AND ppm.Site_Location_Id='"+myDb.Site_Location_Id(User_Id)+"' AND\n" +
                    "    \t  ad.Status= 'WORKING' and ppm.Task_Status = 'Pending' ";
            Cursor cursor= db.rawQuery(Query, null);
            Log.d("cursor_count",cursor.getCount()+" Query:"+Query);
            if (cursor.getCount()!=0) {
                if (cursor.moveToFirst()) {
                    do {
                        TaskId = cursor.getString(cursor.getColumnIndex("Auto_Id"));
                        Site_Location_Id = cursor.getString(cursor.getColumnIndex("Site_Location_Id"));
                        activity_frequency = cursor.getString(cursor.getColumnIndex("Activity_Frequency"));
                        StartDateTime = cursor.getString(cursor.getColumnIndex("Task_Date"));
                        timestartson =  cursor.getString(cursor.getColumnIndex("Timestartson"));
                        EndDateTime = cursor.getString(cursor.getColumnIndex("Task_End_Date"));
                        Task_Status = cursor.getString(cursor.getColumnIndex("Task_Status"));
                        Activity_Name = cursor.getString(cursor.getColumnIndex("Activity_Name"));
                        Form_Id = cursor.getString(cursor.getColumnIndex("Form_Id"));
                        Asset_Code = cursor.getString(cursor.getColumnIndex("Asset_Code"));
                        Asset_Name = cursor.getString(cursor.getColumnIndex("Asset_Name"));
                        Asset_Location = cursor.getString(cursor.getColumnIndex("Asset_Location"));
                        Status = cursor.getString(cursor.getColumnIndex("Status"));
                        Assigned_To_User_Group_Id = cursor.getString(cursor.getColumnIndex("Assigned_To_User_Group_Id"));
                        Group_Name = cursor.getString(cursor.getColumnIndex("Group_Name"));
                        updatedStatus = cursor.getString(cursor.getColumnIndex("UpdatedStatus"));
                        Asset_Type = cursor.getString(cursor.getColumnIndex("Asset_Type"));
                        asset_activity_linking_id = cursor.getString(cursor.getColumnIndex("Asset_Activity_Linking_Id"));
                        Log.d("cursor_count12",Asset_Name+":"+dateTimeStart+":"+dateTimeEnd);
                        try {
                            dateTimeStart = parseDate(StartDateTime + " " + timestartson);
                            dateTimeEnd = parseDate(EndDateTime + " " + timestartson);
                            if (calenderCurrent.getTime().after(dateTimeStart) && calenderCurrent.getTime().before(dateTimeEnd)) {
                                taskProvider = new PpmTaskProvider(TaskId,Site_Location_Id,activity_frequency,formatDate(dateTimeStart),Task_Status,asset_activity_linking_id,formatDate(dateTimeEnd),Activity_Name,Form_Id,Asset_Code,Asset_Name,Asset_Location,Asset_Type,Status,Assigned_To_User_Group_Id,Group_Name,updatedStatus);
                                listItems.add(taskProvider);
                                count++;
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    while (cursor.moveToNext());
                }
            }
            cursor.close();
            db.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.d("ua107 ","ERROR==" + e);
            Toast.makeText(getApplicationContext(), "Error code: ua107", Toast.LENGTH_SHORT).show();
        }
        Log.d("ListeVieCount",listItems.size()+":"+Task_Status);
        lv.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), lv, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                try {
                    taskProvider = listItems.get(position);
                    assetName = taskProvider.getAsset_Name();
                    StartDate = taskProvider.getTask_Scheduled_Date();
                    EndDate =taskProvider.getTaskEndTime();
                    assetCode = taskProvider.getAsset_Code();
                    listFromId = taskProvider.getForm_id();
                    activityName = taskProvider.getActivity_Name();
                    TaskIdList = taskProvider.getTaskId();
                    UserGroupId = taskProvider.getAssigned_To_User_Group_Id();
                } catch (Exception e) {
                    Log.d("ua141 ","ERROR==" + e);
                    Toast.makeText(getApplicationContext(), "Error code: ua141", Toast.LENGTH_SHORT).show();
                }
                    Intent intent = new Intent(PPMMultipuleTask.this, DynamicForm.class);
                    intent.putExtra("Form_Id", listFromId);
                    intent.putExtra("TaskId", TaskIdList);
                    intent.putExtra("AssetName", assetName);
                    intent.putExtra("ActivityName", activityName);
                    intent.putExtra("StartDate",StartDate);
                    intent.putExtra("AssetCode", assetCode);
                    intent.putExtra("User_Group_Id", UserGroupId);
                    intent.putExtra("Completed", "Pending");
                    intent.putExtra("Status","Completed");
                    intent.putExtra("PPMTask","PPMPending");
                    startActivity(intent);
                    finish();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(PPMMultipuleTask.this, ppm_activity.class);
        intent.putExtra("SAuto_Id", User_Id);
        intent.putExtra("TAB","TAB3");
        startActivity(intent);
        finish();
    }

    private Date parseDate(String date) {
        SimpleDateFormat inputParser = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            return inputParser.parse(date);
        } catch (ParseException e) {
            return new Date(0);
        }
    }

    public String formatDate(Date date) {
        SimpleDateFormat inputParser = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return inputParser.format(date);
    }

}
