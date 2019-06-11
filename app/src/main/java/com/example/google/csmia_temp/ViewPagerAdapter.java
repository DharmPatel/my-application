package com.example.google.csmia_temp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Intel on 15-12-2016.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
ArrayList<Fragment> fragments =  new ArrayList<>();
ArrayList<String> subTitls =  new ArrayList<>();

    public void addfragment(Fragment fragments,String subTitls)
    {
this.fragments.add(fragments);
this.subTitls.add(subTitls);
    }
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return subTitls.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return subTitls.get(position);
    }
}
