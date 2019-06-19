package com.example.google.csmia_temp;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class viewTicket extends AppCompatActivity {

    String Auto_Id,User_Id,SiteId,Scan_Type;
    EditText editText;

    Spinner spStatus;//,spType;
    DatabaseHelper myDb;
    SQLiteDatabase db;
    String typeString,statusString,Task_Id,assetName;
    TextView site,location,sublocation, servicearea,product,component,deparment;
    List<String> listStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ticket);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        myDb = new DatabaseHelper(getApplicationContext());

        Intent intent = getIntent();
        Auto_Id= intent.getStringExtra("TicketId");
        Task_Id = intent.getStringExtra("TaskId");
        Log.d("viewTicketId",Task_Id);

        User_Id = settings.getString("userId", null);
        SiteId = myDb.Site_Location_Id(User_Id);
        Scan_Type = myDb.ScanType(User_Id);
        assetName = myDb.Ticket_Asset_Name(Task_Id);
        //spType = (Spinner) findViewById(R.id.spViewType);

        /////
        spStatus = (Spinner) findViewById(R.id.spViewStatus);
        site = (TextView) findViewById(R.id.spSite);
        location = (TextView) findViewById(R.id.spLocation);
        sublocation = (TextView) findViewById(R.id.spSubLocation);
        servicearea = (TextView) findViewById(R.id.spServiceArea);
        product = (TextView) findViewById(R.id.spProduct);
        component = (TextView) findViewById(R.id.spComponent);
        deparment = (TextView) findViewById(R.id.spDepartment);


