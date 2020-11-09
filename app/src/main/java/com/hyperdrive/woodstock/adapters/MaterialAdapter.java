package com.hyperdrive.woodstock.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.holders.MaterialHolder;
import com.hyperdrive.woodstock.models.MaterialModel;
import com.hyperdrive.woodstock.ui.material.MaterialActionFragment;
import com.hyperdrive.woodstock.utils.DateUtil;

import java.util.List;
import java.util.Locale;

public class MaterialAdapter extends RecyclerView.Adapter<MaterialHolder> {

    private Context mContext;
    private List<MaterialModel> mMaterials;
    private Long mCompanyId;

    public MaterialAdapter(Context mContext, List<MaterialModel> mMaterials, Long mCompanyId) {
        this.mContext = mContext;
        this.mMaterials = mMaterials;
        this.mCompanyId = mCompanyId;
    }

    @NonNull
    @Override
    public MaterialHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MaterialHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_material, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MaterialHolder holder, int position) {
        MaterialModel material = mMaterials.get(position);

        if(material.getStock() < material.getMinimumStock()) {
            holder.cardView.setCardBackgroundColor(
                    mContext.getResources().getColor(R.color.errorColor));
            holder.warning.setVisibility(View.VISIBLE);
        } else if (holder.warning.getVisibility() == View.VISIBLE) {
            holder.cardView.setCardBackgroundColor(
                    mContext.getResources().getColor(R.color.whiteColor));
            holder.warning.setVisibility(View.GONE);
        }

        holder.name.setText(
                String.format(Locale.getDefault(),
                "Item %d - %s",
                position+1, material.getName()
        ));

        holder.quantity.setText(
                String.format(Locale.getDefault(),
                        "Quantidade em estoque: %d %s",
                        material.getStock(), material.getUnit()
                ));

        String date = DateUtil.parseDateFromUtc(
                material.getLastUpdate(),
                "d MMMM");
        holder.lastUpdate.setText(String.format(Locale.getDefault(),
                "Última atualização: %s",
                date
        ));

        holder.moreButton.setOnClickListener(v -> {
            callFragment(material, v);
        });
    }

    @Override
    public int getItemCount() {
        return mMaterials != null ? mMaterials.size() : 0;
    }

    private void callFragment(MaterialModel material, View view) {
        AppCompatActivity activity = (AppCompatActivity) view.getContext();

        MaterialActionFragment materialActionFragment =
                MaterialActionFragment.newInstance(mCompanyId, material);

        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.material_fragment_layout, materialActionFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void updateData(List<MaterialModel> materials) {
        this.mMaterials.clear();
        this.mMaterials.addAll(materials);
        notifyDataSetChanged();
    }
}
