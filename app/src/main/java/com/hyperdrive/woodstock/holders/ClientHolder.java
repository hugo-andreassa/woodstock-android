package com.hyperdrive.woodstock.holders;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hyperdrive.woodstock.R;

public class ClientHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public ImageButton moreButton;
    public ImageButton editButton;
    public ImageButton deleteButton;

    public ClientHolder(@NonNull View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.list_client_name);
    }
}
