package com.hyperdrive.woodstock.holders;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hyperdrive.woodstock.R;

public class ClientHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public ImageButton budgetButton;
    public ImageButton moreButton;

    public ClientHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.list_client_name);
        budgetButton = itemView.findViewById(R.id.client_budget_button);
        moreButton = itemView.findViewById(R.id.client_more_button);
    }
}
