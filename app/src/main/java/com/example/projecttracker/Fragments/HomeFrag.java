package com.example.projecttracker.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projecttracker.YourPepsActivity;
import com.example.projecttracker.PlagarismCheckerActivity;
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
        binding.plagraismCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), PlagarismCheckerActivity.class));
            }
        });
        binding.pepsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), YourPepsActivity.class));
            }
        });

        return binding.getRoot();
    }
}