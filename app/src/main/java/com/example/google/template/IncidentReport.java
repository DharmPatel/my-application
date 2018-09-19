package com.example.google.template;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class IncidentReport extends AppCompatActivity {
    LinearLayout LinearLayoutIncident;
    DatabaseHelper myDb;
    SQLiteDatabase db;
    RecyclerView recyclerView;
    private List<IncidentTaskProvider> listItems;
    RecyclerView.Adapter adapter;
    SharedPreferences settings;
    String User_Id;
    private static final String TAG = IncidentReport.class.getSimpleName();
    static final boolean LOG = new applicationClass().checkLog();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_report);
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        User_Id = settings.getString("userId", null);
        LinearLayoutIncident = (LinearLayout) findViewById(R.id.linearLayoutIncident);
        myDb=new DatabaseHelper(getApplicationContext());
        recyclerView = (RecyclerView) findViewById(R.id.rvIncident);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listItems =new ArrayList<IncidentTaskProvider>();
        adapter = new MyAdapter(listItems,this);
        recyclerView.setAdapter(adapter);
        showRecyclerViewData();
    }

    public void showRecyclerViewData(){
        recyclerView = (RecyclerView) findViewById(R.id.rvIncident);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listItems =new ArrayList<>();
        adapter = new MyAdapter(listItems,this);
        recyclerView.setAdapter(adapter);
        try {
            db=myDb.getWritableDatabase();
            //String Query="Select a.Auto_Id,a.Activity_Name,a.Scan_Type,a.Asset_Name,a.Activity_Frequency_Id,GROUP_CONCAT(b.Value) As Value,c.* from Task_Details a,Data_Posting b,Form_Structure c where a.From_Id=c.Form_Id and  a.Auto_Id=b.Task_Id and  Incident=1  Group By a.Auto_Id Limit 2";
            String Query="Select a.sections,c.* from Form_Structure a,Data_Posting b,Task_Details c where a.Field_Id=b.Form_Structure_Id and a.Form_Id=c.From_Id and Record_Status <> 'D' and b.Task_Id=c.Auto_Id and  c.Incident=1  ORDER BY Display_Order ASC";
            Cursor cursor = db.rawQuery(Query,null);
            if (cursor.moveToFirst()) {
                do {
                    String Value="";
                    String Activity_Name = cursor.getString(cursor.getColumnIndex("Activity_Name"));
                    String Asset_Name = cursor.getString(cursor.getColumnIndex("Asset_Name"));
                    String Section_Id = cursor.getString(cursor.getColumnIndex("sections"));
                    String Task_Start_At = cursor.getString(cursor.getColumnIndex("Task_Start_At"));
                    String Activity_Frequency_Id = cursor.getString(cursor.getColumnIndex("Activity_Frequency_Id"));
                    String Scan_Type = cursor.getString(cursor.getColumnIndex("Scan_Type"));
                    String Task_Id = cursor.getString(cursor.getColumnIndex("Auto_Id"));
                    String UpdatedStatus = cursor.getString(cursor.getColumnIndex("UpdatedStatus"));

                    String QueryforVal="Select GROUP_CONCAT(b.Value) As Value from Form_Structure a,Data_Posting b where a.Field_Id=b.Form_Structure_Id and a.Form_Id='"+Section_Id+"' and b.Task_Id='"+Task_Id+"' and a.Field_Label IN ('Incident Number','Date & Time')";
                    Cursor cursorVal = db.rawQuery(QueryforVal,null);
                    if (cursorVal.moveToFirst()) {
                        do {
                            Value = cursorVal.getString(cursorVal.getColumnIndex("Value"));
                        }
                        while (cursorVal.moveToNext());
                    }
                    cursorVal.close();
                    String[] ValueArray = Value.split(",");
                    IncidentTaskProvider dataProvider = new IncidentTaskProvider(ValueArray[1],ValueArray[0],Activity_Name,Asset_Name,Activity_Frequency_Id,Scan_Type,Section_Id,0,Task_Id,UpdatedStatus);
                    listItems.add(dataProvider);

                }
                while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                IncidentTaskProvider taskProvider = listItems.get(position);
                Intent intent= new Intent(IncidentReport.this,IncidentForm.class);
                intent.putExtra("Incident",1);
                intent.putExtra("Section_Id", taskProvider.getSection_Id());
                intent.putExtra("FrequencyId",taskProvider.getActivity_Frequency_Id());
                intent.putExtra("Activity_Name",taskProvider.getActivity());
                intent.putExtra("Asset_Name",taskProvider.getAsset());
                intent.putExtra("TaskId",taskProvider.getTask_Id());
                intent.putExtra("Scan_Type",taskProvider.getScan_Id());
                intent.putExtra("User_Id",User_Id);
                startActivity(intent);
                finish();
            }

            @Override
            public void onLongClick(View view, int position) {
                // Toast.makeText(getApplicationContext(),position + " is selected!", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(IncidentReport.this, HomePage.class);
        intent.putExtra("User_Id", User_Id);
        startActivity(intent);
        getFragmentManager().popBackStackImmediate();
        finish();
        super.onBackPressed();
    }
}
