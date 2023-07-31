package com.example.hostelbiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hostelbiz.ui.User;

import java.util.ArrayList;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
    private ArrayList<User> studentNames;
    private Context context;

    public StudentAdapter(Context context, ArrayList<User> studentNames) {
        this.studentNames = studentNames;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = studentNames.get(position);
        holder.name.setText(user.getName());
        holder.mobile.setText(user.getMobile());
        holder.address.setText(user.getAddress());
    }

    @Override
    public int getItemCount() {
        return studentNames.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, mobile, address;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameTv);
            mobile = itemView.findViewById(R.id.mobileTv);
            address = itemView.findViewById(R.id.addressTv);
        }
    }
}
