package com.hyperdrive.woodstock.holders;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hyperdrive.woodstock.R;

public class BudgetHolder extends RecyclerView.ViewHolder {

    public TextView number;
    public TextView status;
    public TextView creationDate;
    public ImageButton moreButton;
    public ImageButton itensButton;

    public BudgetHolder(@NonNull View itemView) {
        super(itemView);

        number = itemView.findViewById(R.id.budget_list_number);
        status = itemView.findViewById(R.id.budget_list_status);
        creationDate = itemView.findViewById(R.id.budget_list_creation_date);
        moreButton = itemView.findViewById(R.id.budget_list_more_button);
        itensButton = itemView.findViewById(R.id.budget_list_itens_button);
    }
}
