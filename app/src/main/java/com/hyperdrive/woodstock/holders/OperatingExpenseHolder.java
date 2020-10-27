package com.hyperdrive.woodstock.holders;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hyperdrive.woodstock.R;

public class OperatingExpenseHolder extends RecyclerView.ViewHolder {

    public TextView name;
    public TextView creationDate;
    public TextView value;
    public ImageButton moreButton;

    public OperatingExpenseHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.operatingexpense_list_name);
        creationDate = itemView.findViewById(R.id.operatingexpense_list_creationdate);
        value = itemView.findViewById(R.id.operatingexpense_list_value);
        moreButton = itemView.findViewById(R.id.operatingexpense_list_more_button);
    }
}
