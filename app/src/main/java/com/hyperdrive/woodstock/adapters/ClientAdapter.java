package com.hyperdrive.woodstock.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.holders.ClientHolder;

import java.util.List;

public class ClientAdapter extends RecyclerView.Adapter<ClientHolder> {

    private final List<String> mClients;

    public ClientAdapter(List<String> mClients) {
        this.mClients = mClients;
    }

    @NonNull
    @Override
    public ClientHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ClientHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_client, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ClientHolder holder, int position) {
        holder.title.setText(mClients.get(position));

        holder.editButton.setOnClickListener(view -> updateItem(position));
        holder.deleteButton.setOnClickListener(view -> removerItem(position));
    }

    private void removerItem(int position) {

    }

    private void updateItem(int position) {

    }

    @Override
    public int getItemCount() {
        return mClients != null ? mClients.size() : 0;
    }
}

