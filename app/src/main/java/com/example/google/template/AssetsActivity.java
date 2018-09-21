package com.example.google.template;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class AssetsActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    DatabaseHelper myDb;
    SQLiteDatabase db;
    ListView lvAssets;
    int mYear;
    int mMonth;
    int mDay;
    int mHour;
    int mMinute;
    int Asset_View;
    String assetsId,assetLocation,assetsCode,assetsName,site_id,Asset_Status_Id,Asset_Id,statusText,updatedtime="",date,companyId,User_Group_Id,User_Id,Scan_Type="",date_time = "";
    RadioGroup radioGroup;
    ListDataAdapter listDataAdapter;
    EditText etSearch;
    Toolbar toolbar;
    private List<RadioGroup> textRadioGroupList = new ArrayList<RadioGroup>();
    private List<RadioButton> textRadioButtonList = new ArrayList<RadioButton>();
    EditText etTime;
    boolean checknfcopen = false;
    NfcAdapter mNfcAdapter;
    NFC nfc;
    SharedPreferences settings;
    LinearLayout assetLinearLayout;
    ImageView imageViewSync, imageViewFilter;
    LinearLayout filter_layout;
    String LocationValue, Categoryvalue;
    ArrayAdapter<String> adapter2;
    ArrayAdapter<String> adapter1;
    List<String> spinnerArray = new ArrayList<String>();
    List<String> spinnerArray1 = new ArrayList<String>();

    private static final String TAG = AssetsActivity.class.getSimpleName();
    static final boolean LOG = new applicationClass().checkLog();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_assets_listview);

        try{
            myDb = new DatabaseHelper(getApplicationContext());
            settings = PreferenceManager.getDefaultSharedPreferences(this);
            User_Id = settings.getString("userId", null);
            Scan_Type = myDb.ScanType(User_Id);
            site_id = myDb.Site_Location_Id(User_Id);
            User_Group_Id = myDb.UserGroupId(User_Id);
            Asset_View = myDb.assetView();

            if(LOG) Log.d(TAG,"PreferenseValue"+companyId+"\n"+site_id+"\n"+User_Id+"\n"+Scan_Type);
            assetLinearLayout= (LinearLayout) findViewById(R.id.assetLinearLayout);
            imageViewSync= (ImageView) findViewById(R.id.imageViewSync);
            imageViewFilter = (ImageView)findViewById(R.id.imageViewFilter);
            lvAssets = (ListView)findViewById(R.id.lvAssets);
            listDataAdapter = new ListDataAdapter(getApplicationContext(), R.layout.asset_list_item);
            lvAssets.setAdapter(listDataAdapter);
            initToolBar();
            AddAssets();

        }catch (Exception e)
        {
            if(LOG) Log.d(TAG,"aa118 ERROR=="+"" + e);
            Toast.makeText(getApplicationContext(), "Error code: aa118", Toast.LENGTH_SHORT).show();
        }



        try {
            if(LOG) Log.d(TAG,"Scan_Type"+Scan_Type);
            if(!Scan_Type.equals("QR")) {
                try {
                    nfc = new NFC();
                    nfc.onCreate();
                    mNfcAdapter = NfcAdapter.getDefaultAdapter(getApplicationContext());
                    if (mNfcAdapter == null) {
                        Toast.makeText(getApplicationContext(), "This device doesn't support NFC!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        if (!mNfcAdapter.isEnabled()) {
                            final Snackbar snackbar = Snackbar
                                    .make(assetLinearLayout, "NFC is disabled", Snackbar.LENGTH_LONG)
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
        } catch (Exception e) {
            if(LOG) Log.d(TAG,"aa158 ERROR=="+"" + e);
            Toast.makeText(getApplicationContext(), "Error code: aa158", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        imageViewFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //drawerLayout.openDrawer(GravityCompat.START);
                FilterDialog();
            }
        });

        lvAssets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String Asset_Code = listDataAdapter.getItem(position).getAsset_Code();
                    Asset_Id = listDataAdapter.getItem(position).getAsset_Id();
                    AssetDialog(Asset_Code);
                } catch (Exception e) {
                    if (LOG) Log.d(TAG, "aa171 ERROR==" + "" + e);
                    Toast.makeText(getApplicationContext(), "Error code: aa171", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    public void FilterDrawer(){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.activity_drawer, null);
        drawerLayout = (DrawerLayout)layout.findViewById(R.id.drawer_layout);

        navigationView = (NavigationView)layout.findViewById(R.id.drawer_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    public void FilterData(){
        try {
            String Query ="";
            db = myDb.getReadableDatabase();
            listDataAdapter = new ListDataAdapter(getApplicationContext(), R.layout.asset_list_item);
            lvAssets.setAdapter(listDataAdapter);
            if (LocationValue != "Select Location" && Categoryvalue == "Select Category") {
                Query = "select asm.* " +
                        "from Asset_Details asm " +
                        "Left Join Asset_Activity_Linking aal "+
                        "on aal.Asset_Id =  asm.Asset_Id "+
                        "Left Join Asset_Activity_AssignedTo aaa "+
                        "on aal.Auto_Id = aaa.Asset_Activity_Linking_Id "+
                        "where aaa.Assigned_To_User_Group_Id IN (" + User_Group_Id + ") and asm.Asset_Location ='"+LocationValue+"'";
            } else if (Categoryvalue != "Select Category" && LocationValue == "Select Location" ) {
                Query = "select asm.* " +
                        "from Asset_Details asm " +
                        "Left Join Asset_Activity_Linking aal "+
                        "on aal.Asset_Id =  asm.Asset_Id "+
                        "Left Join Asset_Activity_AssignedTo aaa "+
                        "on aal.Auto_Id = aaa.Asset_Activity_Linking_Id "+
                        "where aaa.Assigned_To_User_Group_Id IN (" + User_Group_Id + ") and asm.Asset_Type = '"+Categoryvalue+"'";
            }else if (Categoryvalue != "Select Category" && LocationValue != "Select Location"){
                Query = "select asm.* " +
                        "from Asset_Details asm " +
                        "Left Join Asset_Activity_Linking aal "+
                        "on aal.Asset_Id =  asm.Asset_Id "+
                        "Left Join Asset_Activity_AssignedTo aaa "+
                        "on aal.Auto_Id = aaa.Asset_Activity_Linking_Id "+
                        "where aaa.Assigned_To_User_Group_Id IN (" + User_Group_Id + ") and asm.Asset_Type = '"+Categoryvalue+"' and asm.Asset_Location ='"+LocationValue+"'";

            }
            Log.d(TAG, "AlertQuery" + Query);
            Cursor cursor = db.rawQuery(Query, null);
            Log.d("cursorAssetCount", String.valueOf(cursor.getCount()));
            if (cursor.getCount() == 0){
               Toast.makeText(getApplicationContext(),"No value found for selected assets",Toast.LENGTH_SHORT).show();
               AddAssets();
            }

            if (cursor.moveToFirst()) {
                do {
                    String Asset_Id = cursor.getString(cursor.getColumnIndex("Asset_Id"));
                    String Site_Location_Id = cursor.getString(cursor.getColumnIndex("Site_Location_Id"));
                    String Asset_Code = cursor.getString(cursor.getColumnIndex("Asset_Code"));
                    String Asset_Name = cursor.getString(cursor.getColumnIndex("Asset_Name"));
                    String Asset_Location = cursor.getString(cursor.getColumnIndex("Asset_Location"));
                    String Asset_Status_Id = cursor.getString(cursor.getColumnIndex("Asset_Status_Id"));
                    String Status = cursor.getString(cursor.getColumnIndex("Status"));

                    DataProvider assetDataProvider = new DataProvider(Asset_Id, Asset_Code, Asset_Name, Asset_Location, Status, null, null, null);
                    listDataAdapter.add(assetDataProvider);
                }
                while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }catch (Exception e){

        }
    }

    public void AddAssets(){
        try {
            String Query;
            db = myDb.getReadableDatabase();
            listDataAdapter = new ListDataAdapter(getApplicationContext(), R.layout.asset_list_item);
            lvAssets.setAdapter(listDataAdapter);
            if (Asset_View == 0) {
                Query = "select asm.* " +
                        "from Asset_Details asm " +
                        "Left Join Asset_Activity_Linking aal " +
                        "on aal.Asset_Id =  asm.Asset_Id " +
                        "Left Join Asset_Activity_AssignedTo aaa " +
                        "on aal.Auto_Id = aaa.Asset_Activity_Linking_Id " +
                        "where aaa.Assigned_To_User_Group_Id IN (" + User_Group_Id + ")";
            } else {
                Query = "SELECT * from Asset_Details where Site_Location_Id ='" + site_id + "' ";
            }
            Cursor cursor = db.rawQuery(Query, null);
            Log.d(TAG, "AlertQuery" + Query);
            if (cursor.moveToFirst()) {
                do {
                    String Asset_Id = cursor.getString(cursor.getColumnIndex("Asset_Id"));
                    String Site_Location_Id = cursor.getString(cursor.getColumnIndex("Site_Location_Id"));
                    String Asset_Code = cursor.getString(cursor.getColumnIndex("Asset_Code"));
                    String Asset_Name = cursor.getString(cursor.getColumnIndex("Asset_Name"));
                    String Asset_Location = cursor.getString(cursor.getColumnIndex("Asset_Location"));
                    String Asset_Status_Id = cursor.getString(cursor.getColumnIndex("Asset_Status_Id"));
                    String Status = cursor.getString(cursor.getColumnIndex("Status"));

                    DataProvider assetDataProvider = new DataProvider(Asset_Id, Asset_Code, Asset_Name, Asset_Location, Status, null, null, null);
                    listDataAdapter.add(assetDataProvider);
                }
                while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if(!Scan_Type.equals("QR")) {
                nfc.setupForegroundDispatch(this, mNfcAdapter);
            }
        } catch (Exception e) {
            if(LOG) Log.d(TAG,"aa186 ERROR=="+"" + e);
            Toast.makeText(getApplicationContext(), "Error code: aa186", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        try {
            if(!Scan_Type.equals("QR")) {
                mNfcAdapter.disableForegroundDispatch(this);
            }
        } catch (Exception e) {
            if(LOG) Log.d(TAG,"aa199 ERROR=="+"" + e);
            Toast.makeText(getApplicationContext(), "Error code: aa199", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
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
                if(LOG) {Log.d(TAG, "QR " + nfc.tagcontent);
                }
                if(checknfcopen==false){ /* Prevent Double Open Dialog*/
                    db=myDb.getWritableDatabase();
                    Cursor cursorCheckAssetCode = db.rawQuery("Select * from asset_Details where Asset_Code ='"+nfc.tagcontent+"'",null);
                    if(cursorCheckAssetCode.getCount()==0){
                        Toast.makeText(getApplicationContext(),"No asset found for this Assetcode",Toast.LENGTH_LONG).show();
                    }else{
                        AssetDialog(nfc.tagcontent);
                    }
                    cursorCheckAssetCode.close();
                    db.close();
                }
            }
        }catch (Exception e){
            if(LOG){Log.d(TAG,"aa227"+"ERROR==" + e);}
            Toast.makeText(getApplicationContext(), "Error code: aa227", Toast.LENGTH_SHORT).show();
        }
    }
    public void AssetDialog(final String Asset_Code) {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.custom, null);
        Button dialogSubmit = (Button)alertLayout.findViewById(R.id.dialogSubmit);
        final EditText TextViewRemark = (EditText)alertLayout.findViewById(R.id.etAssetRemark);
        TextViewRemark.setVisibility(View.GONE);
        try{
            date = new applicationClass().yymmddhhmmss();
            radioGroup = (RadioGroup)alertLayout.findViewById(R.id.radioStatusGroup);
            db= myDb.getReadableDatabase();
            Cursor cursor1 = db.rawQuery("select * from asset_Details where Asset_Code ='" + Asset_Code + "'", null);
            if (cursor1.getCount() == 0) {
                Toast.makeText(getApplicationContext(), "No asset found for this Assetcode", Toast.LENGTH_LONG).show();
            } else {
                if (cursor1.moveToFirst()) {
                    do {
                        assetsId = cursor1.getString(cursor1.getColumnIndex("Asset_Id"));
                        Asset_Id = assetsId;
                        assetsCode = cursor1.getString(cursor1.getColumnIndex("Asset_Code"));
                        assetsName = cursor1.getString(cursor1.getColumnIndex("Asset_Name"));
                        Asset_Status_Id = cursor1.getString(cursor1.getColumnIndex("Asset_Status_Id"));
                        updatedtime = cursor1.getString(cursor1.getColumnIndex("Manual_Time"));

                    }
                    while (cursor1.moveToNext());
                }
            }
            cursor1.close();
        }catch (Exception e) {
            if(LOG){Log.d(TAG,"aa258"+"ERROR==" + e);}
            Toast.makeText(getApplicationContext(), "Error code: aa258", Toast.LENGTH_SHORT).show();
        }

        try {
            etTime = (EditText)alertLayout.findViewById(R.id.etAssetChangeTime);
            ImageView imageView = (ImageView)alertLayout.findViewById(R.id.imageViewTime);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    datePicker();
                }
            });

            db = myDb.getReadableDatabase();
            String query = "SELECT Status FROM Asset_Status WHERE Asset_Status_Id = '"+Asset_Status_Id+"'";
            Cursor res =db.rawQuery(query, null);
            if (res.moveToFirst()) {
                do {
                    statusText = res.getString(0);
                    Log.d("Asset",assetsName+" "+Asset_Status_Id+" "+updatedtime+" "+statusText);

                } while (res.moveToNext());
            }
            if (updatedtime.equals("")) {

                etTime.setText(date);
            }
            else
            {
                etTime.setText(updatedtime);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if(LOG){Log.d(TAG,"aa292"+"ERROR==" + e);}
            Toast.makeText(getApplicationContext(), "Error code: aa292", Toast.LENGTH_SHORT).show();
        }

        try {
            db=myDb.getReadableDatabase();
            Cursor cursor2 = db.rawQuery("select * from Asset_Status", null);
            if (cursor2.moveToFirst()) {
                do {
                    String Asset_Status_Id = cursor2.getString(cursor2.getColumnIndex("Asset_Status_Id"));
                    String status = cursor2.getString(cursor2.getColumnIndex("Status"));

                    radioGroup.addView(radioButton(Asset_Code, status, cursor2.getInt(0)));
                }
                while (cursor2.moveToNext());
                textRadioGroupList.add(radioGroup);
            }
        } catch (Exception e) {
            if(LOG) Log.d(TAG,"aa310"+"ERROR==" + e);
            Toast.makeText(getApplicationContext(), "Error code: aa310", Toast.LENGTH_SHORT).show();
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View rb1 = group.findViewById(checkedId);
                int idx = group.indexOfChild(rb1);
                RadioButton radioButton = (RadioButton) group.getChildAt(idx);
                String selectRB = radioButton.getText().toString();
                if(selectRB.equalsIgnoreCase(statusText)){
                    TextViewRemark.setVisibility(View.GONE);
                }
               else {
                    TextViewRemark.setVisibility(View.VISIBLE);
                }
            }
        });
        try {
            CheckBox cbReset =(CheckBox)alertLayout.findViewById(R.id.cbReset);
            if(checkResetForm1(Asset_Code) ==false){
                cbReset.setVisibility(View.GONE);
            }

            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(assetsName);

            alert.setView(alertLayout);
            final AlertDialog dialog = alert.create();
            dialog.show();
            checknfcopen=true;

            dialogSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    save(Asset_Code,TextViewRemark.getText().toString());
                    dialog.dismiss();
                }
            });
            cbReset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(AssetsActivity.this);
                    alertDialog.setTitle("Confirm Reset...");

                    alertDialog.setMessage("Are you sure you want to Reset ?");
                    alertDialog.setPositiveButton("YES",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog1, int which) {
                                    dialog1.dismiss();
                                    checkResetForm(Asset_Code);
                                }
                            });
                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("NO",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //dialog.cancel();
                                    checknfcopen=false;
                                    dialog.cancel();


                                }
                            });
                    alertDialog.show();
                }
            });
            Button btncancel = (Button)alertLayout.findViewById(R.id.btnCancel);
            btncancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checknfcopen=false;
                    dialog.dismiss();

                }
            });
        } catch (Exception e) {
            if(LOG) Log.d(TAG,"aa375"+"ERROR==" + e);
            Toast.makeText(getApplicationContext(), "Error code: aa375", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void FilterDialog(){
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.filter_alert, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(alertLayout);
        final AlertDialog dialog = alert.create();
        dialog.show();
        Button Ok = (Button) alertLayout.findViewById(R.id.OkId);

        //////////Location spinner/////////////////////
        final Spinner LocationSP = (Spinner) alertLayout.findViewById(R.id.spinnerLocation);
        Spinner CategorySP = (Spinner) alertLayout.findViewById(R.id.spinnerCategory);
        HashMap<String, String> spinnerArrayHash = new HashMap<>();

        adapter1 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item_value, spinnerArray);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

          adapter2 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item_value, spinnerArray1);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        try {
            db = myDb.getReadableDatabase();

            Cursor LocationCursor = db.rawQuery("SELECT DISTINCT(Asset_Location) from Asset_Details asm Left Join Asset_Activity_Linking aal " +
                    "on aal.Asset_Id =  asm.Asset_Id " +
                    "Left Join Asset_Activity_AssignedTo aaa " +
                    " on aal.Auto_Id = aaa.Asset_Activity_Linking_Id " +
                    "where aaa.Assigned_To_User_Group_Id IN ("+User_Group_Id+")", null);
            Log.d("ValuesOfCursor", String.valueOf(LocationCursor.getCount()));
            if (LocationCursor.getCount() == 1) {
                if (LocationCursor.moveToFirst()) {
                    do {
                        String Asset_Location = LocationCursor.getString(LocationCursor.getColumnIndex("Asset_Location"));
                        spinnerArray.add(Asset_Location);
                    }
                    while (LocationCursor.moveToNext());
                }
            } else {
                spinnerArray.add("Select Location");
                if (LocationCursor.moveToFirst()) {
                    do {
                        String Asset_Location = LocationCursor.getString(LocationCursor.getColumnIndex("Asset_Location"));
                        spinnerArray.add(Asset_Location);

                    }
                    while (LocationCursor.moveToNext());
                }
            }
            LocationCursor.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        LocationSP.setAdapter(adapter1);
        LocationSP.setSelection(adapter1.getPosition(LocationValue));


        LocationSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                LocationValue = item.toString();
                Log.d("spinnerVal","1: "+LocationValue+":"+item);
               /* for(String Value: myDb.assetType(User_Group_Id,LocationValue)){
                    Log.d("spinnerValqwed","1: "+Value);
                    adapter2.getFilter().filter(Value);
                }*/


                if (myDb.assetType(User_Group_Id,LocationValue) != null){
                    if(myDb.assetType(User_Group_Id,LocationValue).size()>=1){
                        //adapter2.clear();
                        spinnerArray1.clear();
                        adapter2.insert("Select Category", adapter2.getCount());
                    }
                    for (String object : myDb.assetType(User_Group_Id,LocationValue)) {
                        adapter2.insert(object, adapter2.getCount());

                    }
                }

                adapter2.notifyDataSetChanged();

                //adapter2.getFilter().filter(myDb.assetType(User_Group_Id,LocationValue));
                /*adapter2 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item_value, myDb.assetType(User_Group_Id,LocationValue));
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                adapter2.notifyDataSetInvalidated();*/
               // adapter2.add(myDb.assetType(User_Group_Id,LocationValue));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //////////Category spinner/////////////////////

        HashMap<String, String> spinnerArrayHash1 = new HashMap<>();


        try {
            db = myDb.getReadableDatabase();
            Cursor AssetTypeCursor = db.rawQuery("SELECT DISTINCT(Asset_Type) from Asset_Details asm Left Join Asset_Activity_Linking aal " +
                    "on aal.Asset_Id =  asm.Asset_Id " +
                    "Left Join Asset_Activity_AssignedTo aaa " +
                    " on aal.Auto_Id = aaa.Asset_Activity_Linking_Id " +
                    "where aaa.Assigned_To_User_Group_Id IN ("+User_Group_Id+")", null);

            if (AssetTypeCursor.getCount() == 1) {
                if (AssetTypeCursor.moveToFirst()) {
                    do {
                        String Asset_Type = AssetTypeCursor.getString(AssetTypeCursor.getColumnIndex("Asset_Type"));
                        spinnerArray1.add(Asset_Type);
                    }
                    while (AssetTypeCursor.moveToNext());
                }
            } else {
                spinnerArray1.add("Select Category");
                if (AssetTypeCursor.moveToFirst()) {
                    do {
                        String Asset_Type = AssetTypeCursor.getString(AssetTypeCursor.getColumnIndex("Asset_Type"));
                        spinnerArray1.add(Asset_Type);

                    }
                    while (AssetTypeCursor.moveToNext());
                }

            }
            AssetTypeCursor.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        CategorySP.setAdapter(adapter2);
        CategorySP.setSelection(adapter2.getPosition(Categoryvalue));
        try {
            //site_name = settings1.getString("siteName", null);

            /*Iterator it = spinnerArrayHash1.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> pair = (Map.Entry) it.next();
                System.out.println(pair.getKey() + " = " + pair.getValue());
                Log.d(TAG, "assetTypeHashMap" + myDb.assetType(User_Group_Id,LocationValue));
                if (myDb.assetType(User_Group_Id,LocationValue).equalsIgnoreCase(pair.getValue())) {
                    int i = adapter2.getPosition(pair.getValue());
                    CategorySP.setSelection(i);
                }
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }

        CategorySP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Categoryvalue = item.toString();
                Log.d("spinnerVal","2: "+Categoryvalue+":"+item);
                /*for(String Value: myDb.assetLocation(User_Group_Id,Categoryvalue)){
                    Log.d("spinnerValqwed","2: "+Value+":"+item);
                    adapter1.getFilter().filter(Value);
                }*/


                if (myDb.assetLocation(User_Group_Id,Categoryvalue) != null){

                    if(myDb.assetLocation(User_Group_Id,Categoryvalue).size()>=1){
                        //adapter1.clear();
                        spinnerArray.clear();
                        adapter1.insert("Select Location", adapter1.getCount());
                        //adapter1.notifyDataSetInvalidated();
                    }

                    for (String object : myDb.assetLocation(User_Group_Id,Categoryvalue)) {
                        adapter1.insert(object, adapter1.getCount());
                    }
                }

                adapter1.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ///////////Ok Button///////////////////

        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterData();
                dialog.dismiss();

            }
        });

    }

    private RadioButton radioButton(final String assetId,String strvalue,int statusID) {
        RadioButton radioButton = new RadioButton(this);
        radioButton.setText(strvalue);
        try {
            if(strvalue.equals("WORKING"))
                radioButton.setTextColor(Color.parseColor("#196F3D"));
            else if(strvalue.equals("STAND BY"))
                radioButton.setTextColor(Color.parseColor("#D68910"));
            else if(strvalue.equals("MAINTENANCE"))
                radioButton.setTextColor(Color.parseColor("#283747"));
            else if(strvalue.equals("NOT WORKING"))
                radioButton.setTextColor(Color.parseColor("#922B21"));
            radioButton.setId(statusID);

            if(statusText.equals(strvalue))
            {
                Log.d("assetStatus",statusText+":"+strvalue);
                radioButton.setChecked(true);
                radioButton.setEnabled(false);
            }
        } catch (Exception e) {
            if(LOG) Log.d(TAG,"aa399"+"ERROR==" + e);
            Toast.makeText(getApplicationContext(), "Error code: aa399", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        textRadioButtonList.add(radioButton);
        return radioButton;
    }
    public void checkResetForm(String AssetCode){
        String uuid = UUID.randomUUID().toString();
        try {
            db= myDb.getWritableDatabase();
            Cursor cursor= db.rawQuery(" SELECT a.*, b.Field_Type FROM Task_Details a,Form_Structure b WHERE a.Assigned_To_User_Group_Id IN ("+myDb.UserGroupId(User_Id)+") AND a.Site_Location_Id='"+site_id+"' AND a.Asset_Code='"+AssetCode+"' AND a.From_Id=b.Form_Id  AND b.Field_Type='meter'  GROUP BY From_Id", null);

            if (cursor.moveToFirst()) {
                do {
                    String Frequency_Id = cursor.getString(cursor.getColumnIndex("Activity_Frequency_Id"));
                    String Asset_Id = cursor.getString(cursor.getColumnIndex("Asset_Id")) ;
                    String From_Id = cursor.getString(cursor.getColumnIndex("From_Id")) ;
                    String StartDateTime = cursor.getString(cursor.getColumnIndex("Task_Scheduled_Date"))  ;
                    String Asset_Code  = cursor.getString(cursor.getColumnIndex("Asset_Code"));
                    String Asset_Name = cursor.getString(cursor.getColumnIndex("Asset_Name")) ;
                    String Asset_Location = cursor.getString(cursor.getColumnIndex("Asset_Location")) ;
                    String Asset_Status  = cursor.getString(cursor.getColumnIndex("Asset_Status"));
                    String Activity_Name = cursor.getString(cursor.getColumnIndex("Activity_Name"));
                    String Task_Status = cursor.getString(cursor.getColumnIndex("Task_Status"));
                    String Assigned_To_User_Group_Id = cursor.getString(cursor.getColumnIndex("Assigned_To_User_Group_Id"));

                    Intent intent = new Intent(getApplicationContext(), DynamicForm.class);
                    intent.putExtra("ClosingTaskId",uuid);
                    intent.putExtra("Form_Id", From_Id);
                    intent.putExtra("AssetName", Asset_Name);
                    intent.putExtra("AssetId", Asset_Id);
                    intent.putExtra("FrequencyId", Frequency_Id);
                    intent.putExtra("StartDate", StartDateTime);
                    intent.putExtra("AssetCode", Asset_Code);
                    intent.putExtra("Asset_Name", Asset_Name);
                    intent.putExtra("Asset_Location", Asset_Location);
                    intent.putExtra("Asset_Status", Asset_Status);
                    intent.putExtra("Asset_Status", Asset_Status);
                    intent.putExtra("ActivityName", Activity_Name);
                    intent.putExtra("unplanned", "unplanned");
                    intent.putExtra("User_Group_Id", Assigned_To_User_Group_Id);
                    intent.putExtra("FromReset","Closing");

                    startActivity(intent);
                }
                while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }
        catch (Exception e)
        {
            if(LOG){Log.d(TAG,"ua447 "+"ERROR==" + e);}
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error code: ua447", Toast.LENGTH_SHORT).show();
        }

    }
    public boolean checkResetForm1(String AssetCode){
        boolean resetValue = false;
        try {
            int count= 0;
            db= myDb.getWritableDatabase();
            String query= "SELECT a.*, b.Field_Type FROM Task_Details a,Form_Structure b WHERE a.Assigned_To_User_Id = '"+User_Id+"' AND a.Site_Location_Id='"+site_id+"' AND a.Asset_Code='"+AssetCode+"' AND a.From_Id=b.Form_Id  AND b.Field_Type='meter'  GROUP BY From_Id";
            Cursor cursor= db.rawQuery(query, null);

            if(cursor.getCount() >0){
                count=1;
            }

            cursor.close();
            db.close();
            if(count > 0)
                resetValue= true;
            else
                resetValue =  false;
        }
        catch (Exception e)
        {
            Log.d("ua474 ","ERROR==" + e);
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error code: ua474", Toast.LENGTH_SHORT).show();
        }

        return  resetValue;
    }
    public void save(String Asset_Code,String Remark) {
        String Asset_Status_Id="";
        String Previous_Asset_Status_Id="";
        String Status="";
        for (RadioGroup rdgrp : textRadioGroupList) {
            String selectRB="";
            int cbid=rdgrp.getId();

            try{
                int selectedId=rdgrp.getCheckedRadioButtonId();
                View rb1 = rdgrp.findViewById(selectedId);
                int idx= rdgrp.indexOfChild(rb1);
                RadioButton radioButton = (RadioButton) rdgrp.getChildAt(idx);
                if(radioButton.isChecked())
                {
                    selectRB=radioButton.getText().toString();
                    db=myDb.getReadableDatabase();
                    Cursor cursor3 = db.rawQuery("SELECT Asset_Status_Id,Status FROM Asset_Status WHERE Status = '"+selectRB +"'",null);
                    if (cursor3.moveToFirst()) {
                        do {
                            Asset_Status_Id = cursor3.getString(cursor3.getColumnIndex("Asset_Status_Id"));
                            Status = cursor3.getString(cursor3.getColumnIndex("Status"));
                        }
                        while (cursor3.moveToNext());
                    }
                    cursor3.close();
                    db.close();
                }
                else{
                    Toast.makeText(getBaseContext(), selectRB + " Radio value1", Toast.LENGTH_LONG).show();
                }
            }
            catch(NullPointerException e){
                System.out.println("aa514 ERROR=="+ e);
                Toast.makeText(getApplicationContext(), "Error code: aa514", Toast.LENGTH_SHORT).show();
            }
            catch(Exception e){
                System.out.println("aa518 ERROR==" + e);
                Toast.makeText(getApplicationContext(), "Error code: aa518", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                System.out.println("-------ee FD radio  "+"Id_"+cbid+" value "+selectRB) ;

            }
        }

        try {
            db=myDb.getReadableDatabase();
            Cursor cursorAsset = db.rawQuery("SELECT Asset_Status_Id FROM Asset_Details WHERE Asset_Code = '"+Asset_Code +"'",null);
            if (cursorAsset.moveToFirst()) {
                do {
                    Previous_Asset_Status_Id = cursorAsset.getString(cursorAsset.getColumnIndex("Asset_Status_Id"));
                }
                while (cursorAsset.moveToNext());
            }
            cursorAsset.close();
            db.close();

            AssetUpdate(Asset_Status_Id,Status, etTime.getText().toString(),date,Asset_Code,Previous_Asset_Status_Id,Remark);

        } catch (Exception e) {
            Log.d("ua586 ","ERROR==" + e);
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error code: ua586", Toast.LENGTH_SHORT).show();        }
    }

    public void AssetUpdate(String Asset_Status_Id,String Status,String ManualTime,String AssetUpdateTime,String Asset_Code,String Previous_Asset_Status_Id,String Remark){
        ContentValues cvAssetDetails = new ContentValues();
        cvAssetDetails.put("Asset_Status_Id", Asset_Status_Id);
        cvAssetDetails.put("Status", Status);
        cvAssetDetails.put("Manual_Time",ManualTime);
        cvAssetDetails.put("Asset_Update_Time", AssetUpdateTime);
        cvAssetDetails.put("UpdatedStatus", "no");
        db= myDb.getWritableDatabase();
        long AssetUpdate = db.update("asset_Details", cvAssetDetails, "Asset_Code ='" + Asset_Code + "' and Status <>'"+Status+"'", null);
        db.close();
        if(!Status.equals("WORKING")) {
            if(LOG) Log.d(TAG,"updateTaskDetails"+Asset_Id+Scan_Type+Status);
            boolean updateTaskDetails = myDb.updatedTaskCancelled(Asset_Id, "Cancelled", "",Scan_Type, Status);
            if (updateTaskDetails == true) {
                if(LOG) Log.d(TAG,"Cancelled"+"Task is cancelled");
            }
        }else {
            if(LOG) Log.d(TAG,"updateTaskDetails"+Asset_Id+Scan_Type+Status);
            myDb.updatedTaskCancelled(Asset_Id, "Pending", "", Scan_Type, Status);
        }

        if(AssetUpdate == 0){
            if(LOG) Log.d(TAG,"asset_Details"+"Status not updated");}
        else {
            ContentValues cvAssetStatusLog = new ContentValues();
            cvAssetStatusLog.put("Asset_Id", assetsId);
            cvAssetStatusLog.put("Site_Location_Id", site_id);
            cvAssetStatusLog.put("Previous_Asset_Status_Id", Previous_Asset_Status_Id);
            cvAssetStatusLog.put("Asset_Status_Id", Asset_Status_Id);
            cvAssetStatusLog.put("Manual_Time", ManualTime);
            cvAssetStatusLog.put("Asset_Updated_Time",AssetUpdateTime);
            cvAssetStatusLog.put("Remark",Remark);
            cvAssetStatusLog.put("Assigned_To_User_Id",User_Id);
            cvAssetStatusLog.put("UpdatedStatus", "no");
            db= myDb.getWritableDatabase();
            long resultset1 = db.insert("AssetStatusLog", null,cvAssetStatusLog);
            db.close();
            if(resultset1 == -1)
                Log.d(TAG,"Asset_Status_Change_Details not updated");
            else {
                Log.d(TAG,"Asset_Status_Change_Details data updated " + Asset_Status_Id);
                Intent intent = new Intent(AssetsActivity.this, AssetsActivity.class);
                intent.putExtra("site_id", site_id);
                startActivity(intent);
                finish();
            }


        }
    }
    private void datePicker(){
        // Get Current Date
        try {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {

                    //date_time = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                    date_time = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    //*************Call Time Picker Here ********************
                    timePicker();
                }
            }, mYear, mMonth, mDay);
            datePickerDialog.show();
        } catch (Exception e) {
            Log.d("ua611 ","ERROR==" + e);
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error code: ua611", Toast.LENGTH_SHORT).show();
        }
    }
    private void timePicker(){
        try {
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,int minute) {
                            mHour = hourOfDay;
                            mMinute = minute;
                            etTime.setText(date_time + " " + hourOfDay + ":" + minute+ ":00" );
                            updatedtime = date_time+" "+hourOfDay+":"+minute+ ":00";
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        } catch (Exception e) {
            Log.d("ua635 ","ERROR==" + e);
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error code: ua635", Toast.LENGTH_SHORT).show();
        }
    }
    public void initToolBar() {
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        etSearch = (EditText) findViewById(R.id.etSearch);

        ImageView imageViewAssets = (ImageView)findViewById(R.id.titleFragment2);
        //String abc = settings.getString("ScanType", null);
        try{
            if(Scan_Type.equalsIgnoreCase("NFC")) {
                imageViewAssets.setVisibility(View.GONE);
            }
        }catch (Exception e){
            Log.d("ua651 ","ERROR==" + e);
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error code: ua651", Toast.LENGTH_SHORT).show();
        }

        setSupportActionBar(toolbar);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                try {
                    db = myDb.getReadableDatabase();
                    Cursor cursor = db.rawQuery("SELECT * FROM asset_Details where Site_Location_Id ='" + site_id + "' and (Asset_Code LIKE '%" + charSequence + "%' OR Asset_Name LIKE '%" + charSequence + "%' OR Asset_Location LIKE '%" + charSequence + "%' OR Status LIKE '" + charSequence + "%')", null);
                    listDataAdapter = new ListDataAdapter(getApplicationContext(), R.layout.asset_list_item);
                    lvAssets.setAdapter(listDataAdapter);
                    if (cursor.moveToFirst()) {
                        do {
                            String Asset_Id = cursor.getString(cursor.getColumnIndex("Asset_Id"));
                            String Site_Location_Id = cursor.getString(cursor.getColumnIndex("Site_Location_Id"));
                            String Asset_Code = cursor.getString(cursor.getColumnIndex("Asset_Code"));
                            String Asset_Name = cursor.getString(cursor.getColumnIndex("Asset_Name"));
                            String Asset_Location = cursor.getString(cursor.getColumnIndex("Asset_Location"));
                            String Asset_Status_Id = cursor.getString(cursor.getColumnIndex("Asset_Status_Id"));
                            String Status = cursor.getString(cursor.getColumnIndex("Status"));
                            DataProvider assetDataProvider = new DataProvider(Asset_Id, Asset_Code, Asset_Name, Asset_Location, Status, null, null, null);
                            listDataAdapter.add(assetDataProvider);
                        }
                        while (cursor.moveToNext());
                    }
                    cursor.close();
                    db.close();

                } catch (Exception e) {
                    if(LOG) Log.d("aa688 ERROR==", "" + e);
                    Toast.makeText(getApplicationContext(), "Error code: aa688", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        imageViewAssets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                IntentIntegrator integrator = new IntentIntegrator(AssetsActivity.this);
                integrator.setPrompt("Scan a QRcode");
                integrator.setOrientationLocked(true);
                Intent intent = integrator.createScanIntent();
                startActivityForResult(intent, 49374);
            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        try {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result.getContents() != null) {
                etSearch.setText(result.getContents());
                db=myDb.getReadableDatabase();
                Cursor cursor1 = db.rawQuery("select * from asset_Details where Asset_Code ='" + result.getContents() + "'", null);
                if(cursor1.getCount() == 0)
                {
                    Toast.makeText(getApplicationContext(), "Please Enter Valid", Toast.LENGTH_LONG).show();
                }
                else
                {
                    AssetDialog(result.getContents());
                }
                cursor1.close();
                db.close();
            }else{
                Toast.makeText(getApplicationContext(), "No barcode Found", Toast.LENGTH_LONG).show();}

        }catch (Exception e)
        {
            Log.d("aa734 ERROR==","" + e);
            Toast.makeText(getApplicationContext(), "Error code: aa734", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
