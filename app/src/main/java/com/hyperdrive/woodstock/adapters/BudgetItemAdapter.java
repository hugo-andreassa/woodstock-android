package com.hyperdrive.woodstock.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.holders.BudgetItemHolder;
import com.hyperdrive.woodstock.models.BudgetItemModel;
import com.hyperdrive.woodstock.models.BudgetModel;
import com.hyperdrive.woodstock.ui.budget.BudgetActionFragment;
import com.hyperdrive.woodstock.ui.budgetitem.BudgetItemActionFragment;

import java.util.List;
import java.util.Locale;

public class BudgetItemAdapter extends RecyclerView.Adapter<BudgetItemHolder> {

    private static final String TAG = "BUDGET_ITEM_ACTIVITY";

    private final List<BudgetItemModel> mBudgetItems;
    private final Long budgetId;

    public BudgetItemAdapter(List<BudgetItemModel> mBudgetItems, Long budgetId) {
        this.mBudgetItems = mBudgetItems;
        this.budgetId = budgetId;
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

        String environment = String.format(
                Locale.forLanguageTag("BR"),
                "Item %d - %s (%s)",
                position + 1,
                budgetItem.getEnvironment().isEmpty() ? "#" : budgetItem.getEnvironment(),
                budgetItem.getStatus());
        holder.environment.setText(environment);

        holder.description.setText(budgetItem.getDescription());

        holder.moreButton.setOnClickListener((v) -> {
            callFragment(v, budgetItem);
        });

        holder.projectsButton.setOnClickListener((v) -> {
            Toast.makeText(v.getContext(), "Projets Button Clicked", Toast.LENGTH_LONG).show();
        });

        holder.cutPlanButton.setOnClickListener((v) -> {
            Toast.makeText(v.getContext(), "Cut Plan Button Clicked", Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public int getItemCount() {
        return mBudgetItems != null ? mBudgetItems.size() : 0;
    }

    private void callFragment(View v, BudgetItemModel budgetItem) {
        AppCompatActivity activity = (AppCompatActivity) v.getContext();

        BudgetItemActionFragment budgetItemActionFragment = BudgetItemActionFragment.newInstance(budgetId, budgetItem);

        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.budgetitem_fragment_layout, budgetItemActionFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void updateData(List<BudgetItemModel> budgetItems) {
        this.mBudgetItems.clear();
        this.mBudgetItems.addAll(budgetItems);
        notifyDataSetChanged();
    }
}
