package com.hyperdrive.woodstock.holders;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hyperdrive.woodstock.R;

public class UserHolder extends RecyclerView.ViewHolder {

    public TextView name;
    public TextView status;
    public TextView type;
    public ImageButton moreButton;

    public UserHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.user_list_name);
        status = itemView.findViewById(R.id.user_list_status);
        type = itemView.findViewById(R.id.user_list_type);
        moreButton = itemView.findViewById(R.id.user_list_more_button);
    }
}
