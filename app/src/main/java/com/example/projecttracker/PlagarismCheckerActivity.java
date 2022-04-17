package com.example.projecttracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import java.net.*;

import com.example.projecttracker.databinding.ActivityPlagarismCheckerBinding;
import com.google.logging.type.HttpRequest;

public class PlagarismCheckerActivity extends AppCompatActivity {
    ActivityPlagarismCheckerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityPlagarismCheckerBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        binding.checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
            });
    }
}