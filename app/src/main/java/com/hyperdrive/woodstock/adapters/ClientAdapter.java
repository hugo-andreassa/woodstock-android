package com.hyperdrive.woodstock.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.holders.ClientHolder;
import com.hyperdrive.woodstock.models.ClientModel;

import java.util.List;

public class ClientAdapter extends RecyclerView.Adapter<ClientHolder> {

    private final List<ClientModel> mClients;

    public ClientAdapter(List<ClientModel> mClients) {
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
        ClientModel client = mClients.get(position);
        holder.title.setText(client.getName());
    }

    @Override
    public int getItemCount() {
        return mClients != null ? mClients.size() : 0;
    }
}

