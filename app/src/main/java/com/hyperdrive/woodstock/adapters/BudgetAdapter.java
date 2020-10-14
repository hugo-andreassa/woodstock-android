package com.hyperdrive.woodstock.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.holders.BudgetHolder;
import com.hyperdrive.woodstock.models.BudgetModel;

import java.util.List;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetHolder> {

    private final String SERVER_ERROR_DELETE = "Erro ao deletar Orçamento...";
    private static final String TAG = "BUDGET_ACTIVITY";

    private final List<BudgetModel> mBudgets;
    private final String auth;

    public BudgetAdapter(List<BudgetModel> mBudgets, String auth) {
        this.mBudgets = mBudgets;
        this.auth = auth;
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

        holder.creationDate.setText(budget.getCreationDate().toString());
        holder.status.setText(budget.getStatus());

        holder.deleteButton.setOnClickListener((v -> {

        }));

        holder.moreButton.setOnClickListener((v -> {

        }));
    }

    private void updateRecyclerView(int position) {
        mBudgets.remove(position);
        this.notifyItemRemoved(position);
        this.notifyItemRangeChanged(position, mBudgets.size());
    }

    @Override
    public int getItemCount() {
        return mBudgets != null ? mBudgets.size() : 0;
    }
}
