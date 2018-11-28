package com.example.google.template;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class CompletedPPMTask extends Fragment {
    RecyclerView recyclerView;
    private List<PpmTaskProvider> listItems;
    RecyclerView.Adapter adapter;
    DatabaseHelper myDb;
    SQLiteDatabase db;
    PPMTaskAdapter ppmTaskAdapter;
    LinearLayout ProgressBarLayout;
    Calendar calenderCurrent;
    PpmTaskProvider ppmTaskProvider;
    String building_code,floor_code,room_area;
    String User_Id,Auto_Id,Form_Id,updatedStatus, Site_Location_Id,activity_frequency,task_date,task_end_date,task_status,task_done_at,asset_activity_linking_id,timestartson,Activity_Name,Asset_Code,Asset_Name,Asset_Location,Asset_Type,Status,Assigned_To_User_Group_Id,Group_Name;
    Date dateTimeStart,dateTimeEnd,LimitTime;

    int activity_duration,grace_duration_before,grace_duration_after;

    public CompletedPPMTask(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.completed_ppm_task, container, false);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        User_Id = settings.getString("userId", null);
        recyclerView = (RecyclerView)view.findViewById(R.id.completed_list);
        calenderCurrent = Calendar.getInstance();
        ProgressBarLayout = (LinearLayout) view.findViewById(R.id.linlaHeaderProgress);
        recyclerView.setHasFixedSize(true);
        DividerItemDecoration myDivider = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        myDivider.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.list_divider));
        recyclerView.addItemDecoration(myDivider);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listItems =new ArrayList<>();
        adapter = new PPMTaskAdapter(listItems,getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ppmTaskProvider = listItems.get(position);
                Asset_Name = ppmTaskProvider.getAsset_Name();
                //task_date = ppmTaskProvider.getTask_date();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        new AsyncTaskRunner().execute();
        //getPPMPendingTasks();
        return view;
    }

    private class AsyncTaskRunner extends AsyncTask<List<PpmTaskProvider>, List<PpmTaskProvider>, List<PpmTaskProvider>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressBarLayout.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<PpmTaskProvider> doInBackground(List<PpmTaskProvider>... strings) {

            return getPPMPendingTasks();
        }

        @Override
        protected void onPostExecute(List<PpmTaskProvider> s) {
            super.onPostExecute(s);
            int count = 0;
            ProgressBarLayout.setVisibility(View.GONE);
            adapter = new PPMTaskAdapter(listItems,getActivity());
            recyclerView.setAdapter(adapter);
            Collections.sort(listItems, new Comparator<PpmTaskProvider>() {
                @Override
                public int compare(PpmTaskProvider o1, PpmTaskProvider o2) {
                    return o1.getTask_Scheduled_Date().compareTo(o2.getTask_Scheduled_Date());
                }
            });
            Collections.reverse(listItems);

            new ppm_activity().setPPMComplete(true);
            mListener.onComplete(listItems.size()+"");
        }
    }

    private List<PpmTaskProvider> getPPMPendingTasks() {
        try {

            Calendar calLimitTime = Calendar.getInstance();
            calLimitTime.setTime(calenderCurrent.getTime());
            calLimitTime.add(Calendar.MONTH, -1);
            LimitTime = calLimitTime.getTime();

            myDb=new DatabaseHelper(getActivity());
            db= myDb.getWritableDatabase();
            String pending_query =  "SELECT   DISTINCT ppm.Auto_Id , \n" +
                    "          ppm.Site_Location_Id , \n" +
                    "          ppm.Activity_Frequency , \n" +
                    "          ppm.Task_Date , \n" +
                    "          ppm.Task_End_Date , \n" +
                    "          ppm.Task_Status ,ppm.Task_Done_At, \n" +
                    "          ppm.Asset_Activity_Linking_Id , \n" +
                    "          ppm.Timestartson , \n" +
                    "          ppm.Activity_Duration , \n" +
                    "          ppm.Grace_Duration_Before , \n" +
                    "          ppm.Grace_Duration_After,\n" +
                    "          ppm.UpdatedStatus,\n" +
                    "          am.Activity_Name,\n" +
                    "          am.Form_Id,\n" +
                    "          ad.Asset_Code,\n" +
                    "          ad.Asset_Name,\n" +
                    "          ad.Asset_Location,\n" +
                    "          ad.Asset_Type,\n" +
                    "          ad.Status,\n" +
                    "          aaa.Assigned_To_User_Group_Id,\n" +
                    "          ug.Group_Name,\n" +
                    "al.* " +
                    "FROM      ppm_task ppm \n" +
                    "LEFT JOIN asset_activity_assignedto aaa \n" +
                    "ON        aaa.Asset_Activity_Linking_Id = ppm.Asset_Activity_Linking_Id\n" +
                    "LEFT JOIN asset_activity_linking aal \n" +
                    "ON        aal.Auto_Id = ppm.Asset_Activity_Linking_Id \n" +
                    "LEFT JOIN asset_details ad \n" +
                    "ON        ad.Asset_Id = aal.Asset_Id \n" +
                    "LEFT JOIN activity_master am \n" +
                    "ON        am.Auto_Id = aal.Activity_Id \n" +
                    "LEFT JOIN User_Group ug\n" +
                    "ON                ug.User_Group_Id = aaa.Assigned_To_User_Group_Id \n" +
                    "LEFT JOIN Asset_Location al " +
                    "ON al.Asset_Id = ad.Asset_Id " +
                    "WHERE ppm.task_status = 'Completed' and aaa.Assigned_To_User_Group_Id = "+myDb.UserGroupId(User_Id)+" AND  ppm.site_location_id ='"+myDb.Site_Location_Id(User_Id)+"'";
            Log.d("TestingQuery",pending_query);
            Cursor cursor= db.rawQuery(pending_query, null);

            Log.d("TestingQueryCount",cursor.getCount()+"");
            if (cursor.moveToFirst()){
                do {

                    //Log.d("GetAllCount", cursor.getColumnName(0));
                    Auto_Id = cursor.getString(cursor.getColumnIndex("Auto_Id"));
                    Site_Location_Id = cursor.getString(cursor.getColumnIndex("Site_Location_Id"));
                    activity_frequency = cursor.getString(cursor.getColumnIndex("Activity_Frequency"));
                    task_date = cursor.getString(cursor.getColumnIndex("Task_Date"));
                    task_end_date = cursor.getString(cursor.getColumnIndex("Task_End_Date"));
                    task_status = cursor.getString(cursor.getColumnIndex("Task_Status"));
                    task_done_at = cursor.getString(cursor.getColumnIndex("Task_Done_At"));
                    asset_activity_linking_id = cursor.getString(cursor.getColumnIndex("Asset_Activity_Linking_Id"));
                    timestartson =  cursor.getString(cursor.getColumnIndex("Timestartson"));
                    activity_duration = cursor.getInt(cursor.getColumnIndex("Activity_Duration"));
                    grace_duration_before = cursor.getInt(cursor.getColumnIndex("Grace_Duration_Before"));
                    grace_duration_after = cursor.getInt(cursor.getColumnIndex("Grace_Duration_After"));
                    Activity_Name = cursor.getString(cursor.getColumnIndex("Activity_Name"));
                    Form_Id = cursor.getString(cursor.getColumnIndex("Form_Id"));
                    Asset_Code = cursor.getString(cursor.getColumnIndex("Asset_Code"));
                    Asset_Name = cursor.getString(cursor.getColumnIndex("Asset_Name"));
                    building_code = cursor.getString(cursor.getColumnIndex("building_code"));
                    floor_code = cursor.getString(cursor.getColumnIndex("floor_code"));
                    room_area = cursor.getString(cursor.getColumnIndex("room_area"));
                    Asset_Location = building_code+"-"+floor_code+"-"+room_area;
                    Asset_Type = cursor.getString(cursor.getColumnIndex("Asset_Type"));
                    Status = cursor.getString(cursor.getColumnIndex("Status"));
                    Assigned_To_User_Group_Id = cursor.getString(cursor.getColumnIndex("Assigned_To_User_Group_Id"));
                    Group_Name = cursor.getString(cursor.getColumnIndex("Group_Name"));
                    updatedStatus = cursor.getString(cursor.getColumnIndex("UpdatedStatus"));

                    dateTimeStart = parseDate(task_date + " " + timestartson);
                    dateTimeEnd = parseDate(task_done_at);
                    try {
                        if (dateTimeEnd.equals(LimitTime)||dateTimeEnd.after(LimitTime)||updatedStatus.equalsIgnoreCase("no")){
                            ppmTaskProvider = new PpmTaskProvider(Auto_Id,Site_Location_Id,activity_frequency,formatDate(dateTimeStart),task_status,asset_activity_linking_id,task_done_at,Activity_Name,Form_Id,Asset_Code,Asset_Name,Asset_Location,Asset_Type,Status,Assigned_To_User_Group_Id,Group_Name,updatedStatus);
                            listItems.add(ppmTaskProvider);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }while (cursor.moveToNext());
            }

            cursor.close();
            db.close();

        }catch (Exception e){
            e.printStackTrace();
        }
        return listItems;
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
    public static interface OnCompleteListener{
        public abstract void onComplete(String count);
    }
    private CompletedPPMTask.OnCompleteListener mListener;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        System.out.println("in onAttach");
        Activity activity;
        if (context instanceof Activity){
            activity = (Activity)context;

            try{
                this.mListener = (CompletedPPMTask.OnCompleteListener)activity;
            }catch (final ClassCastException e){
                throw new ClassCastException(activity.toString()+"must implement OnCompleteListener");
            }

        }
    }
}
