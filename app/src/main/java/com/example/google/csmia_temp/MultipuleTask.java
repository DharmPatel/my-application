package com.example.google.csmia_temp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MultipuleTask extends AppCompatActivity {
    DatabaseHelper myDb;
    SQLiteDatabase db;
    private ListView lv;
    String id,assetName,activityName,assetId,date;
    TaskDataAdapter taskDataAdapter;
    private String assetCode;
    String Date;
    String companyId,SiteId,User_Id,Sitename;
    int requestCode=0;
    TaskProvider taskProvider;
    String listFromId;
    String StartDate,EndDate,listFrequnecyId,status;
    String UserGroupId,TaskIdList,TaskId,Frequency_Id,Site_Location_Id,Assigned_To_User_Group_Id,Assigned_To_User_Id ,Asset_Id,From_Id,StartDateTime,EndDateTime,Asset_Code,Asset_Name ,Asset_Location,Asset_Status,Activity_Name,Group_Name,Task_Status;

    Calendar calenderCurrent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multipule_task);
        assetCode = getIntent().getStringExtra("Asset_Code");
        Log.d("assetCodeHere",assetCode);
        //status = getIntent().getStringExtra("Completed");
        //Log.d("Status1223"," "+status);

        calenderCurrent = Calendar.getInstance();
        myDb=new DatabaseHelper(getApplicationContext());
        requestCode = getIntent().getIntExtra("requestCode", 0);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        companyId = settings.getString("Company_Customer_Id", null);
        User_Id = settings.getString("userId", null);
        SiteId = myDb.Site_Location_Id(User_Id);
        Sitename = myDb.SiteName(User_Id);
        lv = (ListView)findViewById(R.id.list_Unplanned);
        taskDataAdapter = new TaskDataAdapter(getBaseContext(), R.layout.list_item);
        lv.setAdapter(taskDataAdapter);
        int count = 0;
        try {
            db= myDb.getWritableDatabase();
            String Query = "SELECT ug.Group_Name," +
                    "td.* " +
                    "FROM Task_Details td " +
                    "LEFT JOIN User_Group ug " +
                    "ON ug.User_Group_Id=td.Assigned_To_User_Group_Id " +
                    "LEFT JOIN Asset_Status asst " +
                    "ON asst.Status = td.Asset_Status " +
                    "WHERE td.Assigned_To_User_Group_Id IN ("+myDb.UserGroupId(User_Id)+") " +
                    "AND td.Site_Location_Id='"+SiteId+"' AND asst.Task_State = 'A' AND td.Asset_Code='"+assetCode+"' " +
                    "AND td.Task_Status = 'Pending' AND td.RecordStatus != 'D'";
            Cursor cursor= db.rawQuery(Query, null);
            Log.d("hfdvjhfvb",Query);

            if (cursor.moveToFirst()) {
                do {
                    TaskId = cursor.getString(cursor.getColumnIndex("Auto_Id"));
                    Frequency_Id = cursor.getString(cursor.getColumnIndex("Activity_Frequency_Id"));
                    Site_Location_Id = cursor.getString(cursor.getColumnIndex("Site_Location_Id")) ;
                    Assigned_To_User_Id = cursor.getString(cursor.getColumnIndex("Assigned_To_User_Id")) ;
                    Assigned_To_User_Group_Id = cursor.getString(cursor.getColumnIndex("Assigned_To_User_Group_Id")) ;
                    Asset_Id = cursor.getString(cursor.getColumnIndex("Asset_Id")) ;
                    From_Id = cursor.getString(cursor.getColumnIndex("From_Id")) ;
                    StartDateTime = cursor.getString(cursor.getColumnIndex("Task_Scheduled_Date"))  ;
                    EndDateTime = cursor.getString(cursor.getColumnIndex("EndDateTime")) ;
                    Asset_Code  = cursor.getString(cursor.getColumnIndex("Asset_Code"));
                    Asset_Name = cursor.getString(cursor.getColumnIndex("Asset_Name")) ;
                    Asset_Location = cursor.getString(cursor.getColumnIndex("Asset_Location")) ;
                    Asset_Status  = cursor.getString(cursor.getColumnIndex("Asset_Status"));
                    Activity_Name = cursor.getString(cursor.getColumnIndex("Activity_Name"));
                    Task_Status = cursor.getString(cursor.getColumnIndex("Task_Status"));
                    Group_Name = cursor.getString(cursor.getColumnIndex("Group_Name"));
                    try {
                        if (calenderCurrent.getTime().after(parseDate(StartDateTime)) && calenderCurrent.getTime().before(parseDate(EndDateTime))) {
                            taskProvider = new TaskProvider(TaskId, Frequency_Id, Site_Location_Id, Assigned_To_User_Id, Asset_Id, From_Id, StartDateTime, EndDateTime, Asset_Code, Asset_Name, Asset_Location, Asset_Status, Activity_Name, Task_Status, Group_Name, Assigned_To_User_Group_Id, null);
                            taskDataAdapter.add(taskProvider);
                            count++;
                        }

                        /*if(Task_Status.equals("Pending")) {

                        } else if(Task_Status.equals("Missed")){
                            taskProvider = new TaskProvider(TaskId, Frequency_Id, Site_Location_Id, Assigned_To_User_Id, Asset_Id, From_Id, StartDateTime, EndDateTime, Asset_Code, Asset_Name, Asset_Location, Asset_Status, Activity_Name, Task_Status, Group_Name, Assigned_To_User_Group_Id, null);
                            taskDataAdapter.add(taskProvider);
                            count++;
                        }*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }
        catch (Exception e)
        {

            Log.d("ua107 ","ERROR==" + e);
            Toast.makeText(getApplicationContext(), "Error code: ua107", Toast.LENGTH_SHORT).show();
        }

        Log.d("ListeVieCount",taskDataAdapter.getCount()+":"+Task_Status);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    for (int j = 0; j < parent.getChildCount(); j++)
                        parent.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
                    assetName = taskDataAdapter.getItem(position).getAsset_Name();
                    StartDate = taskDataAdapter.getItem(position).getStartDateTime();
                    EndDate = taskDataAdapter.getItem(position).getEndDateTime();
                    listFrequnecyId = taskDataAdapter.getItem(position).getFrequency_Id();
                    assetCode = taskDataAdapter.getItem(position).getAsset_Code();
                    listFromId = taskDataAdapter.getItem(position).getFrom_Id();
                    activityName = taskDataAdapter.getItem(position).getActivity_Name();
                    assetId = taskDataAdapter.getItem(position).getAsset_Id();
                    TaskIdList = taskDataAdapter.getItem(position).getTaskId();
                    UserGroupId = taskDataAdapter.getItem(position).getAssigned_To_User_Group_Id();
                } catch (Exception e) {
                    Log.d("ua141 ","ERROR==" + e);
                    Toast.makeText(getApplicationContext(), "Error code: ua141", Toast.LENGTH_SHORT).show();
                }

/*
                if (Task_Status.equals("Pending")) {
*/
                    Intent intent = new Intent(MultipuleTask.this, DynamicForm.class);
                    intent.putExtra("Form_Id", listFromId);
                    intent.putExtra("TaskId", TaskIdList);
                    intent.putExtra("AssetName", assetName);
                    intent.putExtra("ActivityName", activityName);
                    intent.putExtra("AssetId", assetId);
                    intent.putExtra("FrequencyId", listFrequnecyId);
                    intent.putExtra("StartDate",StartDate);
                    intent.putExtra("AssetCode", assetCode);
                    intent.putExtra("User_Group_Id", UserGroupId);
                    intent.putExtra("Completed", "Pending");
                    intent.putExtra("Status","Completed");
                    //intent.putExtra("PPMTask","PPMPending");
                    startActivity(intent);
                    finish();
                /*} else if(Task_Status.equals("Missed")){
                    Intent intent = new Intent(MultipuleTask.this, DynamicForm.class);
                    intent.putExtra("Form_Id", listFromId);
                    intent.putExtra("TaskId", TaskIdList);
                    intent.putExtra("AssetName", assetName);
                    intent.putExtra("ActivityName", activityName);
                    intent.putExtra("AssetId", assetId);
                    intent.putExtra("FrequencyId", listFrequnecyId);
                    intent.putExtra("StartDate",StartDate);
                    intent.putExtra("AssetCode", assetCode);
                    intent.putExtra("User_Group_Id", UserGroupId);
                    intent.putExtra("Completed", "Missed");
                    intent.putExtra("Status","Delayed");
                    startActivity(intent);
                    finish();
                }*/
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MultipuleTask.this, TaskDetails.class);
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

}
