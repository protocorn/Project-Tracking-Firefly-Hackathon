package com.example.projecttracker.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.projecttracker.R;
import com.example.projecttracker.SearchUserActivity;
import com.example.projecttracker.databinding.FragmentHomeBinding;


public class HomeFrag extends Fragment {
    FragmentHomeBinding binding;

    public HomeFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        binding.animation1.playAnimation();
        binding.animation1.setProgress(1);
        // Inflate the layout for this fragment

        binding.searchCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), SearchUserActivity.class));
            }
        });
        return binding.getRoot();
    }
}