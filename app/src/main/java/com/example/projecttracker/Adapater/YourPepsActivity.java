package com.example.projecttracker.Adapater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import com.example.projecttracker.Models.Users;
import com.example.projecttracker.R;
import com.example.projecttracker.databinding.ActivityYourPepesBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class YourPepsActivity extends AppCompatActivity {
    ActivityYourPepesBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    ArrayList<Users> list;
    UserAdapter adapter;
    String inst_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityYourPepesBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        list = new ArrayList<>();

        adapter = new UserAdapter(list, YourPepsActivity.this);
        binding.pepsList.setAdapter(adapter);

        firestore.collection("Students").document(auth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                inst_name = "" + value.get("Institute");
                binding.searchPeps.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                        Query query = firestore.collection("Students").orderBy("name").startAt(s.toString()).endAt(s.toString() + "\uf8ff");
                        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                list.clear();
                                for (DocumentSnapshot value1 : value.getDocuments()) {
                                    if (value1.get("Institute").equals(inst_name)) {
                                        Users users = value1.toObject(Users.class);
                                        users.setUserid(value1.getId());
                                        if (!auth.getCurrentUser().getUid().equals(value1.getId())) {
                                            list.add(users);
                                        }
                                    }
                                }
                                adapter.notifyDataSetChanged();

                            }
                        });

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                firestore.collection("Institutes").document(inst_name).collection("students").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        list.clear();
                        for(DocumentSnapshot snapshot:value.getDocuments()){
                            Users users = snapshot.toObject(Users.class);
                            users.setUserid(snapshot.getId());
                            if (!users.getUserid().equals(FirebaseAuth.getInstance().getUid())) {
                                list.add(users);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });

    }
}