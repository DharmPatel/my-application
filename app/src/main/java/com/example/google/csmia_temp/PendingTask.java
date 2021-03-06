package com.example.google.csmia_temp;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
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
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class PendingTask extends Fragment {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ExpandableListView expandableListView;
    ExpandableListViewAdapter expandableListViewAdapter;
    List<String> listTitle;
    Map<String, List<String>> listChild;
    Button OkButton;
    List<String> LocationList = new ArrayList<String>();
    DatabaseHelper myDb;
    SQLiteDatabase db;
    private ListView lv;
    String FloorValue,id,assetName,asetlocation,UserGroupId,activityName,assetId,date,listFrequnecyId,User_Group_Id,activityId,assetStatusId,formId,activityCode;
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
    String CheckedValue = "";
    static String dataABC = "";
    boolean running = true;
    int ScanValue=0;
    private static final String TAG = PendingTask.class.getSimpleName();
    static final boolean LOG = new applicationClass().checkLog();
    String TaskIdList,TaskId,Frequency_Id,Site_Location_Id,Assigned_To_User_Id,Assigned_To_User_Group_Id,Asset_Id,From_Id,StartDateTime,EndDateTime,Asset_Code,Asset_Name ,Asset_Location,Asset_Status,Activity_Name,Group_Name,Task_Status;
    ImageView imageViewFilter;
    ArrayAdapter<String> FloorAdapter;
    List<String> FloorArray = new ArrayList<String>();

    public PendingTask(){}

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        //companyId = settings.getString("SCompany_Customer_Id", null);
        myDb = new DatabaseHelper(getActivity());
        User_Id = settings.getString("userId", null);
        UserGroupId = myDb.UserGroupId(User_Id);
        SiteId = myDb.Site_Location_Id(User_Id);
        Scan_Type = myDb.ScanType(User_Id);
        LocationList = myDb.getTaskLocation(UserGroupId,SiteId);
        taskProviders = new ArrayList<TaskProvider>();
        View view = inflater.inflate(R.layout.fragment_pending_task, container, false);
        pDialog = new ProgressDialog(getContext());
        mProgress = (ProgressBar)view.findViewById(R.id.pbHeaderProgress);
        linlaHeaderProgress = (LinearLayout) view.findViewById(R.id.linlaHeaderProgress);
        calenderCurrent = Calendar.getInstance();
        /*drawerLayout = (DrawerLayout)view.findViewById(R.id.drawer_layout);
        navigationView = (NavigationView)view.findViewById(R.id.drawer_view);*/
        expandableListView = (ExpandableListView)view.findViewById(R.id.submenu);
        OkButton = (Button)view.findViewById(R.id.SubmitBtn);
        /*navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawers();
                return true;
            }
        });*/
        //prepareMenuData();
        //populateExpandableList();
        /*OkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("checkedval"," InJobCard: "+expandableListViewAdapter.getValue());
                CheckedValue = expandableListViewAdapter.getValue();
                if (CheckedValue == null){
                    new AsyncTaskRunner().doInBackground();
                }
                floorData();
                drawerLayout.closeDrawers();
            }
        });
        imageViewFilter = (ImageView)view.findViewById(R.id.imageViewFilter1);
        imageViewFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });*/


        lv = (ListView)view.findViewById(R.id.list_Missed);
        taskDataAdapter = new TaskDataAdapter(getContext(), R.layout.list_item);
        lv.setAdapter(taskDataAdapter);
        new AsyncTaskRunner().execute();
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

                        if (calenderCurrent.getTime().after(parseDate(StartDate)) && calenderCurrent.getTime().before(parseDate(EndDate))) {
                            ScanValue=1;
                            IntentIntegrator integrator = new IntentIntegrator(getActivity());
                            integrator.setPrompt("Scan a QRcode");
                            integrator.setOrientationLocked(true);
                            Intent intent = integrator.createScanIntent();
                            startActivityForResult(intent, 49374);
                        } else {
                            Toast.makeText(getContext(), "Wait for Scheduled Time. ", Toast.LENGTH_SHORT).show();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("LoG@",e.getMessage());
                }

            }
        });
        try {
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
        }
        return view;
    }

    private void populateExpandableList() {

        expandableListViewAdapter = new ExpandableListViewAdapter(getContext(),listTitle,listChild);
        expandableListView.setAdapter(expandableListViewAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Log.d("GroupValues",groupPosition+"");
                String Query ="";

            }
        });
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Log.d("ChildValues",groupPosition+" "+childPosition);
                return true;
            }
        });
        //
    }

    private void prepareMenuData() {
        List<String> TitleList = Arrays.asList("Locations");
        listChild = new HashMap<>();
        Log.d("TaskLocationList",LocationList+"");
        listChild.put(TitleList.get(0),LocationList);
        listTitle = new ArrayList<>(listChild.keySet());
    }

    private void floorData() {
        String Query ="";
        db = myDb.getReadableDatabase();
        taskDataAdapter = new TaskDataAdapter(getContext(), R.layout.list_item);
        lv.setAdapter(taskDataAdapter);
        if (FloorValue != "Select Floor"){
            Query = " SELECT td.*,\n" +
                    "        ug.Group_Name\n" +
                    " FROM Task_Details td \n" +
                    " LEFT JOIN User_Group ug ON \n" +
                    " ug.User_Group_Id=td.Assigned_To_User_Group_Id \n" +
                    " WHERE td.Assigned_To_User_Group_Id IN ("+myDb.UserGroupId(User_Id)+") \n" +
                    " AND td.Site_Location_Id='"+myDb.Site_Location_Id(User_Id)+"' AND td.Asset_Status= 'WORKING'  AND td.Task_Status='Pending' AND  td.Asset_Location IN('"+CheckedValue+"')  AND td.RecordStatus != 'D'";
        }
        Cursor cursor = db.rawQuery(Query, null);
        Log.d("floorDataQuery",Query);
        if (cursor.getCount()!=0){
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
                        if(parseDate(StartDateTime).before(LimitTime) || parseDate(StartDateTime).equals(LimitTime)) {
                            taskProvider = new TaskProvider(TaskId, Frequency_Id, Site_Location_Id, Assigned_To_User_Id, Asset_Id, From_Id, StartDateTime, EndDateTime, Asset_Code, Asset_Name, Asset_Location, Asset_Status, Activity_Name, Task_Status,Group_Name,Assigned_To_User_Group_Id,null);
                            taskDataAdapter.add(taskProvider);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }while (cursor.moveToNext());
            }
        }else {
            new AsyncTaskRunner().execute();
        }
        cursor.close();
        db.close();
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
                String pendingQuery = "SELECT Status,Asset_Name FROM Asset_Details WHERE Asset_Code = '" +dataABC1+ "'";
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

                            if (!TaskStatus.equalsIgnoreCase("Working")) {
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
                intent.putExtra("Asset_Code", dataABC1);
                intent.putExtra("Asset_Location",Asset_Location);
                startActivity(intent);
                running = false;
                dataABC = "";
                getActivity().finish();

            }else {
                assetName = taskDataAdapter.getItem(taskId.get(0)).getAsset_Name();
                StartDate = taskDataAdapter.getItem(taskId.get(0)).getStartDateTime();
                EndDate = taskDataAdapter.getItem(taskId.get(0)).getEndDateTime();
                listFrequnecyId = taskDataAdapter.getItem(taskId.get(0)).getFrequency_Id();
                User_Group_Id = taskDataAdapter.getItem(taskId.get(0)).getAssigned_To_User_Group_Id();
                assetCode = taskDataAdapter.getItem(taskId.get(0)).getAsset_Code();
                listFromId = taskDataAdapter.getItem(taskId.get(0)).getFrom_Id();
                activityName = taskDataAdapter.getItem(taskId.get(0)).getActivity_Name();
                asetlocation = taskDataAdapter.getItem(taskId.get(0)).getAsset_Location();
                assetId = taskDataAdapter.getItem(taskId.get(0)).getAsset_Id();
                TaskIdList = taskDataAdapter.getItem(taskId.get(0)).getTaskId();
                Intent intent = new Intent(getContext(), DynamicForm.class);
                intent.putExtra("Form_Id", listFromId);
                intent.putExtra("TaskId", TaskIdList);
                intent.putExtra("AssetName", assetName);
                intent.putExtra("ActivityName", activityName);
                intent.putExtra("Asset_Location", asetlocation);
                intent.putExtra("AssetId", assetId);
                intent.putExtra("FrequencyId", listFrequnecyId);
                intent.putExtra("StartDate",StartDate);
                intent.putExtra("AssetCode", assetCode);
                intent.putExtra("User_Group_Id",User_Group_Id);
                intent.putExtra("Completed", "Pending");
                intent.putExtra("Status","Completed");
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
    @Override
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
    }
    @Override
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
    }
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
    @SuppressLint("StaticFieldLeak")
    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            linlaHeaderProgress.setVisibility(View.VISIBLE);
            pending =false;complete =false;cancelled =false;missed =false;
        }

        @Override
        protected String doInBackground(String... params) {
            myDb=new DatabaseHelper(getActivity());
            Calendar calendar = Calendar.getInstance();
            final SimpleDateFormat simpleDateFormat12 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            Calendar calLimitTime = Calendar.getInstance();
            calLimitTime.setTime(calenderCurrent.getTime());
            calLimitTime.add(Calendar.MINUTE, 480);
            LimitTime = calLimitTime.getTime();

            try {
                SQLiteDatabase db = myDb.getReadableDatabase();
                String pendingQuery = "SELECT ug.Group_Name," +
                                              "td.* " +
                                              "FROM Task_Details td " +
                                              "LEFT JOIN User_Group ug ON " +
                                              "ug.User_Group_Id=td.Assigned_To_User_Group_Id " +
                                              "LEFT JOIN Asset_Status asst ON " +
                                              "asst.Status = td.Asset_Status "+
                                              "WHERE td.Assigned_To_User_Group_Id IN ("+UserGroupId+") " +
                                              "AND td.Site_Location_Id='"+SiteId+"' AND asst.Task_State = 'A'  AND td.Task_Status='Pending' AND td.RecordStatus != 'D'";

                 Cursor cursor= db.rawQuery(pendingQuery, null);
                if (cursor.getCount()> 0){
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

                            if (calenderCurrent.getTime().after(parseDate(EndDateTime))) {
                                // myDb.updatedTaskDetails(TaskId,"Missed",formatDate(calenderCurrent.getTime()),"",User_Id,"");
                                myDb.updatedTaskDetails(TaskId, "Missed", "Pending", formatDate(calenderCurrent.getTime()), "", User_Id, "",0,"JobCard");
                            }else {
                                try {
                                    if(parseDate(StartDateTime).before(LimitTime) || parseDate(StartDateTime).equals(LimitTime)) {
                                        taskProvider = new TaskProvider(TaskId, Frequency_Id, Site_Location_Id, Assigned_To_User_Id, Asset_Id, From_Id, StartDateTime, EndDateTime, Asset_Code, Asset_Name, Asset_Location, Asset_Status, Activity_Name, Task_Status,Group_Name,Assigned_To_User_Group_Id,null);
                                        taskProviders.add(taskProvider);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        while (cursor.moveToNext());
                    }
                }
                cursor.close();
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                SimpleDateFormat YDMDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String taskServerquery = "SELECT * FROM  Task_Details_Server WHERE  Assigned_To_User_Group_Id IN (" + myDb.UserGroupId(User_Id) + ") AND Task_Scheduled_Date LIKE '%" + YDMDateFormat.format(calenderCurrent.getTime()) + "%' AND UpdatedStatus ='no'";
                if(!db.isOpen()){
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
                            String taskScheduled = Task_Scheduled_Date;
                            Task_Scheduled_Date = formatDate(parseDate(Task_Scheduled_Date));
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("Task_Status", Task_Status);
                            contentValues.put("Auto_Id", Task_Id);
                            if(Task_Status.equals("Completed")|Task_Status.equals("Delayed")) {
                                contentValues.put("Task_Start_At", formatDate(parseDate(Task_Start_At)));
                            }
                            else {
                                contentValues.put("Task_Start_At", Task_Start_At);
                            }
                            contentValues.put("UpdatedStatus", "yes");
                            contentValues.put("Remarks", Remarks);
                            ContentValues contentValues1 = new ContentValues();
                            contentValues1.put("UpdatedStatus", "yes");

                            long resultset = db.update("Task_Details", contentValues, "Activity_Frequency_Id ='" + Activity_Frequency_Id + "' AND  Task_Scheduled_Date ='" + Task_Scheduled_Date + "'AND Task_Status <> 'Completed'", null);
                            if (resultset == -1)
                                Log.d(TAG,"Task Details not updated ");
                            else {
                                db.update("Task_Details_Server", contentValues1, "Activity_Frequency_Id ='" + Activity_Frequency_Id + "' AND  Task_Scheduled_Date ='" + taskScheduled + "'", null);
                                db.delete("Task_Details_Server","UpdatedStatus = 'yes'", null);

                            }
                        } while (cursor.moveToNext());
                    }
                }
                cursor.close();
                //db.close();
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
            mListener.onPending(count+"");
            setPending(true);
            /*for(int i = 0; i < taskDataAdapter.getCount(); i++){
                taskDataAdapter.getItem(i);
            }*/
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_qrcode, menu);
    }
    public void setPending(boolean data) {
        pending = data;
    }
    public void setCancelled(boolean data) {
        cancelled = data;
    }
    public void setComplete(boolean data) {
        complete = data;
    }
    public void setMissed(boolean data) {
        missed = data;
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        try {
            if(Scan_Type.equals("NFC"))
                menu.getItem(1).setVisible(false);
        }catch (Exception e){
            e.printStackTrace();
            Log.d("LOG2",e.getMessage());
        }
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
                        intent.putExtra("Asset_Location", asetlocation);
                        intent.putExtra("User_Group_Id",User_Group_Id);
                        intent.putExtra("Completed", "Pending");
                        intent.putExtra("Status","Completed");
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
                        if (myDb.getassetTaskCount(result.getContents(), myDb.UserGroupId(User_Id)) >= 1) {
                            Log.d("AssetStatusPending",myDb.getassetStatus(result.getContents())+", "+result.getContents());
                            if(myDb.getassetStatus(result.getContents()).contains(",")) {
                                String status[] = myDb.getassetStatus(result.getContents()).split(",");
                                for (int x = 0; x < status.length; x++) {
                                    if (status[x].equalsIgnoreCase("Pending")) {
                                        try {
                                            ArrayList<String> assetList = new ArrayList<>();
                                            ArrayList<Integer> taskId = new ArrayList<>();
                                            Log.d("TaskAdapterValue", " Checjed: " + taskDataAdapter.getCount());
                                            for (int i = 0; i < taskDataAdapter.getCount(); i++) {
                                                taskDataAdapter.getItem(i);
                                                if (result.getContents().equals(taskDataAdapter.getItem(i).Asset_Code)) {
                                                    StartDate = taskDataAdapter.getItem(i).getStartDateTime();
                                                    EndDate = taskDataAdapter.getItem(i).getEndDateTime();

                                                    if (calenderCurrent.getTime().after(parseDate(StartDate)) && calenderCurrent.getTime().before(parseDate(EndDate))) {
                                                        taskId.add(i);
                                                    }
                                                }
                                            }

                                            if (taskId.size() == 0) {
                                                Toast.makeText(getActivity(), "Wait for schedule time.", Toast.LENGTH_LONG).show();

                                            } else if (taskId.size() > 1) {
                                                Intent intent = new Intent(getContext(), MultipuleTask.class);
                                                intent.putExtra("Asset_Code", result.getContents());
                                                intent.putExtra("Completed", "Pending");
                                                startActivity(intent);
                                                getActivity().finish();

                                            } else {
                                                assetName = taskDataAdapter.getItem(taskId.get(0)).getAsset_Name();
                                                StartDate = taskDataAdapter.getItem(taskId.get(0)).getStartDateTime();
                                                EndDate = taskDataAdapter.getItem(taskId.get(0)).getEndDateTime();
                                                listFrequnecyId = taskDataAdapter.getItem(taskId.get(0)).getFrequency_Id();
                                                User_Group_Id = taskDataAdapter.getItem(taskId.get(0)).getAssigned_To_User_Group_Id();
                                                assetCode = taskDataAdapter.getItem(taskId.get(0)).getAsset_Code();
                                                asetlocation = taskDataAdapter.getItem(taskId.get(0)).getAsset_Location();
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
                                                intent.putExtra("Asset_Location", asetlocation);
                                                intent.putExtra("FrequencyId", listFrequnecyId);
                                                intent.putExtra("StartDate", StartDate);
                                                intent.putExtra("AssetCode", assetCode);
                                                intent.putExtra("User_Group_Id", User_Group_Id);
                                                intent.putExtra("Completed", "Pending");
                                                intent.putExtra("Status", "Completed");
                                                startActivity(intent);
                                                getActivity().finish();
                                            }

                                        } catch (Exception e) {
                                            Log.d("pt453", "ERROR==" + e);
                                            e.printStackTrace();
                                            Toast.makeText(getContext(), "Error code: pt453", Toast.LENGTH_SHORT).show();
                                        }
                                    }/* else {
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                                    alertDialog.setMessage("Task is " + myDb.getassetStatus(result.getContents()) + ".");
                                    alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    alertDialog.show();
                                }*/
                                }
                            } else {
                                if(myDb.getassetStatus(result.getContents()).equalsIgnoreCase("Pending")){
                                    try {
                                        ArrayList<String> assetList = new ArrayList<>();
                                        ArrayList<Integer> taskId = new ArrayList<>();
                                        Log.d("Fdsafdasf", "//" + taskDataAdapter.getCount());
                                        for (int i = 0; i < taskDataAdapter.getCount(); i++) {
                                            taskDataAdapter.getItem(i);
                                            if (result.getContents().equals(taskDataAdapter.getItem(i).Asset_Code)) {
                                                StartDate = taskDataAdapter.getItem(i).getStartDateTime();
                                                EndDate = taskDataAdapter.getItem(i).getEndDateTime();

                                                if (calenderCurrent.getTime().after(parseDate(StartDate)) && calenderCurrent.getTime().before(parseDate(EndDate))) {
                                                    taskId.add(i);
                                                }
                                            }
                                        }

                                        if (taskId.size() == 0) {
                                            Toast.makeText(getActivity(), "Wait for schedule time.", Toast.LENGTH_LONG).show();

                                        } else if (taskId.size() > 1) {
                                            Intent intent = new Intent(getContext(), MultipuleTask.class);
                                            intent.putExtra("Asset_Code", result.getContents());
                                            intent.putExtra("Completed", "Pending");
                                            startActivity(intent);
                                            getActivity().finish();

                                        } else {
                                            assetName = taskDataAdapter.getItem(taskId.get(0)).getAsset_Name();
                                            StartDate = taskDataAdapter.getItem(taskId.get(0)).getStartDateTime();
                                            EndDate = taskDataAdapter.getItem(taskId.get(0)).getEndDateTime();
                                            listFrequnecyId = taskDataAdapter.getItem(taskId.get(0)).getFrequency_Id();
                                            User_Group_Id = taskDataAdapter.getItem(taskId.get(0)).getAssigned_To_User_Group_Id();
                                            assetCode = taskDataAdapter.getItem(taskId.get(0)).getAsset_Code();
                                            asetlocation = taskDataAdapter.getItem(taskId.get(0)).getAsset_Location();
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
                                            intent.putExtra("Asset_Location", asetlocation);
                                            intent.putExtra("FrequencyId", listFrequnecyId);
                                            intent.putExtra("StartDate", StartDate);
                                            intent.putExtra("AssetCode", assetCode);
                                            intent.putExtra("User_Group_Id", User_Group_Id);
                                            intent.putExtra("Completed", "Pending");
                                            intent.putExtra("Status", "Completed");
                                            startActivity(intent);
                                            getActivity().finish();
                                        }

                                    } catch (Exception e) {
                                        Log.d("pt453", "ERROR==" + e);
                                        e.printStackTrace();
                                        Toast.makeText(getContext(), "Error code: pt453", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                                    alertDialog.setMessage("Task is " + myDb.getassetStatus(result.getContents()) + ".");
                                    alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    alertDialog.show();
                                }
                            }
                        } else {
                            Toast.makeText(getContext(), "No Task Found for this Asset", Toast.LENGTH_LONG).show();
                        }
                    } else {
                    Toast.makeText(getContext(), "Wrong Barcode Asset", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getContext(), "No barcode Found", Toast.LENGTH_LONG).show();
            }
        }
        else if (ScanValue==3) { // Unplanned
            if (data != null) {
                if (myDb.getassetCount(result.getContents()) != 0) {
                    if (myDb.getassetTaskCount(result.getContents(),myDb.UserGroupId(User_Id)) >= 1) {
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
                }

            }while (alert.moveToNext());
        }
        if(taskStatus.equalsIgnoreCase("Completed") ) {
            alertDialog.setMessage("Task is "+taskStatus );
        }else{
            alertDialog.setMessage("No Task Found for this Asset");
        }
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

    public static interface OnCompleteListener{
        public abstract void onPending(String count);
    }
    private PendingTask.OnCompleteListener mListener;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        System.out.println("in onAttach");
        Activity activity;
        if (context instanceof Activity){
            activity = (Activity)context;

            try{
                this.mListener = (PendingTask.OnCompleteListener)activity;
            }catch (final ClassCastException e){
                throw new ClassCastException(activity.toString()+"must implement OnCompleteListener");
            }

        }
    }


}

