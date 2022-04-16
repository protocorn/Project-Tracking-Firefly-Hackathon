package com.example.projecttracker.Adapater;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projecttracker.R;

import java.util.List;

public class AddTeamAdapter extends RecyclerView.Adapter<AddTeamAdapter.viewholder> {
    Context context;
    List<String> list;

    public AddTeamAdapter(List<String> list, Context context) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AddTeamAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_students, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddTeamAdapter.viewholder holder, int position) {
        holder.username.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView username;
        ImageView add_user1, profile;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.git_username);
            add_user1 = itemView.findViewById(R.id.add_user);
            profile = itemView.findViewById(R.id.user_prof);
        }
    }
}
