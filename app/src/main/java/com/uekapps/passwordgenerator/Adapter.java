package com.uekapps.passwordgenerator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> implements Filterable {
    Context context;
    Activity activity;
    List<Model> passwordList;
    List<Model> newList;

    public Adapter(Context context, Activity activity, List<Model> passwordList) {
        this.context = context;
        this.activity = activity;
        this.passwordList = passwordList;
        newList = new ArrayList<>(passwordList);
    }

    @Override
    public Filter getFilter() {
        return myFilter;
    }

    private Filter myFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Model> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(newList);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (Model item : newList) {
                    if (item.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            passwordList.clear();
            passwordList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.title.setText(passwordList.get(position).getTitle());
        holder.password.setText(passwordList.get(position).getPassword());

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdatePasswordActivity.class);
                intent.putExtra("title", passwordList.get(position).getTitle());
                intent.putExtra("password", passwordList.get(position).getPassword());
                intent.putExtra("id", passwordList.get(position).getId());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return passwordList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, password;
        RelativeLayout relativeLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            password = itemView.findViewById(R.id.password);
            relativeLayout = itemView.findViewById(R.id.password_layout);
        }
    }

    public List<Model> getList() {
        return passwordList;
    }

    public void removeItem(int position) {
        passwordList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Model item, int position) {
        passwordList.add(position, item);
        notifyItemInserted(position);
    }
}
