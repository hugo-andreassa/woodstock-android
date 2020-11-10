package com.hyperdrive.woodstock.viewmodel;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hyperdrive.woodstock.api.config.RetrofitConfig;
import com.hyperdrive.woodstock.api.services.CuttingPlanService;
import com.hyperdrive.woodstock.models.CuttingPlanModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CuttingPlanViewModel extends ViewModel {

    private String TAG = "CUTTING_PLAN_VIEW_MODEL";

    private MutableLiveData<List<CuttingPlanModel>> cuttingPlans;
    private ProgressBar progressBar;

    public LiveData<List<CuttingPlanModel>> getCuttingPlans(Long id, ProgressBar progressBar) {
        this.progressBar = progressBar;
        if (cuttingPlans == null) {
            cuttingPlans = new MutableLiveData<>();
        }

        progressBar.setVisibility(View.VISIBLE);
        loadCuttingPlansFromApi(id);

        return cuttingPlans;
    }

    private void loadCuttingPlansFromApi(Long id) {
        CuttingPlanService cuttingPlanService = RetrofitConfig.getRetrofitInstance().create(CuttingPlanService.class);
        Call<List<CuttingPlanModel>> call = cuttingPlanService.findAll(id);

        call.enqueue(new Callback<List<CuttingPlanModel>>() {
            @Override
            public void onResponse(Call<List<CuttingPlanModel>> call, Response<List<CuttingPlanModel>> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()) {
                    cuttingPlans.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<CuttingPlanModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "onFailure findAll Cutting Plan");
            }
        });

    }
}
