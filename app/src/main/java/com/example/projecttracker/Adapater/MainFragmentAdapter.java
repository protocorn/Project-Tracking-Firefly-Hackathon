package com.example.projecttracker.Adapater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.projecttracker.Fragments.HomeFrag;
import com.example.projecttracker.Fragments.LoginFrag;
import com.example.projecttracker.Fragments.ProfileFrag;
import com.example.projecttracker.Fragments.SearchFrag;
import com.example.projecttracker.Fragments.SignUpFrag;

public class MainFragmentAdapter extends FragmentPagerAdapter {

    public MainFragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new HomeFrag();
            case 1: return new SearchFrag();
            case 2: return new ProfileFrag();
        }
        return new HomeFrag();
    }

    @Override
    public int getCount() {
        return 3;
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title= null;
        if(position==0){

            title="HOME";
        }
        if(position==1){

            title="SEARCH";
        }
        if(position==2){

            title="PROFILE";
        }
        return title;
    }
}
