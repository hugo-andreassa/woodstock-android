package com.hyperdrive.woodstock.viewmodel;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hyperdrive.woodstock.api.config.RetrofitConfig;
import com.hyperdrive.woodstock.api.services.BudgetItemService;
import com.hyperdrive.woodstock.api.services.BudgetService;
import com.hyperdrive.woodstock.models.BudgetItemModel;
import com.hyperdrive.woodstock.models.BudgetModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BudgetItemViewModel  extends ViewModel {

    private String TAG = "BUDGET_ITEM_VIEW_MODEL";

    private MutableLiveData<List<BudgetItemModel>> budgetItems;
    private ProgressBar progressBar;

    public LiveData<List<BudgetItemModel>> getBudgetItems(Long id, ProgressBar progressBar) {
        this.progressBar = progressBar;
        if (budgetItems == null) {
            budgetItems = new MutableLiveData<>();
        }

        progressBar.setVisibility(View.VISIBLE);
        loadBudgetItemsFromApi(id);

        return budgetItems;
    }

    private void loadBudgetItemsFromApi(Long id) {
        BudgetItemService budgetItemService = RetrofitConfig.getRetrofitInstance().create(BudgetItemService.class);
        Call<List<BudgetItemModel>> call = budgetItemService.findAll(id);

        call.enqueue(new Callback<List<BudgetItemModel>>() {
            @Override
            public void onResponse(Call<List<BudgetItemModel>> call, Response<List<BudgetItemModel>> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()) {
                    budgetItems.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<BudgetItemModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "onFailure findAll Budget Items");
            }
        });

    }
}
