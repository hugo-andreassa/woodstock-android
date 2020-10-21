package com.hyperdrive.woodstock.holders;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hyperdrive.woodstock.R;

public class CuttingPlanHolder extends RecyclerView.ViewHolder {

    public TextView quantity;
    public TextView measures;
    public TextView comment;
    public View pieceDesign;
    public ImageButton moreButton;

    public CuttingPlanHolder(@NonNull View itemView) {
        super(itemView);

        quantity = itemView.findViewById(R.id.cuttingplan_list_quantity);
        measures = itemView.findViewById(R.id.cuttingplan_list_measures);
        comment = itemView.findViewById(R.id.cuttingplan_list_comment);
        pieceDesign =itemView.findViewById(R.id.cuttingplan_view);
        moreButton = itemView.findViewById(R.id.cuttingplan_list_more_button);
    }
}
