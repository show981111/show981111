package com.example.user.solviolin;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class Vp_resAdapter extends FragmentPagerAdapter {

    private Fragment[] arrFragments;

    public Vp_resAdapter(FragmentManager fm, Fragment[] arrFragments) {
        super(fm);
        this.arrFragments = arrFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return arrFragments[position];
    }

    @Override
    public int getCount() {
        return 2;
    }

    public CharSequence getPageTitle(int i){
        if(i == 0) {
            return "지난학기";
        }else {
            return "현재학기";
        }
    }
}