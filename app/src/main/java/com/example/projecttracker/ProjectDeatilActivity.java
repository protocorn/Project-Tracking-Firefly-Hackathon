package com.example.projecttracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projecttracker.Adapater.AddTeamAdapter;
import com.example.projecttracker.Adapater.GitProjectAdapter;
import com.example.projecttracker.Adapater.TeamMemberAdapater;
import com.example.projecttracker.databinding.ActivityProjectDeatilBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ProjectDeatilActivity extends AppCompatActivity {
    String project_name, inst_name;
    ActivityProjectDeatilBinding binding;
    FirebaseAuth auth;
    List<String> users;
    ArrayList<String> member_list;
    FirebaseFirestore firestore;
    private static String username, base_url;
    AddTeamAdapter adapter;
    TeamMemberAdapater teamMemberAdapater;
    ArrayList<String> avatarlist;
    HashMap<String, Object> hashMap;
    FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityProjectDeatilBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        users = new ArrayList<>();
        member_list = new ArrayList<>();
        avatarlist = new ArrayList<>();

        project_name = getIntent().getStringExtra("proj_name");

        firestore.collection("Students").document(auth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                username = "" + value.get("Github username");
                inst_name = "" + value.get("Institute");
                base_url = "https://github.com/" + username + "/" + project_name;
                binding.githubLink.setText(base_url);
                String url = "https://api.github.com/repos/" + username + "/" + project_name + "/contributors?page=1&?access_token=fff";
                RequestQueue requestQueue = Volley.newRequestQueue(ProjectDeatilActivity.this);
                StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        member_list.clear();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jo = jsonArray.getJSONObject(i);
                                member_list.add(jo.getString("login"));
                                avatarlist.add(jo.getString("avatar_url"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                            Toast.makeText(ProjectDeatilActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        teamMemberAdapater = new TeamMemberAdapater(avatarlist, member_list, ProjectDeatilActivity.this);
                        binding.teamList.setAdapter(teamMemberAdapater);

                        binding.addReport.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent video = new Intent();
                                video.setAction(Intent.ACTION_GET_CONTENT);
                                video.setType("application/pdf");
                                startActivityForResult(video, 30);
                            }
                        });
                        binding.addPpt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent video = new Intent();
                                video.setAction(Intent.ACTION_GET_CONTENT);
                                video.setType("application/vnd.ms-powerpoint");
                                startActivityForResult(video, 80);
                            }
                        });


                        hashMap = new HashMap<>();
                        hashMap.put("team_members", member_list);
                        firestore.collection("Institutes").document(inst_name).collection("Projects").addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value1, @Nullable FirebaseFirestoreException error) {
                                for (DocumentSnapshot snapshot1 : value1.getDocuments()) {
                                    if (snapshot1.get("project_name").toString().equals(project_name)) {
                                        firestore.collection("Institutes").document(inst_name).collection("Projects").document(snapshot1.getId()).update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        });
                                    }
                                }
                            }
                        });
                        firestore.collection("Students").document(auth.getCurrentUser().getUid()).collection("Projects").addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                for (DocumentSnapshot snapshot : value.getDocuments()) {
                                    if (snapshot.get("project_name").toString().equals(project_name)) {
                                        if (!snapshot.get("report").toString().equals("")) {
                                            binding.addReport.setVisibility(View.GONE);
                                            binding.openReport.setVisibility(View.VISIBLE);
                                        }
                                        binding.openReport.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Uri webpage = Uri.parse("" + snapshot.get("report"));
                                                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                                                startActivity(intent);
                                            }
                                        });
                                        firestore.collection("Students").document(auth.getCurrentUser().getUid()).collection("Projects").document(snapshot.getId()).update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        });
                                    }
                                }
                            }
                        });
                        firestore.collection("AllProjects").addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                for (DocumentSnapshot snapshot2 : value.getDocuments()) {
                                    if (snapshot2.get("project_name").toString().equals(project_name)) {
                                        firestore.collection("AllProjects").document(snapshot2.getId()).update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProjectDeatilActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                requestQueue = Volley.newRequestQueue(ProjectDeatilActivity.this);
                requestQueue.add(stringRequest);
            }
        });
        firestore.collection("Students").document(auth.getCurrentUser().getUid()).collection("Projects").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                users.clear();
                for (DocumentSnapshot snapshot : value.getDocuments()) {
                    users.add("" + snapshot.get("Github username"));
                }
            }
        });


        binding.addTeamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(ProjectDeatilActivity.this);
                dialog.setContentView(R.layout.add_team_dlg);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                RecyclerView user_list;
                user_list = dialog.findViewById(R.id.github_userlist);
                adapter = new AddTeamAdapter(users, ProjectDeatilActivity.this);
                user_list.setAdapter(adapter);
                dialog.show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 30) {
            if (data != null) {
                if (data.getData() != null) {
                    Uri file = data.getData();
                    StorageReference ref = firebaseStorage.getReference().child("Projects").child(String.valueOf(System.currentTimeMillis()));
                    ref.putFile(file).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            Toast.makeText(ProjectDeatilActivity.this, "Completed", Toast.LENGTH_SHORT).show();
                            if (task.isSuccessful()) {
                                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String filepath = uri.toString();
                                        HashMap<String, Object> hashMap1 = new HashMap<>();
                                        hashMap1.put("report", filepath);
                                        firestore.collection("Institutes").document(inst_name).collection("Projects").addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable QuerySnapshot value1, @Nullable FirebaseFirestoreException error) {
                                                for (DocumentSnapshot snapshot1 : value1.getDocuments()) {
                                                    if (snapshot1.get("project_name").toString().equals(project_name)) {
                                                        firestore.collection("Institutes").document(inst_name).collection("Projects").document(snapshot1.getId()).update(hashMap1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {

                                                            }
                                                        });
                                                    }
                                                }
                                            }
                                        });
                                        firestore.collection("Students").document(auth.getCurrentUser().getUid()).collection("Projects").addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                                for (DocumentSnapshot snapshot : value.getDocuments()) {
                                                    if (snapshot.get("project_name").toString().equals(project_name)) {
                                                        firestore.collection("Students").document(auth.getCurrentUser().getUid()).collection("Projects").document(snapshot.getId()).update(hashMap1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {

                                                            }
                                                        });
                                                    }
                                                }
                                            }
                                        });
                                        firestore.collection("AllProjects").addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                                for (DocumentSnapshot snapshot2 : value.getDocuments()) {
                                                    if (snapshot2.get("project_name").toString().equals(project_name)) {
                                                        firestore.collection("AllProjects").document(snapshot2.getId()).update(hashMap1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {

                                                            }
                                                        });
                                                    }
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    });
                }
            }
        }
    }
}