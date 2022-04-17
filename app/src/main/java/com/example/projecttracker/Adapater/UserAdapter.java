package com.example.projecttracker.Adapater;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projecttracker.Models.Users;
import com.example.projecttracker.R;
import com.example.projecttracker.UserProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

public class UserAdapter  extends RecyclerView.Adapter<UserAdapter.viewholder>{
    Context context;
    ArrayList<Users> list;
    FirebaseFirestore firestore;

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
        firestore=FirebaseFirestore.getInstance();

        firestore.collection("Students").document(users.getUserid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                holder.username.setText(""+value.get("name"));
            }
        });

        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, UserProfileActivity.class);
                intent.putExtra("uid",users.getUserid());
                context.startActivity(intent);
            }
        });
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
