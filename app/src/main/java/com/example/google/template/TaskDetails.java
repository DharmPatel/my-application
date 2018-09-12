package com.example.google.template;


import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


public class TaskDetails extends AppCompatActivity implements PendingTask.OnCompleteListener, MissedTask.OnCompleteListener, CompletedTask.OnCompleteListener, CancelledTask.OnCompleteListener{
    DatabaseHelper myDb;
    SQLiteDatabase db;
    android.support.v7.widget.Toolbar toolbar;
    TabLayout tablayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    String companyId,SiteId,User_Id,Sitename,Scan_Type;
    static boolean pending,complete ,cancelled ,missed,pendingNFC ;
    NFC nfc;
    NfcAdapter mNfcAdapter;
    RelativeLayout relativeLayout;
    SharedPreferences.Editor editorTaskInsert;
    public static final String MY_PREFS_NAME1 = "CheckTaskInsert";
    SharedPreferences settings;
    private static final String TAG = TaskDetails.class.getSimpleName();
    static final boolean LOG = new applicationClass().checkLog();
    BadgeView MissedBadge, PendingBadge, CompletedBadge, CancelledBadge;

    private int[] tabIcons = {
            R.drawable.ic_clear,
            R.drawable.ic_alarm_white_24dp,
            R.drawable.ic_done,
            R.drawable.ic_priority
    };

