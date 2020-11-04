package com.hyperdrive.woodstock.holders;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hyperdrive.woodstock.R;

import org.w3c.dom.Text;

public class MaterialHolder extends RecyclerView.ViewHolder {

    public CardView cardView;
    public TextView warning;
    public TextView name;
    public TextView quantity;
    public TextView lastUpdate;
    public ImageButton moreButton;

    public MaterialHolder(@NonNull View itemView) {
        super(itemView);

        cardView = itemView.findViewById(R.id.material_list_cardview);
        warning = itemView.findViewById(R.id.material_list_warning);
        name = itemView.findViewById(R.id.material_list_name);
        quantity = itemView.findViewById(R.id.material_list_quantity);
        lastUpdate = itemView.findViewById(R.id.material_list_last_update);
        moreButton = itemView.findViewById(R.id.material_list_more_button);
    }
}
