package com.example.projecttracker.Fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projecttracker.Adapater.ChipAdapter;
import com.example.projecttracker.Adapater.ChipAdapter2;
import com.example.projecttracker.Adapater.GitProjectAdapter;
import com.example.projecttracker.Adapater.MyProjectsAdapter;
import com.example.projecttracker.R;
import com.example.projecttracker.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProfileFrag extends Fragment {
    FragmentProfileBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    List<String> list;
    ArrayList<String> projectlist;
    ArrayList<String> projectlist2;
    ChipAdapter2 chipAdapter;
    RequestQueue requestQueue;
    GitProjectAdapter gitProjectAdapter;
    MyProjectsAdapter myProjectsAdapter;

    public ProfileFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentProfileBinding.inflate(getLayoutInflater(),container,false);
        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        list = new ArrayList<String>();
        projectlist = new ArrayList<String>();
        projectlist2 = new ArrayList<String>();


        firestore.collection("Students").document(auth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                binding.myUsername.setText(""+value.get("name"));

                String inst=""+value.get("Institute");
                binding.instName.setText(inst.replaceAll("[0-9]",""));
                binding.degName.setText(""+value.get("Degree"));
                binding.yrPass.setText(""+value.get("Passing Year"));

                chipAdapter = new ChipAdapter2((List<String>) value.get("Skills"), getContext());
                binding.skillList.setAdapter(chipAdapter);

            }
        });

        firestore.collection("Students").document(auth.getCurrentUser().getUid()).collection("Projects").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(DocumentSnapshot snapshot:value.getDocuments()){
                    projectlist2.add(""+snapshot.get("project_name"));
                }
                myProjectsAdapter = new MyProjectsAdapter(getContext(),projectlist2);
                binding.projectList.setAdapter(myProjectsAdapter);
            }
        });

        binding.addProjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog=new Dialog(getContext());
                dialog.setContentView(R.layout.project_dlg);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                RecyclerView proj_title_list;
                proj_title_list=dialog.findViewById(R.id.github_proj_list);
                firestore.collection("Students").document(auth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        String github_username=""+value.get("Github username");
                        String url="https://api.github.com/users/"+github_username+"/repos";
                        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
                        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                projectlist.clear();
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jo = jsonArray.getJSONObject(i);
                                        projectlist.add(jo.getString("name"));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();

                                    Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                gitProjectAdapter = new GitProjectAdapter(getContext(), projectlist);
                                proj_title_list.setAdapter(gitProjectAdapter);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
                        requestQueue.add(stringRequest);
                        dialog.show();
                    }
                });
            }
        });

        return binding.getRoot();
    }
}