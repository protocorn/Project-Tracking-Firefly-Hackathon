package com.example.projecttracker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.projecttracker.Adapater.ChipAdapter;
import com.example.projecttracker.Adapater.ChipAdapter2;
import com.example.projecttracker.Adapater.MyProjectsAdapter;
import com.example.projecttracker.Adapater.MyProjectsAdapter2;
import com.example.projecttracker.databinding.ActivityUserProfileBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity {
    ActivityUserProfileBinding binding;
    FirebaseFirestore firestore;
    String uid;
    ChipAdapter2 chipAdapter;
    MyProjectsAdapter2 myProjectsAdapter;
    ArrayList<String> list;
    ArrayList<String> list2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding= ActivityUserProfileBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        firestore=FirebaseFirestore.getInstance();
        list=new ArrayList<>();
        list2=new ArrayList<>();

        uid=getIntent().getStringExtra("uid");


        firestore.collection("Students").document(uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                binding.userUsername.setText(""+value.get("name"));

                String inst=""+value.get("Institute");
                binding.userInstName.setText(inst.replaceAll("[0-9]",""));
                binding.userDeg.setText(""+value.get("Degree"));
                binding.userPassout.setText(""+value.get("Passing Year"));

                chipAdapter = new ChipAdapter2((List<String>) value.get("Skills"),UserProfileActivity.this);
                binding.userSkills.setAdapter(chipAdapter);

            }
        });

        firestore.collection("Students").document(uid).collection("Projects").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(DocumentSnapshot snapshot:value.getDocuments()){
                    list.add(""+snapshot.get("project_name"));
                    list2.add(""+uid);
                }
                myProjectsAdapter=new MyProjectsAdapter2(UserProfileActivity.this,list,list2);
                binding.userProjects.setAdapter(myProjectsAdapter);
            }
        });

    }
}