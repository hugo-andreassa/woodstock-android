package com.hyperdrive.woodstock.holders;

import android.media.Image;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hyperdrive.woodstock.R;

public class BudgetItemHolder extends RecyclerView.ViewHolder {

    public TextView number;
    public TextView value;
    public TextView status;
    public TextView description;
    public ImageButton moreButton;
    public ImageButton projectsButton;
    public ImageButton cutPlanButton;

    public BudgetItemHolder(@NonNull View itemView) {
        super(itemView);

        number = itemView.findViewById(R.id.budgetitem_list_number);
        value = itemView.findViewById(R.id.budgetitem_list_value);
        status = itemView.findViewById(R.id.budgetitem_list_status);
        description = itemView.findViewById(R.id.budgetitem_list_description);
        moreButton = itemView.findViewById(R.id.budgetitem_list_more_button);
        projectsButton = itemView.findViewById(R.id.budgetitem_list_projects_button);
        cutPlanButton = itemView.findViewById(R.id.budgetitem_list_cut_button);
    }
}

