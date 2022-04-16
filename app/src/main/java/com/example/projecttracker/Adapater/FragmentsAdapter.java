package com.example.projecttracker.Adapater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.projecttracker.Fragments.LoginFrag;
import com.example.projecttracker.Fragments.SignUpFrag;

public class FragmentsAdapter extends FragmentPagerAdapter {
    public FragmentsAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new LoginFrag();
            case 1: return new SignUpFrag();
        }
        return new SignUpFrag();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title= null;
        if(position==0){

            title="LOGIN";
        }
        if(position==1){

            title="REGISTER";
        }
        return title;
    }
}