//////////


        List<String> listtype = Arrays.asList(getResources().getStringArray(R.array.type));
        if(myDb.getTicketUser(SiteId,User_Id)==0){
            listStatus = Arrays.asList(getResources().getStringArray(R.array.statusAssigned));
        }else {
            listStatus = Arrays.asList(getResources().getStringArray(R.array.status));
        }


        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listtype);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spType.setAdapter(spinnerAdapter);

        ArrayAdapter<String> AdapterlistStatus = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listStatus);
        AdapterlistStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spStatus.setAdapter(AdapterlistStatus);

        statusString = myDb.getTickStatus(Auto_Id);
        typeString = myDb.gettype(Auto_Id);
        for (String optValue : listtype) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, listtype);
            int selectionPosition = adapter.getPosition(typeString);
            Log.d("dropdownSelectedValue:", typeString);
            //if (typeString.equalsIgnoreCase(optValue))
            // spType.setSelection(selectionPosition);
        }

        for (String optValue : listStatus) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, listStatus);
            int selectionPosition = adapter.getPosition(statusString);
            Log.d("dropdownSelectedValue:", statusString);
            if (statusString.equalsIgnoreCase(optValue))
                spStatus.setSelection(selectionPosition);
        }

        //LinearLayout linearLayout= (LinearLayout)findViewById(R.id.linearcurstom);
        Button dialogSubmit = (Button)findViewById(R.id.btnSubmit);
        Button Cancel = (Button)findViewById(R.id.btnSubmitandClose);

        //editText = (EditText)findViewById(R.id.etComments);

       /* try {
            myDb= new DatabaseHelper(getApplicationContext());
            db=myDb.getWritableDatabase();
            Cursor cursor = db.rawQuery("select Comment from Ticket_Comments where Ticket_Id='"+Auto_Id+"' ORDER BY Id DESC",null);
            if(cursor.moveToFirst()){
                do {
                    TextView textView = new TextView(viewTicket.this);
                    textView.setText("* "+cursor.getString(cursor.getColumnIndex("Comment"))+"");
                    textView.setTypeface(null, Typeface.NORMAL);
                    textView.setTextColor(Color.RED);
                    linearLayout.addView(textView);
                }while (cursor.moveToNext());
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }*/


        Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        final String rdate = simpleDateFormat.format(calendar.getTime());

        dialogSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateTicket(Auto_Id, rdate);
                //save(rdate);
            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateTicketClose(Auto_Id, rdate);
                //saveandclose(rdate);
            }
        });

        setValues();
    }

    private void setValues(){

        try {
            myDb= new DatabaseHelper(getApplicationContext());
            db=myDb.getWritableDatabase();
            String query = "SELECT tc.Auto_Id,ft.Product,ft.Component,ft.Department,stl.Site,stl.Location,stl.Sub_Location FROM Form_Ticket ft \n" +
                    "LEFT JOIN Ticket_Created tc ON \n" +
                    "tc.Form_Ticket_Id = ft.Auto_Id \n" +
                    "LEFT JOIN Site_Ticket_Location stl ON\n" +
                    "stl.Auto_Id = ft.Ticket_Location_Id\n" +
                    "WHERE tc.Auto_Id = '"+Auto_Id+"'";
            Cursor cursor = db.rawQuery(query,null);

            if(cursor.moveToFirst()){
                do {
                    site.setText(cursor.getString(cursor.getColumnIndex("Site")));
                    location.setText(cursor.getString(cursor.getColumnIndex("Location")));
                    sublocation.setText(cursor.getString(cursor.getColumnIndex("Sub_Location")));
                    servicearea.setText(assetName);
                    product.setText(cursor.getString(cursor.getColumnIndex("Product")));
                    component.setText(cursor.getString(cursor.getColumnIndex("Component")));
                    deparment.setText(cursor.getString(cursor.getColumnIndex("Department")));

                }while (cursor.moveToNext());
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    public void UpdateTicket(String TicketId,String rdate){


        //spType = (Spinner) findViewById(R.id.spViewType);
        spStatus = (Spinner) findViewById(R.id.spViewStatus);

        // String type = spType.getSelectedItem().toString();
        String Status = spStatus.getSelectedItem().toString();

        ContentValues contentValues = new ContentValues();
        //   contentValues.put("Ticket_Type",type);
        contentValues.put("Ticket_status",Status);
        contentValues.put("Created_DateTime",rdate);
        contentValues.put("UpdatedStatus","no");

        db=myDb.getWritableDatabase();
        db.update("Ticket_Created",contentValues,"Auto_Id='"+TicketId+"'",null);
        db.close();



    }
    public void UpdateTicketClose(String TicketId,String rdate){
        spStatus = (Spinner) findViewById(R.id.spViewStatus);
        String Status = spStatus.getSelectedItem().toString();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Ticket_status",Status);
        contentValues.put("Created_DateTime",rdate);
        contentValues.put("UpdatedStatus","no");

        db=myDb.getWritableDatabase();
        db.update("Ticket_Created",contentValues,"Auto_Id='"+TicketId+"'",null);
        //Intent intent = new Intent(viewTicket.this,TicketList.class);
       // startActivity(intent);
        ActivityCompat.finishAffinity(viewTicket.this);
        db.close();
    }
/*
    public void save(String rdate) {
        try {
            final EditText editText = (EditText)findViewById(R.id.etComments);
            String StrComment = editText.getText().toString();
            String uuid = UUID.randomUUID().toString();
            if(!StrComment.equals("")){
                ContentValues contentValues = new ContentValues();
                contentValues.put("Ticket_Id",Auto_Id);
                contentValues.put("Auto_Id",uuid);
                contentValues.put("Created_DateTime",rdate);
                contentValues.put("Site_Location_Id",SiteId);
                contentValues.put("Comment",StrComment);
                contentValues.put("Created_User_Id",User_Id);
                contentValues.put("User_Group_Id",myDb.UserGroupId(User_Id));
                contentValues.put("Record_Status","I");
                contentValues.put("UpdatedStatus", "no");
                db=myDb.getWritableDatabase();
                long i = db.insert("Ticket_Comments", null, contentValues);

                db.close();
                editText.getText().clear();
                //updateComment(uuid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/
/*    public void updateComment(String uuid){
        try {
            LinearLayout linearLayout= (LinearLayout)findViewById(R.id.linearcurstom);
            myDb= new DatabaseHelper(getApplicationContext());
            db=myDb.getWritableDatabase();
            Cursor cursor = db.rawQuery("select Comment from Ticket_Comments where Ticket_Id='"+Auto_Id+"' AND Auto_Id='"+uuid+"' ORDER BY Id DESC",null);
            if(cursor.moveToFirst()){
                do {
                    TextView textView = new TextView(viewTicket.this);
                    textView.setText("* "+cursor.getString(cursor.getColumnIndex("Comment"))+"");
                    textView.setTypeface(null, Typeface.NORMAL);
                    textView.setTextColor(Color.RED);
                    linearLayout.addView(textView);
                }while (cursor.moveToNext());
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
/*
    public void saveandclose(String rdate ) {
        try {
            String uuid = UUID.randomUUID().toString();
            final EditText editText = (EditText)findViewById(R.id.etComments);
            String Comment = editText.getText().toString();

            ContentValues contentValues = new ContentValues();
            contentValues.put("Ticket_Id",Auto_Id);
            contentValues.put("Auto_Id",uuid);
            contentValues.put("Created_DateTime",rdate);
            contentValues.put("Site_Location_Id",SiteId);
            contentValues.put("Comment",Comment);
            contentValues.put("Created_User_Id",User_Id);
            contentValues.put("User_Group_Id",myDb.UserGroupId(User_Id));
            contentValues.put("Record_Status","I");
            contentValues.put("UpdatedStatus", "no");
            db=myDb.getWritableDatabase();
            if(!Comment.equals("")) {
                long commentInsert = db.insert("Ticket_Comments", null, contentValues);

            }
            Intent intent = new Intent(viewTicket.this,TicketList.class);
            startActivity(intent);
            ActivityCompat.finishAffinity(viewTicket.this);

            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // Intent intent = new Intent(viewTicket.this, TicketList.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //startActivity(intent);
        finish();

    }
}
