package com.example.projecttracker.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.projecttracker.Adapater.MyProjectsAdapter;
import com.example.projecttracker.R;
import com.example.projecttracker.databinding.FragmentSearchBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchFrag extends Fragment {
    FragmentSearchBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    ArrayAdapter adapter;
    String inst_name;
    MyProjectsAdapter myProjectsAdapter;

    ArrayList<String> projects;

    public SearchFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(getLayoutInflater(), container, false);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        projects = new ArrayList<>();

        String[] spinner_list = new String[]{"Institution", "All Projects"};

        adapter = new ArrayAdapter<String>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, spinner_list);
        binding.spinner2.setAdapter(adapter);


        firestore.collection("Students").document(auth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                inst_name = "" + value.get("Institute");
                projects.clear();
                binding.spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        projects.clear();
                        if(i==0){
                            firestore.collection("Institutes").document(inst_name).collection("Projects").addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                                        projects.add("" + snapshot.get("project_name"));
                                    }
                                    myProjectsAdapter = new MyProjectsAdapter(getContext(), projects);
                                    binding.searchProjectList.setAdapter(myProjectsAdapter);
                                }
                            });
                        }
                        else {
                            firestore.collection("AllProjects").addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                                        projects.add("" + snapshot.get("project_name"));
                                    }
                                    myProjectsAdapter = new MyProjectsAdapter(getContext(), projects);
                                    binding.searchProjectList.setAdapter(myProjectsAdapter);
                                }
                            });
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            }
        });


        return binding.getRoot();
    }
}