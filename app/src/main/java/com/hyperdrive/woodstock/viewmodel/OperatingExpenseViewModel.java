package com.hyperdrive.woodstock.viewmodel;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hyperdrive.woodstock.api.config.RetrofitConfig;
import com.hyperdrive.woodstock.api.services.OperatingExpenseService;
import com.hyperdrive.woodstock.models.OperatingExpenseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OperatingExpenseViewModel extends ViewModel {
    private String TAG = "EXPENSE_VIEW_MODEL";

    private MutableLiveData<List<OperatingExpenseModel>> operatingExpenses;
    private ProgressBar progressBar;

    public LiveData<List<OperatingExpenseModel>> getOperatingExpenses(Long id, ProgressBar progressBar) {
        this.progressBar = progressBar;

        if (operatingExpenses == null) {
            operatingExpenses = new MutableLiveData<>();
        }

        progressBar.setVisibility(View.VISIBLE);
        loadOperatingExpensesFromApi(id);

        return operatingExpenses;
    }

    private void loadOperatingExpensesFromApi(Long id) {
        OperatingExpenseService operatingExpenseService =
                RetrofitConfig.getRetrofitInstance().create(OperatingExpenseService.class);
        Call<List<OperatingExpenseModel>> call = operatingExpenseService.findAll(id);

        call.enqueue(new Callback<List<OperatingExpenseModel>>() {
            @Override
            public void onResponse(Call<List<OperatingExpenseModel>> call, Response<List<OperatingExpenseModel>> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()) {
                    operatingExpenses.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<OperatingExpenseModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "onFailure findAll Operating Expense");
            }
        });

    }

}
