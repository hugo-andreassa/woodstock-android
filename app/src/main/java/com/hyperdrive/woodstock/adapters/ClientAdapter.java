package com.hyperdrive.woodstock.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.holders.ClientHolder;
import com.hyperdrive.woodstock.models.ClientModel;
import com.hyperdrive.woodstock.ui.budget.BudgetActivity;
import com.hyperdrive.woodstock.ui.client.ClientActionFragment;

import java.util.List;

public class ClientAdapter extends RecyclerView.Adapter<ClientHolder> {

    private static final String TAG = "CLIENT_ACTIVITY";

    private final List<ClientModel> mClients;
    private final String mUserType;

    public ClientAdapter(List<ClientModel> mClients, String mUserType) {
        this.mClients = mClients;
        this.mUserType = mUserType;
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

        if(!mUserType.equals("ADMIN")) {
            holder.moreButton.setVisibility(View.GONE);
        }

        holder.title.setText(client.getName());
        holder.budgetButton.setOnClickListener((v -> {
            callBudgetActivity(v, client);
        }));
        holder.moreButton.setOnClickListener((v -> {
            callFragment(v, client);
        }));
    }

    private void callBudgetActivity(View v, ClientModel client) {
        Intent intent = new Intent(v.getContext(), BudgetActivity.class);
        intent.putExtra("clientId", client.getId());
        intent.putExtra("clientName", client.getName());

        v.getContext().startActivity(intent);
    }

    private void callFragment(View v, ClientModel client) {
        AppCompatActivity activity = (AppCompatActivity) v.getContext();

        ClientActionFragment clientActionFragment = ClientActionFragment.newInstance(client);

        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.client_fragment_layout, clientActionFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public int getItemCount() {
        return mClients != null ? mClients.size() : 0;
    }

    public void updateData(List<ClientModel> clients) {
        this.mClients.clear();
        this.mClients.addAll(clients);
        notifyDataSetChanged();
    }
}

