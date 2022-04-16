package com.example.projecttracker.Adapater;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projecttracker.MainActivity;
import com.example.projecttracker.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;

public class GitProjectAdapter extends RecyclerView.Adapter<GitProjectAdapter.ViewHolder> {
    ArrayList<String> projects;
    Context con;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    String inst_name;

    public GitProjectAdapter(Context context, ArrayList<String> projects) {
        super();
        this.con = context;
        this.projects = projects;
    }

    @NonNull
    @Override
    public GitProjectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemsView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.gitprojects_sample, parent, false);

        return new ViewHolder(itemsView);
    }

    @Override
    public void onBindViewHolder(@NonNull GitProjectAdapter.ViewHolder holder, int position) {
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        holder.title.setText(projects.get(position));
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("project_name", projects.get(position));

                firestore.collection("Students").document(auth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        inst_name=""+value.get("Institute");
                    }
                });

                firestore.collection("Students").document(auth.getCurrentUser().getUid())
                        .collection("Projects")
                        .document(String.valueOf(System.currentTimeMillis()))
                        .set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        firestore.collection("Institutes").document(inst_name).collection("Projects").document(String.valueOf(System.currentTimeMillis())).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                firestore.collection("AllProjects").document(String.valueOf(System.currentTimeMillis())).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(con, "Project Added Successfully", Toast.LENGTH_SHORT).show();
                                        con.startActivity(new Intent(con, MainActivity.class));
                                    }
                                });
                            }
                        });
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView add;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.project_title);
            add = itemView.findViewById(R.id.add_btn);
        }
    }
}
