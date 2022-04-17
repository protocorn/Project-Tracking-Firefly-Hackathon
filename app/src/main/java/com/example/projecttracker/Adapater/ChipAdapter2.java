package com.example.projecttracker.Adapater;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projecttracker.R;
import com.google.android.material.chip.Chip;

import java.util.List;

public class ChipAdapter2 extends RecyclerView.Adapter<ChipAdapter2.viewholder> {
    Context context;
    List<String> list;

    public ChipAdapter2(List<String> list, Context context) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chip2, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        if (!list.isEmpty()) {
            holder.chip.setText(list.get(position));
        }
        if (list.get(position).length() <= 8) {
            holder.chip.setChipBackgroundColor(ColorStateList.valueOf(Color.rgb(0, 188, 212)));
        } else if (list.get(position).length() <= 13 && list.get(position).length() > 8) {
            holder.chip.setChipBackgroundColor(ColorStateList.valueOf(Color.rgb(255, 145, 0)));
        } else if (list.get(position).length() <= 17 && list.get(position).length() > 13) {
            holder.chip.setChipBackgroundColor(ColorStateList.valueOf(Color.rgb(0, 150, 136)));
        } else if (list.get(position).length() <= 21 && list.get(position).length() > 17) {
            holder.chip.setChipBackgroundColor(ColorStateList.valueOf(Color.rgb(139, 195, 74)));
        } else if (list.get(position).length() <= 25 && list.get(position).length() > 21) {
            holder.chip.setChipBackgroundColor(ColorStateList.valueOf(Color.rgb(103, 58, 183)));
        } else {
            holder.chip.setChipBackgroundColor(ColorStateList.valueOf(Color.rgb(233, 30, 100)));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        Chip chip;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            chip = itemView.findViewById(R.id.chip_1);
        }
    }
}

