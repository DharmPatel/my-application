package com.example.google.csmia_temp;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CancelledTask extends Fragment {
    DatabaseHelper myDb;
    SQLiteDatabase db;
    private ListView lv;
    Calendar currentTime;

    TaskDataAdapter taskDataAdapter;
    String companyId, SiteId, User_Id;
    LinearLayout linlaHeaderProgress;
    List<TaskProvider> taskProviders;
    TaskProvider taskProvider;
    Calendar calenderCurrent;
    Date LimitTime;
    String id, UpdatedStatus,Group_Name,TaskId, Frequency_Id, UnplannedTime, Site_Location_Id, Assigned_To_User_Group_Id,Assigned_To_User_Id, Asset_Id, From_Id, StartDateTime, EndDateTime, Asset_Code, Asset_Name, Asset_Location, Asset_Status, Activity_Name, Task_Status;
    EditText remark;
    String remarkET;
    TextView assetNote, assetnameValue;
    String UpdatedStatus1 = "", TaskID, StartTime, EndTime;
    public CancelledTask() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myDb = new DatabaseHelper(getActivity());
        currentTime = Calendar.getInstance();
        final View view = inflater.inflate(R.layout.fragment_cancelled_task, container, false);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        User_Id = settings.getString("userId", null);
        SiteId = myDb.Site_Location_Id(User_Id);

        taskProviders = new ArrayList<TaskProvider>();
        linlaHeaderProgress = (LinearLayout) view.findViewById(R.id.linlaHeaderProgress);
        lv = (ListView) view.findViewById(R.id.list_Cancelled);
        taskDataAdapter = new TaskDataAdapter(getContext(), R.layout.list_item);
        lv.setAdapter(taskDataAdapter);
        calenderCurrent = Calendar.getInstance();
        new AsyncTaskRunner().execute();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    TaskID = taskDataAdapter.getItem(position).getTaskId();
                    StartTime = taskDataAdapter.getItem(position).getStartDateTime();
                    EndTime = taskDataAdapter.getItem(position).getEndDateTime();
                    if (taskDataAdapter.getItem(position).getUpdateStatus()!= null){
                    UpdatedStatus1 = taskDataAdapter.getItem(position).getUpdateStatus();
                    }else {
                        UpdatedStatus1 = "";
                    }
                    Log.d("jksdfkjsdf",UpdatedStatus1+"  "+taskDataAdapter.getItem(position).getTaskId()+" "+taskDataAdapter.getItem(position).getStartDateTime()+" "+taskDataAdapter.getItem(position).getEndDateTime());
                    if (!UpdatedStatus1.equalsIgnoreCase("yes")) {
                        String AssetStatus = taskDataAdapter.getItem(position).getAsset_Status();
                        String AssetName = taskDataAdapter.getItem(position).getAsset_Name();
                        View alertLayout = inflater.inflate(R.layout.cancelled_alert, null);
                        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                        alert.setView(alertLayout);
                        final AlertDialog dialog = alert.create();
                        assetnameValue = (TextView) alertLayout.findViewById(R.id.assetnameId);
                        assetnameValue.setText("Asset Name : "+AssetName);
                        remark = (EditText) alertLayout.findViewById(R.id.remarkET);
                        Button submit = (Button) alertLayout.findViewById(R.id.submitbtn);
                        submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                remarkET = remark.getText().toString();
                                Log.d("jksdfkjsdf","11  "+TaskID+" "+StartTime+" "+EndTime);
                                myDb.updateRemarkValue(TaskID,StartTime,EndTime, remarkET);
                                dialog.dismiss();
                            }
                        });
                        assetNote = (TextView) alertLayout.findViewById(R.id.AssetNote);
                        assetNote.setText("Asset Status: " + AssetStatus);
                        assetNote.setTextColor(Color.RED);

                        dialog.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }
    public void cancelledAlert(){

    }
    //NFC START
    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        try {
            if (menuVisible){
                new TaskDetails().setPendingNFC(false);
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.d("aa96","ERROR==" + e);
            Toast.makeText(getActivity(), "Error code: aa96", Toast.LENGTH_SHORT).show();
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

            Calendar calLimitTime = Calendar.getInstance();
            calLimitTime.setTime(calenderCurrent.getTime());
            calLimitTime.add(Calendar.MINUTE, 480);
            LimitTime = calLimitTime.getTime();


            String cancelledQuery = " SELECT ug.Group_Name," +
                                            "td.* " +
                                            "FROM Task_Details td " +
                                            "LEFT JOIN User_Group ug ON " +
                                            "ug.User_Group_Id=td.Assigned_To_User_Group_Id " +
                                            "LEFT JOIN Asset_Status asst ON " +
                                            "asst.Status = td.Asset_Status " +
                                            "WHERE td.Assigned_To_User_Group_Id IN ("+myDb.UserGroupId(User_Id)+") " +
                                            "AND td.Site_Location_Id='"+SiteId+"' AND td.Task_Status ='Cancelled' AND td.RecordStatus != 'D'";
            db = myDb.getWritableDatabase();
            Cursor cursor = db.rawQuery(cancelledQuery, null);

            Log.d("TestingQureyasd",cancelledQuery+"");
            if (cursor.moveToFirst()) {
                do {

                    TaskId = cursor.getString(cursor.getColumnIndex("Auto_Id"));
                    Frequency_Id = cursor.getString(cursor.getColumnIndex("Activity_Frequency_Id"));
                    Site_Location_Id = cursor.getString(cursor.getColumnIndex("Site_Location_Id"));
                    Assigned_To_User_Id = cursor.getString(cursor.getColumnIndex("Assigned_To_User_Id"));
                    Assigned_To_User_Group_Id = cursor.getString(cursor.getColumnIndex("Assigned_To_User_Group_Id"));
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
                    Log.d("CurrentAssetStatus:",Asset_Status);

                    try {
                        if(currentTime.getTime().after(parseDate(EndDateTime))){
                            myDb.updatedTaskCancelledCart(TaskId);
                        }
                        if(parseDate(StartDateTime).before(LimitTime) || parseDate(StartDateTime).equals(LimitTime)) {
                            taskProvider = new TaskProvider(TaskId, Frequency_Id, Site_Location_Id, Assigned_To_User_Id, Asset_Id, From_Id, StartDateTime, EndDateTime, Asset_Code, Asset_Name, Asset_Location, Asset_Status, Activity_Name, Task_Status,Group_Name,Assigned_To_User_Group_Id, UpdatedStatus);
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


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            int count= 0;
            Collections.sort(taskProviders, new Comparator<TaskProvider>() {
                @Override
                public int compare(TaskProvider taskProvider1, TaskProvider taskProvider2) {
                    return taskProvider1.getStartDateTime().compareTo(taskProvider2.getStartDateTime());
                }

            });

            linlaHeaderProgress.setVisibility(View.GONE);
            for (TaskProvider taskProvider1 : taskProviders) {
                taskDataAdapter.add(taskProvider1);
                count++;
            }
            new TaskDetails().setCancelled(true);
            new PendingTask().setCancelled(true);
            mListener.onCancelled(count+"");
        }

    }

    private Date parseDate(String date) {
        SimpleDateFormat inputParser = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            return inputParser.parse(date);
        } catch (ParseException e) {
            return new Date(0);
        }
    }

    public static interface OnCompleteListener{
        public abstract void onCancelled(String count);
    }
    private CancelledTask.OnCompleteListener mListener;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        System.out.println("in onAttach");
        Activity activity;
        if (context instanceof Activity){
            activity = (Activity)context;

            try{
                this.mListener = (CancelledTask.OnCompleteListener)activity;
            }catch (final ClassCastException e){
                throw new ClassCastException(activity.toString()+"must implement OnCompleteListener");
            }

        }
    }

}
