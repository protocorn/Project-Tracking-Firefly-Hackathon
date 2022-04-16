package com.example.projecttracker.Adapater;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projecttracker.ProjectDeatilActivity;
import com.example.projecttracker.R;

import java.util.ArrayList;

public class MyProjectsAdapter extends RecyclerView.Adapter<MyProjectsAdapter.ViewHolder>{
    ArrayList<String> projects;
    Context con;

    public MyProjectsAdapter(Context context, ArrayList<String> projects) {
        super();
        this.con = context;
        this.projects=projects;
    }

    @NonNull
    @Override
    public MyProjectsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemsView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.project_sample, parent, false);

        return new ViewHolder(itemsView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyProjectsAdapter.ViewHolder holder, int position) {
        holder.title.setText(projects.get(position));
        holder.view_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(con, ProjectDeatilActivity.class);
                intent.putExtra("proj_name",projects.get(position));
                con.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView view_btn1;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.project_title2);
            view_btn1=itemView.findViewById(R.id.view_btn);
        }
    }
}
