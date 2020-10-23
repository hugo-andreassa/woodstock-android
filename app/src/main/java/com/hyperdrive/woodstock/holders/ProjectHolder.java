package com.hyperdrive.woodstock.holders;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hyperdrive.woodstock.R;

public class ProjectHolder extends RecyclerView.ViewHolder {

    public ImageView image;
    public TextView tittle;
    public TextView comment;
    public ImageButton moreButton;

    public ProjectHolder(@NonNull View itemView) {
        super(itemView);

        image = itemView.findViewById(R.id.project_list_image);
        tittle = itemView.findViewById(R.id.project_list_title);
        comment = itemView.findViewById(R.id.project_list_comment);
        moreButton = itemView.findViewById(R.id.project_list_more_button);
    }
}
