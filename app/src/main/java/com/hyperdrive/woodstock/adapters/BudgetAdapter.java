package com.hyperdrive.woodstock.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.api.config.RetrofitConfig;
import com.hyperdrive.woodstock.api.services.BudgetService;
import com.hyperdrive.woodstock.api.services.ClientService;
import com.hyperdrive.woodstock.holders.BudgetHolder;
import com.hyperdrive.woodstock.models.BudgetModel;
import com.hyperdrive.woodstock.models.ClientModel;
import com.hyperdrive.woodstock.ui.budget.BudgetActionFragment;
import com.hyperdrive.woodstock.ui.client.ClientActionFragment;
import com.hyperdrive.woodstock.utils.SnackbarUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetHolder> {

    private final String SERVER_ERROR_DELETE = "Erro ao deletar Orçamento...";
    private final String OK_DELETE = "Orçamento deletado com sucesso";
    private static final String TAG = "BUDGET_ACTIVITY";

    private final List<BudgetModel> mBudgets;
    private final String auth;
    private final Long clientId;

    public BudgetAdapter(List<BudgetModel> mBudgets, String auth, Long clientId) {
        this.mBudgets = mBudgets;
        this.auth = auth;
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

        holder.status.setText(budget.getStatus());
        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = sdf1.parse(budget.getCreationDate());
            holder.creationDate.setText(sdf2.format(date));
        } catch (ParseException e) {
            holder.creationDate.setText(budget.getCreationDate());
            Log.e(TAG, e.getMessage());
        }

        holder.deleteButton.setOnClickListener((v -> {
            deleteBudget(v, position);
        }));

        holder.moreButton.setOnClickListener((v -> {
            callFragment(v, budget);
        }));
    }

    private void callFragment(View v, BudgetModel budgetModel) {
        AppCompatActivity activity = (AppCompatActivity) v.getContext();

        BudgetActionFragment budgetActionFragment = BudgetActionFragment.newInstance(budgetModel, clientId);

        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.budget_fragment_layout, budgetActionFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void deleteBudget(View v, int position) {
        BudgetModel BudgetModel = mBudgets.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage("Deseja mesmo excluir este orçamento?")
                .setPositiveButton("Sim", (dialog, id) -> {
                    deleteBudgetFromApi(BudgetModel.getId(), v, position);
                })
                .setNegativeButton("Cancelar", (dialog, id) -> {
                    dialog.dismiss();
                });

        Dialog dialog = builder.create();
        dialog.show();
    }

    private void deleteBudgetFromApi(Long id, View v, int position) {
        BudgetService budgetService = RetrofitConfig.getRetrofitInstance().create(BudgetService.class);
        Call<Void> call = budgetService.delete(id, auth);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    updateRecyclerView(position);
                    SnackbarUtil.showSuccess((AppCompatActivity) v.getContext(), OK_DELETE);
                } else {
                    SnackbarUtil.showError((AppCompatActivity) v.getContext(), SERVER_ERROR_DELETE);
                    Log.e(TAG, response.toString());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                SnackbarUtil.showError((AppCompatActivity) v.getContext(), SERVER_ERROR_DELETE);
                Log.e(TAG, t.getMessage());
            }
        });
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
