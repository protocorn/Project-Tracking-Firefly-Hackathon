package com.example.projecttracker.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projecttracker.Adapater.ChipAdapter;
import com.example.projecttracker.MainActivity;
import com.example.projecttracker.R;
import com.example.projecttracker.databinding.FragmentSignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.okhttp.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SignUpFrag extends Fragment {
    FragmentSignUpBinding binding;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter2;
    ArrayAdapter<String> adapter3;

    ArrayAdapter<String> year;
    ArrayAdapter<String> degree;


    List<String> list;
    List<String> universitylist;
    List<String> Institutelist;
    ChipAdapter chipAdapter;
    FirebaseAuth auth;
    FirebaseFirestore database;
    HashMap<String, Object> hashMap = new HashMap<>();
    String url;
    RequestQueue requestQueue;


    public SignUpFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(getLayoutInflater(), container, false);
        list = new ArrayList<>();
        universitylist = new ArrayList<>();
        Institutelist = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        url = "https://raw.githubusercontent.com/VarthanV/Indian-Colleges-List/master/colleges.json";

        String[] technical = new String[]{"Data Structures", "HTML & CSS", "SQL & NoSQL", "JavaScript", "PHP", "Algorithmic Coding", "Graphic Designing", "User Experience",
                "Game Development", "App Development", "Web Designing", "Audio Mixing", "Complex Problem Solving", "UI/UX Designing", "Deep Learning", "Blockchain", "C/C++", "Swift", "Kotlin", "Virtual Reality", "C#",
                "Python", "Java", "Flutter", "Ruby", "Machine Learning", "IoT", "Matrix Algebra", "Game Theory", "Cryptography", "Statistics For Data Science", "Calculus", "Probability Theory", "Cloud Developing", "Data Analytics",
                "AI Foundations", "Google Cloud", "CyberSecurity", "Image & Video Processing", "Discrete Mathematics", "Executive Data Science", "Fluid Mechanics"
        };

        String[] yearlist = new String[]{"2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009",
                "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024",
                "2025", "2026", "2027", "2028", "2029", "2030"};

        String[] degree_list = new String[]{"BE/B.TECH Aeronautical Engineering", "BE/B.TECH Automobile Engineering", "BE/B.TECH Civil Engineering",
                "BE/B.TECH Computer Science and Engineering", "BE/B.TECH Biotechnology Engineering", "BE/B.TECH Electrical and Electronics Engineering",
                "BE/B.TECH Electronics and Communication Engineering", "BE/B.TECH Information Technology", "BE/B.TECH Automation and Robotics", "BCA - Bachelor of Computer Applications",
                "B.Sc.- Information Technology", "B.Sc. Mathematics"};


        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jo = jsonArray.getJSONObject(i);
                        if (!universitylist.contains(jo.getString("university"))) {
                            universitylist.add(jo.getString("university"));
                        }
                        Institutelist.add(jo.getString("college"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);
        adapter = new ArrayAdapter<String>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, universitylist);
        binding.autoText2.setAdapter(adapter);

        adapter2 = new ArrayAdapter<String>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, Institutelist);
        binding.autoText3.setAdapter(adapter2);


        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.nextBtn.getText().equals("Next")) {
                    if (!binding.pass.getText().toString().equals(binding.confirmpass.getText().toString())) {
                        binding.pass.setError("Password does not match");
                        return;
                    }
                    hashMap.put("name", binding.username.getText().toString());
                    hashMap.put("email", binding.email.getText().toString());
                    hashMap.put("password", binding.pass.getText().toString());
                    hashMap.put("profile_url","");
                    binding.regStep1.setVisibility(View.GONE);
                    binding.regStep2.setVisibility(View.VISIBLE);
                    binding.nextBtn.setText("NEXT");
                } else if (binding.nextBtn.getText().equals("NEXT")) {
                    hashMap.put("University", binding.autoText2.getText().toString());
                    hashMap.put("Institute", binding.autoText3.getText().toString());
                    hashMap.put("Degree", binding.degreeSpin.getSelectedItem().toString());
                    hashMap.put("Passing Year", binding.yrSpin.getSelectedItem().toString());
                    binding.regStep2.setVisibility(View.GONE);
                    binding.regStep3.setVisibility(View.VISIBLE);
                    binding.nextBtn.setText("Register");
                } else if (binding.nextBtn.getText().equals("Register")) {
                    if (list.size() > 2) {
                        hashMap.put("Skills", list);
                        hashMap.put("Github username", binding.githubLink.getText().toString());
                        auth.createUserWithEmailAndPassword(binding.email.getText().toString(), binding.pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    database.collection("Students").document(auth.getCurrentUser().getUid()).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(getContext(), "Sign-Up Was Successful", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getContext(), MainActivity.class));
                                            HashMap<String, Object> hashMap1 = new HashMap<>();
                                            hashMap1.put("uid", auth.getCurrentUser().getUid());
                                            database.collection("Institutes").document(binding.autoText3.getText().toString()).collection("students").document(auth.getCurrentUser().getUid()).set(hashMap1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {

                                                }
                                            });
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });

                    } else {
                        Toast.makeText(getContext(), "Please Select At Least Three Skills", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        chipAdapter = new ChipAdapter(list, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        binding.chipList.setLayoutManager(linearLayoutManager);
        binding.chipList.setAdapter(chipAdapter);


        degree = new ArrayAdapter<String>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, degree_list);
        binding.degreeSpin.setAdapter(degree);

        year = new ArrayAdapter<String>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, yearlist);
        binding.yrSpin.setAdapter(year);

        adapter3 = new ArrayAdapter<String>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, technical);
        binding.autoText.setAdapter(adapter3);


        binding.autoText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (list.contains(binding.autoText.getText().toString())) {
                    binding.autoText.setText("");
                    Toast.makeText(getContext(), "Skill has already been added", Toast.LENGTH_SHORT).show();
                } else {
                    list.add(binding.autoText.getText().toString());
                    binding.autoText.setText("");
                    chipAdapter.notifyDataSetChanged();
                }
            }
        });
        return binding.getRoot();
    }
}