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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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
public class MissedTask extends Fragment {
    DatabaseHelper myDb;
    SQLiteDatabase db;

    private ListView lv;
    TaskDataAdapter taskDataAdapter;
    String SiteId,User_Id,Scan_Type = " ";
    SharedPreferences settings;
    List<TaskProvider> taskProviders;
    TaskProvider taskProvider;
    LinearLayout linlaHeaderProgress;
    Calendar calenderCurrent;
    Date LimitTime;
    String id, assetId, TaskId,UpdatedStatus, Frequency_Id,Group_Name, UnplannedTime, Site_Location_Id,Assigned_To_User_Group_Id, Assigned_To_User_Id, Asset_Id, From_Id, StartDateTime, EndDateTime, Asset_Code, Asset_Name, Asset_Location, Asset_Status, Activity_Name, Task_Status;

    String StartDate,EndDate,TaskIdList,assetCode,assetName,listFrequnecyId,User_Group_Id,listFromId,activityName;

    int ScanValue=0;
    public MissedTask() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myDb = new DatabaseHelper(getActivity());
        settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        taskProviders = new ArrayList<TaskProvider>();
        User_Id = settings.getString("userId",null);
        Scan_Type = myDb.ScanType(User_Id);
        SiteId = myDb.Site_Location_Id(User_Id);
        calenderCurrent = Calendar.getInstance();

        View view = inflater.inflate(R.layout.fragment_missed_task, container, false);

        linlaHeaderProgress = (LinearLayout) view.findViewById(R.id.linlaHeaderProgress);
        taskDataAdapter = new TaskDataAdapter(getContext(), R.layout.list_item);
        lv = (ListView)view.findViewById(R.id.list_Missed);
        lv.setAdapter(taskDataAdapter);

