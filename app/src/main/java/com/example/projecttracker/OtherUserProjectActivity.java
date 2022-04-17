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
import com.example.projecttracker.Adapater.TeamMemberAdapater;
import com.example.projecttracker.databinding.ActivityOtherUserProjectBinding;
import com.example.projecttracker.databinding.ActivityProjectDeatilBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OtherUserProjectActivity extends AppCompatActivity {
    String project_name, inst_name;
    ActivityOtherUserProjectBinding binding;
    List<String> users;
    ArrayList<String> member_list;
    FirebaseFirestore firestore;
    private static String username, base_url;
    TeamMemberAdapater teamMemberAdapater;
    ArrayList<String> avatarlist;
    HashMap<String, Object> hashMap;
    FirebaseStorage firebaseStorage;
    String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityOtherUserProjectBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        firestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        users = new ArrayList<>();
        member_list = new ArrayList<>();
        avatarlist = new ArrayList<>();

        project_name = getIntent().getStringExtra("proj_name");
        uid = getIntent().getStringExtra("uid");

        firestore.collection("Students").document(uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String email= ""+value.get("email");
                binding.reqBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                        emailIntent.setType("text/plain");
                        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{email});
                        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Request for contribution in project!!");
                        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hello I want to contribute in the project : "+project_name);
                        emailIntent.setType("message/rfc822");

                        try {
                            startActivity(Intent.createChooser(emailIntent,
                                    "Send email using..."));
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(OtherUserProjectActivity.this, "No email clients installed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        firestore.collection("Students").document(uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                username = "" + value.get("Github username");
                inst_name = "" + value.get("Institute");
                base_url = "https://github.com/" + username + "/" + project_name;
                binding.githubLink2.setText(base_url);
                String url = "https://api.github.com/repos/" + username + "/" + project_name + "/contributors?page=1&?access_token=fff";
                RequestQueue requestQueue = Volley.newRequestQueue(OtherUserProjectActivity.this);
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

                            Toast.makeText(OtherUserProjectActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        teamMemberAdapater = new TeamMemberAdapater(avatarlist, member_list, OtherUserProjectActivity.this);
                        binding.teamList2.setAdapter(teamMemberAdapater);

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
                        firestore.collection("Students").document(uid).collection("Projects").addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                for (DocumentSnapshot snapshot : value.getDocuments()) {
                                    if (snapshot.get("project_name").toString().equals(project_name)) {
                                        if (!snapshot.get("report").toString().equals("")) {
                                            binding.openReport2.setVisibility(View.VISIBLE);
                                        }
                                        binding.openReport2.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Uri webpage = Uri.parse("" + snapshot.get("report"));
                                                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                                                startActivity(intent);
                                            }
                                        });
                                        firestore.collection("Students").document(uid).collection("Projects").document(snapshot.getId()).update(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                        Toast.makeText(OtherUserProjectActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                requestQueue = Volley.newRequestQueue(OtherUserProjectActivity.this);
                requestQueue.add(stringRequest);
            }
        });
        firestore.collection("Students").document(uid).collection("Projects").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                users.clear();
                for (DocumentSnapshot snapshot : value.getDocuments()) {
                    users.add("" + snapshot.get("Github username"));
                }
            }
        });
    }
}