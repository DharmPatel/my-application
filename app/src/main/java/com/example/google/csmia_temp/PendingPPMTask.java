package com.example.google.csmia_temp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

public class PendingPPMTask extends Fragment {
    RecyclerView recyclerView;
    public List<PpmTaskProvider> listItems;
    RecyclerView.Adapter adapter;
    LinearLayout ProgressBarLayout;
    int listcount;
    String building_code,floor_code,room_area;
    private ProgressDialog pDialog;
    private ProgressBar mProgress;
    DatabaseHelper myDb;
    SQLiteDatabase db;
    PPMTaskAdapter ppmTaskAdapter;
    Calendar calenderCurrent;
    int ScanValue;
    PpmTaskProvider ppmTaskProvider;
    String Scan_Type = " ",User_Id,Task_Id,Old_Task_Id,Form_Id,updatedStatus, Site_Location_Id,activity_frequency,task_date,task_end_date,task_status,asset_activity_linking_id,timestartson,Activity_Name,Asset_Code,Asset_Name,Asset_Location,Asset_Type,Status,Assigned_To_User_Group_Id,Group_Name;
    Date dateTimeStart,dateTimeEnd,LimitTime;
    int count = 0;
    int activity_duration,grace_duration_before,grace_duration_after;
    public PendingPPMTask(){
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pending_ppm_task, container, false);
        myDb=new DatabaseHelper(getActivity());
        db= myDb.getWritableDatabase();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        User_Id = settings.getString("userId", null);
        Scan_Type = myDb.ScanType(User_Id);
        calenderCurrent = Calendar.getInstance();
        recyclerView = (RecyclerView)view.findViewById(R.id.pending_list);
        recyclerView.setHasFixedSize(true);
        DividerItemDecoration myDivider = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        myDivider.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.list_divider));
        recyclerView.addItemDecoration(myDivider);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listItems =new ArrayList<>();
        adapter = new PPMTaskAdapter(listItems,getActivity());
        recyclerView.setAdapter(adapter);
        ProgressBarLayout = (LinearLayout) view.findViewById(R.id.linlaHeaderProgress);
        pDialog = new ProgressDialog(getContext());
        mProgress = (ProgressBar)view.findViewById(R.id.pbHeaderProgress);
        new AsyncTaskRunner().execute();
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                try {
                    if (!Scan_Type.equals("QR")){
                        Toast.makeText(getActivity(), "Tap device with NFC tag", Toast.LENGTH_SHORT).show();
                    }else {
                        ppmTaskProvider = listItems.get(position);
                        task_date = ppmTaskProvider.getTask_Scheduled_Date();
                        Form_Id = ppmTaskProvider.getForm_id();
                        Asset_Code = ppmTaskProvider.getAsset_Code();
                        Task_Id = ppmTaskProvider.getTaskId();
                        Old_Task_Id = ppmTaskProvider.getOldTaskId();
                        Asset_Name = ppmTaskProvider.getAsset_Name();
                        Activity_Name = ppmTaskProvider.getActivity_Name();
                        Assigned_To_User_Group_Id = ppmTaskProvider.getAssigned_To_User_Group_Id();
                        task_end_date = ppmTaskProvider.getTaskEndTime();
                        Log.d("jfvjbhv",task_date+":"+task_end_date);
                        if (calenderCurrent.getTime().after(parseDate(task_date)) && calenderCurrent.getTime().before(parseDate(task_end_date))) {
                            ScanValue = 1;
                            IntentIntegrator integrator = new IntentIntegrator(getActivity());
                            integrator.setPrompt("Scan a QRcode");
                            integrator.setOrientationLocked(true);
                            Intent intent = integrator.createScanIntent();
                            startActivityForResult(intent, 49374);
                        }else {
                            Toast.makeText(getContext(), "Wait for Scheduled Time. ", Toast.LENGTH_SHORT).show();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        return view;
    }

    public class AsyncTaskRunner extends AsyncTask<List<PpmTaskProvider>, List<PpmTaskProvider>, List<PpmTaskProvider>>{

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
            ProgressBarLayout.setVisibility(View.GONE);
            adapter = new PPMTaskAdapter(listItems,getActivity());
            recyclerView.setAdapter(adapter);

            Collections.sort(listItems, new Comparator<PpmTaskProvider>() {
                @Override
                public int compare(PpmTaskProvider o1, PpmTaskProvider o2) {
                    return o1.getTask_Scheduled_Date().compareTo(o2.getTask_Scheduled_Date());
                }
            });

            new ppm_activity().setPPMPending(true);
            mListener.onPending(listItems.size()+"");
        }
    }

    private List<PpmTaskProvider> getPPMPendingTasks() {
        try {

            Calendar calLimitTime = Calendar.getInstance();
            calLimitTime.setTime(calenderCurrent.getTime());
            calLimitTime.add(Calendar.MONTH, 1);
            LimitTime = calLimitTime.getTime();

            myDb=new DatabaseHelper(getActivity());
            db= myDb.getWritableDatabase();
            String pending_query =  "SELECT   DISTINCT ppm.Auto_Id ,\n" +
                    " ppm.Old_PPM_Id ,\n" +
                    " ppm.Site_Location_Id , \n" +
                    " ppm.Activity_Frequency , \n" +
                    " ppm.Task_Date , \n" +
                    " ppm.Task_End_Date , \n" +
                    " ppm.Task_Status , \n" +
                    " ppm.Asset_Activity_Linking_Id , \n" +
                    " ppm.Timestartson , \n" +
                    " ppm.Activity_Duration , \n" +
                    " ppm.Grace_Duration_Before , \n" +
                    " ppm.Grace_Duration_After,\n" +
                    " ppm.UpdatedStatus,\n" +
                    " am.Activity_Name,\n" +
                    " am.Form_Id,\n" +
                    " ad.Asset_Code,\n" +
                    " ad.Asset_Name,\n" +
                    " ad.Asset_Location,\n" +
                    " ad.Asset_Type,\n" +
                    " ad.Status,\n" +
                    " aaa.Assigned_To_User_Group_Id,\n" +
                    " ug.Group_Name,\n" +
                    " al.*\n " +
                    "FROM ppm_task ppm \n" +
                    "LEFT JOIN asset_activity_assignedto aaa \n" +
                    "ON aaa.Asset_Activity_Linking_Id = ppm.Asset_Activity_Linking_Id\n" +
                    "LEFT JOIN asset_activity_linking aal \n" +
                    "ON aal.Auto_Id = ppm.Asset_Activity_Linking_Id \n" +
                    "LEFT JOIN asset_details ad \n" +
                    "ON ad.Asset_Id = aal.Asset_Id \n" +
                    "LEFT JOIN activity_master am \n" +
                    "ON am.Auto_Id = aal.Activity_Id \n" +
                    "LEFT JOIN User_Group ug \n" +
                    "ON ug.User_Group_Id = aaa.Assigned_To_User_Group_Id \n" +
                    "LEFT JOIN Asset_Location al \n " +
                    "ON al.Asset_Id = ad.Asset_Id " +
                    "WHERE ppm.task_status = 'WO Generated' and aaa.Assigned_To_User_Group_Id IN ("+myDb.UserGroupId(User_Id)+") AND  ppm.site_location_id ='"+myDb.Site_Location_Id(User_Id)+"' GROUP by ppm.Auto_Id";
            Log.d("TestingQuery",pending_query);
            Cursor cursor= db.rawQuery(pending_query, null);

            Log.d("TestingQueryCount",cursor.getCount()+"");
            if (cursor.moveToFirst()){
                do {

                    //Log.d("GetAllCount", cursor.getColumnName(0));
                    Task_Id = cursor.getString(cursor.getColumnIndex("Auto_Id"));
                    Old_Task_Id = cursor.getString(cursor.getColumnIndex("Old_PPM_Id"));
                    Site_Location_Id = cursor.getString(cursor.getColumnIndex("Site_Location_Id"));
                    activity_frequency = cursor.getString(cursor.getColumnIndex("Activity_Frequency"));
                    task_date = cursor.getString(cursor.getColumnIndex("Task_Date"));
                    task_end_date = cursor.getString(cursor.getColumnIndex("Task_End_Date"));
                    task_status = cursor.getString(cursor.getColumnIndex("Task_Status"));
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
                    dateTimeEnd = parseDate(task_end_date + " " + timestartson);

                    if (calenderCurrent.getTime().after(dateTimeEnd)) {
                        myDb.updatedPPMTaskDetails(Task_Id, Old_Task_Id,"Missed", "WO Generated", formatDate(calenderCurrent.getTime()), "", User_Id,Assigned_To_User_Group_Id, "");
                    }else {
                        try {
                            if (dateTimeStart.before(LimitTime)||dateTimeStart.equals(LimitTime)){
                                ppmTaskProvider = new PpmTaskProvider(Task_Id,Old_Task_Id,Site_Location_Id,activity_frequency,formatDate(dateTimeStart),task_status,asset_activity_linking_id,formatDate(dateTimeEnd),Activity_Name,Form_Id,Asset_Code,Asset_Name,Asset_Location,Asset_Type,Status,Assigned_To_User_Group_Id,Group_Name,updatedStatus);
                                listItems.add(ppmTaskProvider);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

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



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_ppm, menu);
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
            if (R.id.qrcode == item.getItemId()) {
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        Log.d("ScanValue","1"+result+":"+data+":"+ScanValue);
        if (ScanValue == 1){
            if (result.getContents() != null) {
                if (result.getContents().equals(Asset_Code)) {
                    Intent intent = new Intent(getContext(), DynamicForm.class);
                    intent.putExtra("Form_Id", Form_Id);
                    intent.putExtra("TaskId", Task_Id);
                    intent.putExtra("AssetName", Asset_Name);
                    intent.putExtra("ActivityName", Activity_Name);
                    intent.putExtra("StartDate",task_date);
                    intent.putExtra("AssetCode", Asset_Code);
                    intent.putExtra("User_Group_Id",Assigned_To_User_Group_Id);
                    intent.putExtra("Completed", "WO Generated");
                    intent.putExtra("Status","Completed");
                    intent.putExtra("PPMTask","PPMPending");
                    startActivity(intent);
                    getActivity().finish();
                }
                else {
                    Toast.makeText(getContext(), "Wrong Barcode For the Activity Selected", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(getContext(), "No barcode Found", Toast.LENGTH_SHORT).show();
            }
        }else if (ScanValue == 2){
            ArrayList<Integer> taskId = new ArrayList<>();
            if (result.getContents()!=null){
                if (myDb.getPPMTaskCount(Asset_Code).equals("WO Generated")){

                    Log.d("AdapterCount",adapter.getItemCount()+":"+listItems.size() );
                    for (int i =0; i<listItems.size();i++) {
                        //listItems.get(i);

                        Log.d("AdapterCount", "1: " + result.getContents().equals(listItems.get(i).Asset_Code));
                        if (result.getContents().equals(listItems.get(i).Asset_Code)) {
                            Log.d("NumberCount",i+" "+result.getContents() +" a:"+ listItems.get(i).Asset_Code);

                            //dateTimeStart = parseDate(listItems.get(i).getTask_Scheduled_Date() + " " + timestartson);
                            //dateTimeEnd = parseDate(listItems.get(i).getTaskEndTime() + " " + timestartson);
                            task_date = listItems.get(i).getTask_Scheduled_Date();
                            task_end_date = listItems.get(i).getTaskEndTime();
                            Log.d("AdapterCount2",calenderCurrent.getTime()+ " " + task_date + " && " + task_end_date);
                            Log.d("AdapterCount", "3" + calenderCurrent.getTime().after(parseDate(task_date)) + ":" + calenderCurrent.getTime().before(parseDate(task_end_date)));
                            if (calenderCurrent.getTime().after(parseDate(task_date)) && calenderCurrent.getTime().before(parseDate(task_end_date))) {

                               // Log.d("TaskIdValues",taskId.size()+":"+taskId.add(i));
                                taskId.add(i);
                            }
                        }
                    }
                    Log.d("TaskId",taskId.size()+"");
                            if (taskId.size() == 0) {
                                Toast.makeText(getActivity(),"Wait for schedule time.",Toast.LENGTH_LONG).show();
                            }else if (taskId.size() > 1) {
                                Intent intent = new Intent(getContext(), PPMMultipuleTask.class);
                                intent.putExtra("AssetCode", result.getContents());
                                intent.putExtra("Completed", "WO Generated");
                                /*intent.putExtra("PPMTask","PPMPending");*/
                                startActivity(intent);
                                getActivity().finish();
                            }else {
                                task_date = listItems.get(taskId.get(0)).getTask_Scheduled_Date();
                                Form_Id = listItems.get(taskId.get(0)).getForm_id();
                                Asset_Code = listItems.get(taskId.get(0)).getAsset_Code();
                                Task_Id = listItems.get(taskId.get(0)).getTaskId();
                                Asset_Name = listItems.get(taskId.get(0)).getAsset_Name();
                                Activity_Name = listItems.get(taskId.get(0)).getActivity_Name();
                                Assigned_To_User_Group_Id = listItems.get(taskId.get(0)).getAssigned_To_User_Group_Id();
                                task_end_date = listItems.get(taskId.get(0)).getTaskEndTime();
                                Intent intent = new Intent(getContext(), DynamicForm.class);
                                intent.putExtra("Form_Id", Form_Id);
                                intent.putExtra("TaskId", Task_Id);
                                intent.putExtra("AssetName", Asset_Name);
                                intent.putExtra("ActivityName", Activity_Name);
                                intent.putExtra("StartDate",task_date);
                                intent.putExtra("AssetCode", Asset_Code);
                                intent.putExtra("User_Group_Id",Assigned_To_User_Group_Id);
                                intent.putExtra("Completed", "WO Generated");
                                intent.putExtra("Status","Completed");
                                intent.putExtra("PPMTask","PPMPending");
                                startActivity(intent);
                                getActivity().finish();
                            }
                        }
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

    public String formatDate(Date date) {
        SimpleDateFormat inputParser = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return inputParser.format(date);
    }

    public static interface OnCompleteListener{
        public abstract void onPending(String count);
    }
    private OnCompleteListener mListener;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        System.out.println("in onAttach");
        Activity activity;
        if (context instanceof Activity){
            activity = (Activity)context;

            try{
                this.mListener = (OnCompleteListener)activity;
            }catch (final ClassCastException e){
                throw new ClassCastException(activity.toString()+"must implement OnCompleteListener");
            }

        }
    }

}
