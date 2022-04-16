package com.example.projecttracker.Adapater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projecttracker.Models.Users;
import com.example.projecttracker.R;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

public class UserAdapter  extends RecyclerView.Adapter<UserAdapter.viewholder>{
    Context context;
    ArrayList<Users> list;

    public UserAdapter(ArrayList<Users> list, Context context) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public UserAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_users, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.viewholder holder, int position) {
        Users users = list.get(position);
        holder.username.setText(users.getUserid());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView username;
        ImageView profile1;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username1);
            profile1 = itemView.findViewById(R.id.profile);
        }
    }
}
