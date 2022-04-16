package com.example.projecttracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.projecttracker.Adapater.FragmentsAdapter;
import com.example.projecttracker.Adapater.MainFragmentAdapter;
import com.example.projecttracker.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        binding.viewpager2.setAdapter(new MainFragmentAdapter(getSupportFragmentManager()));
        binding.tabLayout2.setupWithViewPager(binding.viewpager2);
    }
}