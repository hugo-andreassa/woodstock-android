package com.hyperdrive.woodstock.holders;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hyperdrive.woodstock.R;

public class BudgetHolder extends RecyclerView.ViewHolder {

    public TextView creationDate;
    public TextView status;
    public ImageButton moreButton;
    public ImageButton deleteButton;

    public BudgetHolder(@NonNull View itemView) {
        super(itemView);

        creationDate = itemView.findViewById(R.id.budget_list_creation_date);
        status = itemView.findViewById(R.id.budget_list_status);
        moreButton = itemView.findViewById(R.id.budget_list_more_button);
        deleteButton = itemView.findViewById(R.id.budget_list_delete_button);
    }
}
