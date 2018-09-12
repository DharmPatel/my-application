package com.example.google.template;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

public class PpmDemo extends FragmentActivity {
    android.support.v7.widget.Toolbar toolbar;
    TabLayout tabLayout;
    Fragment Pending, Missed, Completed;
    BadgeView badgeView1, badgeView2, badgeView3;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ppm_demo);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar1);
        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        toolbar.setTitle("PPM Tasks" + BuildConfig.VERSION_NAME);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#ffffff"));

        bindWidgetsWithAnEvent();
        setUpTablayout();
        BadgeData();
    }



    private void setUpTablayout(){
        Pending = new PendingPPMTask();
        Missed = new MissedPPMTask();
        Completed = new CompletedPPMTask();

        tabLayout.addTab(tabLayout.newTab().setText("Missed Task").setIcon(R.drawable.ic_clear));
        tabLayout.addTab(tabLayout.newTab().setText("Pending Task").setIcon(R.drawable.ic_alarm2),true);
        tabLayout.addTab(tabLayout.newTab().setText("Completed Task").setIcon(R.drawable.ic_done));
    }

    private void bindWidgetsWithAnEvent(){
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setCurrentTabFragment(tab.getPosition());
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

    private void setCurrentTabFragment(int tabPosition) {
        switch (tabPosition)
        {
            case 0 :
                replaceFragment(Missed);
                break;
            case 1 :
                replaceFragment(Pending);
                break;
            case 2 :
                replaceFragment(Completed);
                break;
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.Fragment, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN
        );
        ft.commit();
    }

    public void BadgeData(){
        badgeView1 = new BadgeView(this,((ViewGroup)tabLayout.getChildAt(0)).getChildAt(0));
        badgeView1.setText("0");
        badgeView1.show();

        badgeView2 = new BadgeView(this,((ViewGroup)tabLayout.getChildAt(0)).getChildAt(1));
        badgeView2.setText("1");
        badgeView2.show();

        badgeView3 = new BadgeView(this,((ViewGroup)tabLayout.getChildAt(0)).getChildAt(2));
        badgeView3.setText("2");
        badgeView3.show();
    }
}


