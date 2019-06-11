package com.example.google.csmia_temp;


import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;

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
public class CheckListPending extends Fragment {
    DatabaseHelper myDb;
    SQLiteDatabase db;
    private ListView lv;
    String id,assetName,activityName,assetId,date,listFrequnecyId,User_Group_Id,activityId,assetStatusId,formId,activityCode;
    TaskDataAdapter taskDataAdapter;
    private String assetCode;

    String companyId,SiteId,User_Id,Scan_Type = " ";
    Date EndOn,StartOn,LimitTime;
    String[] loggerData;
    List<TaskProvider> taskProviders;
    private ProgressDialog pDialog;
    private ProgressBar mProgress;
    LinearLayout linlaHeaderProgress;
    static boolean pending,complete ,cancelled ,missed ;
    TaskProvider taskProvider;
    String listFromId;
    String StartDate,EndDate;
    Calendar calenderCurrent;
    NfcAdapter mNfcAdapter;
    NFC nfc;
    static String dataABC = "";
    boolean running = true;
    int ScanValue=0;
    //NFC END
    private static final String TAG = PendingTask.class.getSimpleName();
    static final boolean LOG = new applicationClass().checkLog();

    String TaskIdList,TaskId,Frequency_Id,Site_Location_Id,Assigned_To_User_Id,Assigned_To_User_Group_Id,Asset_Id,From_Id,StartDateTime,EndDateTime,Asset_Code,Asset_Name ,Asset_Location,Asset_Status,Activity_Name,Group_Name,Task_Status;
    public CheckListPending() {

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        //companyId = settings.getString("SCompany_Customer_Id", null);
        myDb = new DatabaseHelper(getActivity());
        User_Id = settings.getString("userId", null);
        SiteId = myDb.Site_Location_Id(User_Id);
        Scan_Type = myDb.ScanType(User_Id);

        taskProviders = new ArrayList<TaskProvider>();
        View view = inflater.inflate(R.layout.fragment_pending_task, container, false);
        pDialog = new ProgressDialog(getContext());
        mProgress = (ProgressBar)view.findViewById(R.id.pbHeaderProgress);
        linlaHeaderProgress = (LinearLayout) view.findViewById(R.id.linlaHeaderProgress);
        calenderCurrent = Calendar.getInstance();
        lv = (ListView)view.findViewById(R.id.list_Missed);
        taskDataAdapter = new TaskDataAdapter(getContext(), R.layout.list_item);
        lv.setAdapter(taskDataAdapter);
        new AsyncTaskRunner().execute();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    /*if (!Scan_Type.equals("QR")) {
                        Toast.makeText(getActivity(), "Tap device with NFC tag", Toast.LENGTH_SHORT).show();

                    } else {*/
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

                       /* String selectQuery = "SELECT Task_Scheduled_Date,EndDateTime FROM Task_Details WHERE Auto_Id = '"+TaskIdList+"'";
                        db = myDb.getWritableDatabase();
                        Cursor cursor = db.rawQuery(selectQuery, null);
                        if(cursor.moveToNext()){
                            StartDate = cursor.getString(cursor.getColumnIndex("Task_Scheduled_Date"));
                            EndDate = cursor.getString(cursor.getColumnIndex("EndDateTime"));
                        }
                        cursor.close();
                        db.close();*/
                        Log.d("DatetIMEvALUE",parseDate(StartDate)+"    StartDate: " +StartDate +" "+ parseDate(EndDate) + " EndDate:"+EndDate);
                        Log.d("fsdafsdf","  "+calenderCurrent.getTime()+"    "+parseDate(StartDate)+"   "+parseDate(EndDate));
                        //if (calenderCurrent.getTime().after(parseDate(StartDate)) && calenderCurrent.getTime().before(parseDate(EndDate))) {
                            try{
                                Intent intent = new Intent(getContext(), DynamicForm.class);
                                Log.d("Tasdasdasdasd",listFromId);
                            intent.putExtra("Form_Id", listFromId);
                            intent.putExtra("TaskId", TaskIdList);
                            intent.putExtra("AssetName", assetName);
                            intent.putExtra("ActivityName", activityName);
                            intent.putExtra("Asset_Location",Asset_Location);
                            intent.putExtra("AssetId", assetId);
                            intent.putExtra("FrequencyId", listFrequnecyId);
                            intent.putExtra("StartDate",StartDate);
                            intent.putExtra("AssetCode", assetCode);
                            intent.putExtra("User_Group_Id",User_Group_Id);
                            intent.putExtra("Completed", "Pending");
                            intent.putExtra("Status","Completed");
                            intent.putExtra("IntentValue", "CheckList");
                            startActivity(intent);
                            getActivity().finish();
                        } catch (Exception e) {
                            Log.d("pt364","ERROR==" + e);
                            Toast.makeText(getContext(), "Error code: pt364", Toast.LENGTH_SHORT).show();
                        }
                        /*} else {
                            Toast.makeText(getContext(), "Wait for Scheduled Time. ", Toast.LENGTH_SHORT).show();
                        }*/

                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("LoG@",e.getMessage());
                }

            }
        });
        /*try {
            if(!Scan_Type.equals("QR")){
                try {
                    nfc = new NFC();
                    nfc.onCreate();
                    mNfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());
                    if (mNfcAdapter == null) {
                        Toast.makeText(getActivity(),"This device doesn't support NFC!",Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }else {
                        if (!mNfcAdapter.isEnabled()) {
                            Toast.makeText(getActivity(),"NFC is disabled.",Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(),"Tap device with NFC tag",Toast.LENGTH_SHORT).show();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("NfcErrorOnCreate",e.getMessage());
                    Log.d("aa137","ERROR==" + e);
                    Toast.makeText(getActivity(), "Error code: aa137", Toast.LENGTH_SHORT).show();
                }

            }
        }catch (Exception e){
            e.printStackTrace();
            Log.d("LOG1",e.getMessage());
        }*/
        return view;
    }

    public void DataInfo (String dataABC1){
        try {
            ArrayList<String> assetList = new ArrayList<>();
            ArrayList<Integer> taskId = new ArrayList<>();
            for(int i = 0; i < taskDataAdapter.getCount(); i++){
                taskDataAdapter.getItem(i);
                if(dataABC1.equals(taskDataAdapter.getItem(i).Asset_Code)){
                    StartDate = taskDataAdapter.getItem(i).getStartDateTime();
                    EndDate = taskDataAdapter.getItem(i).getEndDateTime();
                    if (calenderCurrent.getTime().after(parseDate(StartDate)) &&calenderCurrent.getTime().before(parseDate(EndDate))) {
                        taskId.add(i);
                    }
                }
            }
            if(LOG){Log.d(TAG,"taskIdSize  "+taskId.size()+"  AssetCode"+dataABC1);}
            if (taskId.size() == 0) {
                String pendingQuery = "SELECT a.Status,a.Asset_Name,a.Asset_Status_Id,b.Task_State FROM Asset_Details a,Asset_Status b WHERE a.Asset_Status_Id = b.Asset_Status_Id and Asset_Code = '" +dataABC1+ "'";
                db=myDb.getReadableDatabase();
                Cursor cursor = db.rawQuery(pendingQuery, null);
                if(cursor.getCount()==0){
                    if(!dataABC1.equals("")) {
                        Toast.makeText(getContext(),"Wrong barcode found.",Toast.LENGTH_LONG).show();
                    }
                }else {
                    if (cursor.moveToFirst()) {
                        do {
                            String TaskStatus = cursor.getString(0);
                            String Asset_Name = cursor.getString(1);
                            String Asset_Status_Id = cursor.getString(2);
                            String Task_State = cursor.getString(3);

                            if (!Task_State.equalsIgnoreCase("A")) {
                                alertDialog(Asset_Name, TaskStatus);
                            } else {
                                noTaskFound(dataABC1);
                            }

                        } while (cursor.moveToNext());
                        running = false;
                        dataABC = "";
                    }
                }
                cursor.close();
                db.close();

            }
            else if(taskId.size()>1){
                Intent intent = new Intent(getContext(), MultipuleTask.class);
                intent.putExtra("AssetCode", dataABC1);
                startActivity(intent);
                running = false;
                dataABC = "";
                getActivity().finish();

            }else {
                assetName = taskDataAdapter.getItem(taskId.get(0)).getAsset_Name();
                //StartDate = taskDataAdapter.getItem(taskId.get(0)).getStartDateTime();
                EndDate = taskDataAdapter.getItem(taskId.get(0)).getEndDateTime();
                listFrequnecyId = taskDataAdapter.getItem(taskId.get(0)).getFrequency_Id();
                User_Group_Id = taskDataAdapter.getItem(taskId.get(0)).getAssigned_To_User_Group_Id();
                assetCode = taskDataAdapter.getItem(taskId.get(0)).getAsset_Code();
                listFromId = taskDataAdapter.getItem(taskId.get(0)).getFrom_Id();
                activityName = taskDataAdapter.getItem(taskId.get(0)).getActivity_Name();
                assetId = taskDataAdapter.getItem(taskId.get(0)).getAsset_Id();
                TaskIdList = taskDataAdapter.getItem(taskId.get(0)).getTaskId();
                Intent intent = new Intent(getContext(), DynamicForm.class);
                intent.putExtra("Form_Id", listFromId);
                intent.putExtra("TaskId", TaskIdList);
                intent.putExtra("AssetName", assetName);
                intent.putExtra("ActivityName", activityName);
                intent.putExtra("AssetId", assetId);
                intent.putExtra("FrequencyId", listFrequnecyId);
                //intent.putExtra("StartDate",StartDate);
                intent.putExtra("AssetCode", assetCode);
                intent.putExtra("User_Group_Id",User_Group_Id);
                intent.putExtra("Completed", "Pending");
                intent.putExtra("IntentValue", "CheckList");
                startActivity(intent);
                running = false;
                dataABC = "";
                getActivity().finish();
            }
        } catch (Exception e) {
            Log.d("pt453", "ERROR==" + e);
            e.printStackTrace();
            Toast.makeText(getContext(), "Error code: pt453", Toast.LENGTH_SHORT).show();
        }

    }

    public void dataValue(String data){
        try {
            dataABC = data;
            Log.d("abcdTagPT",dataABC);
        }catch (Exception e){
            e.printStackTrace();
            Log.d("aa220", "ERROR==" + e);
            Toast.makeText(getActivity(), "Error code: aa220", Toast.LENGTH_SHORT).show();
        }
    }
   /* @Override
    public void onPause() {
        super.onPause();
        try {
            if(!Scan_Type.equals("QR")){
                try {
                    nfc.stopForegroundDispatch(getActivity(), mNfcAdapter);
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("aa334","ERROR==" + e);
                    Toast.makeText(getActivity(), "Error code: aa334", Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.d("LOG3", e.getMessage());
        }
    }*/
   /* @Override
    public void onResume() {
        super.onResume();
        try {
            if(!Scan_Type.equals("QR")){
                try {
                    nfc.setupForegroundDispatch(getActivity(), mNfcAdapter);
                    if(LOG){Log.d(TAG,"AssetCode"+dataABC);}
                    DataInfo(dataABC);
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("NfcErrorOnResume",e.getMessage());
                    Log.d("aa190","ERROR==" + e);
                    Toast.makeText(getActivity(), "Error code: aa190", Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.d("LOG1", e.getMessage());
        }
    }*/
    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        try {
            if (menuVisible){
                new TaskDetails().setPendingNFC(true);
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.d("aa207","ERROR==" + e);
            Toast.makeText(getActivity(), "Error code: aa207", Toast.LENGTH_SHORT).show();
        }
    }
    //NFC END
    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            linlaHeaderProgress.setVisibility(View.VISIBLE);
            pending =false;complete =false;cancelled =false;missed =false;
        }

        @Override
        protected String doInBackground(String... params) {
            Calendar calendar = Calendar.getInstance();
            final SimpleDateFormat simpleDateFormat12 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            Calendar calLimitTime = Calendar.getInstance();
            calLimitTime.setTime(calenderCurrent.getTime());
            calLimitTime.add(Calendar.MINUTE, 480);
            LimitTime = calLimitTime.getTime();

            try {
                myDb=new DatabaseHelper(getActivity());
                db= myDb.getWritableDatabase();

                String pendingQuery = " SELECT a.Group_Name,b.* FROM User_Group a,Task_Details b,Asset_Status c WHERE a.User_Group_Id=b.Assigned_To_User_Group_Id and b.Assigned_To_User_Group_Id IN ("+myDb.UserGroupId(User_Id)+") AND b.Site_Location_Id='"+SiteId+"' AND b.Asset_Status=c.Status AND b.Activity_Type ='CheckList' group by Activity_Frequency_Id";//AND c.Task_State='A'

                Cursor cursor= db.rawQuery(pendingQuery, null);
                Log.d(TAG,"PendingChecklistQuery"+pendingQuery+" "+cursor.getCount());
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

                        Log.d("sdfgsdfgsdfg",StartDateTime+" "+EndDateTime);

                            taskProvider = new TaskProvider(TaskId, Frequency_Id, Site_Location_Id, Assigned_To_User_Id, Asset_Id, From_Id, new applicationClass().yymmddhhmm(), "", Asset_Code, Asset_Name, Asset_Location, Asset_Status, Activity_Name, Task_Status,Group_Name,Assigned_To_User_Group_Id,null);
                            taskProviders.add(taskProvider);

                        /*if (calenderCurrent.getTime().after(parseDate(EndDateTime))) {
                            myDb.updatedTaskDetails(TaskId,"Missed",formatDate(calenderCurrent.getTime()),"",User_Id,"",0);
                        }else {
                            try {
                                if(parseDate(StartDateTime).before(LimitTime) || parseDate(StartDateTime).equals(LimitTime)) {
                                    taskProvider = new TaskProvider(TaskId, Frequency_Id, Site_Location_Id, Assigned_To_User_Id, Asset_Id, From_Id, formatDateTask(parseDate(StartDateTime)), formatDateTask(parseDate(EndDateTime)), Asset_Code, Asset_Name, Asset_Location, Asset_Status, Activity_Name, Task_Status,Group_Name,Assigned_To_User_Group_Id,null);
                                    taskProviders.add(taskProvider);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }*/
                    }
                    while (cursor.moveToNext());
                }
                cursor.close();
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }


            try {

                SimpleDateFormat YDMDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String taskServerquery = "SELECT * FROM  Task_Details_Server WHERE  Assigned_To_User_Group_Id IN (" + myDb.UserGroupId(User_Id) + ") AND Task_Scheduled_Date LIKE '%" + YDMDateFormat.format(calenderCurrent.getTime()) + "%' AND UpdatedStatus ='no'";
                if(db.isOpen()){

                }else {
                    db = myDb.getWritableDatabase();

                }
                Cursor cursor = db.rawQuery(taskServerquery, null);
                if (cursor.getCount() != 0) {
                    if (cursor.moveToFirst()) {
                        do {
                            String Task_Id = cursor.getString(cursor.getColumnIndex("Task_Id"));
                            String Activity_Frequency_Id = cursor.getString(cursor.getColumnIndex("Activity_Frequency_Id"));
                            String Task_Scheduled_Date = cursor.getString(cursor.getColumnIndex("Task_Scheduled_Date"));
                            String Task_Status = cursor.getString(cursor.getColumnIndex("Task_Status"));
                            String Task_Start_At = cursor.getString(cursor.getColumnIndex("Task_Start_At"));
                            String Remarks = cursor.getString(cursor.getColumnIndex("Remarks"));
                            //String Incident = cursor.getString(cursor.getColumnIndex("Incident"));
                            String taskScheduled = Task_Scheduled_Date;
                            Task_Scheduled_Date = formatDate(parseDate(Task_Scheduled_Date));
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("Task_Status", Task_Status);
                            contentValues.put("Auto_Id", Task_Id);
                            if(Task_Status.equals("Completed")) {
                                contentValues.put("Task_Start_At", formatDate(parseDate(Task_Start_At)));
                            }
                            else {
                                contentValues.put("Task_Start_At", Task_Start_At);
                            }
                            contentValues.put("UpdatedStatus", "yes");
                            contentValues.put("Remarks", Remarks);
                           // contentValues.put("Incident", Incident);
                            ContentValues contentValues1 = new ContentValues();
                            contentValues1.put("UpdatedStatus", "yes");

                            long resultset = db.update("Task_Details", contentValues, "Activity_Frequency_Id ='" + Activity_Frequency_Id + "' AND  Task_Scheduled_Date ='" + Task_Scheduled_Date + "'AND Task_Status <> 'Completed'", null);
                            if (resultset == -1)
                                Log.d(TAG,"Task Details not updated ");
                            else {
                                db.update("Task_Details_Server", contentValues1, "Activity_Frequency_Id ='" + Activity_Frequency_Id + "' AND  Task_Scheduled_Date ='" + taskScheduled + "'", null);

                            }
                        } while (cursor.moveToNext());
                    }
                }
                db.delete("Task_Details_Server","UpdatedStatus = 'yes'", null);
                cursor.close();
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
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
            linlaHeaderProgress.setVisibility(View.GONE);
            for(TaskProvider taskProvider1 : taskProviders){
                taskDataAdapter.add(taskProvider1);
                count++;
            }
            new TaskDetails().setPending(true);
            for(int i = 0; i < taskDataAdapter.getCount(); i++){
                taskDataAdapter.getItem(i);
            }

        }

    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
    }
    public int checkCompletedCount(){
        int abc=0;
        String completedQuery = " SELECT * FROM Task_Details WHERE Assigned_To_User_Group_Id IN ("+myDb.UserGroupId(User_Id)+") AND Site_Location_Id='" + SiteId + "'  AND Task_Status IN ('Completed','Unplanned') AND UpdatedStatus='no'";
        db=myDb.getWritableDatabase();
        Cursor cursor = db.rawQuery(completedQuery, null);
        abc=cursor.getCount();
        db.close();
        return abc;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(pending == true && missed == true && cancelled == true && complete == true) {
            if (R.id.qrcode == item.getItemId()) {
                ScanValue=2;
                IntentIntegrator integrator = new IntentIntegrator(getActivity());
                integrator.setPrompt("Scan a QRcode");
                integrator.setOrientationLocked(true);
                Intent intent = integrator.createScanIntent();
                startActivityForResult(intent, 49374);
            }
            if (R.id.unplanned == item.getItemId()) {
                try {

                    if (Scan_Type.equals("QR")) {
                        ScanValue=3;
                        IntentIntegrator integrator = new IntentIntegrator(getActivity());
                        integrator.setPrompt("Scan a QRcode");
                        integrator.setOrientationLocked(true);
                        Intent intent = integrator.createScanIntent();
                        startActivityForResult(intent, 49374);
                    } else if (Scan_Type.equals("NFC")) {
                        Intent intent = new Intent(getContext(), UnplannedActivity.class);
                        intent.putExtra("abc", "unplanned");
                        startActivity(intent);
                        getActivity().finish();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else{
            Toast.makeText(getActivity(),"Task is Loading.Please Wait...",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
/*
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

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
                        intent.putExtra("User_Group_Id",User_Group_Id);
                        intent.putExtra("Completed", "Pending");
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
                    if (myDb.getassetTaskCount(myDb.UserGroupId(User_Id)) >= 1) {
                        try {

                            ArrayList<String> assetList = new ArrayList<>();
                            ArrayList<Integer> taskId = new ArrayList<>();

                            for (int i = 0; i < taskDataAdapter.getCount(); i++) {
                                taskDataAdapter.getItem(i);
                                if (result.getContents().equals(taskDataAdapter.getItem(i).Asset_Code)) {

                                    String selectQuery = "SELECT Task_Scheduled_Date,EndDateTime FROM Task_Details WHERE Auto_Id = '"+taskDataAdapter.getItem(i).getTaskId()+"'";
                                    db = myDb.getWritableDatabase();
                                    Cursor cursor = db.rawQuery(selectQuery, null);
                                    if(cursor.moveToNext()){
                                        StartDate = cursor.getString(cursor.getColumnIndex("Task_Scheduled_Date"));
                                        EndDate = cursor.getString(cursor.getColumnIndex("EndDateTime"));
                                    }
                                    cursor.close();
                                    db.close();


                                    if (calenderCurrent.getTime().after(parseDate(StartDate)) && calenderCurrent.getTime().before(parseDate(EndDate))) {
                                        taskId.add(i);
                                    }
                                }
                            }

                            if (taskId.size() == 0) {

                                String pendingQuery = "SELECT a.Status,a.Asset_Name,a.Asset_Status_Id,b.Task_State FROM Asset_Details a,Asset_Status b WHERE a.Asset_Status_Id=b.Asset_Status_Id and Asset_Code = '" + result.getContents() + "'";
                                db = myDb.getReadableDatabase();
                                Cursor cursor = db.rawQuery(pendingQuery, null);
                                if (cursor.moveToFirst()) {
                                    do {
                                        String TaskStatus = cursor.getString(0);
                                        String Asset_Name = cursor.getString(1);
                                        String Asset_Status_Id = cursor.getString(2);
                                        String Task_State = cursor.getString(3);
                                        if (!Task_State.equalsIgnoreCase("A")) {
                                            alertDialog(Asset_Name, TaskStatus);
                                        } else {
                                            noTaskFound(result.getContents());
                                        }
                                    } while (cursor.moveToNext());
                                }
                                db.close();
                            } else if (taskId.size() > 1) {
                                Intent intent = new Intent(getContext(), MultipuleTask.class);
                                intent.putExtra("AssetCode", result.getContents());
                                startActivity(intent);
                                getActivity().finish();

                            } else {
                                assetName = taskDataAdapter.getItem(taskId.get(0)).getAsset_Name();
                                StartDate = taskDataAdapter.getItem(taskId.get(0)).getStartDateTime();
                                EndDate = taskDataAdapter.getItem(taskId.get(0)).getEndDateTime();
                                listFrequnecyId = taskDataAdapter.getItem(taskId.get(0)).getFrequency_Id();
                                User_Group_Id = taskDataAdapter.getItem(taskId.get(0)).getAssigned_To_User_Group_Id();
                                assetCode = taskDataAdapter.getItem(taskId.get(0)).getAsset_Code();
                                listFromId = taskDataAdapter.getItem(taskId.get(0)).getFrom_Id();
                                activityName = taskDataAdapter.getItem(taskId.get(0)).getActivity_Name();
                                assetId = taskDataAdapter.getItem(taskId.get(0)).getAsset_Id();
                                TaskIdList = taskDataAdapter.getItem(taskId.get(0)).getTaskId();
                                Intent intent = new Intent(getContext(), DynamicForm.class);
                                intent.putExtra("Form_Id", listFromId);
                                intent.putExtra("TaskId", TaskIdList);
                                intent.putExtra("AssetName", assetName);
                                intent.putExtra("ActivityName", activityName);
                                intent.putExtra("AssetId", assetId);
                                intent.putExtra("FrequencyId", listFrequnecyId);
                                intent.putExtra("StartDate", StartDate);
                                intent.putExtra("AssetCode", assetCode);
                                intent.putExtra("User_Group_Id",User_Group_Id);
                                intent.putExtra("Completed", "Pending");
                                startActivity(intent);
                                getActivity().finish();
                            }

                        } catch (Exception e) {
                            Log.d("pt453", "ERROR==" + e);
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error code: pt453", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else
                        Toast.makeText(getContext(), "No Task Found for this Asset", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(getContext(), "Wrong Barcode Asset", Toast.LENGTH_LONG).show();
            }

            else {
                Toast.makeText(getContext(), "No barcode Found", Toast.LENGTH_LONG).show();
            }
        }
        else if (ScanValue==3) { // Unplanned
            if (data != null) {
                if (myDb.getassetCount(result.getContents()) != 0) {
                    if (myDb.getassetTaskCount(myDb.UserGroupId(User_Id)) >= 1) {
                        Intent intent = new Intent(getContext(), UnplannedActivity.class);
                        intent.putExtra("AssetCode", result.getContents());
                        startActivity(intent);
                        getActivity().finish();
                    } else
                        Toast.makeText(getContext(), "No Task Found for this Asset", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(getContext(), "Wrong Barcode Asset", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "No barcode Found", Toast.LENGTH_LONG).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
*/
    private Date parseDate(String date) {
        SimpleDateFormat inputParser = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            return inputParser.parse(date);
        } catch (ParseException e) {
            return new Date(0);
        }
    }


    public void alertDialog(String AssetName,String TaskStatus){

        AlertDialog.Builder alertDialog =new AlertDialog.Builder(getContext());
        alertDialog.setTitle(AssetName);
        alertDialog.setMessage("Asset Status is " + TaskStatus + ".");
        alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                running =true;
                dataABC ="";
            }
        });

        alertDialog.show();
    }
    public void noTaskFound(String assetCodeValue) {
        String taskStatus ="";
        AlertDialog.Builder alertDialog =new AlertDialog.Builder(getContext());
        String querydatavalue = "SELECT * FROM Task_Details WHERE Asset_Code='"+assetCodeValue+"'";
        myDb=new DatabaseHelper(getActivity());
        db=myDb.getReadableDatabase();
        Cursor alert = db.rawQuery(querydatavalue,null);
        if(alert.moveToFirst()){
            do{
                String starttime = alert.getString(alert.getColumnIndex("Task_Scheduled_Date"));
                String endTime = alert.getString(alert.getColumnIndex("EndDateTime"));
                if (calenderCurrent.getTime().after(parseDate(starttime)) && calenderCurrent.getTime().before(parseDate(endTime))) {
                    taskStatus = alert.getString(alert.getColumnIndex("Task_Status"));
                    Log.d("Fsdafsdf",""+taskStatus);
                }

            }while (alert.moveToNext());
        }
        if(taskStatus.equalsIgnoreCase("Completed") ) {
            alertDialog.setMessage("Task is "+taskStatus );
        }else
            alertDialog.setMessage("No Task Found for this Asset");
        alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                running =true;
                dataABC ="";
            }
        });

        alertDialog.show();
    }
    public String formatDate(Date date) {
        SimpleDateFormat inputParser = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return inputParser.format(date);
    }
    public String formatDateTask(Date date) {
        SimpleDateFormat inputParser = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return inputParser.format(date);
    }
}

