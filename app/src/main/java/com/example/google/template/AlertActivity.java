package com.example.google.template;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AlertActivity extends AppCompatActivity {
    DatabaseHelper myDb;
    SQLiteDatabase db;
    ListView lvAlert;
    AlertDataAdapter alertDataAdapter;
    int count=0;
    String SiteId,User_Id,CompanyId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getSupportActionBar().setTitle("Warnings");
        myDb=new DatabaseHelper(getApplicationContext());
        lvAlert = (ListView)findViewById(R.id.list_Alert);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        User_Id = settings.getString("userId", null);
        CompanyId = settings.getString("Company_Customer_Id", null);
        SiteId = myDb.Site_Location_Id(User_Id);
        alertDataAdapter=new AlertDataAdapter(getApplicationContext(), R.layout.alert_list_item);
        lvAlert.setAdapter(alertDataAdapter);

        try{
            db= myDb.getReadableDatabase();
            String alertQuery= "Select * FROM AlertMaster WHERE AlertMaster.Assigned_To_User_Group_Id IN("+myDb.UserGroupId(User_Id)+") AND AlertMaster.Site_Location_Id='"+SiteId+"' Group by AlertMaster.Task_Id";
            Cursor cursor= db.rawQuery(alertQuery, null);
            if(cursor.getCount()==0){
                Toast.makeText(getApplicationContext(), "No Alerts", Toast.LENGTH_SHORT).show();

            }else {
                if (cursor.moveToFirst()) {
                    do {
                        String Asset_Name = cursor.getString(cursor.getColumnIndex("Asset_Name"));
                        String Activity_Name = cursor.getString(cursor.getColumnIndex("Activity_Name"));
                        String Task_Id = cursor.getString(cursor.getColumnIndex("Task_Id"));
                        String AlertType = cursor.getString(cursor.getColumnIndex("Alert_Type"));
                        String Task_Start_At = cursor.getString(cursor.getColumnIndex("Task_Start_At"));
                        String Task_Status = cursor.getString(cursor.getColumnIndex("Task_Status"));
                        String Activity_Frequency_Id = cursor.getString(cursor.getColumnIndex("Activity_Frequency_Id"));
                        String Schedule_Date = cursor.getString(cursor.getColumnIndex("Task_Scheduled_Date"));
                        String UpdatedStatus = cursor.getString(cursor.getColumnIndex("UpdatedStatus"));
                        String ViewFlag = cursor.getString(cursor.getColumnIndex("ViewFlag"));
                        if(Task_Status.equals("Unplanned")) {
                            AlertDataProvider alertdataProvider = new AlertDataProvider(Asset_Name,formatDate(parseDate(Task_Start_At)),Task_Id,Activity_Name,Activity_Frequency_Id," [ Unplanned ]",AlertType,ViewFlag,UpdatedStatus);
                            alertDataAdapter.add(alertdataProvider);
                            count++;
                        }else {
                            AlertDataProvider alertdataProvider = new AlertDataProvider(Asset_Name,Schedule_Date,Task_Id,Activity_Name,Activity_Frequency_Id,formatDate(parseDate(Task_Start_At)),AlertType,ViewFlag,UpdatedStatus);
                            alertDataAdapter.add(alertdataProvider);
                            count++;
                        }
                    }
                    while (cursor.moveToNext());
                }
                cursor.close();
                db.close();
            }
        }catch (Exception e)
        {
            Log.d("aa90 ERROR==","" + e);
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error code: aa90", Toast.LENGTH_SHORT).show();
        }


        try {
            lvAlert.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String Task_Id = alertDataAdapter.getItem(position).getTask_Id();
                    String Asset_Name = alertDataAdapter.getItem(position).getAsset_Name();
                    String FrequencyId = alertDataAdapter.getItem(position).getActivity_Frequency_Id();
                    showAlert(Task_Id, Asset_Name, FrequencyId);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("aa108 ERROR==","" + e);
            Toast.makeText(getApplicationContext(), "Error code: aa108", Toast.LENGTH_SHORT).show();
        }
    }
    public void showAlert(final String Task_Id,String Asset_Name,String FrequencyId) {

        String Status="";
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.customalerts, null);
        LinearLayout linearLayoutAlert = (LinearLayout) alertLayout.findViewById(R.id.linearLayoutAlerts);

        try{
            db= myDb.getReadableDatabase();
            Cursor cursor1 = db.rawQuery("select b.* from  AlertMaster a,Form_Structure b where a.Form_Structure_Id =b.Field_Id and a.Task_Id=='"+Task_Id+"' ", null);
            if (cursor1.getCount() == 0) {
                Toast.makeText(getApplicationContext(), "Please Enter Valid", Toast.LENGTH_LONG).show();
            } else {
                if (cursor1.moveToFirst()) {
                    do {
                        String assetsAutoId = cursor1.getString(cursor1.getColumnIndex("Field_Label"));
                        String Field_Id = cursor1.getString(1);

                        String Query ="select a.Threshold_From ,a. Threshold_To,b.Value from Parameter a,Data_Posting b  where b.Form_Structure_Id ='"+Field_Id+"' and a.Form_Structure_Id ='"+Field_Id+"' and b.Task_Id='"+Task_Id+"' and a.Activity_Frequency_Id='"+FrequencyId+"' ";
                        Cursor cursor12 = db.rawQuery(Query, null);
                        if (cursor12.moveToFirst()) {
                            do {
                                String Threshold_To = cursor12.getString(cursor12.getColumnIndex("Threshold_To"));
                                String Threshold_From = cursor12.getString(cursor12.getColumnIndex("Threshold_From"));
                                String Value = cursor12.getString(cursor12.getColumnIndex("Value"));
                                try {
                                    if(Double.parseDouble(Value)<Double.parseDouble(Threshold_From)){
                                        Status = "Low";
                                    }
                                    if(Double.parseDouble(Value)>Double.parseDouble(Threshold_To)){
                                        Status = "High";
                                    }
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }

                                LinearLayout.LayoutParams fittype1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
                                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.40f);
                                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.15f);
                                params2.setMargins(30,0,0,0);
                                LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.30f);
                                LinearLayout.LayoutParams params4 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.15f);

                                LinearLayout formLayout = new LinearLayout(this);
                                formLayout.setOrientation(LinearLayout.HORIZONTAL);
                                formLayout.setWeightSum(1f);
                                fittype1.setMargins(20,12,0,0);
                                formLayout.setLayoutParams(fittype1);

                                TextView textView = new TextView(this);
                                textView.setTextSize(15);
                                textView.setText(assetsAutoId);
                                textView.setLayoutParams(params1);
                                textView.setPadding(0,10,0,10);
                                formLayout.addView(textView);

                                TextView textView1 = new TextView(this);
                                textView1.setTextSize(15);
                                textView1.setText(Value);
                                textView1.setPadding(0, 10, 0, 10);
                                textView1.setLayoutParams(params2);
                                formLayout.addView(textView1);

                                TextView textView2 = new TextView(this);
                                textView2.setTextSize(15);
                                textView2.setLayoutParams(params3);
                                textView2.setPadding(0, 10, 0, 10);
                                textView2.setText(Threshold_From + "-" + Threshold_To);
                                formLayout.addView(textView2);

                                TextView textView3 = new TextView(this);
                                textView3.setTextSize(15);
                                textView3.setLayoutParams(params4);
                                textView3.setPadding(0, 10, 0, 10);
                                textView3.setText(Status);
                                formLayout.addView(textView3);

                                linearLayoutAlert.addView(formLayout);
                            }

                            while (cursor12.moveToNext());
                        }
                        cursor12.close();

                    }

                    while (cursor1.moveToNext());
                }
            }
            cursor1.close();
        }catch (Exception e)
        {
            e.printStackTrace();
            Log.d("aa200","ERROR==" + e);
            Toast.makeText(getApplicationContext(), "Error code: aa200", Toast.LENGTH_SHORT).show();
        }

        try {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(Asset_Name);
            alert.setView(alertLayout);
            final AlertDialog dialog = alert.create();
            dialog.show();
            try {
                db=myDb.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put("ViewFlag", "yes");
                db.update("AlertMaster", contentValues, "Task_Id ='" + Task_Id + "'", null);
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("aa222","ERROR==" + e);
            Toast.makeText(getApplicationContext(), "Error code: aa222", Toast.LENGTH_SHORT).show();
        }

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
    public void onBackPressed() {
        Intent intent = new Intent(AlertActivity.this, HomePage.class);
        intent.putExtra("User_Id", User_Id);
        startActivity(intent);
        getFragmentManager().popBackStackImmediate();
        finish();
        super.onBackPressed();
    }

}
