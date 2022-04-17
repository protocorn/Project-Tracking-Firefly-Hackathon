package com.example.projecttracker.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.projecttracker.Adapater.MyProjectsAdapter;
import com.example.projecttracker.Adapater.MyProjectsAdapter2;
import com.example.projecttracker.R;
import com.example.projecttracker.databinding.FragmentSearchBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchFrag extends Fragment {
    FragmentSearchBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    ArrayAdapter adapter;
    String inst_name;
    MyProjectsAdapter2 myProjectsAdapter;

    ArrayList<String> projects;
    ArrayList<String> uid;

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
        uid=new ArrayList<>();

        String[] spinner_list = new String[]{"Institution", "All Projects"};

        adapter = new ArrayAdapter<String>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, spinner_list);
        binding.spinner2.setAdapter(adapter);

        myProjectsAdapter = new MyProjectsAdapter2(getContext(), projects,uid);
        binding.searchProjectList.setAdapter(myProjectsAdapter);

        firestore.collection("Students").document(auth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                projects.clear();
                inst_name = "" + value.get("Institute");
                binding.searchProject.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                        if(s.toString().contains(" ")) {
                            String new_text = binding.searchProject.getText().toString().replaceAll(" ", "-");
                            binding.searchProject.setText(new_text);
                            Selection.setSelection(binding.searchProject.getText(),binding.searchProject.getText().length() );
                        }
                        if (binding.spinner2.getSelectedItem().toString().equals("Institution")) {
                            projects.clear();
                            Query query = firestore.collection("Institutes").document(inst_name).collection("Projects").orderBy("project_name").startAt(s.toString()).endAt(s.toString() + "\uf8ff");
                            query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                                        projects.add("" + snapshot.get("project_name"));
                                    }
                                    myProjectsAdapter.notifyDataSetChanged();
                                }
                            });
                        } else {
                            projects.clear();
                            Query query = firestore.collection("AllProjects").orderBy("project_name").startAt(s.toString()).endAt(s.toString() + "\uf8ff");
                            query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                                        projects.add("" + snapshot.get("project_name"));
                                    }
                                    myProjectsAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }
        });


        firestore.collection("Students").document(auth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                inst_name = "" + value.get("Institute");
                projects.clear();
                uid.clear();
                firestore.collection("Institutes").document(inst_name).collection("Projects").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for (DocumentSnapshot snapshot : value.getDocuments()) {
                            projects.add("" + snapshot.get("project_name"));
                            uid.add(""+snapshot.get("uid"));
                        }
                        myProjectsAdapter.notifyDataSetChanged();
                    }
                });
                binding.spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        projects.clear();
                        uid.clear();
                        if (i == 1) {
                            firestore.collection("AllProjects").addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                                        projects.add("" + snapshot.get("project_name"));
                                    }
                                    myProjectsAdapter.notifyDataSetChanged();
                                }
                            });
                        } else {
                            firestore.collection("Institutes").document(inst_name).collection("Projects").addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                    for (DocumentSnapshot snapshot : value.getDocuments()) {
                                        projects.add("" + snapshot.get("project_name"));
                                        uid.add(""+snapshot.get("uid"));
                                    }
                                    myProjectsAdapter.notifyDataSetChanged();
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