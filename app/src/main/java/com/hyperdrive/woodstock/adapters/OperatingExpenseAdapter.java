package com.hyperdrive.woodstock.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.holders.OperatingExpenseHolder;
import com.hyperdrive.woodstock.models.OperatingExpenseModel;
import com.hyperdrive.woodstock.utils.DateUtil;

import java.util.List;
import java.util.Locale;

public class OperatingExpenseAdapter extends RecyclerView.Adapter<OperatingExpenseHolder> {

    private static final String TAG = "OPERATING_EXPENSE_ACTIVITY";

    private final List<OperatingExpenseModel> mOperatingExpenses;
    private final Long mCompanyId;

    public OperatingExpenseAdapter(List<OperatingExpenseModel> mOperatingExpenses, Long mCompanyId) {
        this.mOperatingExpenses = mOperatingExpenses;
        this.mCompanyId = mCompanyId;
    }

    @NonNull
    @Override
    public OperatingExpenseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OperatingExpenseHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_operating_expense, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OperatingExpenseHolder holder, int position) {
        OperatingExpenseModel operatingExpense = mOperatingExpenses.get(position);

        holder.name.setText(operatingExpense.getName());

        String date = DateUtil.parseDateFromUtc(
                operatingExpense.getCreationDate(),
                "d MMMM, yyyy HH:mm:ss");
        holder.creationDate.setText(date);

        holder.value.setText(String.format(
                Locale.getDefault(),
                "R$ %.2f",
                operatingExpense.getValue()));

        holder.moreButton.setOnClickListener((v) -> {
            Toast.makeText(v.getContext(), "Socorro", Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public int getItemCount() {
        return mOperatingExpenses != null ? mOperatingExpenses.size() : 0;
    }
}
