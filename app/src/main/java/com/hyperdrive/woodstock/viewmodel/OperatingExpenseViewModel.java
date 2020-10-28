package com.hyperdrive.woodstock.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hyperdrive.woodstock.api.config.RetrofitConfig;
import com.hyperdrive.woodstock.api.services.OperatingExpenseService;
import com.hyperdrive.woodstock.models.OperatingExpenseModel;
import com.hyperdrive.woodstock.persistence.Preferences;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OperatingExpenseViewModel extends ViewModel {
    private String TAG = "EXPENSE_VIEW_MODEL";

    private MutableLiveData<List<OperatingExpenseModel>> operatingExpenses;

    public LiveData<List<OperatingExpenseModel>> getOperatingExpenses(Long id, String auth) {
        if (operatingExpenses == null) {
            operatingExpenses = new MutableLiveData<>();
        }

        loadOperatingExpensesFromApi(id, auth);

        return operatingExpenses;
    }

    private void loadOperatingExpensesFromApi(Long id, String auth) {
        OperatingExpenseService operatingExpenseService =
                RetrofitConfig.getRetrofitInstance().create(OperatingExpenseService.class);
        Call<List<OperatingExpenseModel>> call = operatingExpenseService.findAll(id, auth);

        call.enqueue(new Callback<List<OperatingExpenseModel>>() {
            @Override
            public void onResponse(Call<List<OperatingExpenseModel>> call, Response<List<OperatingExpenseModel>> response) {
                if(response.isSuccessful()) {
                    operatingExpenses.setValue(response.body());
                } else {
                    Log.e(TAG, "Deu merda");
                }
            }

            @Override
            public void onFailure(Call<List<OperatingExpenseModel>> call, Throwable t) {
                Log.e(TAG, "onFailure findAll Operating Expense");
            }
        });

    }

}
