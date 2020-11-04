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
import com.hyperdrive.woodstock.holders.BudgetHolder;
import com.hyperdrive.woodstock.models.BudgetModel;
import com.hyperdrive.woodstock.ui.budget.BudgetActionFragment;
import com.hyperdrive.woodstock.ui.budgetitem.BudgetItemActivity;
import com.hyperdrive.woodstock.utils.DateUtil;

import java.util.List;
import java.util.Locale;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetHolder> {

    private static final String TAG = "BUDGET_ACTIVITY";

    private final List<BudgetModel> mBudgets;
    private final Long clientId;

    public BudgetAdapter(List<BudgetModel> mBudgets, Long clientId) {
        this.mBudgets = mBudgets;
        this.clientId = clientId;
    }

    @NonNull
    @Override
    public BudgetHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BudgetHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_budget, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetHolder holder, int position) {
        BudgetModel budget = mBudgets.get(position);

        String status = String.format(
                Locale.forLanguageTag("BR"),
                "Orçamento %d",
                position + 1);
        holder.number.setText(status);

        holder.status.setText(budget.getStatus());

        String date = DateUtil.parseDateFromUtc(
                budget.getCreationDate(),
                "d MMMM, yyyy HH:mm:ss");
        holder.creationDate.setText(date);

        holder.moreButton.setOnClickListener((v) -> {
            callFragment(v, budget);
        });

        holder.itensButton.setOnClickListener((v) -> {
            callBudgetItemActivity(v, budget.getId());
        });
    }

    @Override
    public int getItemCount() {
        return mBudgets != null ? mBudgets.size() : 0;
    }

    private void callBudgetItemActivity(View v, Long id) {
        Intent intent = new Intent(v.getContext(), BudgetItemActivity.class);
        intent.putExtra("budgetId", id);

        v.getContext().startActivity(intent);
    }

    private void callFragment(View v, BudgetModel budgetModel) {
        AppCompatActivity activity = (AppCompatActivity) v.getContext();

        BudgetActionFragment budgetActionFragment = BudgetActionFragment.newInstance(budgetModel, clientId);

        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.budget_fragment_layout, budgetActionFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void updateData(List<BudgetModel> budgets) {
        this.mBudgets.clear();
        this.mBudgets.addAll(budgets);
        notifyDataSetChanged();
    }
}