    private int[] tabIconsSelected = {
            R.drawable.ic_clear2,
            R.drawable.ic_alarm2,
            R.drawable.ic_done2,
            R.drawable.ic_priority2
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);
        pending =false;complete =false;cancelled =false;missed =false;
        try {
            myDb=new DatabaseHelper(getApplicationContext());
            settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            editorTaskInsert= getSharedPreferences(MY_PREFS_NAME1, MODE_PRIVATE).edit();
            User_Id = settings.getString("userId",null);
            Scan_Type = myDb.ScanType(User_Id);
            SiteId = myDb.Site_Location_Id(User_Id);
            Sitename = myDb.SiteName(User_Id);
            try {
                Log.d(TAG,"daykey"+settings.getString("day", null));
                if(settings.getString("day", null) == null){
                    new insertTask().execute();
                }
                else {
                    if (parseDate1(settings.getString("day", null)).before(parseDate1(new applicationClass().yymmdd()))) {
                        new insertTask().execute();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar1);
            tablayout = (TabLayout) findViewById(R.id.tabLayout);
            tablayout.setSelectedTabIndicatorColor(Color.parseColor("#ffffff"));

            tablayout.animate();
            relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayoutTaskDetails);
            viewPager = (ViewPager) findViewById(R.id.viewPager);
            toolbar.setTitle("Task Details" + BuildConfig.VERSION_NAME);
            setSupportActionBar(toolbar);
            viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
            viewPagerAdapter.addfragment(new MissedTask(), "Missed");
            viewPagerAdapter.addfragment(new PendingTask(), "Pending");
            viewPagerAdapter.addfragment(new CompletedTask(), "Completed");
            viewPagerAdapter.addfragment(new CancelledTask(), "Cancelled");
            viewPager.setAdapter(viewPagerAdapter);

            try {
                String tabCurrentItem = getIntent().getStringExtra("TAB");
                if(LOG) Log.d(TAG,"tabCurrentItem"+tabCurrentItem);
                if(tabCurrentItem.equalsIgnoreCase("TAB3")){
                    viewPager.setCurrentItem(2);
                    viewPager.setOffscreenPageLimit(3);
                }
                else{
                    viewPager.setCurrentItem(1);
                    viewPager.setOffscreenPageLimit(3);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            tablayout.setupWithViewPager(viewPager);
            MissedBadge = new BadgeView(this,((ViewGroup)tablayout.getChildAt(0)).getChildAt(0));
            PendingBadge = new BadgeView(this,((ViewGroup)tablayout.getChildAt(0)).getChildAt(1));
            CompletedBadge = new BadgeView(this,((ViewGroup)tablayout.getChildAt(0)).getChildAt(2));
            CancelledBadge = new BadgeView(this,((ViewGroup)tablayout.getChildAt(0)).getChildAt(3));

            setupTabIcons();
            int tabindex= 0;
            Log.d("Teasdasdasd",tablayout.getSelectedTabPosition()+"");
            Log.d("Teasdasdasd", tablayout.getTabCount()+"");
            while(tabindex < tablayout.getTabCount()){
                if(tablayout.getSelectedTabPosition() == 1){
                    tablayout.getTabAt(1).setIcon(tabIconsSelected[1]);

                }else {
                    tablayout.getTabAt(tabindex).setIcon(tabIcons[tabindex]);
                }

                tabindex++;
            }

            NFCConfiguration();



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
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

    public void NFCConfiguration(){
    if(!Scan_Type.equals("QR")) {
        try {
            nfc = new NFC();
            nfc.onCreate();
            mNfcAdapter = NfcAdapter.getDefaultAdapter(getApplicationContext());
            if (mNfcAdapter == null) {
                Toast.makeText(getApplicationContext(), "This device doesn't support NFC!", Toast.LENGTH_SHORT).show();
            } else {
                if (!mNfcAdapter.isEnabled()) {
                    final Snackbar snackbar = Snackbar
                            .make(relativeLayout, "NFC is disabled", Snackbar.LENGTH_LONG)
                            .setAction("Change Setting.", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                                }
                            });

                    snackbar.setDuration(20000);
                    snackbar.show();
                } else {
                    Toast.makeText(getApplicationContext(), "Tap device with NFC tag", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error code: aa140", Toast.LENGTH_SHORT).show();
        }
    }else {
        if(LOG) Log.d(TAG,"Scan_Type"+Scan_Type);
    }
}
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        try {
            if(!Scan_Type.equals("QR")) {
                Parcelable[] parcelables = intent.getParcelableArrayExtra(mNfcAdapter.EXTRA_NDEF_MESSAGES);
                if(parcelables !=null && parcelables.length>0)
                    nfc.readnfc((NdefMessage)parcelables[0]);
                if(pendingNFC == true) {
                    if(LOG){Log.d(TAG, "QR " + nfc.tagcontent);}
                    new PendingTask().dataValue(nfc.tagcontent);
                    nfc.RES = "";
                }
                else {
                    viewPager.setCurrentItem(1);
                }
            }
        }catch (Exception e){
            if(LOG)Log.d(TAG,"aa215"+"ERROR==" + e);
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error code: aa215", Toast.LENGTH_SHORT).show();
        }
    }

    public void setPendingNFC(boolean data) {
        pendingNFC = data;
    }

    private void setupTabIcons() {
        tablayout.getTabAt(0).setIcon(tabIcons[0]);
        tablayout.getTabAt(1).setIcon(tabIcons[1]);
        tablayout.getTabAt(2).setIcon(tabIcons[2]);
        tablayout.getTabAt(3).setIcon(tabIcons[3]);

    }

    public class insertTask extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... strings) {

            try {
                Calendar calenderCurrent = Calendar.getInstance();
                Calendar calenderEndon = Calendar.getInstance();
                Calendar calenderStarton = Calendar.getInstance();
                Date EndOn, StartOn, dateTimeStart;
                String Frequency_Auto_Id,Assign_Days,RepeatEveryMonth,Activity_Master_Auto_Id,Asset_Activity_AssignedTo_Auto_Id,Asset_Activity_Linking_Auto_Id, Site_Location_Id, Form_Id, Asset_ID, Assigned_To_User_Group_Id,Assigned_To_User_Id, YearStartson,TimeEndson, TimeStartson, Activity_Name, Asset_Code, Asset_Name, Asset_Location,Status;
                int Activity_Duration, Verified, Grace_Duration_Before, Grace_Duration_After, RepeatEveryDay,RepeatEveryMin;
                String query = "SELECT af.Site_Location_Id,\n" +
                                "af.Frequency_Auto_Id,\n" +
                                "af.YearStartson,\n" +
                                "af.TimeStartson,\n" +
                                "af.TimeEndson,\n" +
                                "af.Activity_Duration,\n" +
                                "af.Grace_Duration_Before,\n" +
                                "af.Grace_Duration_After,\n" +
                                "af.RepeatEveryDay,\n" +
                                "af.RepeatEveryMin,\n" +
                                "af.RepeatEveryMonth,\n" +
                                "af.Verified,\n" +
                                "af.Assign_Days,\n" +
                                "af.Asset_Activity_Linking_Id,\n" +
                                "am.Auto_Id AS Activity_Master_Auto_Id,\n" +
                                "am.Form_Id,\n" +
                                "am.Activity_Name,\n" +
                                "aaa.Auto_Id AS Asset_Activity_AssignedTo_Auto_Id,\n" +
                                "aaa.Assigned_To_User_Id,\n" +
                                "aaa.Assigned_To_User_Group_Id,\n" +
                                "aal.Auto_Id AS Asset_Activity_Linking_Auto_Id,\n" +
                                "aal.Asset_Id,\n" +
                                "ad.Asset_Code,\n" +
                                "ad.Asset_Name,\n" +
                                "ad.Asset_Location,\n" +
                                "ad.Status\n" +
                                "FROM Activity_Frequency af \n" +
                                "LEFT JOIN Asset_Activity_AssignedTo aaa ON \n" +
                                "aaa.Asset_Activity_Linking_Id = af.Asset_Activity_Linking_Id \n" +
                                "LEFT JOIN Asset_Activity_Linking aal ON \n" +
                                "aal.Auto_Id = af.Asset_Activity_Linking_Id \n" +
                                "LEFT JOIN Activity_Master am ON \n" +
                                "am.Auto_Id = aal.Activity_Id \n" +
                                "LEFT JOIN Asset_Details ad ON \n" +
                                "ad.Asset_Id = aal.Asset_Id"+
                        " WHERE aaa.Assigned_To_User_Group_Id IN ("+myDb.UserGroupId(User_Id)+") AND af.Site_Location_Id='"+ SiteId + "'" +
                        "AND af.RecordStatus !='D' AND aaa.RecordStatus !='D' AND aal.RecordStatus !='D' AND am.RecordStatus !='D'";
                Log.d(TAG,"Querfdsafy : "+query);
                SimpleDateFormat YDMDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat YMDHMDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                try {
                    db = myDb.getWritableDatabase();
                    Cursor taskList = db.rawQuery(query, null);
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
                            Asset_Location = taskList.getString(taskList.getColumnIndex("Asset_Location"));
                            Status = taskList.getString(taskList.getColumnIndex("Status"));
                            Verified = taskList.getInt(taskList.getColumnIndex("Verified"));
                            Activity_Master_Auto_Id = taskList.getString(taskList.getColumnIndex("Activity_Master_Auto_Id"));
                            Asset_Activity_AssignedTo_Auto_Id = taskList.getString(taskList.getColumnIndex("Asset_Activity_AssignedTo_Auto_Id"));
                            Asset_Activity_Linking_Auto_Id = taskList.getString(taskList.getColumnIndex("Asset_Activity_Linking_Auto_Id"));

                            if (RepeatEveryDay == 0 && RepeatEveryMin == 0) {        /*For Unplanned Task */
                                String selectQuery = "SELECT  * FROM Task_Details WHERE Asset_Id='" + Asset_ID + "' AND Activity_Frequency_Id='" + Frequency_Auto_Id + "'";
                                Cursor cursor = db.rawQuery(selectQuery, null);
                                if (cursor.getCount() == 0) {
                                    String uuid = UUID.randomUUID().toString();
                                    ContentValues contentValues1 = new ContentValues();
                                    contentValues1.put("Auto_Id", uuid);
                                    contentValues1.put("Company_Customer_Id", companyId);
                                    contentValues1.put("Site_Location_Id", SiteId);
                                    contentValues1.put("Activity_Frequency_Id", Frequency_Auto_Id);
                                    contentValues1.put("Task_Scheduled_Date", "");
                                    contentValues1.put("Task_Status", "");
                                    contentValues1.put("Task_Start_At", "");
                                    contentValues1.put("Assigned_To", "U");
                                    contentValues1.put("Assigned_To_User_Id", User_Id);
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
                                    contentValues1.put("Verified", Verified);
                                    contentValues1.put("Remarks", "");

                                    contentValues1.put("Activity_Master_Auto_Id", Activity_Master_Auto_Id);
                                    contentValues1.put("Asset_Activity_AssignedTo_Auto_Id", Asset_Activity_AssignedTo_Auto_Id);
                                    contentValues1.put("Asset_Activity_Linking_Auto_Id", Asset_Activity_Linking_Auto_Id);

                                    contentValues1.put("RecordStatus", "I");
                                    db = myDb.getWritableDatabase();
                                    db.insert("Task_Details", null, contentValues1);
                                }
                                cursor.close();
                            } else {          /*For Planned Task*/
                                String[] RepeatEveryMonth_OptionList = RepeatEveryMonth.split("\\|");

                                for (int k = 0; k < RepeatEveryMonth_OptionList.length; k++) {
                                    try{

                                   /* }catch (Exception e){
                                        e.printStackTrace();
                                    }*/
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

                                                if (parseDate1(new applicationClass().yymmdd()).after(parseDate1(YearStartson)) || parseDate1(new applicationClass().yymmdd()).equals(parseDate1(YearStartson))) {
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
                                                                if (!Status.equals("WORKING"))
                                                                    contentValues1.put("Task_Status", "Cancelled");
                                                                else
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
                                                                contentValues1.put("Verified",Verified);
                                                                contentValues1.put("Remarks", "");
                                                                contentValues1.put("Activity_Master_Auto_Id", Activity_Master_Auto_Id);
                                                                contentValues1.put("Asset_Activity_AssignedTo_Auto_Id", Asset_Activity_AssignedTo_Auto_Id);
                                                                contentValues1.put("Asset_Activity_Linking_Auto_Id", Asset_Activity_Linking_Auto_Id);
                                                                contentValues1.put("RecordStatus", "I");
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
                                }catch (Exception e){
                                    e.printStackTrace();
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
            int day=0;
            boolean res= false;
            try {
                Calendar previousDay = Calendar.getInstance();
                day = previousDay.get(Calendar.DAY_OF_WEEK);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String[] Assign_Days1 =  Assign_Days.split("\\|");
            for(String Value : Assign_Days1){
                if(Integer.parseInt(Value)==day){
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
            editorTaskInsert = settings.edit();
            editorTaskInsert.putString("day",new applicationClass().yymmdd());
            editorTaskInsert.commit();
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
    public void onBackPressed() {
        if (pending == true && missed == true && cancelled == true && complete == true) {
            Intent intent = new Intent(TaskDetails.this, HomePage.class);
            intent.putExtra("User_Id", User_Id);
            startActivity(intent);
            getFragmentManager().popBackStackImmediate();
            finish();
            super.onBackPressed();
        }else {

        }
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

    @Override
    public void onPending(String count) {
        PendingBadge.setText(count);
        PendingBadge.show();
    }
    @Override
    public void onMissed(String count) {
        MissedBadge.setText(count);
        MissedBadge.show();
    }
    @Override
    public void onComplete(String count) {
        CompletedBadge.setText(count);
        CompletedBadge.show();
    }
    @Override
    public void onCancelled(String count) {
        CancelledBadge.setText(count);
        CancelledBadge.show();
    }
}
