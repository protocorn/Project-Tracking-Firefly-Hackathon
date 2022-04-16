package com.example.projecttracker.Adapater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projecttracker.R;

import java.util.ArrayList;
import java.util.List;

public class TeamMemberAdapater extends RecyclerView.Adapter<TeamMemberAdapater.viewholder>{
    Context context;
    ArrayList<String> list;
    ArrayList<String> list2;

    public TeamMemberAdapater(ArrayList<String>list2,ArrayList<String> list, Context context) {
        this.context = context;
        this.list = list;
        this.list2 = list2;
    }

    @NonNull
    @Override
    public TeamMemberAdapater.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_members, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamMemberAdapater.viewholder holder, int position) {
        holder.username.setText(list.get(position));
        Glide.with(context).load(list2.get(position)).into(holder.profile);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView username;
        ImageView profile;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.git_username1);
            profile = itemView.findViewById(R.id.user_prof1);
        }
    }
}