        //Missed To Delayed Tasks
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    if (!Scan_Type.equals("QR")) {
                        Toast.makeText(getActivity(), "Tap device with NFC tag", Toast.LENGTH_SHORT).show();

                    } else {
                        for (int j = 0; j < parent.getChildCount(); j++)
                            parent.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
                        assetName = taskDataAdapter.getItem(position).getAsset_Name();
                        StartDate = taskDataAdapter.getItem(position).getStartDateTime();
                        EndDate = taskDataAdapter.getItem(position).getEndDateTime();
                        listFrequnecyId = taskDataAdapter.getItem(position).getFrequency_Id();
                        User_Group_Id = taskDataAdapter.getItem(position).getAssigned_To_User_Group_Id();
                        assetCode = taskDataAdapter.getItem(position).getAsset_Code();
                        listFromId = taskDataAdapter.getItem(position).getFrom_Id();
                        activityName = taskDataAdapter.getItem(position).getActivity_Name();
                        assetId = taskDataAdapter.getItem(position).getAsset_Id();
                        TaskIdList = taskDataAdapter.getItem(position).getTaskId();

                        //if (calenderCurrent.getTime().after(parseDate(StartDate)) && calenderCurrent.getTime().before(parseDate(EndDate))) {
                            ScanValue=1;
                            IntentIntegrator integrator = new IntentIntegrator(getActivity());
                            integrator.setPrompt("Scan a QRcode");
                            integrator.setOrientationLocked(true);
                            Intent intent = integrator.createScanIntent();
                            startActivityForResult(intent, 49374);
                       /* } else {
                            Toast.makeText(getContext(), "Wait for Scheduled Time. ", Toast.LENGTH_SHORT).show();
                        }*/
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("LoG@",e.getMessage());
                }
               /* try {
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
                    String UserGroupId = taskDataAdapter.getItem(position).getAssigned_To_User_Group_Id();

                    Intent intent = new Intent(getContext(), DynamicForm.class);
                    intent.putExtra("Form_Id", listFromId);
                    intent.putExtra("TaskId", TaskIdList);
                    intent.putExtra("AssetName", assetName);
                    intent.putExtra("ActivityName", activityName);
                    intent.putExtra("AssetId", assetId);
                    intent.putExtra("FrequencyId", listFrequnecyId);
                    intent.putExtra("StartDate", StartDate);
                    intent.putExtra("AssetCode", assetCode);
                    intent.putExtra("User_Group_Id",UserGroupId);
                    intent.putExtra("Completed", "Missed");
                    intent.putExtra("Status","Delayed");
                    startActivity(intent);
                    getActivity().finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
            }
        });


        try {
            new AsyncTaskRunner().execute();
        }catch (Exception e){
            e.printStackTrace();
        }

        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_delayed, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (R.id.qrcodedelay == item.getItemId()) {
            ScanValue=2;
            IntentIntegrator integrator = new IntentIntegrator(getActivity());
            integrator.setPrompt("Scan a QRcode");
            integrator.setOrientationLocked(true);
            Intent intent = integrator.createScanIntent();
            startActivityForResult(intent, 49374);
        }

        return super.onOptionsItemSelected(item);
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
            Log.d("aa92","ERROR==" + e);
            Toast.makeText(getActivity(), "Error code: aa92", Toast.LENGTH_SHORT).show();
        }
    }
    //NFC END


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        Log.d("ScanOnActivity", "123 " + result.getContents() + ", " + ScanValue + ", " + data + ", " + myDb.UserGroupId(User_Id));
        if (ScanValue==1) { //List View
            if (result.getContents() != null) {
                if (result.getContents().equals(assetCode)) {
                    try {

                        Intent intent = new Intent(getContext(), DynamicForm.class);
                        intent.putExtra("Form_Id", listFromId);
                        intent.putExtra("TaskId", TaskIdList);
                        intent.putExtra("AssetName", assetName);
                        intent.putExtra("ActivityName", activityName);
                        intent.putExtra("AssetId", assetId);
                        intent.putExtra("FrequencyId", listFrequnecyId);
                        intent.putExtra("StartDate",StartDate);
                        intent.putExtra("AssetCode", assetCode);
                        Log.d("333dsfadsf","  "+User_Group_Id);
                        intent.putExtra("User_Group_Id",User_Group_Id);
                        intent.putExtra("Completed", "Missed");
                        intent.putExtra("Status","Delayed");
                        startActivity(intent);
                        getActivity().finish();

                    } catch (Exception e) {
                        Log.d("pt364","ERROR==" + e);
                        Toast.makeText(getContext(), "Error code: pt364", Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(getContext(), "Wrong Barcode For the Activity Selected", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(getContext(), "No barcode Found", Toast.LENGTH_LONG).show();

        }
        else if (ScanValue==2) { // QR Code
            if (data != null) {
                if (myDb.getassetCount(result.getContents()) != 0) {
                    Log.d("AssetTask","12 "+myDb.getassetTaskCount(result.getContents(), myDb.UserGroupId(User_Id))+myDb.getassetStatus(result.getContents()));
                    if (myDb.getassetTaskCount(result.getContents(), myDb.UserGroupId(User_Id)) >= 1) {

                            try {

                                ArrayList<String> assetList = new ArrayList<>();
                                ArrayList<Integer> taskId = new ArrayList<>();
                                Log.d("FdsafdasfMiss", "//" + taskDataAdapter.getCount());
                                for (int i = 0; i < taskDataAdapter.getCount(); i++) {
                                    taskDataAdapter.getItem(i);
                                    taskId.add(i);
                                }

                                if (taskId.size() == 0) {
                                    Toast.makeText(getActivity(), "No task for this asset is Missed.", Toast.LENGTH_LONG).show();
                                } else if (taskId.size() > 1) {
                                    //Log.d("Task"," is greater than 1");
                                    Intent intent = new Intent(getContext(), MissedDelayTask.class);
                                    intent.putExtra("AssetCode", result.getContents());
                                    intent.putExtra("Completed", "Missed");
                                    startActivity(intent);
                                    getActivity().finish();

                                } else {
                                    //Log.d("Task"," is = 1");
                                    String assetName = taskDataAdapter.getItem(0).getAsset_Name();
                                    String StartDate = taskDataAdapter.getItem(0).getStartDateTime();
                                    String EndDate = taskDataAdapter.getItem(0).getEndDateTime();
                                    String listFrequnecyId = taskDataAdapter.getItem(0).getFrequency_Id();
                                    String assetCode = taskDataAdapter.getItem(0).getAsset_Code();
                                    String listFromId = taskDataAdapter.getItem(0).getFrom_Id();
                                    String activityName = taskDataAdapter.getItem(0).getActivity_Name();
                                    assetId = taskDataAdapter.getItem(0).getAsset_Id();
                                    String TaskIdList = taskDataAdapter.getItem(0).getTaskId();
                                    String UserGroupId = taskDataAdapter.getItem(0).getAssigned_To_User_Group_Id();

                                    Intent intent = new Intent(getContext(), DynamicForm.class);
                                    intent.putExtra("Form_Id", listFromId);
                                    intent.putExtra("TaskId", TaskIdList);
                                    intent.putExtra("AssetName", assetName);
                                    intent.putExtra("ActivityName", activityName);
                                    intent.putExtra("AssetId", assetId);
                                    intent.putExtra("FrequencyId", listFrequnecyId);
                                    intent.putExtra("StartDate", StartDate);
                                    intent.putExtra("AssetCode", assetCode);
                                    intent.putExtra("User_Group_Id", UserGroupId);
                                    intent.putExtra("Completed", "Missed");
                                    intent.putExtra("Status", "Delayed");
                                    startActivity(intent);
                                    getActivity().finish();
                                }

                            } catch (Exception e) {
                                Log.d("pt453", "ERROR==" + e);
                                e.printStackTrace();
                                Toast.makeText(getContext(), "Error code: pt453", Toast.LENGTH_SHORT).show();
                            }

                    }
                    else{
                        Toast.makeText(getContext(), "No Task Found for this Asset", Toast.LENGTH_LONG).show();}
                }
                else{
                    Toast.makeText(getContext(), "Wrong Barcode Asset", Toast.LENGTH_LONG).show();}
            }
            else {
                Toast.makeText(getContext(), "No barcode Found", Toast.LENGTH_LONG).show();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


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

            try {
                db = myDb.getWritableDatabase();

                String pendingQuery = "SELECT ug.Group_Name," +
                        "td.* " +
                        "FROM Task_Details td " +
                        "LEFT JOIN User_Group ug ON " +
                        "ug.User_Group_Id=td.Assigned_To_User_Group_Id " +
                        "LEFT JOIN Asset_Status asst ON " +
                        "asst.Status = td.Asset_Status " +
                        "WHERE td.Assigned_To_User_Group_Id IN ("+myDb.UserGroupId(User_Id)+") " +
                        "AND td.Site_Location_Id='"+SiteId+"' AND asst.Task_State = 'A'  AND td.Task_Status ='Missed'  AND td.RecordStatus != 'D'";

                Cursor cursor = db.rawQuery(pendingQuery, null);
                Log.d("MissedCount", cursor.getCount() + "");
                if (cursor.moveToFirst()) {
                    do {

                        TaskId = cursor.getString(cursor.getColumnIndex("Auto_Id"));
                        Frequency_Id = cursor.getString(cursor.getColumnIndex("Activity_Frequency_Id"));
                        Site_Location_Id = cursor.getString(cursor.getColumnIndex("Site_Location_Id"));
                        Assigned_To_User_Id = cursor.getString(cursor.getColumnIndex("Assigned_To_User_Id"));
                        Assigned_To_User_Group_Id = cursor.getString(cursor.getColumnIndex("Assigned_To_User_Group_Id")) ;
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
                            if(parseDate(StartDateTime).before(LimitTime) || parseDate(StartDateTime).equals(LimitTime)) {

                                taskProvider = new TaskProvider(TaskId,Frequency_Id,Site_Location_Id,Assigned_To_User_Id,Asset_Id,From_Id,StartDateTime,EndDateTime,Asset_Code,Asset_Name,Asset_Location,Asset_Status,Activity_Name,Task_Status,Group_Name,Assigned_To_User_Group_Id,UpdatedStatus);
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
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("aa156", "ERROR==" + e);
            }

            return null;
        }


        @Override
        protected void onPostExecute(String result) {
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
            new TaskDetails().setMissed(true);
            new PendingTask().setMissed(true);
            mListener.onMissed(count+"");
            for(int i = 0; i < taskDataAdapter.getCount(); i++){
                taskDataAdapter.getItem(i);
            }


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
        public abstract void onMissed(String count);
    }
    private MissedTask.OnCompleteListener mListener;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        System.out.println("in onAttach");
        Activity activity;
        if (context instanceof Activity){
            activity = (Activity)context;

            try{
                this.mListener = (MissedTask.OnCompleteListener)activity;
            }catch (final ClassCastException e){
                throw new ClassCastException(activity.toString()+"must implement OnCompleteListener");
            }

        }
    }


}