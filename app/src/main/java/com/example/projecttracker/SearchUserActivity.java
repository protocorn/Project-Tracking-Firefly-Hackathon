package com.example.projecttracker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.projecttracker.Adapater.UserAdapter;
import com.example.projecttracker.Models.Users;
import com.example.projecttracker.databinding.ActivitySearchUserBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchUserActivity extends AppCompatActivity {
    ActivitySearchUserBinding binding;
    ArrayAdapter<String> skills;
    ArrayAdapter<String> year;
    ArrayAdapter<String> from;
    ArrayAdapter<String> degree;
    FirebaseAuth auth;
    ArrayList<Users> list;
    FirebaseFirestore firestore;

    UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivitySearchUserBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        list = new ArrayList<>();

        String[] technical = new String[]{"Data Structures", "HTML & CSS", "SQL & NoSQL", "JavaScript", "PHP", "Algorithmic Coding", "Graphic Designing", "User Experience",
                "Game Development", "App Development", "Web Designing", "Audio Mixing", "Complex Problem Solving", "UI/UX Designing", "Deep Learning", "Blockchain", "C/C++", "Swift", "Kotlin", "Virtual Reality", "C#",
                "Python", "Java", "Flutter", "Ruby", "Machine Learning", "IoT", "Matrix Algebra", "Game Theory", "Cryptography", "Statistics For Data Science", "Calculus", "Probability Theory", "Cloud Developing", "Data Analytics",
                "AI Foundations", "Google Cloud", "CyberSecurity", "Image & Video Processing", "Discrete Mathematics", "Executive Data Science", "Fluid Mechanics"
        };

        String[] yearlist = new String[]{"2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009",
                "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024",
                "2025", "2026", "2027", "2028", "2029", "2030"};

        String[] from_1 = new String[]{"My Institute", "My University", "Any Institute"};

        String[] degree_list = new String[]{"BE/B.TECH Aeronautical Engineering", "BE/B.TECH Automobile Engineering", "BE/B.TECH Civil Engineering",
                "BE/B.TECH Computer Science and Engineering", "BE/B.TECH Biotechnology Engineering", "BE/B.TECH Electrical and Electronics Engineering",
                "BE/B.TECH Electronics and Communication Engineering", "BE/B.TECH Information Technology", "BE/B.TECH Automation and Robotics", "BCA - Bachelor of Computer Applications",
                "B.Sc.- Information Technology", "B.Sc. Mathematics"};


        skills = new ArrayAdapter<String>(SearchUserActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, technical);
        binding.skillsSpinner.setAdapter(skills);

        year = new ArrayAdapter<String>(SearchUserActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, yearlist);
        binding.yearSpinner.setAdapter(year);

        from = new ArrayAdapter<String>(SearchUserActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, from_1);
        binding.instSpinner.setAdapter(from);

        degree = new ArrayAdapter<String>(SearchUserActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, degree_list);
        binding.degreeSpinner.setAdapter(degree);

        userAdapter = new UserAdapter(list, SearchUserActivity.this);
        binding.userList.setAdapter(userAdapter);

        binding.srchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firestore.collection("Students").document(auth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value1, @Nullable FirebaseFirestoreException error) {
                        list.clear();
                        String skill = binding.skillsSpinner.getSelectedItem().toString();
                        String year1 = binding.yearSpinner.getSelectedItem().toString();
                        String from1 = binding.instSpinner.getSelectedItem().toString();
                        String degree1 = binding.degreeSpinner.getSelectedItem().toString();
                        firestore.collection("Students").addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                for (DocumentSnapshot snapshot : value.getDocuments()) {
                                    if (snapshot.get("Skills").toString().contains(skill)
                                            && snapshot.get("Passing Year").equals(year1)
                                            && snapshot.get("Degree").equals(degree1)) {
                                        if(from1.equals("My Institute")&& snapshot.get("Institute").equals(value1.get("Institute"))){
                                            Users users = snapshot.toObject(Users.class);
                                            users.setUserid(snapshot.getId());
                                            if (!users.getUserid().equals(FirebaseAuth.getInstance().getUid()) && !list.contains(users)) {
                                                list.add(users);
                                            }
                                        }
                                        else if(from1.equals("My University")&& snapshot.get("University").equals(value1.get("University"))){
                                            Users users = snapshot.toObject(Users.class);
                                            users.setUserid(snapshot.getId());
                                            if (!users.getUserid().equals(FirebaseAuth.getInstance().getUid()) && !list.contains(users)) {
                                                list.add(users);
                                            }
                                        }
                                        else {
                                            binding.instSpinner.setSelection(2);
                                            Users users = snapshot.toObject(Users.class);
                                            users.setUserid(snapshot.getId());
                                            if (!users.getUserid().equals(FirebaseAuth.getInstance().getUid()) && !list.contains(users)) {
                                                list.add(users);
                                            }
                                        }

                                    }
                                }
                                userAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                });
            }
        });
    }
}