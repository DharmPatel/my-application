package com.example.google.csmia_temp.Helpdesk.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.google.csmia_temp.BuildConfig;
import com.example.google.csmia_temp.DatabaseHelper;
import com.example.google.csmia_temp.HomePage;
import com.example.google.csmia_temp.NFC;
import com.example.google.csmia_temp.R;
import com.example.google.csmia_temp.ViewPagerAdapter;
import com.example.google.csmia_temp.applicationClass;

public class TicketTabActivity extends AppCompatActivity {
    static final boolean LOG = new applicationClass().checkLog();
    private static final String TAG = TicketTabActivity.class.getSimpleName();
    static boolean pendingNFC;
    android.support.v7.widget.Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    SharedPreferences settings;
    DatabaseHelper myDb;
    SQLiteDatabase db;
    NfcAdapter mNfcAdapter;
    NFC nfc;
    String Scan_Type,User_Id;
    LinearLayout mainLinearLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickettab_layout);
        mainLinearLayout = (LinearLayout)findViewById(R.id.tickettablayout);
        myDb = new DatabaseHelper(getApplicationContext());
        tabLayout = (TabLayout) findViewById(R.id.TickettabLayout);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#ffffff"));
        tabLayout.animate();
        viewPager = (ViewPager) findViewById(R.id.TicketviewPager);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_addcomplain);
        toolbar.setTitle("IMS" + BuildConfig.VERSION_NAME);
        setSupportActionBar(toolbar);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addfragment(new Filter_Ticket(), "Filtered");
        viewPagerAdapter.addfragment(new All_Ticket(), "All");
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(1);
        tabLayout.setupWithViewPager(viewPager);
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        User_Id = settings.getString("userId", null);
        Scan_Type = myDb.ScanType(User_Id);
        NFC();

    }

    @Override
    public void onResume() {
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
    public void onPause() {
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

    public void setNfcStatus(boolean data) {
        Log.d("booleanStatus",pendingNFC+""+data);
        pendingNFC = data;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        try {
            if(!Scan_Type.equals("QR")) {
                Parcelable[] parcelables = intent.getParcelableArrayExtra(mNfcAdapter.EXTRA_NDEF_MESSAGES);
                if(parcelables !=null && parcelables.length>0)
                    nfc.readnfc((NdefMessage)parcelables[0]);
                Log.d("djfhgjkdfg",pendingNFC+" ");
                if(pendingNFC) {
                    Log.d("InTicketAct_NFC",nfc.tagcontent);
                    new Filter_Ticket().nfcData(nfc.tagcontent);
                }
                else {
                    viewPager.setCurrentItem(0);
                }
            }
        }catch (Exception e){
            if(LOG)Log.d(TAG,"aa215"+"ERROR==" + e);
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error code: aa215", Toast.LENGTH_SHORT).show();
        }
    }



    private void NFC(){
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
                                    .make(mainLinearLayout, "NFC is disabled", Snackbar.LENGTH_LONG)
                                    .setAction("Change Setting.", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                                        }
                                    });

                            snackbar.setDuration(20000);
                            snackbar.show();
                        } /*else {
                            //Toast.makeText(getApplicationContext(), "Tap device with NFC tag", Toast.LENGTH_SHORT).show();
                        }*/
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), HomePage.class);
        intent.putExtra("User_Id", User_Id);
        startActivity(intent);
        getFragmentManager().popBackStackImmediate();
        finish();
    }

}
