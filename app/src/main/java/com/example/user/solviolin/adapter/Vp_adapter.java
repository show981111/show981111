package com.example.user.solviolin.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class Vp_adapter extends FragmentPagerAdapter {

    private Fragment[] arrFragments;

    public Vp_adapter(FragmentManager fm, Fragment[] arrFragments) {
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
            return "모든수업 변경";
        }else {
            return "한번수업 변경";
        }
    }
}
