package com.example.google.csmia_temp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.UUID;


public class UnplannedActivity extends AppCompatActivity {
    DatabaseHelper myDb;
    SQLiteDatabase db;
    private ListView lv;
    String assetName,activityName,assetCode,assetId,date,listFrequnecyId,listFromId,TaskIdList,StartDate,EndDate,companyId,SiteId,UserId,Scan_Type;
    String Frequency_Id,Site_Location_Id,Assigned_To_User_Id ,Asset_Id,From_Id,StartDateTime,EndDateTime,Asset_Code,Asset_Name ,Asset_Location,Asset_Status,Activity_Name,User_Group_Id,Group_Name,Task_Status;
    String building_code,floor_code,room_area;
    String barcodeAsset ="55";
    TaskDataAdapter taskDataAdapter;
    NfcAdapter mNfcAdapter;
    NFC nfc;
    LinearLayout unplannedtLinearLayout;
    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unplanned);
        barcodeAsset = getIntent().getStringExtra("AssetCode"); /*Intent Asset Code from Pending Task*/
        myDb=new DatabaseHelper(getApplicationContext());
        getSupportActionBar().setTitle("Unplanned Task");
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        unplannedtLinearLayout= (LinearLayout) findViewById(R.id.unplannedtLinearLayout);
        UserId = settings.getString("userId", null);/*User Id from Preferences*/
        Scan_Type = myDb.ScanType(UserId);
        SiteId = myDb.Site_Location_Id(UserId);
        lv = (ListView)findViewById(R.id.list_Unplanned);
        taskDataAdapter = new TaskDataAdapter(getBaseContext(), R.layout.list_item);
        lv.setAdapter(taskDataAdapter);
        NFCConfiguration();
        UnplannedListview();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int j = 0; j < parent.getChildCount(); j++)
                    parent.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
                assetName = taskDataAdapter.getItem(position).getAsset_Name();
                StartDate = taskDataAdapter.getItem(position).getStartDateTime();
                EndDate = taskDataAdapter.getItem(position).getEndDateTime();
                listFrequnecyId = taskDataAdapter.getItem(position).getFrequency_Id();
                assetCode = taskDataAdapter.getItem(position).getAsset_Code();
                listFromId = taskDataAdapter.getItem(position).getFrom_Id();
                activityName = taskDataAdapter.getItem(position).getActivity_Name();
                assetId = taskDataAdapter.getItem(position).getAsset_Id();
                TaskIdList = taskDataAdapter.getItem(position).getTaskId();

                Intent intent = new Intent(getApplicationContext(), DynamicForm.class);
                intent.putExtra("Form_Id", listFromId);
                intent.putExtra("AssetName", assetName);
                intent.putExtra("TaskId", TaskIdList);
                intent.putExtra("ActivityName", activityName);
                intent.putExtra("AssetId", assetId);
                intent.putExtra("FrequencyId", listFrequnecyId);
                intent.putExtra("StartDate", new applicationClass().yymmddhhmm());
                intent.putExtra("AssetCode", assetCode);
                intent.putExtra("Asset_Location", Asset_Location);
                intent.putExtra("Asset_Status", Asset_Status);
                intent.putExtra("Scan_Type", "Barcode");
                intent.putExtra("unplanned", "unplanned");
                //intent.putExtra("Completed", "Pending");
                intent.putExtra("Completed", "Unplanned");
                intent.putExtra("User_Group_Id",User_Group_Id);
                startActivity(intent);
                finish();
            }
        });
    }
    public void NFCConfiguration(){
        try {
            if(!Scan_Type.equals("QR")) {

                nfc = new NFC();
                nfc.onCreate();
                mNfcAdapter = NfcAdapter.getDefaultAdapter(getApplicationContext());
                if (mNfcAdapter == null) {
                    Toast.makeText(getApplicationContext(), "This device doesn't support NFC!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    if (!mNfcAdapter.isEnabled()) {
                        final Snackbar snackbar = Snackbar
                                .make(unplannedtLinearLayout, "NFC is disabled", Snackbar.LENGTH_LONG)
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
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.d("aa88","ERROR==" + e);
            Toast.makeText(getApplicationContext(), "Error code: aa88", Toast.LENGTH_SHORT).show();
        }
    }
    public void UnplannedListview(){
        int count = 0;
        try {
            db= myDb.getWritableDatabase();
            //Cursor cursor= db.rawQuery(" SELECT a.*,b.* FROM User_Group a,Task_Details b WHERE a.User_Group_Id=b.Assigned_To_User_Group_Id and b.Assigned_To_User_Group_Id IN ("+myDb.UserGroupId(UserId)+") AND b.Site_Location_Id='"+SiteId+"' AND b.Asset_Code='"+barcodeAsset+"' GROUP BY b.From_Id", null);
            /*Cursor cursor= db.rawQuery("SELECT ug.group_name,frq.Site_Location_Id, frq.Frequency_Auto_Id, frq.form_id, frq.Asset_ID, frq.Assigned_To_User_Id, frq.assigned_to_user_group_id, \n" +
                    "       frq.activity_name, ast.Asset_Code, ast.Asset_Name, ast.Asset_Location, ast.Status, ast.asset_status_id, ug.User_Group_Id FROM   task_frequency frq LEFT JOIN asset_details ast ON frq.Asset_ID= ast.Asset_Id  LEFT JOIN asset_status asst ON asst.Asset_Status_Id = ast.Asset_Status_Id LEFT JOIN user_group ug ON frq.Assigned_To_User_Group_Id = ug.User_Group_Id WHERE frq.Assigned_To_User_Group_Id IN ("+myDb.UserGroupId(UserId)+") AND frq.Site_Location_Id='"+ SiteId + "' AND ast.Asset_Code='"+barcodeAsset+"'", null);
*/


            String unplannedquery = "SELECT af.Site_Location_Id," +
                    "af.Frequency_Auto_Id," +
                    "af.Asset_Activity_Linking_Id," +
                    "am.Auto_Id AS Activity_Master_Auto_Id," +
                    "am.Form_Id," +
                    "am.Activity_Name," +
                    "aaa.Auto_Id AS Asset_Activity_AssignedTo_Auto_Id," +
                    "aaa.Assigned_To_User_Id," +
                    "aaa.Assigned_To_User_Group_Id," +
                    "aal.Auto_Id AS Asset_Activity_Linking_Auto_Id," +
                    "aal.Asset_Id," +
                    "ad.Asset_Code," +
                    "ad.Asset_Name," +
                    "ad.Asset_Location," +
                    "ad.Status," +
                    "ug.group_name, al.* " +
                    "FROM Activity_Frequency af " +
                    "LEFT JOIN Asset_Activity_AssignedTo aaa ON " +
                    "aaa.Asset_Activity_Linking_Id = af.Asset_Activity_Linking_Id " +
                    "LEFT JOIN Asset_Activity_Linking aal ON " +
                    "aal.Auto_Id = af.Asset_Activity_Linking_Id " +
                    "LEFT JOIN Activity_Master am ON " +
                    "am.Auto_Id = aal.Activity_Id " +
                    "LEFT JOIN Asset_Details ad ON " +
                    "ad.Asset_Id = aal.Asset_Id " +
                    "LEFT JOIN User_Group ug ON " +
                    "aaa.Assigned_To_User_Group_Id = ug.User_Group_Id " +
                    "LEFT JOIN Asset_Location al ON " +
                    "al.Asset_Id=ad.Asset_Id " +
                    "WHERE aaa.Assigned_To_User_Group_Id IN ("+myDb.UserGroupId(UserId)+") AND af.Site_Location_Id = '"+SiteId+"' AND ad.Asset_Code ='"+barcodeAsset+"' AND " +
                    "af.RecordStatus !='D' AND aaa.RecordStatus !='D' AND aal.RecordStatus !='D' AND am.RecordStatus !='D' Group BY af.Frequency_Auto_Id";

            Cursor cursor= db.rawQuery(unplannedquery,null);

            Log.d("Unplanned",""+unplannedquery+" "+cursor.getCount());

            if (cursor.moveToFirst()) {
                do {
                    String uuid = UUID.randomUUID().toString();
                    Frequency_Id = cursor.getString(cursor.getColumnIndex("Frequency_Auto_Id"));
                    Site_Location_Id = cursor.getString(cursor.getColumnIndex("Site_Location_Id")) ;
                    Assigned_To_User_Id = cursor.getString(cursor.getColumnIndex("Assigned_To_User_Id")) ;
                    Asset_Id = cursor.getString(cursor.getColumnIndex("Asset_Id")) ;
                    From_Id = cursor.getString(cursor.getColumnIndex("Form_Id"));
                    Asset_Code  = cursor.getString(cursor.getColumnIndex("Asset_Code"));
                    Asset_Name = cursor.getString(cursor.getColumnIndex("Asset_Name")) ;
                    building_code = cursor.getString(cursor.getColumnIndex("building_code"));
                    floor_code = cursor.getString(cursor.getColumnIndex("floor_code"));
                    room_area = cursor.getString(cursor.getColumnIndex("room_area"));
                    Asset_Location = building_code+"-"+floor_code+"-"+room_area;
                    Asset_Status  = cursor.getString(cursor.getColumnIndex("Status"));
                    Activity_Name = cursor.getString(cursor.getColumnIndex("Activity_Name"));
                    Group_Name = cursor.getString(cursor.getColumnIndex("Group_Name"));
                    User_Group_Id = cursor.getString(cursor.getColumnIndex("Assigned_To_User_Group_Id"));
                    TaskProvider dataProvider = new TaskProvider(uuid,Frequency_Id,Site_Location_Id,Assigned_To_User_Id ,Asset_Id,From_Id,new applicationClass().yymmddhhmm(),"Unplanned",Asset_Code ,Asset_Name ,Asset_Location,Asset_Status,Activity_Name,Task_Status,Group_Name,null,null);
                    taskDataAdapter.add(dataProvider);
                    count++;
                }
                while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }
        catch (Exception e) {
            Log.d("ua113 ","ERROR==" + e);
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error code: ua113", Toast.LENGTH_SHORT).show();
        }
    }
    public void DataNfc(String RES){
        try {
            if(!Scan_Type.equals("QR")) {
                if(RES == ""){
                }
                else {
                    Intent intent = new Intent(getApplicationContext(),UnplannedActivity.class);
                    intent.putExtra("AssetCode",RES);
                    startActivity(intent);
                    finish();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.d("aa225","ERROR==" + e);
            Toast.makeText(getApplicationContext(), "Error code: aa225", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        try {
            if(!Scan_Type.equals("QR")) {
                nfc.setupForegroundDispatch(this, mNfcAdapter);
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.d("aa240","ERROR==" + e);
            Toast.makeText(getApplicationContext(), "Error code: aa240", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        try {
            if(!Scan_Type.equals("QR")) {
                nfc.stopForegroundDispatch(this, mNfcAdapter);
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.d("aa257","ERROR==" + e);
            Toast.makeText(getApplicationContext(), "Error code: aa257", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask().execute(tag);
            }else {
                System.out.println("Wrong mime type: "+type);
            }
        }else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)){
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReaderTask().execute(tag);
                    break;
                }
            }
        }
    }

    public class NdefReaderTask extends AsyncTask<Tag, Void, String> {
        @Override
        protected String doInBackground(Tag... params) {
            try {
                Tag tag = params[0];
                Ndef ndef = Ndef.get(tag);
                if (ndef == null) {
                    return null;
                }
                NdefMessage ndefMessage = ndef.getCachedNdefMessage();
                NdefRecord[] records = ndefMessage.getRecords();
                for (NdefRecord ndefRecord : records) {
                    if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                        try {
                            return readText(ndefRecord);
                        } catch (Exception e) {
                            Log.e(TAG, "Unsupported Encoding", e);
                        }
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
                Log.d("aa147","ERROR==" + e);
                Toast.makeText(getApplicationContext(), "Error code: aa147", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        private String readText(NdefRecord record) {
            try {
                byte[] payload = record.getPayload();
                String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
                int languageCodeLength = payload[0] & 0063;
                try {
                    return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }catch (Exception e){
                e.printStackTrace();
                Log.d("aa169","ERROR==" + e);
                Toast.makeText(getApplicationContext(), "Error code: aa169", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String RES) {
            try {
                if (RES != null) {
                    DataNfc(RES);
                }
            }catch (Exception e){
                e.printStackTrace();
                Log.d("aa186","ERROR==" + e);
                Toast.makeText(getApplicationContext(), "Error code: aa186", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(UnplannedActivity.this, TaskDetails.class);
        intent.putExtra("TAB","TAB2");
        startActivity(intent);
        finish();
    }
}
