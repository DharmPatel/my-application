package com.example.google.csmia_temp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckListCompleted extends Fragment {
    DatabaseHelper myDb;
    SQLiteDatabase db;
    private ListView lv;
    String id, SiteId, User_Id,Scan_Type,assetId;
    TaskDataAdapter taskDataAdapter;
    PendingTask pendingTask;
    LinearLayout linlaHeaderProgress;
    TaskProvider taskProvider;
    List<TaskProvider> taskProviders;
    String TaskId,UpdatedStatus,Group_Name,Frequency_Id,UnplannedTime, Site_Location_Id, Assigned_To_User_Group_Id,Assigned_To_User_Id, Asset_Id, From_Id, StartDateTime, EndDateTime, Asset_Code, Asset_Name, Asset_Location, Asset_Status, Activity_Name, Task_Status;


    public CheckListCompleted() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completed_task, container, false);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        myDb = new DatabaseHelper(getActivity());
        pendingTask = new PendingTask();
        User_Id = settings.getString("userId", null);
        SiteId = myDb.Site_Location_Id(User_Id);
        Scan_Type = myDb.ScanType(User_Id);

        taskProviders = new ArrayList<TaskProvider>();
        lv = (ListView) view.findViewById(R.id.list_Completed);
        linlaHeaderProgress = (LinearLayout) view.findViewById(R.id.linlaHeaderProgress);
        taskDataAdapter = new TaskDataAdapter(getContext(), R.layout.list_item);
        lv.setAdapter(taskDataAdapter);
        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute();
        if(new applicationClass().completedView().equals("yes")){
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {

                        for (int j = 0; j < parent.getChildCount(); j++)
                            parent.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
                        String assetName = taskDataAdapter.getItem(position).getAsset_Name();
                        String StartDate = taskDataAdapter.getItem(position).getStartDateTime();
                        String EndDate = taskDataAdapter.getItem(position).getEndDateTime();
                        String listFrequnecyId = taskDataAdapter.getItem(position).getFrequency_Id();
                        String assetCode = taskDataAdapter.getItem(position).getAsset_Code();
                        String listFromId = taskDataAdapter.getItem(position).getFrom_Id();
                        String activityName = taskDataAdapter.getItem(position).getActivity_Name();
                        assetId = taskDataAdapter.getItem(position).getAsset_Id();
                        String TaskIdList = taskDataAdapter.getItem(position).getTaskId();
                        // String UserGroupId = taskDataAdapter.getItem(position).getAssigned_To_User_Group_Id();

                        Intent intent = new Intent(getContext(), DynamicForm.class);
                        intent.putExtra("Form_Id", listFromId);
                        intent.putExtra("TaskId", TaskIdList);
                        intent.putExtra("AssetName", assetName);
                        intent.putExtra("ActivityName", activityName);
                        intent.putExtra("AssetId", assetId);
                        intent.putExtra("FrequencyId", listFrequnecyId);
                        intent.putExtra("StartDate", StartDate);
                        intent.putExtra("AssetCode", assetCode);
                        //intent.putExtra("User_Group_Id", UserGroupId);
                        intent.putExtra("Completed", "Completed");
                        intent.putExtra("IntentValue", "CheckList");
                        startActivity(intent);

                        getActivity().finish();

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("LoG@", e.getMessage());
                    }

                }
            });
        }
        return view;
    }
    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        try {
            if (menuVisible){
                new TaskDetails().setPendingNFC(false);
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.d("NfcErrorMenuVisibility",e.getMessage());
            Log.d("aa79","ERROR==" + e);
            Toast.makeText(getActivity(), "Error code: aa79", Toast.LENGTH_SHORT).show();
        }
    }
    //NFC END
    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            linlaHeaderProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            db = myDb.getWritableDatabase();

            String completedQuery = " SELECT a.Group_Name,b.* FROM User_Group a,Task_Details b WHERE a.User_Group_Id=b.Assigned_To_User_Group_Id and b.Assigned_To_User_Group_Id IN ("+myDb.UserGroupId(User_Id)+") AND b.Site_Location_Id='" + SiteId + "'  AND b.Task_Status IN ('Completed')AND b.Activity_Type ='CheckList" +
                    "'";

            Cursor cursor = db.rawQuery(completedQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    TaskId = cursor.getString(cursor.getColumnIndex("Auto_Id"));
                    Frequency_Id = cursor.getString(cursor.getColumnIndex("Activity_Frequency_Id"));
                    Site_Location_Id = cursor.getString(cursor.getColumnIndex("Site_Location_Id"));
                    Assigned_To_User_Id = cursor.getString(cursor.getColumnIndex("Assigned_To_User_Id"));
                    //Assigned_To_User_Group_Id = cursor.getString(cursor.getColumnIndex("Assigned_To_User_Group_Id"));
                    Asset_Id = cursor.getString(cursor.getColumnIndex("Asset_Id"));
                    From_Id = cursor.getString(cursor.getColumnIndex("From_Id"));
                    StartDateTime = cursor.getString(cursor.getColumnIndex("Task_Scheduled_Date"));
                    EndDateTime = cursor.getString(cursor.getColumnIndex("EndDateTime"));
                    Asset_Code = cursor.getString(cursor.getColumnIndex("Asset_Code"));
                    Asset_Name = cursor.getString(cursor.getColumnIndex("Asset_Name"));
                    Asset_Location = cursor.getString(cursor.getColumnIndex("Asset_Location"));
                    Asset_Status = cursor.getString(cursor.getColumnIndex("Asset_Status"));
                    Activity_Name = cursor.getString(cursor.getColumnIndex("Activity_Name"));
                    Task_Status = cursor.getString(cursor.getColumnIndex("Task_Status"));
                    UnplannedTime = cursor.getString(cursor.getColumnIndex("Task_Start_At"));
                    UpdatedStatus = cursor.getString(cursor.getColumnIndex("UpdatedStatus"));
                    Group_Name = cursor.getString(cursor.getColumnIndex("Group_Name"));

                    Log.d("ValuesOfAsset",""+Asset_Id+" "+Frequency_Id);
                    try {

                        if (Asset_Id == null){
                            String AssetName="", ActivityName="", AssetLocation="";
                            String TaskAssetQuery = "SELECT \n" +
                                    "aal.Asset_Id,\n" +
                                    "am.Activity_Name,\n" +
                                    "am.Form_Id,\n" +
                                    "ad.Asset_Name,\n" +
                                    "ad.Asset_Location\n" +
                                    "FROM Activity_Frequency af\n" +
                                    "left join Asset_Activity_Linking aal\n" +
                                    "on aal.Auto_Id = af.Asset_Activity_Linking_Id\n" +
                                    "left join Asset_Details ad\n" +
                                    "on ad.Asset_Id = aal.Asset_Id\n" +
                                    "left join Activity_Master am\n" +
                                    "on am.Auto_Id = aal.Activity_Id\n" +
                                    "where Frequency_Auto_Id = '"+Frequency_Id+"'";
                            Cursor cur = db.rawQuery(TaskAssetQuery,null);
                            if(cur.getCount()>0) {
                                if (cur.moveToFirst()) {
                                    do {
                                        Asset_Id = cur.getString(cur.getColumnIndex("Asset_Id"));
                                        ActivityName = cur.getString(cur.getColumnIndex("Activity_Name"));
                                        From_Id = cur.getString(cur.getColumnIndex("Form_Id"));
                                        AssetName = cur.getString(cur.getColumnIndex("Asset_Name"));
                                        AssetLocation = cur.getString(cur.getColumnIndex("Asset_Location"));
                                    } while (cur.moveToNext());
                                }
                            }
                            cur.close();
                            taskProvider = new TaskProvider(TaskId, Frequency_Id, Site_Location_Id, Assigned_To_User_Id, Asset_Id, From_Id, UnplannedTime, /*formatDateTask(parseDate(UnplannedTime))*/"", Asset_Code, AssetName, AssetLocation, Asset_Status, ActivityName, Task_Status,Group_Name,null,UpdatedStatus);
                            taskProviders.add(taskProvider);
                        }else {
                            taskProvider = new TaskProvider(TaskId, Frequency_Id, Site_Location_Id, Assigned_To_User_Id, Asset_Id, From_Id, UnplannedTime,/* formatDateTask(parseDate(UnplannedTime))*/"", Asset_Code, Asset_Name, Asset_Location, Asset_Status, Activity_Name, Task_Status,Group_Name,null,UpdatedStatus);
                            taskProviders.add(taskProvider);
                        }

                        /*if(Task_Status.equals("Unplanned")) {
                            taskProvider = new TaskProvider(TaskId, Frequency_Id, Site_Location_Id, Assigned_To_User_Id, Asset_Id, From_Id, ""*//*formatDateTask(parseDate(UnplannedTime))*//*, *//*EndDateTime*//*"", Asset_Code, Asset_Name, Asset_Location, Asset_Status, Activity_Name, Task_Status,Group_Name,null,UpdatedStatus);
                            taskProviders.add(taskProvider);
                        }else {
                            taskProvider = new TaskProvider(TaskId, Frequency_Id, Site_Location_Id, Assigned_To_User_Id, Asset_Id, From_Id, *//*formatDateTask(parseDate(StartDateTime))*//*"", *//*formatDateTask(parseDate(UnplannedTime))*//*"", Asset_Code, Asset_Name, Asset_Location, Asset_Status, Activity_Name, Task_Status,Group_Name,null,UpdatedStatus);
                            taskProviders.add(taskProvider);
                        }*/

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                while (cursor.moveToNext());
            }
            cursor.close();
            db.close();

            return null;
        }
        public String formatDate(Date date) {
            SimpleDateFormat inputParser = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return inputParser.format(date);
        }
        private Date parseDate(String date) {
            SimpleDateFormat inputParser = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                return inputParser.parse(date);
            } catch (ParseException e) {
                return new Date(0);
            }
        }
        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            int count=0;
            Collections.sort(taskProviders, new Comparator<TaskProvider>() {
                @Override
                public int compare(TaskProvider taskProvider1, TaskProvider taskProvider2) {
                    return taskProvider1.getStartDateTime().compareTo(taskProvider2.getStartDateTime());
                }

            });
            Collections.reverse(taskProviders);
            linlaHeaderProgress.setVisibility(View.GONE);
            for (TaskProvider taskProvider1 : taskProviders) {
                taskDataAdapter.add(taskProvider1);
                count++;
            }
            new TaskDetails().setComplete(true);
            new PendingTask().setComplete(true);

        }
    }
    public String formatDateTask(Date date) {
        SimpleDateFormat inputParser = new SimpleDateFormat("EEE MMM dd HH:mm");
        return inputParser.format(date);
    }
}
