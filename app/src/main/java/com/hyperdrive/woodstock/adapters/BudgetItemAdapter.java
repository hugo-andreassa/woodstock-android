package com.hyperdrive.woodstock.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.holders.BudgetItemHolder;
import com.hyperdrive.woodstock.models.BudgetItemModel;
import com.hyperdrive.woodstock.ui.budgetitem.BudgetItemActionFragment;
import com.hyperdrive.woodstock.ui.cuttingplan.CuttingPlanActivity;
import com.hyperdrive.woodstock.ui.project.ProjectActivity;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class BudgetItemAdapter extends RecyclerView.Adapter<BudgetItemHolder> {

    private static final String TAG = "BUDGET_ITEM_ACTIVITY";

    private final List<BudgetItemModel> mBudgetItems;
    private final Long mBudgetId;
    private final String mUserType;

    public BudgetItemAdapter(List<BudgetItemModel> mBudgetItems, Long budgetId, String mUserType) {
        this.mBudgetItems = mBudgetItems;
        this.mBudgetId = budgetId;
        this.mUserType = mUserType;
    }

    @NonNull
    @Override
    public BudgetItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BudgetItemHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_budget_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetItemHolder holder, int position) {
        BudgetItemModel budgetItem = mBudgetItems.get(position);

        if(!mUserType.equals("ADMIN")) {
            holder.value.setVisibility(View.GONE);
        }

        String number = String.format(
                Locale.forLanguageTag("BR"),
                "Item %d",
                position + 1);
        holder.number.setText(number);

        holder.value.setText(
                NumberFormat.getCurrencyInstance(
                        Locale.getDefault())
                        .format(budgetItem.getPrice()));

        String status = budgetItem.getStatus().substring(0, 1).toUpperCase(Locale.forLanguageTag("BR"))
                + budgetItem.getStatus().substring(1).toLowerCase(Locale.forLanguageTag("BR"));
        holder.status.setText(status);

        holder.description.setText(budgetItem.getDescription());

        holder.moreButton.setOnClickListener((v) -> {
            callFragment(v, budgetItem);
        });

        holder.projectsButton.setOnClickListener((v) -> {
            callProjectActivity(v, budgetItem.getId());
        });

        holder.cutPlanButton.setOnClickListener((v) -> {
            callCuttingPlanActivity(v, budgetItem.getId());
        });
    }

    @Override
    public int getItemCount() {
        return mBudgetItems != null ? mBudgetItems.size() : 0;
    }

    private void callFragment(View v, BudgetItemModel budgetItem) {
        AppCompatActivity activity = (AppCompatActivity) v.getContext();

        BudgetItemActionFragment budgetItemActionFragment =
                BudgetItemActionFragment.newInstance(mBudgetId, budgetItem, mUserType);

        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.budgetitem_fragment_layout, budgetItemActionFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void callCuttingPlanActivity(View v, Long id) {
            Intent intent = new Intent(v.getContext(), CuttingPlanActivity.class);
            intent.putExtra("budgetItemId", id);

            v.getContext().startActivity(intent);
    }

    private void callProjectActivity(View v, Long id) {
        Intent intent = new Intent(v.getContext(), ProjectActivity.class);
        intent.putExtra("budgetItemId", id);

        v.getContext().startActivity(intent);
    }

    public void updateData(List<BudgetItemModel> budgetItems) {
        this.mBudgetItems.clear();
        this.mBudgetItems.addAll(budgetItems);
        notifyDataSetChanged();
    }
}
