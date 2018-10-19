package com.example.google.template;


import android.app.Activity;
import android.content.Context;
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
public class CompletedTask extends Fragment {
    DatabaseHelper myDb;
    SQLiteDatabase db;
    private ListView lv;
    String id, SiteId, User_Id,Scan_Type,assetId;
    TaskDataAdapter taskDataAdapter;
    PendingTask pendingTask;
    LinearLayout linlaHeaderProgress;
    TaskProvider taskProvider;
    List<TaskProvider> taskProviders;
    String completedQuery;
    String TaskId,UpdatedStatus,Group_Name,Frequency_Id,UnplannedTime, Site_Location_Id, Assigned_To_User_Group_Id,Assigned_To_User_Id, Asset_Id, From_Id, StartDateTime, EndDateTime, Asset_Code, Asset_Name, Asset_Location, Asset_Status, Activity_Name, Task_Status;


    public CompletedTask() {
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
                        /*if(myDb.Verify(User_Id) == 1){
                            intent.putExtra("Completed", "Verify");
                        }
                        else {*/
                            intent.putExtra("Completed", "Completed");
                        //}
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
           /* if(myDb.Verify(User_Id)==1){
                 completedQuery = "SELECT a.Group_Name,b.* FROM User_Group a,Task_Details b WHERE a.User_Group_Id=b.Assigned_To_User_Group_Id and b.Assigned_To_User_Group_Id IN ("+myDb.UserGroupId(User_Id)+") AND b.Site_Location_Id='" + SiteId + "'  AND b.Task_Status IN ('Completed','Unplanned') AND b.Verified=1";
            }else {*/
            completedQuery = "SELECT ug.Group_Name," +
                    "td.* " +
                    "FROM Task_Details td " +
                    "LEFT JOIN User_Group ug ON " +
                    "ug.User_Group_Id=td.Assigned_To_User_Group_Id " +
                    "WHERE td.Assigned_To_User_Group_Id IN ("+myDb.UserGroupId(User_Id)+") " +
                    "AND td.Site_Location_Id='"+SiteId+"' AND td.Asset_Status= 'WORKING'  AND td.Task_Status IN ('Completed','Unplanned','Delayed') AND td.Activity_Type = 'JobCard'";
            //}


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


                    try {

                        if(Task_Status.equals("Unplanned")) {
                            taskProvider = new TaskProvider(TaskId, Frequency_Id, Site_Location_Id, Assigned_To_User_Id, Asset_Id, From_Id, formatDate(parseDate(UnplannedTime)), EndDateTime, Asset_Code, Asset_Name, Asset_Location, Asset_Status, Activity_Name, Task_Status,Group_Name,null,UpdatedStatus);
                            taskProviders.add(taskProvider);
                        }else {
                            taskProvider = new TaskProvider(TaskId, Frequency_Id, Site_Location_Id, Assigned_To_User_Id, Asset_Id, From_Id, StartDateTime,  formatDate(parseDate(UnplannedTime)), Asset_Code, Asset_Name, Asset_Location, Asset_Status, Activity_Name, Task_Status,Group_Name,null,UpdatedStatus);
                            taskProviders.add(taskProvider);
                        }

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



        /*
        protected String doInBackground(String... params) {
            db = myDb.getWritableDatabase();
            if(myDb.Verify(User_Id)==1){
                 //completedQuery = "SELECT a.Group_Name,b.* FROM User_Group a,Task_Details b WHERE a.User_Group_Id=b.Assigned_To_User_Group_Id and b.Assigned_To_User_Group_Id IN ("+myDb.UserGroupId(User_Id)+") AND b.Site_Location_Id='" + SiteId + "'  AND b.Task_Status IN ('Completed','Unplanned') AND b.Verified=1";
            }else {
                 completedQuery = "SELECT a.Group_Name,b.* FROM User_Group a,Task_Details b WHERE a.User_Group_Id=b.Assigned_To_User_Group_Id and b.Assigned_To_User_Group_Id IN ("+myDb.UserGroupId(User_Id)+") AND b.Site_Location_Id='" + SiteId + "'  AND b.Task_Status IN ('Completed','Unplanned','Delayed')";
            }

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


                    try {

                        if(Task_Status.equals("Unplanned")) {
                            taskProvider = new TaskProvider(TaskId, Frequency_Id, Site_Location_Id, Assigned_To_User_Id, Asset_Id, From_Id, formatDate(parseDate(UnplannedTime)), EndDateTime, Asset_Code, Asset_Name, Asset_Location, Asset_Status, Activity_Name, Task_Status,Group_Name,null,UpdatedStatus);
                            taskProviders.add(taskProvider);
                        }else {
                            taskProvider = new TaskProvider(TaskId, Frequency_Id, Site_Location_Id, Assigned_To_User_Id, Asset_Id, From_Id, StartDateTime,  formatDate(parseDate(UnplannedTime)), Asset_Code, Asset_Name, Asset_Location, Asset_Status, Activity_Name, Task_Status,Group_Name,null,UpdatedStatus);
                            taskProviders.add(taskProvider);
                        }

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
*/
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
            mListener.onComplete(count+"");
        }
    }

    public static interface OnCompleteListener{
        public abstract void onComplete(String count);
    }
    private CompletedTask.OnCompleteListener mListener;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        System.out.println("in onAttach");
        Activity activity;
        if (context instanceof Activity){
            activity = (Activity)context;

            try{
                this.mListener = (CompletedTask.OnCompleteListener)activity;
            }catch (final ClassCastException e){
                throw new ClassCastException(activity.toString()+"must implement OnCompleteListener");
            }

        }
    }
}
