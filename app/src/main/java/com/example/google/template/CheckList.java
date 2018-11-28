package com.example.google.template;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RelativeLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


public class CheckList extends AppCompatActivity {
    android.support.v7.widget.Toolbar toolbar;
    TabLayout tablayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    String companyId,SiteId,UserId,Sitename,Scan_Type,User_Group_Id,User_Id;
    String id;
    DatabaseHelper myDb;
    SQLiteDatabase db;
    NFC nfc;
    SharedPreferences.Editor editorTaskInsert;
    SharedPreferences settings;
    public static final String MY_PREFS_NAME1 = "CheckTaskInsert";
    private static final String TAG = TaskDetails.class.getSimpleName();
    static final boolean LOG = new applicationClass().checkLog();
    RelativeLayout relativeLayout;

    private int[] tabIcons = {
            R.drawable.ic_clear,
            R.drawable.ic_priority,
            R.drawable.ic_done,
            R.drawable.ic_alarm_white_24dp
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);
        try {
            myDb=new DatabaseHelper(getApplicationContext());
            settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            editorTaskInsert= getSharedPreferences(MY_PREFS_NAME1, MODE_PRIVATE).edit();
            companyId = settings.getString("Company_Customer_Id", null);
            UserId = settings.getString("userId",null);
            User_Id = settings.getString("userId",null);
            User_Group_Id = settings.getString("User_Group_Id",null);

            Scan_Type = myDb.ScanType(UserId);
            SiteId = myDb.Site_Location_Id(UserId);
            Sitename = myDb.SiteName(UserId);
            // new insertPPMTask().execute();
            toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_checklist);
            tablayout = (TabLayout) findViewById(R.id.tabLayoutChecklist);
            tablayout.setSelectedTabIndicatorColor(Color.WHITE);
            tablayout.animate();
            relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayoutTaskDetailsPpm);
            viewPager = (ViewPager) findViewById(R.id.viewPagerPpm);
            toolbar.setTitle("Checklist Task v" + BuildConfig.VERSION_NAME);
            setSupportActionBar(toolbar);
            viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
            //viewPagerAdapter.addfragment(new PPMMissedTask(), "Missed Task");
            viewPagerAdapter.addfragment(new CheckListPending(), "Pending Task");
            viewPagerAdapter.addfragment(new CheckListCompleted(), "Completed Task");
            viewPager.setAdapter(viewPagerAdapter);
            /*viewPager.setCurrentItem(0);
            viewPager.setOffscreenPageLimit(1);
            tablayout.setupWithViewPager(viewPager);*/
            try {
                String tabCurrentItem = getIntent().getStringExtra("TAB");
                if(LOG) Log.d(TAG,"tabCurrentItem"+tabCurrentItem);
                if(tabCurrentItem.equalsIgnoreCase("TAB2")){
                    viewPager.setCurrentItem(1);
                    viewPager.setOffscreenPageLimit(1);
                }
                else{
                    viewPager.setCurrentItem(0);
                    viewPager.setOffscreenPageLimit(1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            tablayout.setupWithViewPager(viewPager);
            setupTabIcons();

            new insertTask().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onStart() {
        super.onStart();
        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("sdfsdf11", String.valueOf(tab.getPosition())+":"+tablayout.getSelectedTabPosition());
                tab.getIcon().setColorFilter(Color.parseColor("#0A3560"), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }



    public class insertTask extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... strings) {

            try {
                Calendar calenderCurrent = Calendar.getInstance();
                Calendar calenderEndon = Calendar.getInstance();
                Calendar calenderStarton = Calendar.getInstance();
                Date EndOn, StartOn, dateTimeStart;
                String building_code,floor_code,room_area;
                String Frequency_Auto_Id,Assign_Days,RepeatEveryMonth, Site_Location_Id,Activity_Type, Form_Id, Asset_ID, Assigned_To_User_Group_Id,Assigned_To_User_Id, YearStartson,TimeEndson, TimeStartson, Activity_Name, Asset_Code, Asset_Name, Asset_Location,Task_State, Status,StatusId;
                int Activity_Duration, Grace_Duration_Before, Grace_Duration_After, RepeatEveryDay,RepeatEveryMin;
                String query = "SELECT \n" +
                        " af.Site_Location_Id,\n" +
                        " af.RepeatEveryMin,\n" +
                        " af.RepeatEveryDay,\n" +
                        " af.RepeatEveryMonth,\n" +
                        " af.Frequency_Auto_Id,\n" +
                        " af.YearStartson,\n" +
                        " af.TimeStartson,\n" +
                        " af.TimeEndson,\n" +
                        " af.Grace_Duration_Before,\n" +
                        " af.Grace_Duration_After,\n" +
                        " af.Assign_Days,\n" +
                        " af.Activity_Duration,\n" +
                        " am.Form_Id,\n" +
                        " am.Activity_Type,\n" +
                        " am.Activity_Name,\n" +
                        " aaa.Assigned_To_User_Id,\n" +
                        " aaa.Assigned_To_User_Group_Id,\n" +
                        " aal.Asset_Id,\n" +
                        " ad.Asset_Code,\n" +
                        " ad.Asset_Name,\n" +
                        " ad.Asset_Location,\n" +
                        " ad.Status,\n" +
                        " ad.Asset_Status_Id,\n" +
                        " ast.Task_State,\n" +
                        " al.*\n" +
                        " FROM Activity_Frequency af \n" +
                        " LEFT JOIN Asset_Activity_AssignedTo aaa ON \n" +
                        " aaa.Asset_Activity_Linking_Id = af.Asset_Activity_Linking_Id \n" +
                        " LEFT JOIN Asset_Activity_Linking aal ON \n" +
                        " aal.Auto_Id = af.Asset_Activity_Linking_Id \n" +
                        " LEFT JOIN Activity_Master am ON \n" +
                        " am.Auto_Id = aal.Activity_Id \n" +
                        " LEFT JOIN Asset_Details ad ON \n" +
                        " ad.Asset_Id = aal.Asset_Id \n" +
                        " LEFT JOIN Asset_Status ast ON\n" +
                        " ast.Status = ad.Status\n" +
                        " LEFT JOIN Asset_Location al ON\n" +
                        " al.Asset_Id = ad.Asset_Id\n" +
                        " WHERE \n" +
                        " aaa.Assigned_To_User_Group_Id IN ("+myDb.UserGroupId(User_Id)+") \n" +
                        "AND\n" +
                        "af.Site_Location_Id='"+myDb.Site_Location_Id(User_Id)+"' \n" +
                        "AND \n" +
                        "am.Activity_Type='CheckList'";
                Log.d("TestingValue",query);
                SimpleDateFormat YDMDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat YMDHMDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                try {
                    db = myDb.getWritableDatabase();
                    Cursor taskList = db.rawQuery(query, null);

                    Log.d("TestingValue",query+" 2 "+taskList.getCount());

                    if (taskList.moveToFirst()) {
                        do {
                            Frequency_Auto_Id = taskList.getString(taskList.getColumnIndex("Frequency_Auto_Id"));
                            Site_Location_Id = taskList.getString(taskList.getColumnIndex("Site_Location_Id"));
                            Form_Id = taskList.getString(taskList.getColumnIndex("Form_Id"));
                            Asset_ID = taskList.getString(taskList.getColumnIndex("Asset_Id"));
                            Assigned_To_User_Id = taskList.getString(taskList.getColumnIndex("Assigned_To_User_Id"));
                            Assigned_To_User_Group_Id = taskList.getString(taskList.getColumnIndex("Assigned_To_User_Group_Id"));
                            YearStartson = taskList.getString(taskList.getColumnIndex("YearStartson"));
                            TimeStartson = taskList.getString(taskList.getColumnIndex("TimeStartson"));
                            TimeEndson = taskList.getString(taskList.getColumnIndex("TimeEndson"));
                            Activity_Duration = taskList.getInt(taskList.getColumnIndex("Activity_Duration"));
                            Grace_Duration_Before = taskList.getInt(taskList.getColumnIndex("Grace_Duration_Before"));
                            Grace_Duration_After = taskList.getInt(taskList.getColumnIndex("Grace_Duration_After"));
                            RepeatEveryDay = taskList.getInt(taskList.getColumnIndex("RepeatEveryDay"));
                            RepeatEveryMin = taskList.getInt(taskList.getColumnIndex("RepeatEveryMin"));
                            RepeatEveryMonth = taskList.getString(taskList.getColumnIndex("RepeatEveryMonth"));
                            Assign_Days = taskList.getString(taskList.getColumnIndex("Assign_Days"));
                            Activity_Name = taskList.getString(taskList.getColumnIndex("Activity_Name"));
                            Asset_Code = taskList.getString(taskList.getColumnIndex("Asset_Code"));
                            Asset_Name = taskList.getString(taskList.getColumnIndex("Asset_Name"));
                            building_code = taskList.getString(taskList.getColumnIndex("building_code"));
                            floor_code = taskList.getString(taskList.getColumnIndex("floor_code"));
                            room_area = taskList.getString(taskList.getColumnIndex("room_area"));
                            Asset_Location = building_code+"-"+floor_code+"-"+room_area;
                            Status = taskList.getString(taskList.getColumnIndex("Status"));
                            StatusId = taskList.getString(taskList.getColumnIndex("Asset_Status_Id"));
                            Task_State = taskList.getString(taskList.getColumnIndex("Task_State"));
                            Activity_Type = taskList.getString(taskList.getColumnIndex("Activity_Type"));
                            Log.d("Checklist","1 "+Activity_Type+" 2 "+Asset_Code);

                            if (RepeatEveryDay == 0 && RepeatEveryMin == 0) {
                                String selectQuery = "SELECT  * FROM Task_Details WHERE Asset_Id='" + Asset_ID + "' AND Activity_Frequency_Id='" + Frequency_Auto_Id + "'";
                                Log.d("Unplanned"," "+selectQuery);
                                Cursor cursor = db.rawQuery(selectQuery, null);
                                if (cursor.getCount() == 0) {
                                    String uuid = UUID.randomUUID().toString();
                                    ContentValues contentValues1 = new ContentValues();
                                    contentValues1.put("Auto_Id", uuid);
                                    contentValues1.put("Company_Customer_Id", companyId);
                                    contentValues1.put("Site_Location_Id", SiteId);
                                    contentValues1.put("Activity_Frequency_Id", Frequency_Auto_Id);
                                    contentValues1.put("Task_Scheduled_Date", "[ Unplanned ]");
                                    contentValues1.put("Task_Status", "");
                                    contentValues1.put("Task_Start_At", "");
                                    contentValues1.put("Assigned_To", "U");
                                    contentValues1.put("Assigned_To_User_Id", UserId);
                                    contentValues1.put("Assigned_To_User_Group_Id", Assigned_To_User_Group_Id);
                                    contentValues1.put("Scan_Type", "");
                                    contentValues1.put("Asset_Id", Asset_ID);
                                    contentValues1.put("From_Id", Form_Id);
                                    contentValues1.put("EndDateTime", "");
                                    contentValues1.put("Asset_Code", Asset_Code);
                                    contentValues1.put("Asset_Name", Asset_Name);
                                    contentValues1.put("Asset_Location", Asset_Location);
                                    contentValues1.put("Asset_Status", Status);
                                    contentValues1.put("Activity_Name", Activity_Name);
                                    contentValues1.put("Activity_Type", Activity_Type);
                                    contentValues1.put("Remarks", "");
                                    db = myDb.getWritableDatabase();
                                    db.insert("Task_Details", null, contentValues1);
                                }
                                cursor.close();
                            } else {
                                Log.d("RDASfasdasReasd",RepeatEveryMonth+"");
                                String[] RepeatEveryMonth_OptionList = RepeatEveryMonth.split("\\|");
                                Log.d("RDASfasdasReasd",RepeatEveryMonth_OptionList[0]+"");

                                for (int k = 0; k < RepeatEveryMonth_OptionList.length; k++) {
                                    if (RepeatEveryMonth.equals("null") || Integer.parseInt(RepeatEveryMonth_OptionList[k]) == calenderCurrent.get(Calendar.DAY_OF_MONTH)) {
                                        if (checkDays(Assign_Days)) {
                                            int RepeatHours = 1;
                                            if (RepeatEveryMin != 0) {
                                                RepeatHours = (1440 / RepeatEveryMin);
                                            }
                                            for (Integer i = 0; i < RepeatHours; i++) {
                                                if (i >= 1) {
                                                    Grace_Duration_After = Grace_Duration_After + RepeatEveryMin;
                                                }
                                                if (i >= 1) {
                                                    Grace_Duration_Before = Grace_Duration_Before + RepeatEveryMin;
                                                }
                                                String endtime1[] = TimeEndson.split(":");
                                                String startsOn[] = TimeStartson.split(":");
                                                int endtime = Integer.parseInt(endtime1[0]);
                                                int endtimemins = Integer.parseInt(endtime1[1]);
                                                int starttime = Integer.parseInt(startsOn[0]);

                                                Calendar previousDay = Calendar.getInstance();

                                                if (calenderCurrent.getTime().after(parseDate(YDMDateFormat.format(calenderCurrent.getTime()) + TimeStartson)) && calenderCurrent.getTime().before(parseDate(YDMDateFormat.format(calenderCurrent.getTime()) + TimeEndson))) {
                                                    previousDay.add(Calendar.DAY_OF_MONTH, -1);
                                                    dateTimeStart = parseDate(YDMDateFormat.format(previousDay.getTime()) + " " + TimeStartson);

                                                } else {
                                                    dateTimeStart = parseDate(YDMDateFormat.format(previousDay.getTime()) + " " + TimeStartson);

                                                }
                                                int AfterAddedTime = Activity_Duration + Grace_Duration_After;


                                                calenderStarton.setTime(dateTimeStart);
                                                calenderStarton.add(Calendar.MINUTE, Grace_Duration_Before);
                                                StartOn = calenderStarton.getTime();

                                                calenderEndon.setTime(dateTimeStart);
                                                calenderEndon.add(Calendar.MINUTE, AfterAddedTime);
                                                EndOn = calenderEndon.getTime();

                                                Calendar nextDateEnd = Calendar.getInstance();
                                                nextDateEnd.setTime(parseDateTime(YMDHMDateFormat.format(calenderCurrent.getTime())));
                                                if (starttime > endtime || endtime == 0) {
                                                    nextDateEnd.add(Calendar.DAY_OF_MONTH, 1);
                                                } else {
                                                    nextDateEnd.add(Calendar.DAY_OF_MONTH, 0);
                                                }
                                                nextDateEnd.add(Calendar.HOUR, endtime);
                                                nextDateEnd.add(Calendar.MINUTE, endtimemins);
                                                Date EndDateWithM = nextDateEnd.getTime();

                                                if (parseDate1(new applicationClass().yymmdd()).equals(parseDate1(YearStartson)) || parseDate1(new applicationClass().yymmdd()).after(parseDate1(YearStartson))) {
                                                    if (StartOn.before(EndDateWithM) || StartOn.equals(EndDateWithM)) {

                                                        String selectQuery = "SELECT  * FROM Task_Details WHERE Asset_Id='" + Asset_ID + "' AND Task_Scheduled_Date = '" + YMDHMDateFormat.format(StartOn) + "' AND Activity_Frequency_Id='" + Frequency_Auto_Id + "'";
                                                        Cursor cursor = db.rawQuery(selectQuery, null);
                                                        if (cursor.getCount() == 0) {
                                                            String uuid = UUID.randomUUID().toString();
                                                            ContentValues contentValues1 = new ContentValues();
                                                            contentValues1.put("Auto_Id", uuid);
                                                            contentValues1.put("Company_Customer_Id", companyId);
                                                            contentValues1.put("Site_Location_Id", SiteId);
                                                            contentValues1.put("Activity_Frequency_Id", Frequency_Auto_Id);
                                                            contentValues1.put("Task_Scheduled_Date", YMDHMDateFormat.format(StartOn));
                                                           /* if (!Task_State.equalsIgnoreCase("A"))
                                                                contentValues1.put("Task_Status", "Cancelled");
                                                            else*/
                                                            contentValues1.put("Task_Status", "Pending");
                                                            contentValues1.put("Task_Start_At", "");
                                                            contentValues1.put("Assigned_To", "U");
                                                            contentValues1.put("Assigned_To_User_Id", User_Id);
                                                            contentValues1.put("Assigned_To_User_Group_Id", Assigned_To_User_Group_Id);
                                                            contentValues1.put("Scan_Type", "");
                                                            contentValues1.put("Asset_Id", Asset_ID);
                                                            contentValues1.put("From_Id", Form_Id);
                                                            contentValues1.put("EndDateTime", YMDHMDateFormat.format(EndOn));
                                                            contentValues1.put("Asset_Code", Asset_Code);
                                                            contentValues1.put("Asset_Name", Asset_Name);
                                                            contentValues1.put("Asset_Location", Asset_Location);
                                                            contentValues1.put("Asset_Status", Status);
                                                            contentValues1.put("Activity_Name", Activity_Name);
                                                            contentValues1.put("Activity_Type", Activity_Type);
                                                            contentValues1.put("Remarks", "");
                                                            db = myDb.getWritableDatabase();
                                                            db.insert("Task_Details", null, contentValues1);
                                                            Log.d(TAG, "TaskInerted" + Asset_Name + "  " + Activity_Name + " " + Status + " \n ");
                                                        }
                                                        cursor.close();
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                        }while (taskList.moveToNext());
                    }
                    taskList.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    String taskServerquery = "SELECT * FROM  Task_Details_Server WHERE  Assigned_To_User_Group_Id IN (" + myDb.UserGroupId(User_Id) + ") AND Task_Scheduled_Date LIKE '%" + YDMDateFormat.format(calenderCurrent.getTime()) + "%' AND UpdatedStatus ='no'";
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
                                String Incident = cursor.getString(cursor.getColumnIndex("Incident"));
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
                                contentValues.put("Incident", Incident);
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
                    cursor.close();
                    db.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        public boolean checkDays(String Assign_Days){
            int dAy=0;
            boolean res= false;
            try {
                Calendar previousDay = Calendar.getInstance();
                dAy = previousDay.get(Calendar.DAY_OF_WEEK);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String[] Assign_Days1 =  Assign_Days.split("\\|");
            for(String Value : Assign_Days1){
                Log.d("fdasfdasf","3"+Value+dAy);
                if(Integer.parseInt(Value)==dAy){
                    res=true;
                    break;
                }
                else
                    res=false;
            }

            return res;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            /*editorTaskInsert = settings.edit();
            editorTaskInsert.putString("day",new applicationClass().yymmdd());
            editorTaskInsert.commit();*/
        }
    }

    private void setupTabIcons() {
        tablayout.getTabAt(0).setIcon(tabIcons[0]);
        tablayout.getTabAt(1).setIcon(tabIcons[3]);
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(CheckList.this, HomePage.class);
        intent.putExtra("site_id", SiteId);
        intent.putExtra("User_Id", UserId);
        intent.putExtra("User_Group_Id", User_Group_Id);
        intent.putExtra("Company_Customer_Id",companyId);
        intent.putExtra("siteName", Sitename);
        startActivity(intent);
        getFragmentManager().popBackStackImmediate();
        finish();
        super.onBackPressed();

    }

    public String formatDate(Date date) {
        SimpleDateFormat inputParser = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return inputParser.format(date);
    }
    private Date parseDate1(String date) {
        SimpleDateFormat inputParser = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return inputParser.parse(date);
        } catch (ParseException e) {
            return new Date(0);
        }
    }
    private Date parseDateTime(String date) {
        SimpleDateFormat inputParser = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return inputParser.parse(date);
        } catch (ParseException e) {
            return new Date(0);
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
}
