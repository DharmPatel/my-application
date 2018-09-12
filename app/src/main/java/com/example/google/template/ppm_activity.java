package com.example.google.template;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;

public class ppm_activity extends AppCompatActivity implements PendingPPMTask.OnCompleteListener, CompletedPPMTask.OnCompleteListener, MissedPPMTask.OnCompleteListener{
    android.support.v7.widget.Toolbar toolbar;
    DatabaseHelper myDb;
    TabLayout tablayout;
    ViewPager viewPager;
    String User_Id;
    ViewPagerAdapter viewPagerAdapter;
    private int[] tabIcons = {
            R.drawable.ic_clear,
            R.drawable.ic_alarm_white_24dp,
            R.drawable.ic_done,
            R.drawable.ic_priority
    };
    BadgeView missedBadge, pendingbadge, completedbadge;
    static boolean pending,complete,missed;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ppm_task);
        pending =false;complete =false;missed =false;
        myDb=new DatabaseHelper(this);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        User_Id = settings.getString("userId", null);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar1);
        toolbar.setTitle("PPM Tasks" + BuildConfig.VERSION_NAME);
        setSupportActionBar(toolbar);
        tablayout = (TabLayout) findViewById(R.id.tabLayout);
        tablayout.setSelectedTabIndicatorColor(Color.parseColor("#ffffff"));
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tablayout.animate();
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addfragment(new MissedPPMTask(), "Missed");
        viewPagerAdapter.addfragment(new PendingPPMTask(), "Pending");
        viewPagerAdapter.addfragment(new CompletedPPMTask(), "Completed");
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        try {
            String tabCurrentItem = getIntent().getStringExtra("TAB");
           Log.d("tabCurrentItem",tabCurrentItem);
            if(tabCurrentItem.equalsIgnoreCase("TAB3")){
                viewPager.setCurrentItem(2);
                viewPager.setOffscreenPageLimit(2);
            }
            else{
                viewPager.setCurrentItem(1);
                viewPager.setOffscreenPageLimit(2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        tablayout.setupWithViewPager(viewPager);
        setupTabIcons();
         missedBadge = new BadgeView(this,((ViewGroup)tablayout.getChildAt(0)).getChildAt(0));
        pendingbadge = new BadgeView(this,((ViewGroup)tablayout.getChildAt(0)).getChildAt(1));
        completedbadge = new BadgeView(this,((ViewGroup)tablayout.getChildAt(0)).getChildAt(2));

        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
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

    private void setupTabIcons() {
        tablayout.getTabAt(0).setIcon(tabIcons[0]);
        tablayout.getTabAt(1).setIcon(tabIcons[1]);
        tablayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    public void setPPMPending(boolean data) { pending = data; }
    public void setPPMComplete(boolean data) { complete = data; }
    public void setPPMMissed(boolean data) { missed = data; }

    @Override
    public void onBackPressed() {
        if (pending == true && complete == true && missed == true) {
            Intent intent = new Intent(ppm_activity.this, HomePage.class);
            intent.putExtra("User_Id", User_Id);
            startActivity(intent);
            getFragmentManager().popBackStackImmediate();
            finish();
            super.onBackPressed();
        }

    }



    @Override
    public void onPending(String count) {
        pendingbadge.setText(count);
        pendingbadge.show();
    }

    @Override
    public void onComplete(String count) {
        completedbadge.setText(count);
        completedbadge.show();
    }

    @Override
    public void onMissed(String count) {
        missedBadge.setText(count);
        missedBadge.show();
    }
}
