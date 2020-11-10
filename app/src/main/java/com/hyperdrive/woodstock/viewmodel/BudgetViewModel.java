package com.hyperdrive.woodstock.viewmodel;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hyperdrive.woodstock.api.config.RetrofitConfig;
import com.hyperdrive.woodstock.api.services.BudgetService;
import com.hyperdrive.woodstock.api.services.ClientService;
import com.hyperdrive.woodstock.models.BudgetModel;
import com.hyperdrive.woodstock.models.ClientModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BudgetViewModel  extends ViewModel {

    private String TAG = "BUDGET_VIEW_MODEL";

    private MutableLiveData<List<BudgetModel>> budgets;
    private ProgressBar progressBar;

    public LiveData<List<BudgetModel>> getBudgets(Long id, ProgressBar progressBar) {
        this.progressBar = progressBar;

        if (budgets == null) {
            budgets = new MutableLiveData<>();
        }

        progressBar.setVisibility(View.VISIBLE);
        loadBudgetsFromApi(id);

        return budgets;
    }

    private void loadBudgetsFromApi(Long id) {
        BudgetService budgetService = RetrofitConfig.getRetrofitInstance().create(BudgetService.class);
        Call<List<BudgetModel>> call = budgetService.findAll(id);
        call.enqueue(new Callback<List<BudgetModel>>() {
            @Override
            public void onResponse(Call<List<BudgetModel>> call, Response<List<BudgetModel>> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()) {
                    budgets.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<BudgetModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "onFailure findAll Budgets");
            }
        });

    }
}
