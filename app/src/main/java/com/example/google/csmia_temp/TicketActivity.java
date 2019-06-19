package com.example.google.csmia_temp;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class TicketActivity extends AppCompatActivity {
    EditText editTextDesc;
    Spinner spinnerSubject,spinnerPriority;
    Button btnGenerateTicket;
    DatabaseHelper myDb;
    SQLiteDatabase db;
    //String siteName;
    String site_id,SAuto_Id,CompanyCustomerId,siteName;
    String[] loggerData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        myDb=new DatabaseHelper(getApplicationContext());
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        getSupportActionBar().setTitle("Ticket" +BuildConfig.VERSION_NAME);
        editTextDesc = (EditText) findViewById(R.id.editTextDesc);
        spinnerSubject = (Spinner) findViewById(R.id.spinnerSubject);
        spinnerPriority = (Spinner) findViewById(R.id.spinnerPriority);
        btnGenerateTicket = (Button) findViewById(R.id.btnGenerateTicket);
        btnGenerateTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertTicket();
            }
        });

        CompanyCustomerId = settings.getString("Company_Customer_Id", null);
        SAuto_Id = settings.getString("userId", null);
        siteName = myDb.SiteName(SAuto_Id);
        site_id = myDb.Site_Location_Id(SAuto_Id);

    }
    public void insertTicket() {
        final Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.customdialog);
        dialog.setTitle("Custom Alert Dialog");
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date = simpleDateFormat.format(calendar.getTime());
        String createdSource = "M";
        String updatedStatus = "no";


        String spinnerSubjectStr = spinnerSubject.getSelectedItem().toString();
        String spinnerPriorityStr = spinnerPriority.getSelectedItem().toString();
        try {
            if (spinnerSubjectStr.equals("Select Subject")) {
                Toast.makeText(getApplicationContext(), "Please Select Subject First.", Toast.LENGTH_SHORT).show();
            }
            else if(spinnerPriorityStr.equals("Select Priority") )
            {
                Toast.makeText(getApplicationContext(),"Please Select Priority.",Toast.LENGTH_SHORT).show();
            }
            else {
                String ticketDesc = editTextDesc.getText().toString();
                boolean isInsertedTkt = myDb.insertTicket(site_id, CompanyCustomerId, createdSource, date, spinnerSubjectStr, ticketDesc, spinnerPriorityStr,"manual", updatedStatus,SAuto_Id);
                if (isInsertedTkt == true) {
                    Intent intent = new Intent(TicketActivity.this, HomePage.class);
                    intent.putExtra("User_Id", SAuto_Id);
                    startActivity(intent);
                    Toast.makeText(TicketActivity.this, "Ticket Created Successfully.", Toast.LENGTH_LONG).show();
                    finish();

                } else
                    Toast.makeText(TicketActivity.this, "Data Not Inserted", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.d("ta103 ","ERROR==" + e);
            Toast.makeText(getApplicationContext(), "Error code: ta103", Toast.LENGTH_SHORT).show();

        }
    }

}
